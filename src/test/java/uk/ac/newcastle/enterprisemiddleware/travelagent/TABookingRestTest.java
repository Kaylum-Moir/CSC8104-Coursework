package uk.ac.newcastle.enterprisemiddleware.travelagent;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.CompletableFuture.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@QuarkusTest
public class TABookingRestTest {

    @ConfigProperty(name = "hotel-api/mp-rest/url")
    String hotelURL;

    @ConfigProperty(name = "flight-api/mp-rest/url")
    String flightURL;

    Long agentHotelId;
    Long agentFlightId;
    Long agentTaxiId = 1L; // Defined in import.sql


    Long taxiId;
    Long hotelId;
    Long flightId;

    @BeforeEach
    void setup() {
        // Create travel agent customers on colleagues' API
        agentHotelId =
                given()
                        .baseUri(hotelURL)
                        .contentType("application/json")
                        .body("{\"name\":\"TAgent\",\"email\":\"agent@Travel.com\",\"phoneNumber\":\"07123456789\"}")
                .when()
                        .post("/customers")
                .then()
                        .extract()
                        .path("id");
        agentFlightId =
                given()
                        .baseUri(flightURL)
                        .contentType("application/json")
                        .body("{\"name\":\"TAgent\",\"email\":\"agent@Travel.com\",\"phoneNumber\":\"07123456789\"}")
                .when()
                        .post("/customers")
                .then()
                        .extract()
                        .path("id");


        // Create hotels, flights and taxis

        hotelId =
                given()
                        .baseUri(hotelURL)
                        .contentType("application/json")
                        .body("{\"name\":\"Hotel\",\"postcode\":\"NE10AB\",\"phoneNumber\":\"07123456789\"}")
                .when()
                        .post("/hotels")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        flightId =
                given()
                        .baseUri(flightURL)
                        .contentType("application/json")
                        .body("{\"code\":\"AB123\",\"depart\":\"Newcastle\",\"destination\":\"London\",\"departureDate\":\"2025-12-01\",\"seats\":100}")
                .when()
                        .post("/flights")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");


        taxiId =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"reg\":\"ABC9999\",\"seats\":4}")
                .when()
                        .post("/taxis")
                .then()
                        .statusCode(201)
                        .extract().path("id");

    }

    @Test
    public void CreateBookingTest() {
        Map<String, Object> req = new HashMap<>();
        req.put("customerId", agentTaxiId);
        req.put("taxiId", taxiId);
        req.put("hotelId", hotelId);
        req.put("flightId", flightId);
        req.put("date", "2025-12-01");
        req.put("seats", 1);

        given()
                .contentType(ContentType.JSON)
                .body(req)
        .when()
                .post("/travelagent/bookings")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("hotelId", notNullValue())
                .body("flightId", notNullValue());

    }
}
