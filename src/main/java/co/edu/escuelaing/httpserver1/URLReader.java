/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.escuelaing.httpserver1;
import java.net.*;
import java.io.*;

/**
 *
 * @author emily.norena-c
 */
public class URLReader {
    public static void main(String[] args) throws Exception{
        String site = "http://www.google.com/";
        //Crea el objeto que representa una URL
        URL siteURL = new URL(site);
        try (BufferedReader reader
                = new BufferedReader(new InputStreamReader(siteURL.openStream()))){
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x){
            System.err.println(x);
        }
    }
    
}

