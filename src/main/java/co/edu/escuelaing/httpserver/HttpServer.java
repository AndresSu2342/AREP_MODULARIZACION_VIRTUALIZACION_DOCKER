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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {

    // --- Controllers & Services ---
    public static Map<String, Method> services = new HashMap<>();
    public static Map<String, Object> controllers = new HashMap<>();

    // --- Server state ---
    private static String staticFilesPath = System.getenv().getOrDefault("STATIC", "target/classes/webroot");
    private static final int PORT = 35000;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static ServerSocket serverSocket;

    // --- Load annotated components ---
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

    public static void main(String[] args) throws IOException, URISyntaxException {
        start(args);
    }

    public static void start(String[] args){
        loadComponents(args);
        runServer();
    }

    private static void runServer(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("HTTP server started on port " + PORT);

            while (running.get()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running.get()) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Gracefully stop the server
     */
    public static void stop() {
        running.set(false);
        threadPool.shutdown(); // Reject new tasks, finish current ones
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server stopped gracefully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket){
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                System.out.println("Header: " + headerLine);
            }

            URI requri = new URI(requestLine.split(" ")[1]);
            HttpRequest req = new HttpRequest(requri);
            HttpResponse res = new HttpResponse();

            System.out.println("Requested path: " + req.getPath());

            String outputLine;
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
                    args[i] = value;
                }

                String result = (String) m.invoke(controller, args);
                res.setBody(result);
                outputLine = res.buildResponse();
            } else {
                outputLine = serveStaticFile(req.getPath(), res);
            }

            out.println(outputLine);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private static String serveStaticFile(String path, HttpResponse res){
        try {
            if (path.equals("/")) {
                path = "/index.html";
            }
            String filePath = staticFilesPath + path;
            if(Files.exists(Paths.get(filePath))){
                String mimeType;
                if (path.endsWith(".css")) {
                    mimeType = "text/css";
                } else if (path.endsWith(".js")) {
                    mimeType = "application/javascript";
                } else if (path.endsWith(".html")) {
                    mimeType = "text/html";
                } else {
                    mimeType = Files.probeContentType(Paths.get(filePath));
                    if (mimeType == null) {
                        mimeType = "text/plain";
                    }
                }
                res.setHeader("Content-Type", mimeType);
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