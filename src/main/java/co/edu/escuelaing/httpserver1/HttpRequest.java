package co.edu.escuelaing.httpserver1;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request, including its path and query parameters.
 */
public class HttpRequest {
    private URI requri;
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(URI r) {
        this.requri = r;
        parseQueryParams();
    }

    /**
     * Extracts query parameters from the URI and stores them in a map.
     */
    private void parseQueryParams() {
        if (requri.getQuery() != null) {
            String[] params = requri.getQuery().split("&");
            for (String p : params) {
                String[] kv = p.split("=");
                if (kv.length == 2) {
                    queryParams.put(kv[0], kv[1]);
                }
            }
        }
    }

    /**
     * Retrieves the value of a query parameter by name.
     *
     * @param paramName the parameter name
     * @return the parameter value, or null if not present
     */
    public String getValue(String paramName) {
        return queryParams.get(paramName);
    }

    /**
     * Gets the path part of the request URI.
     *
     * @return the URI path
     */
    public String getPath() {
        return requri.getPath();
    }
}
