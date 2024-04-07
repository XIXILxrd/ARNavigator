package dev.xixil.navigation.data.utils

import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import dev.xixil.navigation.data.database.record.models.RecordDbo
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Record
import dev.xixil.navigation.domain.models.Vertex

fun RecordDbo.toRecord() = Record(
    id = id,
    start = start,
    end = end,
    time = time
)

fun Record.toRecordDbo() = RecordDbo(
    id = id,
    start = start,
    end = end,
    time = time
)

fun Vertex.toVertexDbo() = VertexDbo(
    vertexId = id,
    data = data,
    coordinateX = coordinates.x,
    coordinateY = coordinates.y,
    coordinateZ = coordinates.z
)

fun Edge.toEdgeDto() = EdgeDbo(
    edgeId = id,
    source = source.toVertexDbo(),
    destination = destination.toVertexDbo(),
    weight = weight
)

fun EdgeDbo.toEdge() = Edge(
    id = edgeId,
    source = source.toVertex(),
    destination = destination.toVertex(),
    weight = weight
)

fun VertexDbo.toVertex() = Vertex(
    id = vertexId,
    data = data,
    coordinates = Float3(
        coordinateX,
        coordinateY,
        coordinateZ
    )
)