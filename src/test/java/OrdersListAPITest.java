import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DisplayName("Orders API Tests")
public class OrdersAPITest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/api/v1";
    private static final String ORDERS_ENDPOINT = "/orders";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @Story("Create Order")
    @DisplayName("Create Order Test")
    @Description("Test the creation of an order with different color options")
    public void createOrderTest() {
        String orderId = createOrder();
        assertOrderCreation(orderId);
    }

    @Step("Create Order")
    private String createOrder() {
        String requestBody = "{\n" +
                "    \"firstName\": \"Naruto\",\n" +
                "    \"lastName\": \"Uchiha\",\n" +
                "    \"address\": \"Konoha, 142 apt.\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 35\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2020-06-06\",\n" +
                "    \"comment\": \"Saske, come back to Konoha\",\n" +
                "    \"color\": [\"BLACK\", \"GREY\"]\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(ORDERS_ENDPOINT);

        assertEquals("Unexpected status code", 201, response.getStatusCode());
        assertTrue("Track information not found in the response", response.getBody().asString().contains("track"));
        return response.jsonPath().getString("id");
    }

    @Step("Verify Order Creation")
    private void assertOrderCreation(String orderId) {
    }
}
