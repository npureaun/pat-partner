package team.springpsring.petpartner.domain.feed.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.springpsring.petpartner.domain.feed.comment.entity.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByFeedIdAndId(feedId: Long, commentId: Long): Comment?
}