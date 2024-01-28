import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@Feature("API Тестирование: Создание курьера")
public class CourierCreationAPITest {

    private String testLogin;
    private String testPassword;
    private String testFirstName;
    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        testLogin = generateUniqueLogin();
        testPassword = "password123";
        testFirstName = "JohnDoe";
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }

    @Test
    @Description("Тест проверяет успешное создание и удаление курьера")
    public void testCourierCreationAndDeletion() {
        courierId = createCourier(testLogin, testPassword, testFirstName)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract()
                .path("id");
        deleteCourier(courierId);
    }

    @Step("Шаг: Создание курьера с данными: логин={login}, пароль={password}, имя={firstName}")
    private io.restassured.response.Response createCourier(String login, String password, String firstName) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\", \"firstName\":\"" + firstName + "\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
    }

    @Step("Шаг: Удаление курьера с id {id}")
    private void deleteCourier(String id) {
        given()
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    private String generateUniqueLogin() {
        return "user" + System.currentTimeMillis();
    }
}
