package helloworld.movieDetails.db;

import com.omertron.omdbapi.model.OmdbVideoFull;

public interface MovieDBClient {

    public OmdbVideoFull getMovieDetails(final String IMDBId);
}
