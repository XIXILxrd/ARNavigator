package dev.xixil.navigation.domain.pathfinding

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex

interface Pathfinding {
    fun findPath(start: Vertex, finish: Vertex, graph: Map<Vertex, List<Edge>>): List<Vertex>
}