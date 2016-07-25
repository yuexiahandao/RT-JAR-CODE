/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class RegexpPool
/*     */ {
/*  39 */   private RegexpNode prefixMachine = new RegexpNode();
/*  40 */   private RegexpNode suffixMachine = new RegexpNode();
/*     */   private static final int BIG = 2147483647;
/*  42 */   private int lastDepth = 2147483647;
/*     */ 
/*     */   public void add(String paramString, Object paramObject)
/*     */     throws REException
/*     */   {
/*  59 */     add(paramString, paramObject, false);
/*     */   }
/*     */ 
/*     */   public void replace(String paramString, Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       add(paramString, paramObject, true);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object delete(String paramString)
/*     */   {
/*  89 */     Object localObject = null;
/*  90 */     RegexpNode localRegexpNode1 = this.prefixMachine;
/*  91 */     RegexpNode localRegexpNode2 = localRegexpNode1;
/*  92 */     int i = paramString.length() - 1;
/*     */ 
/*  94 */     int k = 1;
/*     */ 
/*  96 */     if ((!paramString.startsWith("*")) || (!paramString.endsWith("*")))
/*     */     {
/*  98 */       i++;
/*     */     }
/* 100 */     if (i <= 0) {
/* 101 */       return null;
/*     */     }
/*     */ 
/* 104 */     for (int j = 0; localRegexpNode1 != null; j++) {
/* 105 */       if ((localRegexpNode1.result != null) && (localRegexpNode1.depth < 2147483647) && ((!localRegexpNode1.exact) || (j == i)))
/*     */       {
/* 107 */         localRegexpNode2 = localRegexpNode1;
/*     */       }
/* 109 */       if (j >= i)
/*     */         break;
/* 111 */       localRegexpNode1 = localRegexpNode1.find(paramString.charAt(j));
/*     */     }
/*     */ 
/* 115 */     localRegexpNode1 = this.suffixMachine;
/* 116 */     j = i;
/*     */     while (true) { j--; if ((j < 0) || (localRegexpNode1 == null)) break;
/* 117 */       if ((localRegexpNode1.result != null) && (localRegexpNode1.depth < 2147483647)) {
/* 118 */         k = 0;
/* 119 */         localRegexpNode2 = localRegexpNode1;
/*     */       }
/* 121 */       localRegexpNode1 = localRegexpNode1.find(paramString.charAt(j));
/*     */     }
/*     */ 
/* 125 */     if (k != 0) {
/* 126 */       if (paramString.equals(localRegexpNode2.re)) {
/* 127 */         localObject = localRegexpNode2.result;
/* 128 */         localRegexpNode2.result = null;
/*     */       }
/*     */ 
/*     */     }
/* 132 */     else if (paramString.equals(localRegexpNode2.re)) {
/* 133 */       localObject = localRegexpNode2.result;
/* 134 */       localRegexpNode2.result = null;
/*     */     }
/*     */ 
/* 137 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object match(String paramString)
/*     */   {
/* 154 */     return matchAfter(paramString, 2147483647);
/*     */   }
/*     */ 
/*     */   public Object matchNext(String paramString)
/*     */   {
/* 162 */     return matchAfter(paramString, this.lastDepth);
/*     */   }
/*     */ 
/*     */   private void add(String paramString, Object paramObject, boolean paramBoolean) throws REException {
/* 166 */     int i = paramString.length();
/*     */ 
/* 168 */     if (paramString.charAt(0) == '*') {
/* 169 */       localRegexpNode = this.suffixMachine;
/* 170 */       while (i > 1)
/* 171 */         localRegexpNode = localRegexpNode.add(paramString.charAt(--i));
/*     */     }
/* 173 */     boolean bool = false;
/* 174 */     if (paramString.charAt(i - 1) == '*')
/* 175 */       i--;
/*     */     else
/* 177 */       bool = true;
/* 178 */     RegexpNode localRegexpNode = this.prefixMachine;
/* 179 */     for (int j = 0; j < i; j++)
/* 180 */       localRegexpNode = localRegexpNode.add(paramString.charAt(j));
/* 181 */     localRegexpNode.exact = bool;
/*     */ 
/* 184 */     if ((localRegexpNode.result != null) && (!paramBoolean)) {
/* 185 */       throw new REException(paramString + " is a duplicate");
/*     */     }
/* 187 */     localRegexpNode.re = paramString;
/* 188 */     localRegexpNode.result = paramObject;
/*     */   }
/*     */ 
/*     */   private Object matchAfter(String paramString, int paramInt) {
/* 192 */     RegexpNode localRegexpNode1 = this.prefixMachine;
/* 193 */     RegexpNode localRegexpNode2 = localRegexpNode1;
/* 194 */     int i = 0;
/* 195 */     int j = 0;
/* 196 */     int k = paramString.length();
/*     */ 
/* 198 */     if (k <= 0) {
/* 199 */       return null;
/*     */     }
/* 201 */     for (int m = 0; localRegexpNode1 != null; m++) {
/* 202 */       if ((localRegexpNode1.result != null) && (localRegexpNode1.depth < paramInt) && ((!localRegexpNode1.exact) || (m == k)))
/*     */       {
/* 204 */         this.lastDepth = localRegexpNode1.depth;
/* 205 */         localRegexpNode2 = localRegexpNode1;
/* 206 */         i = m;
/* 207 */         j = k;
/*     */       }
/* 209 */       if (m >= k)
/*     */         break;
/* 211 */       localRegexpNode1 = localRegexpNode1.find(paramString.charAt(m));
/*     */     }
/*     */ 
/* 214 */     localRegexpNode1 = this.suffixMachine;
/* 215 */     m = k;
/*     */     while (true) { m--; if ((m < 0) || (localRegexpNode1 == null)) break;
/* 216 */       if ((localRegexpNode1.result != null) && (localRegexpNode1.depth < paramInt)) {
/* 217 */         this.lastDepth = localRegexpNode1.depth;
/* 218 */         localRegexpNode2 = localRegexpNode1;
/* 219 */         i = 0;
/* 220 */         j = m + 1;
/*     */       }
/* 222 */       localRegexpNode1 = localRegexpNode1.find(paramString.charAt(m));
/*     */     }
/* 224 */     Object localObject = localRegexpNode2.result;
/* 225 */     if ((localObject != null) && ((localObject instanceof RegexpTarget)))
/* 226 */       localObject = ((RegexpTarget)localObject).found(paramString.substring(i, j));
/* 227 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 240 */     this.lastDepth = 2147483647;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 245 */     paramPrintStream.print("Regexp pool:\n");
/* 246 */     if (this.suffixMachine.firstchild != null) {
/* 247 */       paramPrintStream.print(" Suffix machine: ");
/* 248 */       this.suffixMachine.firstchild.print(paramPrintStream);
/* 249 */       paramPrintStream.print("\n");
/*     */     }
/* 251 */     if (this.prefixMachine.firstchild != null) {
/* 252 */       paramPrintStream.print(" Prefix machine: ");
/* 253 */       this.prefixMachine.firstchild.print(paramPrintStream);
/* 254 */       paramPrintStream.print("\n");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.RegexpPool
 * JD-Core Version:    0.6.2
 */