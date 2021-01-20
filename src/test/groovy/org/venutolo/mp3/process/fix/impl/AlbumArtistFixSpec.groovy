package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Field.ALBUM_ARTIST
import static org.venutolo.mp3.Field.ARTIST
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_DIFFERENT_NON_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_DIFFERENT_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_EMPTY
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_MIX_EMPTY_SAME
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.AlbumArtistCondition.ALBUM_ARTIST_SAME
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.ArtistCondition.ARTIST_EMPTY
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.ArtistCondition.ARTIST_NON_UNIFORM
import static org.venutolo.mp3.process.fix.impl.AlbumArtistFixSpec.ArtistCondition.ARTIST_UNIFORM

import org.jaudiotagger.tag.id3.ID3v1Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import org.venutolo.mp3.specs.Mp3Specification

class AlbumArtistFixSpec extends Mp3Specification {

    // NOTE TO SELF
    // In this spec, 'uniform' and 'non-uniform' refer to if all the values for
    // a single field are the same or not. 'Same' and 'different' refer to if the
    // artist and album artist field values are the same or not.

    static enum ArtistCondition {

        ARTIST_EMPTY('empty artists'),
        ARTIST_UNIFORM('uniform artists'),
        ARTIST_NON_UNIFORM('non-uniform artists')

        private final String desc

        ArtistCondition(String desc) {
            this.desc = desc
        }

        @Override
        String toString() {
            return desc
        }

    }

    static enum AlbumArtistCondition {

        ALBUM_ARTIST_EMPTY('empty album artists'),
        ALBUM_ARTIST_SAME('uniform same album artist'),
        ALBUM_ARTIST_DIFFERENT_UNIFORM('uniform different album artist'),
        ALBUM_ARTIST_DIFFERENT_NON_UNIFORM('non-uniform different album artists'),
        ALBUM_ARTIST_MIX_EMPTY_SAME('mix of empty and same album artists'),
        ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM('mix of empty and uniform different album artists'),
        ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM('mix of empty and non-uniform different album artists'),
        ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM('mix of same and uniform different album artists'),
        ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM('mix of same and non-uniform different album artists')

        private final String desc

        AlbumArtistCondition(String desc) {
            this.desc = desc
        }

        @Override
        String toString() {
            return desc
        }

    }

    private static final def FIX_CASES = [
        [ARTIST_UNIFORM, ALBUM_ARTIST_EMPTY],
        [ARTIST_UNIFORM, ALBUM_ARTIST_MIX_EMPTY_SAME]
    ]

    private static final def IMPOSSIBLE_CASES = [
        [ARTIST_EMPTY, ALBUM_ARTIST_SAME],
        [ARTIST_EMPTY, ALBUM_ARTIST_MIX_EMPTY_SAME],
        [ARTIST_EMPTY, ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM],
        [ARTIST_EMPTY, ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM],
        [ARTIST_NON_UNIFORM, ALBUM_ARTIST_SAME],
        [ARTIST_NON_UNIFORM, ALBUM_ARTIST_MIX_EMPTY_SAME],
        [ARTIST_NON_UNIFORM, ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM],
        [ARTIST_NON_UNIFORM, ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM],
    ]

