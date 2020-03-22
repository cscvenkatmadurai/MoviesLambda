package helloworld.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static helloworld.db.Details.TABLE_NAME;

@DynamoDBTable(tableName="skven_movies1")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Details {

    public static final String TABLE_NAME = "skven_movies1";

    @DynamoDBHashKey(attributeName = "hashKey")
    private String hashkey;

    @DynamoDBRangeKey(attributeName = "sortKey")
    private long sortKey;

    @DynamoDBAttribute(attributeName = "imdbId")
    private long imdbId;

    @DynamoDBAttribute(attributeName = "theatreId")
    private long theatreId;

    @DynamoDBAttribute(attributeName = "relDate")
    private long relDate;

    @DynamoDBAttribute(attributeName = "watchedLang")
    private String watchedLang;

    @DynamoDBAttribute(attributeName = "theatreName")
    private String theatreName;

    @DynamoDBAttribute(attributeName = "theatreLocation")
    private String theatreLocation;

    @DynamoDBAttribute(attributeName = "movieDetailsJsonAsString")
    private String movieDetailsJsonAsString;

    @DynamoDBAttribute(attributeName = "theatreCounter")
    private long theatreCounter;

    @DynamoDBAttribute(attributeName = "movieRating")
    private Double movieRating;

    @DynamoDBVersionAttribute
    private Long version;

}
