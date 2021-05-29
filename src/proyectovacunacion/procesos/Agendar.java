/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.util.Queue;
import proyectovacunacion.clases.Agenda;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.CriteriosActivos;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class Agendar implements Runnable{
        
    private final CriteriosActivos criterios;
    private final VacunatoriosActivos vacunatorios;
    private final Logger logger;

    public Agendar(CriteriosActivos criterios, VacunatoriosActivos vacunatorios) {
        this.criterios = criterios;
        this.vacunatorios = vacunatorios;
        this.logger = new Logger();        
    }
    
    @Override   
    public void run() {
        while (true){
            try{
                Persona persona = null;
                Criterio criterioSeleccionado = null;
                
                criterios.getMutex().acquire();                
                for(Criterio criterio: criterios.getCriteriosDeAgenda()){
                    if(!criterio.getPersonasEnCriterio().isEmpty()){
                        criterio.getActualizado().acquire();
                        criterio.getMutex().acquire();

                        persona = criterio.getPersonasEnCriterio().remove();
                        criterioSeleccionado = criterio;
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Removido de la cola de Prioridad: " + criterio.getGrupoPrioritario());
                        System.out.println("Documento: " + persona.getCedula() + " Removido de la cola de Prioridad: " + criterio.getGrupoPrioritario());
                        
                        criterio.getMutex().release();
                        criterio.getConsumido().release();
                        break;
                    }
                }                
                criterios.getMutex().release();               
                
                if(persona != null){
                    
                    vacunatorios.getMutex().acquire();
                    for (Vacunatorio vacunatorio: vacunatorios.getVacunatoriosActivos()){
                        if(vacunatorio.getId().equals(persona.getVacunatorioSeleccionado())){
                            persona.tieneVacunatorio();
                             //Si tengo un vacunatorio no permito que nadie tome las vacunas o las fechas hasta que verifique 
                             vacunatorio.getMutexVacunatorio().acquire();                             
                             for(Vacuna vacuna : vacunatorio.getVacunasDisponibles())
                                 if(vacuna.getTipo().equals(criterioSeleccionado.getTipoVacuna().getTipo()) && vacuna.getCantidad()>0){                                  
                                     persona.tieneVacuna();
                                     for (Agenda agenda : vacunatorio.getFechasDisponibles()){
                                         if(!agenda.isAsignada()){
                                             agenda.setAsignada(true);
                                             agenda.setPersonaAsignada(persona);
                                             vacuna.setCantidad(vacuna.getCantidad()- 1);
                                             persona.tieneAgenda();
                                             logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId());
                                             System.out.println("Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId());
                                             break;
                                         }
                                     }                                     
                                    break;
                                 }
                             vacunatorio.getMutexVacunatorio().release();
                            break;
                        }
                    } 
                    vacunatorios.getMutex().release();                   
                    
                    if(persona.getVacunatorioDispo() ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio seleccionado, no disponible");
                    }
                    if(persona.getVacunatorioDispo() ==true && persona.getVacunaDIspo() ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Sin vacunas disponibles");
                    }
                    if(persona.getVacunatorioDispo() ==true && persona.getVacunaDIspo() ==true && persona.getAgendaDIspo() ==false){
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agenda llena");
                    }
                }                
            }catch(InterruptedException ex){            
            }            
        }        
    }    
}
