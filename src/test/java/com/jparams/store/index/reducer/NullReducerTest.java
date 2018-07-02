package com.jparams.store.index.reducer;

import java.util.Arrays;
import java.util.function.Function;

import com.jparams.store.index.Element;
import com.jparams.store.memory.MemoryReference;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NullReducerTest
{
    private NullReducer<Object, Object> subject;

    @Before
    public void setUp()
    {
        subject = new NullReducer<>(Function.identity());
    }

    @Test
    public void testReduceNulls()
    {
        final Element<Object> element0 = new Element<>(new MemoryReference<>(null));
        final Element<Object> element1 = new Element<>(new MemoryReference<>("element1"));
        final Element<Object> element2 = new Element<>(new MemoryReference<>("element2"));
        final Element<Object> element3 = new Element<>(new MemoryReference<>(null));

        subject.reduce("key", Arrays.asList(element0, element1, element2, element3));

        assertThat(element0).extracting("removed").contains(true);
        assertThat(element1).extracting("removed").contains(false);
        assertThat(element2).extracting("removed").contains(false);
        assertThat(element3).extracting("removed").contains(true);
    }
}