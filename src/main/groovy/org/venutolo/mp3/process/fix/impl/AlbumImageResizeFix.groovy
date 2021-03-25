package org.venutolo.mp3.process.fix.impl

import static java.lang.Math.max
import static java.lang.Math.min
import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.core.Constants.TARGET_PIXELS
import static org.venutolo.mp3.process.util.ImageUtil.readImage
import static org.venutolo.mp3.process.util.ImageUtil.resizeImage

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractDirFix
import org.venutolo.mp3.process.util.ImageUtil

@Slf4j
final class AlbumImageResizeFix extends AbstractDirFix {

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
        def imageOptional = readImage(albumImageFile, output)
        if (!imageOptional.isPresent()) {
            return false
        }
        def image = imageOptional.get()
        def imageWidth = image.getWidth()
        def imageHeight = image.getHeight()
        if (isTargetDimensions(imageWidth, imageHeight)) {
            return false
        }
        if (!shouldResize(imageWidth, imageHeight)) {
            return false
        }
        def resizedImageOptional = resizeImage(albumImageFile, TARGET_PIXELS, TARGET_PIXELS, output)
        if (!resizedImageOptional.isPresent()) {
            return false
        }
        def resizedImage = resizedImageOptional.get()
        def wroteImage = ImageUtil.writeImage(resizedImage, albumImageFile, output)
        if (wroteImage) {
            output.write(albumImageFile, 'Resized image', "${TARGET_PIXELS}x${TARGET_PIXELS}")
        }
        // This fix doesn't require writing mp3 tags, so return false, even if
        // a fix was made
        false
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
            ratio >= SQUARE_TOLERANCE
        }
    }

}
