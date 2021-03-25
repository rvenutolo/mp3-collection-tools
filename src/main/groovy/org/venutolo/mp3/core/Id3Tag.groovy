package org.venutolo.mp3.core

import javax.annotation.Nonnull

interface Id3Tag {

    String getTagType()

    @Nonnull
    String get(@Nonnull final Field field)

    void set(@Nonnull final Field field, @Nonnull final String value)

    default boolean has(@Nonnull final Field field) {
        get(field)
    }

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

}