package com.amazonaws.codesamples.globalsecondaryindex;


import java.util.ArrayList;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class DescribeWeatherData {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("WeatherData");
		TableDescription tableDesc = table.describe();
		    

		Iterator<GlobalSecondaryIndexDescription> gsiIter = tableDesc.getGlobalSecondaryIndexes().iterator();
		while (gsiIter.hasNext()) {
		    GlobalSecondaryIndexDescription gsiDesc = gsiIter.next();
		    System.out.println("Info for index "
		         + gsiDesc.getIndexName() + ":");

		    Iterator<KeySchemaElement> kseIter = gsiDesc.getKeySchema().iterator();
		    while (kseIter.hasNext()) {
		        KeySchemaElement kse = kseIter.next();
		        System.out.printf("\t%s: %s\n", kse.getAttributeName(), kse.getKeyType());
		    }
		    Projection projection = gsiDesc.getProjection();
		    System.out.println("\tThe projection type is: "
		        + projection.getProjectionType());
		    if (projection.getProjectionType().toString().equals("INCLUDE")) {
		        System.out.println("\t\tThe non-key projected attributes are: "
		            + projection.getNonKeyAttributes());
		    }
		}

	}

}
