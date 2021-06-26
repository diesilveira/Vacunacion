/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.net.*;
import java.io.*;

/**
 *
 * @author danie
 */
public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private final Server server;//
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, Server server) {//
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    out.println("good bye");
                    break;
                }
                out.println("Recibido: " + inputLine);
            }
            in.close();
            out.close();
            clientSocket.close();
            this.server.stop();//
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
