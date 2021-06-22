import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// вылет до текущего момента времени
public class DepartureCurrentTime implements Filter {
    private final LocalDateTime now = LocalDateTime.now();

    @Override
    public List<Flight> filter(List<Flight> flightList) {
        System.out.println("Список рейсов до текущего момента времени:");

        List<Flight> list = new ArrayList<>();

        for (Flight flight : flightList) {
            for (Segment segment : flight.getSegments()) {
                if (now.isBefore(segment.getDepartureDate())) {
                    list.add(flight);
                    break;
                }
            }
        }
        return list;
    }
}
