package com.mymy.hotelbooking.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.mymy.hotelbooking.security.user.HotelUserDetails;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	
	@Value("${auth.token.expirationInMils}")
	private int jwtExpirationTime;
	
	public String generateJwtTokenForUser(Authentication authentication)
	{
		HotelUserDetails userPrinciple = (HotelUserDetails) authentication.getPrincipal();
		List<String> roles = userPrinciple.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority).toList();
		return Jwts.builder()
					.setSubject(userPrinciple.getUsername())
					.claim("role", roles)
					.setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime()+jwtExpirationTime))
					.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	private Key key() 
	{
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUserNameFromToken(String token)
	{
		return Jwts.parserBuilder()
					.setSigningKey(key())
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
	}
	
	public boolean validateToken(String token) 
	{
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid jwt token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("Expired token: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("This token is not supported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("No claims found: {}", e.getMessage());
		}
		return false;
	}
	

}
