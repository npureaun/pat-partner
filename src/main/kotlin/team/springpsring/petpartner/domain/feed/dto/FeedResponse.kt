package team.springpsring.petpartner.domain.feed.dto

import team.springpsring.petpartner.domain.feed.comment.dto.CommentResponse
import team.springpsring.petpartner.domain.feed.entity.CategoryType
import java.time.LocalDateTime

data class FeedResponse(
    val id:Long,
    val name:String,
    val title: String,
    val body:String,
    val images: String,
    val category:CategoryType,
    val views:Int,
    val created: LocalDateTime,
    val loveCnt:Int,
    val comments: List<CommentResponse>,
)