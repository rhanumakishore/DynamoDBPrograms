package com.amazonaws.codesamples.localsecondaryindex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class QueryMusicTable {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Music";

		Table table = dynamoDB.getTable(tableName);
		Index index = table.getIndex("AlbumTitleIndex");

		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("Artist = :v_artist and AlbumTitle = :v_title")
		    .withValueMap(new ValueMap()
		        .withString(":v_artist", "Acme Band")
		        .withString(":v_title", "Songs About Life"));

		ItemCollection<QueryOutcome> items = index.query(spec);

		Iterator<Item> itemsIter = items.iterator();

		while (itemsIter.hasNext()) {
		    Item item = itemsIter.next();
		    System.out.println(item.toJSONPretty());
		}


	}

}
