package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.ALBUM
import static org.venutolo.mp3.fields.Field.ARTIST
import static org.venutolo.mp3.fields.Field.COMMENT
import static org.venutolo.mp3.fields.Field.GENRE
import static org.venutolo.mp3.fields.Field.TITLE
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.YEAR

import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fields.Field
import org.venutolo.mp3.output.WarningOutput
import spock.lang.Specification

class CheckSpecification extends Specification {

    protected static final Collection<Field> ID3_FIELDS = [ARTIST, ALBUM, TITLE, TRACK, YEAR, GENRE, COMMENT]
    protected def mockWarnings = Mock(WarningOutput)
    protected def mp3File = new MP3File('src/test/resources/test.mp3')
    protected def artFile = new File('src/test/resources/test_cover.jpg')

}
