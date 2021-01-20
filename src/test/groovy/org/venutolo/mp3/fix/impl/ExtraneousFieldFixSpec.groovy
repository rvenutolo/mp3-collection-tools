package org.venutolo.mp3.fix.impl

import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import static org.venutolo.mp3.Constants.EXTRANEOUS_FIELDS
import static org.venutolo.mp3.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.fields.Field.COVER_ART

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class ExtraneousFieldFixSpec extends Mp3Specification {

    private def fixer = new ExtraneousFieldFix(mockOutput)

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
        assert !mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        mp3File.setID3v1Tag(new ID3v1Tag())

        and:
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when there are no fields populated"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when required field #field is populated"() {

        setup:
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.setField(field.key, '1')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(field.key)

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
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.setField(field.key, '1')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(field.key)

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Removed: ${field.desc}")
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.getID3v2Tag().getFirst(field.key)

        where:
        field << EXTRANEOUS_FIELDS.findAll { field -> field != COVER_ART }

    }

    def "Output, returns true, and removes field when cover art is populated"() {

        setup:
        def tag = new ID3v24Tag()
        tag.addField(createArtworkFromFile(jpgFile))
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirstArtwork()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Removed: ${COVER_ART.desc}")
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.getID3v2Tag().getFirstArtwork()

    }

}
