package sd.rest;

import jakarta.ws.rs.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Root resource (exposed at "server" path)
 */
@Path(value = "/server")
public class ServerResource {
    private Queries q;

    public ServerResource() {
        q = new Queries();
    }

    @Path("/ad")
    @POST
    @Consumes({"application/json"})
    public synchronized int postAd(Ad ad) {
        return q.insertIntoTableAdvertisement(ad);
    }

    @Path("/ads")
    @GET
    @Produces({"application/json"})
    public synchronized List<Ad> getAds(@QueryParam("state") String state, @QueryParam("aid") String aid, @QueryParam("description") String description, @QueryParam("local") String local) {
        String fields = state;
        if(!aid.equals("") || aid != null)
            fields += aid;
        if(!description.equals("") || description != null)
            fields += description;
        if(!local.equals("") || local != null)
            fields += local;
        return q.consultTableAdvertisement(fields);
    }
}
