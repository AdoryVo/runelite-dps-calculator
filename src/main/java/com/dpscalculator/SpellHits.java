package com.dpscalculator;
import java.util.Map;
import java.util.Collections;

public class SpellHits {
    //add crystal staff later ._.
    //volatile nm staff formula: floor(min(visible magic, 99) * (7/12) + (7/6))
    //eldritch nm staff formula: floor(44-floor((99-min(visible magic, 99))/2.27))
    static final Map<String, Integer> SPELL_MAX_HITS;
    static {
        final Map<String, Integer> valuesByName = Map.ofEntries(
                Map.entry("WIND_STRIKE", 2),
                Map.entry("WIND_BOLT", 9),
                Map.entry("WIND_BLAST", 13),
                Map.entry("WIND_WAVE", 17),
                Map.entry("WIND_SURGE", 21),
                Map.entry("WATER_STRIKE", 4),
                Map.entry("WATER_BOLT", 10),
                Map.entry("WATER_BLAST", 14),
                Map.entry("WATER_WAVE", 18),
                Map.entry("WATER_SURGE", 22),
                Map.entry("EARTH_STRIKE", 6),
                Map.entry("EARTH_BOLT", 11),
                Map.entry("EARTH_BLAST", 15),
                Map.entry("EARTH_WAVE", 19),
                Map.entry("EARTH_SURGE", 23),
                Map.entry("FIRE_STRIKE", 8),
                Map.entry("FIRE_BOLT",12),
                Map.entry("FIRE_BLAST", 16),
                Map.entry("FIRE_WAVE", 20),
                Map.entry("FIRE_SURGE", 24),
                Map.entry("CRUMBLE_UNDEAD", 15),
                Map.entry("IBAN_BLAST", 25),
                Map.entry("SARADOMIN_STRIKE", 20),
                Map.entry("CLAWS_OF_GUTHIX", 20),
                Map.entry("FLAMES_OF_ZAMORAK", 20),
                Map.entry("SMOKE_RUSH", 14),
                Map.entry("SMOKE_BURST", 18),
                Map.entry("SMOKE_BLITZ", 23),
                Map.entry("SMOKE_BARRAGE", 27),
                Map.entry("SHADOW_RUSH", 15),
                Map.entry("SHADOW_BURST", 19),
                Map.entry("SHADOW_BLITZ", 24),
                Map.entry("SHADOW_BARREGE", 28),
                Map.entry("BLOOD_RUSH", 16),
                Map.entry("BLOOD_BURST", 21),
                Map.entry("BLOOD_BLITZ", 25),
                Map.entry("BLOOD_BARRAGE", 29),
                Map.entry("ICE_RUSH", 17),
                Map.entry("ICE_BURST", 22),
                Map.entry("ICE_BLITZ", 26),
                Map.entry("ICE_BARRAGE", 30),
                Map.entry("SLAYER_DART", -100),
                Map.entry("TRIDENT", -101),
                Map.entry("TOXIC_TRIDENT", -102),
                Map.entry("SWAMP_LIZARD", -103),
                Map.entry("ORANGE_SALAMANDER", -104),
                Map.entry("RED_SALAMANDER", -105),
                Map.entry("BLACK_SALAMANDER", -106),
                Map.entry("SANGUINESTI_STAFF", -107),
                Map.entry("VOLATILE_NIGHTMARE_STAFF", -108),
                Map.entry("ELDRITCH_NIGHTMARE_STAFF", -109),
                Map.entry("CRYSTAL_STAFF_PERFECTED", -110),
                Map.entry("CRYSTAL_STAFF_ATTUNED", -111),
                Map.entry("CRYSTAL_STAFF_BASIC", -112)
        );
        SPELL_MAX_HITS = Collections.unmodifiableMap(valuesByName);
    }

    public static int mageMax(String spell, int visibleMage) {
        int max = SPELL_MAX_HITS.get(spell);

        if(max > 0)
            return max;

        switch(max) {
            case -100:
                return (int) (visibleMage / 10.0) + 10;
            case -101:
                return (int) (visibleMage / 3.0) - 5;
            case -102:
                return (int) (visibleMage / 3.0) - 2;
            case -103:
                return (int) (0.5 + visibleMage * (64+56)/640.0);
            case -104:
                return (int) (0.5 + visibleMage * (64+59)/640.0);
            case -105:
                return (int) (0.5 + visibleMage * (64+77)/640.0);
            case -106:
                return (int) (0.5 + visibleMage * (64+92)/640.0);
            case -107:
                return (int) (visibleMage/3.0) - 1;
            case -108:
                return (int) (Math.min(visibleMage, 99) * (7.0/12) + (7.0/6));
            case -109:
                return 44 - (int)((99 - Math.min(visibleMage, 99))/2.27);
            case -110:
                return (int) (visibleMage / 3.0) + 6;
            case -111:
                return (int) (visibleMage / 3.0) - 9;
            case -112:
                return (int) (visibleMage / 3.0) - 15;
        }
        return -1; //shouldn't happen
    }
    public static void main(String[] args) {
        System.out.println(SpellHits.mageMax("VOLATILE_NIGHTMARE_STAFF", 75));
    }
}