/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

/**
 *
 * @author dsilv
 */
public class Persona {

    private final String cedula;
    private final String departamento;
    private final String vacunatorioSeleccionado;
    private String grupoPrioritario;
    private boolean habilitado;
    private boolean vacunatorioDisponible;
    private boolean asignadaVacuna;
    private boolean asignadaAgenda;
    private int edad;
    private boolean movilidad;

    public Persona(String ced, String depto, String vs, boolean mov, String grupoPrioritario) {
        this.cedula = ced;
        this.departamento = depto;
        this.vacunatorioSeleccionado = vs;
        this.movilidad = mov;
        this.grupoPrioritario = grupoPrioritario;
        habilitado = false;
        vacunatorioDisponible = false;
        asignadaAgenda = false;
        asignadaVacuna = false;
    }

    public void setGrupoPrioritario(String gp) {
        grupoPrioritario = gp;
    }

    public boolean isMovilidad() {
        return movilidad;
    }

    public void setMovilidad(boolean movilidad) {
        this.movilidad = movilidad;
    }

    public String getGrupoPrioritario() {
        return grupoPrioritario;
    }

    public String getCedula() {
        return cedula;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getVacunatorioSeleccionado() {
        return vacunatorioSeleccionado;
    }

    public int getEdad() {
        return edad;
    }

    public boolean getMovilidad() {
        return movilidad;
    }

    public void habilitado() {
        this.habilitado = true;
    }

    public void tieneVacunatorio() {
        this.vacunatorioDisponible = true;
    }

    public void tieneVacuna() {
        this.asignadaVacuna = true;
    }

    public void tieneAgenda() {
        this.asignadaAgenda = true;
    }

    public boolean getHabilitado() {
        return this.habilitado;
    }

    public boolean getVacunatorioDispo() {
        return this.habilitado;
    }

    public boolean getAgendaDIspo() {
        return this.habilitado;
    }

    public boolean getVacunaDIspo() {
        return this.habilitado;
    }
}
