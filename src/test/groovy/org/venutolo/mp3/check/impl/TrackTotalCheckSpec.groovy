package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TrackTotalCheckSpec extends Mp3Specification {

    private def checker = new TrackTotalCheck(mockOutput)

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
        param | mp3s | dir
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
            assert !mp3File.hasID3v1Tag()
            assert !mp3File.hasID3v2Tag()
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v1Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            // id3v1 doesn't support total tracks
            mp3File.setID3v1Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v1Tag()
            assert !mp3File.hasID3v2Tag()
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have no track total numbers"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            mp3File.setID3v2Tag(new ID3v24Tag())
        }
        mp3Files.each { mp3File -> assert mp3File.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have correct track track values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            tag.setField(TRACK_TOTAL.key, mp3Files.size() as String)
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key)
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "Output when MP3 files have incorrect track total value"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            tag.setField(TRACK_TOTAL.key, "${idx + 1}")
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key)
        }

        when:
        checker.check(mp3Files, dir)

        then:
        1 * mockOutput.write(dir, "Wrong ${TRACK_TOTAL.desc}")
        0 * mockOutput._

    }

    def "No output when MP3 files have empty track values"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, '')
            tag.setField(TRACK_TOTAL.key, '99')
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key).isEmpty()
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == '99'
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have empty track total values"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, '1')
            tag.setField(TRACK_TOTAL.key, '')
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == '1'
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key).isEmpty()
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

}