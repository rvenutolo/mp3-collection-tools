package org.venutolo.mp3.core.impl.jaudiotagger

import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS
import static org.venutolo.mp3.core.Field.ORIGINAL_YEAR
import static org.venutolo.mp3.core.Field.YEAR
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_2
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_3
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import groovy.transform.Immutable
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.apache.commons.lang3.RandomStringUtils
import org.jaudiotagger.tag.id3.AbstractID3v2Tag
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v2Tag.Version
import org.venutolo.mp3.specs.Mp3Specification

class JAudioTaggerId3v2TagSpec extends Mp3Specification {

    @Immutable
    static class VersionAndField {
        Version version
        Field field
    }

    // Can use multi-variable data pipes in test, but types for those variable are lost.
    // So use this so types aren't lost and IDE won't warn on wrong types on method calls.
    static List<VersionAndField> allCombinations(
        final Collection<Field> fields = Field.values(),
        final Collection<Version> versions = Version.values()
    ) {
        versions.collect { version ->
            fields.collect { field ->
                new VersionAndField(version, field)
            }
        }.flatten() as List<VersionAndField>
    }

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

    def "#version tag type is '#expected'"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        expect:
        tag.getTagType() == expected

        where:
        version || expected
        V2_2    || 'ID3v2.2'
        V2_3    || 'ID3v2.3'
        V2_4    || 'ID3v2.4'

    }

    def "#versionAndField.version sets/gets #versionAndField.field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)
        def fieldValue = fieldVal(versionAndField.field)
        tag.set(versionAndField.field, fieldValue)

        expect:
        tag.get(versionAndField.field) == fieldValue

        where:
        versionAndField << allCombinations()

    }

    def "#versionAndField.version sets/gets #versionAndField.field (random string)"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)
        def fieldValue = RandomStringUtils.randomAlphanumeric(32)
        tag.set(versionAndField.field, fieldValue)

        expect:
        tag.get(versionAndField.field) == fieldValue

        where:
        versionAndField << allCombinations(Field.values().findAll { field ->
            !field.isNumeric && field != YEAR && field != ORIGINAL_YEAR
        })

    }

    def "#version set null field throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        when:
        tag.set(null, '1')

        then:
        thrown(NullPointerException)

        where:
        version << Version.values().toList()

    }

    def "#versionAndField.version setting #versionAndField.field to null throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, null)

        then:
        thrown(NullPointerException)

        where:
        versionAndField << allCombinations()

    }

    def "#versionAndField.version setting #versionAndField.field to empty string throws IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, '')

        then:
        thrown(IllegalArgumentException)

        where:
        versionAndField << allCombinations()

    }

    def "#versionAndField.version setting #versionAndField.field to non-numeric string throws IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, 'x')

        then:
        thrown(IllegalArgumentException)

        where:
        versionAndField << allCombinations(Field.values().findAll { field -> field.isNumeric })

    }

    def "#versionAndField.version setting #versionAndField.field to numeric string does not throw IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, '1234')

        then:
        noExceptionThrown()

        where:
        versionAndField << allCombinations(Field.values().findAll { field -> field.isNumeric })

    }

    def "#versionAndField.version setting #versionAndField.field to non-four-digit string throws IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, 'year')

        then:
        thrown(IllegalArgumentException)

        where:
        versionAndField << allCombinations([YEAR, ORIGINAL_YEAR])

    }

    def "#versionAndField.version setting #versionAndField.field to four-digit string does not throw IAE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        when:
        tag.set(versionAndField.field, '1234')

        then:
        noExceptionThrown()

        where:
        versionAndField << allCombinations([YEAR, ORIGINAL_YEAR])

    }

    def "#versionAndField.version get #versionAndField.field returns empty string when not set"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        expect:
        tag.get(versionAndField.field) == ''

        where:
        versionAndField << allCombinations()

    }

    def "#version get null field throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        when:
        tag.get(null)

        then:
        thrown(NullPointerException)

        where:
        version << Version.values().toList()

    }

    def "#versionAndField.version has #versionAndField.field returns false for non-set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)

        expect:
        !tag.has(versionAndField.field)

        where:
        versionAndField << allCombinations()

    }

    def "#versionAndField.version has #versionAndField.field returns true for set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)
        tag.set(versionAndField.field, fieldVal(versionAndField.field))

        expect:
        tag.has(versionAndField.field)

        where:
        versionAndField << allCombinations()

    }

    def "#versionAndField.version deletes #versionAndField.field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(versionAndField.version)
        tag.set(versionAndField.field, fieldVal(versionAndField.field))

        when:
        tag.delete(versionAndField.field)

        then:
        !tag.has(versionAndField.field)
        !tag.get(versionAndField.field)

        where:
        versionAndField << allCombinations()

    }

    def "#version returns populated fields"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)
        def expectedMap = [:]
        REQUIRED_FIELDS.each { field ->
            def fieldValue = fieldVal(field)
            tag.set(field, fieldValue)
            expectedMap[field] = fieldValue
        }

        expect:
        tag.populatedFields() == expectedMap

        where:
        version << Version.values().toList()

    }

    def "#version returns expected version"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        expect:
        tag.getVersion() == version

        where:
        version << Version.values().toList()

    }

    def "#version sets/gets artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)
        tag.setArtwork(JPG_FILE)
        def bufferedImage = ImageIO.read(JPG_FILE)

        expect:
        compareImages(tag.getArtwork(), bufferedImage)

        where:
        version << Version.values().toList()

    }

    def "#version setting artwork to null throws NPE"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        when:
        tag.setArtwork(null)

        then:
        thrown(NullPointerException)

        where:
        version << Version.values().toList()

    }

    def "#version get artwork returns null when not set"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        expect:
        tag.getArtwork() == null

        where:
        version << Version.values().toList()

    }

    def "#version has artwork returns false for non-set artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)

        expect:
        !tag.hasArtwork()

        where:
        version << Version.values().toList()

    }

    def "#version has artwork returns true for set field"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)
        tag.setArtwork(JPG_FILE)

        expect:
        tag.hasArtwork()

        where:
        version << Version.values().toList()

    }

    def "#version deletes artwork"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)
        tag.setArtwork(JPG_FILE)

        when:
        tag.deleteArtwork()

        then:
        !tag.hasArtwork()
        !tag.getArtwork()

        where:
        version << Version.values().toList()

    }

    def "#version verify equals/hashCode"() {

        setup:
        def tag = new JAudioTaggerId3v2Tag(version)
        def sameTag = new JAudioTaggerId3v2Tag(version)
        def id3v1Tag = new JAudioTaggerId3v1Tag()
        def otherId3v2Tags = Version.values()
            .findAll { other -> version != other }
            .collect { other -> new JAudioTaggerId3v2Tag(other) }
        def diffFieldTags = Field.values()
            .collect { field -> new JAudioTaggerId3v2Tag(version).tap { set(field, fieldVal(field)) } }
        def diffArtworkTag = new JAudioTaggerId3v2Tag(version).tap { setArtwork(JPG_FILE) }

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

        where:
        version << Version.values().toList()

    }

}
