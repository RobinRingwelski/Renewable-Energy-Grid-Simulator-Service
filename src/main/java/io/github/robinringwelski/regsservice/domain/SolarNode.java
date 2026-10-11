package io.github.robinringwelski.regsservice.domain;

/**
 * A solar array. Its available power scales with the current irradiance aka
 * (sunlight) and drops to zero at night.
 */
public final class SolarNode extends Node {
    private double irradiance, dcVoltageV, inverterTempC;

    public SolarNode(String id, String name, double ratedPowerKw, double x, double y) {
        super(id, name, ratedPowerKw, x, y);
    }

    @Override public AssetType type() { return AssetType.SOLAR; }

    @Override
    public void tick(Environment env, double dt) {
        irradiance = env.getIrradiance();
        theoreticalPowerKw = Math.min(getRatedPowerKw(), getRatedPowerKw() * irradiance / 1000.0);
        updateActivePower(dt);

        dcVoltageV = isRunning() && irradiance > 20 ? 600 + 0.3 * irradiance : 0.0;
        inverterTempC = 25 + 20 * (activePowerKw / getRatedPowerKw());
    }

    @Override
    public NodeSnapshot snapshot() {
        return new NodeSnapshot.SolarSnapshot(getId(), type(), connection, stopSource,
                activePowerKw, setpointKw, theoreticalPowerKw, getRatedPowerKw(), reactivePowerKvar,
                irradiance, dcVoltageV, inverterTempC);
    }
}
