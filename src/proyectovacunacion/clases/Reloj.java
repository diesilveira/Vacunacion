/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.concurrent.Semaphore;

/**
 *
 * @author dsilv
 */
public class Reloj {

    private int contador;
    private long sumaDeTiempos;
    public Semaphore mutex;

    public void setContador(int contador) {
        this.contador = contador;
        this.mutex = new Semaphore(1);
    }

    public long getSumaDeTiempos() {
        return sumaDeTiempos;
    }

    public int getContador() {
        return contador;
    }

    public Reloj() {
        this.contador = 0;
        this.sumaDeTiempos = 0;
    }

    public void agregarSumaDeTiempos(long n) {

        this.sumaDeTiempos += n;
        this.contador++;
    }

    public long obtenerPromedioFinal() {
        return (this.sumaDeTiempos / this.contador);
    }

}
