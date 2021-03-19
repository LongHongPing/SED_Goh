package utils;

import java.util.ArrayList;

/** 布隆过滤器 */
public class BloomFilter {
    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] seeds = new int[] { 3,5, 7, 11, 13, 31, 37, 61 };  //质数可以更好的防止hash冲突,位数越多误判率越低
    private SimpleHash[] func = new SimpleHash[seeds.length];
    public boolean[] bits = new boolean[DEFAULT_SIZE];

    /** 布隆过滤器 */
    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }
    public BloomFilter(ArrayList<Boolean> index){

    }
    /** 映射码字到过滤器相应位置 */
    public static ArrayList<Integer> findPosition(ArrayList<byte[]> codeWords){
        //int[] indexPositions = new int[cnt];
        ArrayList<Integer> indexPositions = new ArrayList<>();
        for(byte[] codeWord : codeWords){
            int x = findUint(codeWord);
            if(x < 0){
                return null;
            }
            indexPositions.add(x%DEFAULT_SIZE);
        }
       // System.out.println(indexPositions);
        return indexPositions;
    }
    private static int findUint(byte[] codeword){
        for(byte tmp : codeword){
            if(tmp >= 0){
                return tmp;
            }
        }
        return -1;
    }
    /** 添加内容 */
    public void add(ArrayList<byte[]> codeWords) {
       ArrayList<Integer> position = findPosition(codeWords);
        for (int i : position) {
            bits[i] = true;
        }

    }
    /** 检验内容 */
    public boolean check(ArrayList<byte[]> value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        ArrayList<Integer> position = findPosition(value);
        for (int i : position) {
            ret = ret && bits[i];
        }
        return ret;
    }

    /** 内部类，Hash函数 */
    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
}