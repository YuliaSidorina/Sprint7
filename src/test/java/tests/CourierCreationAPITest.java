package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.APISteps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierCreationAPITest extends BaseCourierAPITest {

    private String courierId;
    private static final Faker faker = new Faker();

    @Before
    public void setUp() {
        super.setUp();
        testLogin = generateUniqueLogin(new Faker());
        testPassword = generatePassword(new Faker());
        testFirstName = generateRandomFirstName(new Faker());
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            APISteps.deleteCourier(courierId);
        }
    }

    @Test
    @Description("Курьера можно создать")
    public void testCourierCreation() {
        Response createResponse = APISteps.createCourier(testLogin, testPassword, testFirstName);

        assertEquals(201, createResponse.getStatusCode());
        assertTrue(createResponse.getBody().jsonPath().getBoolean("ok"));
        courierId = createResponse.getBody().jsonPath().getString("id");
    }

    @Test
    @Description("Ошибка при создании курьера без логина")
    public void testCourierCreationWithoutLogin() {
        String randomPassword = faker.internet().password();
        String randomFirstName = faker.name().firstName();

        Response response = APISteps.createCourierWithoutLogin(randomPassword, randomFirstName);

        assertEquals(400, response.getStatusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Description("Успешный запрос возвращает код ответа 201")
    public void testCourierCreationResponseCode() {
        Response response = APISteps.createCourier(testLogin, testPassword, testFirstName);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    @Description("Успешный запрос возвращает ok: true")
    public void testCourierCreationSuccessResponse() {
        Response response = APISteps.createCourier(testLogin, testPassword, testFirstName);
        assertTrue(response.getBody().jsonPath().getBoolean("ok"));
    }

    @Test
    @Description("Успешное удаление курьера")
    public void testCourierDeletion() {
        try {
            Response createResponse = APISteps.createCourier(testLogin, testPassword, testFirstName);
            assertEquals(201, createResponse.getStatusCode());
            assertTrue(createResponse.getBody().jsonPath().getBoolean("ok"));

            courierId = createResponse.getBody().jsonPath().getString("id");

            Response deleteResponse = APISteps.deleteCourier(courierId);
            assertEquals(200, deleteResponse.getStatusCode());
            assertTrue(deleteResponse.getBody().jsonPath().getBoolean("ok"));
        } catch (Exception e) {
            System.err.println("Exception during testCourierDeletion: " + e.getMessage());
        }
    }
}