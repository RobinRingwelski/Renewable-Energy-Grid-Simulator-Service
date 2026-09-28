package io.github.robinringwelski.regsservice.domain;

public abstract sealed class Node permits SolarNode, WindNode, BatteryNode {
}
