package com.jparams.store.index.reducer;

import java.util.Arrays;

import com.jparams.store.index.Element;
import com.jparams.store.index.reducer.LimitReducer.Retain;
import com.jparams.store.memory.MemoryReference;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LimitReducerTest
{
    @Test
    public void testRetainOldest()
    {
        final LimitReducer<String, String> subject = new LimitReducer<>(2, Retain.OLDEST);

        final Element<String> element1 = new Element<>(new MemoryReference<>("element1"));
        final Element<String> element2 = new Element<>(new MemoryReference<>("element2"));
        final Element<String> element3 = new Element<>(new MemoryReference<>("element3"));
        final Element<String> element4 = new Element<>(new MemoryReference<>("element4"));

        subject.reduce("key", Arrays.asList(element1, element2, element3, element4));

        assertThat(element1).extracting("removed").contains(false);
        assertThat(element2).extracting("removed").contains(false);
        assertThat(element3).extracting("removed").contains(true);
        assertThat(element4).extracting("removed").contains(true);
    }

    @Test
    public void testRetainNewest()
    {
        final LimitReducer<String, String> subject = new LimitReducer<>(2, Retain.NEWEST);

        final Element<String> element1 = new Element<>(new MemoryReference<>("element1"));
        final Element<String> element2 = new Element<>(new MemoryReference<>("element2"));
        final Element<String> element3 = new Element<>(new MemoryReference<>("element3"));
        final Element<String> element4 = new Element<>(new MemoryReference<>("element4"));

        subject.reduce("key", Arrays.asList(element1, element2, element3, element4));

        assertThat(element1).extracting("removed").contains(true);
        assertThat(element2).extracting("removed").contains(true);
        assertThat(element3).extracting("removed").contains(false);
        assertThat(element4).extracting("removed").contains(false);
    }
}