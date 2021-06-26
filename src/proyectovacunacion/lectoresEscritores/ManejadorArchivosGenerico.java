package proyectovacunacion.lectoresEscritores;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que permite leer y escribir archivos que contienen cadenas de
 * caracteres.
 */
public class ManejadorArchivosGenerico {

    /**
     * Método que lee un archivo.
     *
     * @param nombreCompletoArchivo
     * @param ignoreHeader
     * @return
     */
    public static String[] leerArchivo(String nombreCompletoArchivo, boolean ignoreHeader) {
        FileReader fr;
        ArrayList<String> listaLineasArchivo = new ArrayList<>();
        try {
            fr = new FileReader(nombreCompletoArchivo);
            try (BufferedReader br = new BufferedReader(fr)) {
                String lineaActual = br.readLine();
                if (ignoreHeader) {
                    lineaActual = br.readLine();
                }
                while (lineaActual != null) {
                    listaLineasArchivo.add(lineaActual);
                    lineaActual = br.readLine();
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error al leer el archivo "
                    + nombreCompletoArchivo);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo "
                    + nombreCompletoArchivo);
        }
//		System.out.println("Archivo leido satisfactoriamente");

        return listaLineasArchivo.toArray(new String[0]);
    }
}
