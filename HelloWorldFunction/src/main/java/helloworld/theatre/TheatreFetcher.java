package helloworld.theatre;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import helloworld.db.Details;
import helloworld.db.DetailsDbHelper;

import java.util.List;

public class TheatreFetcher {
    private static final DetailsDbHelper DETAILS_DB_HELPER = new DetailsDbHelper();
    public String getAllTheatres() {
        final List<Details> theatreDetailsList = DETAILS_DB_HELPER.query(TheatreConstants.THEATRE_ID_HASH_KEY);
        System.out.println("### query theatreDetailsList " + theatreDetailsList);
        final String s = TheatreTranslator.translateToJsonString(theatreDetailsList);
        System.out.println("\n\n\n\n" + s );


        return s;

    }



}
