package io.github.robinringwelski.regsservice.domain;

/**
 * An immutable, read-only picture of one node at one moment in time.
 * This is what the API will return to the frontend, so the internal mutable
 * nodes are never exposed. Each asset type has its own record with its own fields described below.
 */
public sealed interface NodeSnapshot {
    String id();
    AssetType type();
    ConnectionStatus connection();
    StopSource stopSource();
    double activePowerKw();
    double setpointKw();
    double theoreticalPowerKw();
    double ratedPowerKw();
    double reactivePowerKvar();

    record WindSnapshot(
            String id, AssetType type, ConnectionStatus connection, StopSource stopSource,
            double activePowerKw, double setpointKw, double theoreticalPowerKw,
            double ratedPowerKw, double reactivePowerKvar,
            double windSpeedMs, double windDirectionDeg, double generatorRpm, double pitchAngleDeg
    ) implements NodeSnapshot {}

    record SolarSnapshot(
            String id, AssetType type, ConnectionStatus connection, StopSource stopSource,
            double activePowerKw, double setpointKw, double theoreticalPowerKw,
            double ratedPowerKw, double reactivePowerKvar,
            double irradiance, double dcVoltageV, double inverterTempC
    ) implements NodeSnapshot {}

    record BatterySnapshot(
            String id, AssetType type, ConnectionStatus connection, StopSource stopSource,
            double activePowerKw, double setpointKw, double theoreticalPowerKw,
            double ratedPowerKw, double reactivePowerKvar,
            double stateOfChargePercent, BatteryMode mode, double cellTempC
    ) implements NodeSnapshot {}
}
