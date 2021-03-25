package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.core.Constants.TARGET_PIXELS

import org.venutolo.mp3.Mp3Specification
import org.venutolo.mp3.TempDirFileCopyUtil
import spock.lang.TempDir

class AlbumImageCheckSpec extends Mp3Specification {

    @TempDir
    private File tempDir
    private TempDirFileCopyUtil copyUtil
    private AlbumImageCheck checker = new AlbumImageCheck(mockOutput)

    def setup() {
        copyUtil = new TempDirFileCopyUtil(tempDir)
    }

    def "NPE when output is null"() {

        when:
        new AlbumImageCheck(null)

        then:
        thrown(NullPointerException)

    }

    def "NPE when dir is null"() {

        when:
        checker.check(null)

        then:
        thrown(NullPointerException)

    }

    def "IAE when dir is not a directory"() {

        when:
        checker.check(newMp3File().file)

        then:
        thrown(IllegalArgumentException)

    }

    def "No output when directory is empty"() {

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "Output when no album image"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'No album image')
        0 * mockOutput._

    }

    def "Output for #width x #height album JPG image"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')
        def imageFile = copyUtil.copyResized(JPG_FILE, ALBUM_IMAGE_FILENAME, width, height)

        when:
        checker.check(tempDir)

        then:
        minDimensionWarn * mockOutput.write(imageFile, "Dimensions less than ${TARGET_PIXELS}", "${width}x${height}")
        largeWarn * mockOutput.write(imageFile, "Larger than ${TARGET_PIXELS}x${TARGET_PIXELS}", "${width}x${height}")
        notSquareWarn * mockOutput.write(imageFile, 'Not square', "${width}x${height}")
        0 * mockOutput._

        where:
        width | height || minDimensionWarn | largeWarn | notSquareWarn
        999   | 999    || 1                | 0         | 0
        999   | 1000   || 0                | 0         | 1
        999   | 1001   || 0                | 0         | 1
        1000  | 999    || 0                | 0         | 1
        1000  | 1000   || 0                | 0         | 0
        1000  | 1001   || 0                | 0         | 1
        1001  | 999    || 0                | 0         | 1
        1001  | 1000   || 0                | 0         | 1
        1001  | 1001   || 0                | 1         | 0

    }

    def "Output when album image is a misnamed JPG"() {

        setup:
        copyUtil.copy(mp3File, 'file.mp3')
        def imageFile = copyUtil.copy(PNG_FILE, ALBUM_IMAGE_FILENAME)

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(imageFile, 'Not expected image format', 'PNG')
        0 * mockOutput._

    }

}
