package com.jparams.store;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;

import com.jparams.store.model.Person;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MemoryStoreTest
{
    private Store<Person> subject;
    private Index<Person> firstNameIndex;
    private Person person1;
    private Person person2;
    private Person person3;
    private boolean throwException = false;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        firstNameIndex = subject.addIndex("firstName", (person) -> {
            if (throwException)
            {
                throw new RuntimeException("error indexing " + person.getFirstName());
            }

            return Keys.create(person.getFirstName());
        });
        person1 = new Person("John", "Smith", LocalDate.of(1990, 10, 1));
        subject.add(person1);
        person2 = new Person("James", "Johnson", LocalDate.of(1995, 5, 10));
        subject.add(person2);
        person3 = new Person("James", "Johnson", LocalDate.of(1982, 2, 19));
        subject.add(person3);
    }

    @Test
    public void testAddIndex()
    {
        final Index<Person> lastNameIndex = subject.addIndex((person) -> Keys.create(person.getLastName()));
        assertThat(lastNameIndex.getFirst("Smith")).isSameAs(person1);
        assertThat(lastNameIndex.get("Johnson")).containsExactly(person2, person3);
        assertThat(lastNameIndex.get("Random")).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIndex()
    {
        assertThat(subject.addIndex(firstNameIndex.getName(), (obj) -> null));
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
        assertThat(subject.getIndex("firstName")).isEqualTo(firstNameIndex);
        assertThat(subject.getIndex("lastName")).isNull();
    }

    @Test
    public void testFindIndex()
    {
        assertThat(subject.findIndex("firstName")).hasValue(firstNameIndex);
        assertThat(subject.findIndex("lastName")).isEmpty();
    }

    @Test
    public void testRemoveIndex()
    {
        assertThat(subject.removeIndex(firstNameIndex));
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testRemoveIndexHandlesInvalidIndex()
    {
        assertThat(subject.removeIndex(new ReferenceIndex<>(firstNameIndex.getName(), null)));
        assertThat(subject.getIndexes()).isNotEmpty();
    }

    @Test
    public void testRemoveIndexByName()
    {
        assertThat(subject.removeIndex(firstNameIndex.getName()));
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testGetIndexes()
    {
        assertThat(subject.getIndexes()).containsExactly(firstNameIndex);
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

    /**
     * testReindexHandlesExceptions
     */
    @Test
    public void testReindexHandlesExceptions()
    {
        throwException = true;

        try
        {
            subject.reindex();
            fail("expected exceptions");
        }
        catch (final IndexException e)
        {
            assertThat(e.getSuppressed()).hasSize(3);
        }

        // test original data remains unchanged
        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testClear()
    {
        subject.clear();
        assertThat(subject.getIndex("firstName").getFirst("John")).isNull();
        assertThat(subject).isEmpty();
    }

    @Test
    public void testAddAll()
    {
        subject.clear();
        subject.addAll(Arrays.asList(person1, person2, person3));
        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testAddReIndexesIfItemAlreadyInStore()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.add(person1);

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);

        // person 2 isn't re-indexed
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
    }

    @Test
    public void testReindexSingleItem()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.reindex(person1);

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);

        // person 2 isn't re-indexed
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
    }

    @Test
    public void testRemove()
    {
        assertThat(subject.remove(person1)).isTrue();
        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(subject).hasSize(2);
    }

    @Test
    public void testIterator()
    {
        final Iterator<Person> iterator = subject.iterator();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person2);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person3);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testRemoveUsingIterator()
    {
        final Iterator<Person> iterator = subject.iterator();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();

        assertThat(subject).hasSize(1);
        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.get("James")).containsExactly(person3);
    }

    @Test
    public void testRemoveItemThatDoesNotExist()
    {
        assertThat(subject.remove(new Person("", "", null))).isFalse();
    }

    @Test
    public void testContains()
    {
        assertThat(subject.contains(person1)).isTrue();
        assertThat(subject.contains(new Person("", "", null))).isFalse();
    }

    @Test
    public void testContainsAll()
    {
        assertThat(subject.containsAll(Arrays.asList(person1, person2))).isTrue();
        assertThat(subject.containsAll(Arrays.asList(new Person("", "", null), person3))).isFalse();
    }

    @Test
    public void testRemoveAllIndexes()
    {
        subject.removeAllIndexes();
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testCreateCopy()
    {
        final Store<Person> copy = subject.copy();
        assertThat(copy).isNotSameAs(subject);

        final Index<Person> copyIndex = copy.getIndex(firstNameIndex.getName());
        assertThat(copyIndex).isNotSameAs(firstNameIndex);

        final Person newPerson = new Person("Jim", "Jaf", null);
        copy.add(newPerson);

        assertThat(firstNameIndex.getFirst("Jim")).isNull();
        assertThat(copyIndex.getFirst("Jim")).isEqualTo(newPerson);

        assertThat(subject).hasSize(3);
        assertThat(copy).hasSize(4);

        assertThat(copyIndex.getFirst("John")).isSameAs(person1);
        assertThat(copyIndex.get("James")).containsExactly(person2, person3);
        assertThat(copyIndex.get("Random")).isEmpty();
    }

    @Test
    public void testUnmodifiableStore()
    {
        final Store<Person> unmodifiable = subject.unmodifiableStore();
        assertThat(unmodifiable).isNotSameAs(subject);

        assertThat(unmodifiable.getIndex("firstName").getFirst("John")).isSameAs(person1);
        assertThat(unmodifiable.getIndex("firstName").get("James")).containsExactly(person2, person3);
        assertThat(unmodifiable.getIndex("firstName").get("Random")).isEmpty();

        final Person newPerson = new Person("Jim", "Jaf", null);
        subject.add(newPerson);

        assertThat(unmodifiable.getIndex("firstName").getFirst("Jim")).isEqualTo(newPerson);

        try
        {
            unmodifiable.add(new Person("Jafy", "Jim", null));
            fail("expected UnsupportedOperationException");
        }
        catch (final UnsupportedOperationException e)
        {
            // expected
        }
    }

    @Test
    public void testSynchronizedStore()
    {
        final Store<Person> synchronizedStore = subject.synchronizedStore();
        assertThat(synchronizedStore).isExactlyInstanceOf(SynchronizedStore.class);
        assertThat(synchronizedStore.getIndex("firstName")).isExactlyInstanceOf(SynchronizedIndex.class);
        assertThat(((SynchronizedStore<?>) synchronizedStore).getStore()).isSameAs(subject);
    }

    @Test
    public void testSynchronizeSynchronizedStore()
    {
        final Store<Person> synchronizedStore = subject.synchronizedStore().synchronizedStore();
        assertThat(synchronizedStore).isExactlyInstanceOf(SynchronizedStore.class);
        assertThat(synchronizedStore.getIndex("firstName")).isExactlyInstanceOf(SynchronizedIndex.class);
        assertThat(((SynchronizedStore<?>) synchronizedStore).getStore()).isSameAs(subject);
    }
}