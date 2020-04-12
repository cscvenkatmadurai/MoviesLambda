package helloworld.movieVisit;

import helloworld.movieVisit.dao.MovieVisitMini;

import java.util.List;

public class MovieVisitHelper {

    public static int getNumberOfDistinctMovies(final List<MovieVisitMini> movieVisitMinis) {
        return (int)movieVisitMinis.stream().
                map(MovieVisitMini::getImdbId).
                distinct().count();
    }
}

