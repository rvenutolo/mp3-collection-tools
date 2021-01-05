package org.venutolo.mp3

import javax.annotation.Nonnull
import org.venutolo.mp3.check.AlbumImageCheck
import org.venutolo.mp3.check.ExtraneousFieldsCheck
import org.venutolo.mp3.check.GenericDirContentsCheck
import org.venutolo.mp3.check.GenreFieldsCheck
import org.venutolo.mp3.check.MissingTrackCheck
import org.venutolo.mp3.check.Mp3DirContentsCheck
import org.venutolo.mp3.check.OverlappingTrackCheck
import org.venutolo.mp3.check.RequiredFieldsCheck
import org.venutolo.mp3.check.SameFieldValueCheck
import org.venutolo.mp3.check.TagTypeCheck
import org.venutolo.mp3.check.TrackFieldsCheck
import org.venutolo.mp3.check.TrackTotalCheck
import org.venutolo.mp3.check.YearFieldCheck
import org.venutolo.mp3.output.Output

class CheckMain {

    static void main(@Nonnull final String... args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Expected one argument: ${args}")
        }
        runCollectionCheck(new File(args[0]))
    }

    static void runCollectionCheck(@Nonnull final File baseDir) {
        def output = new Output()
        def dirChecks = [
            new GenericDirContentsCheck(output),
            new Mp3DirContentsCheck(output),
            new AlbumImageCheck(output)
        ]
        def filesChecks = [
            new SameFieldValueCheck(output),
            new MissingTrackCheck(output),
            new OverlappingTrackCheck(output),
            new TrackTotalCheck(output)
        ]
        def fileChecks = [
            new TagTypeCheck(output),
            new RequiredFieldsCheck(output),
            new ExtraneousFieldsCheck(output),
            new GenreFieldsCheck(output),
            new TrackFieldsCheck(output),
            new YearFieldCheck(output)
        ]
        def collectionChecker = new Mp3CollectionChecker(dirChecks, filesChecks, fileChecks)
        collectionChecker.checkCollection(baseDir)
    }

}

