package java.util.concurrent.atomic;

import java.io.Serializable;

import sun.misc.Unsafe;

/**
 * AtomicReference是作用是对"对象"进行原子操作。
 *
 * AtomicReference的源码比较简单。它是通过"volatile"和"Unsafe提供的CAS函数实现"原子操作。
 * (01) value是volatile类型。这保证了：当某线程修改value的值时，其他线程看到的value值都是最新的value值，即修改之后的volatile的值。
 * (02) 通过CAS设置value。这保证了：当某线程池通过CAS函数(如compareAndSet函数)设置value时，它的操作是原子的，即线程在操作value时不会被中断。
 * @param <V>
 *
 * 查看：http://www.cnblogs.com/skywang12345/p/3514623.html
 * http://blog.csdn.net/xieyuooo/article/details/8594713
 */
public class AtomicReference<V>
        implements Serializable {
    private static final long serialVersionUID = -1848883965231344442L;

    // 获取Unsafe对象，Unsafe的作用是提供CAS操作
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    /**
     * volatile保证一致性
     */
    private volatile V value;

    // 使用给定的初始值创建新的 AtomicReference。
    public AtomicReference(V paramV) {
        this.value = paramV;
    }

    // 使用 null 初始值创建新的 AtomicReference。
    public AtomicReference() {
    }

    // 获取当前值。
    public final V get() {
        return this.value;
    }

    // 设置为给定值。
    public final void set(V paramV) {
        this.value = paramV;
    }

    // 最终设置为给定值。
    public final void lazySet(V paramV) {
        unsafe.putOrderedObject(this, valueOffset, paramV);
    }

    // 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
    public final boolean compareAndSet(V paramV1, V paramV2) {
        return unsafe.compareAndSwapObject(this, valueOffset, paramV1, paramV2);
    }

    // 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
    public final boolean weakCompareAndSet(V paramV1, V paramV2) {
        return unsafe.compareAndSwapObject(this, valueOffset, paramV1, paramV2);
    }

    // 以原子方式设置为给定值，并返回旧值。
    public final V getAndSet(V paramV) {
        while (true) {
            Object localObject = get();
            if (compareAndSet(localObject, paramV))
                return localObject;
        }
    }

    // 返回当前值的字符串表示形式。
    public String toString() {
        return String.valueOf(get());
    }

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicReference.class.getDeclaredField("value"));
        } catch (Exception localException) {
            throw new Error(localException);
        }
    }
}
