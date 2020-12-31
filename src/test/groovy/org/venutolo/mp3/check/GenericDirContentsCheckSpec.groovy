package org.venutolo.mp3.check

import java.nio.file.Files
import spock.lang.TempDir

class GenericDirContentsCheckSpec extends CheckSpecification {

    @TempDir
    private File tempDir

    private def checker = new GenericDirContentsCheck(mockWarnings)

    def "NPE when WarningOutput is null"() {

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

    def "Warning when dir is empty"() {

        when:
        checker.check(tempDir)

        then:
        1 * mockWarnings.write(tempDir, 'Empty directory')
        0 * mockWarnings._

    }

    def "Warning when dir contains both directories and files"() {

        setup:
        new File("${tempDir}/dir").mkdir()
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        1 * mockWarnings.write(tempDir, 'Contains both directories and files')
        0 * mockWarnings._

    }

    def "No warning when dir contains only directories"() {

        setup:
        new File("${tempDir}/dir1").mkdir()
        new File("${tempDir}/dir2").mkdir()

        when:
        checker.check(tempDir)

        then:
        0 * mockWarnings._

    }

    def "No warning when dir contains only files"() {

        setup:
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file1.mp3").toPath())
        Files.copy(mp3File.file.toPath(), new File("${tempDir}/file2.mp3").toPath())

        when:
        checker.check(tempDir)

        then:
        0 * mockWarnings._

    }

}