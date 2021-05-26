/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.util.Queue;
import proyectovacunacion.clases.Agenda;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class Agendar implements Runnable{
        
    private Queue <Criterio> colaCriterio;
    private Queue <Vacunatorio> colaVacunacion;
    private Logger logger;
    private String[] log;

    public Agendar(Queue<Criterio> colaCriterio, Queue<Vacunatorio> colaVacunacion) {
        this.colaCriterio = colaCriterio;
        this.colaVacunacion = colaVacunacion;
        this.logger = new Logger();
        this.log= new String[1];
        
    }
    @Override
    public void run() {
        while (true){
            try{
                Persona persona = null;
                Criterio criterioSeleccionado = null;
                for(Criterio criterio: colaCriterio){
                    if(!criterio.getPersonasEnCriterio().isEmpty()){
                        criterio.getActualizado().acquire();
                        criterio.getMutex().acquire();

                        persona = criterio.getPersonasEnCriterio().remove();
                        criterioSeleccionado = criterio;
                        //System.out.println("Documento: " + persona.getCedula() + " REMOVIDO de la cola de Prioridad: " + criterio.getGrupoPrioritario());                            

                        criterio.getMutex().release();
                        criterio.getConsumido().release();
                        break;
                    }
                }
                if(persona != null){
                    for (Vacunatorio vacunatorio: colaVacunacion){
                        if(vacunatorio.getId().equals(persona.getVacunatorioSeleccionado())){
                             //System.out.println("Existe vacunatorio para : " + persona.getCedula());
                             //Si tengo un vacunatorio no permito que nadie tome las vacunas o las fechas hasta que verifique 
                             vacunatorio.getMutexVacunatorio().acquire();                             
                             for(Vacuna vacuna : vacunatorio.getVacunasDisponibles())
                                 if(vacuna.getTipo().equals(criterioSeleccionado.getTipoVacuna().getTipo()) && vacuna.getCantidad()>0){
                                     //System.out.println("Existe Vacuna para : " + persona.getCedula());
                                     
                                     for (Agenda agenda : vacunatorio.getFechasDisponibles()){
                                         if(!agenda.isAsignada()){
                                             agenda.setAsignada(true);
                                             agenda.setPersonaAsignada(persona);
                                             vacuna.setCantidad(vacuna.getCantidad()- 1);
                                             System.out.println("DOCUMENTO: " + persona.getCedula() + " AGENDADO EN: " + vacunatorio.getId() );
                                             break;
                                         }
                                     }                                     
                                    break;
                                 }
                             vacunatorio.getMutexVacunatorio().release();
                            break;
                        }                       
                    }                  
                }
            }catch(InterruptedException ex){            
            }            
        }        
    }    
}
