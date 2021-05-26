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
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.procesos.Seleccion;
import proyectovacunacion.procesos.Captura;
import proyectovacunacion.lectoresEscritores.LectorDeCriterios;

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
        Semaphore s_recepcion = new Semaphore(1, true);
        
        Semaphore s_consumido = new Semaphore(3, true);
        Semaphore s_actualizado = new Semaphore(0, true);
        
        Captura clienteWeb = new Captura("src/proyectovacunacion/archivos/SolicitudesWeb.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion1 = new Thread(clienteWeb); 
        Captura clienteApp = new Captura("src/proyectovacunacion/archivos/SolicitudesApp.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion2 = new Thread(clienteApp); 
        Captura clienteLinea = new Captura("src/proyectovacunacion/archivos/SolicitudesLinea.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion3 = new Thread(clienteLinea);
        
        //La Agenda se abre para determinados grupos prioritarios (Criterios).
        Queue <Criterio> criterios = new LinkedList<>(); 
        LectorDeCriterios criterioAgenda = new LectorDeCriterios ();
        criterios = criterioAgenda.generarCriterios("src/proyectovacunacion/archivos/CriteriosDeAgenda.csv");
        Semaphore s_criterio = new Semaphore(1, true);
        Seleccion clasificador1 = new Seleccion(s_recepcion, s_criterio, s_consumido, s_actualizado,recepcion, criterios);
        Thread hiloCriterio1 = new Thread(clasificador1);
        Seleccion clasificador2 = new Seleccion(s_recepcion, s_criterio,s_consumido, s_actualizado,recepcion, criterios);
        Thread hiloCriterio2 = new Thread(clasificador2);
        Seleccion clasificador3 = new Seleccion(s_recepcion, s_criterio,s_consumido, s_actualizado,recepcion, criterios);
        Thread hiloCriterio3 = new Thread(clasificador3); 
        
        
        
        
        hiloRecepcion1.start();
        hiloRecepcion2.start();
        hiloRecepcion3.start();
        hiloCriterio1.start();
        hiloCriterio2.start();
        hiloCriterio3.start();
    }    
}
