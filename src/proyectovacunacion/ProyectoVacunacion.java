/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.hilos.ReceptorSolicitud;

/**
 *
 * @author dsilv
 */
public class ProyectoVacunacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Se simula la recepción de solicitudes de diferentes clientes.
        Queue <Persona> recepcion = new LinkedList<>();        
        Semaphore semaphore = new Semaphore(1, true);
        ReceptorSolicitud clienteWeb = new ReceptorSolicitud("src/proyectovacunacion/archivos/SolicitudesWeb.csv", semaphore, recepcion);
        ReceptorSolicitud clienteApp = new ReceptorSolicitud("src/proyectovacunacion/archivos/SolicitudesApp.csv", semaphore, recepcion);
        ReceptorSolicitud clienteLinea = new ReceptorSolicitud("src/proyectovacunacion/archivos/SolicitudesLinea.csv", semaphore, recepcion);
        Thread hiloRecepcion1 = new Thread(clienteWeb);      
        hiloRecepcion1.start();
        Thread hiloRecepcion2 = new Thread(clienteApp);      
        hiloRecepcion2.start();
        Thread hiloRecepcion3 = new Thread(clienteLinea);      
        hiloRecepcion3.start();
    }    
}
