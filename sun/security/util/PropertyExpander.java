package sun.security.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import sun.net.www.ParseUtil;

/**
 * 属性扩充器（${paramName}或者${{paramName}}（多层嵌套也可以替换）替换）
 */
public class PropertyExpander {
    /**
     * 扩充
     * @param paramString
     * @return
     * @throws PropertyExpander.ExpandException
     */
    public static String expand(String paramString)
            throws PropertyExpander.ExpandException {
        return expand(paramString, false);
    }

    /**
     * 真正的扩充器
     * @param paramString
     * @param paramBoolean
     * @return
     * @throws PropertyExpander.ExpandException
     */
    public static String expand(String paramString, boolean paramBoolean)
            throws PropertyExpander.ExpandException {
        if (paramString == null) {
            return null;
        }
        // 查找开头
        int i = paramString.indexOf("${", 0);

        if (i == -1) return paramString;

        StringBuffer localStringBuffer = new StringBuffer(paramString.length());
        int j = paramString.length();
        int k = 0;

        while (i < j) {
            if (i > k) {
                // 将${之前的字符载入
                localStringBuffer.append(paramString.substring(k, i));
                k = i;
            }
            int m = i + 2;

            if ((m < j) && (paramString.charAt(m) == '{')) {
                m = paramString.indexOf("}}", m);
                if ((m == -1) || (m + 2 == j)) {
                    // 格式不对不替换
                    localStringBuffer.append(paramString.substring(i));
                    break;
                }

                m++;
                localStringBuffer.append(paramString.substring(i, m + 1));
            } else {
                while ((m < j) && (paramString.charAt(m) != '}')) {
                    m++;
                }
                if (m == j) {
                    // 没有找到闭合的符号
                    localStringBuffer.append(paramString.substring(i, m));
                    break;
                }
                // 取得相应的标识
                String str1 = paramString.substring(i + 2, m);
                if (str1.equals("/")) {
                    localStringBuffer.append(File.separatorChar);
                } else {
                    // 从系统属性中取值替换
                    String str2 = System.getProperty(str1);
                    if (str2 != null) {
                        if (paramBoolean) {
                            try {
                                if ((localStringBuffer.length() > 0) || (!new URI(str2).isAbsolute())) {
                                    // 编码
                                    str2 = ParseUtil.encodePath(str2);
                                }
                            } catch (URISyntaxException localURISyntaxException) {
                                str2 = ParseUtil.encodePath(str2);
                            }
                        }
                        localStringBuffer.append(str2);
                    } else {
                        throw new ExpandException("unable to expand property " + str1);
                    }
                }

            }

            k = m + 1;
            i = paramString.indexOf("${", k);
            if (i == -1) {
                if (k >= j) break;
                localStringBuffer.append(paramString.substring(k, j));
                break;
            }

        }

        return localStringBuffer.toString();
    }

    public static class ExpandException extends GeneralSecurityException {
        private static final long serialVersionUID = -7941948581406161702L;

        public ExpandException(String paramString) {
            super();
        }
    }
}
