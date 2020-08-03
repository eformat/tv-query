package org.acme.submission;

import org.acme.data.Aggregation;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/query")
@ApplicationScoped
public class QueryResource {

    private final Logger log = LoggerFactory.getLogger(QueryResource.class);

    @Inject
    EntityManager entityManager;

    @GET
    @Path("/aggregates/{minutes}/route/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgRoute",
            summary = "get all N min rolling averages for all routes",
            description = "This operation returns all N minute rolling averages for all routes",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgRoute(@PathParam int minutes) {
        if(minutes == 1)
            return Aggregation.AvgRoute1.listAll();
        else if (minutes == 5)
            return Aggregation.AvgRoute5.listAll();
        return new ArrayList<>();
    }

    @GET
    @Path("/count/{minutes}/route/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countAvgRoute",
            summary = "count all N min rolling averages for all routes",
            description = "This operation returns a count of all N min rolling averages for all routes",
            deprecated = false,
            hidden = false)
    public Long countAvgRoute(@PathParam int minutes) {
        if(minutes == 1)
            return Aggregation.AvgRoute1.count();
        else if (minutes == 5)
            return Aggregation.AvgRoute5.count();
        return 0l;
    }

    @GET
    @Path("/aggregates/{minutes}/route/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgRouteById",
            summary = "get all N min rolling averages for all routes",
            description = "This operation returns all N minute rolling averages for all routes",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgRouteById(@PathParam int minutes, @PathParam String key) {
        if(minutes == 1)
            return Aggregation.AvgRoute1.find(" key = '" + key + "'").list();
        else if (minutes == 5)
            return Aggregation.AvgRoute5.find(" key = '" + key + "'").list();
        return new ArrayList<>();
    }

    @GET
    @Path("/aggregates/{minutes}/trip/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgTrip",
            summary = "get all N min rolling averages for all trips",
            description = "This operation returns all N minute rolling averages for all trips",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgTrip(@PathParam int minutes) {
        if(minutes == 1)
            return Aggregation.AvgTrip1.listAll();
        else if (minutes == 5)
            return Aggregation.AvgTrip5.listAll();
        return new ArrayList<>();
    }

    @GET
    @Path("/count/{minutes}/trip/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countAvgTrip",
            summary = "count all N min rolling averages for all trips",
            description = "This operation returns a count of all N min rolling averages for all trips",
            deprecated = false,
            hidden = false)
    public Long countAvgTrip(@PathParam int minutes) {
        if(minutes == 1)
            return Aggregation.AvgTrip1.count();
        else if (minutes == 5)
            return Aggregation.AvgTrip5.count();
        return 0l;
    }

    @GET
    @Path("/aggregates/{minutes}/trip/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgTripById",
            summary = "get all N min rolling averages for all trips",
            description = "This operation returns all N minute rolling averages for all trips",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgTripById(@PathParam int minutes, @PathParam String key) {
        if(minutes == 1)
            return Aggregation.AvgTrip1.find(" key = '" + key + "'").list();
        else if (minutes == 5)
            return Aggregation.AvgTrip5.find(" key = '" + key + "'").list();
        return new ArrayList<>();
    }

}
