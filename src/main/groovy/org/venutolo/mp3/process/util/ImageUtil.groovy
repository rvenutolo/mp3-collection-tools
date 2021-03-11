package org.venutolo.mp3.process.util

import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.imageio.ImageIO

@Slf4j
class ImageUtil {

    static BufferedImage readImage(@Nonnull final File imageFile) {
        try {
            ImageIO.read(imageFile)
        } catch (final Exception e) {
            log.warn("Exception when reading image: ${imageFile}", e)
            throw e
        }
    }

    static String getImageFormat(@Nonnull final File imageFile) {
        try {
            ImageIO.createImageInputStream(imageFile).withCloseable { imageInputStream ->
                ImageIO.getImageReaders(imageInputStream).next().getFormatName().toUpperCase()
            }
        } catch (final Exception e) {
            log.warn("Exception when reading image format: ${imageFile}", e)
            throw e
        }
    }

}