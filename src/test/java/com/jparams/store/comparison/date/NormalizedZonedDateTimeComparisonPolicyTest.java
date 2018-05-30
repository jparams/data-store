package com.jparams.store.comparison.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NormalizedZonedDateTimeComparisonPolicyTest
{
    private NormalizedZonedDateTimeComparisonPolicy subject;

    @Before
    public void setUp()
    {
        subject = new NormalizedZonedDateTimeComparisonPolicy();
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
        final ZonedDateTime zonedDateTime = LocalDateTime.of(2018, 5, 5, 13, 55, 30).atZone(ZoneId.of("Europe/London"));
        assertThat(subject.createComparable(zonedDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 5, 12, 55, 30));
    }
}