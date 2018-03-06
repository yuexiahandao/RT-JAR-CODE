package sun.nio.fs;

import java.nio.file.LinkOption;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件系统的工具类
 */
class Util {
    /**
     * split字符串
     * @param paramString
     * @param paramChar
     * @return
     */
    static String[] split(String paramString, char paramChar) {
        int i = 0;
        // 先进行统计
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.charAt(j) == paramChar)
                i++;
        }
        // 创建数组
        String[] arrayOfString = new String[i + 1];
        int k = 0;
        int m = 0;
        // 取出参数
        for (int n = 0; n < paramString.length(); n++) {
            if (paramString.charAt(n) == paramChar) {
                arrayOfString[(k++)] = paramString.substring(m, n);
                m = n + 1;
            }
        }
        arrayOfString[k] = paramString.substring(m, paramString.length());
        // 返回
        return arrayOfString;
    }

    // 通过数组创建Set
    static <E> Set<E> newSet(E[] paramArrayOfE) {
        HashSet localHashSet = new HashSet();
        for (E ? :paramArrayOfE){
            localHashSet.add( ?);
        }
        return localHashSet;
    }

    /**
     * Set加入新的元素
     * @param paramSet
     * @param paramArrayOfE
     * @param <E>
     * @return
     */
    static <E> Set<E> newSet(Set<E> paramSet, E[] paramArrayOfE) {
        HashSet localHashSet = new HashSet(paramSet);
        for (E ? :paramArrayOfE){
            localHashSet.add( ?);
        }
        return localHashSet;
    }

    static boolean followLinks(LinkOption[] paramArrayOfLinkOption) {
        boolean bool = true;
        for (LinkOption localLinkOption : paramArrayOfLinkOption) {
            if (localLinkOption == LinkOption.NOFOLLOW_LINKS) {
                bool = false;
            } else {
                if (localLinkOption == null) {
                    throw new NullPointerException();
                }
                throw new AssertionError("Should not get here");
            }
        }
        return bool;
    }
}
