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
    public Semaphore mutex;

    public Reloj() {
        this.contador = 0;
        this.mutex = new Semaphore(1);
    }

    public void setContador(int contador) {
        this.contador = contador;
        }

    public int getContador() {
        return contador;
    }

    public void agregarCuenta() throws InterruptedException {
        mutex.acquire();
        this.contador++;
        mutex.release();
    }
}
