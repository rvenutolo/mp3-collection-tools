package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import org.venutolo.mp3.specs.Mp3Specification

class TrackFieldsFixSpec extends Mp3Specification {

    private TrackFieldsFix fixer = new TrackFieldsFix(mockOutput)

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
        def tag = newId3v1Tag()
        // NOTE: cannot actually set ID3v1 track values due to missing functionality in MP3 library
        mp3File.setId3v1Tag(tag)

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

    def "No output, returns false, and doesn't change track when track is #trackDesc and track total is #totalDesc"() {

        setup:
        def tag = newId3v2Tag()
        if (trackVal) {
            tag.set(TRACK, trackVal)
        }
        if (totalVal) {
            tag.set(TRACK_TOTAL, totalVal)
        }
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        if (trackVal) {
            assert mp3File.getId3v2Tag().get(TRACK) == trackVal
        } else {
            assert !mp3File.getId3v2Tag().has(TRACK)
        }
        if (totalVal) {
            assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == totalVal
        } else {
            assert !mp3File.getId3v2Tag().has(TRACK_TOTAL)
        }

        when:
        def fixed = fixer.fix(mp3File)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        mp3File.getId3v2Tag().get(TRACK) == trackVal
        mp3File.getId3v2Tag().get(TRACK_TOTAL) == totalVal

        where:
        trackDesc    | totalDesc    | trackVal | totalVal
        'empty'      | 'empty'      | ''       | ''
        'empty'      | 'non-padded' | ''       | '2'
        'non-padded' | 'empty'      | '1'      | ''
        'non-padded' | 'non-padded' | '1'      | '2'
    }

    def "Output, returns true, and sets track when track is #trackDesc and track total is #totalDesc"() {

        setup:
        def tag = newId3v2Tag()
        if (trackVal) {
            tag.set(TRACK, trackVal)
        }
        if (totalVal) {
            tag.set(TRACK_TOTAL, totalVal)
        }
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        if (trackVal) {
            assert mp3File.getId3v2Tag().get(TRACK) == trackVal
        } else {
            assert !mp3File.getId3v2Tag().has(TRACK)
        }
        if (totalVal) {
            assert mp3File.getId3v2Tag().get(TRACK_TOTAL) == totalVal
        } else {
            assert !mp3File.getId3v2Tag().has(TRACK_TOTAL)
        }

        when:
        def fixed = fixer.fix(mp3File)

        then:
        if (newTrackVal) {
            mockOutput.write(mp3File, "Removed ${TRACK} 0-padding")
        }
        if (newTotalVal) {
            mockOutput.write(mp3File, "Removed ${TRACK_TOTAL} 0-padding")
        }

        and:
        fixed

        and:
        mp3File.getId3v2Tag().get(TRACK) == (newTrackVal ?: trackVal)
        mp3File.getId3v2Tag().get(TRACK_TOTAL) == (newTotalVal ?: totalVal)

        where:
        trackDesc    | totalDesc    | trackVal | totalVal | newTrackVal | newTotalVal
        'empty'      | 'padded'     | ''       | '02'     | null        | '2'
        'non-padded' | 'padded'     | '1'      | '02'     | null        | '2'
        'padded'     | 'empty'      | '01'     | ''       | '1'         | null
        'padded'     | 'non-padded' | '01'     | '2'      | '1'         | null
        'padded'     | 'padded'     | '01'     | '02'     | '1'         | '2'

    }

}
