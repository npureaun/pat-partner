package team.springpsring.petpartner.domain.user.dto

data class UpdateUserPasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val user : GetUserInfoRequest
)
