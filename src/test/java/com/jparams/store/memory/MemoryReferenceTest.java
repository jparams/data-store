package com.jparams.store.memory;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryReferenceTest
{
    @Test
    public void testCreateAndGet()
    {
        final Object data = new Object();
        final MemoryReference<Object> reference = new MemoryReference<>(data);
        assertThat(reference.get()).isSameAs(data);
    }

    @Test
    public void testToString()
    {
        final String data = "abcd";
        final MemoryReference<String> reference = new MemoryReference<>(data);
        assertThat(reference.toString()).isEqualTo(data);
    }
}