package dev.xixil.navigation.domain.models

import dev.romainguy.kotlin.math.Float3

//Вершина
data class Vertex<T>(
    val id: Int,
    val data: T? = null,
    val coordinates: Float3,
)