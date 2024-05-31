package team.springpsring.petpartner.domain.user.validemail.dto

data class CheckPassCodeRequest(
    val email: String,
    val passcode: Int,
)
