/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.hilos;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.lectoresEscritores.ManejadorArchivosGenerico;

/**
 *
 * @author danie
 */
public class ReceptorSolicitud implements Runnable{
    private String rutaArchivo;
    private Semaphore s;
    private Queue <Persona> colaRecepcion;
    
    public ReceptorSolicitud (String rutaArchivo, Semaphore s, Queue<Persona> colaRecepcion){
        this.rutaArchivo = rutaArchivo;
        this.s = s;
        this.colaRecepcion = colaRecepcion;
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
                             Boolean.parseBoolean(lineaAProcesar[3].trim()));                                
                s.acquire();
                colaRecepcion.add(persona);
                System.out.println(persona.getCedula());
                s.release();                        
            }      
        }catch(InterruptedException ex){            
        } 
    }
}
