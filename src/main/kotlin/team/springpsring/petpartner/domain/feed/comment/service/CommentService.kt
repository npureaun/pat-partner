package team.springpsring.petpartner.domain.feed.comment.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.springpsring.petpartner.domain.feed.comment.dto.CommentResponse
import team.springpsring.petpartner.domain.feed.comment.dto.CommentRequest
import team.springpsring.petpartner.domain.feed.comment.entity.Comment
import team.springpsring.petpartner.domain.feed.comment.entity.toResponse
import team.springpsring.petpartner.domain.feed.comment.repository.CommentRepository
import team.springpsring.petpartner.domain.feed.repository.FeedRepository
import team.springpsring.petpartner.domain.user.dto.GetUserInfoRequest
import team.springpsring.petpartner.domain.user.dto.UserResponse
import team.springpsring.petpartner.domain.user.service.UserService

@Service
class CommentService(
    private val feedRepository: FeedRepository,
    private val commentRepository: CommentRepository,
    private val userService: UserService
){
    @Transactional
    fun createComment(feedId:Long, commentRequest: CommentRequest, username:String): CommentResponse {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw NullPointerException("Feed not found")
        val comment = Comment(
            username = username,
            body = commentRequest.body,
            feed = feed
        )
        feed.addComment(comment)
        commentRepository.save(comment)

        return comment.toResponse()
    }

    @Transactional
    fun updateComment(feedId: Long, commentId: Long, commentRequest: CommentRequest): CommentResponse{
        val comment = commentRepository.findByFeedIdAndId(feedId, commentId) ?:
        throw EntityNotFoundException("Feed not found")

        comment.body = commentRequest.body
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun deleteComment(feedId: Long,commentId: Long){
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw NullPointerException("Feed not found")
        val comment = commentRepository.findByFeedIdAndId(feedId, commentId) ?:
        throw EntityNotFoundException("Comment not found")

        feed.deleteComment(comment)
        commentRepository.delete(comment)
    }

    fun validateToken(token:String):UserResponse{
        return userService.getUserInfo(GetUserInfoRequest(token))
    }

    fun checkOwner(token:String, feedId:Long, commentId:Long):Boolean{
        val comment = commentRepository.findByFeedIdAndId(feedId, commentId)
            ?: throw EntityNotFoundException("Comment not found")
        return validateToken(token).username == comment.username
    }
}