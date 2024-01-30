import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrdersCreationAPITest {

    private final OrderCreationData orderData;

    public OrdersCreationAPITest(OrderCreationData orderData) {
        this.orderData = orderData;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { generateOrderCreationData() },
                { generateOrderCreationDataWithoutColor() },
                { generateOrderCreationDataWithInvalidData() }
        });
    }

    @Test
    @Description("Тест создания заказа")
    public void testOrderCreation() {
        io.restassured.response.Response response = APISteps.createOrder(orderData);
        response.then().statusCode(orderData.getStatusCode());
        response.then().body("orders", notNullValue());
        if (orderData.getColor() != null) {
            for (String color : orderData.getColor()) {
                response.then().body("orders.color", hasItem(color));
            }
        }
    }

    private static OrderCreationData generateOrderCreationData() {
        Faker faker = new Faker();

        return new OrderCreationDataBuilder()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withAddress(faker.address().fullAddress())
                .withMetroStation(faker.number().randomDigit())
                .withPhone(faker.phoneNumber().phoneNumber())
                .withRentTime(faker.number().randomDigit())
                .withDeliveryDate(faker.date().future(5, java.util.concurrent.TimeUnit.DAYS).toString())
                .withComment(faker.lorem().sentence())
                .withColor(List.of("BLACK"))
                .withStatusCode(201)
                .build();
    }

    private static OrderCreationData generateOrderCreationDataWithoutColor() {
        Faker faker = new Faker();

        return new OrderCreationDataBuilder()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withAddress(faker.address().fullAddress())
                .withMetroStation(faker.number().randomDigit())
                .withPhone(faker.phoneNumber().phoneNumber())
                .withRentTime(faker.number().randomDigit())
                .withDeliveryDate(faker.date().future(5, java.util.concurrent.TimeUnit.DAYS).toString())
                .withComment(faker.lorem().sentence())
                .withColor(null)
                .withStatusCode(201)
                .build();
    }

    private static OrderCreationData generateOrderCreationDataWithInvalidData() {
        Faker faker = new Faker();

        return new OrderCreationDataBuilder()
                .withFirstName("") // Пустое имя
                .withLastName(faker.name().lastName())
                .withAddress(faker.address().fullAddress())
                .withMetroStation(faker.number().randomDigit())
                .withPhone(faker.phoneNumber().phoneNumber())
                .withRentTime(faker.number().randomDigit())
                .withDeliveryDate(faker.date().future(5, java.util.concurrent.TimeUnit.DAYS).toString())
                .withComment(faker.lorem().sentence())
                .withColor(List.of("BLACK"))
                .withStatusCode(400)
                .build();
    }
}
