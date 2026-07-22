import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem2_Combine {

    public static <A, B> List<Object> combine(List<A> listA, List<B> listB) {
        List<Object> result = new ArrayList<>();
        int maxLength = Math.max(listA.size(), listB.size());

        for (int i = 0; i < maxLength; i++) {
            if (i < listA.size()) {
                result.add(listA.get(i));
            }
            if (i < listB.size()) {
                result.add(listB.get(i));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> letters = Arrays.asList("a", "b", "c");
        List<Integer> numbers = Arrays.asList(1, 2, 3);

        System.out.println(combine(letters, numbers));
    }
}
