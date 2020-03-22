package helloworld.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.Attribute;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DetailsDbHelper {

    private static final DynamoDBMapper mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.
            standard().build());


    public void save(final Details details) {
        try {
            mapper.save(details);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void batchSave(final List<Details> details) {
        try {
            mapper.batchSave(details);

        }catch (Exception e) {
            System.out.println("Error in batchSave" + e);
            e.printStackTrace();
        }
    }

    public Details get(final String hashKey, final long sortKey) {
        final Details load = mapper.load(Details.class, hashKey, sortKey);
        if(load != null) {
            System.out.println("\n\n\n########" + load.toString());
        }
        return load;
    }

    public List<Details> query(final String hashKey) {
        DynamoDBQueryExpression<Details> queryExpression =
                new DynamoDBQueryExpression<Details>();
        queryExpression.withHashKeyValues(Details.builder().hashkey(hashKey).build());
        final PaginatedQueryList<Details> query = mapper.query(Details.class, queryExpression);
        query.loadAllResults();
        return new ArrayList<>(query);

    }

    public List<Details> query(final String hashKey, final String sortKey, final String start, String end) {
        DynamoDBQueryExpression<Details> queryExpression =
                new DynamoDBQueryExpression<Details>();
        queryExpression.withHashKeyValues(Details.builder().hashkey(hashKey).build());
        final Condition c = new Condition();
        c.withComparisonOperator(ComparisonOperator.BETWEEN).
                withAttributeValueList(new AttributeValue().withN(start), new AttributeValue().withN((end)));
        queryExpression.withRangeKeyCondition(sortKey, c);
        final PaginatedQueryList<Details> query = mapper.query(Details.class, queryExpression);
        query.loadAllResults();
        return new ArrayList<>(query);

    }

    public List<Details> batchLoad(final List<Details> queryList) {
        System.out.println("\n\n\n### before load " + queryList);
        final Map<String, List<Object>> stringListMap = mapper.batchLoad(queryList);
        return stringListMap.
                get(Details.TABLE_NAME).
                stream().map(e -> (Details)e).
                collect(Collectors.toList());

    }



}
