package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class RequiredFieldsCheckSpec extends Mp3Specification {

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
        mp3File.setID3v1Tag(new ID3v1Tag())
        assert mp3File.hasID3v1Tag()
        REQUIRED_FIELDS.each { field ->
            assert !mp3File.getID3v1Tag().getFirst(field.key)
        }
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when missing #missingField"() {

        setup:
        def tag = new ID3v24Tag()
        REQUIRED_FIELDS
            .findAll { field ->
                field != missingField
            }
            .each { field ->
                tag.setField(field.key, fieldVal(field))
            }
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(missingField.key)
        REQUIRED_FIELDS
            .findAll { field ->
                field != missingField
            }
            .each { field ->
                assert tag.getFirst(field.key) == fieldVal(field)
            }

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Missing field: ${missingField.desc}")
        0 * mockOutput._

        where:
        missingField << REQUIRED_FIELDS

    }

    def "No output when all required fields are populated"() {

        setup:
        def tag = new ID3v24Tag()
        REQUIRED_FIELDS.each { field ->
            tag.setField(field.key, fieldVal(field))
        }
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        REQUIRED_FIELDS.each { field ->
            assert mp3File.getID3v2Tag().getFirst(field.key) == fieldVal(field)
        }

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when multiple #multipleField"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, fieldVal(TRACK, 1))
        tag.setField(TRACK_TOTAL.key, fieldVal(TRACK_TOTAL, 1))
        // when setting genre to a numeric string, it will be converted to a desc string (ex: '1' -> 'Classic Rock')
        def fieldValue = fieldVal(multipleField, 1)
        def extraFieldValue = fieldVal(multipleField, 2)
        REQUIRED_FIELDS.each { field ->
            tag.setField(field.key, fieldValue)
        }
        tag.addField(multipleField.key, extraFieldValue)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        REQUIRED_FIELDS.every { field ->
            assert mp3File.getID3v2Tag().getFirst(field.key) == fieldValue
        }
        assert mp3File.getID3v2Tag().getAll(multipleField.key).toSet() == [fieldValue, extraFieldValue] as Set

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(
            mp3File, "Multiple values for field: ${multipleField.desc}", "${fieldValue}, ${extraFieldValue}"
        )
        0 * mockOutput._

        where:
        // track and track total do not support multiple fields
        multipleField << REQUIRED_FIELDS.findAll { field -> field != TRACK && field != TRACK_TOTAL }

    }

}
