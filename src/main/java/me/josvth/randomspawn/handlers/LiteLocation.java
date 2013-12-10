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


    public String toString(String playerName)
    {
        //Expected Output "x,z" f.e. "Diemex@25,25"
        return playerName + "@" + x + "," + "z";
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
            String[] firstCut = input.split("@");
            if (firstCut.length > 1)
            {
                loc.playerName = firstCut[0];
                String[] secondCut = firstCut[1].split(",");
                if (secondCut.length > 1)
                {
                    //Remove everything besides digits from the string
                    Pattern keepDigits = Pattern.compile("[^0-9]");
                    secondCut[0] = keepDigits.matcher(secondCut[0]).replaceAll("");
                    secondCut[1] = keepDigits.matcher(secondCut[1]).replaceAll("");

                    if (secondCut[0].length() > 0 && secondCut[1].length() > 0)
                    {
                        loc.x = Integer.parseInt(secondCut[0]);
                        loc.z = Integer.parseInt(secondCut[1]);
                        //Only now the parsing was successful = the input was valid
                        return loc;
                    }
                }
                return null;
            }
        }
        return null;
    }
}
