package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ZonedTimeComparisonTest
{
    private ZonedTimeComparison subject;

    @Before
    public void setUp()
    {
        subject = new ZonedTimeComparison();
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(ZonedDateTime.class)).isTrue();
        assertThat(subject.supports(Object.class)).isFalse();
    }

    @Test
    public void testGetComparable()
    {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2018, 5, 5, 12, 0, 1), ZoneId.of("Europe/London"));
        assertThat(subject.createComparable(zonedDateTime)).isEqualTo(LocalTime.of(11, 0, 1));
    }
}