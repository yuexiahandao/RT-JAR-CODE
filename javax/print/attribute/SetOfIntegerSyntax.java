/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class SetOfIntegerSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3666874174847632203L;
/*     */   private int[][] members;
/*     */ 
/*     */   protected SetOfIntegerSyntax(String paramString)
/*     */   {
/* 106 */     this.members = parse(paramString);
/*     */   }
/*     */ 
/*     */   private static int[][] parse(String paramString)
/*     */   {
/* 115 */     Vector localVector = new Vector();
/*     */ 
/* 118 */     int i = paramString == null ? 0 : paramString.length();
/* 119 */     int j = 0;
/* 120 */     int k = 0;
/* 121 */     int m = 0;
/* 122 */     int n = 0;
/*     */ 
/* 125 */     while (j < i) {
/* 126 */       char c = paramString.charAt(j++);
/*     */       int i1;
/* 127 */       switch (k)
/*     */       {
/*     */       case 0:
/* 130 */         if (Character.isWhitespace(c)) {
/* 131 */           k = 0;
/*     */         }
/* 133 */         else if ((i1 = Character.digit(c, 10)) != -1) {
/* 134 */           m = i1;
/* 135 */           k = 1;
/*     */         } else {
/* 137 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 1:
/* 142 */         if (Character.isWhitespace(c)) {
/* 143 */           k = 2;
/* 144 */         } else if ((i1 = Character.digit(c, 10)) != -1) {
/* 145 */           m = 10 * m + i1;
/* 146 */           k = 1;
/* 147 */         } else if ((c == '-') || (c == ':')) {
/* 148 */           k = 3;
/* 149 */         } else if (c == ',') {
/* 150 */           accumulate(localVector, m, m);
/* 151 */           k = 6;
/*     */         } else {
/* 153 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 2:
/* 158 */         if (Character.isWhitespace(c)) {
/* 159 */           k = 2;
/*     */         }
/* 161 */         else if ((c == '-') || (c == ':')) {
/* 162 */           k = 3;
/*     */         }
/* 164 */         else if (c == ',') {
/* 165 */           accumulate(localVector, m, m);
/* 166 */           k = 6;
/*     */         } else {
/* 168 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 3:
/* 173 */         if (Character.isWhitespace(c)) {
/* 174 */           k = 3;
/* 175 */         } else if ((i1 = Character.digit(c, 10)) != -1) {
/* 176 */           n = i1;
/* 177 */           k = 4;
/*     */         } else {
/* 179 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 4:
/* 184 */         if (Character.isWhitespace(c)) {
/* 185 */           k = 5;
/* 186 */         } else if ((i1 = Character.digit(c, 10)) != -1) {
/* 187 */           n = 10 * n + i1;
/* 188 */           k = 4;
/* 189 */         } else if (c == ',') {
/* 190 */           accumulate(localVector, m, n);
/* 191 */           k = 6;
/*     */         } else {
/* 193 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 5:
/* 198 */         if (Character.isWhitespace(c)) {
/* 199 */           k = 5;
/* 200 */         } else if (c == ',') {
/* 201 */           accumulate(localVector, m, n);
/* 202 */           k = 6;
/*     */         } else {
/* 204 */           throw new IllegalArgumentException();
/*     */         }
/*     */ 
/*     */         break;
/*     */       case 6:
/* 209 */         if (Character.isWhitespace(c)) {
/* 210 */           k = 6;
/* 211 */         } else if ((i1 = Character.digit(c, 10)) != -1) {
/* 212 */           m = i1;
/* 213 */           k = 1;
/*     */         } else {
/* 215 */           throw new IllegalArgumentException();
/*     */         }
/*     */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 222 */     switch (k) {
/*     */     case 0:
/* 224 */       break;
/*     */     case 1:
/*     */     case 2:
/* 227 */       accumulate(localVector, m, m);
/* 228 */       break;
/*     */     case 4:
/*     */     case 5:
/* 231 */       accumulate(localVector, m, n);
/* 232 */       break;
/*     */     case 3:
/*     */     case 6:
/* 235 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 239 */     return canonicalArrayForm(localVector);
/*     */   }
/*     */ 
/*     */   private static void accumulate(Vector paramVector, int paramInt1, int paramInt2)
/*     */   {
/* 248 */     if (paramInt1 <= paramInt2)
/*     */     {
/* 250 */       paramVector.add(new int[] { paramInt1, paramInt2 });
/*     */ 
/* 254 */       for (int i = paramVector.size() - 2; i >= 0; i--)
/*     */       {
/* 256 */         int[] arrayOfInt1 = (int[])paramVector.elementAt(i);
/* 257 */         int j = arrayOfInt1[0];
/* 258 */         int k = arrayOfInt1[1];
/* 259 */         int[] arrayOfInt2 = (int[])paramVector.elementAt(i + 1);
/* 260 */         int m = arrayOfInt2[0];
/* 261 */         int n = arrayOfInt2[1];
/*     */ 
/* 269 */         if (Math.max(j, m) - Math.min(k, n) <= 1)
/*     */         {
/* 272 */           paramVector.setElementAt(new int[] { Math.min(j, m), Math.max(k, n) }, i);
/*     */ 
/* 275 */           paramVector.remove(i + 1); } else {
/* 276 */           if (j <= m)
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 281 */           paramVector.setElementAt(arrayOfInt2, i);
/* 282 */           paramVector.setElementAt(arrayOfInt1, i + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int[][] canonicalArrayForm(Vector paramVector)
/*     */   {
/* 297 */     return (int[][])paramVector.toArray(new int[paramVector.size()][]);
/*     */   }
/*     */ 
/*     */   protected SetOfIntegerSyntax(int[][] paramArrayOfInt)
/*     */   {
/* 317 */     this.members = parse(paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   private static int[][] parse(int[][] paramArrayOfInt)
/*     */   {
/* 326 */     Vector localVector = new Vector();
/*     */ 
/* 329 */     int i = paramArrayOfInt == null ? 0 : paramArrayOfInt.length;
/* 330 */     for (int j = 0; j < i; j++)
/*     */     {
/*     */       int m;
/*     */       int k;
/* 333 */       if (paramArrayOfInt[j].length == 1) {
/* 334 */         k = m = paramArrayOfInt[j][0];
/* 335 */       } else if (paramArrayOfInt[j].length == 2) {
/* 336 */         k = paramArrayOfInt[j][0];
/* 337 */         m = paramArrayOfInt[j][1];
/*     */       } else {
/* 339 */         throw new IllegalArgumentException();
/*     */       }
/*     */ 
/* 343 */       if ((k <= m) && (k < 0)) {
/* 344 */         throw new IllegalArgumentException();
/*     */       }
/*     */ 
/* 348 */       accumulate(localVector, k, m);
/*     */     }
/*     */ 
/* 352 */     return canonicalArrayForm(localVector);
/*     */   }
/*     */ 
/*     */   protected SetOfIntegerSyntax(int paramInt)
/*     */   {
/* 365 */     if (paramInt < 0) {
/* 366 */       throw new IllegalArgumentException();
/*     */     }
/* 368 */     this.members = new int[][] { { paramInt, paramInt } };
/*     */   }
/*     */ 
/*     */   protected SetOfIntegerSyntax(int paramInt1, int paramInt2)
/*     */   {
/* 384 */     if ((paramInt1 <= paramInt2) && (paramInt1 < 0)) {
/* 385 */       throw new IllegalArgumentException();
/*     */     }
/* 387 */     this.members = (paramInt1 <= paramInt2 ? new int[][] { { paramInt1, paramInt2 } } : new int[0][]);
/*     */   }
/*     */ 
/*     */   public int[][] getMembers()
/*     */   {
/* 401 */     int i = this.members.length;
/* 402 */     int[][] arrayOfInt = new int[i][];
/* 403 */     for (int j = 0; j < i; j++) {
/* 404 */       arrayOfInt[j] = { this.members[j][0], this.members[j][1] };
/*     */     }
/* 406 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public boolean contains(int paramInt)
/*     */   {
/* 419 */     int i = this.members.length;
/* 420 */     for (int j = 0; j < i; j++) {
/* 421 */       if (paramInt < this.members[j][0])
/* 422 */         return false;
/* 423 */       if (paramInt <= this.members[j][1]) {
/* 424 */         return true;
/*     */       }
/*     */     }
/* 427 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean contains(IntegerSyntax paramIntegerSyntax)
/*     */   {
/* 440 */     return contains(paramIntegerSyntax.getValue());
/*     */   }
/*     */ 
/*     */   public int next(int paramInt)
/*     */   {
/* 468 */     int i = this.members.length;
/* 469 */     for (int j = 0; j < i; j++) {
/* 470 */       if (paramInt < this.members[j][0])
/* 471 */         return this.members[j][0];
/* 472 */       if (paramInt < this.members[j][1]) {
/* 473 */         return paramInt + 1;
/*     */       }
/*     */     }
/* 476 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 499 */     if ((paramObject != null) && ((paramObject instanceof SetOfIntegerSyntax))) {
/* 500 */       int[][] arrayOfInt1 = this.members;
/* 501 */       int[][] arrayOfInt2 = ((SetOfIntegerSyntax)paramObject).members;
/* 502 */       int i = arrayOfInt1.length;
/* 503 */       int j = arrayOfInt2.length;
/* 504 */       if (i == j) {
/* 505 */         for (int k = 0; k < i; k++) {
/* 506 */           if ((arrayOfInt1[k][0] != arrayOfInt2[k][0]) || (arrayOfInt1[k][1] != arrayOfInt2[k][1]))
/*     */           {
/* 508 */             return false;
/*     */           }
/*     */         }
/* 511 */         return true;
/*     */       }
/* 513 */       return false;
/*     */     }
/*     */ 
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 526 */     int i = 0;
/* 527 */     int j = this.members.length;
/* 528 */     for (int k = 0; k < j; k++) {
/* 529 */       i += this.members[k][0] + this.members[k][1];
/*     */     }
/* 531 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 543 */     StringBuffer localStringBuffer = new StringBuffer();
/* 544 */     int i = this.members.length;
/* 545 */     for (int j = 0; j < i; j++) {
/* 546 */       if (j > 0) {
/* 547 */         localStringBuffer.append(',');
/*     */       }
/* 549 */       localStringBuffer.append(this.members[j][0]);
/* 550 */       if (this.members[j][0] != this.members[j][1]) {
/* 551 */         localStringBuffer.append('-');
/* 552 */         localStringBuffer.append(this.members[j][1]);
/*     */       }
/*     */     }
/* 555 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.SetOfIntegerSyntax
 * JD-Core Version:    0.6.2
 */