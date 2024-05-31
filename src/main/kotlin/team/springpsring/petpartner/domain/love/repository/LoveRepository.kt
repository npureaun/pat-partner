package team.springpsring.petpartner.domain.love.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.springpsring.petpartner.domain.love.entity.Love

interface LoveRepository : JpaRepository<Love, Long> {
    fun findByFeedIdAndLoginId(feedId:Long, loginId:String):Love?
    fun countLoveByFeedId(feedId:Long):Int
    fun existsByFeedIdAndLoginId(feedId:Long, loginId:String):Boolean
}