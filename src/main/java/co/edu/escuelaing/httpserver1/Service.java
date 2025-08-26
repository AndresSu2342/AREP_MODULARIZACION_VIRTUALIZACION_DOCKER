package co.edu.escuelaing.httpserver1;

/**
 * Defines a service endpoint that can be invoked
 * with an HTTP request and response.
 */
public interface Service {
    /**
     * Executes the service logic for the given request and response.
     *
     * @param res the HTTP response object to populate
     * @param req the HTTP request object containing client data
     * @return a string representation of the response body
     */
    String invoke(HttpResponse res, HttpRequest req);
}