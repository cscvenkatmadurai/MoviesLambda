package helloworld.movieVisit.dao;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieVisitMini {

    private String movieName;
    private String releaseDate;
    private String imageUrl;
    private long watchedDateInMillisecond;
    private Double rating;
    private String watchedLang;
    private String theatreName;
    private String theatreLocation;
    private String imdbId;
    private Long theatreId;


}
