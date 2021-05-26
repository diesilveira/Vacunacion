/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author dsilv
 */
public class Vacunatorio {

    private String id;
    /*identificacion segun departamento barrio y numero
                        Ejemplo MOTC01 -> Montevideo Tres Cruces Vacunatorio 1
                        (puede haber varios por barrio)
                        Las 2 primeras letras depto. las otras dos barrio,
                        los dos nuemros el numero de vacunatorio en ese barrio
     */    
    private Queue<Vacuna> vacunasDisponibles;
    private Semaphore mutexVacunatorio;
    private Queue<Agenda> fechasDisponibles;
    private Queue<Persona> agendasFuturas;//Guarda a aquellas personas que no se pudieron agendar

    public Vacunatorio(String id, Queue<Vacuna> vacunasDisponibles, Queue<Agenda> fechasDisponibles) {
        this.id = id;
        this.vacunasDisponibles = vacunasDisponibles;
        this.fechasDisponibles = fechasDisponibles;
    }

    public String getId() {
        return id;
    }   

    public Queue<Vacuna> getVacunasDisponibles() {
        return vacunasDisponibles;
    }

    public void setVacunasDisponibles(Queue<Vacuna> vacunasDisponibles) {
        this.vacunasDisponibles = vacunasDisponibles;
    }

    public Queue<Agenda> getFechasDisponibles() {
        return fechasDisponibles;
    }

    public void setFechasDisponibles(Queue<Agenda> fechasDisponibles) {
        this.fechasDisponibles = fechasDisponibles;
    }    

    public Queue<Persona> getAgendasFuturas() {
        return agendasFuturas;
    }

    public void setAgendasFuturas(Queue<Persona> agendasFuturas) {
        this.agendasFuturas = agendasFuturas;
    }

    public Semaphore getMutexVacunatorio() {
        return mutexVacunatorio;
    }

    public void setMutexVacunatorio(Semaphore mutexVacunatorio) {
        this.mutexVacunatorio = mutexVacunatorio;
    }       
}
