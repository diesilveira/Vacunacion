/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyectovacunacion.clases.Persona;
import proyectovacunacion.clases.Criterio;
import proyectovacunacion.clases.CriteriosActivos;
import proyectovacunacion.clases.Vacunatorio;
import proyectovacunacion.clases.VacunatoriosActivos;
import proyectovacunacion.procesos.Seleccion;
import proyectovacunacion.procesos.Captura;
import proyectovacunacion.lectoresEscritores.GeneradorDeCriterios;
import proyectovacunacion.lectoresEscritores.GeneradorDeVacunatorios;
import proyectovacunacion.procesos.Agendar;

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
        
        //La Agenda se abre para determinados grupos prioritarios (Criterios).
        CriteriosActivos criteriosActivos = new CriteriosActivos();
        GeneradorDeCriterios criterioAgenda = new GeneradorDeCriterios ();
        try {            
            criteriosActivos.setCriteriosDeAgenda(criterioAgenda.generarCriterios(criteriosActivos, "src/proyectovacunacion/archivos/CriteriosDeAgenda.csv"));
        } catch (InterruptedException ex) {
            Logger.getLogger(ProyectoVacunacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Seleccion clasificador1 = new Seleccion(s_recepcion, s_consumido, s_actualizado,recepcion, criteriosActivos.getCriteriosDeAgenda());
        Thread hiloCriterio1 = new Thread(clasificador1);
        Seleccion clasificador2 = new Seleccion(s_recepcion, s_consumido, s_actualizado,recepcion, criteriosActivos.getCriteriosDeAgenda());
        Thread hiloCriterio2 = new Thread(clasificador2);
        Seleccion clasificador3 = new Seleccion(s_recepcion, s_consumido, s_actualizado,recepcion, criteriosActivos.getCriteriosDeAgenda());
        Thread hiloCriterio3 = new Thread(clasificador3);
        
        //Se abren los vacunatoris disponibles
        
        VacunatoriosActivos vacunatoriosActivos = new VacunatoriosActivos();
        GeneradorDeVacunatorios generadorVacunatorios = new GeneradorDeVacunatorios();
        try {
            vacunatoriosActivos.setVacunatoriosActivos(generadorVacunatorios.generarVacunatorios(vacunatoriosActivos,"src/proyectovacunacion/archivos/Vacunatorios.csv"));
        } catch (InterruptedException ex) {
            Logger.getLogger(ProyectoVacunacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Agendar agenda1 = new Agendar(criteriosActivos, vacunatoriosActivos);
        Thread hiloAgenda1 = new Thread(agenda1);
        Agendar agenda2 = new Agendar(criteriosActivos, vacunatoriosActivos);
        Thread hiloAgenda2 = new Thread(agenda2);
        Agendar agenda3 = new Agendar(criteriosActivos, vacunatoriosActivos);
        Thread hiloAgenda3 = new Thread(agenda3);

        Captura clienteWeb = new Captura("src/proyectovacunacion/archivos/SolicitudesWeb.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion1 = new Thread(clienteWeb); 
        Captura clienteApp = new Captura("src/proyectovacunacion/archivos/SolicitudesApp.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion2 = new Thread(clienteApp); 
        Captura clienteLinea = new Captura("src/proyectovacunacion/archivos/SolicitudesLinea.csv", s_recepcion, s_consumido, s_actualizado, recepcion);
        Thread hiloRecepcion3 = new Thread(clienteLinea);
           
        hiloRecepcion1.start();
        hiloRecepcion2.start();
        hiloRecepcion3.start();
        hiloCriterio1.start();
        hiloCriterio2.start();
        hiloCriterio3.start();
        hiloAgenda1.start();
        hiloAgenda2.start();
        hiloAgenda3.start();
        
//        try {
//            criteriosActivos.setCriteriosDeAgenda(criterioAgenda.generarCriterios(criteriosActivos, "src/proyectovacunacion/archivos/CriteriosDeAgenda_2.csv"));
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ProyectoVacunacion.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }    
}
