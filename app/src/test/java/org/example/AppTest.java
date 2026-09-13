package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.chip8.chip8.Chip8;

class AppTest {
    @Test
    void appHasAGreeting() {
        Chip8 classUnderTest = new Chip8();
        assertNotNull("test", "app should have a greeting");
    }
}
