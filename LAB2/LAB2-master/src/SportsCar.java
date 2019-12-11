public class SportsCar extends Vehicle implements Automobile {
    private String name;
    private int numberOfSeats;
    private int numberOfSuitcases;

    public SportsCar(String name, int numberOfSeats, int numberOfSuitcases) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.numberOfSuitcases = numberOfSuitcases;
    }

    @Override
    public void accelerate(int accelerationTo100) {

        System.out.println(name+" Sports car can accelerate to 100 metres in "+ accelerationTo100 +" seconds");

    }

    @Override
    public void stop(int stoppageFrom100) {

        System.out.println(name+" Sports Car can stop from 100Km/hr in"+ stoppageFrom100 +" seconds");
    }

    @Override
    public void gas(int litresPerKm) {
        System.out.println(name+" Sports car uses averagely "+ litresPerKm +" litres per KM");
    }

    @Override
    public void numberOfSeats(int numberOfSeats) {

        System.out.println(name+" Sports car has "+ numberOfSeats +" number of seats");

    }

    @Override
    public void cargoSpace(int numberOfSuitCases) {

        System.out.println(name+" Sports car has a cargo capacity that can store "+ numberOfSuitCases +" Suitcases");

    }
}
