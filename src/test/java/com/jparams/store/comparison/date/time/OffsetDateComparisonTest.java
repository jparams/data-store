package com.jparams.store.comparison.date.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class OffsetDateComparisonTest
{
    private OffsetDateComparison subject;

    @Before
    public void setUp()
    {
        subject = new OffsetDateComparison();
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(OffsetDateTime.class)).isTrue();
        assertThat(subject.supports(Object.class)).isFalse();
    }

    @Test
    public void testGetComparable()
    {
        final OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.of(2018, 5, 5, 1, 0, 1), ZoneOffset.ofHours(2));
        assertThat(subject.createComparable(offsetDateTime)).isEqualTo(LocalDate.of(2018, 5, 4));
    }
}