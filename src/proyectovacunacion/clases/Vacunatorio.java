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
    public String departamento; // Departamento al que pertenece
    public String barrio;       // Barrio en que se encuentra
    public String id; /*identificacion segun departamento barrio y numero
                        Ejemplo MOTC01 -> Montevideo Tres Cruces Vacunatorio 1
                        (puede haber varios por barrio)
                        Las 2 primeras letras depto. las otras dos barrio,
                        los dos nuemros el numero de vacunatorio en ese barrio
                        */
    public Queue<Persona> colaDePersonas;
    
    public Vacunatorio(String depto, String bo, String nId){
        departamento = depto;
        barrio = bo;
        id = nId;
    }
    
    public boolean agregarPersona(Persona p){
        try {
            colaDePersonas.add(p);
            return true;
        } catch (Exception ex){
            System.out.println("Error "+ ex.getMessage() + " al cargar  "
                    + p.getCedula() + " al vacunatorio" + this.id);
            return false;
        }
}
}
