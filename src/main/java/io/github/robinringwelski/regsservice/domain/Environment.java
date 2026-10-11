package io.github.robinringwelski.regsservice.domain;

/**
 * The outside conditions the simulation/world operates in (sunlight and wind), calculated
 * as a function of simulated time. It has ownership of the simulation clock.
 * It only describes the weather; turning weather into power is done by each of the nodes.
 */
public class Environment {
    private double timeSeconds = 0;   // start at 00:00

    public void advance(double dtSeconds) { timeSeconds += dtSeconds; }
    public double getTimeSeconds() { return timeSeconds; }

    private double hourOfDay() { return (timeSeconds / 3600.0) % 24; }

    /** W/m²: 0 at night, peak ~1000 at noon. */
    public double getIrradiance() {
        return Math.max(0, Math.sin(Math.PI * (hourOfDay() - 6) / 12.0)) * 1000.0;
    }

    /** m/s: slowly varying around 9. */
    public double getWindSpeedMs() {
        return 9 + 4 * Math.sin(timeSeconds / 5400.0) + 1.5 * Math.sin(timeSeconds / 700.0);
    }

    public double getWindDirectionDeg() {
        return (245 + 20 * Math.sin(timeSeconds / 3000.0) + 360) % 360;
    }
}