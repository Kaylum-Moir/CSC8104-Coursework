package uk.ac.newcastle.enterprisemiddleware.taxi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BookingRestTest {
    // Create data to be used in the tests
    private Long createCustomer(String email) {
        return given()
                .contentType(ContentType.JSON)
                .body(("{\"name\":\"Kaylum Moir\",\"email\":\"" + email + "\",\"phone\":\"07123456789\"}"))
        .when()
                .post("/customers")
        .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getLong("id");
    }

    private Long createTaxi(String reg) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"reg\":\""+reg+"\",\"seats\":4}")
        .when()
                .post("/taxis")
        .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getLong("id");
    }

    @Test
    public void testCreateListDeleteBooking() {
        // Create a booking, List the customer's bookings, then delete the booking
        Long custId = createCustomer("testCreateListDeleteBooking@ncl.ac.uk");
        Long taxId = createTaxi("ABC0004");
        String date = LocalDate.now().plusDays(5).toString();

        Long bookingId =
                given()
                        .contentType(ContentType.JSON)
                        .body( "{\"customer\":{\"id\":" + custId + "},\"taxi\":{\"id\":" + taxId + "},\"date\":\"" + date + "\"}")
                .when()
                        .post("/bookings")
                .then()
                        .statusCode(201)
                        .body("customer.id", equalTo(custId.intValue()))
                        .body("taxi.id", equalTo(taxId.intValue()))
                        .extract()
                        .body()
                        .jsonPath()
                        .getLong("id");

        // List bookings under custId
        given()
                .queryParam("customer", custId)
        .when()
                .get("/bookings")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1)) // Ensure there is at least one
                .body("customer.id", everyItem(equalTo(custId.intValue())));


        //Delete booking
        given()
        .when()
                .delete("/bookings/{id}", bookingId)
        .then()
                .statusCode(204);


        // List bookings under custId
        given()
        .when()
                .get("/bookings?customerId=" + custId)
        .then()
                .statusCode(200)
                .body("id", not(hasItem(bookingId.intValue()))); // Ensure there are no entries with the booking id
    }


    @Test
    public void timeTravelBooking() {
        // Ensure bookings made for the past can not be made
        Long custId = createCustomer("timeTravelBooking@ncl.ac.uk");
        Long taxId = createTaxi("ABC0005");
        String date = LocalDate.now().minusDays(5).toString();

        given()
                .contentType(ContentType.JSON)
                .body( "{\"customer\":{\"id\":" + custId + "},\"taxi\":{\"id\":" + taxId + "},\"date\":\"" + date + "\"}")
        .when()
                .post("/bookings")
        .then()
                .statusCode(400);
    }

    @Test
    public void doubleBooking() {
        Long custId1 = createCustomer("kaylummoir@gmail.com");
        Long custId2 = createCustomer("kaylum@gmail.com");
        Long taxId = createTaxi("ABC0006");
        String date = LocalDate.now().plusDays(5).toString();

        given()
                .contentType(ContentType.JSON)
                .body( "{\"customer\":{\"id\":" + custId1 + "},\"taxi\":{\"id\":" + taxId + "},\"date\":\"" + date + "\"}")
        .when()
                .post("/bookings")
        .then()
                .statusCode(201);


        // Second booking should fail
        given()
                .contentType(ContentType.JSON)
                .body( "{\"customer\":{\"id\":" + custId2 + "},\"taxi\":{\"id\":" + taxId + "},\"date\":\"" + date + "\"}")
        .when()
                .post("/bookings")
        .then()
                .statusCode(409);
    }
}
