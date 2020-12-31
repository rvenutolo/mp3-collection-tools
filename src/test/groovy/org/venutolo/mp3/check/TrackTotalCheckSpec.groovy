package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class TrackTotalCheckSpec extends CheckSpecification {

    private def checker = new TrackTotalCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

        when:
        new TrackTotalCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when #field is null"() {

        when:
        checker.check(mp3s, dir)

        then:
        thrown(NullPointerException)

        where:
        field            | mp3s           | dir
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

    def "No warning when MP3 files don't have tags"() {

        setup:
        mp3Files.each { assert !it.hasID3v1Tag() && !it.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v1Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            // id3v1 doesn't support total tracks
            mp3File.setID3v1Tag(tag)
        }
        mp3Files.each { assert it.hasID3v1Tag() && !it.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 files have no track total numbers"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            mp3File.setID3v2Tag(new ID3v24Tag())
        }
        mp3Files.each { assert it.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 files have correct track track values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            tag.setField(TRACK_TOTAL.key, '2')
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { assert it.hasID3v2Tag() && it.getID3v2Tag().getFirst(TRACK.key) }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "Warning when MP3 files have incorrect track total value"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
            tag.setField(TRACK_TOTAL.key, "${idx + 1}")
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { assert it.hasID3v2Tag() && it.getID3v2Tag().getFirst(TRACK.key) }

        when:
        checker.check(mp3Files, dir)

        then:
        1 * mockWarnings.write(dir, 'Wrong total tracks')

    }

}