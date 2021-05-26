/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.util.Queue;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Vacunatorio;

/**
 *
 * @author danie
 */
public class Agendar implements Runnable{
        
    private Queue <Criterio> colaCriterio;
    private Queue <Vacunatorio> colaVacunacion;

    public Agendar(Queue<Criterio> colaCriterio, Queue<Vacunatorio> colaVacunacion) {
        this.colaCriterio = colaCriterio;
        this.colaVacunacion = colaVacunacion;
    }
    
    
    @Override
    public void run() {
        while (true){
            try{
                for(Criterio criterio: colaCriterio){
                        if(!criterio.getPersonasEnCriterio().isEmpty()){
                            criterio.getActualizado().acquire();
                            criterio.getMutex().acquire();
                            
                            Persona persona = criterio.getPersonasEnCriterio().remove();
                            System.out.println("Documento: " +persona.getCedula() + " removida de la cola de Prioridad: " + criterio.getGrupoPrioritario());                            
                            
                            criterio.getMutex().release();
                            criterio.getConsumido().release();
                            break;
                        }
                    }        
            }catch(InterruptedException ex){            
            }            
        }        
    }    
}
