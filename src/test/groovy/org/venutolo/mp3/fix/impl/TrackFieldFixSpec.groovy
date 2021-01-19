package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TrackFieldFixSpec extends Mp3Specification {

    private def fixer = new TrackFieldFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new TrackFieldFix(null)

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
        ID3_FIELDS.each { field -> tag.setField(field.key, '1') }
        mp3File.setID3v1Tag(tag)
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
        tag.setField(TRACK.key, trackVal)
        tag.setField(TRACK_TOTAL.key, totalVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        if (trackVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK.key)
        } else {
            assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        }
        if (totalVal) {
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)
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

        where:
        // NOTE: track values, if non-empty, cannot be non-numeric
        trackDesc      | totalDesc      | trackVal | totalVal
        'empty'        | 'empty'        | ''       | ''
        'empty'        | 'single digit' | ''       | '1'
        'empty'        | 'double digit' | ''       | '11'
        'single digit' | 'empty'        | '1'      | ''
        'single digit' | 'single digit' | '1'      | '1'
        'double digit' | 'empty'        | '11'     | ''
        'double digit' | 'single digit' | '11'     | '1'
        'double digit' | 'double digit' | '11'     | '11'
        'triple digit' | 'empty'        | '111'    | ''
        'triple digit' | 'single digit' | '111'    | '1'
        'triple digit' | 'double digit' | '111'    | '11'
        'triple digit' | 'triple digit' | '111'    | '111'

    }

    def "Output, returns true, and sets track when track is #trackDesc and track total is #totalDesc"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, trackVal)
        tag.setField(TRACK_TOTAL.key, totalVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(TRACK.key)
        assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)

        when:
        def fixed = fixer.fix(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Formatted track', newTrackVal)

        and:
        fixed

        and:
        mp3File.getID3v2Tag().getFirst(TRACK.key) == newTrackVal

        where:
        trackDesc      | totalDesc      | trackVal | totalVal | newTrackVal
        'single digit' | 'double digit' | '1'      | '11'     | '01'
        'single digit' | 'triple digit' | '1'      | '111'    | '001'
        'double digit' | 'triple digit' | '11'     | '111'    | '011'

    }

}
