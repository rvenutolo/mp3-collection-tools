package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FORMAT
import static org.venutolo.mp3.process.util.ImageUtil.getImageFormat

import org.venutolo.mp3.specs.Mp3Specification
import org.venutolo.mp3.specs.TempDirFileCopyUtil
import spock.lang.TempDir

class AlbumImageFormatFixSpec extends Mp3Specification {

    @TempDir
    private File tempDir
    private TempDirFileCopyUtil copyUtil
    private AlbumImageFormatFix fixer = new AlbumImageFormatFix(mockOutput)

    def setup() {
        copyUtil = new TempDirFileCopyUtil(tempDir)
    }

    def "NPE when output is null"() {

        when:
        new AlbumImageFormatFix(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when dir is null"() {

        when:
        fixer.fix(null)

        then:
        thrown(NullPointerException)

    }

    def "IAE when dir is not a directory"() {

        when:
        fixer.fix(newMp3File().file)

        then:
        thrown(IllegalArgumentException)

    }

    def "No output and returns false when dir is empty"() {

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when dir has JPG format Folder.jpg"() {

        setup:
        copyUtil.copy(JPG_FILE, ALBUM_IMAGE_FILENAME)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "Output, returns false, and reformats image when dir has non-JPG Folder.jpg"() {

        setup:
        def imageFile = copyUtil.copy(PNG_FILE, ALBUM_IMAGE_FILENAME)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        1 * mockOutput.write(imageFile, "Wrote ${ALBUM_IMAGE_FORMAT}")
        0 * mockOutput._

        and:
        !fixed

        and:
        def imageFormat = getImageFormat(imageFile).get()
        imageFormat == ALBUM_IMAGE_FORMAT

    }

}
