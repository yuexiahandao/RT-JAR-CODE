/*     */ package sun.net.www;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class HeaderParser
/*     */ {
/*     */   String raw;
/*     */   String[][] tab;
/*     */   int nkeys;
/*  55 */   int asize = 10;
/*     */ 
/*     */   public HeaderParser(String paramString) {
/*  58 */     this.raw = paramString;
/*  59 */     this.tab = new String[this.asize][2];
/*  60 */     parse();
/*     */   }
/*     */ 
/*     */   private HeaderParser()
/*     */   {
/*     */   }
/*     */ 
/*     */   public HeaderParser subsequence(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     if ((paramInt1 == 0) && (paramInt2 == this.nkeys)) {
/*  72 */       return this;
/*     */     }
/*  74 */     if ((paramInt1 < 0) || (paramInt1 >= paramInt2) || (paramInt2 > this.nkeys))
/*  75 */       throw new IllegalArgumentException("invalid start or end");
/*  76 */     HeaderParser localHeaderParser = new HeaderParser();
/*  77 */     localHeaderParser.tab = new String[this.asize][2];
/*  78 */     localHeaderParser.asize = this.asize;
/*  79 */     System.arraycopy(this.tab, paramInt1, localHeaderParser.tab, 0, paramInt2 - paramInt1);
/*  80 */     localHeaderParser.nkeys = (paramInt2 - paramInt1);
/*  81 */     return localHeaderParser;
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */   {
/*  86 */     if (this.raw != null) {
/*  87 */       this.raw = this.raw.trim();
/*  88 */       char[] arrayOfChar = this.raw.toCharArray();
/*  89 */       int i = 0; int j = 0; int k = 0;
/*  90 */       int m = 1;
/*  91 */       int n = 0;
/*  92 */       int i1 = arrayOfChar.length;
/*  93 */       while (j < i1) {
/*  94 */         int i2 = arrayOfChar[j];
/*  95 */         if ((i2 == 61) && (n == 0)) {
/*  96 */           this.tab[k][0] = new String(arrayOfChar, i, j - i).toLowerCase();
/*  97 */           m = 0;
/*  98 */           j++;
/*  99 */           i = j;
/* 100 */         } else if (i2 == 34) {
/* 101 */           if (n != 0) {
/* 102 */             this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/* 103 */             n = 0;
/*     */             do
/* 105 */               j++;
/* 106 */             while ((j < i1) && ((arrayOfChar[j] == ' ') || (arrayOfChar[j] == ',')));
/* 107 */             m = 1;
/* 108 */             i = j;
/*     */           } else {
/* 110 */             n = 1;
/* 111 */             j++;
/* 112 */             i = j;
/*     */           }
/*     */         } else { if ((i2 == 32) || (i2 == 44)) {
/* 115 */             if (n != 0) {
/* 116 */               j++;
/* 117 */               continue;
/* 118 */             }if (m != 0)
/* 119 */               this.tab[(k++)][0] = new String(arrayOfChar, i, j - i).toLowerCase();
/*     */             else {
/* 121 */               this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/*     */             }
/* 123 */             while ((j < i1) && ((arrayOfChar[j] == ' ') || (arrayOfChar[j] == ','))) {
/* 124 */               j++;
/*     */             }
/* 126 */             m = 1;
/* 127 */             i = j; break label307;
/*     */           }
/* 129 */           j++;
/*     */         }
/* 131 */         label307: if (k == this.asize) {
/* 132 */           this.asize *= 2;
/* 133 */           String[][] arrayOfString = new String[this.asize][2];
/* 134 */           System.arraycopy(this.tab, 0, arrayOfString, 0, this.tab.length);
/* 135 */           this.tab = arrayOfString;
/*     */         }
/*     */       }
/*     */ 
/* 139 */       j--; if (j > i) {
/* 140 */         if (m == 0) {
/* 141 */           if (arrayOfChar[j] == '"')
/* 142 */             this.tab[(k++)][1] = new String(arrayOfChar, i, j - i);
/*     */           else
/* 144 */             this.tab[(k++)][1] = new String(arrayOfChar, i, j - i + 1);
/*     */         }
/*     */         else
/* 147 */           this.tab[(k++)][0] = new String(arrayOfChar, i, j - i + 1).toLowerCase();
/*     */       }
/* 149 */       else if (j == i) {
/* 150 */         if (m == 0) {
/* 151 */           if (arrayOfChar[j] == '"')
/* 152 */             this.tab[(k++)][1] = String.valueOf(arrayOfChar[(j - 1)]);
/*     */           else
/* 154 */             this.tab[(k++)][1] = String.valueOf(arrayOfChar[j]);
/*     */         }
/*     */         else {
/* 157 */           this.tab[(k++)][0] = String.valueOf(arrayOfChar[j]).toLowerCase();
/*     */         }
/*     */       }
/* 160 */       this.nkeys = k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String findKey(int paramInt)
/*     */   {
/* 166 */     if ((paramInt < 0) || (paramInt > this.asize))
/* 167 */       return null;
/* 168 */     return this.tab[paramInt][0];
/*     */   }
/*     */ 
/*     */   public String findValue(int paramInt) {
/* 172 */     if ((paramInt < 0) || (paramInt > this.asize))
/* 173 */       return null;
/* 174 */     return this.tab[paramInt][1];
/*     */   }
/*     */ 
/*     */   public String findValue(String paramString) {
/* 178 */     return findValue(paramString, null);
/*     */   }
/*     */ 
/*     */   public String findValue(String paramString1, String paramString2) {
/* 182 */     if (paramString1 == null)
/* 183 */       return paramString2;
/* 184 */     paramString1 = paramString1.toLowerCase();
/* 185 */     for (int i = 0; i < this.asize; i++) {
/* 186 */       if (this.tab[i][0] == null)
/* 187 */         return paramString2;
/* 188 */       if (paramString1.equals(this.tab[i][0])) {
/* 189 */         return this.tab[i][1];
/*     */       }
/*     */     }
/* 192 */     return paramString2;
/*     */   }
/*     */ 
/*     */   public Iterator keys()
/*     */   {
/* 214 */     return new ParserIterator(false);
/*     */   }
/*     */ 
/*     */   public Iterator values() {
/* 218 */     return new ParserIterator(true);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 222 */     Iterator localIterator = keys();
/* 223 */     StringBuffer localStringBuffer = new StringBuffer();
/* 224 */     localStringBuffer.append("{size=" + this.asize + " nkeys=" + this.nkeys + " ");
/* 225 */     for (int i = 0; localIterator.hasNext(); i++) {
/* 226 */       String str1 = (String)localIterator.next();
/* 227 */       String str2 = findValue(i);
/* 228 */       if ((str2 != null) && ("".equals(str2))) {
/* 229 */         str2 = null;
/*     */       }
/* 231 */       localStringBuffer.append(" {" + str1 + (str2 == null ? "" : new StringBuilder().append(",").append(str2).toString()) + "}");
/* 232 */       if (localIterator.hasNext()) {
/* 233 */         localStringBuffer.append(",");
/*     */       }
/*     */     }
/* 236 */     localStringBuffer.append(" }");
/* 237 */     return new String(localStringBuffer);
/*     */   }
/*     */ 
/*     */   public int findInt(String paramString, int paramInt) {
/*     */     try {
/* 242 */       return Integer.parseInt(findValue(paramString, String.valueOf(paramInt))); } catch (Throwable localThrowable) {
/*     */     }
/* 244 */     return paramInt;
/*     */   }
/*     */ 
/*     */   class ParserIterator
/*     */     implements Iterator
/*     */   {
/*     */     int index;
/*     */     boolean returnsValue;
/*     */ 
/*     */     ParserIterator(boolean arg2)
/*     */     {
/*     */       boolean bool;
/* 200 */       this.returnsValue = bool;
/*     */     }
/*     */     public boolean hasNext() {
/* 203 */       return this.index < HeaderParser.this.nkeys;
/*     */     }
/*     */     public Object next() {
/* 206 */       return HeaderParser.this.tab[(this.index++)][0];
/*     */     }
/*     */     public void remove() {
/* 209 */       throw new UnsupportedOperationException("remove not supported");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.HeaderParser
 * JD-Core Version:    0.6.2
 */