package team.springpsring.petpartner.domain.user.service

import team.springpsring.petpartner.domain.user.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import team.springpsring.petpartner.domain.security.hash.BCHash
import team.springpsring.petpartner.domain.security.jwt.JwtUtil
import team.springpsring.petpartner.domain.user.dto.*
import team.springpsring.petpartner.domain.user.entity.User
import team.springpsring.petpartner.domain.user.entity.toResponse
import team.springpsring.petpartner.domain.user.loginUser.service.LoginUserService
import javax.naming.AuthenticationException

@Service
class UserService(
    private val loginService: LoginUserService,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val encoder: BCHash,
){
    //!!!GlobalExceptionHandler 적용 후 Exception 종류 개선해야함

    private fun validateLoginIdFromToken(token:String):User {
        return jwtUtil.validateToken(token).let {
            loginService.checkLoginStatus(it, token)
            userRepository.findByLoginId(it)
                ?: throw EntityNotFoundException("User Not Found")
        }
    }

    @Transactional
    fun signUpUser(request: SignUpUserRequest):UserResponse{
        val user=request.toEntity(encoder.hashPassword(request.password))
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            throw DataIntegrityViolationException("Data Duplication")
        }
        return user.toResponse()
    }

    @Transactional
    fun logInUser(request: LogInUserRequest):String {
        val token= userRepository.findByLoginId(request.loginId)
            ?.let { user ->
            if (encoder.verifyPassword(request.password, user.password)) {
                jwtUtil.generateAccessToken("loginId", user.loginId)
            }else throw AuthenticationException("User Password Not Match")
        } ?: throw EntityNotFoundException("User Not Found")
        loginService.login(request.loginId,token)
        return token
    }

    @Transactional
    fun getUserInfo(request:GetUserInfoRequest):UserResponse {
        return validateLoginIdFromToken(request.token).toResponse()
    }

    @Transactional
    fun updatePassword(request:UpdateUserPasswordRequest):Boolean {
        validateLoginIdFromToken(request.user.token)
            .let {
                if(encoder.verifyPassword(request.oldPassword,it.password)){
                    if(!encoder.verifyPassword(request.newPassword,it.password)){
                        it.password=encoder.hashPassword(request.newPassword)
                    }
                    else throw AuthenticationException("User Password Not changed")
                }
                else throw AuthenticationException("User Password Not Match")
            }
        return true
    }

    @Transactional
    fun logoutUser(request:GetUserInfoRequest):Boolean{
        val user=validateLoginIdFromToken(request.token)
        loginService.logout(user.loginId)
        return true
    }
}