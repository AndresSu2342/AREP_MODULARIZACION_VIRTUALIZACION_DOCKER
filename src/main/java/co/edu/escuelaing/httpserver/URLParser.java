/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.escuelaing.httpserver;
import java.net.*;


/**
 *
 * @author emily.norena-c
 */
public class URLParser {
    public static void main(String[] args) throws MalformedURLException{
        URL personalurl = new URL ("http://ldbn.escuelaing.edu.co:5678/index.html?val=56&color=red"); //Falta ref
        System.out.println("Protocol: " + personalurl.getProtocol());
        System.out.println("Authority: " + personalurl.getAuthority());
        System.out.println("Host: " + personalurl.getHost());
        System.out.println("Port: " + personalurl.getPort());
        System.out.println("Path: " + personalurl.getPath());
        System.out.println("Query: " + personalurl.getQuery());
        System.out.println("File: " + personalurl.getFile());
        System.out.println("Ref: " + personalurl.getRef());
    }
    
}
