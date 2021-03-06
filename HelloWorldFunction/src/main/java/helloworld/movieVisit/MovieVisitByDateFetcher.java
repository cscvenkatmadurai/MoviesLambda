package helloworld.movieVisit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.omdbapi.model.OmdbVideoFull;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;
import helloworld.movieVisit.dao.MovieVisitMini;

public class MovieVisitByDateFetcher implements MovieVisitFetcher{
    private static final DetailsDbHelper DETAILS_DB_HELPER = new DetailsDbHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public List<MovieVisitMini> fetchMovieVisitByDate(final String userName, final String startTime, final String endTime) {

        System.out.println("\n\n ###userName " + userName + " startTime: " + startTime + " endTime: " + endTime );
        final List<Details> visitedMovies = DETAILS_DB_HELPER.query(MovieVisitAdder.getHashKey(userName), "sortKey", startTime, endTime);
        if(visitedMovies.isEmpty()) {
            System.out.println("### Visited movies is empty");
            return Collections.EMPTY_LIST;
        }
        System.out.println("\n\n\n### visited Movies " + visitedMovies);
        final Collection<Details> movieDetailsList = visitedMovies.stream().map(this::createMovie).
                collect(Collectors.toMap(Details::getSortKey, e -> e, (o, v) -> o)).values();
        System.out.println("\n\n\n### movieDetailsList: " + movieDetailsList);
        final Collection<Details> theatreList = visitedMovies.stream().map(this::createTheatreDetails).
                collect(Collectors.toMap(Details::getSortKey, e->e, (o, v) -> o)).values();
        System.out.println("\n\n\n### theatreList: " + theatreList);
        final List<Details> inputList = new ArrayList<>();
        inputList.addAll(movieDetailsList);
        inputList.addAll(theatreList);
        final List<Details> detailsList = DETAILS_DB_HELPER.batchLoad(inputList);

        final Map<String, Details> moviesAndTheatreDetails = detailsList.
                stream().
                collect(Collectors.toMap(e -> e.getHashkey() + "_" + e.getSortKey(), e -> e, (oldValue, newValue) -> oldValue));
        System.out.println("\n\n\ndetailsList:   " + detailsList);

        System.out.println("\n\n\n moviesAndTheatreDetails: " + moviesAndTheatreDetails);

        final List<MovieVisitMini> visitedMoviesList = visitedMovies.
                stream().
                map(visitedMovie -> createMovieVisitMini(visitedMovie, moviesAndTheatreDetails)).
                collect(Collectors.toList());

        visitedMoviesList.
                sort(Comparator.comparing(MovieVisitMini::getWatchedDateInMillisecond).
                        reversed());
        return visitedMoviesList;

    }

    public String  fetchMovieVisit(final String userName, final String startTime, final String endTime) throws JsonProcessingException {

        return objectMapper.writeValueAsString(fetchMovieVisitByDate(userName, startTime, endTime));

    }


    private Details createTheatreDetails(Details d) {
        return Details.builder().
                hashkey("theatreId").
                sortKey(d.getTheatreId()).
                theatreId(d.getTheatreId()).
                build();
    }

    private Details createMovie(Details d) {
        return Details.builder().
                hashkey("movie").
                sortKey(d.getImdbId()).
                imdbId(d.getImdbId()).
                build();
    }

    private MovieVisitMini createMovieVisitMini(final Details visitedMovie, final Map<String, Details> moviesAndTheatreDetails) {
        System.out.println("#### in createMovieVisitMini " + visitedMovie);
        final Details movieDetail = moviesAndTheatreDetails.get("movie_" + visitedMovie.getImdbId());
        final Details theatreDetail = moviesAndTheatreDetails.get("theatreId" + "_" + visitedMovie.getTheatreId());
        return createMovieVisitMini(visitedMovie, movieDetail, theatreDetail);

    }


    private MovieVisitMini createMovieVisitMini(final Details visitedDetails, final Details movieDetails, final Details theatreDetails) {
        try {
            OmdbVideoFull movieDetail = objectMapper.readValue(movieDetails.getMovieDetailsJsonAsString(),OmdbVideoFull.class);
            return MovieVisitMini.builder().
                    movieName(movieDetail.getTitle()).
                    releaseDate(movieDetail.getReleased()).
                    imageUrl(movieDetail.getPoster()).
                    watchedDateInMillisecond(visitedDetails.getSortKey()).
                    rating(visitedDetails.getMovieRating()).
                    theatreName(theatreDetails.getTheatreName()).
                    theatreLocation(theatreDetails.getTheatreLocation()).
                    imdbId(movieDetail.getImdbID()).
                    watchedLang(visitedDetails.getWatchedLang()).
                    theatreId(theatreDetails.getSortKey()).
                    build();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
