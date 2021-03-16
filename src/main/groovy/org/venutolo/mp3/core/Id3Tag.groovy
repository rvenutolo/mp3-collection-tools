package org.venutolo.mp3.core

import static java.util.Objects.hash

import javax.annotation.Nonnull

interface Id3Tag {

    String getTagType()

    default boolean has(@Nonnull final Field field) {
        get(field)
    }

    @Nonnull
    String get(@Nonnull final Field field)

    void set(@Nonnull final Field field, @Nonnull final String value)

    void delete(@Nonnull final Field field)

    default SortedMap<Field, String> populatedFields() {
        Field.values()
            .findAll { field -> has(field) }
            .collectEntries { field -> [(field): get(field)] }
            .sort() as SortedMap<Field, String>
    }

    @Override
    default String toString() {
        def fieldInfo = populatedFields()
            .collect { field, value -> "${field}: ${value}" }
            .join(', ')
        "${getTagType()} tag [${fieldInfo}]"
    }

    @Override
    default boolean equals(@Nonnull final Object o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }
        def that = (Id3Tag) o
        if (this.populatedFields() != that.populatedFields()) {
            return false
        }
        return true
    }

    @Override
    default int hashCode() {
        hash(populatedFields())
    }

}