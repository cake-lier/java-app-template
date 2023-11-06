package io.github.cakelier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class ExampleTest {
    @Test
    public void emptySetShouldHaveSizeZero() {
        Assertions.assertEquals(new HashSet<>().size(), 0);
    }
}
