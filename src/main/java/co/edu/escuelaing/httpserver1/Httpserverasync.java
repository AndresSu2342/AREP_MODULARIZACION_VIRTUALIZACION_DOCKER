/*
 * Simple Asynchronous HTTP Server
 *
 * This class implements a basic asynchronous HTTP server
 * that can serve static files and handle custom services
 * mapped to specific routes.
 */

package co.edu.escuelaing.httpserver1;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Httpserverasync {

    /**
     * Map of registered services associated with specific routes
     */
    public static Map<String, Service> services = new HashMap();

    /**
     * Default path to static files
     */
    private static String staticFilesPath = "webroot";

    /**
     * Main entry point - starts the server.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        start(args);
    }

    /**
     * Registers a service (handler) for a specific HTTP path.
     * @param path the requested path
     * @param s the service implementation
     */
    public static void get(String path, Service s){
        services.put(path, s);
    }

    /**
     * Configures the local path to static files.
     * @param localFilesPath path where static files are stored
     */
    public static void staticfiles(String localFilesPath){
        staticFilesPath = localFilesPath;
    }

    /**
     * Starts the server execution.
     * @param args program arguments
     */
    public static void start(String[] args){
        runServer(args);
    }

    /**
     * Initializes and runs the HTTP server.
     * @param args program arguments
     */
    private static void runServer(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(35000)){
            boolean running = true;
            while(running){
                System.out.println("Ready to receive ...");
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Handles an individual client request.
     * Reads the request, processes it and sends back the response.
     * @param clientSocket the socket connected to the client
     */
    private static void handleClient(Socket clientSocket){
        try(
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ){
            String inputLine;
            String requestLine = in.readLine();
            if(requestLine == null) return;

            URI requri = new URI(requestLine.split(" ")[1]);
            HttpRequest req = new HttpRequest(requri);
            HttpResponse res = new HttpResponse();

            System.out.println("Requested path: " + req.getPath());

            String outputLine;

            // Check if a registered service exists for the path
            if(services.containsKey(req.getPath())){
                Service s = services.get(req.getPath());
                String result = s.invoke(res, req);
                res.setBody(result);
                outputLine = res.buildResponse();
            } else {
                // If not a service, try to serve a static file
                outputLine = serveStaticFile(req.getPath(), res);
            }

            out.println(outputLine);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Attempts to serve a static file from the configured directory.
     * @param path the requested file path
     * @param res the HTTP response object
     * @return the full HTTP response as a string
     */
    private static String serveStaticFile(String path, HttpResponse res){
        try {
            String filePath = "target/classes" + staticFilesPath + path;
            if(Files.exists(Paths.get(filePath))){
                String mimeType = Files.probeContentType(Paths.get(filePath));
                res.setHeader("Content-Type", mimeType != null ? mimeType : "text/plain");
                String body = new String(Files.readAllBytes(Paths.get(filePath)));
                res.setBody(body);
                return res.buildResponse();
            } else {
                res.setStatus(404);
                res.setBody("<h1>404 Not Found</h1>");
                return res.buildResponse();
            }
        } catch (IOException e){
            res.setStatus(500);
            res.setBody("<h1>500 Internal Server Error</h1>");
            return res.buildResponse();
        }
    }
}
