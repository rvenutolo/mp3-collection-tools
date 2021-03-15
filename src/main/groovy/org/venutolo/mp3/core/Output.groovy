package org.venutolo.mp3.core

import javax.annotation.Nonnull

class Output {

    void write(@Nonnull final Mp3File mp3File, @Nonnull final String message) {
        write(mp3File.file, message)
    }

    void write(@Nonnull final Mp3File mp3File, @Nonnull final String message, @Nonnull final String details) {
        write(mp3File.file, message, details)
    }

    void write(@Nonnull final File file, @Nonnull final String message) {
        println("${file.canonicalPath}: ${message}")
    }

    void write(@Nonnull final File file, @Nonnull final String message, @Nonnull final String details) {
        println("${file.canonicalPath}: ${message} (${details})")
    }

}