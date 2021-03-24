package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.TRACK

import org.venutolo.mp3.specs.Mp3Specification

class OverlappingTrackCheckSpec extends Mp3Specification {

    private OverlappingTrackCheck checker = new OverlappingTrackCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new OverlappingTrackCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when #param is null"() {

        when:
        checker.check(mp3s, dir)

        then:
        thrown(NullPointerException)

        where:
        param            | mp3s           | dir
        'MP3 collection' | null           | RESOURCE_DIR
        'dir'            | [newMp3File()] | null

    }

    def "IAE when #desc"() {

        when:
        checker.check(mp3s, dir)

        then:
        thrown(IllegalArgumentException)

        where:
        desc                      | mp3s           | dir
        'MP3 collection is empty' | []             | RESOURCE_DIR
        'dir is not a directory'  | [newMp3File()] | newMp3File().file

    }

    def "No output when MP3 files don't have tags"() {

        setup:
        mp3Files.each { mp3File ->
            assert !mp3File.hasId3v1Tag()
            assert !mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = newId3v1Tag()
            tag.set(TRACK, '1')
            mp3File.setId3v1Tag(tag)
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v1Tag()
            assert mp3File.getId3v1Tag().get(TRACK) == '1'
            assert !mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have no track numbers"() {

        setup:
        mp3Files.each { mp3File ->
            mp3File.setId3v2Tag(newId3v2Tag())
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have distinct track values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v2Tag()
            tag.set(TRACK, fieldVal(TRACK, idx))
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(TRACK) == fieldVal(TRACK, idx)
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "Output when MP3 files have same track values"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = newId3v2Tag()
            tag.set(TRACK, '1')
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(TRACK) == '1'
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        1 * mockOutput.write(RESOURCE_DIR, "Multiple ${TRACK} #1")
        0 * mockOutput._

    }

}
