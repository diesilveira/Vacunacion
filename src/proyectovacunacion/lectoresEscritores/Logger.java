/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.lectoresEscritores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author meki
 */

public class Logger {
    
  /**
     * Método que escribe un un String [] en un archivo.
     * @param nombreCompletoArchivo
     * @param listaLineasArchivo lista con las lineas del archivo
     */
    
    private static final String archivoLog = "src/proyectovacunacion/archivos/logs.txt";
    
    public void escribirLog(String nombreHilo,
            String listaLineasArchivo) {
        FileWriter fw;
        try {
            fw = new FileWriter(archivoLog, true);
            BufferedWriter bw = new BufferedWriter(fw);
    
            DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            bw.write("[FECHA: "+dateFormat.format(date)+"] [HILO: " + nombreHilo +"] EVENTO: "+listaLineasArchivo);
            bw.newLine();
            
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo "
                    + archivoLog);
        }
    }
    
}
