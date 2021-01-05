package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_IMAGE_DIMENSION

import java.nio.file.Files
import org.venutolo.mp3.specs.CheckSpecification
import spock.lang.TempDir

class AlbumImageCheckSpec extends CheckSpecification {

    @TempDir
    private File tempDir

    private def checker = new AlbumImageCheck(mockOutput)

    def "NPE when WarningOutput is null"() {

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

    def "No warning when directory is empty"() {

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "Warning when no album image"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'No album image')
        0 * mockOutput._

    }

    def "Produces expected warnings for #width x #height album image"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origImageFile = new File("${RESOURCE_DIR}/images/${width}x${height}.jpg")
        def imageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        Files.copy(origImageFile.toPath(), imageFile.toPath())

        when:
        checker.check(tempDir)

        then:
        minDimensionWarn *
            mockOutput.write(imageFile, "Dimensions less than ${TARGET_IMAGE_DIMENSION}", "${width}x${height}")
        largeWarn * mockOutput.
            write(imageFile, "Larger than ${TARGET_IMAGE_DIMENSION}x${TARGET_IMAGE_DIMENSION}", "${width}x${height}")
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

}
