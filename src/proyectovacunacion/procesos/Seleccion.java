/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.Persona;

/**
 *
 * @author danie
 */
public class Seleccion implements Runnable{   
        
    private Semaphore s_consumido;
    private Semaphore s_recepcion;
    private Semaphore s_actualizado;
    private Queue <Persona> colaRecepcion;
    private Queue <Criterio> colaCriterio;

    public Seleccion(Semaphore s_recepcion, Semaphore c, Semaphore a, Queue<Persona> colaRecepcion, Queue<Criterio> colaCriterio) {
        this.s_recepcion = s_recepcion;
        this.s_consumido = c;
        this.s_actualizado = a;
        this.colaRecepcion = colaRecepcion;
        this.colaCriterio = colaCriterio;
    }  
    
    @Override
    public void run() {
        while(true){
            try{
                    s_actualizado.acquire();
                    s_recepcion.acquire();
                    Persona persona = colaRecepcion.remove(); 
                    s_recepcion.release();
                    s_consumido.release();
                    
                    for(Criterio criterio: colaCriterio){
                        if(persona.getGrupoPrioritario().equals(criterio.getGrupoPrioritario())){
                            criterio.getConsumido().acquire();
                            criterio.getMutex().acquire();
                            
                            criterio.agregarPersona(persona);
                            System.out.println("Documento: " +persona.getCedula() + " insertado en cola de Prioridad: " + criterio.getGrupoPrioritario());                            
                            
                            criterio.getMutex().release();
                            criterio.getActualizado().release();
                        }
                    }
                     
            }catch(InterruptedException ex){            
            } 
        }        
    }    
}
