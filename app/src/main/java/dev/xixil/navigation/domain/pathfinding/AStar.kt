package dev.xixil.navigation.domain.pathfinding

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import java.util.PriorityQueue
import javax.inject.Inject
import kotlin.math.abs

class AStar @Inject constructor() : Pathfinding {
    override fun findPath(
        start: Vertex,
        finish: Vertex,
        graph: Map<Vertex, List<Edge>>,
    ): List<Edge> {
        val fScore = mutableMapOf<Vertex, Long>().withDefault { Long.MAX_VALUE }
        val gScore = mutableMapOf<Vertex, Long>().withDefault { Long.MAX_VALUE }

        val openSet = PriorityQueue<Vertex>(compareBy { fScore[it] ?: Long.MAX_VALUE }).apply {
            add(start)
        }
        val cameFrom = mutableMapOf<Vertex, Edge>()

        gScore[start] = 0
        fScore[start] = heuristicCostEstimate(start, finish)

        while (openSet.isNotEmpty()) {
            val current = openSet.poll() ?: break

            if (current == finish) {
                return reconstructPath(cameFrom, current)
            }

            openSet.remove(current)
            for (edge in graph[current] ?: emptyList()) {
                val neighbor = edge.destination
                val tentativeGScore = (gScore[current] ?: Long.MAX_VALUE) + edge.weight

                if (tentativeGScore < (gScore[neighbor] ?: Long.MAX_VALUE)) {
                    cameFrom[neighbor] = edge
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + heuristicCostEstimate(neighbor, finish)

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor)
                    }
                }
            }
        }

        return listOf()
    }

    private fun reconstructPath(cameFrom: Map<Vertex, Edge>, current: Vertex): List<Edge> {
        val path = mutableListOf<Edge>()
        var currentVertex: Vertex? = current

        while (currentVertex != null && cameFrom.containsKey(currentVertex)) {
            val edge = cameFrom[currentVertex] ?: break
            path.add(edge)
            currentVertex = edge.source
        }

        return path.reversed()
    }

    private fun heuristicCostEstimate(start: Vertex, finish: Vertex): Long {
        return abs(start.coordinates.x - finish.coordinates.x).toLong() +
                abs(start.coordinates.y - finish.coordinates.y).toLong() +
                abs(start.coordinates.z - finish.coordinates.z).toLong()
    }
}
