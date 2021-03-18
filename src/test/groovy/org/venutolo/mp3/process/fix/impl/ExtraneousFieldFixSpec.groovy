package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.EXTRANEOUS_FIELDS
import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.core.Field.COVER_ART

import org.venutolo.mp3.specs.Mp3Specification

class ExtraneousFieldFixSpec extends Mp3Specification {

    private ExtraneousFieldFix fixer = new ExtraneousFieldFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new ExtraneousFieldFix(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when MP3 file is null"() {

        when:
        fixer.fix(null)

        then:
        thrown(NullPointerException)

    }

    def "No output and returns false when MP3 file doesn't have tags"() {

        setup:
        assert !mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        mp3File.setId3v1Tag(newId3v1Tag())

        and:
        assert mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when there are no fields populated"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when required field #field is populated"() {

        setup:
        def tag = newId3v2Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.set(field, '1')
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(field)

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

        where:
        field << REQUIRED_FIELDS

    }

    def "Output, returns true, and removes field when extraneous field #field is populated"() {

        setup:
        def tag = newId3v2Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.set(field, '1')
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().has(field)

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Removed: ${field}")
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.getId3v2Tag().has(field)

        where:
        field << EXTRANEOUS_FIELDS.findAll { field -> field != COVER_ART }

    }

    def "Output, returns true, and removes field when cover art is populated"() {

        setup:
        def tag = newId3v2Tag()
        tag.setArtwork(jpgFile)
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().hasArtwork()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Removed: ${COVER_ART}")
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.getId3v2Tag().hasArtwork()

    }

}
