package org.venutolo.mp3.check

import static GenreFieldsCheck.ALLOWED_GENRES
import static org.venutolo.mp3.fields.Field.GENRE

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag

class GenreFieldsCheckSpec extends CheckSpecification {

    private def checker = new GenreFieldsCheck(mockWarnings)

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
        tag.setGenre('BAD_GENRE')
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "No warning when no genre"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(GENRE.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

    }

    def "Warning when unexpected genre"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(GENRE.key, 'BAD_GENRE')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(GENRE.key) == 'BAD_GENRE'

        when:
        checker.check(mp3File)

        then:
        1 * mockWarnings.write(mp3File, 'Unexpected genre: BAD_GENRE')

    }

    def "No warning when genre: #genre"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(GENRE.key, genre)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(GENRE.key) == genre

        when:
        checker.check(mp3File)

        then:
        0 * mockWarnings._

        where:
        genre << ALLOWED_GENRES

    }

}