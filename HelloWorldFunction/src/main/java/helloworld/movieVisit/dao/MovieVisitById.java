package helloworld.movieVisit.dao;

import lombok.Data;

import java.util.List;

@Data
public class MovieVisitById {
    private int numberOfMovieOfMoviesWatched;
    private int numberOfDistinctMoviesWatched;
    private String id;
    private List<MovieVisitMini> movieList;

}
