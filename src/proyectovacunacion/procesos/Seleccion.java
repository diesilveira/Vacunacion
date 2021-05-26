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
    
    private Semaphore s_criterio;
    private Semaphore s_consumido;
    private Semaphore s_recepcion;
    private Semaphore s_actualizado;
    private Queue <Persona> colaRecepcion;
    private Queue <Criterio> colaCriterio;

    public Seleccion(Semaphore s_recepcion,Semaphore s_criterio, Semaphore c, Semaphore a, Queue<Persona> colaRecepcion, Queue<Criterio> colaCriterio) {
        this.s_recepcion = s_recepcion;
        this.s_criterio = s_criterio;
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
                            s_criterio.acquire();
                            criterio.agregarPersona(persona);
                            s_criterio.release();                            
                        }
                    }
                     
            }catch(InterruptedException ex){            
            } 
        }        
    }    
}
