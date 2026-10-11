package io.github.robinringwelski.regsservice.domain;

/**
 * A battery storage unit. It does not react to the weather at all: the grid "asks" it to
 * charge (surplus) or discharge (deficit) through {@link #dispatch}, and it
 * tracks its stored energy within capacity and power limits.
 */
public final class BatteryNode extends Node {
    private final double capacityKwh;
    private final double efficiency;   // one-way, e.g. 0.95
    private double storedKwh;
    private double cellTempC = 25;

    public BatteryNode(String id, String name, double ratedPowerKw, double capacityKwh,
                       double initialChargePercent, double x, double y) {
        super(id, name, ratedPowerKw, x, y);
        this.capacityKwh = capacityKwh;
        this.efficiency = 0.95;
        this.storedKwh = capacityKwh * initialChargePercent / 100.0;
    }

    @Override public AssetType type() { return AssetType.BATTERY; }

    @Override
    public void tick(Environment env, double dt) {
        activePowerKw = 0.0;   // idle unless the grid dispatches it this step (added later)
        cellTempC = 25 + 10 * Math.abs(activePowerKw) / getRatedPowerKw();
    }

    /** +kW = discharge (supply grid), -kW = charge. Returns what was actually delivered/absorbed. */
    public double dispatch(double requestedKw, double dtSeconds) {
        if (!isRunning()) { activePowerKw = 0; return 0; }
        double hours = dtSeconds / 3600.0;
        double kw = Math.clamp(requestedKw, -getRatedPowerKw(), getRatedPowerKw());

        if (kw > 0) {
            kw = Math.min(kw, storedKwh * efficiency / hours);
            storedKwh -= kw * hours / efficiency;
        } else if (kw < 0) {
            kw = -Math.min(-kw, (capacityKwh - storedKwh) / (efficiency * hours));
            storedKwh += -kw * hours * efficiency;
        }
        theoreticalPowerKw = Math.abs(requestedKw);
        activePowerKw = kw;
        return kw;
    }

    private BatteryMode mode() {
        if (activePowerKw > 0.5) return BatteryMode.DISCHARGING;
        if (activePowerKw < -0.5) return BatteryMode.CHARGING;
        return BatteryMode.IDLE;
    }

    @Override
    public NodeSnapshot snapshot() {
        return new NodeSnapshot.BatterySnapshot(getId(), type(), connection, stopSource,
                activePowerKw, setpointKw, theoreticalPowerKw, getRatedPowerKw(), reactivePowerKvar,
                storedKwh / capacityKwh * 100.0, mode(), cellTempC);
    }
}
