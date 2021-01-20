package org.venutolo.mp3.process.check.impl

import java.nio.file.Files
import org.venutolo.mp3.specs.Mp3Specification
import spock.lang.TempDir

class GenericDirContentsCheckSpec extends Mp3Specification {

    @TempDir
    private File tempDir

    private def checker = new GenericDirContentsCheck(mockOutput)

    def "NPE when output is null"() {

        when:
        new GenericDirContentsCheck(null)

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

    def "Output when directory is empty"() {

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'Empty directory')
        0 * mockOutput._

    }

    def "Output when dir contains both directories and files"() {

        setup:
        new File("${tempDir}/dir").mkdir()
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'Contains both directories and files')
        0 * mockOutput._

    }

    def "No output when dir contains only directories"() {

        setup:
        new File("${tempDir}/dir1").mkdir()
        new File("${tempDir}/dir2").mkdir()

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "No output when dir contains only MP3 files"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file1.mp3").toPath())
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file2.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        0 * mockOutput._

    }

    def "No output when dir contains files but no MP3 files"() {

        setup:
        Files.copy(jpgFile.toPath(), new File("${tempDir}/file.jpg").toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockOutput.write(tempDir, 'Contains no MP3 files')

    }

}