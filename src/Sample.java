public class Sample {
    int timeStamp;
    double valueA;
    double valueB;


    public Sample(double valueA) {
        this.valueA = valueA;
        this.valueB = Double.NaN;
    }

    public Sample(double valueA, double valueB) {
        this.valueB = valueB;
        this.valueA = valueA;
    }

    public Sample(int timeStamp, double valueA, double valueB) {
        this.timeStamp = timeStamp;
        this.valueA = valueA;
        this.valueB = valueB;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "timeStamp=" + timeStamp +
                ", valueA=" + valueA +
                ", valueB=" + valueB +
                '}';
    }
}
