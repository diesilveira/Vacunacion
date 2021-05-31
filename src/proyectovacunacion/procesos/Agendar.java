/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.time.LocalTime;
import proyectovacunacion.clases.Fecha;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.CriteriosActivos;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Reloj;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class Agendar implements Runnable {

    private final CriteriosActivos criterios;
    private final VacunatoriosActivos vacunatorios;
    private final Logger logger;
    private final Reloj reloj;

    public Agendar(CriteriosActivos criterios, VacunatoriosActivos vacunatorios, Reloj rel) {
        this.criterios = criterios;
        this.vacunatorios = vacunatorios;
        this.logger = new Logger();
        this.reloj = rel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Persona persona = null;
                Criterio criterioSeleccionado = null;

                criterios.getMutex().acquire();
                for (Criterio criterio : criterios.getCriteriosDeAgenda()) {
                    if (!criterio.getPersonasEnCriterio().isEmpty()) {
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
                criterios.getMutex().release();

                if (persona != null) {

                    vacunatorios.getMutex().acquire();
                    for (Vacunatorio vacunatorio : vacunatorios.getVacunatoriosActivos()) {
                        if (vacunatorio.getId().equals(persona.getVacunatorioSeleccionado())) {
                            persona.tieneVacunatorio();
                            //Si tengo un vacunatorio no permito que nadie tome las vacunas o las fechas hasta que verifique 
                            vacunatorio.getMutex().acquire();
                            for (Vacuna vacuna : vacunatorio.getVacunasDisponibles()) {
                                if (vacuna.getTipo().equals(criterioSeleccionado.getVacuna().getTipo()) && vacuna.getCantidad() > 0) {
                                    persona.tieneVacuna();
                                    for (Fecha primeraFecha : vacunatorio.getFechasDisponibles()) {
                                        if (!primeraFecha.isAsignada()) {
                                            primeraFecha.setAsignada(true);
                                            primeraFecha.setPersonaAsignada(persona);
                                            vacuna.setCantidad(vacuna.getCantidad() - 1);
                                            persona.setPrimerFecha(primeraFecha);

                                            Fecha segFecha = new Fecha(primeraFecha.getFechasDisponible().plusMonths(1));
                                            segFecha.setAsignada(true);
                                            segFecha.setPersonaAsignada(persona);
                                            vacuna.setCantidad(vacuna.getCantidad() - 1);
                                            persona.setSegundaFecha(segFecha);

                                            persona.tieneAgenda();
                                            LocalTime time = LocalTime.now();

                                            persona.setNanoSecAgendado(time.toNanoOfDay() - persona.getNanoSecInicializado());
                                            reloj.agregarSumaDeTiempos(persona.getNanoSecAgendado());

                                            logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId());
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            vacunatorio.getMutex().release();
                            break;
                        }
                    }
                    vacunatorios.getMutex().release();

                    if (persona.getVacunatorioDispo() == false) {
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio seleccionado, no disponible");
                    }
                    if (persona.getVacunatorioDispo() == true && persona.getVacunaDIspo() == false) {
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Sin vacunas disponibles");
                    }
                    if (persona.getVacunatorioDispo() == true && persona.getVacunaDIspo() == true && persona.getAgendaDIspo() == false) {
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agenda llena");
                    }
                }
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }
    }
}
