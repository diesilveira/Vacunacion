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
public class Server {

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            InetAddress addr = InetAddress.getByName("192.168.56.1");
            serverSocket = new ServerSocket(port, 50, addr);
            //Local
            //serverSocket = new ServerSocket(port);
            boolean init = true;
            while (init) {
                init = false;
                new ClientHandler(serverSocket.accept(), this).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
