package com.amazonaws.codesamples.document;

import java.util.Arrays;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 
import com.amazonaws.services.dynamodbv2.document.DynamoDB; 
import com.amazonaws.services.dynamodbv2.document.Table; 

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition; 
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement; 
import com.amazonaws.services.dynamodbv2.model.KeyType; 
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput; 
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class ProductsCreateTable {  
  public static void main(String[] args) throws Exception { 
     AmazonDynamoDBClient client = new AmazonDynamoDBClient() 
        .withEndpoint("http://localhost:8000");  
     
     DynamoDB dynamoDB = new DynamoDB(client); 
     String tableName = "Products3";  
     try { 
        System.out.println("Creating the table, wait..."); 
        Table table = dynamoDB.createTable (tableName, 
           Arrays.asList ( 
              new KeySchemaElement("ProductId", KeyType.HASH), // the partition key 
                                                        
              new KeySchemaElement("ProductName", KeyType.RANGE)// the sort key 
           ),
           Arrays.asList ( 
              new AttributeDefinition("ProductId", ScalarAttributeType.N),
              new AttributeDefinition("ProductName", ScalarAttributeType.S)
           ),
           new ProvisionedThroughput(10L, 10L)
        );
        table.waitForActive(); 
        System.out.println("Table created successfully.  Status: " + 
           table.getDescription().getTableStatus());
           
     } catch (Exception e) {
        System.err.println("Cannot create the table: "); 
        System.err.println(e.getMessage()); 
     } 
  } 
}


