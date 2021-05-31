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

/**
 *
 * @author meki
 */

public class LoggerSistema {
    
  /**
     * Método que escribe un un String [] en un archivo.
     * @param nombreCompletoArchivo
     * @param listaLineasArchivo lista con las lineas del archivo
     */
    
    private static final String archivoLogSitema = "src/proyectovacunacion/archivos/logSitema.txt";
    
    public void escribirLog(
            String listaLineasArchivo) {
        FileWriter fw;
        try {
            fw = new FileWriter(archivoLogSitema, true);
            BufferedWriter bw = new BufferedWriter(fw);
    
            DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            bw.write("[FECHA: "+dateFormat.format(date)+"] EVENTO: "+listaLineasArchivo);
            bw.newLine();
            
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo "
                    + archivoLogSitema);
        }
    }
    
}
