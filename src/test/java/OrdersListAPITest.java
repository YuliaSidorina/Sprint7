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
public class OrdersListAPITest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/api/v1";
    private static final String ORDERS_ENDPOINT = "/orders";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @Story("Get Orders List")
    @DisplayName("Get Orders List Test")
    @Description("Test the retrieval of the orders list")
    public void getOrdersListTest() {
        assertOrdersList();
    }

    @Step("Get Orders List")
    private void assertOrdersList() {
        Response response = given()
                .contentType(ContentType.JSON)
                .get(ORDERS_ENDPOINT);

        assertEquals("Unexpected status code", 200, response.getStatusCode());
        assertTrue("Orders list not found in the response", response.getBody().asString().contains("orders"));
    }
}
