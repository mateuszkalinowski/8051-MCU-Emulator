package converters;

/**
 * Created by Mateusz on 28.04.2017.
 * Project 8051 MCU Emulator
 */
public class Converters {
    public static int[] bcdto7seg(String liczba) {
        switch (liczba) {
            case "0000":
                return new int[]{ 1, 1, 1, 1, 1, 1, 0 };
            case "0001":
                return new int[]{ 0, 1, 1, 0, 0, 0, 0 };
            case "0010":
                return new int[]{ 1, 1, 0, 1, 1, 0, 1 };
            case "0011":
                return new int[]{ 1, 1, 1, 1, 0, 0, 1 };
            case "0100":
                return new int[]{ 0, 1, 1, 0, 0, 1, 1 };
            case "0101":
                return new int[]{ 1, 0, 1, 1, 0, 1, 1 };
            case "0110":
                return new int[]{ 1, 0, 1, 1, 1, 1, 1 };
            case "0111":
                return new int[]{ 1, 1, 1, 0, 0, 0, 0 };
            case "1000":
                return new int[]{ 1, 1, 1, 1, 1, 1, 1 };
            case "1001":
                return new int[]{ 1, 1, 1, 1, 0, 1, 1 };
            case "1010":
                return new int[]{ 1, 1, 1, 0, 1, 1, 1 };
            case "1011":
                return new int[]{ 0, 0, 1, 1, 1, 1, 1 };
            case "1100":
                return new int[]{ 1, 0, 0, 1, 1, 1, 0 };
            case "1101":
                return new int[]{ 0, 1, 1, 1, 1, 0, 1 };
            case "1110":
                return new int[]{ 1, 0, 0, 1, 1, 1, 1 };
            case "1111":
                return new int[]{ 1, 0, 0, 0, 1, 1, 1 };
            default:
                return new int[]{ 1, 1, 1, 1, 1, 1, 0 };
        }
    }
}
