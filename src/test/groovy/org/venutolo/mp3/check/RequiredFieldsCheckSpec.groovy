package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.REQUIRED_FIELDS

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class RequiredFieldsCheckSpec extends CheckSpecification {

    private def checker = new RequiredFieldsCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

        when:
        new RequiredFieldsCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when MP3 file is null"() {

        when:
        checker.check(null)

        then:
        thrown(NullPointerException)

    }

    def "No warning when MP3 file doesn't have tags"() {

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

    def "Warning when missing #field"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getAll(field.key).empty

        when:
        checker.check(mp3File)

        then:
        1 * mockWarnings.write(mp3File, "Missing field: ${field.desc}")

        where:
        field << REQUIRED_FIELDS

    }

    def "No warning when all required fields are populated"() {

        setup:
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        REQUIRED_FIELDS.each { tag.setField(it.key, '1') }
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert REQUIRED_FIELDS.every { mp3File.getID3v2Tag().getFirst(it.key) }

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

}
