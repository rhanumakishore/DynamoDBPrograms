package com.amazonaws.codesamples.globalsecondaryindex;

import java.util.ArrayList;

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
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class CreateWeatherData {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);


		// Attribute definitions
		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

		attributeDefinitions.add(new AttributeDefinition()
		    .withAttributeName("Location")
		    .withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition()
		    .withAttributeName("Date")
		    .withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition()
		    .withAttributeName("Precipitation")
		    .withAttributeType("N"));

		// Table key schema
		ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
		tableKeySchema.add(new KeySchemaElement()
		    .withAttributeName("Location")
		    .withKeyType(KeyType.HASH));  //Partition key
		tableKeySchema.add(new KeySchemaElement()
		    .withAttributeName("Date")
		    .withKeyType(KeyType.RANGE));  //Sort key

		// PrecipIndex
		GlobalSecondaryIndex precipIndex = new GlobalSecondaryIndex()
		    .withIndexName("PrecipIndex")
		    .withProvisionedThroughput(new ProvisionedThroughput()
		        .withReadCapacityUnits((long) 10)
		        .withWriteCapacityUnits((long) 1))
		        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));

		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();

		indexKeySchema.add(new KeySchemaElement()
		    .withAttributeName("Date")
		    .withKeyType(KeyType.HASH));  //Partition key
		indexKeySchema.add(new KeySchemaElement()
		    .withAttributeName("Precipitation")
		    .withKeyType(KeyType.RANGE));  //Sort key

		precipIndex.setKeySchema(indexKeySchema);

		CreateTableRequest createTableRequest = new CreateTableRequest()
		    .withTableName("WeatherData")
		    .withProvisionedThroughput(new ProvisionedThroughput()
		        .withReadCapacityUnits((long) 5)
		        .withWriteCapacityUnits((long) 1))
		    .withAttributeDefinitions(attributeDefinitions)
		    .withKeySchema(tableKeySchema)
		    .withGlobalSecondaryIndexes(precipIndex);

		Table table = dynamoDB.createTable(createTableRequest);
		System.out.println(table.getDescription());

	}

}
