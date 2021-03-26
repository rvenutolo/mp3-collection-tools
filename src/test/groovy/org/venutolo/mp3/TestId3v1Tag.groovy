package org.venutolo.mp3

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v1Tag

class TestId3v1Tag implements Id3v1Tag {

    private final Map<Field, String> fieldValues

    TestId3v1Tag(@Nonnull final Map<Field, String> fieldValues = [:]) {
        this.fieldValues = new HashMap(fieldValues)
    }

    @Override
    @Nonnull
    String get(@Nonnull final Field field) {
        fieldValues[field] ?: ''
    }

    @Override
    void set(@Nonnull final Field field, @Nonnull final String value) {
        fieldValues[field] = value
    }

    @Override
    void delete(@Nonnull final Field field) {
        fieldValues.remove(field)
    }

}
