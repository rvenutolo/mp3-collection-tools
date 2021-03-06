package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import org.venutolo.mp3.Mp3Specification

class TrackTotalCheckSpec extends Mp3Specification {

    private TrackTotalCheck checker = new TrackTotalCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new TrackTotalCheck(null)

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
        mp3Files.eachWithIndex { mp3File, idx ->
            // id3v1 doesn't support total tracks
            mp3File.setId3v1Tag(newId3v1Tag())
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v1Tag()
            assert !mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files don't all have ID3v2 tags (#numWithoutId3v2Tags without ID3v2 tag)"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            if (idx >= numWithoutId3v2Tags) {
                def tag = newId3v2Tag()
                tag.set(TRACK, "${idx + 1}")
                tag.set(TRACK_TOTAL, "${idx + 1}")
                mp3File.setId3v2Tag(tag)
            }
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            if (idx < numWithoutId3v2Tags) {
                assert !mp3File.hasId3v2Tag()
            } else {
                assert mp3File.getId3v2Tag().get(TRACK) == "${idx + 1}"
                assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == "${idx + 1}"
            }
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

        where:
        numWithoutId3v2Tags << (1..NUM_MP3_FILES)

    }

    def "No output when MP3 files have no track total numbers"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v2Tag()
            tag.set(TRACK, "${idx + 1}")
            mp3File.setId3v2Tag(newId3v2Tag())
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
            assert !mp3File.getId3v2Tag().get(TRACK_TOTAL)
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have correct track track values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v2Tag()
            tag.set(TRACK, fieldVal(TRACK, idx))
            tag.set(TRACK_TOTAL, NUM_MP3_FILES as String)
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(TRACK) == fieldVal(TRACK, idx)
            assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == NUM_MP3_FILES as String
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "Output when MP3 files have incorrect track total value"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v2Tag()
            tag.set(TRACK, fieldVal(TRACK, idx))
            tag.set(TRACK_TOTAL, fieldVal(TRACK_TOTAL, idx))
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(TRACK) == fieldVal(TRACK, idx)
            assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == fieldVal(TRACK_TOTAL, idx)
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        1 * mockOutput.write(RESOURCE_DIR, "Wrong ${TRACK_TOTAL}")
        0 * mockOutput._

    }

    def "No output when MP3 files have empty track values"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = newId3v2Tag()
            tag.set(TRACK_TOTAL, '99')
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
            assert !mp3File.getId3v2Tag().has(TRACK)
            assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == '99'
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have empty track total values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v2Tag()
            tag.set(TRACK, fieldVal(TRACK, idx))
            tag.set(TRACK_TOTAL, '')
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(TRACK) == fieldVal(TRACK, idx)
            assert !mp3File.getId3v2Tag().has(TRACK_TOTAL)
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

}
