package io.github.robinringwelski.regsservice.api;

import io.github.robinringwelski.regsservice.domain.NodeInfo;
import io.github.robinringwelski.regsservice.simulation.Simulation;
import io.github.robinringwelski.regsservice.simulation.SimulationSnapshot;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** REST endpoints for reading the plant and advancing the simulation. */
@RestController
@RequestMapping("/api")
public class SimulationController {

    private final Simulation simulation;

    public SimulationController(Simulation simulation) {   // Spring injects the bean
        this.simulation = simulation;
    }

    @GetMapping("/plant")
    public List<NodeInfo> plant() {
        return simulation.layout();
    }

    @GetMapping("/plant/state")
    public SimulationSnapshot state() {
        return simulation.snapshot();
    }

    @PostMapping("/simulation/step")
    public SimulationSnapshot step(@RequestParam(defaultValue = "1") int steps,
                                   @RequestParam(defaultValue = "1.0") double dt) {
        int n = Math.clamp(steps, 1, 3600);          // This should guard against absurd requests
        double dtSeconds = Math.clamp(dt, 0.1, 60.0);
        for (int i = 0; i < n; i++) simulation.step(dtSeconds);
        return simulation.snapshot();
    }
}
