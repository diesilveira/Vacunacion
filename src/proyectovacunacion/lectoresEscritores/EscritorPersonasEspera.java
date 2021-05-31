/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.lectoresEscritores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import proyectovacunacion.clases.Persona;

/**
 *
 * @author meki
 */

public class EscritorPersonasEspera {
    
  /**
     * Método que escribe un un String [] en un archivo.
     * @param nombreCompletoArchivo
     * @param listaLineasArchivo lista con las lineas del archivo
     */
    
    private static final String archivoEspera = "src/proyectovacunacion/archivos/personasEspera.txt";
    
    public void escribirLog(Persona persona) {
        FileWriter fw;
        try {
            fw = new FileWriter(archivoEspera, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(persona.datosPersona());
            bw.newLine();
            
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo "
                    + archivoEspera);
        }
    }
    
}
