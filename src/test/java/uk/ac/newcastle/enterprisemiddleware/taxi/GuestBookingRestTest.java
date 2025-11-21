package uk.ac.newcastle.enterprisemiddleware.taxi;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class GuestBookingRestTest {
    @Test
    public void GuestBookingCreation() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"customer\": {\"name\": \"Guest Test\",\"email\": \"GuestBookingCreation@gmail.com\",\"phone\": \"07123456789\"},\"booking\": {\"taxi\": {\"id\": 1},\"date\": \"2025-12-12\"}}")
        .when()
                .post("/guestbookings")
        .then()
                .statusCode(201);
    }


    // Create erroneous Guest Booking (Missing Taxi)
    @Test
    public void GuestBookingRollback() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"customer\": {\"name\": \"Guest Test\",\"email\": \"GuestBookingRollback@gmail.com\",\"phone\": \"07123456789\"},\"booking\": {\"taxi\": {\"id\": 999},\"date\": \"2025-12-12\"}}")
                .when()
                .post("/guestbookings")
                .then()
                .statusCode(500);


        // Make sure user was rolled back
        given()
        .when()
                .get("/customers")
        .then()
                .statusCode(200)
                .body("email", not(hasItem("GuestBookingRollback@gmail.com")));

    }



}
