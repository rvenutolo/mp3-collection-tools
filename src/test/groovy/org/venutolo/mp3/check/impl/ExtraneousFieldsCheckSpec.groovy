package org.venutolo.mp3.check.impl

import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import static org.venutolo.mp3.Constants.EXTRANEOUS_FIELDS
import static org.venutolo.mp3.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.fields.Field.COMMENT
import static org.venutolo.mp3.fields.Field.COVER_ART

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
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
        assert !mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = new ID3v1Tag()
        tag.setComment('comment')
        mp3File.setID3v1Tag(tag)

        and:
        assert COMMENT in EXTRANEOUS_FIELDS
        assert mp3File.hasID3v1Tag()
        assert mp3File.getID3v1Tag().getFirst(COMMENT.key) == 'comment'
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when there are no fields populated"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when required field #field is populated"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(field.key, fieldVal(field))
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(field.key) == fieldVal(field)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        field << REQUIRED_FIELDS

    }

    def "Output when extraneous field #field is populated"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(field.key, fieldVal(field))
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(field.key) == fieldVal(field)

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Extraneous field: ${field.desc}")
        0 * mockOutput._

        where:
        field << EXTRANEOUS_FIELDS.findAll { field -> field != COVER_ART }

    }

    def "Output when cover art is populated"() {

        setup:
        def tag = new ID3v24Tag()
        tag.addField(createArtworkFromFile(jpgFile))
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirstArtwork()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Extraneous field: ${COVER_ART.desc}")
        0 * mockOutput._

    }

}
