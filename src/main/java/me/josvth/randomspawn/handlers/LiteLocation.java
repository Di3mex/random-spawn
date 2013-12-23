package me.josvth.randomspawn.handlers;


import java.util.regex.Pattern;

/**
 * @author Diemex
 */
public class LiteLocation
{
    int x;
    int z;
    String playerName;


    LiteLocation(){}


    LiteLocation(int x, int z)
    {
        this.x = x;
        this.z = z;
    }


    @Override
    public String toString()
    {
        //Expected Output "x,z" f.e. "Diemex@25,25"
        return x + "," + z;
    }


    /**
     * Expected input: "PlayerName@x,z" f.e. Diemex@25,235
     *
     * @param input input string
     *
     * @return a parsed LiteLocation or null of the input is invalid
     */
    public static LiteLocation fromString(String input)
    {
        LiteLocation loc = new LiteLocation();
        if (input != null)
        {
            String[] cut = input.split(",");
            if (cut.length > 1)
            {
                //Remove everything besides digits from the string
                Pattern keepDigits = Pattern.compile("[^0-9]");
                cut[0] = keepDigits.matcher(cut[0]).replaceAll("");
                cut[1] = keepDigits.matcher(cut[1]).replaceAll("");

                if (cut[0].length() > 0 && cut[1].length() > 0)
                {
                    loc.x = Integer.parseInt(cut[0]);
                    loc.z = Integer.parseInt(cut[1]);
                    //Only now the parsing was successful = the input was valid
                    return loc;
                }
            }
        }
        return null;
    }
}
