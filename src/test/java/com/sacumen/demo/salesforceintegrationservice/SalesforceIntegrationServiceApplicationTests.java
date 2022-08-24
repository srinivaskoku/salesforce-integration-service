package com.sacumen.demo.salesforceintegrationservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class SalesforceIntegrationServiceApplicationTests {

    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private static WireMockServer server = new WireMockServer(PORT);

    @Test
    public void testGetEventsAll() throws Exception {
        String testAPI = "http://localhost:"+ PORT + "/events";
        System.out.println("Service to be Hit :"+ testAPI);
        Response response = RestAssured.given().get(testAPI).then().statusCode(200).extract().response();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        //response.prettyPrint();
        //System.out.println("Status code is "+ response.statusCode());
    }

    @Test
    public void testGetEventsAll_NotFound() throws Exception {
        String testAPI = "http://localhost:"+ PORT + "/event";
        System.out.println("Service to be Hit :"+ testAPI);
        Response response = RestAssured.given().get(testAPI).then().statusCode(404).extract().response();
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
        assertNotNull(response.getBody());
        //response.prettyPrint();
        //System.out.println("Status code is "+ response.statusCode());
    }
}
