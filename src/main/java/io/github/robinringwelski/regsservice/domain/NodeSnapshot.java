package io.github.robinringwelski.regsservice.domain;

public sealed interface NodeSnapshot {

    record SolarSnapshot(String id, NodeStatus status, double powerKw, double irradiance) implements NodeSnapshot {}

    record WindSnapshot(String id, NodeStatus status, double powerKw, double windSpeedMs) implements NodeSnapshot {}

    record BatterySnapshot(String id, NodeStatus status, double powerKw, double chargePercent) implements NodeSnapshot {}
}
