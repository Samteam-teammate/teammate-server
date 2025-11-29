package com.samteam.teammate.domain.scrap.dto;

public record ScrapResponse(boolean scraped) {
    public static ScrapResponse of(boolean scraped) {
        return new ScrapResponse(scraped);
    }
}
