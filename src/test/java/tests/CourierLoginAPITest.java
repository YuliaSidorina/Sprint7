package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import steps.APISteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginAPITest extends BaseAPITest {

    private String courierId;

    @Override
    public void setUp() {
        super.setUp();
        courierId = APISteps.createAndDeleteCourier(testLogin, testPassword, testFirstName);
    }

    @After
    public void tearDown() {
        APISteps.deleteCourier(courierId);
    }

    @Test
    @Description("Курьер может успешно авторизоваться")
    public void testCourierLoginSuccess() {
        APISteps.createCourier(testLogin, testPassword, testFirstName);

        Response response = APISteps.loginCourier(testLogin, testPassword);
        response.then().statusCode(200);
        response.then().body("id", notNullValue());
    }

    @Test
    @Description("Ошибка при попытке логина без обязательных данных")
    public void testCourierLoginMissingData() {
        Response response = APISteps.loginCourier("", testPassword);
        response.then().statusCode(400);
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Ошибка при попытке логина с неправильным паролем")
    public void testCourierLoginIncorrectPassword() {
        Response response = APISteps.loginCourier(testLogin, "wrongPassword");
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Ошибка при попытке логина с несуществующим логином")
    public void testCourierLoginNonexistentUser() {
        Response response = APISteps.loginCourier("nonexistentUser", testPassword);
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
