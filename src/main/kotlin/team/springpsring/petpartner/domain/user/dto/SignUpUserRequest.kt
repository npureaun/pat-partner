package team.springpsring.petpartner.domain.user.dto

import team.springpsring.petpartner.domain.user.entity.User

data class SignUpUserRequest(
    val loginId: String,
    val password: String,
    val email: String,
    val username: String,
)

fun SignUpUserRequest.toEntity(password: String): User {
    return User(
        loginId = this.loginId,
        password = password,
        email = this.email,
        username = this.username,
    )
}

