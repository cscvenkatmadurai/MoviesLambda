package helloworld.movieVisit;

import com.fasterxml.jackson.databind.JsonNode;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;
import helloworld.movieDetails.MovieDetailsAdder;

public class MovieVisitAdder {
    private final DetailsDbHelper detailsDbHelper = new DetailsDbHelper();


    public void addMovieVisit(final JsonNode jsonNode) {
        final Details movieVisitDetails = Details.builder().
                hashkey(getHashKey(jsonNode.get("userName").asText())).
                sortKey(jsonNode.get("showTime").asLong()).
                imdbId(MovieDetailsAdder.removeImdbPrefix(jsonNode.get("imdbId").asText())).
                theatreId(jsonNode.get("theatreId").asLong()).
                movieRating(jsonNode.get("rating").asDouble()).
                watchedLang(jsonNode.get("langWatched").asText()).
                build();
        detailsDbHelper.save(movieVisitDetails);
    }

    public static String getHashKey(final String userName) {
        return userName+"-visit";
    }


}
