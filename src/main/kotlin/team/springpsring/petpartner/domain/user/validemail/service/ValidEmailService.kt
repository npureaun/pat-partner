package team.springpsring.petpartner.domain.user.validemail.service

import org.springframework.mail.MailSendException
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.SimpleMailMessage
import team.springpsring.petpartner.domain.user.validemail.dto.CheckPassCodeRequest

@Service
class ValidEmailService(
    private val mailSender : JavaMailSender
) {
    private val passCodes = ConcurrentHashMap<String, Int>()

    fun sendPassCode(sendEmail: String) {
        val passCode = generatePassCode()
        passCodes[sendEmail] = passCode
        val message = createMessage(sendEmail, passCode)
        try {
            mailSender.send(message)
        } catch (e: MailSendException) {
            throw MailSendException("Error : $e", e)
        }
    }

    private fun generatePassCode(): Int {
        return Random.nextInt(10000, 100000)
    }

    private fun createMessage(to: String, passCode: Int): SimpleMailMessage {
        return SimpleMailMessage().apply {
            from = System.getenv("MAIL_USERNAME")
            setTo(to)
            subject = "Your Verification Code"
            text = "Your verification code is: $passCode"
        }
    }

    fun checkPassCode(request: CheckPassCodeRequest):Boolean {
        val savedCode = passCodes[request.email]
        return if (savedCode != null && savedCode == request.passcode) {
            passCodes.remove(request.email)
            true
        } else {
            false
        }
    }
}
