import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierSteps {

    public static Response createCourier(String login, String password, String firstName) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);
        requestBody.put("firstName", firstName);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
    }

    public static void deleteCourier(String id) {
        given()
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
