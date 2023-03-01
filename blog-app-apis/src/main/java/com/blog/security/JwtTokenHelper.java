package com.blog.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenHelper {
	
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;
	
	private String secret = "jwtTokenKey";
	
	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token){
		return getClaimFromToken(token,Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token,Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//for retrieving any info from token secret is needed
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map <String,Object> claims =  new HashMap<>();
		return doGenerateToken(claims,userDetails.getUsername());
	}
	
	//while creating the token
	private String doGenerateToken(Map<String,Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY * 100))
				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY * 100))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}