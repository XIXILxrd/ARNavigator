package dev.xixil.navigation.domain.models

import dev.romainguy.kotlin.math.Float3

data class Vertex(
    val id: Long,
    val data: String? = null,
    val coordinates: Float3,
) {
    override fun toString(): String {
        return "$data"
    }
}