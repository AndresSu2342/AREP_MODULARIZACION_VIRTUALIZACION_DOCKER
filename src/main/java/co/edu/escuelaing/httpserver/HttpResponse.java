package co.edu.escuelaing.httpserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response, including status, headers, and body.
 */
public class HttpResponse {
    private int status = 200;
    private String reason = "OK";
    private String body = "";
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse() {
        headers.put("Content-Type", "text/html; charset=UTF-8");
    }

    /**
     * Sets the HTTP status code for the response.
     *
     * @param status the status code (e.g., 200, 404, 500)
     */
    public void setStatus(int status) {
        this.status = status;
        this.reason = switch (status) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "OK";
        };
        }

        /**
         * Sets the body content of the response.
         *
         * @param body the response body as a string
         */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Adds or replaces a header in the response.
     *
     * @param key   the header name
     * @param value the header value
     */
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Builds the raw HTTP response string to be sent to the client.
     *
     * @return the full HTTP response as a string
     */
    public String buildResponse() {
        StringBuilder response = new StringBuilder("HTTP/1.1 ")
                .append(status).append(' ').append(reason).append("\r\n");
        headers.forEach((k, v) -> response.append(k).append(": ").append(v).append("\r\n"));
        response.append("\r\n").append(body);
        return response.toString();
    }
}
