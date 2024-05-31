package team.springpsring.petpartner.domain.user.entity

import team.springpsring.petpartner.domain.user.dto.SignUpUserRequest
import jakarta.persistence.*
import team.springpsring.petpartner.domain.user.dto.UserResponse

@Entity
@Table(name = "users")
class User(
    @Column(name = "login_id")
    var loginId: String,
    @Column(name = "password")
    var password: String,
    @Column(name = "email")
    var email: String,
    @Column(name = "name")
    var username: String,
) {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun User.toResponse(): UserResponse {
    return UserResponse(
        loginId = this.loginId,
        email = this.email,
        username = this.username
    )
}
