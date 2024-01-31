package tests;

import models.OrderCreationData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.APISteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrdersCreationAPITest extends BaseOrderAPITest {

    private final String colors;

    @Before
    public void setUp() {
        super.setUp();
    }

    public OrdersCreationAPITest(String colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"BLACK"},
                {"BLACK, GREY"},
                {""}
        });
    }

    @Test
    public void testOrderCreationWithColors() {
        List<String> colorList = colors.isEmpty() ? null : Arrays.asList(colors.split(", "));
        OrderCreationData orderData = new OrderCreationData(
                testFirstName,
                testLastName,
                testAddress,
                testMetroStation,
                testPhone,
                testRentTime,
                testDeliveryDate,
                testComment,
                colorList,
                201
        );

        APISteps.createOrder(orderData);
    }
}
