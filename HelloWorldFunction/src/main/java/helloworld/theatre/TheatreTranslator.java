package helloworld.theatre;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.db.Details;

public class TheatreTranslator {
    final static ObjectMapper objectMapper = new ObjectMapper();

    public static List<Theatre> translate(List<Details> theatreDetailsList) {
        return theatreDetailsList.
                stream().
                filter(Objects::nonNull).
                map(TheatreTranslator::translate).collect(Collectors.toList());


    }

    public static String translateToJsonString(List<Details> theatreDetailsList) {

        final List<Theatre> theatreList = translate(theatreDetailsList);
        try {
            return objectMapper.writeValueAsString(theatreList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;


    }

    public static Theatre translate(final Details theatreDetails) {
        return Theatre.builder().
                theatreId(theatreDetails.getSortKey()).
                theatreLocation(theatreDetails.getTheatreLocation()).
                theatreName(theatreDetails.getTheatreName()).
                build();
    }



 }
