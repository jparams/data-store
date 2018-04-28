package com.jparams.store;

import java.time.LocalDate;

import com.jparams.store.model.Person;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryStoreTest
{
    private MemoryStore<Person> subject;
    private Index<Person> firstNameIndex;
    private Person person1;
    private Person person2;
    private Person person3;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        firstNameIndex = subject.addIndex("firstName", (person) -> Keys.create(person.getFirstName()));
        person1 = new Person("John", "Smith", LocalDate.of(1990, 10, 1));
        subject.add(person1);
        person2 = new Person("James", "Johnson", LocalDate.of(1995, 5, 10));
        subject.add(person2);
        person3 = new Person("James", "Johnson", LocalDate.of(1982, 2, 19));
        subject.add(person3);
    }

    @Test
    public void testGetSize()
    {
        assertThat(subject.size()).isEqualTo(3);
    }

    @Test
    public void testGetAll()
    {
        assertThat(subject).containsExactly(person1, person2, person3);
    }

    @Test
    public void testGetIndex()
    {
        assertThat(subject.getIndex("firstName")).isSameAs(firstNameIndex);
        assertThat(subject.getIndex("lastName")).isNull();
    }

    @Test
    public void testGetIndexedData()
    {
        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testReindex()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.reindex();

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);
        assertThat(firstNameIndex.get("Richard")).containsExactly(person2);
        assertThat(firstNameIndex.get("James")).containsExactly(person3);
    }
}