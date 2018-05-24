package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;

import com.jparams.store.comparison.date.TruncateDateTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TruncatedLocalDateTimeComparisonTest
{
    private TruncatedLocalDateTimeComparison subject;

    @Before
    public void setUp()
    {
        subject = new TruncatedLocalDateTimeComparison(TruncateDateTime.DAY);
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
        final LocalDateTime localDateTime = LocalDateTime.of(2018, 5, 7, 12, 5, 15, 250);
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.SECOND).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 7, 12, 5, 15, 0));
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.MINUTE).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 7, 12, 5, 0, 0));
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.HOUR).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 7, 12, 0, 0, 0));
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.DAY).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 7, 0, 0, 0, 0));
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.MONTH).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 5, 1, 0, 0, 0, 0));
        assertThat(new TruncatedLocalDateTimeComparison(TruncateDateTime.YEAR).createComparable(localDateTime)).isEqualTo(LocalDateTime.of(2018, 1, 1, 0, 0, 0, 0));
    }
}