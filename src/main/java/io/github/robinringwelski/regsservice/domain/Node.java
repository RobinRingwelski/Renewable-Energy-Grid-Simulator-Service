package io.github.robinringwelski.regsservice.domain;

/**
 * Base class for every asset in the simulation (wind, solar, battery).
 * Holds data and behavior they all share: identity, position, rated power,
 * power setpoint, start/stop state and the ramping of active power.
 * Each subclass defines how it reacts to the environment in {@link #tick} and
 * how it reports its state in {@link #snapshot}.
 */
public abstract sealed class Node permits SolarNode, WindNode, BatteryNode {
    private final String id;
    private final String name;
    private final double ratedPowerKw;
    private final double x, y;

    protected ConnectionStatus connection = ConnectionStatus.CONNECTED;
    protected StopSource stopSource = StopSource.NONE;
    protected double activePowerKw;
    protected double setpointKw;
    protected double theoreticalPowerKw;
    protected double reactivePowerKvar;

    protected Node(String id, String name, double ratedPowerKw, double x, double y) {
        this.id = id;
        this.name = name;
        this.ratedPowerKw = ratedPowerKw;
        this.x = x;
        this.y = y;
        this.setpointKw = ratedPowerKw;   // start unrestricted, otherwise output is always 0
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getRatedPowerKw() { return ratedPowerKw; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getActivePowerKw() { return activePowerKw; }

    public boolean isRunning() {
        return connection == ConnectionStatus.CONNECTED && stopSource == StopSource.NONE;
    }

    protected void updateActivePower(double dtSeconds) {
        double target = isRunning() ? Math.min(theoreticalPowerKw, setpointKw) : 0.0;
        double maxStep = ratedPowerKw * 0.10 * dtSeconds;   // ramp 10% of rated per second
        activePowerKw += Math.clamp(target - activePowerKw, -maxStep, maxStep);
    }

    public void setSetpointKw(double kw) { this.setpointKw = Math.clamp(kw, 0, ratedPowerKw); }
    public void stop(StopSource source) { this.stopSource = source; }
    public void start() { this.stopSource = StopSource.NONE; }

    public abstract AssetType type();
    public abstract void tick(Environment env, double dtSeconds);
    public abstract NodeSnapshot snapshot();
}
