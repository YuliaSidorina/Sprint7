import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тестирование API для создания курьера")
public class CourierCreationAPITest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://your.api.base.url";
    }

    @Test
    @DisplayName("Создание курьера с корректными данными")
    @Description("Тест проверяет создание курьера с корректными данными")
    public void testCourierCreationSuccess() {
        createCourier("ninja", "1234", "Saske")
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    // Other test methods...

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
}
