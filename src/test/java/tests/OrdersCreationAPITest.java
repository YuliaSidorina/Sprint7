package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import models.OrderCreationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.APISteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrdersCreationAPITest {

    public final List<String> colors;

    public OrdersCreationAPITest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { List.of("BLACK") },
                { List.of("GREY") },
                { List.of("BLACK", "GREY") },
                { null }
        });
    }

    @Test
    @Description("Тест создания заказа")
    public void testOrderCreation() {
        OrderCreationData orderData = generateOrderCreationData(colors);
        io.restassured.response.Response response = APISteps.createOrder(orderData);
        response.then().statusCode(orderData.getStatusCode());
        response.then().body("track", notNullValue());
        if (colors != null) {
            for (String color : colors) {
                response.then().body("color", hasItem(color));
            }
        }
    }

    private static OrderCreationData generateOrderCreationData(List<String> colors) {
        Faker faker = new Faker();

        return new OrderCreationData(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.number().randomDigit(),
                faker.phoneNumber().phoneNumber(),
                faker.number().randomDigit(),
                faker.date().future(5, java.util.concurrent.TimeUnit.DAYS).toString(),
                faker.lorem().sentence(),
                colors,
                201
        );
    }
}
