package javatro_core;

public record HandResult(String handName, int chips, int multiplier) {
    @Override
    public String toString() {
        return String.format("%s (Base Chips: %d, Multiplier: %d)", handName, chips, multiplier);
    }
}
