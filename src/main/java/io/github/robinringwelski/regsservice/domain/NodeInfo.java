package io.github.robinringwelski.regsservice.domain;

/** Static description of a node (never changes while running): used by the frontend to draw the map. */
public record NodeInfo(String id, String name, AssetType type, double ratedPowerKw, double x, double y) {}
