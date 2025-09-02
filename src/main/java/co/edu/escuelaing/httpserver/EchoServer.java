/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.escuelaing.httpserver;

/**
 *
 * @author emily.norena-c
 */
import java.net.*;
import java.io.*;

//Primero debemos ejecutar el servidor para que pueda escuchar al cliente
public class EchoServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null; //Crea la variable
        try {
            serverSocket = new ServerSocket(35000); //Crea el servidor con el puerto 35000
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null; //Crea la variable del cliente
        try {
            clientSocket = serverSocket.accept(); //Empieza a escuchar
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine; //Flujo de entrada y de salida
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje:" + inputLine);
            outputLine = "Respuesta: " + inputLine;
            out.println(outputLine);
            if (outputLine.equals("Respuesta: Bye.")) {
                break;
            }
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
