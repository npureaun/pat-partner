package team.springpsring.petpartner.domain.user.controller


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.springpsring.petpartner.domain.user.dto.*
import team.springpsring.petpartner.domain.user.service.UserService

@RequestMapping("/users")
@RestController
class UserController(private val userService: UserService) {
    @PostMapping("/signup")
    fun signUpUser(@RequestBody userRequest: SignUpUserRequest)
    : ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signUpUser(userRequest))
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody userRequest: LogInUserRequest)
    : ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.logInUser(userRequest))
    }

    @PostMapping("/userinfo")
    fun getUserInfo(@RequestBody userInfoRequest: GetUserInfoRequest)
    : ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserInfo(userInfoRequest))
    }

    @PatchMapping("/update-password")
    fun updatePassword(@RequestBody updateRequest: UpdateUserPasswordRequest)
            : ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updatePassword(updateRequest))
    }

    @DeleteMapping("/logout")
    fun logout(@RequestBody deleteRequest: GetUserInfoRequest)
            : ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.logoutUser(deleteRequest))
    }
}
