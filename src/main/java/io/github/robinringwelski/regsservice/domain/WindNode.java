package io.github.robinringwelski.regsservice.domain;

/**
 * A wind turbine. Converts wind speed into power using a cut-in / rated / cut-out
 * power curve, and shuts itself down when the wind is dangerously strong. Values defined in class.
 */
public final class WindNode extends Node {
    private static final double CUT_IN = 3.0, RATED = 12.0, CUT_OUT = 25.0;

    private double windSpeedMs, windDirectionDeg, generatorRpm, pitchAngleDeg;

    public WindNode(String id, String name, double ratedPowerKw, double x, double y) {
        super(id, name, ratedPowerKw, x, y);
    }

    @Override public AssetType type() { return AssetType.WIND; }

    @Override
    public void tick(Environment env, double dt) {
        windSpeedMs = env.getWindSpeedMs();
        windDirectionDeg = env.getWindDirectionDeg();

        if (windSpeedMs >= CUT_OUT) stop(StopSource.WEATHER);
        else if (stopSource == StopSource.WEATHER) start();

        theoreticalPowerKw = powerCurve(windSpeedMs);
        updateActivePower(dt);

        pitchAngleDeg = windSpeedMs > RATED ? (windSpeedMs - RATED) * 2.0 : 0.0;
        generatorRpm = isRunning() ? 400 + 900 * Math.min(1.0, windSpeedMs / RATED) : 0.0;
    }

    private double powerCurve(double v) {
        if (v < CUT_IN || v >= CUT_OUT) return 0.0;
        if (v >= RATED) return getRatedPowerKw();
        double in3 = Math.pow(CUT_IN, 3), r3 = Math.pow(RATED, 3);
        return getRatedPowerKw() * (Math.pow(v, 3) - in3) / (r3 - in3);
    }

    @Override
    public NodeSnapshot snapshot() {
        return new NodeSnapshot.WindSnapshot(getId(), type(), connection, stopSource,
                activePowerKw, setpointKw, theoreticalPowerKw, getRatedPowerKw(), reactivePowerKvar,
                windSpeedMs, windDirectionDeg, generatorRpm, pitchAngleDeg);
    }
}