package tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.APISteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class OrdersListAPITest extends BaseOrderAPITest {

    private final Integer courierId;
    private final String nearestStation;
    private final Integer limit;
    private final Integer page;

    @Before
    public void setUp() {
        super.setUp();
    }

    public OrdersListAPITest(Integer courierId, String nearestStation, Integer limit, Integer page) {
        this.courierId = (courierId != null) ? courierId : generateRandomCourierId();
        this.nearestStation = (nearestStation != null) ? nearestStation : generateRandomStation();
        this.limit = limit;
        this.page = page;
    }

    private static int generateRandomCourierId() {
        return 100000 + new Random().nextInt(900000);
    }

    private static String generateRandomStation() {
        return String.valueOf(1 + new Random().nextInt(237));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, null, null, null},
                {generateRandomCourierId(), null, null, null},
                {generateRandomCourierId(), generateRandomStation(), null, null}
        });
    }

    @Test
    public void testOrdersList() {
        APISteps.getOrdersList(courierId, nearestStation, limit, page);
    }
}
