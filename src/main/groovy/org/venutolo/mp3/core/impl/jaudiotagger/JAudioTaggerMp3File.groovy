package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.jaudiotagger.tag.id3.ID3v11Tag
import org.jaudiotagger.tag.id3.ID3v11Tag as JatId3v11Tag
import org.jaudiotagger.audio.mp3.MP3File as JATMp3File
import org.venutolo.mp3.core.Id3v1Tag
import org.venutolo.mp3.core.Id3v2Tag
import org.venutolo.mp3.core.Mp3File

// TODO unit test
final class JAudioTaggerMp3File implements Mp3File {

    @Nonnull private final JATMp3File jatMp3File

    JAudioTaggerMp3File(@Nonnull final File file) {
        requireNonNull(file, 'File cannot be null')
        this.jatMp3File = new JATMp3File(file)
        if (jatMp3File.hasID3v1Tag()) {
            // ensure that id3 tag is the 'v11' version
            def id3Tag = jatMp3File.getID3v1Tag()
            if (!(id3Tag instanceof JatId3v11Tag)) {
                jatMp3File.setID3v1Tag(new ID3v11Tag(id3Tag))
            }
        }
    }

    @Nonnull
    @Override
    File getFile() {
        jatMp3File.file
    }

    @Nullable
    @Override
    Id3v1Tag getId3v1Tag() {
        jatMp3File.hasID3v1Tag() ? new JAudioTaggerId3v1Tag(jatMp3File.getID3v1Tag() as JatId3v11Tag) : null
    }

    @Override
    void setId3v1Tag(@Nonnull final Id3v1Tag tag) {
        requireNonNull(tag, 'Tag cannot be null')
        jatMp3File.setID3v1Tag(((JAudioTaggerId3v1Tag)tag).getJatTag())
    }

    @Override
    void removeId3v1Tag() {
        jatMp3File.setID3v1Tag(null)
    }

    @Nullable
    @Override
    Id3v2Tag getId3v2Tag() {
        jatMp3File.hasID3v2Tag() ? new JAudioTaggerId3v2Tag(jatMp3File.getID3v2Tag()) : null
    }

    @Override
    void setId3v2Tag(@Nonnull final Id3v2Tag tag) {
        requireNonNull(tag, 'Tag cannot be null')
        jatMp3File.setID3v2Tag(((JAudioTaggerId3v2Tag)tag).getJatTag())
    }

}
