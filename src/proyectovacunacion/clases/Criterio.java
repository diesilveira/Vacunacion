/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author dsilv
 */
public class Criterio {

    private int prioridad;
    private String grupoPrioritario;
    private Semaphore mutex;
    private Semaphore consumido;
    private Semaphore actualizado;
    private Vacuna vacuna;
    private Queue<Persona> personasEnCriterio;
    private final Logger logger;
    private long nanoSecInicializado;
    private long nanoSecModificado;

    public Criterio(int prioridad, String grupoPrioritario, Vacuna tipoVacuna) {
        this.prioridad = prioridad;
        this.grupoPrioritario = grupoPrioritario;
        this.vacuna = tipoVacuna;
        this.personasEnCriterio = new LinkedList();
        this.logger = new Logger();
    }

    public int getPrioridad() {
        return prioridad;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getGrupoPrioritario() {
        return grupoPrioritario;
    }

    public void setGrupoPrioritario(String grupoPrioritario) {
        this.grupoPrioritario = grupoPrioritario;
    }

    public Queue<Persona> getPersonasEnCriterio() {
        return personasEnCriterio;
    }

    public void setPersonasEnCriterio(Queue<Persona> personasEnCriterio) {
        this.personasEnCriterio = personasEnCriterio;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public Semaphore getConsumido() {
        return consumido;
    }

    public Semaphore getActualizado() {
        return actualizado;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    public void setConsumido(Semaphore consumido) {
        this.consumido = consumido;
    }

    public void setActualizado(Semaphore actualizado) {
        this.actualizado = actualizado;
    }

    public boolean agregarPersona(Persona p) {

        try {
            personasEnCriterio.add(p);

            return true;
        } catch (Exception ex) {
            this.logger.escribirLog("Criterio", "Error " + ex.getMessage() + " al cargar  "
                    + p.getCedula() + " al criterio " + this.grupoPrioritario);

            return false;
        }

    }

    public boolean borrarPersona(Persona p) {

        try {
            personasEnCriterio.remove(p);
            return true;
        } catch (Exception ex) {
            this.logger.escribirLog("Criterio", "Error " + ex.getMessage() + " al borrar  "
                    + p.getCedula() + " del criterio" + this.grupoPrioritario);
            return false;
        }

    }

    public void setNanoSecModificado(long nanoSecModificado) {
        this.nanoSecModificado = nanoSecModificado;
    }

    public long getNanoSecModificado() {
        return nanoSecModificado;
    }

    public long getNanoSecInicializado() {
        return nanoSecInicializado;
    }

    public void setNanoSecInicializado(long nanoSecInicializado) {
        this.nanoSecInicializado = nanoSecInicializado;
    }
}
