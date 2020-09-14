package com.dpscalculator;
import java.util.Set;
// todo: add spec support perhaps in a different function?
// todo: add set effects and whatnot to max/attackroll
// todo: magic stuff :(
public class Player
{
    private final int atk, str, mage, range, HP, currHP, pray, currPray;
    private final int stabBonus, slashBonus, crushBonus, magicBonus, rangeBonus, melStr, rangeStr;
    private final double mageStr;
    private final int[] specials;
    public Player(int atk, int str, int mage, int range, int HP, int currHP, int pray, int currPray,
                  int stabBonus, int slashBonus, int crushBonus, int magicBonus, int rangeBonus,
                  int melStr, int rangeStr, double mageStr, int[] specials)
    {
        this.atk = atk;
        this.str = str;
        this.mage = mage;
        this.range = range;
        this.HP = HP;
        this.currHP = currHP;
        this.pray = pray;
        this.currPray = currPray;
        this.stabBonus = stabBonus;
        this.slashBonus = slashBonus;
        this.crushBonus = crushBonus;
        this.magicBonus = magicBonus;
        this.rangeBonus = rangeBonus;
        this.melStr = melStr;
        this.rangeStr = rangeStr;
        this.mageStr = mageStr;
        this.specials = specials;

        /*
        specials should be in this format:
        0: black mask/helm (0 for none, 1 for normal, 2 for imbued)
        1: salve amulet (0 for none, 1 for normal, 2 for i, 3 for e, 4 for ei)
        2: void (0 for none, 1/2 for melee/elite, 3/4 for range/elite, 5/6 for mage/elite)
        3: tome of fire (0 for none, 1 for equipped)
        4: chaos gauntlets (0 for none, 1 for equipped)
        5: charge (0 for none, 1 for cast active)
        */
    }

    /**
     * Returns max attack roll (player)
     *
     * @param style  stab, slash, crush, magic, range -> Domain: [0, 4]
     * @param prayer prayer boost
     * @param stance accurate, controlled, aggressive/rapid, defensive-> Domain: [0, 3]
     * @param potion level boost from potion
     * @param type   undead, demon, dragon, vorkath -> Domain: [0, 3]
     * @return max attack roll
     */
    public int attackRoll(int style, double prayer, int stance, int potion, int type)
    {
        int[] levels = {atk, atk, atk, mage, range};
        int[] equipBonus = {stabBonus, slashBonus, crushBonus, magicBonus, rangeBonus};
        int[] stanceBoosts = {3, 1, 0, 0}; //accurate, controlled, aggressive/rapid, defensive/none
        //note for magic: bonuses only apply if using trident like
        int effLevel = (int) (prayer * (potion + levels[style])) + stanceBoosts[stance] + 8;
        //check for void
        //style <3 = melee, =3 = mage, =4 = range
        if ((style < 3 && (specials[2] == 1 || specials[2] == 2))
                || (style == 4 && (specials[2] == 3 || specials[2] == 4)))
            effLevel *= 1.1;
        else if (style == 3 && (specials[2] == 5 || specials[2] == 6))
            effLevel *= 1.45;

        int baseRoll = effLevel * (equipBonus[style] + 64);

        return applyTypeBonus(style, type, baseRoll);
    }

    //max hit for mage
    public int maxHit(String spell, int potion, int type) {
        int base = SpellHits.mageMax(spell, mage+potion);
        Set<String> godSpells = Set.of("SARADOMIN_STRIKE", "CLAWS_OF_GUTHIX", "FLAMES_OF_ZAMORAK");
        if (godSpells.contains(spell) && specials[5] == 1)
            base = 30;
        else if (spell.contains("BOLT"))
            base += 3;

        double salveBonus = 0;
        if(type == 0 || type == 3) {
            if(specials[1] == 4)
                salveBonus = 0.2;
            else if(specials[1] == 2)
                salveBonus = 0.15;
        }

        //note: mageStr for magic max hit already includes magic void bonus, so we don't have to
        //explicitly check for that
        base *= 1 + mageStr + salveBonus;

        //consider slayer helm if salve bonus doesn't apply
        if(salveBonus == 0 && specials[0] == 2)
            base *= 1.15;

        if(specials[3] == 1 && spell.contains("FIRE"))
            base *= 1.5;

        return base;
    }

