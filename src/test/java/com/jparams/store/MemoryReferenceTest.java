package com.jparams.store;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link MemoryReference}
 */
public class MemoryReferenceTest
{
    @Test
    public void testCreateAndGet()
    {
        final Object data = new Object();
        final MemoryReference<Object> reference = new MemoryReference<>(data);
        assertThat(reference.get()).isSameAs(data);
    }
}