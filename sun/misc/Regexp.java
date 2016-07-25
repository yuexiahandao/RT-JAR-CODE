/*     */ package sun.misc;
/*     */ 
/*     */ public class Regexp
/*     */ {
/*     */   public boolean ignoreCase;
/*     */   public String exp;
/*     */   public String prefix;
/*     */   public String suffix;
/*     */   public boolean exact;
/*     */   public int prefixLen;
/*     */   public int suffixLen;
/*     */   public int totalLen;
/*     */   public String[] mids;
/*     */ 
/*     */   public Regexp(String paramString)
/*     */   {
/*  60 */     this.exp = paramString;
/*  61 */     int i = paramString.indexOf('*');
/*  62 */     int j = paramString.lastIndexOf('*');
/*  63 */     if (i < 0) {
/*  64 */       this.totalLen = paramString.length();
/*  65 */       this.exact = true;
/*     */     } else {
/*  67 */       this.prefixLen = i;
/*  68 */       if (i == 0)
/*  69 */         this.prefix = null;
/*     */       else
/*  71 */         this.prefix = paramString.substring(0, i);
/*  72 */       this.suffixLen = (paramString.length() - j - 1);
/*  73 */       if (this.suffixLen == 0)
/*  74 */         this.suffix = null;
/*     */       else
/*  76 */         this.suffix = paramString.substring(j + 1);
/*  77 */       int k = 0;
/*  78 */       int m = i;
/*  79 */       while ((m < j) && (m >= 0)) {
/*  80 */         k++;
/*  81 */         m = paramString.indexOf('*', m + 1);
/*     */       }
/*  83 */       this.totalLen = (this.prefixLen + this.suffixLen);
/*  84 */       if (k > 0) {
/*  85 */         this.mids = new String[k];
/*  86 */         m = i;
/*  87 */         for (int n = 0; n < k; n++) {
/*  88 */           m++;
/*  89 */           int i1 = paramString.indexOf('*', m);
/*  90 */           if (m < i1) {
/*  91 */             this.mids[n] = paramString.substring(m, i1);
/*  92 */             this.totalLen += this.mids[n].length();
/*     */           }
/*  94 */           m = i1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final boolean matches(String paramString)
/*     */   {
/* 102 */     return matches(paramString, 0, paramString.length());
/*     */   }
/*     */ 
/*     */   boolean matches(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 108 */     if (this.exact) {
/* 109 */       return (paramInt2 == this.totalLen) && (this.exp.regionMatches(this.ignoreCase, 0, paramString, paramInt1, paramInt2));
/*     */     }
/* 111 */     if (paramInt2 < this.totalLen)
/* 112 */       return false;
/* 113 */     if (((this.prefixLen > 0) && (!this.prefix.regionMatches(this.ignoreCase, 0, paramString, paramInt1, this.prefixLen))) || ((this.suffixLen > 0) && (!this.suffix.regionMatches(this.ignoreCase, 0, paramString, paramInt1 + paramInt2 - this.suffixLen, this.suffixLen))))
/*     */     {
/* 121 */       return false;
/* 122 */     }if (this.mids == null)
/* 123 */       return true;
/* 124 */     int i = this.mids.length;
/* 125 */     int j = paramInt1 + this.prefixLen;
/* 126 */     int k = paramInt1 + paramInt2 - this.suffixLen;
/* 127 */     for (int m = 0; m < i; m++) {
/* 128 */       String str = this.mids[m];
/* 129 */       int n = str.length();
/* 130 */       while ((j + n <= k) && (!str.regionMatches(this.ignoreCase, 0, paramString, j, n)))
/*     */       {
/* 133 */         j++;
/* 134 */       }if (j + n > k)
/* 135 */         return false;
/* 136 */       j += n;
/*     */     }
/* 138 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Regexp
 * JD-Core Version:    0.6.2
 */