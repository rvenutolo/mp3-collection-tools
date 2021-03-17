package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ALLOWED_GENRES
import static org.venutolo.mp3.core.Field.GENRE

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
        assert !mp3File.hasId3v1Tag()
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when MP3 file has ID3v1 tag and doesn't have ID3v2 tag"() {

        setup:
        def tag = newId3v1Tag()
        // must use a defined genre, ex: Polka
        tag.set(GENRE, 'Polka')
        mp3File.setId3v1Tag(tag)

        and:
        assert mp3File.hasId3v1Tag()
        assert mp3File.getId3v1Tag().get(GENRE) == 'Polka'
        assert !mp3File.hasId3v2Tag()

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "No output when no genre"() {

        setup:
        mp3File.setId3v2Tag(newId3v2Tag())

        and:
        assert mp3File.hasId3v2Tag()
        assert !mp3File.getId3v2Tag().has(GENRE)

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

    }

    def "Output when unexpected genre"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(GENRE, 'BAD_GENRE')
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(GENRE) == 'BAD_GENRE'

        when:
        checker.check(mp3File)

        then:
        1 * mockOutput.write(mp3File, "Unexpected ${GENRE}", 'BAD_GENRE')
        0 * mockOutput._

    }

    def "No output when genre: #genre"() {

        setup:
        def tag = newId3v2Tag()
        tag.set(GENRE, genre)
        mp3File.setId3v2Tag(tag)

        and:
        assert mp3File.hasId3v2Tag()
        assert mp3File.getId3v2Tag().get(GENRE) == genre

        when:
        checker.check(mp3File)

        then:
        0 * mockOutput._

        where:
        genre << ALLOWED_GENRES

    }

}