    //max hit for range/melee
    public int maxHit(int style, double prayer, int stance, int potion, int type)
    {
        if (style == 3)
        { //called with magic
            return -1;
        }
        int[] levels = {str, str, str, -1, range};
        int[] equipBonus = {melStr, melStr, melStr, -1, rangeStr};
        int[] stanceBoosts = {0, 1, 3, 0}; //accurate, controlled, aggressive/rapid, defensive
        if (style == 4)
        {
            stanceBoosts[0] = 3; //range accurate stance adds max hit boosts
            stanceBoosts[2] = 0; //range rapid doesn't give max hit boosts
        }

        int effLevel = (int) (prayer * (potion + levels[style])) + stanceBoosts[stance] + 8;

        if ((style < 3 && (specials[2] == 1 || specials[2] == 2))
                || (style == 4 && specials[2] == 3))
            effLevel *= 1.1;
        else if (style == 4 && specials[2] == 4)
            effLevel *= 1.125;

        int base = (int) (0.5 + (double) effLevel * ((double) equipBonus[style] + 64) / 640.0);

        return applyTypeBonus(style, type, base);
    }

    //salve/black mask for range/melee rolls
    private int applyTypeBonus(int style, int type, int base) {
        //melee
        if (style < 3)
        {
            //typecheck for undead here
            if ((type == 0 || type == 3) && (specials[1] == 3 || specials[1] == 4))
                base *= 1.2;
            else if ((type == 0 || type == 3) && specials[1] == 1 || specials[1] == 2)
                base *= 7.0 / 6;
            else if (specials[0] == 1 || specials[0] == 2) //assuming always on slayer task
                base *= 7.0 / 6;
        }
        //range
        else
        {
            if ((type == 0 || type == 3) && specials[1] == 4)
                base *= 1.2;
            else if ((type == 0 || type == 3) && specials[1] == 2)
                base *= 1.15; //trusting the DPS sheet over wiki on this one - could be wrong
            else if (specials[0] == 2) //assuming always on slayer task
                base *= 1.15;
        }

        return base;
    }

    /**
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        //ancestral magic set, void range
        Player p1 = new Player(99, 99, 99, 99, 99, 99, 99, 99,
                0, 0, 0, 147, 91, 0, 62, 0.23,
                new int[]{0, 0, 4, 0, 0, 0});
        assert p1.attackRoll(3, 1.25, 0, 10, 0) == 31017
                && p1.maxHit("SANGUINESTI_STAFF", 10, 0) == 43
                : "magic set 1 failed";
        assert p1.attackRoll(3, 1.25, 3, 10, 0) == 30384
                && p1.maxHit("WATER_SURGE", 10, 0) == 27
                : "magic set 2 failed";
        assert p1.attackRoll(4, 1.2, 2, 13, 0) == 24180
                && p1.maxHit(4, 1.23, 2, 13, 0) == 32
                : "ranged set 1 failed";

        //slayer helm max melee, mage + tome of fire
        Player p2 = new Player(99, 99, 99, 99, 99, 99, 99, 99,
                161, 0, 0, 154, 91, 148, 0,
                0.36, new int[]{2, 0, 0, 1, 0, 0});
        assert p2.attackRoll(0, 1.2, 2, 19, 0) == 39112
                && p2.maxHit(0, 1.23, 2, 19, 0) == 60
                : "melee set 1 failed";
        assert p2.attackRoll(3, 1.25, 3, 10, 0) == 36100
                && p2.maxHit("FIRE_SURGE", 10, 0) == 54
                : "magic set 1 failed";
    }
}
