import java.util.List;
import java.util.Arrays;

public class Problem1_Sum {

    public static int sumForLoop(List<Integer> numbers) {
        int total = 0;
        for (int i = 0; i < numbers.size(); i++) {
            total += numbers.get(i);
        }
        return total;
    }

    public static int sumWhileLoop(List<Integer> numbers) {
        int total = 0;
        int i = 0;
        while (i < numbers.size()) {
            total += numbers.get(i);
            i++;
        }
        return total;
    }

    public static int sumRecursive(List<Integer> numbers) {
        return sumRecursiveHelper(numbers, 0);
    }

    private static int sumRecursiveHelper(List<Integer> numbers, int index) {
        if (index == numbers.size()) {
            return 0;
        }
        return numbers.get(index) + sumRecursiveHelper(numbers, index + 1);
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("For-loop sum:   " + sumForLoop(nums));
        System.out.println("While-loop sum: " + sumWhileLoop(nums));
        System.out.println("Recursive sum:  " + sumRecursive(nums));
    }
}
