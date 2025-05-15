package org.example.fibonaccivisualization.model;

import java.util.*;

public class FibonacciCalculator {
    private int recursionCount = 0;
    private int totalCalls = 0;
    private int cacheHits = 0;
    private final List<Integer> memoizationCallOrder = new ArrayList<>();
    private final Map<Integer, Integer> callCounts = new HashMap<>();

    public void resetCounters() {
        recursionCount = 0;
        totalCalls = 0;
        cacheHits = 0;
        memoizationCallOrder.clear();
        callCounts.clear();
    }

    public int getRecursionCount() {
        return recursionCount;
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public List<Integer> getMemoizationCallOrder() {
        return memoizationCallOrder;
    }

    public Map<Integer, Integer> getCallCounts() {
        return callCounts;
    }

    public long recursive(int n) {
        recursionCount++;
        if (n <= 1) return n;
        return recursive(n - 1) + recursive(n - 2);
    }

    public FibNode buildRecursiveTree(int n) {
        resetCounters();
        FibNode node = new FibNode(n);
        if (n <= 1) {
            node.result = n;
            recursionCount++;
            return node;
        }

        node.left = buildRecursiveTree(n - 1);
        node.right = buildRecursiveTree(n - 2);
        node.result = node.left.result + node.right.result;
        recursionCount++;

        return node;
    }

    public long calculateResult(FibNode node) {
        if (node == null) return 0;
        if (node.value <= 1) return node.value;
        return calculateResult(node.left) + calculateResult(node.right);
    }

    public long memoized(int n) {
        totalCalls = 0;
        cacheHits = 0;
        Map<Integer, Long> memo = new HashMap<>();
        return memoizedHelper(n, memo);
    }

    private long memoizedHelper(int n, Map<Integer, Long> memo) {
        totalCalls++;

        // Count calls per value
        callCounts.put(n, callCounts.getOrDefault(n, 0) + 1);

        // Record call sequence
        memoizationCallOrder.add(n);

        if (memo.containsKey(n)) {
            cacheHits++;
            return memo.get(n);
        }

        if (n <= 1) {
            memo.put(n, (long) n);
            return n;
        }

        long result = memoizedHelper(n - 1, memo) + memoizedHelper(n - 2, memo);
        memo.put(n, result);
        return result;
    }

    public Map<Integer, Long> getMemoizedWithVisualization(int n) {
        resetCounters();
        Map<Integer, Long> memo = new HashMap<>();
        memoizedHelper(n, memo);
        return memo;
    }

    public long tabulated(int n) {
        if (n <= 1) return n;

        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    public List<Long> getTabulatedWithSteps(int n) {
        List<Long> dp = new ArrayList<>(n + 1);

        // Initialize with zeros
        for (int i = 0; i <= n; i++) {
            dp.add(0L);
        }

        // Set base cases
        if (n >= 0) dp.set(0, 0L);
        if (n >= 1) dp.set(1, 1L);

        // Calculate remaining values
        for (int i = 2; i <= n; i++) {
            dp.set(i, dp.get(i - 1) + dp.get(i - 2));
        }

        return dp;
    }
}
