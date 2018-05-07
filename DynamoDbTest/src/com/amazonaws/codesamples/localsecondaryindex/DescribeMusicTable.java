package com.amazonaws.codesamples.localsecondaryindex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
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

public class DescribeMusicTable {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Music";

		Table table = dynamoDB.getTable(tableName);

		TableDescription tableDescription = table.describe();

		List<LocalSecondaryIndexDescription> localSecondaryIndexes 
		    = tableDescription.getLocalSecondaryIndexes();

		// This code snippet will work for multiple indexes, even though
		// there is only one index in this example.

		Iterator<LocalSecondaryIndexDescription> lsiIter = localSecondaryIndexes.iterator();
		while (lsiIter.hasNext()) {

		    LocalSecondaryIndexDescription lsiDescription = lsiIter.next();
		    System.out.println("Info for index " + lsiDescription.getIndexName() + ":");
		    Iterator<KeySchemaElement> kseIter = lsiDescription.getKeySchema().iterator();
		    while (kseIter.hasNext()) {
		        KeySchemaElement kse = kseIter.next();
		        System.out.printf("\t%s: %s\n", kse.getAttributeName(), kse.getKeyType());
		    }
		    Projection projection = lsiDescription.getProjection();
		    System.out.println("\tThe projection type is: " + projection.getProjectionType());
		    if (projection.getProjectionType().toString().equals("INCLUDE")) {
		        System.out.println("\t\tThe non-key projected attributes are: " + projection.getNonKeyAttributes());
		    }
		}


	}

}
