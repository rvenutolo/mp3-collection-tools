package org.venutolo.mp3.check.impl

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v22Tag
import org.jaudiotagger.tag.id3.ID3v23Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TagTypeCheckSpec extends Mp3Specification {

    private def checker = new TagTypeCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new TagTypeCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when MP3 file is null"() {

        when:
        checker.check(null)

        then:
        thrown(NullPointerException)

    }

    def "Output when file has ID3v1 tag"() {

        setup:
        mp3File.setID3v1Tag(new ID3v1Tag())
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v1Tag()
        assert mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Has ID3v1 tag')
        0 * mockOutput._

    }

    def "Output when file does not have ID3v2 tag"() {

        setup:
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Does not have ID3v2 tag')
        0 * mockOutput._

    }

    def "Output when file has ID3v#version tag"() {

        setup:
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == version

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'ID3v2 tag is not v2.4', "v2.${version}")
        0 * mockOutput._

        where:
        version | tag
        2       | new ID3v22Tag()
        3       | new ID3v23Tag()

    }

    def "No output when file has only ID3v2.4 tag"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == 4

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

}