import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Problem3_Fibonacci {

    public static List<BigInteger> firstNFibonacci(int n) {
        List<BigInteger> result = new ArrayList<>();
        if (n <= 0) return result;

        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;

        for (int i = 0; i < n; i++) {
            result.add(a);
            BigInteger next = a.add(b);
            a = b;
            b = next;
        }
        return result;
    }

    public static void main(String[] args) {
        List<BigInteger> fibs = firstNFibonacci(100);
        for (int i = 0; i < fibs.size(); i++) {
            System.out.println((i + 1) + ": " + fibs.get(i));
        }
    }
}
