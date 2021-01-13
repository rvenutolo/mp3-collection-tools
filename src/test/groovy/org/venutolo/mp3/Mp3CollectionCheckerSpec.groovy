package org.venutolo.mp3

import java.nio.file.Files
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.DirCheck
import org.venutolo.mp3.check.Mp3FileCheck
import org.venutolo.mp3.check.MultipleMp3FilesCheck
import org.venutolo.mp3.specs.Mp3Specification
import spock.lang.TempDir

class Mp3CollectionCheckerSpec extends Mp3Specification {

    private DirCheck mockDirCheck1 = Mock(DirCheck)
    private DirCheck mockDirCheck2 = Mock(DirCheck)
    private MultipleMp3FilesCheck mockFilesCheck1 = Mock(MultipleMp3FilesCheck)
    private MultipleMp3FilesCheck mockFilesCheck2 = Mock(MultipleMp3FilesCheck)
    private Mp3FileCheck mockFileCheck1 = Mock(Mp3FileCheck)
    private Mp3FileCheck mockFileCheck2 = Mock(Mp3FileCheck)

    private Collection<DirCheck> dirChecks = [mockDirCheck1, mockDirCheck2]
    private Collection<MultipleMp3FilesCheck> filesChecks = [mockFilesCheck1, mockFilesCheck2]
    private Collection<Mp3FileCheck> fileChecks = [mockFileCheck1, mockFileCheck2]

    private Mp3CollectionChecker checker = new Mp3CollectionChecker(dirChecks, filesChecks, fileChecks)

    @TempDir
    private File tempDir

    def "NPE when #field is null"() {

        when:
        new Mp3CollectionChecker(dirChecks, filesChecks, fileChecks)

        then:
        thrown(NullPointerException)

        where:
        field          | dirChecks | filesChecks | fileChecks
        'Dir checks'   | null      | []          | []
        'Files checks' | []        | null        | []
        'File checks'  | []        | []          | null

    }

    def "NPE when base dir is null"() {

        when:
        checker.checkCollection(null)

        then:
        thrown(NullPointerException)

    }

    def "IAE when dir is not a directory"() {

        when:
        checker.checkCollection(jpgFile)

        then:
        thrown(IllegalArgumentException)

    }

