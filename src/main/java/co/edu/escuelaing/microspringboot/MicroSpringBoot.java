/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package co.edu.escuelaing.microspringboot;

import co.edu.escuelaing.httpserver.HttpServer;
import java.io.IOException;
import java.net.URISyntaxException;

/** Entry point that delegates to the reflective HTTP server. */
public class MicroSpringBoot {

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.main(args);
    }
}
