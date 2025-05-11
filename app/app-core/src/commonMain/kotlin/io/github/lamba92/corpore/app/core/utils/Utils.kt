package io.github.lamba92.corpore.app.core.utils

/**
 * Converts a [Number] to a string with exactly [precision] digits after the decimal point.
 *
 * This implementation is safe for Kotlin Multiplatform `commonMain`, and handles cases like:
 * - Adding trailing zeros to match the desired precision.
 * - Truncating fractional digits beyond the desired precision.
 * - Preserving exponential notation (e.g., "1.23E-5") by copying the rest of the string once such a symbol is encountered.
 *
 * Note: this method does **not round** — it truncates digits after the precision.
 *
 * ### Examples:
 * ```
 * 3.14159.toStringWithPrecision(2) // "3.14"
 * 3.1.toStringWithPrecision(3)     // "3.100"
 * 5.toStringWithPrecision(2)       // "5.00"
 * 1.0E-4.toStringWithPrecision(2)  // "1.0E-4"
 * ```
 *
 * @param precision the number of digits to keep after the decimal point.
 * @return a string representation of the number with the specified decimal precision.
 */
internal fun Number.toStringWithPrecision(precision: Int): String {
    require(precision >= 0) { "Precision must be non-negative" }

    val raw = this.toString()
    var inFraction = false // True once '.' is seen
    var digitsAfterDot = 0 // Counter for fractional digits
    var copyRest = false // Set to true when we hit an unexpected char or finish precision

    return buildString {
        for (ch in raw) {
            if (copyRest) {
                // After finishing the relevant digits or seeing scientific notation,
                // just copy everything else (e.g., "E-5")
                append(ch)
                continue
            }

            append(ch)

            when {
                inFraction ->
                    when {
                        ch.isDigit() -> {
                            digitsAfterDot++
                            // If we’ve reached the desired precision, we’re done with precision trimming
                            if (digitsAfterDot >= precision) copyRest = true
                        }
                        // Unexpected char inside the fraction — stop counting and copy the rest
                        else -> copyRest = true
                    }
                ch == '.' -> inFraction = true
            }
        }

        // If the number ends before enough digits, pad with zeros
        when {
            inFraction && digitsAfterDot < precision ->
                repeat(precision - digitsAfterDot) { append('0') }
            // If the number has no decimal part but precision > 0, add dot and pad
            !inFraction && precision > 0 -> {
                append('.')
                repeat(precision) { append('0') }
            }
        }
    }
}
