/*     */
package java.lang;
/*     */ 
/*     */

import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;

/*     */
/*     */ final class ProcessEnvironment extends HashMap<String, String>
/*     */ {
    /*     */   static final int MIN_NAME_LENGTH = 1;
    /* 234 */   private static final NameComparator nameComparator = new NameComparator(null);
    /* 235 */   private static final EntryComparator entryComparator = new EntryComparator(null);
    /* 236 */   private static final ProcessEnvironment theEnvironment = new ProcessEnvironment();
    /* 237 */   private static final Map<String, String> theUnmodifiableEnvironment = Collections.unmodifiableMap(theEnvironment);
    /*     */   private static final Map<String, String> theCaseInsensitiveEnvironment;

    /*     */
/*     */
    private static String validateName(String paramString)
/*     */ {
/*  73 */
        if ((paramString.indexOf('=', 1) != -1) || (paramString.indexOf(0) != -1))
/*     */ {
/*  75 */
            throw new IllegalArgumentException("Invalid environment variable name: \"" + paramString + "\"");
/*     */
        }
/*  77 */
        return paramString;
/*     */
    }

    /*     */
/*     */
    private static String validateValue(String paramString) {
/*  81 */
        if (paramString.indexOf(0) != -1) {
/*  82 */
            throw new IllegalArgumentException("Invalid environment variable value: \"" + paramString + "\"");
/*     */
        }
/*  84 */
        return paramString;
/*     */
    }

    /*     */
/*     */
    private static String nonNullString(Object paramObject) {
/*  88 */
        if (paramObject == null)
/*  89 */ throw new NullPointerException();
/*  90 */
        return (String) paramObject;
/*     */
    }

    /*     */
/*     */
    public String put(String paramString1, String paramString2) {
/*  94 */
        return (String) super.put(validateName(paramString1), validateValue(paramString2));
/*     */
    }

    /*     */
/*     */
    public String get(Object paramObject) {
/*  98 */
        return (String) super.get(nonNullString(paramObject));
/*     */
    }

    /*     */
/*     */
    public boolean containsKey(Object paramObject) {
/* 102 */
        return super.containsKey(nonNullString(paramObject));
/*     */
    }

    /*     */
/*     */
    public boolean containsValue(Object paramObject) {
/* 106 */
        return super.containsValue(nonNullString(paramObject));
/*     */
    }

    /*     */
/*     */
    public String remove(Object paramObject) {
/* 110 */
        return (String) super.remove(nonNullString(paramObject));
/*     */
    }

    /*     */
/*     */
    public Set<String> keySet()
/*     */ {
/* 179 */
        return new CheckedKeySet(super.keySet());
/*     */
    }

    /*     */
/*     */
    public Collection<String> values() {
/* 183 */
        return new CheckedValues(super.values());
/*     */
    }

    /*     */
/*     */
    public Set<Map.Entry<String, String>> entrySet() {
/* 187 */
        return new CheckedEntrySet(super.entrySet());
/*     */
    }

    /*     */
/*     */
    private ProcessEnvironment()
/*     */ {
/*     */
    }

    /*     */
/*     */
    private ProcessEnvironment(int paramInt)
/*     */ {
/* 262 */
        super(paramInt);
/*     */
    }

    /*     */
/*     */
    static String getenv(String paramString)
/*     */ {
/* 275 */
        return (String) theCaseInsensitiveEnvironment.get(paramString);
/*     */
    }

    /*     */
/*     */
    static Map<String, String> getenv()
/*     */ {
/* 280 */
        return theUnmodifiableEnvironment;
/*     */
    }

    /*     */
/*     */
    static Map<String, String> environment()
/*     */ {
/* 285 */
        return (Map) theEnvironment.clone();
/*     */
    }

    /*     */
/*     */
    static Map<String, String> emptyEnvironment(int paramInt)
/*     */ {
/* 290 */
        return new ProcessEnvironment(paramInt);
/*     */
    }

    /*     */
/*     */
    private static native String environmentBlock();

    /*     */
/*     */   String toEnvironmentBlock()
/*     */ {
/* 298 */
        ArrayList localArrayList = new ArrayList(entrySet());
/* 299 */
        Collections.sort(localArrayList, entryComparator);
/*     */ 
/* 301 */
        StringBuilder localStringBuilder = new StringBuilder(size() * 30);
/* 302 */
        int i = -1;
/*     */ 
/* 309 */
        for (Map.Entry localEntry : localArrayList) {
/* 310 */
            String str1 = (String) localEntry.getKey();
/* 311 */
            String str2 = (String) localEntry.getValue();
/* 312 */
            if ((i < 0) && ((i = nameComparator.compare(str1, "SystemRoot")) > 0))
/*     */ {
/* 314 */
                addToEnvIfSet(localStringBuilder, "SystemRoot");
/*     */
            }
/* 316 */
            addToEnv(localStringBuilder, str1, str2);
/*     */
        }
/* 318 */
        if (i < 0)
/*     */ {
/* 320 */
            addToEnvIfSet(localStringBuilder, "SystemRoot");
/*     */
        }
/* 322 */
        if (localStringBuilder.length() == 0)
/*     */ {
/* 324 */
            localStringBuilder.append('\000');
/*     */
        }
/*     */ 
/* 327 */
        localStringBuilder.append('\000');
/* 328 */
        return localStringBuilder.toString();
/*     */
    }

    /*     */
/*     */
    private static void addToEnvIfSet(StringBuilder paramStringBuilder, String paramString)
/*     */ {
/* 333 */
        String str = getenv(paramString);
/* 334 */
        if (str != null)
/* 335 */ addToEnv(paramStringBuilder, paramString, str);
/*     */
    }

    /*     */
/*     */
    private static void addToEnv(StringBuilder paramStringBuilder, String paramString1, String paramString2) {
/* 339 */
        paramStringBuilder.append(paramString1).append('=').append(paramString2).append('\000');
/*     */
    }

    /*     */
/*     */
    static String toEnvironmentBlock(Map<String, String> paramMap) {
/* 343 */
        return paramMap == null ? null : ((ProcessEnvironment) paramMap).toEnvironmentBlock();
/*     */
    }

    /*     */
/*     */   static
/*     */ {
/* 240 */
        String str = environmentBlock();
/*     */
        int j;
/*     */
        int k;
/* 242 */
        for (int i = 0;
/* 243 */       ((j = str.indexOf(0, i)) != -1) && ((k = str.indexOf('=', i + 1)) != -1); 
/* 246 */       i = j + 1)
/*     */ {
/* 248 */
            if (k < j) {
/* 249 */
                theEnvironment.put(str.substring(i, k), str.substring(k + 1, j));
/*     */
            }
/*     */
        }
/*     */ 
/* 253 */
        theCaseInsensitiveEnvironment = new TreeMap(nameComparator);
/* 254 */
        theCaseInsensitiveEnvironment.putAll(theEnvironment);
/*     */
    }

    /*     */
/*     */   private static class CheckedEntry
/*     */ implements Map.Entry<String, String>
/*     */ {
        /*     */     private final Map.Entry<String, String> e;

        /*     */
/*     */
        public CheckedEntry(Map.Entry<String, String> paramEntry)
/*     */ {
/* 117 */
            this.e = paramEntry;
        }

        /* 118 */
        public String getKey() {
            return (String) this.e.getKey();
        }

        /* 119 */
        public String getValue() {
            return (String) this.e.getValue();
        }

        /*     */
        public String setValue(String paramString) {
/* 121 */
            return (String) this.e.setValue(ProcessEnvironment.validateValue(paramString));
/*     */
        }

        /* 123 */
        public String toString() {
            return getKey() + "=" + getValue();
        }

        /* 124 */
        public boolean equals(Object paramObject) {
            return this.e.equals(paramObject);
        }

        /* 125 */
        public int hashCode() {
            return this.e.hashCode();
        }
/*     */
    }

    /*     */
/*     */   private static class CheckedEntrySet extends AbstractSet<Map.Entry<String, String>> {
        /*     */     private final Set<Map.Entry<String, String>> s;

        /*     */
/*     */
        public CheckedEntrySet(Set<Map.Entry<String, String>> paramSet) {
/* 132 */
            this.s = paramSet;
        }

        /* 133 */
        public int size() {
            return this.s.size();
        }

        /* 134 */
        public boolean isEmpty() {
            return this.s.isEmpty();
        }

        /* 135 */
        public void clear() {
            this.s.clear();
        }

        /*     */
        public Iterator<Map.Entry<String, String>> iterator() {
/* 137 */
            return new Iterator() {
                /* 138 */ Iterator<Map.Entry<String, String>> i = ProcessEnvironment.CheckedEntrySet.this.s.iterator();

                /*     */
/* 139 */
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                /*     */
                public Map.Entry<String, String> next() {
/* 141 */
                    return new ProcessEnvironment.CheckedEntry((Map.Entry) this.i.next());
/*     */
                }

                /* 143 */
                public void remove() {
                    this.i.remove();
                }
            };
/*     */
        }

        /*     */
/*     */
        private static Map.Entry<String, String> checkedEntry(Object paramObject) {
/* 147 */
            Map.Entry localEntry = (Map.Entry) paramObject;
/* 148 */
            ProcessEnvironment.nonNullString(localEntry.getKey());
/* 149 */
            ProcessEnvironment.nonNullString(localEntry.getValue());
/* 150 */
            return localEntry;
/*     */
        }

        /* 152 */
        public boolean contains(Object paramObject) {
            return this.s.contains(checkedEntry(paramObject));
        }

        /* 153 */
        public boolean remove(Object paramObject) {
            return this.s.remove(checkedEntry(paramObject));
        }
/*     */ 
/*     */
    }

    /*     */
/*     */   private static class CheckedKeySet extends AbstractSet<String>
/*     */ {
        /*     */     private final Set<String> s;

        /*     */
/*     */
        public CheckedKeySet(Set<String> paramSet)
/*     */ {
/* 169 */
            this.s = paramSet;
        }

        /* 170 */
        public int size() {
            return this.s.size();
        }

        /* 171 */
        public boolean isEmpty() {
            return this.s.isEmpty();
        }

        /* 172 */
        public void clear() {
            this.s.clear();
        }

        /* 173 */
        public Iterator<String> iterator() {
            return this.s.iterator();
        }

        /* 174 */
        public boolean contains(Object paramObject) {
            return this.s.contains(ProcessEnvironment.nonNullString(paramObject));
        }

        /* 175 */
        public boolean remove(Object paramObject) {
            return this.s.remove(ProcessEnvironment.nonNullString(paramObject));
        }
/*     */ 
/*     */
    }

    /*     */
/*     */   private static class CheckedValues extends AbstractCollection<String>
/*     */ {
        /*     */     private final Collection<String> c;

        /*     */
/*     */
        public CheckedValues(Collection<String> paramCollection)
/*     */ {
/* 158 */
            this.c = paramCollection;
        }

        /* 159 */
        public int size() {
            return this.c.size();
        }

        /* 160 */
        public boolean isEmpty() {
            return this.c.isEmpty();
        }

        /* 161 */
        public void clear() {
            this.c.clear();
        }

        /* 162 */
        public Iterator<String> iterator() {
            return this.c.iterator();
        }

        /* 163 */
        public boolean contains(Object paramObject) {
            return this.c.contains(ProcessEnvironment.nonNullString(paramObject));
        }

        /* 164 */
        public boolean remove(Object paramObject) {
            return this.c.remove(ProcessEnvironment.nonNullString(paramObject));
        }
/*     */ 
/*     */
    }

    /*     */
/*     */   private static final class EntryComparator
/*     */ implements Comparator<Map.Entry<String, String>>
/*     */ {
        /*     */
        public int compare(Map.Entry<String, String> paramEntry1, Map.Entry<String, String> paramEntry2)
/*     */ {
/* 220 */
            return ProcessEnvironment.nameComparator.compare((String) paramEntry1.getKey(), (String) paramEntry2.getKey());
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static final class NameComparator
/*     */ implements Comparator<String>
/*     */ {
        /*     */
        public int compare(String paramString1, String paramString2)
/*     */ {
/* 198 */
            int i = paramString1.length();
/* 199 */
            int j = paramString2.length();
/* 200 */
            int k = Math.min(i, j);
/* 201 */
            for (int m = 0; m < k; m++) {
/* 202 */
                char c1 = paramString1.charAt(m);
/* 203 */
                char c2 = paramString2.charAt(m);
/* 204 */
                if (c1 != c2) {
/* 205 */
                    c1 = Character.toUpperCase(c1);
/* 206 */
                    c2 = Character.toUpperCase(c2);
/* 207 */
                    if (c1 != c2)
/*     */ {
/* 209 */
                        return c1 - c2;
/*     */
                    }
/*     */
                }
/*     */
            }
/* 212 */
            return i - j;
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ProcessEnvironment
 * JD-Core Version:    0.6.2
 */