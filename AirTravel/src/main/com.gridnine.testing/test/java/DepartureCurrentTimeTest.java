import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DepartureCurrentTimeTest extends FlightBuilderTest {

    public DepartureCurrentTimeTest() {
        super(new DepartureCurrentTime());
    }

    @Before
    public void setUp() {
        flightList.add(flight1);
        flightList.add(flight2);
        flightList.add(flight3);
        flightList.add(flight4);
    }

    @Test
    public void filter() {
        final List<Flight> actual = filterTest.filter(flightList);
        final List<Flight> expected = Arrays.asList(flight2, flight3, flight4);
        assertEquals(expected, actual);
    }
}
