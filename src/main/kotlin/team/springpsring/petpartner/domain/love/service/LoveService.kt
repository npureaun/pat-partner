package team.springpsring.petpartner.domain.love.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.springpsring.petpartner.domain.feed.repository.FeedRepository
import team.springpsring.petpartner.domain.love.dto.LoveResponse
import team.springpsring.petpartner.domain.love.entity.Love
import team.springpsring.petpartner.domain.love.entity.toResponse
import team.springpsring.petpartner.domain.love.repository.LoveRepository
import team.springpsring.petpartner.domain.user.dto.UserResponse

@Service
class LoveService(
    private val loveRepository: LoveRepository,
    private val feedRepository: FeedRepository,) {

    @Transactional
    fun createLove(feedId:Long, loginId:String): LoveResponse {
        val feed=feedRepository.findByIdOrNull(feedId)?:throw NullPointerException("Feed not found")
        val love = Love(
            loginId,
            feed = feed
        )
        loveRepository.save(love)
        return love.toResponse()
    }

    @Transactional
    fun deleteLove(feedId:Long, userInfo:UserResponse) {
        val loginId = userInfo.loginId
        val love = loveRepository.findByFeedIdAndLoginId(feedId, loginId)?: throw EntityNotFoundException("Love not found")
        loveRepository.delete(love)
    }

    @Transactional
    fun countLove(feedId:Long):Int{
        return loveRepository.countLoveByFeedId(feedId)
    }
}