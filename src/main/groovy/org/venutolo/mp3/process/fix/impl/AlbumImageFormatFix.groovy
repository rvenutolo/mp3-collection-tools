package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FORMAT
import static org.venutolo.mp3.process.util.ImageUtil.getImageFormat
import static org.venutolo.mp3.process.util.ImageUtil.readImage
import static org.venutolo.mp3.process.util.ImageUtil.writeImage

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractDirFix

@Slf4j
class AlbumImageFormatFix extends AbstractDirFix {

    AlbumImageFormatFix(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    protected boolean fixInternal(@Nonnull final File dir) {
        def albumImageFile = new File(dir, ALBUM_IMAGE_FILENAME)
        if (!albumImageFile.exists()) {
            return false
        }
        def imageFormatOptional = getImageFormat(albumImageFile, output)
        if (!imageFormatOptional.isPresent()) {
            return false
        }
        def imageFormat = imageFormatOptional.get()
        if (imageFormat == ALBUM_IMAGE_FORMAT) {
            return false
        }
        def imageOptional = readImage(albumImageFile, output)
        if (!imageOptional.isPresent()) {
            return false
        }
        def image = imageOptional.get()
        def wroteImage = writeImage(image, albumImageFile, output)
        if (wroteImage) {
            output.write(albumImageFile, "Wrote ${ALBUM_IMAGE_FORMAT}")
        }
        // This fix doesn't require writing mp3 tags, so return false, even if
        // a fix was made
        false
    }

}
