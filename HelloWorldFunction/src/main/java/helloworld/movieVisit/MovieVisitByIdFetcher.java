package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.movieVisit.dao.MovieVisitById;
import helloworld.movieVisit.dao.MovieVisitFetcherResponse;
import helloworld.movieVisit.dao.MovieVisitMini;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MovieVisitByIdFetcher {

    final MovieVisitByDateFetcher movieVisitByDateFetcher = new MovieVisitByDateFetcher();
    final ObjectMapper objectMapper = new ObjectMapper();



    public String fetchMovieVisit(final String username, final String startDate, final String endDate, final Function<MovieVisitMini, String> getId, final Function<MovieVisitMini, String> getIdText)  throws JsonProcessingException {
        final List<MovieVisitMini> movieVisitMinis = movieVisitByDateFetcher.fetchMovieVisitByDate(username, startDate, endDate);

        final Map<String, List<MovieVisitMini>> langToMovieVisitMap = getIdToMovieVisitMap(movieVisitMinis, getId);

        final List<MovieVisitById> movieVisitByIdList = langToMovieVisitMap.keySet().stream().map(key -> {
            final MovieVisitById movieVisitById = new MovieVisitById();
            final List<MovieVisitMini> moviesWatched = langToMovieVisitMap.get(key);
            movieVisitById.setId(getIdText.apply(moviesWatched.get(0)));
            movieVisitById.setNumberOfMovieOfMoviesWatched(moviesWatched.size());
            movieVisitById.setNumberOfDistinctMoviesWatched(getNumberOfDistinctMovies(moviesWatched));
            moviesWatched.sort(Comparator.comparing(MovieVisitMini::getWatchedDateInMillisecond).reversed());
            movieVisitById.setMovieList(moviesWatched);
            return movieVisitById;

        }).sorted(Comparator.comparing(MovieVisitById::getNumberOfMovieOfMoviesWatched).reversed()).
                collect(Collectors.toList());

        final MovieVisitFetcherResponse movieVisitFetcherResponse = MovieVisitFetcherResponse.builder().
                numberOfMovieOfMoviesWatched(movieVisitMinis.size()).
                numberOfDistinctMoviesWatched(getNumberOfDistinctMovies(movieVisitMinis)).
                movieVisitByIdList(movieVisitByIdList).
                build();


        return objectMapper.writeValueAsString(movieVisitFetcherResponse);
    }

    private  Map<String, List<MovieVisitMini>> getIdToMovieVisitMap(List<MovieVisitMini> movieVisitMinis, final Function<MovieVisitMini, String> getId) {
        final Map<String, List<MovieVisitMini>> idToMovieVisit = new HashMap<>();
        for (final MovieVisitMini movieVisit: movieVisitMinis) {
            final String id = getId.apply(movieVisit);
            if(idToMovieVisit.containsKey(id)) {
                idToMovieVisit.get(id).add(movieVisit);
            } else {
                final List<MovieVisitMini> movieVisitMiniList = new ArrayList<>();
                movieVisitMiniList.add(movieVisit);
                idToMovieVisit.put(id, movieVisitMiniList);
            }

        }
        return idToMovieVisit;
    }

    private int getNumberOfDistinctMovies(final List<MovieVisitMini> movieVisitMinis) {
        return (int)movieVisitMinis.stream().
                map(MovieVisitMini::getImdbId).
                distinct().count();
    }
}
