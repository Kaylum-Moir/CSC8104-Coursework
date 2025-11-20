package uk.ac.newcastle.enterprisemiddleware.taxi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

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
}
