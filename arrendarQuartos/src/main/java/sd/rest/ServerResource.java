package sd.rest;

import jakarta.ws.rs.*;

import java.sql.Date;
import java.util.List;

/**
 * Root resource (exposed at "server" path)
 */
@Path(value = "/client")
public class ClientResource {
    private Queries q;

    public ClientResource() {
        q = new Queries();
    }

    @Path("/ad")
    @POST
    @Consumes({"application/json"})
    public synchronized int postAd(Ad ad) {
        ad.setDate(new Date(System.currentTimeMillis()));
        ad.setState("inativo");

        return q.insertIntoTableAdvertisement(ad);
    }

    @Path("/ads")
    @GET
    @Produces({"application/json"})
    public synchronized List<Ad> searchAds(@QueryParam("description") String description, @QueryParam("local") String local, @QueryParam("aid") String aid) {
        String fields = "statead=ativo";
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
        msg.setDate(new Date(System.currentTimeMillis()));

        return q.insertIntoTableMessages(msg);
    }

    @Path("/msgs")
    @GET
    @Produces({"application/json"})
    public synchronized List<Message> getMessages(@QueryParam("aid") String aid) {
        return q.consultTableMessages(aid);
    }
}
