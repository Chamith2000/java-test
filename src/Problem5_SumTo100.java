public class Problem5_SumTo100 {

    private static final int[] DIGITS = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final int TARGET = 100;
    private static int solutionCount = 0;

    public static void main(String[] args) {
        solve(1, String.valueOf(DIGITS[0]), DIGITS[0], DIGITS[0]);
        System.out.println("Total solutions found: " + solutionCount);
    }

    private static void solve(int pos, String expression, long total, long lastTerm) {
        if (pos == DIGITS.length) {
            if (total == TARGET) {
                System.out.println(expression + " = " + TARGET);
                solutionCount++;
            }
            return;
        }

        int digit = DIGITS[pos];

        solve(pos + 1, expression + " + " + digit, total + digit, digit);

        solve(pos + 1, expression + " - " + digit, total - digit, -digit);

        long newLastTerm;
        if (lastTerm >= 0) {
            newLastTerm = lastTerm * 10 + digit;
            solve(pos + 1, expression + digit, total - lastTerm + newLastTerm, newLastTerm);
        } else {
            newLastTerm = lastTerm * 10 - digit;
            solve(pos + 1, expression + digit, total - lastTerm + newLastTerm, newLastTerm);
        }
    }
}
