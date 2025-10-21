package com.jackhammer.url.shortener.service;

import com.jackhammer.url.shortener.dtos.UrlMappingDto;
import com.jackhammer.url.shortener.models.UrlMapping;
import com.jackhammer.url.shortener.models.User;
import com.jackhammer.url.shortener.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_URL_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();
    public UrlMappingDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateUniqueShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return convertToDto(savedUrlMapping);
    }
    private String generateUniqueShortUrl(){
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (urlMappingRepository.existsByShortUrl(shortUrl));
        return shortUrl;
    }

    private String generateShortUrl() {
        StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);

        for (int i=0; i<SHORT_URL_LENGTH; i++){
            shortUrl.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    private UrlMappingDto convertToDto(UrlMapping urlMapping){
        UrlMappingDto urlMappingDto = new UrlMappingDto();
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setClickCount(urlMapping.getClickCount());
        urlMappingDto.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDto.setUsername(urlMapping.getUser().getUsername());

        return urlMappingDto;
    }
}
