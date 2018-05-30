package com.jparams.store.comparison.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalingComparisonPolicyTest
{
    private ScalingComparisonPolicy subject;

    @Before
    public void setUp()
    {
        subject = new ScalingComparisonPolicy(2, RoundingMode.UP);
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(BigDecimal.class)).isTrue();
        assertThat(subject.supports(Float.class)).isTrue();
        assertThat(subject.supports(Integer.class)).isTrue();
        assertThat(subject.supports(String.class)).isFalse();
    }

    @Test
    public void testGetComparable()
    {
        assertThat(subject.createComparable(1)).isEqualTo(new BigDecimal("1.00"));
        assertThat(subject.createComparable(1.523)).isEqualTo(new BigDecimal("1.53"));
        assertThat(subject.createComparable(new BigDecimal(1.523))).isEqualTo(new BigDecimal("1.53"));
    }
}