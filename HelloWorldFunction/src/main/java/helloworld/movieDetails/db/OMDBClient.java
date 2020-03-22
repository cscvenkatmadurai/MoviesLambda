package helloworld.movieDetails.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.omdbapi.OMDBException;
import com.omertron.omdbapi.OmdbApi;
import com.omertron.omdbapi.model.OmdbVideoFull;
import com.omertron.omdbapi.tools.OmdbBuilder;


public class OMDBClient implements MovieDBClient {
    final static OmdbApi omdb = new OmdbApi("89475db8");
    @Override
    public OmdbVideoFull getMovieDetails(final String imdbId) {

        OmdbVideoFull info = null;
        try {
            info = omdb.getInfo(new OmdbBuilder().setImdbId(imdbId).build());

        } catch (OMDBException e) {
            System.out.println("### movie not found");
        }

        return info;
    }
}
