public class Bus extends Vehicle {

    private String name;
    private int passengerCapacity;

    public Bus(String name, int capacity) {
        this.name = name;
        this.passengerCapacity = capacity;
    }

    @Override
    public void accelerate(int accelerationTo100) {

        System.out.println(name+" bus can accelerate to 100 metres in "+ accelerationTo100 +" seconds");

    }

    @Override
    public void stop(int stoppageFrom100) {

        System.out.println(name+" bus can stop from 100Km/hr in"+ stoppageFrom100 +" seconds");

    }

    @Override
    public void gas(int litresPerKm) {

        System.out.println(name+" bus uses averagely "+ litresPerKm +" litres per KM");

    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }
}
