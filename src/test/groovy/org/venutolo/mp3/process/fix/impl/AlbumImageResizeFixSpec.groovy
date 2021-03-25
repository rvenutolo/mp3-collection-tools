package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.core.Constants.TARGET_PIXELS
import static org.venutolo.mp3.process.util.ImageUtil.readImage

import org.venutolo.mp3.Mp3Specification
import org.venutolo.mp3.TempDirFileCopyUtil
import spock.lang.TempDir

class AlbumImageResizeFixSpec extends Mp3Specification {

    @TempDir
    private File tempDir
    private TempDirFileCopyUtil copyUtil
    private AlbumImageResizeFix fixer = new AlbumImageResizeFix(mockOutput)

    def setup() {
        copyUtil = new TempDirFileCopyUtil(tempDir)
    }

    def "NPE when output is null"() {

        when:
        new AlbumImageResizeFix(null)

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

    def "No output and returns false when no album image"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when album image not named Folder.jpg"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')
        copyUtil.copy(PNG_FILE, ALBUM_IMAGE_FILENAME.replace('jpg', 'png'))

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output, returns false, and does not resize image when image is #width x #height (#desc)"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')
        def albumImageFile = copyUtil.copyResized(JPG_FILE, ALBUM_IMAGE_FILENAME, width, height)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = readImage(albumImageFile).get()
        readImage.width == width
        readImage.height == height

        where:
        width | height | desc
        999   | 999    | 'too small square'
        1000  | 1000   | 'target dimensions'
        998   | 999    | 'too small roughly-square'
        999   | 998    | 'too small roughly-square'
        999   | 1000   | 'too small roughly-square'
        1000  | 999    | 'too small roughly-square'
        979   | 999    | 'too small non-square'
        999   | 979    | 'too small non-square'
        979   | 1000   | 'too small non-square'
        1000  | 979    | 'too small non-square'
        1000  | 1021   | 'large enough non-square'
        1021  | 1000   | 'large enough non-square'

    }

    def "No output, returns false, and resizes image when image is #width x #height (#desc)"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')
        def albumImageFile = copyUtil.copyResized(JPG_FILE, ALBUM_IMAGE_FILENAME, width, height)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        1 * mockOutput.write(albumImageFile, 'Resized image', "${TARGET_PIXELS}x${TARGET_PIXELS}")
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = readImage(albumImageFile).get()
        readImage.width == TARGET_PIXELS
        readImage.height == TARGET_PIXELS

        where:
        width | height | desc
        1001  | 1001   | 'large enough square'
        1002  | 1002   | 'large enough square'
        1500  | 1500   | 'large enough square'
        1000  | 1001   | 'large enough roughly-square'
        1001  | 1000   | 'large enough roughly-square'
        1000  | 1020   | 'large enough roughly-square'
        1020  | 1000   | 'large enough roughly-square'
        2000  | 2001   | 'large enough roughly-square'
        2001  | 2000   | 'large enough roughly-square'
        2000  | 2040   | 'large enough roughly-square'
        2040  | 2000   | 'large enough roughly-square'

    }

}
