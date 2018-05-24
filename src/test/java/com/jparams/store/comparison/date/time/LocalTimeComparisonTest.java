package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LocalTimeComparisonTest
{
    private LocalTimeComparison subject;

    @Before
    public void setUp()
    {
        subject = new LocalTimeComparison();
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(LocalDateTime.class)).isTrue();
        assertThat(subject.supports(Object.class)).isFalse();
    }

    @Test
    public void testGetComparable()
    {
        assertThat(subject.createComparable(LocalDateTime.of(2018, 5, 5, 12, 0, 1))).isEqualTo(LocalTime.of(12, 0, 1));
    }
}