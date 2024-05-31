package team.springpsring.petpartner.domain.user.repository

import team.springpsring.petpartner.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByLoginId(userId: String): User?
}