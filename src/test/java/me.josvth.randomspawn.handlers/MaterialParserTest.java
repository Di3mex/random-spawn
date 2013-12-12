package me.josvth.randomspawn.handlers;


import org.bukkit.Material;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests to see if {@link me.josvth.randomspawn.handlers.MaterialParser} works
 * @author Diemex
 */
public class MaterialParserTest
{
    @Test
    public void validStringInput ()
    {
        assertEquals(Material.ARROW.getId(), MaterialParser.parseMaterial(Material.ARROW.name()));
    }

    @Test
    public void validBlockIdInput ()
    {
        assertEquals(Material.ARROW.getId(), MaterialParser.parseMaterial(Material.ARROW.getId() + ""));
    }

    @Test
    public void correctableInput ()
    {
        assertEquals(Material.ARROW.getId(), MaterialParser.parseMaterial(Material.ARROW.name() + "12"));
    }

    @Test
    public void incorrectableInput ()
    {
        assertTrue(MaterialParser.parseMaterial("herp" + Material.ARROW.name() + "derp") < 0);
    }
}