    def "Runs checks in desired order"() {

        // baseDir
        // ├── A
        // |   ├── ArtistA1
        // |   |   ├── AlbumA1a
        // |   |   |   ├── fileA1a1.mp3
        // |   |   |   └── fileA1a2.mp3
        // |   |   └── AlbumA1b
        // |   |       ├── fileA1b1.mp3
        // |   |       └── fileA1b2.mp3
        // |   └── ArtistA2
        // |       ├── AlbumA2a
        // |       |   ├── fileA2a1.mp3
        // |       |   └── fileA2a2.mp3
        // |       └── AlbumA2b
        // |           ├── fileA2b1.mp3
        // |           └── fileA2b2.mp3
        // └── B
        //     ├── ArtistB1
        //     |   ├── AlbumB1a
        //     |   |   ├── fileB1a1.mp3
        //     |   |   └── fileB1a2.mp3
        //     |   └── AlbumB1b
        //     |       ├── fileB1b1.mp3
        //     |       └── fileB1b2.mp3
        //     └── ArtistB2
        //         ├── AlbumB2a
        //         |   ├── fileB2a1.mp3
        //         |   └── fileB2a2.mp3
        //         └── AlbumB2b
        //             ├── fileB2b1.mp3
        //             └── fileB2b2.mp3

        setup:
        def baseDir = new File("${tempDir}/baseDir")
        def dirA = new File("${tempDir}/baseDir/A")
        def artistA1 = new File("${tempDir}/baseDir/A/ArtistA1")
        def albumA1a = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1a")
        def fileA1a1 = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1a/fileA1a1.mp3")
        def fileA1a2 = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1a/fileA1a2.mp3")
        def albumA1b = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1b")
        def fileA1b1 = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1b/fileA1b1.mp3")
        def fileA1b2 = new File("${tempDir}/baseDir/A/ArtistA1/AlbumA1b/fileA1b2.mp3")
        def artistA2 = new File("${tempDir}/baseDir/A/ArtistA2")
        def albumA2a = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2a")
        def fileA2a1 = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2a/fileA2a1.mp3")
        def fileA2a2 = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2a/fileA2a2.mp3")
        def albumA2b = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2b")
        def fileA2b1 = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2b/fileA2b1.mp3")
        def fileA2b2 = new File("${tempDir}/baseDir/A/ArtistA2/AlbumA2b/fileA2b2.mp3")
        def dirB = new File("${tempDir}/baseDir/B")
        def artistB1 = new File("${tempDir}/baseDir/B/ArtistB1")
        def albumB1a = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1a")
        def fileB1a1 = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1a/fileB1a1.mp3")
        def fileB1a2 = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1a/fileB1a2.mp3")
        def albumB1b = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1b")
        def fileB1b1 = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1b/fileB1b1.mp3")
        def fileB1b2 = new File("${tempDir}/baseDir/B/ArtistB1/AlbumB1b/fileB1b2.mp3")
        def artistB2 = new File("${tempDir}/baseDir/B/ArtistB2")
        def albumB2a = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2a")
        def fileB2a1 = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2a/fileB2a1.mp3")
        def fileB2a2 = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2a/fileB2a2.mp3")
        def albumB2b = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2b")
        def fileB2b1 = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2b/fileB2b1.mp3")
        def fileB2b2 = new File("${tempDir}/baseDir/B/ArtistB2/AlbumB2b/fileB2b2.mp3")
        [albumA1a, albumA1b, albumA2a, albumA2b, albumB1a, albumB1b, albumB2a, albumB2b].each { it.mkdirs() }
        [
            fileA1a1, fileA1a2, fileA1b1, fileA1b2, fileA2a1, fileA2a2, fileA2b1, fileA2b2,
            fileB1a1, fileB1a2, fileB1b1, fileB1b2, fileB2a1, fileB2a2, fileB2b1, fileB2b2
        ].each { Files.copy(mp3File.file.toPath(), it.toPath()) }

        when:
        checker.checkCollection(baseDir)

        then:
        1 * mockDirCheck1.check(baseDir)
        1 * mockDirCheck2.check(baseDir)

        then:
        1 * mockDirCheck1.check(dirA)
        1 * mockDirCheck2.check(dirA)

        then:
        1 * mockDirCheck1.check(artistA1)
        1 * mockDirCheck2.check(artistA1)

        then:
        1 * mockDirCheck1.check(albumA1a)
        1 * mockDirCheck2.check(albumA1a)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA1a1, fileA1a2] },
            albumA1a
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA1a1, fileA1a2] },
            albumA1a
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA1a1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA1a2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA1a1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA1a2 })

        then:
        1 * mockDirCheck1.check(albumA1b)
        1 * mockDirCheck2.check(albumA1b)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA1b1, fileA1b2] },
            albumA1b
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA1b1, fileA1b2] },
            albumA1b
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA1b1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA1b2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA1b1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA1b2 })

        then:
        1 * mockDirCheck1.check(artistA2)
        1 * mockDirCheck2.check(artistA2)

        then:
        1 * mockDirCheck1.check(albumA2a)
        1 * mockDirCheck2.check(albumA2a)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA2a1, fileA2a2] },
            albumA2a
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA2a1, fileA2a2] },
            albumA2a
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA2a1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA2a2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA2a1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA2a2 })

        then:
        1 * mockDirCheck1.check(albumA2b)
        1 * mockDirCheck2.check(albumA2b)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA2b1, fileA2b2] },
            albumA2b
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileA2b1, fileA2b2] },
            albumA2b
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA2b1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileA2b2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA2b1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileA2b2 })

        then:
        1 * mockDirCheck1.check(dirB)
        1 * mockDirCheck2.check(dirB)

        then:
        1 * mockDirCheck1.check(artistB1)
        1 * mockDirCheck2.check(artistB1)

        then:
        1 * mockDirCheck1.check(albumB1a)
        1 * mockDirCheck2.check(albumB1a)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB1a1, fileB1a2] },
            albumB1a
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB1a1, fileB1a2] },
            albumB1a
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB1a1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB1a2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB1a1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB1a2 })

        then:
        1 * mockDirCheck1.check(albumB1b)
        1 * mockDirCheck2.check(albumB1b)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB1b1, fileB1b2] },
            albumB1b
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB1b1, fileB1b2] },
            albumB1b
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB1b1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB1b2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB1b1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB1b2 })

        then:
        1 * mockDirCheck1.check(artistB2)
        1 * mockDirCheck2.check(artistB2)

        then:
        1 * mockDirCheck1.check(albumB2a)
        1 * mockDirCheck2.check(albumB2a)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB2a1, fileB2a2] },
            albumB2a
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB2a1, fileB2a2] },
            albumB2a
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB2a1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB2a2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB2a1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB2a2 })

        then:
        1 * mockDirCheck1.check(albumB2b)
        1 * mockDirCheck2.check(albumB2b)

        then:
        1 * mockFilesCheck1.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB2b1, fileB2b2] },
            albumB2b
        )
        1 * mockFilesCheck2.check(
            { Collection<MP3File> mp3Files -> mp3Files.collect { it.file } == [fileB2b1, fileB2b2] },
            albumB2b
        )

        then:
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB2b1 })
        1 * mockFileCheck1.check({ MP3File mp3File -> mp3File.file == fileB2b2 })

        then:
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB2b1 })
        1 * mockFileCheck2.check({ MP3File mp3File -> mp3File.file == fileB2b2 })

        then:
        0 * mockDirCheck1._
        0 * mockDirCheck2._
        0 * mockFilesCheck1._
        0 * mockFilesCheck2._
        0 * mockFileCheck1._
        0 * mockFileCheck2._

    }

}
