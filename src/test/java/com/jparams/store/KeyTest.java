package com.jparams.store;

import java.util.Arrays;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link Key}
 */
public class KeyTest
{
    @Test
    public void testCreateEmptyKey()
    {
        assertThat(Key.none().getValues()).isEmpty();
    }

    @Test
    public void testCreateKeyWithSingleValue()
    {
        assertThat(Key.on("abc").getValues()).containsExactly("abc");
    }

    @Test
    public void testCreateKeyWithMultipleValues()
    {
        assertThat(Key.onEach("1", "2").getValues()).containsExactlyInAnyOrder("1", "2");
    }

    @Test
    public void testCreateKeyWithCollection()
    {
        assertThat(Key.onEach(Arrays.asList("1", "2")).getValues()).containsExactlyInAnyOrder("1", "2");
    }
}