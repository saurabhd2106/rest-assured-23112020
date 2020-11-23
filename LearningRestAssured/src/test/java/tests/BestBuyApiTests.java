package tests;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class BestBuyApiTests {

	@Test
	public void verifyGetProductApi() {

		// Create a request specification
		RequestSpecification request = RestAssured.given();

		// Adding a URI
		request.baseUri("http://localhost:3030/products");

		// Calling a GET method, request sent to base URI
		Response response = request.get();

		// Prints the response in pretty json format
		response.prettyPrint();

		String responseAsString = response.asString();

		System.out.println(responseAsString);

		ValidatableResponse validateResponse = response.then();

		validateResponse.statusCode(200);

	}

	@Test
	public void verifyGetProductApiWithBDDStyle() {

		given().baseUri("http://localhost:3030").when().get("/products").then().statusCode(200);

	}
	
	@Test
	public void verifyGetProductApiWithParameters() {

		given()
		.baseUri("http://localhost:3030")
		.when()
		.queryParam("$limit", 5)
		.get("/products")
		.then()
		.statusCode(200)
		.body("limit", equalTo(5));

	}

}
