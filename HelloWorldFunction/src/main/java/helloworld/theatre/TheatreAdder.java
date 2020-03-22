package helloworld.theatre;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.JsonNode;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;

import java.util.Arrays;
import java.util.List;

public class TheatreAdder {
    static DetailsDbHelper detailsDbHelper = new DetailsDbHelper();

    public void addTheatre(final JsonNode jsonNode) {
        final Details theatreCounter = detailsDbHelper.get("theatreCounter", 0);
        final long nextTheatreId = theatreCounter.getTheatreCounter() + 1;
        theatreCounter.setTheatreCounter(nextTheatreId);

        final Details theatreDetails = Details.
                builder().
                hashkey("theatreId").
                sortKey(nextTheatreId).
                theatreName(jsonNode.get(TheatreConstants.THEATRE_NAME).asText()).
                theatreLocation(jsonNode.get(TheatreConstants.THEATRE_LOCATION).asText()).
                build();
        final List<Details> detailsList = Arrays.asList(theatreCounter, theatreDetails);
        detailsDbHelper.batchSave(detailsList);


    }
}
