package com.jackhammer.url.shortener.security;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    String token;
}
