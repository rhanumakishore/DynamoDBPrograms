package com.amazonaws.codesamples.localsecondaryindex;

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
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class CreateMusicTable {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Music";

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

		//ProvisionedThroughput
		createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)5).withWriteCapacityUnits((long)5));

		//AttributeDefinitions
		ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Artist").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("SongTitle").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("AlbumTitle").withAttributeType("S"));

		createTableRequest.setAttributeDefinitions(attributeDefinitions);

		//KeySchema
		ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
		tableKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH));  //Partition key
		tableKeySchema.add(new KeySchemaElement().withAttributeName("SongTitle").withKeyType(KeyType.RANGE));  //Sort key

		createTableRequest.setKeySchema(tableKeySchema);

		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH));  //Partition key
		indexKeySchema.add(new KeySchemaElement().withAttributeName("AlbumTitle").withKeyType(KeyType.RANGE));  //Sort key

		Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
		ArrayList<String> nonKeyAttributes = new ArrayList<String>();
		nonKeyAttributes.add("Genre");
		nonKeyAttributes.add("Year");
		projection.setNonKeyAttributes(nonKeyAttributes);

		LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
		    .withIndexName("AlbumTitleIndex").withKeySchema(indexKeySchema).withProjection(projection);

		ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
		localSecondaryIndexes.add(localSecondaryIndex);
		createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);

		Table table = dynamoDB.createTable(createTableRequest);
		System.out.println(table.getDescription());


	}

}
