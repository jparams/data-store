package com.jparams.store.comparison;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Test for {@link DefaultComparison}
 */
public class DefaultComparisonTest
{
    private DefaultComparison<String> subject;

    @Before
    public void setUp()
    {
        subject = new DefaultComparison<>();
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
        assertThat(subject.getComparable("aBcd")).isEqualTo("aBcd");
    }
}