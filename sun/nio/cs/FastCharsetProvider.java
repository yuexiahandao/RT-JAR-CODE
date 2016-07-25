/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.spi.CharsetProvider;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class FastCharsetProvider extends CharsetProvider
/*     */ {
/*     */   private Map<String, String> classMap;
/*     */   private Map<String, String> aliasMap;
/*     */   private Map<String, Charset> cache;
/*     */   private String packagePrefix;
/*     */ 
/*     */   protected FastCharsetProvider(String paramString, Map<String, String> paramMap1, Map<String, String> paramMap2, Map<String, Charset> paramMap)
/*     */   {
/*  61 */     this.packagePrefix = paramString;
/*  62 */     this.aliasMap = paramMap1;
/*  63 */     this.classMap = paramMap2;
/*  64 */     this.cache = paramMap;
/*     */   }
/*     */ 
/*     */   private String canonicalize(String paramString) {
/*  68 */     String str = (String)this.aliasMap.get(paramString);
/*  69 */     return str != null ? str : paramString;
/*     */   }
/*     */ 
/*     */   private static String toLower(String paramString)
/*     */   {
/*  75 */     int i = paramString.length();
/*  76 */     int j = 1;
/*  77 */     for (int k = 0; k < i; k++) {
/*  78 */       m = paramString.charAt(k);
/*  79 */       if ((m - 65 | 90 - m) >= 0) {
/*  80 */         j = 0;
/*  81 */         break;
/*     */       }
/*     */     }
/*  84 */     if (j != 0)
/*  85 */       return paramString;
/*  86 */     char[] arrayOfChar = new char[i];
/*  87 */     for (int m = 0; m < i; m++) {
/*  88 */       int n = paramString.charAt(m);
/*  89 */       if ((n - 65 | 90 - n) >= 0)
/*  90 */         arrayOfChar[m] = ((char)(n + 32));
/*     */       else
/*  92 */         arrayOfChar[m] = ((char)n);
/*     */     }
/*  94 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   private Charset lookup(String paramString)
/*     */   {
/*  99 */     String str1 = canonicalize(toLower(paramString));
/*     */ 
/* 102 */     Object localObject = (Charset)this.cache.get(str1);
/* 103 */     if (localObject != null) {
/* 104 */       return localObject;
/*     */     }
/*     */ 
/* 107 */     String str2 = (String)this.classMap.get(str1);
/* 108 */     if (str2 == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     if (str2.equals("US_ASCII")) {
/* 112 */       localObject = new US_ASCII();
/* 113 */       this.cache.put(str1, localObject);
/* 114 */       return localObject;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 119 */       Class localClass = Class.forName(this.packagePrefix + "." + str2, true, getClass().getClassLoader());
/*     */ 
/* 122 */       localObject = (Charset)localClass.newInstance();
/* 123 */       this.cache.put(str1, localObject);
/* 124 */       return localObject;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 126 */       return null;
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 128 */       return null; } catch (InstantiationException localInstantiationException) {
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public final Charset charsetForName(String paramString)
/*     */   {
/* 135 */     synchronized (this) {
/* 136 */       return lookup(canonicalize(paramString));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Iterator<Charset> charsets()
/*     */   {
/* 142 */     return new Iterator()
/*     */     {
/* 144 */       Iterator<String> i = FastCharsetProvider.this.classMap.keySet().iterator();
/*     */ 
/*     */       public boolean hasNext() {
/* 147 */         return this.i.hasNext();
/*     */       }
/*     */ 
/*     */       public Charset next() {
/* 151 */         String str = (String)this.i.next();
/* 152 */         return FastCharsetProvider.this.lookup(str);
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 156 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.FastCharsetProvider
 * JD-Core Version:    0.6.2
 */