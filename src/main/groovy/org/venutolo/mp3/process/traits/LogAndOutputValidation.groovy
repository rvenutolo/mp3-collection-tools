package org.venutolo.mp3.process.traits

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.core.Output

trait LogAndOutputValidation {

    void validateLogAndOutput(@Nonnull final Logger log, @Nonnull final Output output) {
        requireNonNull(log, 'Logger cannot be null')
        requireNonNull(output, 'Output cannot be null')
    }

}