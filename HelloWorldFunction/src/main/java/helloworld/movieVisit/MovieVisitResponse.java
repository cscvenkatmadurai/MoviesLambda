package helloworld.movieVisit;

import helloworld.movieVisit.dao.MovieVisitMini;
import lombok.Data;
import java.util.List;

@Data
public class MovieVisitResponse {
    private int numberOfMovieOfMoviesWatched;
    private int numberOfUniqueMoviesWatched;
    private List<Entity> entityList;

}


@Data
class Entity {
    private int numberOfMovieOfMoviesWatched;
    private int numberOfUniqueMoviesWatched;
    private String id;
    private List<MovieVisitMini> movieList;

}
