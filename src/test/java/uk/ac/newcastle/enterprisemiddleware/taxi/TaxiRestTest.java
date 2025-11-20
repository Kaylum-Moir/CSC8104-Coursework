package uk.ac.newcastle.enterprisemiddleware.taxi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TaxiRestTest {

    @Test
    public void testCreateAndListTaxi() {
        // Creating a valid Taxi and using List to verify that it was created
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status
        given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\"ABC0001\",\"seats\":4}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("reg", equalTo("ABC0001"))
                .body("seats", equalTo(4));


        given()
        .when()
                .get("/taxis")
        .then()
                .statusCode(200)
                .body("reg", hasItem("ABC0001"));
    }


    @Test
    public void testInvalidReg() {
        // Testing taxi creation with an invalid registration number
        given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\"0\",\"seats\":4}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(400);
    }


    @Test
    public void testInvalidSeats() {
        // Testing taxi creation with an invalid number of seats
        // Lower Bound
        given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\"ABC0002\",\"seats\":0}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(400);


        // Upper Bound
        given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\"ABC0002\",\"seats\":100}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(400);
    }

}
