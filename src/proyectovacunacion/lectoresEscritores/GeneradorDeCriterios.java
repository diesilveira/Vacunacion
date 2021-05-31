/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.lectoresEscritores;

import java.time.LocalTime;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.ComparadorDeCriterios;
import proyectovacunacion.clases.CriteriosActivos;
import proyectovacunacion.clases.Reloj;

/**
 *
 * @author danie
 */
public class GeneradorDeCriterios {

    private final LoggerSistema logSistema;
    private final Reloj reloj;

    public GeneradorDeCriterios(Reloj rel) {
        logSistema = new LoggerSistema();
        this.reloj = rel;
    }

    //Cuando generamos los criterios asumimos que vienen ordenados.
    public Queue<Criterio> generarCriterios(CriteriosActivos criterios, String rutaArchivo) throws InterruptedException {
        String[] listaCriterios = ManejadorArchivosGenerico.leerArchivo(rutaArchivo, false);

        criterios.getMutex().acquire();

        for (String lineaLeida : listaCriterios) {
            String[] lineaAProcesar = lineaLeida.split("\\|");
            Criterio criterio = new Criterio(Integer.parseInt(lineaAProcesar[0].trim()),
                    lineaAProcesar[1].trim(), new Vacuna(lineaAProcesar[2].trim()));
            LocalTime time0 = LocalTime.now();
            criterio.setNanoSecInicializado(time0.toNanoOfDay());

            logSistema.escribirLog("Nuevo criterio creado: " + lineaAProcesar[1].trim());
            criterio.setMutex(new Semaphore(1, true));
            criterio.setConsumido(new Semaphore(3, true));
            criterio.setActualizado(new Semaphore(0, true));
            boolean enCola = false;
            for (Criterio criterioEnCola : criterios.getCriteriosDeAgenda()) {
                if (criterioEnCola.getGrupoPrioritario().equals(criterio.getGrupoPrioritario())) {
                    criterios.getCriteriosDeAgenda().remove(criterioEnCola);
                    criterios.getCriteriosDeAgenda().add(criterio);
                    LocalTime time = LocalTime.now();
                    criterio.setNanoSecModificado(time.toNanoOfDay() - criterio.getNanoSecInicializado());
                    logSistema.escribirLog("Criterio " + lineaAProcesar[1].trim() + " reordenado [" + criterio.getNanoSecModificado() / 1000000 + " ms]" );
                    enCola = true;
                    break;
                }
            }
            if (enCola == false) {
                criterios.getCriteriosDeAgenda().add(criterio);
                LocalTime time = LocalTime.now();
                criterio.setNanoSecModificado(time.toNanoOfDay() - criterio.getNanoSecInicializado());
                logSistema.escribirLog("Criterio " + lineaAProcesar[1].trim() + " ingresado al sistema [" + criterio.getNanoSecModificado() / 1000000 + " ms]");
            }
        }
        LinkedList<Criterio> listaAOrdenar = (LinkedList<Criterio>) criterios.getCriteriosDeAgenda();
        listaAOrdenar.sort(new ComparadorDeCriterios());
        criterios.setCriteriosDeAgenda(listaAOrdenar);

        criterios.getMutex().release();
        return criterios.getCriteriosDeAgenda();
    }
}
