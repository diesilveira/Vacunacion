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
public class VacunatoriosActivos {
    
    private Semaphore mutex;
    private Queue <Vacunatorio> vacunatoriosActivos;
    private Logger logger;

    public VacunatoriosActivos() {
        this.mutex = new Semaphore(1, true);
        this.vacunatoriosActivos = new LinkedList();
        this.logger = new Logger();
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    public Queue<Vacunatorio> getVacunatoriosActivos() {
        return vacunatoriosActivos;
    }

    public void setVacunatoriosActivos(Queue<Vacunatorio> vacunatoriosActivos) {
        this.vacunatoriosActivos = vacunatoriosActivos;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }   
}
