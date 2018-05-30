package com.jparams.store;

import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnmodifiableIteratorTest
{
    private UnmodifiableIterator<String> subject;

    /**
     * set up
     */
    @Before
    public void setUp()
    {
        subject = new UnmodifiableIterator<>(Collections.singletonList("abc").iterator());
    }

    @Test
    public void testIterator()
    {
        assertThat(subject.hasNext()).isTrue();
        assertThat(subject.next()).isEqualTo("abc");
        assertThatThrownBy(() -> subject.remove()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(subject.hasNext()).isFalse();
        assertThatThrownBy(() -> subject.next()).isInstanceOf(NoSuchElementException.class);
    }
}