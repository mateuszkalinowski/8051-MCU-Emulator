package converters;

/**
 * Created by Mateusz on 28.04.2017.
 * Project 8051 MCU Emulator
 */
public class Converters {
    public static int[] bcdto7seg(String liczba) {
        if(liczba.equals("0000"))
            return new int[] {1,1,1,1,1,1,0};
        else if(liczba.equals("0001"))
            return new int[] {0,1,1,0,0,0,0};
        else if(liczba.equals("0010"))
            return new int[] {1,1,0,1,1,0,1};
        else if(liczba.equals("0011"))
            return new int[] {1,1,1,1,0,0,1};
        else if(liczba.equals("0100"))
            return new int[] {0,1,1,0,0,1,1};
        else if(liczba.equals("0101"))
            return new int[] {1,0,1,1,0,1,1};
        else if(liczba.equals("0110"))
            return new int[] {1,0,1,1,1,1,1};
        else if(liczba.equals("0111"))
            return new int[] {1,1,1,0,0,0,0};
        else if(liczba.equals("1000"))
            return new int[] {1,1,1,1,1,1,1};
        else if(liczba.equals("1001"))
            return new int[] {1,1,1,1,0,1,1};
        else
            return new int[] {1,1,1,1,1,1,0};
    }
}
