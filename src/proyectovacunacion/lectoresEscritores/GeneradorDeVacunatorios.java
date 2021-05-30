/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.lectoresEscritores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Agenda;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;

/**
 *
 * @author danie
 */
public class GeneradorDeVacunatorios {
    LoggerSistema logSistema;

    public GeneradorDeVacunatorios() {
        logSistema = new LoggerSistema();
    }
    

    public Queue<Vacunatorio> generarVacunatorios(VacunatoriosActivos vacunatoriosActivos, String rutaArchivo) throws InterruptedException {
        String[] listaVacunatorios = ManejadorArchivosGenerico.leerArchivo(rutaArchivo, false);

        vacunatoriosActivos.getMutex().acquire();

        for (String lineaLeida : listaVacunatorios) {
            String[] lineaAProcesar = lineaLeida.split("\\|");
            Queue<Vacuna> vacunas = new LinkedList<>();
            Vacuna pfizzer = new Vacuna(lineaAProcesar[1].trim(), Integer.parseInt(lineaAProcesar[2].trim()));
            Vacuna sinovac = new Vacuna(lineaAProcesar[3].trim(), Integer.parseInt(lineaAProcesar[4].trim()));
            int stockPfizzer = Integer.parseInt(lineaAProcesar[2].trim());
            int stockSinovac = Integer.parseInt(lineaAProcesar[4].trim());
            vacunas.add(pfizzer);
            vacunas.add(sinovac);
            
            Queue<Agenda> fechasDisponibles = new LinkedList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            Agenda fecha1 = new Agenda(LocalDateTime.parse(lineaAProcesar[5].trim(), formatter));
            Agenda fecha2 = new Agenda(LocalDateTime.parse(lineaAProcesar[6].trim(), formatter));
            Agenda fecha3 = new Agenda(LocalDateTime.parse(lineaAProcesar[7].trim(), formatter));
            fechasDisponibles.add(fecha1);
            fechasDisponibles.add(fecha2);
            fechasDisponibles.add(fecha3);
            
            Vacunatorio vacunatorioNuevo = new Vacunatorio(lineaAProcesar[0].trim(),
                    vacunas, fechasDisponibles, lineaAProcesar[8]);
            logSistema.escribirLog("Nuevo vacunatorio creado: "+lineaAProcesar[0].trim());

            vacunatorioNuevo.setMutex(new Semaphore(1, true));
            vacunatorioNuevo.setConsumido(new Semaphore(3, true));
            vacunatorioNuevo.setActualizado(new Semaphore(0, true));

            boolean enCola = false;

            for (Vacunatorio vacunatorioEnCola : vacunatoriosActivos.getVacunatoriosActivos()) {
                //compruebo si vacunatorio ya esta en la cola
                if (vacunatorioNuevo.getId().equals(vacunatorioEnCola.getId())) {
                    //compruebo si el vac. esta habilitado en el nuevo ingreso
                    if (vacunatorioNuevo.getHabilitado().equals("true")) {
                        //agrego las vacunas
                        vacunatorioEnCola.agregarStock(stockPfizzer, stockSinovac);
                        logSistema.escribirLog("Vacunatorio : "+lineaAProcesar[0].trim()+" ya ingresado en sistema. Agregando stock...");
                    } else {
                        //si no esta habilitado lo saco de la cola
                        vacunatoriosActivos.getVacunatoriosActivos().remove(vacunatorioEnCola);
                        logSistema.escribirLog("Vacunatorio : "+lineaAProcesar[0].trim()+" deshabilitado. Eliminado del sistema");
                    }
                    enCola = true;
                    break;
                }
            }
            //si no esta en la cola lo agrego
            if (enCola == false) {

                vacunatoriosActivos.getVacunatoriosActivos().add(vacunatorioNuevo);
                logSistema.escribirLog("Vacunatorio : "+lineaAProcesar[0].trim()+" Nuevo. Ingresado al sistema");
                
            }

        }

        vacunatoriosActivos.getMutex().release();
        return vacunatoriosActivos.getVacunatoriosActivos();
    }
}
