package dev.xixil.navigation.data.utils

import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import dev.xixil.navigation.data.database.record.models.RecordDbo
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Record
import dev.xixil.navigation.domain.models.User
import dev.xixil.navigation.domain.models.UserDbo
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
    cloudAnchorId = cloudAnchorId,
    x = coordinates.x,
    y = coordinates.y,
    z = coordinates.z
)

fun Edge.toEdgeDbo() = EdgeDbo(
    edgeId = id,
    source = source.toVertexDbo(),
    destination = destination.toVertexDbo(),
    cloudAnchorId = cloudAnchorId,
    weight = weight
)

fun EdgeDbo.toEdge() = Edge(
    id = edgeId,
    source = source.toVertex(),
    destination = destination.toVertex(),
    cloudAnchorId = cloudAnchorId,
    weight = weight
)

fun VertexDbo.toVertex() = Vertex(
    id = vertexId,
    data = data,
    cloudAnchorId = cloudAnchorId,
    coordinates = Float3(x, y, z)
)

fun User.toUserDbo() = UserDbo(
    id = this.id,
    name = this.name,
    profilePictureUrl = this.photoUrl
)

fun UserDbo.toUser() = User(
    id = this.id,
    name = this.name ?: "",
    photoUrl = this.profilePictureUrl ?: ""
)