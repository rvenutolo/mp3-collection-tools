package org.venutolo.mp3.process.util

import static java.util.Objects.requireNonNull

import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.imageio.ImageIO

@Slf4j
class ImageUtil {

    static BufferedImage readImage(@Nonnull final File file) {
        requireNonNull(file, "Image file cannot be null")
        try {
            ImageIO.read(file)
        } catch (final Exception e) {
            log.warn("Exception when reading image: ${file}", e)
            throw e
        }
    }

    static String getImageFormat(@Nonnull final File file) {
        requireNonNull(file, "Image file cannot be null")
        try {
            ImageIO.createImageInputStream(file).withCloseable { imageInputStream ->
                ImageIO.getImageReaders(imageInputStream).next().getFormatName().toUpperCase()
            }
        } catch (final Exception e) {
            log.warn("Exception when reading image format: ${file}", e)
            throw e
        }
    }

    static BufferedImage resizeImage(@Nonnull final BufferedImage image, final int width, final int height) {
        requireNonNull(image, "Image cannot be null")
        try {
            new ResampleOp(width, height)
                .tap { setFilter(ResampleFilters.lanczos3Filter) }
                .filter(image, null)
        } catch (final Exception e) {
            log.warn("Exception when resizing image", e)
            throw e
        }
    }

    static void writeImage(@Nonnull final BufferedImage image, @Nonnull final File file) {
        requireNonNull(image, "Image cannot be null")
        requireNonNull(file, "Target file cannot be null")
        try {
            ImageIO.write(image, "jpg", file)
        } catch (final Exception e) {
            log.warn("Exception when writing image: ${file}", e)
            throw e
        }
    }

}