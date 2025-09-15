package co.edu.escuelaing.httpserver;

import co.edu.escuelaing.annotations.GetMapping;
import co.edu.escuelaing.annotations.RequestParam;
import co.edu.escuelaing.annotations.RestController;

import java.net.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {

    /**
     * Map of registered services associated with specific routes
     * Key = path, Value = Method to invoke
     */
    public static Map<String, Method> services = new HashMap<>();

    /**
     * Map of controller instances (object) to invoke methods on
     */
    public static Map<String, Object> controllers = new HashMap<>();

    private static void loadComponents(String[] args) {
        try {
            Class<?> c = Class.forName(args[0]);
            if (c.isAnnotationPresent(RestController.class)) {
                Object controllerInstance = c.getDeclaredConstructor().newInstance();
                Method[] methods = c.getDeclaredMethods();

                for(Method m: methods){
                    if (m.isAnnotationPresent(GetMapping.class)){
                        String mapping = m.getAnnotation(GetMapping.class).value();
                        services.put(mapping, m);
                        controllers.put(mapping, controllerInstance);
                        System.out.println("Mapped " + mapping + " -> " + m.getName());
                    }
                }
            }
        } catch (Exception ex){
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
     * Registers a service (legacy version with Service interface).
     * NOTE: not used with reflection-based controllers,
     * but kept for backward compatibility.
     */
    public static void get(String path, Service s){
        // ya no compatible directo porque services ahora guarda Method
        throw new UnsupportedOperationException("Use @RestController with @GetMapping instead");
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
        loadComponents(args);
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
            String requestLine = in.readLine();
            if(requestLine == null) return;

            URI requri = new URI(requestLine.split(" ")[1]);
            HttpRequest req = new HttpRequest(requri);
            HttpResponse res = new HttpResponse();

            System.out.println("Requested path: " + req.getPath());

            String outputLine;

            // Check if a registered service exists for the path
            if (services.containsKey(req.getPath())) {
                Method m = services.get(req.getPath());
                Object controller = controllers.get(req.getPath());

                Object[] args = new Object[m.getParameterCount()];
                java.lang.annotation.Annotation[][] paramAnnotations = m.getParameterAnnotations();

                for (int i = 0; i < m.getParameterCount(); i++) {
                    String value = null;
                    for (java.lang.annotation.Annotation a : paramAnnotations[i]) {
                        if (a instanceof RequestParam) {
                            RequestParam rp = (RequestParam) a;
                            value = req.getQueryParams().getOrDefault(rp.value(), rp.defaultValue());
                        }
                    }
                    args[i] = value; // ojo: si el método espera String funciona directo
                }

                String result = (String) m.invoke(controller, args);
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
