package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

import java.io.File;

public class UploadFile {
	
	String currentWorkingDirectory;

	@BeforeClass
	public void setup() {
		
		currentWorkingDirectory = System.getProperty("user.dir");
		
		RestAssured.baseURI = "https://sandbox.zamzar.com/v1";

	}
	
	@Test
	public void verifyFileUpload() {
		
		String apiKey = "225e727e056d0c61e2bf8cb6b99201ebe65a5a32";
		
		File imageFileToUpload = new File(currentWorkingDirectory+ "/testData/APINotes.png");
		
		given()
		.auth()
		.basic(apiKey, "")
		.multiPart("source_file",imageFileToUpload)
		.multiPart("target_format","jpg")
		.when()
		.post("/jobs")
		.then()
		.statusCode(201);
		
		
	}

}
