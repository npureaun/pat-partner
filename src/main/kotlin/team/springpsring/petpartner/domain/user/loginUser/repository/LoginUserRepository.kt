package team.springpsring.petpartner.domain.user.loginUser.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import team.springpsring.petpartner.domain.user.loginUser.entity.LoginUser

interface LoginUserRepository: JpaRepository<LoginUser, Long>{
    fun findByLoginId(loginId: String): LoginUser?
    fun deleteByLoginId(loginId: String)
    fun existsByLoginId(loginId: String):Boolean
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM LoginUser u WHERE u.loginId = :loginId AND u.token = :token")
    fun existsByLoginIdAndToken(@Param("loginId") loginId: String, @Param("token") token: String): Boolean
}