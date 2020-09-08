package com.dpscalculator;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.File;
import java.util.List;

public class NPC
{
    private int defLvl, magicLvl, stabDef, slashDef, crushDef, magicDef, rangeDef, type;

    public NPC(String npcName)
    {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNullValue("-100");
        TsvParser parser = new TsvParser(settings);
        List<String[]> allRows = parser.parseAll(new File("src/main/resources/npc_stats.tsv"));

        for (String[] row : allRows)
        {
            if (row[0] != null && row[0].equals(npcName))
            {
                this.defLvl = processCol(row[6]);
                this.magicLvl = processCol(row[8]);

                this.stabDef = processCol(row[21]);
                this.slashDef = processCol(row[22]);
                this.crushDef = processCol(row[23]);
                this.magicDef = processCol(row[24]);
                this.rangeDef = processCol(row[25]);

                if (row[27] == "-100")
                {
                    row[27] = "no type";
                }
                this.type = processCol(row[27]);

                break;
            }
        }
    }

    public int processCol(String value)
    {
        switch (value.toLowerCase())
        {
            case ("no type"):
                return -1;
            case ("undead"):
                return 0;
            case ("demon"):
                return 1;
            case ("dragon"):
                return 2;
            case ("vorkath"):
                return 3;
            case ("fire"):
                return 4;
            case ("kalphite"):
                return 5;
            case ("kurask"):
                return 6;
            case ("guardian"):
                return 7;
        }

        return Integer.parseInt(value);
    }

    /**
     * Returns the max defence roll.
     *
     * @param style stab, slash, crush, magic, range -> Domain: [0, 4]
     * @return max defence roll
     */
    public int defRoll(int style)
    {
        int effLevel = (style == 3) ? defLvl : magicLvl;
        int[] bonuses = {stabDef, slashDef, crushDef, magicDef, rangeDef};
        int defBonus = bonuses[style];

        return (effLevel + 9) * (defBonus + 64);
    }

    public int getType()
    {
        return type;
    }

    /**
     * Tester
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        NPC npc = new NPC("Torag");
    }
}
