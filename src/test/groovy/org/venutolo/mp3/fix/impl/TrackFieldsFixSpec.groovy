package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TrackFieldsFixSpec extends Mp3Specification {

    private def fixer = new TrackFieldsFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new TrackFieldsFix(null)

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
        def tag = new ID3v1Tag()
        // NOTE: cannot actually set ID3v1 track values due to missing functionality in MP3 library
        mp3File.setID3v1Tag(tag)

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

    def "No output, returns false, and doesn't change track when track is #trackDesc and track total is #totalDesc"() {

        setup:
        def tag = new ID3v24Tag()
        if (trackVal) {
            tag.setField(TRACK.key, trackVal)
        }
        if (totalVal) {
            tag.setField(TRACK_TOTAL.key, totalVal)
        }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        if (trackVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        } else {
            assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        }
        if (totalVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal
        } else {
            assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)
        }

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal

        where:
        trackDesc    | totalDesc    | trackVal | totalVal
        'empty'      | 'empty'      | ''       | ''
        'empty'      | 'non-padded' | ''       | '2'
        'non-padded' | 'empty'      | '1'      | ''
        'non-padded' | 'non-padded' | '1'      | '2'
    }

    def "Output, returns true, and sets track when track is #trackDesc and track total is #totalDesc"() {

        setup:
        def tag = new ID3v24Tag()
        if (trackVal) {
            tag.setField(TRACK.key, trackVal)
        }
        if (totalVal) {
            tag.setField(TRACK_TOTAL.key, totalVal)
        }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        if (trackVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        } else {
            assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        }
        if (totalVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal
        } else {
            assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)
        }

        when:
        def fixed = fixer.fix(mp3File)

        then:
        if (newTrackVal) {
            mockOutput.write(mp3File, "Removed ${TRACK.desc} 0-padding")
        }
        if (newTotalVal) {
            mockOutput.write(mp3File, "Removed ${TRACK_TOTAL.desc} 0-padding")
        }

        and:
        fixed

        and:
        mp3File.getID3v2Tag().getFirst(TRACK.key) == (newTrackVal ?: trackVal)
        mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == (newTotalVal ?: totalVal)

        where:
        trackDesc    | totalDesc    | trackVal | totalVal | newTrackVal | newTotalVal
        'empty'      | 'padded'     | ''       | '02'     | null        | '2'
        'non-padded' | 'padded'     | '1'      | '02'     | null        | '2'
        'padded'     | 'empty'      | '01'     | ''       | '1'         | null
        'padded'     | 'non-padded' | '01'     | '2'      | '1'         | null
        'padded'     | 'padded'     | '01'     | '02'     | '1'         | '2'

    }

}
