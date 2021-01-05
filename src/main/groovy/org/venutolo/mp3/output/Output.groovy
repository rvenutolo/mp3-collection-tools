package org.venutolo.mp3.output

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

class Output {

    void write(@Nonnull final MP3File mp3File, @Nonnull final String message) {
        write(mp3File.file, message)
    }

    void write(@Nonnull final MP3File mp3File, @Nonnull final String message, @Nonnull final String details) {
        write(mp3File.file, message, details)
    }

    void write(@Nonnull final File file, @Nonnull final String message) {
        println("${file.canonicalPath}: ${message}")
    }

    void write(@Nonnull final File file, @Nonnull final String message, @Nonnull final String details) {
        println("${file.canonicalPath}: ${message} (${details})")
    }

}