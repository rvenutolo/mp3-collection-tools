package org.venutolo.mp3.fix.impl

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v22Tag
import org.jaudiotagger.tag.id3.ID3v23Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TagTypeFixSpec extends Mp3Specification {

    private def fixer = new TagTypeFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new ExtraneousFieldFix(null)

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

    def "Output, returns true, and removes ID3v1 tag when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        mp3File.setID3v1Tag(new ID3v1Tag())
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Removed ID3v1 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasID3v1Tag()
        !mp3File.hasID3v2Tag()

    }

    def "Output, returns true, removes ID3v1 tag, and updates ID3v2 tag when MP3 file has ID3v1 tag and has ID3v#version tag"() {

        setup:
        mp3File.setID3v1Tag(new ID3v1Tag())
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == majorVersion

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Removed ID3v1 tag')
        1 * mockOutput.write(mp3File, 'Converted to ID3v2.4 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasID3v1Tag()
        mp3File.hasID3v2Tag()
        mp3File.getID3v2Tag().getMajorVersion() == 4

        where:
        version | tag             | majorVersion
        '2.2'   | new ID3v22Tag() | 2
        '2.3'   | new ID3v23Tag() | 3

    }

    def "Output, returns true, and removes ID3v1 tag when MP3 file has ID3v1 tag and has ID3v2.4 tag"() {

        setup:
        mp3File.setID3v1Tag(new ID3v1Tag())
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v1Tag()
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == 4

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Removed ID3v1 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasID3v1Tag()
        mp3File.hasID3v2Tag()
        mp3File.getID3v2Tag().getMajorVersion() == 4

    }

    def "Output, returns true, and updates ID3v2 tag when MP3 file doesn't have ID3v1 tag and has ID3v#version tag"() {

        setup:
        mp3File.setID3v2Tag(tag)
        assert !mp3File.hasID3v1Tag()
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == majorVersion

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Converted to ID3v2.4 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasID3v1Tag()
        mp3File.hasID3v2Tag()
        mp3File.getID3v2Tag().getMajorVersion() == 4

        where:
        version | tag             | majorVersion
        '2.2'   | new ID3v22Tag() | 2
        '2.3'   | new ID3v23Tag() | 3

    }

    def "Output and returns false when MP3 file doesn't have ID3v1 tag and has ID3v2.4 tag"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert !mp3File.hasID3v1Tag()
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getMajorVersion() == 4

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        !mp3File.hasID3v1Tag()
        mp3File.hasID3v2Tag()
        mp3File.getID3v2Tag().getMajorVersion() == 4

    }

}
