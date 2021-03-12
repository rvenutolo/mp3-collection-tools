package org.venutolo.mp3.process.fix.impl

import static java.lang.Math.max
import static java.lang.Math.min
import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_PIXELS

import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractDirFix
import org.venutolo.mp3.process.util.ImageUtil

@Slf4j
class AlbumImageResizeFix extends AbstractDirFix {

    private static final float SQUARE_TOLERANCE = 0.98f

    AlbumImageResizeFix(@Nonnull final Output output) {
        super(log, output, true);
    }

    @Override
    boolean fixInternal(@Nonnull final File dir) {
        def albumImageFile = new File(dir, ALBUM_IMAGE_FILENAME)
        log.debug('Checking album art: {}', albumImageFile.canonicalPath)
        if (!albumImageFile.exists()) {
            return false
        }
        def image = readImage(albumImageFile)
        if (!image) {
            return false
        }
        def imageWidth = image.getWidth()
        def imageHeight = image.getHeight()
        if (isTargetDimensions(imageWidth, imageHeight)) {
            return false
        }
        if (!shouldResize(imageWidth, imageHeight)) {
            return false
        }
        def resizedImage = resizeImage(albumImageFile)
        if (!resizedImage) {
            return false
        }
        def wroteImage = writeImage(resizedImage, albumImageFile)
        if (wroteImage) {
            output.write(albumImageFile, 'Resized image', "${TARGET_PIXELS}x${TARGET_PIXELS}")
        }
        // This fix doesn't require writing mp3 tags, so return false, even if
        // a fix was made
        false
    }

    @Nullable
    private BufferedImage readImage(@Nonnull final File file) {
        try {
            ImageUtil.readImage(file)
        } catch (final Exception e) {
            output.write(file, 'Exception when reading image', e.message)
            null
        }
    }

    private static boolean isTargetDimensions(final int width, final int height) {
        width == TARGET_PIXELS && height == TARGET_PIXELS
    }

    private static boolean shouldResize(final int width, final int height) {
        dimensionsLargeEnough(width, height) && isRoughlySquare(width, height)
    }

    private static boolean dimensionsLargeEnough(final int width, final int height) {
        width >= TARGET_PIXELS && height >= TARGET_PIXELS
    }

    private static boolean isRoughlySquare(final int width, final int height) {
        if (width == height) {
            return true
        } else {
            def smaller = min(width, height)
            def bigger = max(width, height)
            def ratio = (smaller / bigger) as float
            ratio>= SQUARE_TOLERANCE
        }
    }

    private BufferedImage resizeImage(@Nonnull final File file) {
        def image = readImage(file)
        try {
            ImageUtil.resizeImage(image, TARGET_PIXELS, TARGET_PIXELS)
        } catch (final Exception e) {
            output.write(file, 'Exception when resizing image', e.message)
            null
        }
    }

    private boolean writeImage(@Nonnull final BufferedImage image, @Nonnull final File file) {
        try {
            ImageUtil.writeImage(image, file)
            true
        } catch (final Exception e) {
            output.write(file, 'Exception when writing image', e.message)
            false
        }
    }

}
