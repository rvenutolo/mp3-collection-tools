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
import org.venutolo.mp3.output.WarningOutput

class Main {

    static void main(@Nonnull final String... args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Expected one argument: ${args}")
        }
        runCollectionCheck(new File(args[0]))
    }

    static void runCollectionCheck(@Nonnull final File baseDir) {
        def warningsOutput = new WarningOutput()
        def dirChecks = [
            new GenericDirContentsCheck(warningsOutput),
            new Mp3DirContentsCheck(warningsOutput),
            new AlbumImageCheck(warningsOutput)
        ]
        def filesChecks = [
            new SameFieldValueCheck(warningsOutput),
            new MissingTrackCheck(warningsOutput),
            new OverlappingTrackCheck(warningsOutput),
            new TrackTotalCheck(warningsOutput)
        ]
        def fileChecks = [
            new TagTypeCheck(warningsOutput),
            new RequiredFieldsCheck(warningsOutput),
            new ExtraneousFieldsCheck(warningsOutput),
            new GenreFieldsCheck(warningsOutput),
            new TrackFieldsCheck(warningsOutput),
            new YearFieldCheck(warningsOutput)
        ]
        def collectionChecker = new Mp3CollectionChecker(dirChecks, filesChecks, fileChecks)
        collectionChecker.checkCollection(baseDir)
    }

}

