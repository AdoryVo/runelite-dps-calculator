package com.dpscalculator;

import java.io.*;
import java.util.List;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class EquipmentItem
{
    public int stabBonus, slashBonus, crushBonus, magicBonus, rangeBonus;

    public int melStr, rangeStr;
    public double mageStr;

    public int specAcc, specDefStyle;
    public double specDmg;

    public int attackSpd;
    public int combatOptions;

    public EquipmentItem(String itemName)
    {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNullValue("-100");
        TsvParser parser = new TsvParser(settings);
        List<String[]> allRows = parser.parseAll(new File("src/main/resources/items.tsv"));

        for (String[] row : allRows)
        {
            if (row[0] != null && row[0].equals(itemName))
            {
                this.stabBonus = processCol(row[1]);
                this.slashBonus = processCol(row[2]);
                this.crushBonus = processCol(row[3]);
                this.magicBonus = processCol(row[4]);
                this.rangeBonus = processCol(row[5]);

                this.melStr = processCol(row[11]);
                this.rangeStr = processCol(row[12]);
                this.mageStr = Double.parseDouble(row[13]);

                this.specAcc = processCol(row[15]);
                this.specDmg = (Double.parseDouble(row[16]) + 1) * (Double.parseDouble(row[17]) + 1) - 1;
                this.specDefStyle = processCol(row[18]);

                this.attackSpd = processCol(row[21]);
                this.combatOptions = processCol(row[22]);
                break;
            }
        }
    }

    public int processCol(String value)
    {
        switch (value.toLowerCase())
        {
            case ("stab"):
                return 0;
            case ("slash"):
                return 1;
            case ("crush"):
                return 2;
            case ("magic"):
                return 3;
            case ("ranged"):
                return 4;
        }

        return Integer.parseInt(value);
    }

    /**
     * Tester
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        EquipmentItem ei = new EquipmentItem("Ham joint");
    }
}
