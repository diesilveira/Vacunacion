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
        
    private final Queue <Criterio> colaCriterio;
    private final Queue <Vacunatorio> colaVacunacion;
    private final Logger logger;


    public Agendar(Queue<Criterio> colaCriterio, Queue<Vacunatorio> colaVacunacion) {
        this.colaCriterio = colaCriterio;
        this.colaVacunacion = colaVacunacion;
        this.logger = new Logger();
     

        
    }
    @Override
   
    public void run() {
        while (true){
            try{
                Persona persona = null;
                Criterio criterioSeleccionado = null;
                boolean consigueVacunatorio = false;
                boolean consigueVacuna = false;
                boolean consigueAgenda = false;
                for(Criterio criterio: colaCriterio){
                    if(!criterio.getPersonasEnCriterio().isEmpty()){
                        criterio.getActualizado().acquire();
                        criterio.getMutex().acquire();

                        persona = criterio.getPersonasEnCriterio().remove();
                        criterioSeleccionado = criterio;
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Removido de la cola de Prioridad: " + criterio.getGrupoPrioritario());
                        
                        criterio.getMutex().release();
                        criterio.getConsumido().release();
                        break;
                    }
                }
                if(persona != null){
                    for (Vacunatorio vacunatorio: colaVacunacion){
                        if(vacunatorio.getId().equals(persona.getVacunatorioSeleccionado())){
                            consigueVacunatorio= true;
                             //Si tengo un vacunatorio no permito que nadie tome las vacunas o las fechas hasta que verifique 
                             vacunatorio.getMutexVacunatorio().acquire();                             
                             for(Vacuna vacuna : vacunatorio.getVacunasDisponibles())
                                 if(vacuna.getTipo().equals(criterioSeleccionado.getTipoVacuna().getTipo()) && vacuna.getCantidad()>0){
                                     consigueVacuna = true;
                                     for (Agenda agenda : vacunatorio.getFechasDisponibles()){
                                         if(!agenda.isAsignada()){
                                             agenda.setAsignada(true);
                                             agenda.setPersonaAsignada(persona);
                                             vacuna.setCantidad(vacuna.getCantidad()- 1);
                                             consigueAgenda = true;
                                             logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId());
                                             
                                             break;
                                         }
                                     }                                     
                                    break;
                                 }
                             vacunatorio.getMutexVacunatorio().release();
                            break;
                        }
                    } 
                    if(consigueVacunatorio ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio seleccionado, no disponible");
                    }
                    if(consigueVacunatorio ==true && consigueVacuna ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Sin vacunas disponibles");
                    }
                    if(consigueVacunatorio ==true && consigueVacuna ==true && consigueAgenda ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agenda llena");
                    }
                }
                
            }catch(InterruptedException ex){            
            }            
        }        
    }    
}
