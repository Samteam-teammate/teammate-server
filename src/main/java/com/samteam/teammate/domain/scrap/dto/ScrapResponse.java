package com.samteam.teammate.domain.scrap.dto;

public record ScrapResponse(
    Long scrapId,
    boolean scraped
) {
    public static ScrapResponse of(Long scrapId, boolean scraped) {
        return new ScrapResponse(scrapId, scraped);
    }
}
