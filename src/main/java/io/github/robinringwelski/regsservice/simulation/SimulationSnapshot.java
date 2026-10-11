package io.github.robinringwelski.regsservice.simulation;

import io.github.robinringwelski.regsservice.domain.NodeSnapshot;

import java.util.List;

/** Live state of the whole plant at one moment: simulated time plus every node's snapshot. As List of nodes */
public record SimulationSnapshot(double timeSeconds, List<NodeSnapshot> nodes) {}
