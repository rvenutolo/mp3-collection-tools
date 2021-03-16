package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.EXTRANEOUS_FIELDS
import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.core.Field.COMMENT
import static org.venutolo.mp3.core.Field.COVER_ART

import org.venutolo.mp3.specs.Mp3Specification

class ExtraneousFieldsCheckSpec extends Mp3Specification {

    private def checker = new ExtraneousFieldsCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new ExtraneousFieldsCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when MP3 file is null"() {

        when:
        checker.check(null)

        then:
        thrown(NullPointerException)

    }


    def "Does nothing when MP3 file doesn't have tags"() {

        setup:
        assert !mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = newId3v1Tag()
        mp3File.setId3v1Tag(tag)

        and:
        assert COMMENT in EXTRANEOUS_FIELDS
        assert mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when there are no fields populated"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when required field #field is populated"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(field, fieldVal(field))
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(field) == fieldVal(field)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        field << REQUIRED_FIELDS

    }

    def "Output when extraneous field #field is populated"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(field, fieldVal(field))
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(field) == fieldVal(field)

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Extraneous field: ${field}")
        0 * mockOutput._

        where:
        field << EXTRANEOUS_FIELDS.findAll { field -> field != COVER_ART }

    }

    def "Output when cover art is populated"() {

        setup:
        def tag = newId3v2Tag()
        tag.setArtwork(jpgFile)
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().hasArtwork()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Extraneous field: ${COVER_ART}")
        0 * mockOutput._

    }

}
