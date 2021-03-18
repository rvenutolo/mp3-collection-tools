package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME

import org.venutolo.mp3.specs.Mp3Specification
import org.venutolo.mp3.specs.TempDirFileCopyUtil
import spock.lang.TempDir

class AlbumImageFilenameFixSpec extends Mp3Specification {

    @TempDir
    private File tempDir
    private def copyUtil
    private def fixer = new AlbumImageFilenameFix(mockOutput)

    def setup() {
        copyUtil = new TempDirFileCopyUtil(tempDir)
    }

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

    def "No output and returns false when dir has Folder.jpg"() {

        setup:
        copyUtil.copy(jpgFile, ALBUM_IMAGE_FILENAME)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

    }

    def "No output, returns false, and does not rename files when dir has Folder.jpg and #filename"() {

        setup:
        copyUtil.copy(jpgFile, ALBUM_IMAGE_FILENAME)
        copyUtil.copy(jpgFile, filename)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        0 * mockOutput._

        and:
        !fixed

        and:
        tempDir.listFiles().collect { File file -> file.name }.toSet() == ['Folder.jpg', filename] as Set

        where:
        filename << ['folder.jpg', 'FOLDER.JPG', 'folder.jpeg', 'Folder.jpeg', 'FOLDER.JPEG']

    }

    def "Output, returns false, and renames file when dir has single misnamed file (#filename)"() {

        setup:
        def badFile = copyUtil.copy(jpgFile, filename)

        when:
        def fixed = fixer.fix(tempDir)

        then:
        1 * mockOutput.write(badFile, 'Renamed', ALBUM_IMAGE_FILENAME)

        and:
        !fixed

        and:
        tempDir.listFiles().collect { File file -> file.name } == ['Folder.jpg']

        where:
        filename << ['folder.jpg', 'FOLDER.JPG', 'folder.jpeg', 'Folder.jpeg', 'FOLDER.JPEG']

    }

}
