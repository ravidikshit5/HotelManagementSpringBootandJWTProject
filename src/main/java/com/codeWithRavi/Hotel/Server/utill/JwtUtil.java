package com.codeWithRavi.Hotel.Server.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private String generateToken(Map<String,Object> extraClaims, UserDetails details){
        return Jwts.builder().setClaims(extraClaims).setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    //Check whether the token is expired or not
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey(){
//        byte[] keyBytes = Decoders.BASE64.decode("NFIDNVEBVEOUIFWEVUEIUEIeibhcevviewufveciqofgew8frfcwei");
//        return Keys.hmacShaKeyFor(keyBytes);
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}
