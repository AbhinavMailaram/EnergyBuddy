package com.energybuddy.util;

/**
 * Utility class for energy calculations.
 * Formula: P (Power in Watts) → E = P × t (Energy = Power × time) → Wh (Watt-hours) → kWh (Kilowatt-hours)
 */
public class EnergyCalculator {

    private EnergyCalculator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Calculate energy in Watt-hours (Wh)
     * @param powerWatts Power rating in Watts
     * @param timeHours Time duration in hours
     * @return Energy in Watt-hours
     */
    public static double calculateEnergyWh(double powerWatts, double timeHours) {
        if (powerWatts < 0 || timeHours < 0) {
            throw new IllegalArgumentException("Power and time must be non-negative");
        }
        return powerWatts * timeHours;
    }

    /**
     * Convert Watt-hours to Kilowatt-hours
     * @param energyWh Energy in Watt-hours
     * @return Energy in Kilowatt-hours
     */
    public static double calculateEnergyKwh(double energyWh) {
        if (energyWh < 0) {
            throw new IllegalArgumentException("Energy must be non-negative");
        }
        return energyWh / 1000.0;
    }

    /**
     * Calculate energy directly in kWh
     * @param powerWatts Power rating in Watts
     * @param timeHours Time duration in hours
     * @return Energy in Kilowatt-hours
     */
    public static double calculateEnergyKwhDirect(double powerWatts, double timeHours) {
        double energyWh = calculateEnergyWh(powerWatts, timeHours);
        return calculateEnergyKwh(energyWh);
    }
}
