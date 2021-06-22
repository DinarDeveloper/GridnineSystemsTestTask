

import java.util.ArrayList;
import java.util.List;

public class ArrivalDateBeforeDepartureDate implements Filter {
    @Override
    public List<Flight> filter(List<Flight> flightList) {
        System.out.println("Список рейсов с датой прилета раньше даты вылета:");

        List<Flight> list = new ArrayList<>();
        flightList.forEach(flight -> flight.getSegments()
                .stream()
                .filter(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()))
                .forEach(segment -> list.add(flight)));

//        flightList.stream().filter(flight -> flight.getSegments()
//        .stream().filter(segment -> segment.getArrivalDate().isBefore(segment.getArrivalDate())));

        return list;
    }
}
