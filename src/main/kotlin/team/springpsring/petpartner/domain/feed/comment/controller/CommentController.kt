package team.springpsring.petpartner.domain.feed.comment.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.springpsring.petpartner.domain.feed.comment.dto.CommentResponse
import team.springpsring.petpartner.domain.feed.comment.dto.CommentRequest
import team.springpsring.petpartner.domain.feed.comment.service.CommentService
import team.springpsring.petpartner.domain.user.dto.GetUserInfoRequest

@RequestMapping("/feeds/{feedId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping
    fun createComment(
        @PathVariable feedId: Long,
        @RequestBody commentRequest: CommentRequest,
        getUserInfoRequest: GetUserInfoRequest
    ): ResponseEntity<CommentResponse> {
        return commentService.validateToken(getUserInfoRequest.token).let{
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(feedId, commentRequest,it.username))
        }
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable feedId: Long,
        @PathVariable commentId: Long,
        @RequestBody commentRequest: CommentRequest,
        getUserInfoRequest: GetUserInfoRequest
    ): ResponseEntity<CommentResponse> {
        return commentService.checkOwner(getUserInfoRequest.token,feedId,commentId).let{
            ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(feedId, commentId, commentRequest))
        }
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable feedId: Long,
        @PathVariable commentId: Long,
        getUserInfoRequest: GetUserInfoRequest,
    ): ResponseEntity<Unit> {
        return commentService.checkOwner(getUserInfoRequest.token,feedId,commentId).let {
            ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(commentService.deleteComment(feedId, commentId))
        }
    }
}