package org.venutolo.mp3.specs

import static org.venutolo.mp3.Field.DISC_NO
import static org.venutolo.mp3.Field.DISC_TOTAL
import static org.venutolo.mp3.Field.RATING
import static org.venutolo.mp3.Field.TRACK
import static org.venutolo.mp3.Field.TRACK_TOTAL

import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Field
import org.venutolo.mp3.Output
import spock.lang.Specification

class Mp3Specification extends Specification {

    // these are the fields which won't accept non-numeric values
    protected static final Set<Field> NUMERIC_FIELDS = [TRACK, TRACK_TOTAL, DISC_NO, DISC_TOTAL, RATING]

    protected static final int NUM_MP3_FILES = 4

    protected static final File RESOURCE_DIR = new File('src/test/resources')

    protected static MP3File newMp3File() {
        new MP3File("${RESOURCE_DIR.path}/test.mp3")
    }

    // This uses a faster filter than the similar resize method in ImageUtil
    // as I don't care about quality during tests
    protected BufferedImage fastResizeImage(@Nonnull final BufferedImage image, final int width, final int height) {
        new ResampleOp(width, height)
            .tap { setFilter(ResampleFilters.boxFilter) }
            .filter(image, null)
    }

    protected static String fieldVal(@Nonnull final Field field) {
        field in NUMERIC_FIELDS ? '1' : "${field.toString().toLowerCase()}"
    }

    protected static String fieldVal(@Nonnull final Field field, final int idx) {
        field in NUMERIC_FIELDS ? (idx + 1) as String : "${field.toString().toLowerCase()}_${idx + 1}"
    }

    protected def mockOutput = Mock(Output)
    protected def mp3File = newMp3File()
    protected def mp3Files = (0..(NUM_MP3_FILES - 1)).collect { idx ->
        def mp3File = newMp3File()
        // give each file a distinct file name
        // file has to exist at call to constructor, so set file after construction
        mp3File.setFile(new File("${RESOURCE_DIR.path}/test${idx + 1}.mp3"))
        mp3File
    }
    protected def dir = RESOURCE_DIR
    protected def jpgFile = new File("${RESOURCE_DIR.path}/test.jpg")

}
