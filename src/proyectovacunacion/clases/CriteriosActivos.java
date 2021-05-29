/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.lectoresEscritores.Logger;

/**
 *
 * @author danie
 */
public class CriteriosActivos {
    
    private Semaphore mutex;
    private Queue <Criterio> criteriosDeAgenda;
    private Logger logger;

    public CriteriosActivos() {
        this.mutex = new Semaphore(1, true);
        this.criteriosDeAgenda = new LinkedList();
        this.logger = new Logger();
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }    

    public Queue<Criterio> getCriteriosDeAgenda() {
        return criteriosDeAgenda;
    }

    public void setCriteriosDeAgenda(Queue<Criterio> criteriosDeAgenda) {
        this.criteriosDeAgenda = criteriosDeAgenda;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }   
}
