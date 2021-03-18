package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_2
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_3
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import org.venutolo.mp3.specs.Mp3Specification

class TagTypeCheckSpec extends Mp3Specification {

    private TagTypeCheck checker = new TagTypeCheck(mockOutput)

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
        mp3File.setId3v1Tag(newId3v1Tag())
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v1Tag()
        assert mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Has ID3v1 tag')
        0 * mockOutput._

    }

    def "Output when file does not have ID3v2 tag"() {

        setup:
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Does not have ID3v2 tag')
        0 * mockOutput._

    }

    def "Output when file has ID3v#version tag"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag(version))

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == version

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'ID3v2 tag is not v2.4', "v${version}")
        0 * mockOutput._

        where:
        version << [V2_2, V2_3]

    }

    def "No output when file has only ID3v2.4 tag"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag(V2_4))

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().getVersion() == V2_4

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

}