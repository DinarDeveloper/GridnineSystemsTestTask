public class Main {
    public static void main(String[] args) {

        Filter departureToTime = new DepartureCurrentTime();
        departureToTime.filter(FlightBuilder.createFlights()).forEach(System.out::println);

        Filter arrivalDateBeforeDepartureDate = new ArrivalDateBeforeDepartureDate();
        arrivalDateBeforeDepartureDate.filter(FlightBuilder.createFlights()).forEach(System.out::println);

        Filter total = new TotalTimeMoreTwoHours();
        total.filter(FlightBuilder.createFlights()).forEach(System.out::println);
    }
}
