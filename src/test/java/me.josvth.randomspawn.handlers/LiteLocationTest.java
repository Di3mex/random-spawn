package me.josvth.randomspawn.handlers;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Diemex
 */
public class LiteLocationTest
{
    @Test
    public void normal_2coords()
    {
        LiteLocation location = LiteLocation.fromString("321,123");
        assertEquals(321, location.x);
        assertEquals(123, location.z);
    }

    @Test
    public void normal_3coords()
    {
        LiteLocation location = LiteLocation.fromString("567,321,123");
        assertEquals(567, location.x);
        assertEquals(321, (int) location.y);
        assertEquals(123, location.z);
    }

    @Test
    public void fail_1coord()
    {
        LiteLocation location = LiteLocation.fromString("321");
        assertEquals(null, location);
    }
}
