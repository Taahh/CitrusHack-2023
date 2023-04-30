class Run {
    public static void main(String[] args) {
        Solution solution = new Solution();
        assert Arrays.equals(solution.twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{0, 1});
        assert Arrays.equals(solution.twoSum(new int[]{3,2,4}, 6), new int[]{1,2});
        assert Arrays.equals(solution.twoSum(new int[]{3,3}, 6), new int[]{0,1});
    }
}