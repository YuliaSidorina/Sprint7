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

@Feature("API Тестирование: Логин курьера")
public class CourierLoginAPITest {

    private String testLogin;
    private String testPassword;
    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        testLogin = "existingUser";
        testPassword = "password123";
        courierId = createCourier(testLogin, testPassword, "JohnDoe")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        deleteCourier(courierId);
    }

    @Test
    @Description("Тест проверяет успешный логин курьера")
    public void testCourierLoginSuccess() {
        loginCourier(testLogin, testPassword)
                .then()
                .statusCode(200)
                .body("id", equalTo(Integer.parseInt(courierId)));
    }

    @Test
    @Description("Тест проверяет попытку логина без обязательных данных")
    public void testCourierLoginMissingData() {
        loginCourier("", "password123")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Тест проверяет попытку логина с неправильным паролем")
    public void testCourierLoginIncorrectPassword() {
        loginCourier(testLogin, "wrongPassword")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Тест проверяет попытку логина с несуществующим логином")
    public void testCourierLoginNonexistentUser() {
        loginCourier("nonexistentUser", "password123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Шаг: Логин курьера с данными: логин={login}, пароль={password}")
    private io.restassured.response.Response loginCourier(String login, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
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
}
