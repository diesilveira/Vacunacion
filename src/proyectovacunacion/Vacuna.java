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
public class Vacuna {
    private String tipo;
    
    
    public Vacuna(String tipoVacuna){
        tipo = tipoVacuna;
    }
    
    public String getTipoVacuna(){
     return tipo;
    }
}
