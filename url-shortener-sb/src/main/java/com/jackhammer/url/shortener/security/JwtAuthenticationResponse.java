package com.jackhammer.url.shortener.security;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class JwtAuthenticationResponse {
    String token;
}
