package com.jparams.store.index.reducer;

import java.util.Arrays;

import com.jparams.store.index.Element;
import com.jparams.store.memory.MemoryReference;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MinReducerTest
{
    @Test
    public void testReducerWithNullGreater()
    {
        final MinReducer<String, String> subject = new MinReducer<>(str -> str, true);

        final Element<String> element0 = new Element<>(new MemoryReference<>(null));
        final Element<String> element1 = new Element<>(new MemoryReference<>("element1"));
        final Element<String> element2 = new Element<>(new MemoryReference<>("element2"));
        final Element<String> element3 = new Element<>(new MemoryReference<>("element3"));
        final Element<String> element4 = new Element<>(new MemoryReference<>("element4"));
        final Element<String> element5 = new Element<>(new MemoryReference<>(null));

        subject.reduce("key", Arrays.asList(element0, element3, element3, element4, element2, element1, element5));

        assertThat(element0).extracting("removed").contains(true);
        assertThat(element1).extracting("removed").contains(false);
        assertThat(element2).extracting("removed").contains(true);
        assertThat(element3).extracting("removed").contains(true);
        assertThat(element4).extracting("removed").contains(true);
        assertThat(element5).extracting("removed").contains(true);
    }

    @Test
    public void testReducer()
    {
        final MinReducer<String, String> subject = new MinReducer<>(str -> str, false);

        final Element<String> element0 = new Element<>(new MemoryReference<>(null));
        final Element<String> element1 = new Element<>(new MemoryReference<>("element1"));
        final Element<String> element2 = new Element<>(new MemoryReference<>("element2"));

        subject.reduce("key", Arrays.asList(element0, element1, element2));

        assertThat(element0).extracting("removed").contains(false);
        assertThat(element1).extracting("removed").contains(true);
        assertThat(element2).extracting("removed").contains(true);
    }
}
