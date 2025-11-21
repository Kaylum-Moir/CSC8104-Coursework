package uk.ac.newcastle.enterprisemiddleware.taxi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CustomerRestTest {

    @Test
    public void testCreateAndListCustomer() {
        // Creating a valid customer and using List to verify that it was created
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status
        given()
                .contentType(ContentType.JSON)
                .body(("{\"name\":\"Kaylum Moir\",\"email\":\"testCreateAndListCustomer@ncl.ac.uk\",\"phone\":\"07123456789\"}"))
        .when()
                .post("/customers")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Kaylum Moir"))
                .body("email", equalTo("testCreateAndListCustomer@ncl.ac.uk"))
                .body("phone", equalTo("07123456789"));


        given()
        .when()
                .get("/customers")
        .then()
                .statusCode(200)
                .body("email", hasItem("testCreateAndListCustomer@ncl.ac.uk"));
    }


    @Test
    public void testInvalidEmail() {
        // Testing customer creation with an invalid email address
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Kaylum Moir\",\"email\":\"testInvalidEmail.ac.uk\",\"phone\":\"07123456789\"}")
        .when()
                .post("/customers")
        .then()
                .statusCode(400);
    }

    @Test
    public void testCustomerCascadeDelete() {
        // Test to make sure that all bookings with a user are removed when the customer is deleted.
        // First need to make a booking with a customer, taxi and date
        Long customerId = given()
                    .contentType(ContentType.JSON)
                    .body(("{\"name\":\"Kaylum Moir\",\"email\":\"testCustomerCascadeDelete@ncl.ac.uk\",\"phone\":\"07123456789\"}"))
            .when()
                    .post("/customers")
            .then()
                    .statusCode(201)
                    .extract().body().jsonPath().getLong("id");

        Long taxiId = given()
                    .contentType(ContentType.JSON)
                    .body("{\"reg\":\"ABC5449\",\"seats\":4}")
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

        // Delete customer
        given()
        .when()
                .delete("/customers/{id}", customerId)
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


        // Delete customer again should throw 404
        given()
        .when()
                .delete("/customers/{id}", customerId)
        .then()
                .statusCode(404);
    }
}
