package sun.nio.fs;

import java.util.regex.PatternSyntaxException;

public class Globs {
    private static final String regexMetaChars = ".^$+{[]|()";
    private static final String globMetaChars = "\\*?[{";
    private static char EOL = '\000';

    private static boolean isRegexMeta(char paramChar) {
        return ".^$+{[]|()".indexOf(paramChar) != -1;
    }

    private static boolean isGlobMeta(char paramChar) {
        return "\\*?[{".indexOf(paramChar) != -1;
    }

    private static char next(String paramString, int paramInt) {
        if (paramInt < paramString.length()) {
            return paramString.charAt(paramInt);
        }
        return EOL;
    }

    private static String toRegexPattern(String paramString, boolean paramBoolean) {
        int i = 0;
        StringBuilder localStringBuilder = new StringBuilder("^");

        int j = 0;
        while (j < paramString.length()) {
            char c1 = paramString.charAt(j++);
            switch (c1) {
                case '\\':
                    if (j == paramString.length()) {
                        throw new PatternSyntaxException("No character to escape", paramString, j - 1);
                    }

                    char c2 = paramString.charAt(j++);
                    if ((isGlobMeta(c2)) || (isRegexMeta(c2))) {
                        localStringBuilder.append('\\');
                    }
                    localStringBuilder.append(c2);
                    break;
                case '/':
                    if (paramBoolean)
                        localStringBuilder.append("\\\\");
                    else {
                        localStringBuilder.append(c1);
                    }
                    break;
                case '[':
                    if (paramBoolean)
                        localStringBuilder.append("[[^\\\\]&&[");
                    else {
                        localStringBuilder.append("[[^/]&&[");
                    }
                    if (next(paramString, j) == '^') {
                        localStringBuilder.append("\\^");
                        j++;
                    } else {
                        if (next(paramString, j) == '!') {
                            localStringBuilder.append('^');
                            j++;
                        }

                        if (next(paramString, j) == '-') {
                            localStringBuilder.append('-');
                            j++;
                        }
                    }
                    int k = 0;
                    char c3 = '\000';
                    while (j < paramString.length()) {
                        c1 = paramString.charAt(j++);
                        if (c1 == ']') {
                            break;
                        }
                        if ((c1 == '/') || ((paramBoolean) && (c1 == '\\'))) {
                            throw new PatternSyntaxException("Explicit 'name separator' in class", paramString, j - 1);
                        }

                        if ((c1 == '\\') || (c1 == '[') || ((c1 == '&') && (next(paramString, j) == '&'))) {
                            localStringBuilder.append('\\');
                        }
                        localStringBuilder.append(c1);

                        if (c1 == '-') {
                            if (k == 0) {
                                throw new PatternSyntaxException("Invalid range", paramString, j - 1);
                            }

                            if (((c1 = next(paramString, j++)) == EOL) || (c1 == ']')) {
                                break;
                            }
                            if (c1 < c3) {
                                throw new PatternSyntaxException("Invalid range", paramString, j - 3);
                            }

                            localStringBuilder.append(c1);
                            k = 0;
                        } else {
                            k = 1;
                            c3 = c1;
                        }
                    }
                    if (c1 != ']') {
                        throw new PatternSyntaxException("Missing ']", paramString, j - 1);
                    }
                    localStringBuilder.append("]]");
                    break;
                case '{':
                    if (i != 0) {
                        throw new PatternSyntaxException("Cannot nest groups", paramString, j - 1);
                    }

                    localStringBuilder.append("(?:(?:");
                    i = 1;
                    break;
                case '}':
                    if (i != 0) {
                        localStringBuilder.append("))");
                        i = 0;
                    } else {
                        localStringBuilder.append('}');
                    }
                    break;
                case ',':
                    if (i != 0)
                        localStringBuilder.append(")|(?:");
                    else {
                        localStringBuilder.append(',');
                    }
                    break;
                case '*':
                    if (next(paramString, j) == '*') {
                        localStringBuilder.append(".*");
                        j++;
                    } else if (paramBoolean) {
                        localStringBuilder.append("[^\\\\]*");
                    } else {
                        localStringBuilder.append("[^/]*");
                    }

                    break;
                case '?':
                    if (paramBoolean)
                        localStringBuilder.append("[^\\\\]");
                    else {
                        localStringBuilder.append("[^/]");
                    }
                    break;
                default:
                    if (isRegexMeta(c1)) {
                        localStringBuilder.append('\\');
                    }
                    localStringBuilder.append(c1);
            }
        }

        if (i != 0) {
            throw new PatternSyntaxException("Missing '}", paramString, j - 1);
        }

        return '$';
    }

    static String toUnixRegexPattern(String paramString) {
        return toRegexPattern(paramString, false);
    }

    static String toWindowsRegexPattern(String paramString) {
        return toRegexPattern(paramString, true);
    }
}
