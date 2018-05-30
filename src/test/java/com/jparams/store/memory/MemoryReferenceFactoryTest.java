package com.jparams.store.memory;

import com.jparams.store.reference.Reference;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryReferenceFactoryTest
{
    @Test
    public void testCreate()
    {
        final Object obj = new Object();
        final Reference<Object> reference = new MemoryReferenceFactory<>().createReference(obj);
        assertThat(reference.get()).isSameAs(obj);
    }
}