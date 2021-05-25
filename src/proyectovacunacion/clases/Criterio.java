/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.Queue;

/**
 *
 * @author dsilv
 */
public class Criterio implements Runnable{

    private String nombre;
    private String descripcion;
    private Queue<Persona> personasEnCriterio;

    public Queue<Persona> getPersonasEnCriterio() {
        return personasEnCriterio;
    }

    public Criterio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean agregarPersona(Persona p) {

        try {
            personasEnCriterio.add(p);

            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage() + " al cargar  "
                    + p.getCedula() + " al criterio " + this.nombre);
            return false;
        }

    }

    public boolean borrarPersona(Persona p) {

        try {
            personasEnCriterio.remove(p);
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage() + " al borrar  "
                    + p.getCedula() + " del vacunatorio" + this.nombre);
            return false;
        }

    }

    @Override
    public void run() {
        




    }

}
