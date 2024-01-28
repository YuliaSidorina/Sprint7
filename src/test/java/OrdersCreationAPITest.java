import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@Feature("API Тестирование: Создание заказа")
public class OrdersCreationAPITest {

    @Test
    @Description("Тест успешного создания заказа")
    public void testOrderCreationSuccess() {
        OrderCreationData orderData = new OrderCreationData(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                Arrays.asList("BLACK")
        );

        createOrder(orderData)
                .then()
                .statusCode(201)
                .body("track", equalTo(124124));
    }

    @Test
    @Description("Тест создания заказа без указания цвета")
    public void testOrderCreationWithoutColor() {
        OrderCreationData orderData = new OrderCreationData(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                null
        );

        createOrder(orderData)
                .then()
                .statusCode(201)
                .body("track", equalTo(124124));
    }

    @Test
    @Description("Тест создания заказа с невалидными данными")
    public void testOrderCreationInvalidData() {
        OrderCreationData orderData = new OrderCreationData(
                "", // Пустое имя
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                Arrays.asList("BLACK")
        );

        createOrder(orderData)
                .then()
                .statusCode(400); // Ожидаем ошибку валидации
    }

    @Step("Шаг: Создание заказа с данными: {orderData}")
    private io.restassured.response.Response createOrder(OrderCreationData orderData) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderData)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract().response();
    }

    private static class OrderCreationData {
        private String firstName;
        private String lastName;
        private String address;
        private int metroStation;
        private String phone;
        private int rentTime;
        private String deliveryDate;
        private String comment;
        private List<String> color;

        public OrderCreationData(String firstName, String lastName, String address, int metroStation,
                                 String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.metroStation = metroStation;
            this.phone = phone;
            this.rentTime = rentTime;
            this.deliveryDate = deliveryDate;
            this.comment = comment;
            this.color = color;
        }
    }
}
