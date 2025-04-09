package org.mathieu.cleanrmapi.domain.location.models

/**
 * Represents a minimal description of a Rick&Morty location.
 *
 * @property id The unique identifier for the location.
 * @property name The name of the location.
 */
data class LocationPreview(
    val id: Int,
    val name: String,
)