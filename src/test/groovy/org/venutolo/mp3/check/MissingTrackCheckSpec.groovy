package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.CheckSpecification

class MissingTrackCheckSpec extends CheckSpecification {

    private def checker = new MissingTrackCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new MissingTrackCheck(null)

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

    def "No output when MP3 files don't have tags"() {

        setup:
        mp3Files.each { mp3FIle ->
            assert !mp3FIle.hasID3v1Tag()
            assert !mp3FIle.hasID3v2Tag()
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v1Tag()
            tag.setField(TRACK.key, '9')
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

    def "No output when MP3 files have no track numbers"() {

        setup:
        mp3Files.each { mp3File -> mp3File.setID3v2Tag(new ID3v24Tag()) }
        mp3Files.each { mp3File -> assert mp3File.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have no missing tracks"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, "${idx + 1}")
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

    def "Output when MP3 files have missing track"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, '3')
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key)
        }

        when:
        checker.check(mp3Files, dir)

        then:
        1 * mockOutput.write(dir, 'Missing track #1')
        1 * mockOutput.write(dir, 'Missing track #2')
        0 * mockOutput._

    }

    def "No output when MP3 files have empty track values"() {

        setup:
        mp3Files.each { mp3File ->
            def tag = new ID3v24Tag()
            tag.setField(TRACK.key, '')
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v2Tag()
            assert mp3File.getID3v2Tag().getFirst(TRACK.key).isEmpty()
        }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockOutput._

    }

}
