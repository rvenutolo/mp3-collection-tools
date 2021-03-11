package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_PIXELS

import java.nio.file.Files
import org.venutolo.mp3.specs.Mp3Specification
import spock.lang.TempDir

class AlbumImageCheckSpec extends Mp3Specification {

    @TempDir
    private File tempDir

    private def checker = new AlbumImageCheck(mockOutput)

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
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'No album image')
        0 * mockOutput._

    }

    def "Output for #width x #height album JPG image"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origImageFile = new File("${RESOURCE_DIR}/images/${width}x${height}.jpg")
        def imageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        Files.copy(origImageFile.toPath(), imageFile.toPath())

        when:
        checker.check(tempDir)

        then:
        minDimensionWarn * mockOutput.write(imageFile, "Dimensions less than ${TARGET_PIXELS}", "${width}x${height}")
        largeWarn * mockOutput.write(imageFile, "Larger than ${TARGET_PIXELS}x${TARGET_PIXELS}", "${width}x${height}")
        notSquareWarn * mockOutput.write(imageFile, 'Not square', "${width}x${height}")
        0 * mockOutput._

        where:
        width | height || minDimensionWarn | largeWarn | notSquareWarn
        500   | 500    || 1                | 0         | 0
        500   | 1000   || 0                | 0         | 1
        500   | 1500   || 0                | 0         | 1
        1000  | 500    || 0                | 0         | 1
        1000  | 1000   || 0                | 0         | 0
        1000  | 1500   || 0                | 0         | 1
        1500  | 500    || 0                | 0         | 1
        1500  | 1000   || 0                | 0         | 1
        1500  | 1500   || 0                | 1         | 0

    }

    def "Output when album image is a misnamed JPG"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origImageFile = new File("${RESOURCE_DIR}/images/${TARGET_PIXELS}x${TARGET_PIXELS}.png")
        def imageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        Files.copy(origImageFile.toPath(), imageFile.toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(imageFile, 'Not expected image format', 'PNG')
        0 * mockOutput._

    }

}
