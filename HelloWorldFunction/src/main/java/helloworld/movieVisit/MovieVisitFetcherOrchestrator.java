package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import java.util.HashMap;

public class MovieVisitFetcherOrchestrator {
    private static final String BY_DATE = "date";
    private static final String BY_THEATRE = "theatre";
    private static final String BY_COUNT = "count";
    private static final String BY_LANG = "lang";


    private Map<String, MovieVisitFetcher> movieVisitFetcherMap;
    private final MovieVisitByDateFetcher movieVisitByDateFetcher;

    public MovieVisitFetcherOrchestrator() {
        movieVisitByDateFetcher = new MovieVisitByDateFetcher();
        movieVisitFetcherMap = new HashMap();
        movieVisitFetcherMap.put(BY_DATE, movieVisitByDateFetcher);
        movieVisitFetcherMap.put(BY_LANG, new MovieVisitByLangFetcher());
        movieVisitFetcherMap.put(BY_THEATRE, new MovieVisitByTheaterFetcher());
        movieVisitFetcherMap.put(BY_COUNT, new MovieVisitByCountFetcher());

    }



    public String  getMovieVisits(final JsonNode jsonNode) throws JsonProcessingException {
        final JsonNode queryStringParameters = jsonNode.get("queryStringParameters");
        final String userName = queryStringParameters.get("userName").asText();
        final String startTime =  queryStringParameters.get("startTime").asText() ;
        final String endTime = queryStringParameters.get("endTime").asText();
        final String by = queryStringParameters.has("by") ? queryStringParameters.get("by").asText() : BY_THEATRE;
        return movieVisitFetcherMap.containsKey(by) ? movieVisitFetcherMap.get(by).fetchMovieVisit(userName, startTime, endTime) : "";

    }


}


/*
queryStringParameters={endTime=1585763051480, startTime=0, userName=skven}
queryStringParameters={userName=skven, startTime=0, endTime=1585763051480}

 */