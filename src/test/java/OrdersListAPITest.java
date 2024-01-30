import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты API Заказов")
public class OrdersListAPITest extends BaseAPITest {

    private static final String ORDERS_ENDPOINT = "/api/v1/orders";

    @Test
    @Story("Получение списка заказов")
    @DisplayName("Тест получения списка заказов")
    @Description("Тест на получение списка заказов")
    public void getOrdersListTest() {
        Response response = APISteps.getOrdersList();

        assertEquals("Неожиданный статус-код", 200, response.getStatusCode());
        assertTrue("Список заказов не найден в ответе", response.getBody().asString().contains("orders"));
    }
}
