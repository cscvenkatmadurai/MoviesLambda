package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MovieVisitFetcher {

    public String fetchMovieVisit(final String username, final String startDate, final String endDate) throws JsonProcessingException;
}
