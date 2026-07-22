import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem4_LargestNumber {

    public static String largestNumber(List<Integer> numbers) {
        List<String> asStrings = new ArrayList<>();
        for (int n : numbers) {
            asStrings.add(String.valueOf(n));
        }

        asStrings.sort((a, b) -> (b + a).compareTo(a + b));

        StringBuilder sb = new StringBuilder();
        for (String s : asStrings) {
            sb.append(s);
        }

        if (sb.charAt(0) == '0') {
            return "0";
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(50, 2, 1, 9);
        System.out.println(largestNumber(nums));
    }
}
