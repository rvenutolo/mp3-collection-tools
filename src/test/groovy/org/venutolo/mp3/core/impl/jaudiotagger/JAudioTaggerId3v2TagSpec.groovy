package org.venutolo.mp3.core.impl.jaudiotagger

import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.jaudiotagger.tag.id3.AbstractID3v2Tag
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v2Tag.Version
import org.venutolo.mp3.specs.Mp3Specification

class JAudioTaggerId3v2TagSpec extends Mp3Specification {

    static boolean compareImages(final BufferedImage img1, final BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false
        }
        (0..<img1.getHeight()).every { y ->
            (0..<img1.getHeight()).every { x ->
                img1.getRGB(x, y) == img2.getRGB(x, y)
            }
        }
    }

    def "null wrapped tag throws NPE"() {

        when:
        new JAudioTaggerId3v2Tag(null as AbstractID3v2Tag)

        then:
        thrown(NullPointerException)

    }

    def "null version throws NPE"() {

        when:
        new JAudioTaggerId3v2Tag(null as Version)

        then:
        thrown(NullPointerException)

    }

    def "tag type is v2.4"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        tag.getTagType() == 'ID3v2.4'

    }

    def "sets/gets #field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        def fieldValue = fieldVal(field)
        tag.set(field, fieldValue)

        expect:
        tag.get(field) == fieldValue

        where:
        field << Field.values().toList()

    }

    def "set null field throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.set(null, '1')

        then:
        thrown(NullPointerException)

    }

    def "setting #field to null throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.set(field, null)

        then:
        thrown(NullPointerException)

        where:
        field << Field.values().toList()

    }

    def "setting #field to empty string throws IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.set(field, '')

        then:
        thrown(IllegalArgumentException)

        where:
        field << Field.values().toList()

    }

    def "setting #field to non-numeric string throws IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.set(field, 'x')

        then:
        thrown(IllegalArgumentException)

        where:
        field << Field.values().findAll {field -> field.isNumeric }.toList()

    }

    def "setting #field to non-numeric string does not throw IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.set(field, 'x')

        then:
        noExceptionThrown()

        where:
        field << Field.values().findAll {field -> !field.isNumeric }.toList()

    }

    def "get #field returns empty string when not set"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        tag.get(field) == ''

        where:
        field << Field.values().toList()

    }

    def "get null field throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.get(null)

        then:
        thrown(NullPointerException)

    }

    def "has #field returns false for non-set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        !tag.has(field)

        where:
        field << Field.values().toList()

    }

    def "has #field returns true for set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        tag.set(field, fieldVal(field))

        expect:
        tag.has(field)

        where:
        field << Field.values().toList()

    }

    def "deletes #field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        tag.set(field, fieldVal(field))

        when:
        tag.delete(field)

        then:
        !tag.has(field)
        !tag.get(field)

        where:
        field << Field.values().toList()

    }

    def "returns populated fields"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        def expectedMap = [:]
        REQUIRED_FIELDS.eachWithIndex { field, idx ->
            def fieldValue = fieldVal(field, idx)
            tag.set(field, fieldValue)
            expectedMap[field] = fieldValue
        }

        expect:
        tag.populatedFields() == expectedMap

    }

    def "returns expected version"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        tag.getVersion() == V2_4

    }

    def "sets/gets artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        tag.setArtwork(JPG_FILE)
        def bufferedImage = ImageIO.read(JPG_FILE)

        expect:
        compareImages(tag.getArtwork(), bufferedImage)

    }

    def "setting artwork to null throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        when:
        tag.setArtwork(null)

        then:
        thrown(NullPointerException)

        where:
        field << Field.values().toList()

    }

    def "get artwork returns null when not set"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        tag.getArtwork() == null

    }

    def "has artwork returns false for non-set artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)

        expect:
        !tag.hasArtwork()

    }

    def "has artwork returns true for set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        tag.setArtwork(JPG_FILE)

        expect:
        tag.hasArtwork()

    }

    def "deletes artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        tag.setArtwork(JPG_FILE)

        when:
        tag.deleteArtwork()

        then:
        !tag.hasArtwork()
        !tag.getArtwork()

    }

    def "verify equals/hashCode"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(V2_4)
        def sameTag = new JAudioTaggerId3v2Tag(V2_4)
        def id3v1Tag = new JAudioTaggerId3v1Tag()
        def otherId3v2Tags = Version.values()
            .findAll { version -> version != V2_4 }
            .collect { version -> new JAudioTaggerId3v2Tag(version) }
        def diffFieldTags = Field.values()
            .collect { field -> new JAudioTaggerId3v2Tag(V2_4).tap { set(field, fieldVal(field)) } }
        def diffArtworkTag = new JAudioTaggerId3v2Tag(V2_4).tap { setArtwork(JPG_FILE) }

        expect: 'equal to itself'
        tag == tag

        and: 'equal to tag with same fields'
        tag == sameTag
        tag.hashCode() == sameTag.hashCode()

        and: 'not equal to null'
        tag != null

        and: 'not equal to id3 tag'
        tag != id3v1Tag
        tag.hashCode() != id3v1Tag.hashCode()

        and: 'not equal to other version id3v2 tags'
        otherId3v2Tags.every { otherTag -> tag != otherTag }
        otherId3v2Tags.every { otherTag -> tag.hashCode() != otherTag.hashCode() }

        and: 'not equal to other v2.4 tag with populated fields'
        diffFieldTags.every { otherTag -> tag != otherTag }
        diffFieldTags.every { otherTag -> tag.hashCode() != otherTag.hashCode() }

        and: 'not equal to other 2.4 tag with artwork'
        tag != diffArtworkTag
        tag.hashCode() != diffArtworkTag.hashCode()

    }

}
