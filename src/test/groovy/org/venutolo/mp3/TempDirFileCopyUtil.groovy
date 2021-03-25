package org.venutolo.mp3

import static java.util.Objects.requireNonNull

import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import groovy.util.logging.Slf4j
import java.nio.file.Files
import javax.annotation.Nonnull
import javax.imageio.ImageIO
import org.venutolo.mp3.core.Mp3File

@Slf4j
class TempDirFileCopyUtil {

    @Nonnull
    private final File tempDir

    TempDirFileCopyUtil(@Nonnull final File tempDir) {
        requireNonNull(tempDir, 'Temp dir is null')
        if (!tempDir.exists()) {
            throw new IllegalArgumentException('Temp dir does not exist')
        }
        this.tempDir = tempDir
    }

    @Nonnull
    File copy(@Nonnull final Mp3File mp3File, @Nonnull final String destName) {
        copy(mp3File.file, destName)
    }

    @Nonnull
    File copy(@Nonnull final File srcFile, @Nonnull final String destName) {
        def destFile = new File(tempDir, destName)
        log.debug("Copying from: {} to: {}", srcFile, destFile)
        Files.copy(srcFile.toPath(), destFile.toPath())
        log.debug("Copied from: {} to: {}", srcFile, destFile)
        destFile
    }

    @Nonnull
    File copyResized(@Nonnull final File srcFile, @Nonnull final String destName, final int width, final int height) {
        def destFile = new File(tempDir, destName)
        log.debug("Resizing ({}x{}) and copying from: {} to: {}", width, height, srcFile, destFile)
        def srcImage = ImageIO.read(srcFile)
        def resizedImage = new ResampleOp(width, height)
            .tap { setFilter(ResampleFilters.boxFilter) }
            .filter(srcImage, null)
        def destFormat = destName.substring(destName.lastIndexOf('.') + 1)
        ImageIO.write(resizedImage, destFormat, destFile)
        log.debug("Resized ({}x{}) and copied from: {} to: {}", width, height, srcFile, destFile)
        destFile
    }

}
