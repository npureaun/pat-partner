package team.springpsring.petpartner.domain.feed.dto

import team.springpsring.petpartner.domain.feed.entity.CategoryType
import team.springpsring.petpartner.domain.user.dto.GetUserInfoRequest

data class FeedRequest(
    val title: String,
    val body:String,
    val images : String,
    val category:CategoryType,
    val user: GetUserInfoRequest
)
