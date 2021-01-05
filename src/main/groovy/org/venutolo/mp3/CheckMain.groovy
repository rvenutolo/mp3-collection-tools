package org.venutolo.mp3

import javax.annotation.Nonnull
import org.venutolo.mp3.check.impl.AlbumImageCheck
import org.venutolo.mp3.check.impl.ExtraneousFieldsCheck
import org.venutolo.mp3.check.impl.GenericDirContentsCheck
import org.venutolo.mp3.check.impl.GenreFieldsCheck
import org.venutolo.mp3.check.impl.MissingTrackCheck
import org.venutolo.mp3.check.impl.Mp3DirContentsCheck
import org.venutolo.mp3.check.impl.OverlappingTrackCheck
import org.venutolo.mp3.check.impl.RequiredFieldsCheck
import org.venutolo.mp3.check.impl.SameFieldValueCheck
import org.venutolo.mp3.check.impl.TagTypeCheck
import org.venutolo.mp3.check.impl.TrackFieldsCheck
import org.venutolo.mp3.check.impl.TrackTotalCheck
import org.venutolo.mp3.check.impl.YearFieldCheck
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

