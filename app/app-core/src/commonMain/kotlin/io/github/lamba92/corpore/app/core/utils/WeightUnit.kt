package io.github.lamba92.corpore.app.core.utils

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

sealed interface WeightUnit {
    val factorToGrams: Double

    object Grams : WeightUnit {
        override val factorToGrams: Double = 1.0

        override fun toString(): String = "g"
    }

    object Kilograms : WeightUnit {
        override val factorToGrams: Double = 1000.0

        override fun toString(): String = "kg"
    }

    object Pounds : WeightUnit {
        override val factorToGrams: Double = 453.59237

        override fun toString(): String = "lb"
    }

    object Ounces : WeightUnit {
        override val factorToGrams: Double = 28.349523125

        override fun toString(): String = "oz"
    }
}

// Inline class to represent weight with a specific unit
@JvmInline
@Serializable
value class Weight(private val grams: Double) : Comparable<Weight> {
    // Function for creating Weight from a value and unit
    companion object {
        fun from(
            value: Number,
            unit: WeightUnit,
        ) = Weight(value.toDouble() * unit.factorToGrams)

        val ZERO = Weight(0.0)
    }

    // Properties to get the weight value in different units
    val inGrams: Double get() = grams
    val inKilograms: Double get() = grams / WeightUnit.Kilograms.factorToGrams
    val inPounds: Double get() = grams / WeightUnit.Pounds.factorToGrams
    val inOunces: Double get() = grams / WeightUnit.Ounces.factorToGrams

    // Method to convert weight to a specific unit
    fun to(unit: WeightUnit): Double = grams / unit.factorToGrams

    // Arithmetic operators
    operator fun plus(other: Weight): Weight = from(grams + other.grams, WeightUnit.Grams)

    operator fun minus(other: Weight): Weight = from(grams - other.grams, WeightUnit.Grams)

    operator fun times(factor: Number): Weight = from(grams * factor.toDouble(), WeightUnit.Grams)

    operator fun div(factor: Number): Weight = from(grams / factor.toDouble(), WeightUnit.Grams)

    // Comparison
    override fun compareTo(other: Weight): Int = grams.compareTo(other.grams)

    // String representation
    override fun toString(): String =
        when {
            inGrams < 1000 -> "${inGrams.toInt()} ${WeightUnit.Grams}"
            else -> "${inKilograms.toInt()} ${WeightUnit.Kilograms}"
        }

    fun toString(
        unit: WeightUnit,
        precision: Int = 2,
    ): String =
        when (unit) {
            WeightUnit.Grams -> inGrams.toStringWithPrecision(precision) + " ${WeightUnit.Grams}"
            WeightUnit.Kilograms -> inKilograms.toStringWithPrecision(precision) + " ${WeightUnit.Kilograms}"
            WeightUnit.Pounds -> inPounds.toStringWithPrecision(precision) + " ${WeightUnit.Pounds}"
            WeightUnit.Ounces -> inOunces.toStringWithPrecision(precision) + " ${WeightUnit.Ounces}"
        }
}

// Extension properties for creating Weight instances with specific units
val Number.grams: Weight get() = Weight.from(this, WeightUnit.Grams)
val Number.kilograms: Weight get() = Weight.from(this, WeightUnit.Kilograms)
val Number.pounds: Weight get() = Weight.from(this, WeightUnit.Pounds)
val Number.ounces: Weight get() = Weight.from(this, WeightUnit.Ounces)
