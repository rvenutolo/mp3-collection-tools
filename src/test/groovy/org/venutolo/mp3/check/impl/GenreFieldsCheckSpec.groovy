package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.ALLOWED_GENRES
import static org.venutolo.mp3.fields.Field.GENRE

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class GenreFieldsCheckSpec extends Mp3Specification {

    private def checker = new GenreFieldsCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new GenreFieldsCheck(null)

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
        tag.setGenre('BAD_GENRE')
        mp3File.setID3v1Tag(tag)
        assert mp3File.hasID3v1Tag()
        assert !mp3File.hasID3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when no genre"() {

        setup:
        mp3File.setID3v2Tag(new ID3v24Tag())
        assert mp3File.hasID3v2Tag()
        assert !mp3File.getID3v2Tag().getFirst(GENRE.key)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when unexpected genre"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(GENRE.key, 'BAD_GENRE')
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(GENRE.key) == 'BAD_GENRE'

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, 'Unexpected genre: BAD_GENRE')
        0 * mockOutput._

    }

    def "No output when genre: #genre"() {

        setup:
        def tag = new ID3v24Tag()
        tag.setField(GENRE.key, genre)
        mp3File.setID3v2Tag(tag)
        assert mp3File.hasID3v2Tag()
        assert mp3File.getID3v2Tag().getFirst(GENRE.key) == genre

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        genre << ALLOWED_GENRES

    }

}
