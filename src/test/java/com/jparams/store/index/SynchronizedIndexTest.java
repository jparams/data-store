package com.jparams.store.index;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizedIndexTest
{
    private SynchronizedIndex<String> subject;

    @Mock
    private Index<String> mockIndex;

    /**
     * set up
     */
    @Before
    public void setUp()
    {
        subject = new SynchronizedIndex<>(mockIndex, new Object());
    }

    @Test
    public void testGetFirst()
    {
        when(mockIndex.getFirst(any())).thenReturn("first");

        final String first = subject.getFirst("abc");

        assertThat(first).isEqualTo("first");
        verify(mockIndex).getFirst("abc");
    }

    @Test
    public void testFindFirst()
    {
        when(mockIndex.findFirst(any())).thenReturn(Optional.of("first"));

        final Optional<String> first = subject.findFirst("abc");

        assertThat(first).hasValue("first");
        verify(mockIndex).findFirst("abc");
    }

    @Test
    public void testGet()
    {
        final List<String> list = Collections.singletonList("first");
        when(mockIndex.get(any())).thenReturn(list);
        assertThat(subject.get("abc")).isEqualTo(list);
        verify(mockIndex).get("abc");
    }

    @Test
    public void testGetName()
    {
        when(mockIndex.getName()).thenReturn("abc");
        assertThat(subject.getName()).isEqualTo("abc");
        verify(mockIndex).getName();
    }

    @Test
    public void testToString()
    {
        when(mockIndex.toString()).thenReturn("abc");
        assertThat(subject.toString()).isEqualTo("abc");
    }

    @Test
    public void equalsAndHashCodeContract()
    {
        EqualsVerifier.forClass(SynchronizedIndex.class).usingGetClass().verify();
    }
}