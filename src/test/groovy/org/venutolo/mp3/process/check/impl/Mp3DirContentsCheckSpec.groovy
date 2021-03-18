package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME

import org.venutolo.mp3.specs.Mp3Specification
import org.venutolo.mp3.specs.TempDirFileCopyUtil
import spock.lang.TempDir

class Mp3DirContentsCheckSpec extends Mp3Specification {

    @TempDir
    private File tempDir
    private TempDirFileCopyUtil copyUtil
    private Mp3DirContentsCheck checker = new Mp3DirContentsCheck(mockOutput)

    def setup() {
        copyUtil = new TempDirFileCopyUtil(tempDir)
    }

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
            copyUtil.copy(mp3File.file, "${idx}.mp3")
        }

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "No output when all mp3 files with lowercase file extension and album image"() {

        setup:
        (1..3).each { idx ->
            copyUtil.copy(mp3File.file, "${idx}.mp3")
        }
        copyUtil.copy(jpgFile, ALBUM_IMAGE_FILENAME)

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "Output when some mp3 files have non-lowercase file extension"() {

        setup:
        copyUtil.copy(mp3File.file, 'lower.mp3')
        def uppercaseFile1 = copyUtil.copy(mp3File.file, 'upper1.MP3')
        def uppercaseFile2 = copyUtil.copy(mp3File.file, 'upper2.Mp3')

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(uppercaseFile1, 'Non-lowercase file extension')
        1 * mockOutput.write(uppercaseFile2, 'Non-lowercase file extension')
        0 * mockOutput._

    }

    def "Output when there are unexpected files"() {

        setup:
        (1..3).each { idx ->
            copyUtil.copy(mp3File.file, "${idx}.mp3")
        }
        def lowercaseFolderJpg = copyUtil.copy(jpgFile, 'folder.jpg')
        def randomFile1 = copyUtil.copy(jpgFile, 'foo.jpg')
        def randomFile2 = copyUtil.copy(jpgFile, 'bar.txt')

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(lowercaseFolderJpg, 'Unexpected file')
        1 * mockOutput.write(randomFile1, 'Unexpected file')
        1 * mockOutput.write(randomFile2, 'Unexpected file')
        0 * mockOutput._

    }

}