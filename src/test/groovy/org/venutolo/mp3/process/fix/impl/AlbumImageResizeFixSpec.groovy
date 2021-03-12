package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_PIXELS

import java.nio.file.Files
import org.venutolo.mp3.process.util.ImageUtil
import org.venutolo.mp3.specs.Mp3Specification
import spock.lang.TempDir

class AlbumImageResizeFixSpec extends Mp3Specification {

    @TempDir
    private File tempDir

    private def fixer = new AlbumImageResizeFix(mockOutput)

    def "NPE when output is null"() {

        when:
        new AlbumImageFilenameFix(null)

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
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when album image not named Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origImageFile = new File("${RESOURCE_DIR}/images/1500x1500.png")
        def imageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME.replace('jpg', 'png')}")
        Files.copy(origImageFile.toPath(), imageFile.toPath())

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output and returns false when dir contains too small square #dimension x #dimension Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, dimension, dimension)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == dimension
        readImage.height == dimension

        where:
        dimension << [900, 999, 1000]

    }

    def "Output, returns false, and resizes image when dir contains large enough square #dimension x #dimension Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, dimension, dimension)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        1 * mockOutput.write(albumImageFile, 'Resized image', "${TARGET_PIXELS}x${TARGET_PIXELS}")

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == TARGET_PIXELS
        readImage.height == TARGET_PIXELS

        where:
        dimension << [1001, 1002, 1500]

    }

    def "No output and returns false when dir contains too small roughly-square #width x #height Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, width, height)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == width
        readImage.height == height

        where:
        width | height
        500   | 501
        501   | 500
        900   | 901
        901   | 900
        999   | 1000
        1000  | 999

    }

    def "No output, returns false, and resizes image when dir contains large enough roughly-square #width x #height Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, width, height)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        1 * mockOutput.write(albumImageFile, 'Resized image', "${TARGET_PIXELS}x${TARGET_PIXELS}")

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == TARGET_PIXELS
        readImage.height == TARGET_PIXELS

        where:
        width | height
        1000  | 1001
        1001  | 1000
        1000  | 1020
        1020  | 1000
        2000  | 2001
        2001  | 2000
        2000  | 2040
        2040  | 2000

    }

    def "No output and returns false when dir contains too small non-square #width x #height Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, width, height)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == width
        readImage.height == height

        where:
        width | height
        500   | 550
        550   | 500
        900   | 950
        950   | 900
        979   | 1000
        1000  | 979

    }

    def "No output, returns false, and resizes image when dir contains large enough non-square #width x #height Folder.jpg"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())
        def origSizeImage = ImageUtil.readImage(new File("${RESOURCE_DIR}/images/1500x1500.jpg"))
        def resizedImage = fastResizeImage(origSizeImage, width, height)
        def albumImageFile = new File("${tempDir}/${ALBUM_IMAGE_FILENAME}")
        ImageUtil.writeImage(resizedImage, albumImageFile)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        def readImage = ImageUtil.readImage(albumImageFile)
        readImage.width == width
        readImage.height == height

        where:
        width | height
        1000  | 1021
        1021  | 1000
        2000  | 2041
        2041  | 2000

    }

}
