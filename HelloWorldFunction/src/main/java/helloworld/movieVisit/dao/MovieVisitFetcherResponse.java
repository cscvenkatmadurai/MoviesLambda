package helloworld.movieVisit.dao;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieVisitFetcherResponse {
    private int numberOfMovieOfMoviesWatched;
    private int numberOfDistinctMoviesWatched;
    private List<MovieVisitById> movieVisitByIdList;

}

