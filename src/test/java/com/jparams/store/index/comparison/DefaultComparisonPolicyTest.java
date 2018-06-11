package com.jparams.store.index.comparison;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class DefaultComparisonPolicyTest
{
    private DefaultComparisonPolicy<String> subject;

    @Before
    public void setUp()
    {
        subject = new DefaultComparisonPolicy<>();
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(String.class)).isTrue();
        assertThat(subject.supports(Object.class)).isTrue();
    }

    @Test
    public void testGetComparable()
    {
        assertThat(subject.createComparable("aBcd")).isEqualTo("aBcd");
    }
}