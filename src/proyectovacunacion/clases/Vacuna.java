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
public class Vacuna {
    private String tipo;
    private int cantidad;
    
    public Vacuna(String tipoVacuna){
        this.tipo = tipoVacuna;
    }  
    
    public Vacuna(String tipoVacuna, int cantidad){
        this.tipo = tipoVacuna;
        this.cantidad = cantidad;
    }   

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    //metodo que suma mas cantidad de vacunas a las existentes
    public void setAgregarCantidad(int cantidadNueva) {
        this.cantidad = this.cantidad + cantidadNueva;
    }      
}
