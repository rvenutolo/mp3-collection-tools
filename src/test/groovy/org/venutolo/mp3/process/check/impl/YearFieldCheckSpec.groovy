package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Field.YEAR

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class YearFieldCheckSpec extends Mp3Specification {

    private def checker = new YearFieldCheck(mockOutput)

    def "NPE when output is null"() {

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
        tag.setYear('2020-01-01')
        mp3File.setID3v1Tag(tag)

        and:
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when no year"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(YEAR.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when year is #desc"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, yearVal)
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == yearVal

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "${YEAR.desc.capitalize()} not in #### format", yearVal)
        0 * mockOutput._

        where:
        desc           | yearVal
        'single digit' | '1'
        'double digit' | '12'
        'with date'    | '2020-12-31'

    }

    def "No output when year is expected format"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, '2020')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == '2020'

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

}
