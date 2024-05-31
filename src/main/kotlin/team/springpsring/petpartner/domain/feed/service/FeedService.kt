package team.springpsring.petpartner.domain.feed.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.springpsring.petpartner.domain.feed.comment.entity.toResponse
import team.springpsring.petpartner.domain.feed.comment.repository.CommentRepository
import team.springpsring.petpartner.domain.feed.dto.FeedRequest
import team.springpsring.petpartner.domain.feed.dto.FeedResponse
import team.springpsring.petpartner.domain.feed.entity.CategoryType
import team.springpsring.petpartner.domain.feed.entity.Feed
import team.springpsring.petpartner.domain.feed.entity.toResponse
import team.springpsring.petpartner.domain.feed.repository.FeedRepository
import team.springpsring.petpartner.domain.love.repository.LoveRepository
import team.springpsring.petpartner.domain.love.service.LoveService
import team.springpsring.petpartner.domain.user.dto.GetUserInfoRequest
import team.springpsring.petpartner.domain.user.dto.UserResponse
import team.springpsring.petpartner.domain.user.service.UserService
import javax.naming.AuthenticationException

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val commentRepository: CommentRepository,
    private val loveService: LoveService,
    private val userService: UserService,
    private val loveRepository: LoveRepository
) {
    private fun checkUsername(requestUsername:String, entityUsername:String){
        if(requestUsername!=entityUsername)
            throw AuthenticationException("userNotMatch")
    }

    @Transactional
    fun checkValidate(token: String):UserResponse{
        return userService.getUserInfo(
            GetUserInfoRequest(
                token=token))
    }

    fun getAllFeeds(): List<FeedResponse> {
        return feedRepository.findAll().map { it.toResponse() }
    }

    fun getFeedByCategory(category: CategoryType): List<FeedResponse> {
        return feedRepository.findByCategoryOrderByCreatedDesc(category)
            .map { it.toResponse(loveService.countLove(it.id!!)) }
    }

    fun getFeedByUsername(userInfo: UserResponse): List<FeedResponse> {
        return feedRepository.findByNameOrderByCreatedDesc(userInfo.username)
            .map { it.toResponse(loveService.countLove(it.id!!)) }
    }

    fun getFeedById(feedId: Long): FeedResponse {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw NullPointerException("Feed not found")
        val comments = commentRepository.findById(feedId).map { listOf(it.toResponse()) }.orElseGet { emptyList() }

        return FeedResponse(
            feedId,
            feed.name,
            feed.title,
            feed.body,
            feed.images,
            feed.category,
            feed.views++,
            feed.created,
            loveService.countLove(feedId),
            comments
        )
    }

    @Transactional
    fun createFeed(userInfo:UserResponse,feedRequest: FeedRequest): FeedResponse {
        return feedRepository.save(
            Feed(
                name = userInfo.username,
                title = feedRequest.title,
                body = feedRequest.body,
                images = feedRequest.images,
                category = feedRequest.category,
            )
        ).toResponse()
    }

    @Transactional
    fun updateFeed(userInfo:UserResponse ,feedId: Long, feedRequest: FeedRequest): FeedResponse {
        return feedRepository.findByIdOrNull(feedId)
            ?.let { checkUsername(userInfo.username, it.name)
                it.title = feedRequest.title
                it.body = feedRequest.body
                it.images = feedRequest.images
                it.category = feedRequest.category
                it.toResponse(loveService.countLove(feedId))
            }?: throw NullPointerException("Feed not found")
    }

    @Transactional
    fun deleteFeed(userInfo:UserResponse ,feedId: Long) {
        feedRepository.findByIdOrNull(feedId)
            ?.let { checkUsername(userInfo.username, it.name)
                feedRepository.delete(it)
            } ?: throw NullPointerException("Feed not found")
    }

    @Transactional
    fun updateLoveForFeed(feedId: Long, isLove: Boolean, userInfo: UserResponse) {
        if(isLove) loveService.deleteLove(feedId,userInfo)
        else loveService.createLove(feedId, userInfo.loginId)
    }

    @Transactional
    fun isLove(feedId:Long, userInfo:UserResponse):Boolean{
        return loveRepository.existsByFeedIdAndLoginId(feedId, userInfo.loginId)
    }
}