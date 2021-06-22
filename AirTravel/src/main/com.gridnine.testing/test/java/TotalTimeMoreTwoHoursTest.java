import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TotalTimeMoreTwoHoursTest extends FlightBuilderTest {

    public TotalTimeMoreTwoHoursTest() {
        super(new TotalTimeMoreTwoHours());
    }

    @Before
    public void setUp() {
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);
        flightList.add(flight5);
    }

    @Test
    public void filter() {
        final List<Flight> actual = filterTest.filter(flightList);
        final List<Flight> expected = Collections.singletonList(flight5);
        assertEquals(expected, actual);
    }
}
