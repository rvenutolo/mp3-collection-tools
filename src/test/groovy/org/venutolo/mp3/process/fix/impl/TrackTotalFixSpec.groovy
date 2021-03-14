package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Field.TRACK
import static org.venutolo.mp3.Field.TRACK_TOTAL

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class TrackTotalFixSpec extends Mp3Specification {

    private def fixer = new TrackTotalFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new TrackTotalFix(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when #param is null"() {

        when:
        fixer.fix(mp3s, dir)

        then:
        thrown(NullPointerException)

        where:
        param             | mp3s           | dir
        'MP3s collection' | null           | RESOURCE_DIR
        'dir'             | [newMp3File()] | null

    }

    def "IAE when #desc"() {

        when:
        fixer.fix(mp3s, dir)

        then:
        thrown(IllegalArgumentException)

        where:
        desc                      | mp3s           | dir
        'MP3 collection is empty' | []             | RESOURCE_DIR
        'dir is not a directory'  | [newMp3File()] | newMp3File().file

    }

    def "No output and returns false when MP3 files files don't have tags"() {

        setup:
        mp3Files.each { mp3File ->
            assert !mp3File.hasID3v1Tag()
            assert !mp3File.hasID3v2Tag()
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.each { mp3File ->
            mp3File.setID3v1Tag(new ID3v1Tag())
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v1Tag()
            assert !mp3File.hasID3v2Tag()
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when all have same track total"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK_TOTAL.key, mp3Files.size() as String)
            mp3File.setTag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == mp3Files.size() as String
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when there are differing track totals"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK_TOTAL.key, idx as String)
            mp3File.setTag(tag)
        }
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == idx as String
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when there are overlapping track numbers"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, '1')
            mp3File.setTag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == '1'
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when there are missing track numbers"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, (idx + 10) as String)
            mp3File.setTag(tag)
        }
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == (idx + 10) as String
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "Output, returns true, and sets track total when none have track total and there are no missing or overlapping tracks"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, (idx + 1) as String)
            mp3File.setTag(tag)
        }
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == (idx + 1) as String
            assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        mp3Files.each { mp3File ->
            1 * mockOutput.write(mp3File, "Wrote: ${TRACK_TOTAL.desc}", "${mp3Files.size()}")
        }
        0 * mockOutput._

        and:
        mp3Files.every { mp3File ->
            mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == mp3Files.size() as String
        }

        and:
        fixed

    }

    def "Output, returns true, and sets track total when some have track total and there are no missing or overlapping tracks"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, (idx + 1) as String)
            if (idx % 2 == 1) {
                tag.setField(TRACK_TOTAL.key, mp3Files.size() as String)
            }
            mp3File.setTag(tag)
        }
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key) == (idx + 1) as String
            if (idx % 2 == 1) {
                assert mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == mp3Files.size() as String
            } else {
                assert !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key)
            }
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        mp3Files
            .findAll { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK.key).toInteger() % 2 }
            .each { mp3File ->
                1 * mockOutput.write(mp3File, "Wrote: ${TRACK_TOTAL.desc}", "${mp3Files.size()}")
            }
        0 * mockOutput._

        and:
        mp3Files.every { mp3File ->
            mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) == mp3Files.size() as String
        }

        and:
        fixed

    }

}
