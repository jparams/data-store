package com.jparams.store.comparison.number;

import java.math.RoundingMode;

import com.jparams.store.Store;
import com.jparams.store.index.Index;
import com.jparams.store.memory.MemoryStore;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalingComparisonPolicyIT
{
    private Store<DummyClass> subject;
    private Index<DummyClass> index;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        index = subject.index(DummyClass::getNum, new ScalingComparisonPolicy(2, RoundingMode.UP));
    }

    @Test
    public void testPolicy()
    {
        final DummyClass dummyClass = new DummyClass(1.523);
        subject.add(dummyClass);

        assertThat(index.getFirst(1.523)).isSameAs(dummyClass);
        assertThat(index.getFirst(1.523456)).isSameAs(dummyClass);
        assertThat(index.getFirst(1.53)).isSameAs(dummyClass);
        assertThat(index.getFirst(1.52)).isNull();
    }

    private static class DummyClass
    {
        private final double num;

        DummyClass(final double num)
        {
            this.num = num;
        }

        double getNum()
        {
            return num;
        }
    }
}