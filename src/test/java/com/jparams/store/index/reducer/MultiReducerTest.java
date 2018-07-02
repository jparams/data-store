package com.jparams.store.index.reducer;

import java.util.Arrays;

import com.jparams.store.index.Element;
import com.jparams.store.memory.MemoryReference;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiReducerTest
{
    @Test
    public void testReducer()
    {
        final FilteredReducer<String, String> reducer1 = new FilteredReducer<>(obj -> obj.equals("a"));
        final FilteredReducer<String, String> reducer2 = new FilteredReducer<>(obj -> obj.equals("b"));
        final MultiReducer<String, String> subject = new MultiReducer<>(Arrays.asList(reducer1, reducer2));

        final Element<String> element0 = new Element<>(new MemoryReference<>("a"));
        final Element<String> element1 = new Element<>(new MemoryReference<>("b"));
        final Element<String> element2 = new Element<>(new MemoryReference<>("c"));
        final Element<String> element3 = new Element<>(new MemoryReference<>("d"));

        subject.reduce("key", Arrays.asList(element0, element1, element2, element3));

        assertThat(element0).extracting("removed").contains(true);
        assertThat(element1).extracting("removed").contains(true);
        assertThat(element2).extracting("removed").contains(false);
        assertThat(element3).extracting("removed").contains(false);

        assertThat(subject.getReducers()).containsExactly(reducer1, reducer2);
    }

    @Test
    public void testAndThen()
    {
        final FilteredReducer<String, String> reducer1 = new FilteredReducer<>(obj -> obj.equals("a"));
        final FilteredReducer<String, String> reducer2 = new FilteredReducer<>(obj -> obj.equals("b"));

        final MultiReducer<String, String> subject = new MultiReducer<>(Arrays.asList(reducer1, reducer2));

        final FilteredReducer<String, String> reducer3 = new FilteredReducer<>(obj -> obj.equals("c"));

        final MultiReducer<String, String> multiReducer = subject.andThen(reducer3);

        assertThat(multiReducer).isNotSameAs(subject);
        assertThat(multiReducer.getReducers()).containsExactly(reducer1, reducer2, reducer3);
    }
}