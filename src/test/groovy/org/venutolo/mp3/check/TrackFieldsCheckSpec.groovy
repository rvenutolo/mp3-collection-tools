package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class TrackFieldsCheckSpec extends CheckSpecification {

    private def checker = new TrackFieldsCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

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

    def "No warning when MP3 file doesn't have tags"() {

        setup:
        assert !mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = new ID3v1Tag()
        tag.setField(TRACK.key, '1')
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when has neither track or track total"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(TRACK.key)
        assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when has track but not track total"() {

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
        0 * mockWarnings._

    }

    def "No warning when has track total but not track"() {

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
        0 * mockWarnings._

    }

    def "Warning when #desc digit track and track total"() {

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
        1 * mockWarnings.write(mp3File, 'Track and track total are not in ##/## format', "${trackVal}/${totalVal}")

        where:
        desc        | trackVal | totalVal
        'single'    | '1'      | '2'
        'quadruple' | '0001'   | '0002'

    }

    def "No warning when #desc digit track and track total"() {

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
        0 * mockWarnings._

        where:
        desc     | trackVal | totalVal
        'double' | '01'     | '02'
        'triple' | '001'    | '002'

    }

    def "Warning when #trackDesc digit track and #totalDesc digit track total"() {

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
        1 * mockWarnings.write(mp3File, 'Track and track total are not in ##/## format', "${trackVal}/${totalVal}")

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
