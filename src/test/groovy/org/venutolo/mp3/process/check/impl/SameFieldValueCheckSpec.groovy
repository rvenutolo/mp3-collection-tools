package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.SAME_VALUE_FIELDS
import static org.venutolo.mp3.core.Field.ALBUM

import org.venutolo.mp3.specs.Mp3Specification

class SameFieldValueCheckSpec extends Mp3Specification {

    private SameFieldValueCheck checker = new SameFieldValueCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new SameFieldValueCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when #param is null"() {

        when:
        checker.check(mp3s, dir)

        then:
        thrown(NullPointerException)

        where:
        param            | mp3s           | dir
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
            assert !mp3File.hasId3v1Tag()
            assert !mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have ID3v1 tags and don't have ID3v2 tags"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = newId3v1Tag()
            tag.set(ALBUM, fieldVal(ALBUM, idx))
            mp3File.setId3v1Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasId3v1Tag()
            assert mp3File.getId3v1Tag().get(ALBUM) == fieldVal(ALBUM, idx)
            assert !mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have empty ID3v2 tags"() {

        setup:
        mp3Files.each { mp3File ->
            mp3File.setId3v2Tag(newId3v2Tag())
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 files have same #field values"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(field, fieldVal(field))
        mp3Files.each { mp3File ->
            mp3File.setId3v2Tag(tag)
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasId3v2Tag()
            assert mp3File.getId3v2Tag().get(field) == fieldVal(field)
        }

        when:
        checker.check(mp3Files, RESOURCE_DIR)

        then:
        0 * mockOutput._

        where:
        field << SAME_VALUE_FIELDS

    }

    // TODO missing test for when there should be output

}
