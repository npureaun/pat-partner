package team.springpsring.petpartner.domain.security.jwt

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${SECURITY_JWT_ISSUER}")
    lateinit var issuer: String

    @Value("\${SECURITY_JWT_SECRET}")
    lateinit var secret: String

    @Value("\${SECURITY_JWT_ACCESS_TOKEN_EXPIRATION_HOUR}")
    var accessTokenExpirationHour: Long = 0

    fun validateToken(token:String): String{
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        try {
            val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            return claims.payload["loginId"].toString()
        } catch (e:Exception){
            throw JwtException("Invalid JWT token")
        }
    }

    fun generateAccessToken(subject:String, loginId: String): String {
        return generateToken(subject,loginId,Duration.ofHours(accessTokenExpirationHour))
    }

    private fun generateToken(subject:String, loginId: String, expirationPeriod:Duration?): String {
        val claims=Jwts.claims()
            .add(mapOf("loginId" to loginId))
            .build()

        val key: SecretKey =Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val now=Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }
}