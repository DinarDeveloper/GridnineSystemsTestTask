import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlightBuilderTest {

    public FlightBuilderTest(Filter filterTest) {
        this.filterTest = filterTest;
    }

    final Filter filterTest;

    List<Flight> flightList = new ArrayList<>();

    LocalDateTime currentDate = LocalDateTime.now();

    Segment segment1 = new Segment(currentDate.minusDays(6), currentDate.minusHours(5));
    Segment segment2 = new Segment(currentDate.plusDays(4), currentDate.plusHours(5));
    Segment segment3 = new Segment(currentDate.plusDays(1), currentDate.plusDays(5));
    Segment segment4 = new Segment(currentDate.plusHours(5), currentDate.minusHours(8));

    Flight flight1 = new Flight(Collections.singletonList(segment1));
    Flight flight2 = new Flight(Collections.singletonList(segment2));
    Flight flight3 = new Flight(Collections.singletonList(segment3));
    Flight flight4 = new Flight(Collections.singletonList(segment4));
    Flight flight5 = new Flight(Arrays.asList(segment1, segment2));
}