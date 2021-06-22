import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TotalTimeMoreTwoHours implements Filter {
    final int TWO_HOURS = 2; // часа

    @Override
    public List<Flight> filter(List<Flight> flightList) {
        System.out.println("Список рейсов, у которых общее время, проведённое на земле превышает два часа:");

        List<Flight> list = new ArrayList<>();
        for (Flight flight : flightList) {
            if (flight.getSegments().size() > 1) {   // если сегментов больше 1
                for (int i = 1; i < flight.getSegments().size(); i++) { // вычесялем время на земле
                    int timeOnGround = parking(flight.getSegments().get(i - 1).getArrivalDate(),
                            flight.getSegments().get(i).getDepartureDate());
                    if (timeOnGround > TWO_HOURS) {  // если время на земле больше 2 ч
                        list.add(flight);
                    }
                }
            }
        }

        return list;
    }

    // стоянка самолета между перелетами
    public int parking(LocalDateTime arrival, LocalDateTime departure) {

        return (int) Duration.between(arrival, departure).toHours();
    }
}
