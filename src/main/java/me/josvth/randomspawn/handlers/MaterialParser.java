package me.josvth.randomspawn.handlers;


import org.bukkit.Material;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author Diemex
 */
public class MaterialParser
{
    private final static Pattern keepNumbers = Pattern.compile("[^0-9]");
    private final static Pattern rmWhitespace = Pattern.compile("\\s"); //Includes tabs/newline characters
    private final static Pattern keepLetters = Pattern.compile("[^a-zA-Z_]");

    /**
     * Parse a given string into into it's material representation
     *
     * @param blockString input string.
     *                    <pre>Expected input: Either materialname or blockid of material
     *                    </pre>
     * @return the blockid or a negative number if selected input couldn't be parsed. Doesn't return material so we also support mods that add new blocks
     */
    public static int parseMaterial (String blockString)
    {
        if (blockString == null)
            blockString = "";
        //Strip all whitespace
        if (rmWhitespace.matcher(blockString).find())
        {
            blockString = rmWhitespace.matcher(blockString).replaceAll("");
        }

        //First try to match with the bukkit name, if it fails strip all letters
        Material material = Material.matchMaterial(blockString);
        int blockId = -1;

        if (material != null)
            blockId = material.getId();
        /* couldn't be matched by enum constant */
        else
        {
            /* try as number (blockId) */
            String tempId = keepNumbers.matcher(blockString).replaceAll("");
            if (!tempId.isEmpty())
                material = Material.getMaterial(tempId);
            /* still fail -> try as enum again but strip numbers */
            if (material == null)
                material = Material.matchMaterial(keepLetters.matcher(blockString).replaceAll(""));
            //If value wasn't found in the material enum, return the blockId if available
            if (material != null)
                blockId = material.getId();
            else
            {
                blockString = keepNumbers.matcher(blockString).replaceAll("");
                if (blockString.length() > 0)
                    blockId = Integer.parseInt(blockString); //this could also be negative
            }
        }

        return blockId;
    }


    /**
     * Convert material ids to their string representation. Keep item ids that haven't been recognized as numerical item ids.
     *
     * @param itemIds collection of item ids
     *
     * @return the converted strings
     */
    public static String[] toMaterials(Collection <Integer> itemIds)
    {
        String[] materials = new String[itemIds.size()];
        int i = 0; //Collection doesn't have get()
        for (Integer itemId : itemIds)
        {
            Material matched = Material.getMaterial(itemId);
            if (matched != null)
                materials[i] = matched.name();
            else
                materials[i] = Integer.toString(itemId);
            i++;
        }
        return materials;
    }
}
