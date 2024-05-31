package team.springpsring.petpartner.domain.user.validemail.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.springpsring.petpartner.domain.user.validemail.dto.CheckPassCodeRequest
import team.springpsring.petpartner.domain.user.validemail.dto.SendPassCodeRequest
import team.springpsring.petpartner.domain.user.validemail.service.ValidEmailService

@RequestMapping("/users/signup")
@RestController
class CheckController (
    private val validEmailService: ValidEmailService
){
    @PostMapping("/send-passcode")
    fun sendPassCode(@RequestBody request: SendPassCodeRequest) : ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(validEmailService.sendPassCode(request.email))
    }

    @PostMapping("/check-passcode")
    fun checkPassCode(@RequestBody request: CheckPassCodeRequest): ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(validEmailService.checkPassCode(request))
    }
}