    private def fixer = new AlbumArtistFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new AlbumArtistFix(null)

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
            def tag = new ID3v1Tag()
            tag.setField(ARTIST.key, 'artist')
            mp3File.setID3v1Tag(tag)
        }

        and:
        mp3Files.each { mp3File ->
            assert mp3File.hasID3v1Tag()
            assert mp3File.getID3v1Tag().getFirst(ARTIST.key) == 'artist'
            assert !mp3File.hasID3v2Tag()
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "Output, sets album artist, and returns true when #artistCondition and #albumArtistCondition"() {

        setup:
        // sanity checks
        if (!FIX_CASES.contains([artistCondition, albumArtistCondition])) {
            throw new IllegalArgumentException("Not in to-fix combos")
        }
        if (IMPOSSIBLE_CASES.contains([artistCondition, albumArtistCondition])) {
            throw new IllegalArgumentException("Impossible combo")
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            switch (artistCondition) {
                case ARTIST_UNIFORM:
                    tag.setField(ARTIST.key, 'artist')
                    break
                default:
                    throw new IllegalArgumentException("Unexpected artist case: ${artistCondition}")
            }
            switch (albumArtistCondition) {
                case ALBUM_ARTIST_EMPTY:
                    // do nothing
                    break
                case ALBUM_ARTIST_MIX_EMPTY_SAME:
                    if (idx % 2) {
                        // do nothing
                    } else {
                        tag.setField(ALBUM_ARTIST.key, 'artist')
                    }
                    break
                default:
                    throw new IllegalArgumentException("Unexpected album artist case: ${albumArtistCondition}")
            }
            mp3File.setID3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            switch (artistCondition) {
                case ARTIST_UNIFORM:
                    assert mp3File.getID3v2Tag().getFirst(ARTIST.key) == 'artist'
                    break
                default:
                    throw new IllegalArgumentException("Unexpected artist case: ${artistCondition}")
            }
            switch (albumArtistCondition) {
                case ALBUM_ARTIST_EMPTY:
                    // do nothing
                    break
                case ALBUM_ARTIST_MIX_EMPTY_SAME:
                    if (idx % 2) {
                        assert !mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key)
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
                    }
                    break
                default:
                    throw new IllegalArgumentException("Unexpected album artist case: ${albumArtistCondition}")
            }
        }

        and:
        def mp3FilesThatShouldBeFixed = mp3Files.withIndex()
            .findAll { tuple ->
                switch (albumArtistCondition) {
                    case ALBUM_ARTIST_EMPTY:
                        true
                        break
                    case ALBUM_ARTIST_MIX_EMPTY_SAME:
                        tuple.v2 % 2
                        break
                    default:
                        throw new IllegalArgumentException("Unexpected album artist case: ${albumArtistCondition}")
                }
            }
            .collect { it.v1 }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        mp3FilesThatShouldBeFixed.each { mp3File ->
            1 * mockOutput.write(mp3File, "Wrote: ${ALBUM_ARTIST.desc}", 'artist')
        }
        0 * mockOutput._

        and:
        mp3FilesThatShouldBeFixed.every { mp3File ->
            mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
        }

        and:
        fixed

        where:
        artistCondition | albumArtistCondition
        ARTIST_UNIFORM  | ALBUM_ARTIST_EMPTY
        ARTIST_UNIFORM  | ALBUM_ARTIST_MIX_EMPTY_SAME

    }

    def "No output and returns false when #artistCondition and #albumArtistCondition"() {

        setup:
        // sanity checks
        if (FIX_CASES.contains([artistCondition, albumArtistCondition])) {
            throw new IllegalArgumentException("In to-fix combos")
        }
        if (IMPOSSIBLE_CASES.contains([artistCondition, albumArtistCondition])) {
            throw new IllegalArgumentException("Impossible combo")
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            def tag = new ID3v24Tag()
            switch (artistCondition) {
                case ARTIST_EMPTY:
                    // do nothing
                    break
                case ARTIST_UNIFORM:
                    tag.setField(ARTIST.key, 'artist')
                    break
                case ARTIST_NON_UNIFORM:
                    tag.setField(ARTIST.key, fieldVal(ARTIST, idx))
                    break
                default:
                    throw new IllegalArgumentException("Unexpected artist case: ${artistCondition}")
            }
            switch (albumArtistCondition) {
                case ALBUM_ARTIST_EMPTY:
                    // do nothing
                    break
                case ALBUM_ARTIST_SAME:
                    tag.setField(ALBUM_ARTIST.key, 'artist')
                    break
                case ALBUM_ARTIST_DIFFERENT_UNIFORM:
                    tag.setField(ALBUM_ARTIST.key, 'album_artist')
                    break
                case ALBUM_ARTIST_DIFFERENT_NON_UNIFORM:
                    tag.setField(ALBUM_ARTIST.key, fieldVal(ALBUM_ARTIST, idx))
                    break
                case ALBUM_ARTIST_MIX_EMPTY_SAME:
                    if (idx % 2) {
                        // do nothing
                    } else {
                        tag.setField(ALBUM_ARTIST.key, 'artist')
                    }
                    break
                case ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM:
                    if (idx % 2) {
                        // do nothing
                    } else {
                        tag.setField(ALBUM_ARTIST.key, 'album_artist')
                    }
                    break
                case ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM:
                    if (idx % 2) {
                        // do nothing
                    } else {
                        tag.setField(ALBUM_ARTIST.key, fieldVal(ALBUM_ARTIST, idx))
                    }
                    break
                case ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM:
                    if (idx % 2) {
                        tag.setField(ALBUM_ARTIST.key, 'artist')
                    } else {
                        tag.setField(ALBUM_ARTIST.key, 'album_artist')
                    }
                    break
                case ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM:
                    if (idx % 2) {
                        tag.setField(ALBUM_ARTIST.key, 'artist')
                    } else {
                        tag.setField(ALBUM_ARTIST.key, fieldVal(ALBUM_ARTIST, idx))
                    }
                    break
                default:
                    throw new IllegalArgumentException("Unexpected album artist case: ${albumArtistCondition}")
            }
            mp3File.setID3v2Tag(tag)
        }

        and:
        mp3Files.eachWithIndex { mp3File, idx ->
            assert mp3File.hasID3v2Tag()
            switch (artistCondition) {
                case ARTIST_EMPTY:
                    // do nothing
                    break
                case ARTIST_UNIFORM:
                    assert mp3File.getID3v2Tag().getFirst(ARTIST.key) == 'artist'
                    break
                case ARTIST_NON_UNIFORM:
                    assert mp3File.getID3v2Tag().getFirst(ARTIST.key) == fieldVal(ARTIST, idx)
                    break
                default:
                    throw new IllegalArgumentException("Unexpected artist case: ${artistCondition}")
            }
            switch (albumArtistCondition) {
                case ALBUM_ARTIST_EMPTY:
                    // do nothing
                    break
                case ALBUM_ARTIST_SAME:
                    assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
                    break
                case ALBUM_ARTIST_DIFFERENT_UNIFORM:
                    assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'album_artist'
                    break
                case ALBUM_ARTIST_DIFFERENT_NON_UNIFORM:
                    assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == fieldVal(ALBUM_ARTIST, idx)
                    break
                case ALBUM_ARTIST_MIX_EMPTY_SAME:
                    if (idx % 2) {
                        assert !mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key)
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
                    }
                    break
                case ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM:
                    if (idx % 2) {
                        assert !mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key)
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'album_artist'
                    }
                    break
                case ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM:
                    if (idx % 2) {
                        assert !mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key)
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == fieldVal(ALBUM_ARTIST, idx)
                    }
                    break
                case ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM:
                    if (idx % 2) {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'album_artist'
                    }
                    break
                case ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM:
                    if (idx % 2) {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == 'artist'
                    } else {
                        assert mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) == fieldVal(ALBUM_ARTIST, idx)
                    }
                    break
                default:
                    throw new IllegalArgumentException("Unexpected album artist case: ${albumArtistCondition}")
            }
        }

        when:
        def fixed = fixer.fix(mp3Files, dir)

        then:
        0 * mockOutput._

        and:
        !fixed

        where:
        artistCondition    | albumArtistCondition
        ARTIST_UNIFORM     | ALBUM_ARTIST_SAME
        ARTIST_UNIFORM     | ALBUM_ARTIST_DIFFERENT_UNIFORM
        ARTIST_UNIFORM     | ALBUM_ARTIST_DIFFERENT_NON_UNIFORM
        ARTIST_UNIFORM     | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM
        ARTIST_UNIFORM     | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM
        ARTIST_UNIFORM     | ALBUM_ARTIST_MIX_SAME_DIFFERENT_UNIFORM
        ARTIST_UNIFORM     | ALBUM_ARTIST_MIX_SAME_DIFFERENT_NON_UNIFORM
        ARTIST_EMPTY       | ALBUM_ARTIST_EMPTY
        ARTIST_EMPTY       | ALBUM_ARTIST_DIFFERENT_UNIFORM
        ARTIST_EMPTY       | ALBUM_ARTIST_DIFFERENT_NON_UNIFORM
        ARTIST_EMPTY       | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM
        ARTIST_EMPTY       | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM
        ARTIST_NON_UNIFORM | ALBUM_ARTIST_EMPTY
        ARTIST_NON_UNIFORM | ALBUM_ARTIST_DIFFERENT_UNIFORM
        ARTIST_NON_UNIFORM | ALBUM_ARTIST_DIFFERENT_NON_UNIFORM
        ARTIST_NON_UNIFORM | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_UNIFORM
        ARTIST_NON_UNIFORM | ALBUM_ARTIST_MIX_EMPTY_DIFFERENT_NON_UNIFORM

    }

}
