package dev.xixil.navigation.domain.pathfinding

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import java.util.PriorityQueue
import kotlin.math.abs

class AStar : Pathfinding {
    override fun findPath(
        start: Vertex,
        finish: Vertex,
        graph: Map<Vertex, List<Edge>>,
    ): List<Vertex> {
        val fScore = mutableMapOf<Vertex, Long>().withDefault { Long.MAX_VALUE }
        val gScore = mutableMapOf<Vertex, Long>().withDefault { Long.MAX_VALUE }

        val openSet = PriorityQueue<Vertex>(compareBy { fScore[it] ?: Long.MAX_VALUE }).apply {
            add(start)
        }
        val cameFrom = mutableMapOf<Vertex, Vertex>()


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
                    cameFrom[neighbor] = current
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

    private fun reconstructPath(cameFrom: Map<Vertex, Vertex>, current: Vertex): List<Vertex> {
        val path = mutableListOf<Vertex>()
        var currentVertex: Vertex? = current

        while (currentVertex != null) {
            path.add(currentVertex)
            currentVertex = cameFrom[currentVertex]
        }

        return path.reversed()
    }


    private fun heuristicCostEstimate(start: Vertex, finish: Vertex): Long {
        return abs(start.coordinates.x - finish.coordinates.x).toLong() +
                abs(start.coordinates.y - finish.coordinates.y).toLong() +
                abs(start.coordinates.z - finish.coordinates.z).toLong()
    }
}