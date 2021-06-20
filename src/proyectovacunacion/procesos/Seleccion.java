/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.procesos;

import java.time.LocalTime;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.lectoresEscritores.EscritorPersonasEspera;
import proyectovacunacion.lectoresEscritores.Logger;
import proyectovacunacion.clases.Reloj;

/**
 *
 * @author danie
 */
public class Seleccion implements Runnable {

    private final Semaphore s_consumido;
    private final Semaphore s_recepcion;
    private final Semaphore s_actualizado;
    private final Queue<Persona> colaRecepcion;
    private final Queue<Criterio> colaCriterio;
    private final Logger logger;
    private final EscritorPersonasEspera espera;
    private Reloj reloj;

    public Seleccion(Semaphore s_recepcion, Semaphore c, Semaphore a, Queue<Persona> colaRecepcion, Queue<Criterio> colaCriterio, Reloj rel) {
        this.s_recepcion = s_recepcion;
        this.s_consumido = c;
        this.s_actualizado = a;
        this.colaRecepcion = colaRecepcion;
        this.colaCriterio = colaCriterio;
        this.logger = new Logger();
        this.espera = new EscritorPersonasEspera();
        this.reloj = rel;

    }

    @Override
    public void run() {
        while (true) {
            try {

                s_actualizado.acquire();
                s_recepcion.acquire();
                Persona persona = colaRecepcion.remove();
                s_recepcion.release();
                s_consumido.release();

                for (Criterio criterio : colaCriterio) {
                    if (persona.getGrupoPrioritario().equals(criterio.getGrupoPrioritario())) {
                        criterio.getConsumido().acquire();
                        criterio.getMutex().acquire();
                        criterio.agregarPersona(persona);
                        persona.habilitado();
                        criterio.getMutex().release();
                        criterio.getActualizado().release();
                    }
                  
                }
                  if (persona.getHabilitado() == false) {
                      persona.setCicloNoAgendado(reloj.getContador() - persona.getCicloInicializado());
                      this.logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula()
                              + " No habilitado a vacunarse - [Grupo " + persona.getGrupoPrioritario() + "] (" + persona.getCicloNoAgendado() + " ciclos)");
                        this.espera.escribirLog(persona);
                    }
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }

    }
}
