
package org.example.project.model;

public class FibonacciCalculator {
    public long recursive(int n) {
        if (n <= 1) return n;
        return recursive(n - 1) + recursive(n - 2);
    }

    public long recursiveWithTrace(int n) {
        StringBuilder trace = new StringBuilder();
        long result = traceRecursive(n, trace);
        System.out.println(trace);
        return result;
    }

    private long traceRecursive(int n, StringBuilder trace) {
        trace.append("F(").append(n).append(") called\n");
        if (n <= 1) return n;
        return traceRecursive(n - 1, trace) + traceRecursive(n - 2, trace);
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
