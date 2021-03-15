package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS

import org.venutolo.mp3.core.ID3v1Tag
import org.venutolo.mp3.core.ID3v2Tag
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

        and:
        assert mp3File.hasID3v1Tag()
        REQUIRED_FIELDS.each { field ->
            assert !mp3File.getID3v1Tag().has(field)
        }
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when missing #missingField"() {

        setup:
        def tag = new ID3v2Tag()
        REQUIRED_FIELDS
            .findAll { field ->
                field != missingField
            }
            .each { field ->
                tag.set(field, fieldVal(field))
            }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().has(missingField)
        REQUIRED_FIELDS
            .findAll { field ->
                field != missingField
            }
            .each { field ->
                assert tag.get(field) == fieldVal(field)
            }

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Missing field: ${missingField}")
        0 * mockOutput._

        where:
        missingField << REQUIRED_FIELDS

    }

    def "No output when all required fields are populated"() {

        setup:
        def tag = new ID3v2Tag()
        REQUIRED_FIELDS.each { field ->
            tag.set(field, fieldVal(field))
        }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        REQUIRED_FIELDS.each { field ->
            assert mp3File.getID3v2Tag().get(field) == fieldVal(field)
        }

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

}
