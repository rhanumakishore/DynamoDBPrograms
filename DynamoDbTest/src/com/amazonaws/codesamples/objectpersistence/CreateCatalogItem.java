package com.amazonaws.codesamples.objectpersistence;

import java.util.Arrays;
import java.util.HashSet;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//Insert data to table using DynamoDBMapper
public class CreateCatalogItem {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);

		CatalogItem item = new CatalogItem();
		item.setId(102);
		item.setTitle("Book 102 Title");
		item.setISBN("222-2222222222");
		item.setBookAuthors(new HashSet<String>(Arrays.asList("Author 1", "Author 2")));
		item.setSomeProp("Test");

		mapper.save(item);          

	}

}
