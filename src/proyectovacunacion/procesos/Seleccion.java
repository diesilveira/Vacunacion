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
import proyectovacunacion.lectoresEscritores.Logger;

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

    public Seleccion(Semaphore s_recepcion, Semaphore c, Semaphore a, Queue<Persona> colaRecepcion, Queue<Criterio> colaCriterio) {
        this.s_recepcion = s_recepcion;
        this.s_consumido = c;
        this.s_actualizado = a;
        this.colaRecepcion = colaRecepcion;
        this.colaCriterio = colaCriterio;
        this.logger = new Logger();

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
                        this.logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " Insertado en cola de Prioridad: " + criterio.getGrupoPrioritario());
                        criterio.getMutex().release();
                        criterio.getActualizado().release();
                    }

                }
                if (!persona.getHabilitado()) {
                    this.logger.escribirLog(Thread.currentThread().getName(), "Documento: " + persona.getCedula() + " No habilitado a vacunarse ");
                }

            } catch (InterruptedException ex) {
            }
        }
    }
}
