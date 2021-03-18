package org.venutolo.mp3.specs

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

abstract class Mp3Specification extends Specification {

    private static final CoreTypesFactory CORE_TYPES_FACTORY = new JAudioTaggerCoreTypesFactory()

    // TODO make sure all tests work regardless of value
    protected static final int NUM_MP3_FILES = 4
    protected static final File RESOURCE_DIR = new File('src/test/resources')

    protected def mockOutput = Mock(Output)
    protected def mp3File = newMp3File()
    protected def mp3Files = (0..(NUM_MP3_FILES - 1)).collect { idx -> newMp3File() }
    protected def dir = RESOURCE_DIR
    protected def jpgFile = new File("${RESOURCE_DIR}/${TARGET_PIXELS}x${TARGET_PIXELS}.jpg")
    protected def pngFile = new File("${RESOURCE_DIR}/${TARGET_PIXELS}x${TARGET_PIXELS}.png")

    protected static Mp3File newMp3File() {
        CORE_TYPES_FACTORY.newMp3File(new File("${RESOURCE_DIR}/test.mp3"))
    }

    protected static Id3v1Tag newId3v1Tag() {
        CORE_TYPES_FACTORY.newId3v1Tag()
    }

    protected static Id3v2Tag newId3v2Tag() {
        CORE_TYPES_FACTORY.newId3v2Tag()
    }

    protected static Id3v2Tag newId3v2Tag(Version version) {
        CORE_TYPES_FACTORY.newId3v2Tag(version)
    }

    protected static String fieldVal(@Nonnull final Field field) {
        field.isNumeric ? '1' : "${field.toString().toLowerCase()}"
    }

    protected static String fieldVal(@Nonnull final Field field, final int idx) {
        field.isNumeric ? (idx + 1) as String : "${field.toString().toLowerCase()}_${idx + 1}"
    }

}
