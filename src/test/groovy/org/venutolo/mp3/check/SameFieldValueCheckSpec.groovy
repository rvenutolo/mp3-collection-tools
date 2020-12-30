package org.venutolo.mp3.check

import static org.venutolo.mp3.check.SameFieldValueCheck.SAME_VALUE_FIELDS
import static org.venutolo.mp3.fields.Field.GENRE

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class SameFieldValueCheckSpec extends CheckSpecification {

    private def checker = new SameFieldValueCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

        when:
        new SameFieldValueCheck(null)

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
        'dir is not a directory'  | [newMp3File()] | new File(newMp3File().file.path)

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
            tag.setAlbum("album${idx}")
            mp3File.setID3v1Tag(tag)
        }
        mp3Files.each { assert it.hasID3v1Tag() && !it.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 files have empty ID3v2 tags"() {

        setup:
        mp3Files.each { mp3File ->
            mp3File.setID3v2Tag(new ID3v24Tag())
        }
        mp3Files.each { assert it.hasID3v2Tag() }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

    }

    def "No warning when MP3 files have same #field values"() {

        setup:
        def tag = new ID3v24Tag()
        // when setting genre to a numeric string, it will be converted to a desc string (ex: '1' -> 'Classic Rock')
        def fieldValue = field == GENRE ? 'genre1' : '1'
        tag.setField(field.key, fieldValue)
        mp3Files.each { it.setID3v2Tag(tag) }
        mp3Files.each { assert it.hasID3v2Tag() && it.getID3v2Tag().getFirst(field.key) == fieldValue }

        when:
        checker.check(mp3Files, dir)

        then:
        0 * mockWarnings._

        where:
        field << SAME_VALUE_FIELDS

    }

    def "Warning when MP3 files have multiple #field values"() {

        setup:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            // when setting genre to a numeric string, it will be converted to a desc string (ex: '1' -> 'Classic Rock')
            def fieldValue = field == GENRE ? "genre${idx + 1}" : "${idx + 1}"
            tag.setField(field.key, fieldValue)
            mp3File.setID3v2Tag(tag)
        }
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag() &&
                mp3File.getID3v2Tag().getFirst(field.key) == (field == GENRE ? "genre${idx + 1}" : "${idx + 1}")
        }

        when:
        checker.check(mp3Files, dir)

        then:
        1 * mockWarnings.write(dir, "Non-uniform ${field.desc} values", field == GENRE ? 'genre1, genre2' : '1, 2')

        where:
        field << SAME_VALUE_FIELDS

    }

}
