package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.movieDetails.MovieDetailsAdder;
import helloworld.movieDetails.MovieDetailsConstants;
import helloworld.movieVisit.MovieVisitAdder;
import helloworld.movieVisit.MovieVisitConstants;
import helloworld.movieVisit.MovieVisitFetcherOrchestrator;
import helloworld.theatre.TheatreAdder;
import helloworld.theatre.TheatreConstants;
import helloworld.theatre.TheatreFetcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {
    private static final MovieDetailsAdder movieDetailsAdder = new MovieDetailsAdder();
    private static final TheatreAdder theatreAdder = new TheatreAdder();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TheatreFetcher theatreFetcher = new TheatreFetcher();
    private static final MovieVisitAdder movieVisitAdder = new MovieVisitAdder();
    private static final MovieVisitFetcherOrchestrator MOVIE_VISIT_FETCHER_ORCHESTRATOR = new MovieVisitFetcherOrchestrator();



    public Object handleRequest(final Object input, final Context context) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        context.getLogger().log("### hello \n" + input);


        try {

            final String result = orchestrate(input, context);
            return new GatewayResponse(result, headers, 200);
        } catch (IOException e) {
            return new GatewayResponse("{}", headers, 500);
        }
    }


    private String orchestrate(final Object input, final Context context) throws IOException {
        final JsonNode jsonNode = objectMapper.valueToTree(input);
        String result = "{}";

        if (isAddTheatreRequest(jsonNode, context)) {
            final JsonNode body = objectMapper.readTree(jsonNode.get("body").asText());
            context.getLogger().log("theatreAddition request with input " + body);
            theatreAdder.addTheatre(body);
        } else if (isAddMovieRequest(jsonNode, context)) {
            final JsonNode body = objectMapper.readTree(jsonNode.get("body").asText());
            context.getLogger().log("\n\n movieAddition request with input " + body);
            context.getLogger().log("\n\n imdbId:  " + body.get("imdbId"));
            result = movieDetailsAdder.addMovieDetails(body);
        } else if (isGetAllTheatresRequest(jsonNode, context)) {
            context.getLogger().log("\n\n getAllTheatresRequest");
            result = theatreFetcher.getAllTheatres();
        } else if (isAddMovieVisitRequest(jsonNode, context)) {
            final JsonNode body = objectMapper.readTree(jsonNode.get("body").asText());
            context.getLogger().log("\n\n addMovieVisitRequest");
            movieVisitAdder.addMovieVisit(body);
        } else if(isGetMovieVisitRequest(jsonNode, context)) {
            result = MOVIE_VISIT_FETCHER_ORCHESTRATOR.getMovieVisits(jsonNode);
        }
        return result;

    }

    private boolean isGetAllTheatresRequest(final JsonNode jsonNode, final Context context) {
        return isFieldEquals(jsonNode, "resource", TheatreConstants.THEATRE_RESOURCE) &&
                isFieldEquals(jsonNode, "httpMethod", "GET" );

    }

    private boolean isAddTheatreRequest(final JsonNode jsonNode, final Context context) {
        return isFieldEquals(jsonNode, "resource", TheatreConstants.THEATRE_RESOURCE) &&
                isFieldEquals(jsonNode, "httpMethod", "POST" );

    }

    private boolean isAddMovieRequest(final JsonNode jsonNode, final Context context) {
        return isFieldEquals(jsonNode, "resource", MovieDetailsConstants.MOVIE_RESOURCE_PATH) &&
                isFieldEquals(jsonNode, "httpMethod", "POST" );

    }

    private boolean isAddMovieVisitRequest(final JsonNode jsonNode, final Context context) {
        return isFieldEquals(jsonNode, "resource", MovieVisitConstants.MOVIE_VISIT_RESOURCE) &&
                isFieldEquals(jsonNode, "httpMethod", "POST" );

    }

    private boolean isGetMovieVisitRequest(final JsonNode jsonNode, final Context context) {
        return isFieldEquals(jsonNode, "resource", MovieVisitConstants.MOVIE_VISIT_RESOURCE) &&
                isFieldEquals(jsonNode, "httpMethod", "GET" );

    }


    private boolean isFieldEquals(JsonNode jsonNode, final String fieldName, final String expectedFiledValue) {
        return jsonNode.has(fieldName)  && expectedFiledValue.equals(jsonNode.get(fieldName).asText());
    }

}
