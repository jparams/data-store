package com.jparams.store.comparison.date;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NormalizedOffsetDateTimeComparisonPolicyTest
{
    private NormalizedOffsetDateTimeComparisonPolicy subject;

    @Before
    public void setUp()
    {
        subject = new NormalizedOffsetDateTimeComparisonPolicy();
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
        final OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.of(2018, 5, 5, 13, 55, 30), ZoneOffset.ofHours(2));
        assertThat(subject.createComparable(offsetDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 5, 11, 55, 30));
    }
}