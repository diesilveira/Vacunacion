/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.lectoresEscritores.ManejadorArchivosGenerico;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class Captura implements Runnable{
    private final String rutaArchivo;
    private final Semaphore s;
    private final Semaphore s_consumido;
    private final Semaphore s_actualizado;
    private final Queue <Persona> colaRecepcion;
    private final Logger logger;
    
    public Captura (String rutaArchivo, Semaphore s, Semaphore c,Semaphore a, Queue<Persona> colaRecepcion){
        this.rutaArchivo = rutaArchivo;
        this.s = s;
        this.s_consumido = c;
        this.s_actualizado = a;        
        this.colaRecepcion = colaRecepcion;
        this.logger = new Logger();  
    }
    
    @Override
    public void run() {
        try{
            String [] listaSolicitudes = ManejadorArchivosGenerico.leerArchivo
                                                      (rutaArchivo, false);
            for(String lineaLeida : listaSolicitudes){                
                String [] lineaAProcesar = lineaLeida.split("\\|");
                Persona persona = new Persona (lineaAProcesar[0].trim(),
                             lineaAProcesar[1].trim(), lineaAProcesar[2].trim(), 
                                Boolean.parseBoolean(lineaAProcesar[3].trim()),
                                                      lineaAProcesar[4].trim());
                s_consumido.acquire();
                s.acquire();
                
                colaRecepcion.add(persona);
            
                this.logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + "Nueva persona ingresada al sistema.");
                
                s.release();
                s_actualizado.release();
            }      
        }catch(InterruptedException ex){            
        } 
    }
}
