package helloworld.movieDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.omdbapi.model.OmdbVideoFull;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;
import helloworld.movieDetails.db.MovieDBClient;
import helloworld.movieDetails.db.OMDBClient;

public class MovieDetailsAdder {
    private static final MovieDBClient MOVIE_DB_CLIENT = new OMDBClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String IMDB_PREFIX  = "tt";
    private static final int IMDB_PREFIX_LENGTH  = IMDB_PREFIX.length();
    private static final DetailsDbHelper DYNAMO_DB_HELPER = new DetailsDbHelper();

    public String addMovieDetails(final JsonNode jsonNode) {
        final String imdbId = jsonNode.get("imdbId").asText();
        final OmdbVideoFull movieDetails = MOVIE_DB_CLIENT.getMovieDetails(imdbId);
        String moviesDetailsJsonAsString = "";
        try {
            moviesDetailsJsonAsString = OBJECT_MAPPER.writeValueAsString(movieDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final Details details = Details.builder().
                hashkey("movie").
                sortKey(removeImdbPrefix(imdbId)).
                movieDetailsJsonAsString(moviesDetailsJsonAsString).
                build();
        final Details details1 = DYNAMO_DB_HELPER.get("movie", removeImdbPrefix(imdbId));
        if(details1 != null) {
            details.setVersion(details1.getVersion());
        }
        DYNAMO_DB_HELPER.save(details);

        return moviesDetailsJsonAsString;

    }

    public static long removeImdbPrefix(final String imdbId) {
        return Long.parseLong(imdbId.substring(IMDB_PREFIX_LENGTH));
    }
 }
