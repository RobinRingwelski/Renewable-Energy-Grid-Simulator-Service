package io.github.robinringwelski.regsservice;

import io.github.robinringwelski.regsservice.simulation.Simulation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RegsServiceApplicationTests {

    @Test
    void producesPowerAfterRunning() {
        Simulation sim = new Simulation();
        for (int i = 0; i < 600; i++) sim.step(1.0);   // 10 simulated minutes
        sim.snapshots().forEach(System.out::println);
        assertTrue(sim.snapshots().stream().anyMatch(s -> s.activePowerKw() > 0));
    }

}
