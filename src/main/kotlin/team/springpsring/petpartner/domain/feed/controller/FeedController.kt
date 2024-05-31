package team.springpsring.petpartner.domain.feed.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.springpsring.petpartner.domain.feed.dto.*
import team.springpsring.petpartner.domain.feed.entity.CategoryType
import team.springpsring.petpartner.domain.feed.service.FeedService
import team.springpsring.petpartner.domain.user.dto.GetUserInfoRequest


@RestController
@RequestMapping("/feeds")
class FeedController(
    private val feedService: FeedService,
) {
    @GetMapping
    fun getAllFeeds()
            : ResponseEntity<List<FeedResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getAllFeeds())
    }

    @GetMapping("/{feedId}")
    fun getFeedById(@PathVariable feedId: Long)
            : ResponseEntity<FeedResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedById(feedId))
    }

    @GetMapping("/categories")
    fun getCategoryFeeds(@RequestParam category: CategoryType)
            : ResponseEntity<List<FeedResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedByCategory(category))
    }

    //이 부분을 고민을 해봐야한다고 생각해요.. 여기서 되는게 맞나? 이런 느낌으로다가...
    @PostMapping("/username")
    fun getUsernameFeeds(@RequestBody getUsernameFeedRequest: GetUserInfoRequest)
            : ResponseEntity<List<FeedResponse>> {
        return feedService.checkValidate(getUsernameFeedRequest.token)
            .let {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(feedService.getFeedByUsername(it))
            }
    }

    @PostMapping
    fun createFeed(@RequestBody feedRequest: FeedRequest)
            : ResponseEntity<FeedResponse> {
        return feedService.checkValidate(feedRequest.user.token)
            .let {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(feedService.createFeed(it, feedRequest))
            }
    }

    @PutMapping("/{feedId}")
    fun updateFeed(
        @PathVariable feedId: Long,
        @RequestBody feedRequest: FeedRequest)
            : ResponseEntity<FeedResponse> {
        return feedService.checkValidate(feedRequest.user.token)
            .let {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(feedService.updateFeed(it, feedId, feedRequest))
            }
    }

    @DeleteMapping("/{feedId}")
    fun deleteFeed(@PathVariable feedId: Long, @RequestBody getUserInfoRequest: GetUserInfoRequest)
            : ResponseEntity<Unit> {
        return feedService.checkValidate(getUserInfoRequest.token)
            .let {
                ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(feedService.deleteFeed(it, feedId))
            }
    }

    @PostMapping("/{feedId}/loves")
    fun updateLoveForFeed(
        @PathVariable feedId: Long,
        @RequestBody getUserInfoRequest: GetUserInfoRequest)
            : ResponseEntity<Unit> {

        return feedService.checkValidate(getUserInfoRequest.token)
            .let {
                val isLove = feedService.isLove(feedId,it)
                ResponseEntity
                    .status(
                        if (isLove) HttpStatus.NO_CONTENT
                        else HttpStatus.CREATED
                    )
                    .body(feedService.updateLoveForFeed(feedId, isLove, it))
            }
    }
}