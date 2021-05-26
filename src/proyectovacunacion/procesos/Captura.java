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
    private String rutaArchivo;
    private Semaphore s;
    private Semaphore s_consumido;
    private Semaphore s_actualizado;
    private Queue <Persona> colaRecepcion;
    private Logger logger;
    private String[] log;
    
    public Captura (String rutaArchivo, Semaphore s, Semaphore c,Semaphore a, Queue<Persona> colaRecepcion){
        this.rutaArchivo = rutaArchivo;
        this.s = s;
        this.s_consumido = c;
        this.s_actualizado = a;        
        this.colaRecepcion = colaRecepcion;
        this.logger = new Logger();
        this.log= new String[1];        
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
                log[0] = "Nueva persona ingresada al sistema con el documento: " +persona.getCedula();
                logger.escribirLog(log);
                //System.out.println(persona.getCedula());
                
                s.release();
                s_actualizado.release();
            }      
        }catch(InterruptedException ex){            
        } 
    }
}
