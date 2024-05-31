package team.springpsring.petpartner.domain.feed.comment.dto

import java.time.LocalDateTime

class CommentResponse(
    val id: Long,
    val name: String,
    val body: String,
    val createdAt: LocalDateTime,
)