/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion;

/**
 *
 * @author dsilv
 */
public class Persona {
    private String cedula;
    private String departamento;
    private String barrio;
    private String grupoPrioritario;
    private int edad;
    
    public Persona(String ced, String depto, String bo){
        cedula = ced;
        departamento = depto;
        barrio = bo;
    }
    
    public void setGrupoPrioritario(String gp){
        grupoPrioritario = gp;
    }
    
    public String getGrupoPrioritario(){
        return grupoPrioritario;
    }
    
    public String getCedula(){
        return cedula;
    }
    
    public String getDepartamento(){
        return departamento;
    }
    
     public String getBarrio(){
        return barrio;
    }
    
    public int getEdad(){
        return edad;
    }
}
