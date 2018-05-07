package com.amazonaws.codesamples.objectpersistence;

import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//Retrieve data from table using DynamoDBMapper
public class GetCatalogItem {

	public static void main(String[] args) {
		CatalogItem partitionKey = new CatalogItem();

		partitionKey.setId(102);
		DynamoDBQueryExpression<CatalogItem> queryExpression = new DynamoDBQueryExpression<CatalogItem>()
		    .withHashKeyValues(partitionKey);
		
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		List<CatalogItem> itemList = mapper.query(CatalogItem.class, queryExpression);

		for (int i = 0; i < itemList.size(); i++) {
		    System.out.println(itemList.get(i).getTitle());
		    System.out.println(itemList.get(i).getBookAuthors());
		}

	}

}
