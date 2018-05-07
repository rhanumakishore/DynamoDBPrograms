package com.amazonaws.codesamples.globalsecondaryindex;

import java.util.Iterator;

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
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class QueryWeatherData {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("WeatherData");
		Index index = table.getIndex("PrecipIndex");

		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("#d = :v_date and Precipitation = :v_precip")
		    .withNameMap(new NameMap()
		        .with("#d", "Date"))
		    .withValueMap(new ValueMap()
		        .withString(":v_date","2013-08-10")
		        .withNumber(":v_precip",0));

		ItemCollection<QueryOutcome> items = index.query(spec);
		Iterator<Item> iter = items.iterator(); 
		while (iter.hasNext()) {
		    System.out.println(iter.next().toJSONPretty());
		}

	}

}
