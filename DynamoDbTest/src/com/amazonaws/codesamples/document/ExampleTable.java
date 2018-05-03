package com.amazonaws.codesamples.document;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;

public class ExampleTable {

    private static final String EXAMPLE_TABLE_NAME = "example_table";

    public static void main(String[] args) {
        AmazonDynamoDB client = new AmazonDynamoDBClient(new BasicAWSCredentials("accessKey", "secretKey"));
        client.setEndpoint("http://localhost:8000");
        DynamoDB dynamoDB = new DynamoDB(client);
        DeleteTableRequest deleteTableRequest = new DeleteTableRequest(EXAMPLE_TABLE_NAME);
		TableUtils.deleteTableIfExists(client, deleteTableRequest );
     

        // Create table with hash key 'customer_id'
        CreateTableRequest createTableRequest = new CreateTableRequest();
        createTableRequest.withTableName(EXAMPLE_TABLE_NAME);
        createTableRequest.withKeySchema(new KeySchemaElement("customer_id", KeyType.HASH));
        createTableRequest.withAttributeDefinitions(new AttributeDefinition("customer_id", ScalarAttributeType.S));
        createTableRequest.withProvisionedThroughput(new ProvisionedThroughput(15l, 15l));
        dynamoDB.createTable(createTableRequest);
        try {
			TableUtils.waitUntilActive(client, EXAMPLE_TABLE_NAME);
		} catch (TableNeverTransitionedToStateException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        Table exampleTable = dynamoDB.getTable(EXAMPLE_TABLE_NAME);

        exampleTable.putItem(new Item()
                .withPrimaryKey("customer_id", "ABCD")
                .withString("customer_name", "Jim")
                .withString("customer_email", "jim@gmail.com"));

        System.out.println("After Jim:");
        exampleTable.scan()
                    .forEach(System.out::println);
        System.out.println();

        try {
            exampleTable.putItem(new Item()
                    .withPrimaryKey("customer_id", "EFGH")
                    .withString("customer_name", "Garret")
                    .withString("customer_email", "garret@gmail.com"), new Expected("customer_id").notExist());
        } catch (ConditionalCheckFailedException e) {
            System.out.println("Conditional check failed!");
        }

        System.out.println("After Garret:");
        exampleTable.scan()
                    .forEach(System.out::println);
        System.out.println();

        try {
            exampleTable.putItem(new Item()
                    .withPrimaryKey("customer_id", "ABCD")
                    .withString("customer_name", "Bob")
                    .withString("customer_email", "bob@gmail.com"), new Expected("customer_id").notExist());
        } catch (ConditionalCheckFailedException e) {
            System.out.println("Conditional check failed!");
        }

        System.out.println("After Bob:");
        exampleTable.scan()
                    .forEach(System.out::println);
    }
}