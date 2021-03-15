package org.venutolo.mp3.process.util

import static java.util.Objects.requireNonNull

import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.imageio.ImageIO
import org.venutolo.mp3.core.Output

@Slf4j
class ImageUtil {

    // In this class, if the method is passed an Output instance, that indicates
    // that the method is being called from check/fix code and no exceptions
    // should be thrown. The methods should catch exceptions, write to the
    // Output, and return a value.
    // If the method is not passed an Output instance, that indicates that the
    // method is being called from a test and thrown exceptions are fine

    @Nonnull
    static Optional<BufferedImage> readImage(@Nonnull final File file, @Nullable final Output output = null) {
        requireNonNull(file, "Image file cannot be null")
        try {
            def image = ImageIO.read(file)
            Optional.of(image)
        } catch (final Exception e) {
            log.warn("Exception when reading image: ${file}", e)
            if (output) {
                output.write(file, 'Exception when reading image', e.message)
                return Optional.empty()
            }
            throw e
        }
    }

    @Nonnull
    static Optional<String> getImageFormat(@Nonnull final File file, @Nullable final Output output = null) {
        requireNonNull(file, "Image file cannot be null")
        try {
            ImageIO.createImageInputStream(file).withCloseable { imageInputStream ->
                def imageFormat = ImageIO.getImageReaders(imageInputStream).next().getFormatName().toUpperCase()
                Optional.of(imageFormat)
            }
        } catch (final Exception e) {
            log.warn("Exception when reading image format: ${file}", e)
            if (output) {
                output.write(file, 'Exception when getting image format', e.message)
                return Optional.empty()
            }
            throw e
        }
    }

    @Nonnull
    static Optional<BufferedImage> resizeImage(
        @Nonnull final File file, final int width, final int height, @Nullable final Output output = null
    ) {
        def imageOptional = readImage(file)
        if (!imageOptional.isPresent()) {
            return Optional.empty()
        }
        def image = imageOptional.get()
        try {
            def resizedImage = new ResampleOp(width, height)
                .tap { setFilter(ResampleFilters.lanczos3Filter) }
                .filter(image, null)
            Optional.of(resizedImage)
        } catch (final Exception e) {
            log.warn("Exception when resizing image", e)
            if (output) {
                output.write(file, 'Exception when resizing image', e.message)
                return Optional.empty()
            }
            throw e
        }
    }

    static boolean writeImage(
        @Nonnull final BufferedImage image, @Nonnull final File file, @Nullable final Output output = null
    ) {
        requireNonNull(image, "Image cannot be null")
        requireNonNull(file, "Target file cannot be null")
        try {
            ImageIO.write(image, "jpg", file)
            true
        } catch (final Exception e) {
            log.warn("Exception when writing image: ${file}", e)
            if (output) {
                output.write(file, 'Exception when writing image', e.message)
                return false
            }
            throw e
        }
    }

}