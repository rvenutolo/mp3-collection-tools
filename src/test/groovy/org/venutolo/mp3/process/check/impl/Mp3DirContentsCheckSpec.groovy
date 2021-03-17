package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME

import java.nio.file.Files
import org.venutolo.mp3.specs.Mp3Specification
import spock.lang.TempDir

class Mp3DirContentsCheckSpec extends Mp3Specification {

    @TempDir
    private File tempDir

    private def checker = new Mp3DirContentsCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new Mp3DirContentsCheck(null)

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

    def "No output when all mp3 files with lowercase file extension"() {

        setup:
        (1..3).each { idx ->
            Files.copy(mp3File.file.toPath(), new File("${tempDir}/${idx}.mp3").toPath())
        }

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "No output when all mp3 files with lowercase file extension and album image"() {

        setup:
        (1..3).each { idx ->
            Files.copy(mp3File.file.toPath(), new File("${tempDir}/${idx}.mp3").toPath())
        }
        Files.copy(jpgFile.toPath(), new File("${tempDir}/${ALBUM_IMAGE_FILENAME}").toPath())

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "Output when some mp3 files have non-lowercase file extension"() {

        setup:
        def lowercaseFile = new File("${tempDir}/lower.mp3")
        def uppercaseFile1 = new File("${tempDir}/upper1.MP3")
        def uppercaseFile2 = new File("${tempDir}/upper2.Mp3")
        Files.copy(mp3File.file.toPath(), lowercaseFile.toPath())
        Files.copy(mp3File.file.toPath(), uppercaseFile1.toPath())
        Files.copy(mp3File.file.toPath(), uppercaseFile2.toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(uppercaseFile1, 'Non-lowercase file extension')
        1 * mockOutput.write(uppercaseFile2, 'Non-lowercase file extension')
        0 * mockOutput._

    }

    def "Output when there are unexpected files"() {

        setup:
        def lowercaseFolderJpg = new File("${tempDir}/folder.jpg")
        def randomFile1 = new File("${tempDir}/foo.jpg")
        def randomFile2 = new File("${tempDir}/bar.txt")
        (1..3).each { idx ->
            Files.copy(mp3File.file.toPath(), new File("${tempDir}/${idx}.mp3").toPath())
        }
        Files.copy(jpgFile.toPath(), lowercaseFolderJpg.toPath())
        Files.copy(jpgFile.toPath(), randomFile1.toPath())
        Files.copy(jpgFile.toPath(), randomFile2.toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(lowercaseFolderJpg, 'Unexpected file')
        1 * mockOutput.write(randomFile1, 'Unexpected file')
        1 * mockOutput.write(randomFile2, 'Unexpected file')
        0 * mockOutput._

    }

}