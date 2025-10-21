package com.jackhammer.url.shortener.controller;

import com.jackhammer.url.shortener.dtos.UrlMappingDto;
import com.jackhammer.url.shortener.models.User;
import com.jackhammer.url.shortener.service.UrlMappingService;
import com.jackhammer.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/urls")
public class UrlMappingController {

    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String, String> map, Principal principal){
        String originalUrl = map.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDto urlMappingDto = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDto);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> getShortUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDto> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

}
