public class CarBuilderPattern {
    private final int doors; 
    private final Motor motor; 
    private final int seats; 
    private final boolean leatherSeats; 
    private final boolean heatingSeats; 
    private final boolean spareWheel; 
    private final boolean automaticLightSwitching; 


    public static class Builder {
        // Required Parameters
        private final int doors; 
        private final Motor motor; 
        private final int seats; 
    
        // Optional Parameters (Quitamos el final)
        private boolean leatherSeats; 
        private boolean heatingSeats; 
        private boolean spareWheel; 
        private boolean automaticLightSwitching; 

        public Builder(int doors, Motor motor, int seats) {
            this.doors = doors;
            this.motor = motor;
            this.seats = seats;
        }

        public Builder leatherSeats(boolean leatherSeats) {
            this.leatherSeats = leatherSeats;
            return this;
        }

        public Builder heatingSeats(boolean heatingSeats) {
            this.heatingSeats = heatingSeats;
            return this;
        }

        public Builder spareWheel(boolean spareWheel) {
            this.spareWheel = spareWheel;
            return this;
        }

        public Builder automaticLightSwitching(boolean automaticLightSwitching) {
            this.automaticLightSwitching = automaticLightSwitching;
            return this;
        }

        public CarBuilderPattern build(Builder builder) {
            return new CarBuilderPattern(this);
        }
    
    }


    private CarBuilderPattern(Builder Builder) {
        this.doors = builder.doors;
        this.motor = builder.motor;
        this.seats = builder.seats;
        this.leatherSeats = builder.leatherSeats;
        this.heatingSeats = builder.heatingSeats;
        this.spareWheel = builder.spareWheel;
        this.automaticLightSwitching = builder.automaticLightSwitching;
    }

}