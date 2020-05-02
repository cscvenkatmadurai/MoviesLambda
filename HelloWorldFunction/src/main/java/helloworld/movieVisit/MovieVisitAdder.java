package helloworld.movieVisit;

import com.fasterxml.jackson.databind.JsonNode;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;
import helloworld.movieDetails.MovieDetailsAdder;

public class MovieVisitAdder {
    private final DetailsDbHelper detailsDbHelper = new DetailsDbHelper();
    private final MovieDetailsAdder movieVisitAdder = new MovieDetailsAdder();


    public void addMovieVisit(final JsonNode jsonNode) {
        final String imdbId = jsonNode.get("imdbId").asText();
        try {
            movieVisitAdder.addMovieDetail(imdbId);
        }catch (final Exception e ) {
            System.out.println("error in adding movie imdbId: " + imdbId);
            e.printStackTrace();
        }

        final Details movieVisitDetails = Details.builder().
                hashkey(getHashKey(jsonNode.get("userName").asText())).
                sortKey(jsonNode.get("showTime").asLong()).
                imdbId(MovieDetailsAdder.removeImdbPrefix(imdbId)).
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
