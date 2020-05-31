public class CarJavaBeanPattern {
    private final int doors; // required
    private final Motor motor; // required
    private final int seats; // required
    private final boolean leatherSeats; // optional
    private final boolean heatingSeats; // optional
    private final boolean spareWheel; // optional
    private final boolean automaticLightSwitching; // optional

    public CarJavaBeanPattern() {}

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setLeatherSeats(boolean leatherSeats) {
        this.leatherSeats = leatherSeats;
    }

    public void setHeatingSeats(boolean heatingSeats) {
        this.heatingSeats = heatingSeats;
    }

    public void setSpareWheel(boolean spareWheel) {
        this.spareWheel = spareWheel;
    }

    public void setAutomaticLightSwitching(boolean automaticLightSwitching) {
        this.automaticLightSwitching = automaticLightSwitching;
    }

    private void example() {
        CarJavaBeanPattern carExample = new CarJavaBeanPattern();
        carExample.setMotor(Motor.create('110cv', 'hdi'));
        carExample.setDoors(4);
        carExample.setSeats(4);
        carExample.setHeatingSeats(true);
    }

}