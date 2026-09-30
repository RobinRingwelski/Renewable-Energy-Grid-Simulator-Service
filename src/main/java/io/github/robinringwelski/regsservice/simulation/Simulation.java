package io.github.robinringwelski.regsservice.simulation;

import io.github.robinringwelski.regsservice.domain.Environment;

public class Simulation {
    private Environment environment = new Environment();

    public Simulation() {
        environment.EnvironmentInitializer();
    }
}
