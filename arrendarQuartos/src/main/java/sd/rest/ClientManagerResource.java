package sd.rest;

import jakarta.ws.rs.*;

import java.util.List;

@Path("/manage")
public class ClientManagerResource {

    private Queries q;

    @Path("/ads")
    @GET
    @Produces({"application/json"})
    public synchronized List<Ad> getAdByState(@QueryParam("state") String state) {
        return q.consultTableAdvertisement(state);
    }

    @Path("/changead")
    @GET
    @Produces({"application/json"})
    public synchronized boolean changeAdState(@QueryParam("state") String state, @QueryParam("aid") String aid) {
        return q.alterTableAdvertisement(state, aid);
    }
}
