/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import proyectovacunacion.clases.Fecha;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.CriteriosActivos;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Reloj;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;
import proyectovacunacion.lectoresEscritores.EscritorPersonasEspera;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class Agendar implements Runnable {

    private final CriteriosActivos criterios;
    private final VacunatoriosActivos vacunatorios;
    private final Logger logger;
    private final EscritorPersonasEspera espera;
    private final Reloj reloj;

    public Agendar(CriteriosActivos criterios, VacunatoriosActivos vacunatorios, Reloj rel) {
        this.criterios = criterios;
        this.vacunatorios = vacunatorios;
        this.espera = new EscritorPersonasEspera();
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
                                    int fecha = 1;
                                    for (Fecha fechaDeAgenda : vacunatorio.getFechasDisponibles()) {

                                        if (!fechaDeAgenda.isAsignada() && (fecha == 1)) {
                                            fechaDeAgenda.setAsignada(true);
                                            fechaDeAgenda.setPersonaAsignada(persona);
                                            vacuna.setCantidad(vacuna.getCantidad() - 1);
                                            persona.setPrimerFecha(fechaDeAgenda);
                                            fecha++;
                                            logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId() + " Primera dosis: " + persona.getPrimerFecha().getFechasDisponible().toString() + " (" + (persona.getCicloAgendado() - persona.getCicloInicializado()) + " ciclos)");
                                            
                                        }
                                        if (!fechaDeAgenda.isAsignada() && (fecha == 2)) {
                                            fechaDeAgenda.setAsignada(true);
                                            fechaDeAgenda.setPersonaAsignada(persona);
                                            vacuna.setCantidad(vacuna.getCantidad() - 1);
                                            persona.setPrimerFecha(fechaDeAgenda);
                                            fecha = 1;
                                            persona.tieneAgenda();
                                            persona.setCicloAgendado(reloj.getContador());
                                            reloj.agregarCuenta();
                                            logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Agendado en: " + vacunatorio.getId() + " Segunda dosis: " + persona.getPrimerFecha().getFechasDisponible().toString() + " (" + (persona.getCicloAgendado() - persona.getCicloInicializado()) + " ciclos)");
                                            break;
                                        }

                                    }
                                    break;
                                }
                            }
                            vacunatorio.getMutex().release();
//                            break;
                        }
                    }
                    vacunatorios.getMutex().release();

                    if (persona.getVacunatorioDispo() == false && persona.getVacunaDIspo() == false && persona.getAgendaDIspo() == false) {

                        persona.setCicloNoAgendado(reloj.getContador());
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio " + persona.getVacunatorioSeleccionado() + ": no disponible (" + (persona.getCicloNoAgendado() - persona.getCicloInicializado()) + " ciclos)");
                        espera.escribirLog(persona);
                    }
                    if (persona.getVacunatorioDispo() == true && persona.getVacunaDIspo() == false && persona.getAgendaDIspo() == false) {

                        persona.setCicloNoAgendado(reloj.getContador());
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio " + persona.getVacunatorioSeleccionado() + ": Sin vacunas disponibles (" + (persona.getCicloNoAgendado() - persona.getCicloInicializado()) + " ciclos)");
                        espera.escribirLog(persona);
                    }
                    if (persona.getVacunatorioDispo() == true && persona.getVacunaDIspo() == true && persona.getAgendaDIspo() == false) {

                        persona.setCicloNoAgendado(reloj.getContador());
                        logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Vacunatorio " + persona.getVacunatorioSeleccionado() + ": Agenda llena (" + (persona.getCicloNoAgendado() - persona.getCicloInicializado()) + " ciclos)");
                        espera.escribirLog(persona);
                    }
                }
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }
    }
}
