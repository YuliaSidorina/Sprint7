package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import steps.APISteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLoginAPITest extends BaseCourierAPITest {

    @Test
    @Description("Курьер может успешно авторизоваться")
    public void testCourierLoginSuccess() {
        Response response = APISteps.loginCourier("SpecialUserForTestingLogin", "1234");
        response.then().statusCode(200);
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
        Response response = APISteps.loginCourier("SpecialUserForTestingLogin", "wrongPassword");
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Ошибка при попытке логина с несуществующим логином")
    public void testCourierLoginNonexistentUser() {
        String nonexistentUser = "nonexistentUser" + new Faker().number().randomNumber();
        Response response = APISteps.loginCourier(nonexistentUser, "1234");
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
