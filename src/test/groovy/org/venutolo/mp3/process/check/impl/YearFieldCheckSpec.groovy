package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.YEAR

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
        assert !mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = newId3v1Tag()
        tag.set(YEAR, '2020-01-01')
        mp3File.setId3v1Tag(tag)

        and:
        assert mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when no year"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v2Tag()
        assert !mp3File.getId3v2Tag().has(YEAR)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when year is #desc"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(YEAR, yearVal)
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(YEAR) == yearVal

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "${YEAR.toString().capitalize()} not in #### format", yearVal)
        0 * mockOutput._

        where:
        desc           | yearVal
        'single digit' | '1'
        'double digit' | '12'
        'with date'    | '2020-12-31'

    }

    def "No output when year is expected format"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(YEAR, '2020')
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(YEAR) == '2020'

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

}
