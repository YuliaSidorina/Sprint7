package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import models.OrderCreationData;
import org.junit.Before;
import steps.APISteps;

import java.util.Arrays;
import java.util.List;

public class BaseOrderAPITest {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    protected String testFirstName;
    protected String testLastName;
    protected String testAddress;
    protected int testMetroStation;
    protected String testPhone;
    protected int testRentTime;
    protected String testDeliveryDate;
    protected String testComment;
    protected List<String> testColors;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        Faker faker = new Faker();
        testFirstName = faker.name().firstName();
        testLastName = faker.name().lastName();
        testAddress = faker.address().fullAddress();
        testMetroStation = generateRandomMetroStation(faker);
        testPhone = faker.phoneNumber().phoneNumber();
        testRentTime = faker.number().randomDigit();
        testDeliveryDate = faker.date().future(5, java.util.concurrent.TimeUnit.DAYS).toString();
        testComment = faker.lorem().sentence();
        testColors = Arrays.asList("BLACK", "GREY"); // Можно задать разные варианты цветов
    }

    @Step("Шаг: Генерация станции метро")
    protected int generateRandomMetroStation(Faker faker) {
        return faker.number().randomDigit();
    }

    @Step("Шаг: Создание заказа")
    protected void createTestOrder() {
        OrderCreationData orderData = new OrderCreationData();
        orderData.setFirstName(testFirstName);
        orderData.setLastName(testLastName);
        orderData.setAddress(testAddress);
        orderData.setMetroStation(testMetroStation);
        orderData.setPhone(testPhone);
        orderData.setRentTime(testRentTime);
        orderData.setDeliveryDate(testDeliveryDate);
        orderData.setComment(testComment);
        orderData.setColor(testColors);

        APISteps.createOrder(orderData);
    }
}
