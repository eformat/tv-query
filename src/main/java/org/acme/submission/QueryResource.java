package org.acme.submission;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.acme.data.Aggregation;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.annotations.SseElementType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
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
        if (minutes == 1)
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
        if (minutes == 1)
            return Aggregation.AvgRoute1.count();
        else if (minutes == 5)
            return Aggregation.AvgRoute5.count();
        return 0l;
    }

    @GET
    @Path("/aggregates/{minutes}/route/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgRouteById",
            summary = "get all N min rolling averages for all routes by id",
            description = "This operation returns all N minute rolling averages for all routes by id",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgRouteById(@PathParam int minutes, @PathParam String key) {
        if (minutes == 1)
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
        if (minutes == 1)
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
        if (minutes == 1)
            return Aggregation.AvgTrip1.count();
        else if (minutes == 5)
            return Aggregation.AvgTrip5.count();
        return 0l;
    }

    @GET
    @Path("/aggregates/{minutes}/trip/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgTripById",
            summary = "get all N min rolling averages for all trips by id",
            description = "This operation returns all N minute rolling averages for all trips by id",
            deprecated = false,
            hidden = false)
    public List<Aggregation> getAvgTripById(@PathParam int minutes, @PathParam String key) {
        if (minutes == 1)
            return Aggregation.AvgTrip1.find(" key = '" + key + "'").list();
        else if (minutes == 5)
            return Aggregation.AvgTrip5.find(" key = '" + key + "'").list();
        return new ArrayList<>();
    }


    @GET
    @Path("/stream/aggregates/{minutes}/route/")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgRouteStream",
            summary = "stream all N min rolling averages for all routes",
            description = "This operation allows you to stream server side events for all N minute rolling averages for all routes",
            deprecated = false,
            hidden = false)
    public Publisher<Aggregation> getAvgRouteStream(@PathParam int minutes) {
        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofSeconds(3)).onOverflow().drop();
        return ticks.on().request(subscription -> log.info("We are subscribed!"))
                .on().cancellation(() -> log.info("Downstream has cancelled the interaction"))
                .onFailure().invoke(failure -> log.warn("Failed with " + failure.getMessage()))
                .onCompletion().invoke(() -> log.info("Completed"))
                .onItem().transformToMulti(
                        x -> avgRouteMulti(minutes)
                ).merge();
    }

    private Multi<Aggregation> avgRouteMulti(int minutes) {
        return Multi.createFrom().iterable(
                getAvgRoute(minutes)
        ).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @GET
    @Path("/stream/aggregates/{minutes}/route/{key}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgRouteStreamId",
            summary = "stream all N min rolling averages for all routes by id",
            description = "This operation allows you to stream server side events for all N minute rolling averages for all routes by id",
            deprecated = false,
            hidden = false)
    public Publisher<Aggregation> getAvgRouteIdStream(@PathParam int minutes, @PathParam String key) {
        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofSeconds(3)).onOverflow().drop();
        return ticks.on().request(subscription -> log.info("We are subscribed!"))
                .on().cancellation(() -> log.info("Downstream has cancelled the interaction"))
                .onFailure().invoke(failure -> log.warn("Failed with " + failure.getMessage()))
                .onCompletion().invoke(() -> log.info("Completed"))
                .onItem().transformToMulti(
                        x -> avgRouteMultiId(minutes, key)
                ).merge();
    }

    private Multi<Aggregation> avgRouteMultiId(int minutes, String key) {
        return Multi.createFrom().iterable(
                getAvgRouteById(minutes, key)
        ).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @GET
    @Path("/stream/aggregates/{minutes}/trip/")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgTripStream",
            summary = "stream all N min rolling averages for all trips",
            description = "This operation allows you to stream server side events for all N minute rolling averages for all trips",
            deprecated = false,
            hidden = false)
    public Publisher<Aggregation> getAvgTripStream(@PathParam int minutes) {
        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofSeconds(3)).onOverflow().drop();
        return ticks.on().request(subscription -> log.info("We are subscribed!"))
                .on().cancellation(() -> log.info("Downstream has cancelled the interaction"))
                .onFailure().invoke(failure -> log.warn("Failed with " + failure.getMessage()))
                .onCompletion().invoke(() -> log.info("Completed"))
                .onItem().transformToMulti(
                        x -> avgTripMulti(minutes)
                ).merge();
    }

    private Multi<Aggregation> avgTripMulti(int minutes) {
        return Multi.createFrom().iterable(
                getAvgTrip(minutes)
        ).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @GET
    @Path("/stream/aggregates/{minutes}/trip/{key}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvgTripIdStream",
            summary = "stream all N min rolling averages for all trips by id",
            description = "This operation allows you to stream server side events for all N minute rolling averages for all trips by id",
            deprecated = false,
            hidden = false)
    public Publisher<Aggregation> getAvgTripIdStream(@PathParam int minutes, @PathParam String key) {
        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofSeconds(3)).onOverflow().drop();
        return ticks.on().request(subscription -> log.info("We are subscribed!"))
                .on().cancellation(() -> log.info("Downstream has cancelled the interaction"))
                .onFailure().invoke(failure -> log.warn("Failed with " + failure.getMessage()))
                .onCompletion().invoke(() -> log.info("Completed"))
                .onItem().transformToMulti(
                        x -> avgTripIdMulti(minutes, key)
                ).merge();
    }

    private Multi<Aggregation> avgTripIdMulti(int minutes, String key) {
        return Multi.createFrom().iterable(
                getAvgTripById(minutes, key)
        ).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }
}
