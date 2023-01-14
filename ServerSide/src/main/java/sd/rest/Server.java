package sd.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.net.URI;
import java.util.Properties;

/**
 * Main class.
 *
 */
public class Server {
    // Base URI the Grizzly HTTP server will listen on
    public static final URI BASE_URI = getBaseURI();

    private static int getPort(int defaultPort) {
        return defaultPort;
    }

    private static URI getBaseURI() {
        String baseuri = new String();

        try {
            InputStream is = new FileInputStream("src/main/resources/configs.properties");
            Properties p = new Properties();
            p.load(is);
            baseuri = p.getProperty("baseuri");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return URI.create(baseuri);
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() throws IOException{
        // create a resource config that scans for JAX-RS resources and providers
        // in sd.rest package
        final ResourceConfig rc = new ResourceConfig().packages("sd.rest");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Starting grizzly...");
        HttpServer server = startServer();

        System.out.println("\n## Server on at " + BASE_URI);
        System.out.println("\n## Hit enter to stop the server...");

        System.in.read();

        //após pressionar o enter
        server.stop();
    }
}

