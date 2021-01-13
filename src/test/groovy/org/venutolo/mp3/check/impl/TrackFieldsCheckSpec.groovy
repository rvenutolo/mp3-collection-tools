package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
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
        tag.setField(TRACK.key, '1')
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has neither track or track total"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has track but not track total"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, '1')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(TRACK.key) == '1'
        assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when has track total but not track"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK_TOTAL.key, '1')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == '1'

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when #desc digit track and track total"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, trackVal)
        tag.setField(TRACK_TOTAL.key, totalVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(
            mp3File,
            "${TRACK.desc.capitalize()} and ${TRACK_TOTAL.desc} are not in ##/## format",
            "${trackVal}/${totalVal}"
        )
        0 * mockOutput._

        where:
        desc        | trackVal | totalVal
        'single'    | '1'      | '2'
        'quadruple' | '0001'   | '0002'

    }

    def "No output when #desc digit track and track total"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, trackVal)
        tag.setField(TRACK_TOTAL.key, totalVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        desc     | trackVal | totalVal
        'double' | '01'     | '02'
        'triple' | '001'    | '002'

    }

    def "Output when #trackDesc digit track and #totalDesc digit track total"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(TRACK.key, trackVal)
        tag.setField(TRACK_TOTAL.key, totalVal)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(TRACK.key) == trackVal
        assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == totalVal

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(
            mp3File,
            "${TRACK.desc.capitalize()} and ${TRACK_TOTAL.desc} are not in ##/## format",
            "${trackVal}/${totalVal}"
        )
        0 * mockOutput._

        where:
        trackDesc | trackVal | totalDesc | totalVal
        'single'  | '1'      | 'double'  | '02'
        'single'  | '1'      | 'triple'  | '002'
        'double'  | '01'     | 'single'  | '2'
        'double'  | '01'     | 'triple'  | '002'
        'triple'  | '001'    | 'single'  | '2'
        'triple'  | '001'    | 'double'  | '02'

    }

}
