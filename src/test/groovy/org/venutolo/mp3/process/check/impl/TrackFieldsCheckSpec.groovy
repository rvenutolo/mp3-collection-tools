package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import org.venutolo.mp3.core.ID3v1Tag
import org.venutolo.mp3.core.ID3v2Tag
import org.venutolo.mp3.specs.Mp3Specification

class TrackFieldsCheckSpec extends Mp3Specification {

    private def checker = new TrackFieldsCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new TrackFieldsCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when MP3 file is null"() {

        when:
        checker.check(null)

        then:
        thrown(NullPointerException)

    }

    def "No output when MP3 file doesn't have tags"() {

        setup:
        assert !mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = new ID3v1Tag()
        // NOTE: cannot actually set ID3v1 track values due to missing functionality in MP3 library
        mp3File.setID3v1Tag(tag)

        and:
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has empty track and track total"() {

        setup:
        mp3File.setID3v2Tag(new ID3v2Tag())

        and:
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().get(TRACK)
        assert !mp3File.getID3v2Tag().get(TRACK_TOTAL)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has non-padded track and track total"() {

        setup:
        def tag = new ID3v2Tag()
        tag.set(TRACK, '1')
        tag.set(TRACK_TOTAL, '1')
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().get(TRACK) == '1'
        assert mp3File.getID3v2Tag().get(TRACK_TOTAL) == '1'

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has #trackDesc track and #totalDesc track total"() {

        setup:
        def tag = new ID3v2Tag()
        if (trackVal) {
            tag.set(TRACK, trackVal)
        }
        if (totalVal) {
            tag.set(TRACK_TOTAL, totalVal)
        }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        if (trackVal) {
            assert mp3File.getID3v2Tag().get(TRACK) == trackVal
        } else {
            assert !mp3File.getID3v2Tag().has(TRACK)
        }
        if (totalVal) {
            assert mp3File.getID3v2Tag().get(TRACK_TOTAL) == totalVal
        } else {
            assert !mp3File.getID3v2Tag().has(TRACK_TOTAL)
        }

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        trackDesc    | totalDesc    | trackVal | totalVal
        'empty'      | 'empty'      | ''       | ''
        'empty'      | 'non-padded' | ''       | '2'
        'non-padded' | 'empty'      | '1'      | ''
        'non-padded' | 'non-padded' | '1'      | '2'

    }

    def "Output when has #trackDesc track and #totalDesc track total"() {

        setup:
        def tag = new ID3v2Tag()
        if (trackVal) {
            tag.set(TRACK, trackVal)
        }
        if (totalVal) {
            tag.set(TRACK_TOTAL, totalVal)
        }
        mp3File.setID3v2Tag(tag)

        and:
        assert mp3File.hasID3v2Tag()
        if (trackVal) {
            assert mp3File.getID3v2Tag().get(TRACK) == trackVal
        } else {
            assert !mp3File.getID3v2Tag().has(TRACK)
        }
        if (totalVal) {
            assert mp3File.getID3v2Tag().get(TRACK_TOTAL) == totalVal
        } else {
            assert !mp3File.getID3v2Tag().has(TRACK_TOTAL)
        }

        when:
        checker.check(mp3File)

        then:
        if (trackVal.startsWith('0')) {
            1 * mockOutput.write(mp3File, "${TRACK} has 0-padding", trackVal)
        }
        if (totalVal.startsWith('0')) {
            1 * mockOutput.write(mp3File, "${TRACK_TOTAL} has 0-padding", totalVal)
        }
        0 * mockOutput._

        where:
        trackDesc    | totalDesc    | trackVal | totalVal
        'empty'      | 'padded'     | ''       | '02'
        'non-padded' | 'padded'     | '1'      | '02'
        'padded'     | 'empty'      | '01'     | ''
        'padded'     | 'non-padded' | '01'     | '2'
        'padded'     | 'padded'     | '01'     | '02'

    }

}
