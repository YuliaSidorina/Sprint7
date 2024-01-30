package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import steps.APISteps;

@DisplayName("Base API Tests")
public class BaseAPITest {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    protected String testLogin;
    protected String testPassword;
    protected String testFirstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        Faker faker = new Faker();
        testLogin = generateUniqueLogin(faker);
        testPassword = "password123";
        testFirstName = generateRandomFirstName(faker);
        createTestCourier();
    }

    @Step("Шаг: Создание курьера")
    private void createTestCourier() {
        APISteps.createCourier(testLogin, testPassword, testFirstName);
    }

    @Step("Шаг: Генерация логина")
    protected String generateUniqueLogin(Faker faker) {
        return "user" + System.currentTimeMillis() + faker.number().randomNumber();
    }

    @Step("Шаг: Генерация имени")
    protected String generateRandomFirstName(Faker faker) {
        return faker.name().firstName();
    }

}
