/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.time.LocalDateTime;
import java.util.Queue;

/**
 *
 * @author dsilv
 */
public class Agenda  {
    private LocalDateTime fechasDisponible;
    private boolean asignada;
    private Persona personaAsignada;

    public Agenda(LocalDateTime fechasDisponible) {
        this.fechasDisponible = fechasDisponible;
        this.asignada = false;
    }

    public boolean isAsignada() {
        return asignada;
    }
    
    public void setAsignada(boolean asignada) {
        this.asignada = asignada;
    }

    public Persona getPersonaAsignada() {
        return personaAsignada;
    }

    public void setPersonaAsignada(Persona personaAsignada) {
        this.personaAsignada = personaAsignada;
    }
}
