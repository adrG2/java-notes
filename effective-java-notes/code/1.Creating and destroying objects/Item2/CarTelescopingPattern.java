public class CarTelescopingPattern {
    private final int doors; // required
    private final Motor motor; // required
    private final int seats; // required
    private final boolean leatherSeats; // optional
    private final boolean heatingSeats; // optional
    private final boolean spareWheel; // optional
    private final boolean automaticLightSwitching; // optional


    public CarTelescopingPattern (int doors, Motor motor, int seats) {
        this(doors, motor, seats, false);
    }

    public CarTelescopingPattern(int doors, Motor motor, int seats, boolean leatherSeats) {
        this(doors, motor, seats, leatherSeats, false);
    }

    public CarTelescopingPattern(int doors, Motor motor, int seats, boolean leatherSeats, boolean heatingSeats) {
        this(doors, motor, seats, leatherSeats, heatingSeats, false);
    }

    public CarTelescopingPattern(int doors, Motor motor, int seats, boolean leatherSeats, boolean heatingSeats, boolean spareWheel) {
        this(doors, motor, seats, leatherSeats, heatingSeats, spareWheel, false);
    }

    public CarTelescopingPattern(
        int doors,
        Motor motor, 
        int seats,
        boolean leatherSeats,
        boolean heatingSeats, 
        boolean spareWheel, 
        boolean automaticLightSwitching) {

        this.doors = doors;
        this.motor = motor;
        this.seats = seats;
        this.leatherSeats = leatherSeats;
        this.heatingSeats = heatingSeats;
        this.spareWheel = spareWheel;
        this.automaticLightSwitching = automaticLightSwitching;
    }
}

