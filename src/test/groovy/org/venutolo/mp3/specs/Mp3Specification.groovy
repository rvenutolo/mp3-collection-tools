package org.venutolo.mp3.specs

import static org.venutolo.mp3.core.Constants.FOUR_DIGITS
import static org.venutolo.mp3.core.Constants.POSITIVE_INTEGER
import static org.venutolo.mp3.core.Constants.TARGET_PIXELS
import static org.venutolo.mp3.core.Id3v2Tag.Version

import javax.annotation.Nonnull
import org.venutolo.mp3.core.CoreTypesFactory
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v1Tag
import org.venutolo.mp3.core.Id3v2Tag
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.core.impl.jaudiotagger.JAudioTaggerCoreTypesFactory
import spock.lang.Specification

class Mp3Specification extends Specification {

    private static final CoreTypesFactory CORE_TYPES_FACTORY = new JAudioTaggerCoreTypesFactory()

    // TODO make sure all tests work regardless of value
    protected static final int NUM_MP3_FILES = 4
    protected static final File RESOURCE_DIR = new File('src/test/resources')
    protected static final File JPG_FILE = new File(RESOURCE_DIR, "${TARGET_PIXELS}x${TARGET_PIXELS}.jpg")
    protected static final File PNG_FILE = new File(RESOURCE_DIR, "${TARGET_PIXELS}x${TARGET_PIXELS}.png")

    protected Output mockOutput = Mock(Output)
    protected Mp3File mp3File = newMp3File()
    protected Collection<Mp3File> mp3Files = (1..NUM_MP3_FILES).collect { newMp3File() }

    @Nonnull
    protected static Mp3File newMp3File() {
        CORE_TYPES_FACTORY.newMp3File(new File(RESOURCE_DIR, 'test.mp3'))
    }

    @Nonnull
    protected static Id3v1Tag newId3v1Tag() {
        CORE_TYPES_FACTORY.newId3v1Tag()
    }

    @Nonnull
    protected static Id3v2Tag newId3v2Tag() {
        CORE_TYPES_FACTORY.newId3v2Tag()
    }

    @Nonnull
    protected static Id3v2Tag newId3v2Tag(@Nonnull final Version version) {
        CORE_TYPES_FACTORY.newId3v2Tag(version)
    }

    @Nonnull
    protected static String fieldVal(@Nonnull final Field field) {
        if (!field.pattern) {
            return field.toString().toLowerCase()
        }
        if (field.pattern == FOUR_DIGITS) {
            return '2000'
        }
        if (field.pattern == POSITIVE_INTEGER) {
            return '1'
        }
        throw new IllegalStateException("expected pattern: ${field.pattern}")
    }

    @Nonnull
    protected static String fieldVal(@Nonnull final Field field, final int idx) {
        if (!field.pattern) {
            return "${field.toString().toLowerCase()}_${idx + 1}"
        }
        if (field.pattern == FOUR_DIGITS) {
            return (idx + 2000) as String
        }
        if (field.pattern == POSITIVE_INTEGER) {
            return (idx + 1) as String
        }
        throw new IllegalStateException("expected pattern: ${field.pattern}")
    }

}
