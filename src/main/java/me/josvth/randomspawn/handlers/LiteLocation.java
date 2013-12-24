package me.josvth.randomspawn.handlers;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Diemex
 */
public class LiteLocation
{
    //Remove everything besides digits from the string
    private static Pattern keepDigits = Pattern.compile("[^0-9]");

    int x;
    Integer y; //so we have a null value and know if it is set or not
    int z;
    String playerName;

    LiteLocation(){}


    public LiteLocation(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public LiteLocation(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public String toString()
    {
        //Expected Output "x,z" f.e. "23,25" or "12,13,14" if y coord is set
        return x + "," + (y != null ? y.toString()+"," : "") + z;
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
        LiteLocation loc = null;
        if (input != null)
        {
            String[] cut = input.split(",");
            if (cut.length > 1)
            {

                List<Integer> parsedNumbers = new ArrayList<Integer>();
                for (int i = 0; i < cut.length; i++)
                {
                    cut[i] = keepDigits.matcher(cut[i]).replaceAll("");
                    if (cut[i].length() > 0)
                        parsedNumbers.add(Integer.parseInt(cut[i]));
                }

                if (parsedNumbers.size() == 2)
                {
                    loc = new LiteLocation();
                    loc.x = parsedNumbers.get(0);
                    loc.z = parsedNumbers.get(1);
                } else if (parsedNumbers.size() > 2)
                {
                    loc = new LiteLocation();
                    loc.x = parsedNumbers.get(0);
                    loc.y = parsedNumbers.get(1);
                    loc.z = parsedNumbers.get(2);
                }
            }
        }
        return loc;
    }
}
