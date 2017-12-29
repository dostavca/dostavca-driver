package si.dostavca.driver;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Log
@Metered
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("driver")
@RequestScoped
public class DriverResource {

    @Inject
    @DiscoverService(value = "dostavca-notifications")
    Optional<WebTarget> targetNotifications;

    @GET
    @Path("notifications")
    @CircuitBreaker
    @Fallback(fallbackMethod = "getNotificationsFallback")
    public Response getNotifications() {
        WebTarget service = targetNotifications.get().path("v1/notifications/all");

        Response response = service.request().get();

        return Response.fromResponse(response).build();
    }

    public Response getNotificationsFallback() {
        return Response.ok("{\"message\": \"Notifications are currently under maintenance.\"}").build();
    }

    @POST
    @Path("request")
    @CircuitBreaker
    @Fallback(fallbackMethod = "getNotificationsFallback")
    public Response requestDriver(Packet packet) {
        WebTarget service = targetNotifications.get().path("v1/notifications/add");

        Notification notification = new Notification("Packet " + packet.getPid() + " assigned to driver.");

        Response response = service.request().post(Entity.json(notification));

        if(response.getStatus() == 200) {
            return Response.ok("{\"message\": \"Packet " + packet.getPid() + " assigned to driver.\"}").build();
        } else {
            return getNotificationsFallback();
        }

    }

}
