package org.venutolo.mp3.check

import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import static org.venutolo.mp3.fields.Field.COVER_ART
import static org.venutolo.mp3.fields.Field.EXTRANEOUS_FIELDS
import static org.venutolo.mp3.fields.Field.REQUIRED_FIELDS

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class ExtraneousFieldsCheckSpec extends CheckSpecification {

    private def checker = new ExtraneousFieldsCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

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
        0 * mockWarnings._

    }

    def "No warning when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = new ID3v1Tag()
        ID3_FIELDS.each { tag.setField(it.key, '1') }
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when there are no fields populated"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when required field #field is populated"() {

        setup:
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.setField(field.key, '1')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2TagAsv24().getFirst(field.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

        where:
        field << REQUIRED_FIELDS

    }

    def "Warning when extraneous field #field is populated"() {

        setup:
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        tag.setField(field.key, '1')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2TagAsv24().getFirst(field.key)

        when:
        checker.check(mp3File)

        then:
        1 * mockWarnings.write(mp3File, "Extraneous field: ${field.desc}")

        where:
        field << EXTRANEOUS_FIELDS.findAll { it != COVER_ART }

    }

    def "Warning when cover art is populated"() {

        setup:
        def tag = new ID3v24Tag()
        tag.addField(createArtworkFromFile(artFile))
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2TagAsv24().getFirstArtwork()

        when:
        checker.check(mp3File)

        then:
        1 * mockWarnings.write(mp3File, "Extraneous field: ${COVER_ART.desc}")

    }

}
