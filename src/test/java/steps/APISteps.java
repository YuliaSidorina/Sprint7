package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.OrderCreationData;
import org.junit.Assume;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class APISteps {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Step("Шаг: Получение списка заказов")
    public static Response getOrdersList() {
        return given()
                .contentType(ContentType.JSON)
                .get("/orders")
                .then()
                .extract().response();
    }

    @Step("Шаг: Создание заказа с данными: {orderData}")
    public static Response createOrder(OrderCreationData orderData) {
        try {
            String requestBody = objectMapper.writeValueAsString(orderData);
            return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/orders")
                    .then()
                    .extract().response();
        } catch (IOException e) {
            throw new RuntimeException("Error converting OrderCreationData to JSON", e);
        } catch (Exception e) {
            System.out.println("Ошибка при соединении с сервером: " + e.getMessage());
            Assume.assumeNoException("Тест пропущен из-за ошибки при соединении с сервером", e);
            return null;
        }
    }


    @Step("Шаг: Создание курьера с данными: логин={login}, пароль={password}, имя={firstName}")
    public static Response createCourier(String login, String password, String firstName) {
        try {
            Map<String, String> requestBodyMap = new HashMap<>();
            if (login != null) {
                requestBodyMap.put("login", login);
            }
            if (password != null) {
                requestBodyMap.put("password", password);
            }
            if (firstName != null) {
                requestBodyMap.put("firstName", firstName);
            }

            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            return given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/courier")
                    .then()
                    .log().all()
                    .statusCode(201)
                    .body("ok", equalTo(true))
                    .extract().response();

        } catch (IOException e) {
            throw new RuntimeException("Error converting Courier data to JSON", e);
        }
    }

    @Step("Шаг: Получение courierId из ответа")
    public static String getCourierIdFromResponse(Response response) {
        return response.getBody().jsonPath().getString("id");
    }

    @Step("Шаг: Создание курьера без логина")
    public static Response createCourierWithoutLogin(String password, String firstName) {
        try {
            Map<String, String> requestBodyMap = new HashMap<>();
            if (password != null) {
                requestBodyMap.put("password", password);
            }
            if (firstName != null) {
                requestBodyMap.put("firstName", firstName);
            }

            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            return given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/courier")
                    .then()
                    .log().all()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                    .extract().response();
        } catch (IOException e) {
            throw new RuntimeException("Error converting Courier data to JSON", e);
        }
    }

    @Step("Шаг: Создание и удаление курьера с данными: логин={login}, пароль={password}, имя={firstName}")
    public static String createAndDeleteCourier(String login, String password, String firstName) {
        Response createResponse = null;
        try {
            createResponse = createCourier(login, password, firstName);
            String courierId = getCourierIdFromResponse(createResponse);
            deleteCourier(courierId);
            return courierId;
        } catch (Exception e) {
            throw new RuntimeException("Error in createAndDeleteCourier", e);
        } finally {
            if (createResponse != null) {
                createResponse.then().log().all().statusCode(204);
            }
        }
    }

    @Step("Шаг: Логин курьера с данными: логин={login}, пароль={password}")
    public static Response loginCourier(String login, String password) {
        try {
            Map<String, String> requestBodyMap = Map.of(
                    "login", login,
                    "password", password
            );

            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .extract().response();
        } catch (IOException e) {
            throw new RuntimeException("Error converting login data to JSON", e);
        }
    }

    @Step("Шаг: Удаление курьера с id {id}")
    public static Response deleteCourier(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null for deleteCourier.");
        }

        return given()
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("ok", equalTo(true))
                .extract().response();
    }
}