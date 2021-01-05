package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.fields.Field.GENRE
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.CheckSpecification

class RequiredFieldsCheckSpec extends CheckSpecification {

    private def checker = new RequiredFieldsCheck(mockOutput)

    def "NPE when output is null"() {

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

    def "No output when MP3 file doesn't have tags"() {

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
        ID3_FIELDS.each { field -> tag.setField(field.key, '1') }
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when missing #requiredField"() {

        setup:
        def tag = new ID3v24Tag()
        REQUIRED_FIELDS
            .findAll { field -> field != requiredField }
            .each { field -> tag.setField(field.key, '1') }
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getAll(requiredField.key).empty

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Missing field: ${requiredField.desc}")
        0 * mockOutput._

        where:
        requiredField << REQUIRED_FIELDS

    }

    def "No output when all required fields are populated"() {

        setup:
        def tag = new ID3v24Tag()
        // use '1' as value for all fields to fit both string and numeric fields
        REQUIRED_FIELDS.each { field -> tag.setField(field.key, '1') }
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        REQUIRED_FIELDS.each { field -> assert mp3File.getID3v2Tag().getFirst(field.key) }

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when multiple #requiredField"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, '1')
        tag.setField(TRACK_TOTAL.key, '1')
        // when setting genre to a numeric string, it will be converted to a desc string (ex: '1' -> 'Classic Rock')
        def fieldValue = requiredField == GENRE ? 'genre1' : '1'
        def extraFieldValue = requiredField == GENRE ? 'genre2' : '2'
        REQUIRED_FIELDS.each { field -> tag.setField(field.key, fieldValue) }
        tag.addField(requiredField.key, extraFieldValue)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        REQUIRED_FIELDS.every { field -> assert mp3File.getID3v2Tag().getFirst(field.key) }
        assert mp3File.getID3v2Tag().getAll(requiredField.key).size() == 2

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(
            mp3File, "Multiple values for field: ${requiredField.desc}", "${fieldValue}, ${extraFieldValue}"
        )
        0 * mockOutput._

        where:
        // track and track total do not support multiple fields
        requiredField << REQUIRED_FIELDS.findAll { field -> field != TRACK && field != TRACK_TOTAL }

    }

}
