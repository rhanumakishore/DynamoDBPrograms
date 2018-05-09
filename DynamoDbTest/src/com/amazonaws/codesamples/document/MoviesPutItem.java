// Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// Licensed under the Apache License, Version 2.0.

package com.amazonaws.codesamples.document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
//Insert data to table
public class MoviesPutItem {

    public static void main(String[] args) throws Exception {

    	
    	AWSCredentialsProviderChain providers = new AWSCredentialsProviderChain(
				new ProfileCredentialsProvider("sandbox"), new EC2ContainerCredentialsProviderWrapper(),
				new InstanceProfileCredentialsProvider(false));

     
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(providers)
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://dynamodb.us-west-2.amazonaws.com/", "us-west-2")).build();
    	
        /*AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();*/

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        int year = 2015;
        String title = "The Big New Movie";

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);

        try {
            System.out.println("Adding a new map item...");
            PutItemOutcome mapOutcome = table
                .putItem(new Item().withPrimaryKey("year", year, "title", title).withMap("info", infoMap));

            System.out.println("PutItem succeeded:\n" + mapOutcome.getPutItemResult());
            
            System.out.println("Adding a new list item...");
            List<?> attrList = Arrays.asList("test1",2,3,4.0,"test2");
			PutItemOutcome listOutcome = table
                .putItem(new Item().withPrimaryKey("year", 2018, "title", "Adding list to item").withList("list", attrList ));

            System.out.println("PutItem succeeded:\n" + listOutcome.getPutItemResult());
            
            System.out.println("Adding a new set item...");
            Set<String> val = new HashSet<String>(Arrays.asList("setval1","setval2","setval3"));
			PutItemOutcome setOutcome = table
                    .putItem(new Item().withPrimaryKey("year", 2018, "title", "Adding set to item").withStringSet("stringset", val));
			System.out.println("PutItem succeeded:\n" + setOutcome.getPutItemResult());
			
        }
        catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }

    }
}
