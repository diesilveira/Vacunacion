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
    private String rutaArchivo;        
    
    public Queue <Vacunatorio> generarVacunatorios (VacunatoriosActivos vacunatorios, String rutaArchivo) throws InterruptedException{
        String [] listaVacunatorios = ManejadorArchivosGenerico.leerArchivo
                                                      (rutaArchivo, false);
            for(String lineaLeida : listaVacunatorios){                
                String [] lineaAProcesar = lineaLeida.split("\\|");
                Queue<Vacuna> vacunas = new LinkedList<>();
                Vacuna pfizzer = new Vacuna (lineaAProcesar[1].trim(),Integer.parseInt(lineaAProcesar[2].trim()));
                Vacuna sinovac = new Vacuna (lineaAProcesar[3].trim(),Integer.parseInt(lineaAProcesar[4].trim()));
                vacunas.add(pfizzer);
                vacunas.add(sinovac);
                Queue<Agenda> fechasDisponibles= new LinkedList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                Agenda fecha1 = new Agenda (LocalDateTime.parse(lineaAProcesar[5].trim(), formatter));
                Agenda fecha2 = new Agenda (LocalDateTime.parse(lineaAProcesar[6].trim(), formatter));
                Agenda fecha3 = new Agenda (LocalDateTime.parse(lineaAProcesar[7].trim(), formatter));
                fechasDisponibles.add(fecha1);
                fechasDisponibles.add(fecha2);
                fechasDisponibles.add(fecha3);
                Vacunatorio vacunatorio = new Vacunatorio(lineaAProcesar[0].trim(),
                                        vacunas, fechasDisponibles);
                
                
                
                
                
                
                
                
                vacunatorio.setMutexVacunatorio(new Semaphore(1, true));
                
                vacunatorios.getMutex().acquire();
                
                vacunatorios.getVacunatoriosActivos().add(vacunatorio);
                
                vacunatorios.getMutex().release();
            
        }
        return vacunatorios.getVacunatoriosActivos();
    }
}
