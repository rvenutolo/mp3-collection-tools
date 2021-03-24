package org.venutolo.mp3.core

interface Id3v1Tag extends Id3Tag {

    default String getTagType() {
        "ID3v1"
    }

}