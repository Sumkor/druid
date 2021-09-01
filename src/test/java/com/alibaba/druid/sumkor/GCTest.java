package com.alibaba.druid.sumkor;

import com.alibaba.druid.sql.test.TestUtils;
import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * @author Sumkor
 * @since 2021/9/1
 */
public class GCTest {

    /**
     * -verbose:gc -XX:+PrintGCDetails -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8
     *
     * 复制算法介绍：
     * https://blog.csdn.net/zc19921215/article/details/83029952
     * 因为新生代对象生命周期一般很短，现在一般将该内存区域划分为三块部分：一块大的叫 Eden，两块小的叫 Survivor。他们之间的比例一般为 8:1:1。
     * 使用的时候只使用 Eden + 一块 Survivor。用 Eden 区用满时会进行一次 minor gc，将存活下面的对象复制到另外一块 Survivor上。如果另一块 Survivor 放不下，对象直接进入老年代。
     *
     * @see com.alibaba.druid.benckmark.sql.MySqlPerfTest
     */
    @Test
    public void gc() {
        long startYGC = TestUtils.getYoungGC();
        long startYGCTime = TestUtils.getYoungGCTime();
        long startFGC = TestUtils.getFullGC();
        long startMillis = System.currentTimeMillis();

        System.out.println("Start------------" + " ygc " + startYGC + ", ygct " + startYGCTime + ", fgc " + startFGC + "\r\n");

        WeakReference<byte[]> weakReference01 = new WeakReference<byte[]>(new byte[1024 * 1024 * 3]);
        WeakReference<byte[]> weakReference02 = new WeakReference<byte[]>(new byte[1024 * 1024 * 3]);

        //System.out.println("weakReference01 = " + weakReference01.get());
        //System.out.println("weakReference02 = " + weakReference02.get());

        System.gc();

        long millis = System.currentTimeMillis() - startMillis;
        long ygc = TestUtils.getYoungGC() - startYGC;
        long ygct = TestUtils.getYoungGCTime() - startYGCTime;
        long fgc = TestUtils.getFullGC() - startFGC;

        System.out.println("End--------------" + " ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc + ", ms " + millis + "\r\n");
        /**
         * 执行结果：
         * https://blog.csdn.net/qq_41701363/article/details/90551189
         *
         * [GC (Allocation Failure) [PSYoungGen: 8041K->1012K(9216K)] 8041K->2844K(19456K), 0.0022382 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [GC (Allocation Failure) [PSYoungGen: 9204K->997K(9216K)] 11036K->3028K(19456K), 0.0034608 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [GC (Allocation Failure) [PSYoungGen: 9189K->1013K(9216K)] 11220K->3249K(19456K), 0.0017702 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * Start------------ ygc 3, ygct 7, fgc 0
         *
         * [GC (Allocation Failure) [PSYoungGen: 6885K->1013K(9216K)] 9121K->3666K(19456K), 0.0014629 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [GC (System.gc()) [PSYoungGen: 7317K->1013K(9216K)] 9970K->3698K(19456K), 0.0016832 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [Full GC (System.gc()) [PSYoungGen: 1013K->0K(9216K)] [ParOldGen: 2684K->2198K(10240K)] 3698K->2198K(19456K), [Metaspace: 6944K->6944K(1056768K)], 0.0107511 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
         * End-------------- ygc 2, ygct 3, fgc 1, ms 16
         *
         * Heap
         *  PSYoungGen      total 9216K, used 613K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *   eden space 8192K, 7% used [0x00000000ff600000,0x00000000ff699608,0x00000000ffe00000)
         *   from space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
         *   to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
         *  ParOldGen       total 10240K, used 2198K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   object space 10240K, 21% used [0x00000000fec00000,0x00000000fee259a8,0x00000000ff600000)
         *  Metaspace       used 6964K, capacity 7142K, committed 7296K, reserved 1056768K
         *   class space    used 807K, capacity 821K, committed 896K, reserved 1048576K
         *
         * Process finished with exit code 0
         *
         * 分析：
         * GC 表明进行了一次垃圾回收，前面没有 Full 修饰，表明这是一次 Minor GC ,注意它不表示只 GC 新生代，并且现有的不管是新生代还是老年代都会 STW。
         * Allocation Failure 表明本次引起 GC 的原因是因为在年轻代中没有足够的空间能够存储新的数据了。
         *
         * 流程：
         * 1. 在执行代码之前，年轻代中已有 6885K 的占用了。
         * 2. 创建 weakReference01 对象时，年轻代中容量不足，由于 Allocation Failure 触发一次 GC，为年轻代腾出空间。
         * 3. 创建 weakReference02 对象时，年轻代中容量充足，直接存入。
         * 4. 执行 System.gc()，分别触发一次 GC 和 FullGC，清理年轻代和老年代
         */
    }
}
