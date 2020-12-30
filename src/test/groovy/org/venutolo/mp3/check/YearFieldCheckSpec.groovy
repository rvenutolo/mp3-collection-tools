package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.YEAR

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class YearFieldCheckSpec extends CheckSpecification {

    private def checker = new YearFieldCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

        when:
        new YearFieldCheck(null)

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
        tag.setYear('2000')
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when no year"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(YEAR.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "Warning when year is #desc"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, yearVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == yearVal

        when:
        checker.check(mp3File)

        then:
        1 * mockWarnings.write(mp3File, 'Year not in #### format', yearVal)

        where:
        desc           | yearVal
        'single digit' | '1'
        'double digit' | '12'
        'with date'    | '2020-12-31'

    }

    def "No warning when year is expected format"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, '2020')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == '2020'

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

}
