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
import proyectovacunacion.clases.Fecha;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;

/**
 *
 * @author danie
 */
public class GeneradorDeVacunatorios {

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

            Queue<Fecha> fechasDisponibles = new LinkedList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            Fecha fecha = new Fecha(LocalDateTime.parse(lineaAProcesar[5].trim(), formatter));
            Fecha segundaFecha = new Fecha(fecha.getFechasDisponible().plusMonths(1));
            int contador = (stockPfizzer + stockSinovac / 2);
            fechasDisponibles.add(fecha);
            fechasDisponibles.add(segundaFecha);
            contador--;
            int i = 1;
            while (contador != 0) {
                Fecha fecha2 = new Fecha(fecha.getFechasDisponible().plusMinutes(2 * i));
                i++;
                if (fecha2.getFechasDisponible().getHour() > 8 && fecha2.getFechasDisponible().getHour() < 20) {
                    Fecha segundaFecha2 = new Fecha(fecha2.getFechasDisponible().plusMonths(1));
                    fechasDisponibles.add(segundaFecha2);
                    fechasDisponibles.add(fecha2);
                    contador--;
                }

            }

            Vacunatorio vacunatorioNuevo = new Vacunatorio(lineaAProcesar[0].trim(),
                    vacunas, fechasDisponibles, lineaAProcesar[7]);

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
                    } else {
                        //si no esta habilitado lo saco de la cola
                        vacunatoriosActivos.getVacunatoriosActivos().remove(vacunatorioEnCola);
                    }
                    enCola = true;
                    break;

                }

            }
            //si no esta en la cola lo agrego
            if (enCola == false) {

                vacunatoriosActivos.getVacunatoriosActivos().add(vacunatorioNuevo);

            }

        }

        vacunatoriosActivos.getMutex().release();
        return vacunatoriosActivos.getVacunatoriosActivos();
    }
}
