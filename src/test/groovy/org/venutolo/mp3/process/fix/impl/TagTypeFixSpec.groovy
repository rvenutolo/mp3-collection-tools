package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_2
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_3
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import org.venutolo.mp3.Mp3Specification

class TagTypeFixSpec extends Mp3Specification {

    private TagTypeFix fixer = new TagTypeFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new TagTypeFix(null)

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
        assert !mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        mp3File.setId3v1Tag(newId3v1Tag())

        and:
        assert mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 file doesn't have ID3v1 tag and has ID3v2.4 tag"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert !mp3File.hasId3v1Tag()
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == V2_4

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        !mp3File.hasId3v1Tag()
        mp3File.hasId3v2Tag()
        mp3File.getId3v2Tag().getVersion() == V2_4

    }

    def "Output, returns true, removes ID3v1 tag, and updates ID3v2 tag when MP3 file has ID3v1 tag and has ID3v#version tag"() {

        setup:
        mp3File.setId3v1Tag(newId3v1Tag())
        mp3File.setId3v2Tag(newId3v2Tag(version))

        and:
        assert mp3File.hasId3v1Tag()
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == version

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Removed ID3v1 tag')
        1 * mockOutput.write(mp3File, 'Converted to ID3v2.4 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasId3v1Tag()
        mp3File.hasId3v2Tag()
        mp3File.getId3v2Tag().getVersion() == V2_4

        where:
        version << [V2_2, V2_3]

    }

    def "Output, returns true, and removes ID3v1 tag when MP3 file has ID3v1 tag and has ID3v2.4 tag"() {

        setup:
        mp3File.setId3v1Tag(newId3v1Tag())
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v1Tag()
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == V2_4

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Removed ID3v1 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasId3v1Tag()
        mp3File.hasId3v2Tag()
        mp3File.getId3v2Tag().getVersion() == V2_4

    }

    def "Output, returns true, and updates ID3v2 tag when MP3 file doesn't have ID3v1 tag and has ID3v#version tag"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag(version))

        and:
        assert !mp3File.hasId3v1Tag()
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == version

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Converted to ID3v2.4 tag')
        0 * mockOutput._

        and:
        fixed

        and:
        !mp3File.hasId3v1Tag()
        mp3File.hasId3v2Tag()
        mp3File.getId3v2Tag().getVersion() == V2_4

        where:
        version << [V2_2, V2_3]

    }

}
