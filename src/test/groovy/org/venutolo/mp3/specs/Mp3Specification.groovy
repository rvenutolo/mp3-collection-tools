package org.venutolo.mp3.specs

import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import spock.lang.Specification

class Mp3Specification extends Specification {

    protected static final int NUM_MP3_FILES = 4

    protected static final File RESOURCE_DIR = new File('src/test/resources')

    protected static Mp3File newMp3File() {
        new Mp3File("${RESOURCE_DIR.path}/test.mp3")
    }

    // This uses a faster filter than the similar resize method in ImageUtil
    // as I don't care about quality during tests
    protected BufferedImage fastResizeImage(@Nonnull final BufferedImage image, final int width, final int height) {
        new ResampleOp(width, height)
            .tap { setFilter(ResampleFilters.boxFilter) }
            .filter(image, null)
    }

    protected static String fieldVal(@Nonnull final Field field) {
        field.isNumeric ? '1' : "${field.toString().toLowerCase()}"
    }

    protected static String fieldVal(@Nonnull final Field field, final int idx) {
        field.isNumeric ? (idx + 1) as String : "${field.toString().toLowerCase()}_${idx + 1}"
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
