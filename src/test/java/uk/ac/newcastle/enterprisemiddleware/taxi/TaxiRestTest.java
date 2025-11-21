package uk.ac.newcastle.enterprisemiddleware.taxi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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



    @Test
    public void testTaxiCascadeDelete() {
        // Test to make sure that all bookings with a user are removed when the Taxi is deleted.
        // First need to make a booking with a Taxi, taxi and date
        Long customerId = given()
                .contentType(ContentType.JSON)
                .body(("{\"name\":\"Kaylum Moir\",\"email\":\"testTaxiCascadeDelete@ncl.ac.uk\",\"phone\":\"07123456789\"}"))
        .when()
                .post("/customers")
        .then()
                .statusCode(201)
                .extract().body().jsonPath().getLong("id");

        Long taxiId = given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\"ABC5344\",\"seats\":4}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(201)
                .extract().body().jsonPath().getLong("id");

        String date = LocalDate.now().plusDays(5).toString();

        given()
                .contentType(ContentType.JSON)
                .body( "{\"customer\":{\"id\":" + customerId + "},\"taxi\":{\"id\":" + taxiId + "},\"date\":\"" + date + "\"}")
        .when()
                .post("/bookings")
        .then()
                .statusCode(201)
                .extract().body().jsonPath().getLong("id");


        // Make sure the booking exists
        given()
                .queryParam("customer", customerId)
        .when()
                .get("/bookings")
        .then()
                .statusCode(200)
                .body("size()", is(1));

        // Delete Taxi
        given()
        .when()
                .delete("/taxis/{id}", taxiId)
        .then()
                .statusCode(204);


        // Make sure the booking is deleted
        given()
                .queryParam("customer", customerId)
        .when()
                .get("/bookings")
        .then()
                .statusCode(200)
                .body("size()", is(0));


        // Delete Taxi again should throw 404
        given()
                .when()
                .delete("/taxis/{id}", taxiId)
                .then()
                .statusCode(404);
    }

}
