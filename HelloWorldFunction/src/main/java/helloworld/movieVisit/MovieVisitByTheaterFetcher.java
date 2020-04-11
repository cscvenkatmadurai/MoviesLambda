package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;
import helloworld.movieVisit.dao.MovieVisitMini;

import java.util.function.Function;

public class MovieVisitByTheaterFetcher implements MovieVisitFetcher {

    private static final Function<MovieVisitMini, String> GET_THEATRE_ID = (movieVisit) -> Long.toString(movieVisit.getTheatreId());
    private static final Function<MovieVisitMini, String> GET_THEATRE_ID_DISPLAY_TEXT = (movieVisit) -> movieVisit.getTheatreName() + " " + movieVisit.getTheatreLocation();
    final MovieVisitByIdFetcher movieVisitByIdFetcher = new MovieVisitByIdFetcher();
    @Override
    public String fetchMovieVisit(final String username, final String startDate, String endDate) throws JsonProcessingException {
        return movieVisitByIdFetcher.fetchMovieVisit(username, startDate, endDate, GET_THEATRE_ID, GET_THEATRE_ID_DISPLAY_TEXT);
    }
}
