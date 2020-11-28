package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class BestBuyApiTests {

	String currentWorkingDirectory;

	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "http://localhost";

		RestAssured.port = 3030;
	}

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

		given().baseUri("http://localhost:3030").when().get("/products").then().statusCode(200).time(Matchers.lessThan(10l));

	}

	@Test
	public void verifyGetProductApiAndPrintJsonResponse() {

		Response response = given().baseUri("http://localhost:3030").when().get("/products");

		String responseAsString = response.asString();

		Map<String, Object> responseAsMap = JsonPath.read(responseAsString, "$");

		System.out.println(responseAsMap.get("total"));

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseAsMap.get("data");

		System.out.println(dataList.get(0).get("id"));

	}

	@Test
	public void verifyGetProductApiAndGetDataPrintJsonResponse() {

		Response response = given().baseUri("http://localhost:3030").when().get("/products");

		String responseAsString = response.asString();

		List<Map<String, Object>> dataList = JsonPath.read(responseAsString, "$.data");

		System.out.println(dataList.get(0).get("id"));

	}

	@Test
	public void verifyGetProductApiWithParameters() {

		given().baseUri("http://localhost:3030").when().queryParam("$limit", 5).get("/products").then().statusCode(200)
				.body("limit", equalTo(5));

	}

	@Test
	public void verifyAddProductWithPostRequest() {
		String requestPayload = "{\r\n" + "  \"name\": \"Samsung Mobile\",\r\n" + "  \"type\": \"Mobile\",\r\n"
				+ "  \"price\": 500,\r\n" + "  \"shipping\": 10,\r\n" + "  \"upc\": \"Mobile\",\r\n"
				+ "  \"description\": \"Best Mobile in the town\",\r\n" + "  \"manufacturer\": \"Samsung\",\r\n"
				+ "  \"model\": \"M31\",\r\n" + "  \"url\": \"string\",\r\n" + "  \"image\": \"string\"\r\n" + "}";

		given().contentType(ContentType.JSON).body(requestPayload).when().post("/products").then().log().all()
				.statusCode(201);
	}

	@Test
	public void verifyAddProductWithPostRequestFromJsonFile() {

		currentWorkingDirectory = System.getProperty("user.dir");

		File requestPayload = new File(currentWorkingDirectory + "/testData/product.json");

		given().contentType(ContentType.JSON).body(requestPayload).when().post("/products").then().log().all()
				.statusCode(201);
	}

	@Test
	public void verifyUpdateProductWithPutRequest() {

		currentWorkingDirectory = System.getProperty("user.dir");

		File requestPayloadFile = new File(currentWorkingDirectory + "/testData/product.json");

		int productId = given().contentType(ContentType.JSON).body(requestPayloadFile).when().post("/products").then()
				.extract().path("id");

		Map<String, Object> requestPayload = new HashMap<String, Object>();

		requestPayload.put("name", "Samsung Mobile 2");
		requestPayload.put("type", "Mobile");
		requestPayload.put("price", 500);
		requestPayload.put("shipping", 10);
		requestPayload.put("upc", "Samsung Mobile");
		requestPayload.put("description", "Best Samsung Mobile");
		requestPayload.put("manufacturer", "Samsung Mobile");
		requestPayload.put("model", "Samsung Mobile M51");
		requestPayload.put("url", "Samsung Mobile");
		requestPayload.put("image", "Samsung Mobile");

		given().contentType(ContentType.JSON).body(requestPayload).when().put("/products/" + productId).then().log()
				.all().statusCode(200);

	}

	@Test
	public void verifyUpdateProductWithPatchRequest() {

		currentWorkingDirectory = System.getProperty("user.dir");

		File requestPayloadFile = new File(currentWorkingDirectory + "/testData/product.json");

		int productId = given().contentType(ContentType.JSON).body(requestPayloadFile).when().post("/products").then()
				.extract().path("id");

		Map<String, Object> requestPayload = new HashMap<String, Object>();

		requestPayload.put("name", "Samsung Mobile 2");

		requestPayload.put("model", "Samsung Mobile M61");

		given().contentType(ContentType.JSON).body(requestPayload).when().patch("/products/" + productId).then().log()
				.all().statusCode(200);

	}

	@Test
	public void verifyDeleteProductWithDeleteRequest() {

		currentWorkingDirectory = System.getProperty("user.dir");

		File requestPayloadFile = new File(currentWorkingDirectory + "/testData/product.json");

		int productId = given().contentType(ContentType.JSON).body(requestPayloadFile).when().post("/products").then()
				.extract().path("id");

		given().when().delete("/products/" + productId).then().log().all().statusCode(200);

		given().when().get("/products/" + productId).then().log().all().statusCode(404);

	}

	@Test
	public void verifyAddProductWithPostRequestWithMapPayload() {

		Map<String, Object> requestPayload = new HashMap<String, Object>();

		requestPayload.put("name", "Samsung Mobile");
		requestPayload.put("type", "Mobile");
		requestPayload.put("price", 500);
		requestPayload.put("shipping", 10);
		requestPayload.put("upc", "Samsung Mobile");
		requestPayload.put("description", "Best Samsung Mobile");
		requestPayload.put("manufacturer", "Samsung Mobile");
		requestPayload.put("model", "Samsung Mobile M21");
		requestPayload.put("url", "Samsung Mobile");
		requestPayload.put("image", "Samsung Mobile");

		given().contentType(ContentType.JSON).body(requestPayload).when().post("/products").then().log().all()
				.statusCode(201);
	}

	@Test
	public void verifyAddProductWithPostRequestWithPojo() {

		ProductPojo requestPayload = new ProductPojo();

		requestPayload.setName("Samsung Mobile");
		requestPayload.setType("Mobile");
		requestPayload.setPrice(500);
		requestPayload.setShipping(10);
		requestPayload.setUpc("Mobile");
		requestPayload.setDescription("Samsung Mobile ");
		requestPayload.setManufacturer("Samsung");
		requestPayload.setModel("M21");
		requestPayload.setUrl("rwerg");
		requestPayload.setImage("shajkdas");

		given().contentType(ContentType.JSON).body(requestPayload).when().post("/products").then().log().all()
				.statusCode(201);
	}

	@Test
	public void sampleExmpleOfComplexJSon() {

		Map<String, Object> requestPayload = new HashMap<String, Object>();

		requestPayload.put("firstName", "Saurabh");
		requestPayload.put("lastName", "Dhingra");

		List<Long> phoneNumbers = new ArrayList<Long>();

		phoneNumbers.add(3478937895l);
		phoneNumbers.add(534534534l);
		phoneNumbers.add(43525345l);

		requestPayload.put("phoneNumber", phoneNumbers);

		Map<String, Object> address1 = new HashMap<String, Object>();
		Map<String, Object> address2 = new HashMap<String, Object>();

		address1.put("type", "home");
		address1.put("HouseNumber", 317);
		address1.put("City", "Gurgaon");

		address2.put("type", "Office");
		address2.put("HouseNumber", 317);
		address2.put("City", "Mumbai");

		List<Map<String, Object>> addressList = new ArrayList<>();

		addressList.add(address1);
		addressList.add(address2);

		requestPayload.put("address", addressList);

	}

}
