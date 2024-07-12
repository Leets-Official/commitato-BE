package com.leets.commitatobe.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    STUPID_POTATO("바보감자", 0, "", ""),
    NORMAL_POTATO("그냥감자", 1000, "", ""),
    SPEAKING_POTATO("말하는감자", 15000, "", ""),
    GENIUS_POTATO("천재감자", 33000, "", "");

    private final String tierName;
    private final Integer requiredExp;
    private final String characterUrl;
    private final String badgeUrl;
}