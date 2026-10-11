package io.github.robinringwelski.regsservice.simulation;

import io.github.robinringwelski.regsservice.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the simulated environment. Owns the environment and the list of nodes, advances
 * time with {@link #step}, and produces snapshots of the current state.
 * This is the single entry point the API layer will talk to.
 */
public class Simulation {
    private final Environment environment = new Environment();
    private final List<Node> nodes = new ArrayList<>();

    public Simulation() {
        nodes.add(new WindNode("wind-1", "Turbine 1", 850, 0.2, 0.3));
        nodes.add(new SolarNode("solar-1", "Array A", 1200, 0.6, 0.5));
        nodes.add(new BatteryNode("bat-1", "Storage 1", 1000, 2000, 60, 0.8, 0.2));
    }

    public void step(double dtSeconds) {
        environment.advance(dtSeconds);
        for (Node n : nodes) n.tick(environment, dtSeconds);
    }

    public List<NodeSnapshot> snapshots() {
        return nodes.stream().map(Node::snapshot).toList();
    }
}
