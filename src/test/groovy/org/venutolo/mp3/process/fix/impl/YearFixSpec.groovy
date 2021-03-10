package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Field.YEAR

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class YearFixSpec extends Mp3Specification {

    private def fixer = new YearFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new YearFix(null)

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

    def "No output and returns false when MP3 file has no year"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(YEAR.key)

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file has correctly formatted year"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, '2020')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == '2020'

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file has unexpected formatted year"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, '2020-blah')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == '2020-blah'

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "Output, returns true, and truncates year when MP3 file has full-date year"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(YEAR.key, '2020-12-31')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(YEAR.key) == '2020-12-31'

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Truncated ${YEAR.desc}")
        0 * mockOutput._

        and:
        fixed

    }

}
