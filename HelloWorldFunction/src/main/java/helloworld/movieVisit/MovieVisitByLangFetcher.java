package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.movieVisit.dao.MovieVisitMini;

import java.util.function.Function;

public class MovieVisitByLangFetcher implements MovieVisitFetcher {

    final MovieVisitByDateFetcher movieVisitByDateFetcher = new MovieVisitByDateFetcher();
    final ObjectMapper objectMapper = new ObjectMapper();
    final MovieVisitByIdFetcher movieVisitByIdFetcher = new MovieVisitByIdFetcher();
    private final static Function<MovieVisitMini, String> GET_LANG_ID = (movieVisitMini -> movieVisitMini.getWatchedLang().trim().toLowerCase());

    @Override
    public String fetchMovieVisit(final String username, final String startDate, final String endDate) throws JsonProcessingException {
        return movieVisitByIdFetcher.fetchMovieVisit(username, startDate, endDate, GET_LANG_ID, GET_LANG_ID);

    }

}
