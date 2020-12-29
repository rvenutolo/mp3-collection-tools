package org.venutolo.mp3.output

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

interface WarningOutput {

    void write(@Nonnull final MP3File file, @Nonnull final String message)

    void write(@Nonnull final MP3File file, @Nonnull final String message, @Nonnull final String details)

    void write(@Nonnull final File file, @Nonnull final String message)

    void write(@Nonnull final File file, @Nonnull final String message, @Nonnull final String details)

}