/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.lectoresEscritores;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import proyectovacunacion.clases.Vacuna;
import proyectovacunacion.clases.Criterio;

/**
 *
 * @author danie
 */
public class GeneradorDeCriterios {    
       
    private String rutaArchivo;
    private Queue <Criterio> criteriosDeAgenda;

    public GeneradorDeCriterios() {
        this.criteriosDeAgenda = new LinkedList<Criterio>();        
    }

    public Queue<Criterio> getCriteriosDeAgenda() {
        return criteriosDeAgenda;
    }    
    
    public Queue <Criterio> generarCriterios (String rutaArchivo){
        String [] listaCriterios = ManejadorArchivosGenerico.leerArchivo
                                                      (rutaArchivo, false);
            for(String lineaLeida : listaCriterios){                
                String [] lineaAProcesar = lineaLeida.split("\\|");
                Criterio criterio = new Criterio(Integer.parseInt(lineaAProcesar[0].trim()),
                                        lineaAProcesar[1].trim(), new Vacuna (lineaAProcesar[2].trim()));
                criterio.setMutex(new Semaphore(1, true));
                criterio.setConsumido(new Semaphore(3, true));
                criterio.setActualizado(new Semaphore(0, true));
                criteriosDeAgenda.add(criterio);
        }
        return this.criteriosDeAgenda;
    }   
}
