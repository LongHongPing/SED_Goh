import java.util.ArrayList;
import java.util.BitSet;

/** 布隆过滤器 */
public class BloomFilter {
    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] seeds = new int[] { 3,5, 7, 11, 13, 31, 37, 61 };  //质数可以更好的防止hash冲突,位数越多误判率越低
    private boolean[] bits = new boolean[DEFAULT_SIZE];
    private SimpleHash[] func = new SimpleHash[seeds.length];

    private static int cnt = 1;
    /** 布隆过滤器 */
    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }
    /** 映射码字到过滤器相应位置 */
    public static int[] findPosition(byte[][] codeWords,int filterSize){
        int[] indexPositions = new int[cnt];
        for(byte[] codeWord : codeWords){
            int x = 0;
            indexPositions = append(indexPositions,x%DEFAULT_SIZE);
        }
        return indexPositions;
    }
    /** 追加内容 */
    private static int[] append(int[] values, int value) {

        return values;
    }
    /** 添加内容 */
    public void add(byte[][] codeWords) {
       int[] position = findPosition(codeWords,DEFAULT_SIZE);
        for (int i : position) {
            bits[i] = true;
        }

    }
    /** 检验内容 */
    public boolean check(byte[][] value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        int[] position = findPosition(value,DEFAULT_SIZE);
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