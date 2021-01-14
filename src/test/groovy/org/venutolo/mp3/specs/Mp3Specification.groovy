package org.venutolo.mp3.specs

import static org.venutolo.mp3.fields.Field.ALBUM
import static org.venutolo.mp3.fields.Field.ARTIST
import static org.venutolo.mp3.fields.Field.COMMENT
import static org.venutolo.mp3.fields.Field.GENRE
import static org.venutolo.mp3.fields.Field.TITLE
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.YEAR

import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fields.Field
import org.venutolo.mp3.output.Output
import spock.lang.Specification

class Mp3Specification extends Specification {

    protected static final Collection<Field> ID3_FIELDS = [ARTIST, ALBUM, TITLE, TRACK, YEAR, GENRE, COMMENT]
    protected static final File RESOURCE_DIR = new File('src/test/resources')

    protected static MP3File newMp3File() {
        new MP3File("${RESOURCE_DIR.path}/test.mp3")
    }

    protected def mockOutput = Mock(Output)
    protected def mp3File = newMp3File()
    protected def mp3Files = (1..4).collect { idx ->
        def mp3File = newMp3File()
        // give each file a distinct file name
        // file has to exist at call to constructor, so set file after construction
        mp3File.setFile(new File("${RESOURCE_DIR.path}/test${idx}.mp3"))
        mp3File
    }
    protected def dir = RESOURCE_DIR
    protected def jpgFile = new File("${RESOURCE_DIR.path}/test.jpg")

}
