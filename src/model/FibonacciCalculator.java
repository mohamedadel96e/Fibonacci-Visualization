package model;

import java.util.function.Consumer;

public class FibonacciCalculator {
    public long recursive(int n) {
        if (n <= 1) return n;
        return recursive(n - 1) + recursive(n - 2);
    }

    public FibNode buildRecursiveTree(int n) {
        FibNode node = new FibNode(n);
        if (n <= 1) return node;
        node.left = buildRecursiveTree(n - 1);
        node.right = buildRecursiveTree(n - 2);
        return node;
    }
    private long traceRecursiveAnimated(int n, Consumer<String> traceConsumer) {
        traceConsumer.accept("F(" + n + ") called\n");
        if (n <= 1) return n;
        return traceRecursiveAnimated(n - 1, traceConsumer) + traceRecursiveAnimated(n - 2, traceConsumer);
    }

    public long memoized(int n) {
        Long[] memo = new Long[n + 1];
        return memoizedHelper(n, memo);
    }

    private long memoizedHelper(int n, Long[] memo) {
        if (memo[n] != null) return memo[n];
        if (n <= 1) return memo[n] = (long)n;
        return memo[n] = memoizedHelper(n - 1, memo) + memoizedHelper(n - 2, memo);
    }

    public long tabulated(int n) {
        if (n <= 1) return n;
        long[] dp = new long[n + 1];
        dp[0] = 0; dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
