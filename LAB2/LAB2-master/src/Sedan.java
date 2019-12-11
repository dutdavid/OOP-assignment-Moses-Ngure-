public class Sedan extends Vehicle implements Automobile {
    private String name;
    private int numberOfSeats;
    private int numberOfSuitcases;

    public Sedan() {
    }

    public Sedan(String name, int numberOfSeats, int numberOfSuitcases) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.numberOfSuitcases = numberOfSuitcases;
    }

    /*
       A subclass provides the specific implementation of the method
       that has been declared by one of its parent class
     */

    @Override
    public void accelerate(int accelerationTo100) {

        System.out.println(name+" sedan can accelerate to 100 Km/h in "+ accelerationTo100 +" seconds");

    }

    public void accelerate(){

        System.out.println("Average acceleration of sedans to 100Km/h is 8 seconds ");
    }

    @Override
    public void stop(int stoppageFrom100) {

        System.out.println(name+" sedan can stop from 100Km/hr in"+ stoppageFrom100 +" seconds");
    }

    @Override
    public void gas(int litresPerKm) {
        System.out.println(name+" sedan uses averagely "+ litresPerKm +" litres per KM");
    }

    @Override
    public void numberOfSeats(int numberOfSeats) {

        System.out.println(name+" sedan has "+ numberOfSeats +" number of seats");

    }

    @Override
    public void cargoSpace(int numberOfSuitCases) {

        System.out.println(name+" sedan has a cargo capacity that can store "+ numberOfSuitCases +" Suitcases");

    }
}
