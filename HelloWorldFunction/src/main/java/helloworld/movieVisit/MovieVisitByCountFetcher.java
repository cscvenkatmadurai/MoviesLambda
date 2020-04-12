package helloworld.movieVisit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.movieVisit.dao.MovieVisitById;
import helloworld.movieVisit.dao.MovieVisitFetcherResponse;
import helloworld.movieVisit.dao.MovieVisitMini;

import java.util.HashMap;
import java.util.List;
import java.util.*;

public class MovieVisitByCountFetcher implements MovieVisitFetcher {

    final MovieVisitByDateFetcher movieVisitByDateFetcher = new MovieVisitByDateFetcher();
    final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String fetchMovieVisit(String username, String startDate, String endDate) throws JsonProcessingException {
        final List<MovieVisitMini> movieVisitMiniList = movieVisitByDateFetcher.fetchMovieVisitByDate(username, startDate, endDate);
        movieVisitMiniList.sort(Comparator.comparing(MovieVisitMini::getImdbId));
        Map<String, TreeSet<MovieVisitMini>> imdbIdToMovieVisit = new HashMap<>();
        final TreeMap<Integer, TreeSet<MovieVisitMini>> countToMovieVisit = new TreeMap<>();


        for (int i = 0; i < movieVisitMiniList.size(); ) {
            final String imdbId = movieVisitMiniList.get(i).getImdbId();
            final TreeSet<MovieVisitMini> treeSet = new TreeSet<>(Comparator.comparing(MovieVisitMini::getWatchedDateInMillisecond).reversed());
            while (i < movieVisitMiniList.size() && imdbId.equals(movieVisitMiniList.get(i).getImdbId())) {
                treeSet.add(movieVisitMiniList.get(i));
                i++;
            }
            imdbIdToMovieVisit.put(imdbId, treeSet);
            if(countToMovieVisit.containsKey(treeSet.size())) {

                countToMovieVisit.get(treeSet.size()).addAll(treeSet);
            } else {
                final TreeSet<MovieVisitMini> treeSet1 = new TreeSet<>(Comparator.comparing(MovieVisitMini::getWatchedDateInMillisecond).reversed());
                treeSet1.addAll(treeSet);
                countToMovieVisit.put(treeSet.size(), treeSet1);
            }

        }


        TreeMap<Integer, List<MovieVisitMini>> resultMap = new TreeMap<>();
        countToMovieVisit.descendingMap().forEach((count, movieVisitsSet) -> {

        movieVisitsSet.forEach(movieVisitMini -> {
            if(imdbIdToMovieVisit.containsKey(movieVisitMini.getImdbId())) {
                final TreeSet<MovieVisitMini> movieVisitMinis1 = imdbIdToMovieVisit.get(movieVisitMini.getImdbId());
                if(resultMap.containsKey(count)) {
                    resultMap.get(count).addAll(movieVisitMinis1);
                } else {
                    final List<MovieVisitMini> movieVisitMinis = new ArrayList<>(movieVisitMinis1);
                    resultMap.put(count, movieVisitMinis);

                }
                imdbIdToMovieVisit.remove(movieVisitMini.getImdbId());

            }
        });
        });




        return objectMapper.writeValueAsString(populateResponse(movieVisitMiniList, resultMap));
    }

    private MovieVisitFetcherResponse populateResponse(List<MovieVisitMini> movieVisitMinis, TreeMap<Integer, List<MovieVisitMini>> countToMovieVisitMap) {
        final List<MovieVisitById> movieVisitByCount = new ArrayList<>();
        countToMovieVisitMap.
                descendingMap().
                forEach( (count, movieVisits) -> {
                    final MovieVisitById movieVisitById = new MovieVisitById();
                    movieVisitById.setId(Integer.toString(count));
                    movieVisitById.setNumberOfMovieOfMoviesWatched(movieVisits.size());
                    movieVisitById.setNumberOfDistinctMoviesWatched(MovieVisitHelper.getNumberOfDistinctMovies(movieVisitMinis));
                    movieVisitById.setMovieList(movieVisits);
                    movieVisitByCount.add(movieVisitById);
                });
        return MovieVisitFetcherResponse.builder()
                .movieVisitByIdList(movieVisitByCount).
                numberOfMovieOfMoviesWatched(movieVisitMinis.size()).
                numberOfMovieOfMoviesWatched(MovieVisitHelper.getNumberOfDistinctMovies(movieVisitMinis)).
                build();
    }
}
