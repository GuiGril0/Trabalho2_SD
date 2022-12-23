package sd.rest;

import jakarta.ws.rs.*;

import java.util.List;

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
    public synchronized List<Ad> searchAds(@QueryParam("state") String state, @QueryParam("description") String description, @QueryParam("local") String local, @QueryParam("aid") String aid) {
        String fields = "statead=" + state;
        if(state == null) {
            fields = "aid=" + aid;
            return q.consultTableAdvertisement(fields);
        }
        if(description == null && local == null && aid == null)
            return q.consultTableAdvertisement(fields);
        else if(aid == null) {
            if(local == null || local.trim().length() == 0) {
                fields += "&description=" + description;
                return q.consultTableAdvertisement(fields);
            }
            else {
                fields += "&description=" + description + "&localad=" + local;
                return q.consultTableAdvertisement(fields);
            }
        }
        else {
            fields += "&aid=" + aid;
            return q.consultTableAdvertisement(fields);
        }
    }

    @Path("/msg")
    @POST
    @Consumes({"application/json"})
    public synchronized boolean sendMessage(Message msg) {
        return q.insertIntoTableMessages(msg);
    }

    @Path("/msgs")
    @GET
    @Produces({"application/json"})
    public synchronized List<Message> getMessages(@QueryParam("aid") String aid) {
        return q.consultTableMessages(aid);
    }

    @Path("/changead")
    @POST
    @Consumes({"application/json"})
    public synchronized boolean changeAdState(@QueryParam("state") String state, @QueryParam("aid") String aid) {
        return q.alterTableAdvertisement(state, aid);
    }
}
