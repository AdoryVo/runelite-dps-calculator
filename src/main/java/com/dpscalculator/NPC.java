package com.dpscalculator;

public class NPC {
    private final int defLvl, stabDef, slashDef, crushDef, magicLvl, magicDef, rangeDef;
    public NPC(int defLvl, int stabDef, int slashDef, int crushDef,
               int magicLvl, int magicDef, int rangeDef) {
        this.defLvl = defLvl;
        this.stabDef = stabDef;
        this.slashDef = slashDef;
        this.crushDef = crushDef;
        this.magicLvl = magicLvl;
        this.magicDef = magicDef;
        this.rangeDef = rangeDef;
    }
    //returns max defence roll for NPC
    public int defRoll(int style) { //stab, slash, crush, magic, range 0-4
        int effLevel = (style == 3) ? defLvl : magicLvl;
        int[] bonuses = {stabDef, slashDef, crushDef, magicDef, rangeDef};
        int defBonus = bonuses[style];

        return (effLevel + 9) * (defBonus + 64);
    }
}
