package com.protoxon.promenu.menus.search.tags;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum SearchTag {

    HORROR(
            "horror",
            "scary",
            "creepy",
            "spooky",
            "haunted",
            "ghost",
            "demon",
            "monster",
            "zombie",
            "killer",
            "evil",
            "asylum",
            "mansion",
            "dark",
            "nightmare",
            "blood",
            "cursed",
            "ritual",
            "murderer"
    ),
    PUZZLE(
            "puzzle",
            "logic",
            "brain",
            "riddle",
            "mind",
            "maze",
            "escape",
            "challenge",
            "think",
            "code",
            "combination",
            "trap"
    ),
    PVP(
            "pvp",
            "kill",
            "combat",
            "fight",
            "fighting",
            "battle",
            "arena",
            "duel",
            "versus",
            "war",
            "clash",
            "brawl",
            "kit",
            "weapon",
            "sword"
    ),
    PARKOUR(
            "parkour",
            "jump",
            "obstacle",
            "course",
            "climb",
            "speedrun",
            "checkpoint",
            "ladder",
            "precision",
            "movement",
            "sprint"
    ),
    NONE("");

    private final List<String> synonyms;

    SearchTag(String... synonyms) {
        this.synonyms = Arrays.asList(synonyms);
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public static Optional<SearchTag> matchFromQuery(String query) {
        String lower = query.toLowerCase();
        for (SearchTag tag : values()) {
            if (tag.name().equalsIgnoreCase(lower)) return Optional.of(tag);
            if (tag.synonyms.stream().anyMatch(s -> s.equalsIgnoreCase(lower))) return Optional.of(tag);
        }
        return Optional.empty();
    }

    public static SearchTag fromNameOrNone(String input) {
        for (SearchTag tag : values()) {
            if (tag.name().equalsIgnoreCase(input)) {
                return tag;
            }
        }
        return NONE;
    }
}
