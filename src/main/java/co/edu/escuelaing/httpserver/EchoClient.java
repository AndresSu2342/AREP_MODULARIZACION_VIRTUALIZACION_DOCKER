/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.escuelaing.httpserver;

/**
 *
 * @author emily.norena-c
 */
import java.io.*;
import java.net.*;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        Socket echoSocket = null; //Socket de cliente
        PrintWriter out = null; //Entrada
        BufferedReader in = null; //Salida
        try {
            echoSocket = new Socket("127.0.0.1", 35000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream())); //Flujo constante
        } catch (UnknownHostException e) {
            System.err.println("Don’t know about host!.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn’t get I/O for "
                    + "the connection to: localhost.");
            System.exit(1);
        }
        BufferedReader stdIn = new BufferedReader( //Lee del teclado
                new InputStreamReader(System.in));
        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine()); //Imprime lo que devuelve el servidor
        }
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
