package team.springpsring.petpartner.domain.user.loginUser.entity

import jakarta.persistence.*

@Entity
@Table(name = "login_user")
class LoginUser(
    @Column(name = "login_id")
    var loginId: String,
    @Column(name = "token")
    var token: String
) {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}