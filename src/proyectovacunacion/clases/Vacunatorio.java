/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import proyectovacunacion.clases.Persona;
import java.util.Queue;

/**
 *
 * @author dsilv
 */
public class Vacunatorio {

    private String departamento; // Departamento al que pertenece
    private String barrio;       // Barrio en que se encuentra
    private String id;
    /*identificacion segun departamento barrio y numero
                        Ejemplo MOTC01 -> Montevideo Tres Cruces Vacunatorio 1
                        (puede haber varios por barrio)
                        Las 2 primeras letras depto. las otras dos barrio,
                        los dos nuemros el numero de vacunatorio en ese barrio
     */
    private Queue<Persona> colaDePersonas;
    private int vacunasDisponibles;

    public Vacunatorio(String depto, String bo, String nId) {
        departamento = depto;
        barrio = bo;
        id = nId;
    }

    public boolean agregarPersona(Persona p) {
        if (vacunasDisponibles > 0) {
            try {
                colaDePersonas.add(p);
                vacunasDisponibles--;
                return true;
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage() + " al cargar  "
                        + p.getCedula() + " al vacunatorio" + this.id);
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean borrarPersona(Persona p) {

        try {
            colaDePersonas.remove(p);
            vacunasDisponibles++;
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage() + " al borrar  "
                    + p.getCedula() + " del vacunatorio" + this.id);
            return false;
        }

    }

    public int getVacunasDisponibles() {
        return vacunasDisponibles;
    }

    public void setVacunasDisponibles(int vacunas) {
        vacunasDisponibles = vacunas;
    }

}
