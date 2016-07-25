/*     */ package javax.management;
/*     */ 
/*     */ class MatchQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -7156603696948215014L;
/*     */   private AttributeValueExp exp;
/*     */   private String pattern;
/*     */ 
/*     */   public MatchQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MatchQueryExp(AttributeValueExp paramAttributeValueExp, StringValueExp paramStringValueExp)
/*     */   {
/*  64 */     this.exp = paramAttributeValueExp;
/*  65 */     this.pattern = paramStringValueExp.getValue();
/*     */   }
/*     */ 
/*     */   public AttributeValueExp getAttribute()
/*     */   {
/*  73 */     return this.exp;
/*     */   }
/*     */ 
/*     */   public String getPattern()
/*     */   {
/*  80 */     return this.pattern;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 101 */     ValueExp localValueExp = this.exp.apply(paramObjectName);
/* 102 */     if (!(localValueExp instanceof StringValueExp)) {
/* 103 */       return false;
/*     */     }
/* 105 */     return wildmatch(((StringValueExp)localValueExp).getValue(), this.pattern);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return this.exp + " like " + new StringValueExp(this.pattern);
/*     */   }
/*     */ 
/*     */   private static boolean wildmatch(String paramString1, String paramString2)
/*     */   {
/* 124 */     int j = 0; int k = 0;
/* 125 */     int m = paramString1.length();
/* 126 */     int n = paramString2.length();
/*     */ 
/* 128 */     while (k < n) {
/* 129 */       int i = paramString2.charAt(k++);
/* 130 */       if (i == 63) {
/* 131 */         j++; if (j > m)
/* 132 */           return false;
/* 133 */       } else if (i == 91) {
/* 134 */         if (j >= m)
/* 135 */           return false;
/* 136 */         int i1 = 1;
/* 137 */         int i2 = 0;
/* 138 */         if (paramString2.charAt(k) == '!') {
/* 139 */           i1 = 0;
/* 140 */           k++;
/*     */         }
/* 142 */         while ((i = paramString2.charAt(k)) != ']') { k++; if (k >= n) break;
/* 143 */           if ((paramString2.charAt(k) == '-') && (k + 1 < n) && (paramString2.charAt(k + 1) != ']'))
/*     */           {
/* 146 */             if ((paramString1.charAt(j) >= paramString2.charAt(k - 1)) && (paramString1.charAt(j) <= paramString2.charAt(k + 1)))
/*     */             {
/* 148 */               i2 = 1;
/*     */             }
/* 150 */             k++;
/*     */           }
/* 152 */           else if (i == paramString1.charAt(j)) {
/* 153 */             i2 = 1;
/*     */           }
/*     */         }
/*     */ 
/* 157 */         if ((k >= n) || (i1 != i2)) {
/* 158 */           return false;
/*     */         }
/* 160 */         k++;
/* 161 */         j++; } else {
/* 162 */         if (i == 42) {
/* 163 */           if (k >= n)
/* 164 */             return true;
/*     */           do {
/* 166 */             if (wildmatch(paramString1.substring(j), paramString2.substring(k)))
/* 167 */               return true;
/* 168 */             j++; } while (j < m);
/* 169 */           return false;
/* 170 */         }if (i == 92) {
/* 171 */           if ((k >= n) || (j >= m) || (paramString2.charAt(k++) != paramString1.charAt(j++)))
/*     */           {
/* 173 */             return false;
/*     */           }
/* 175 */         } else if ((j >= m) || (i != paramString1.charAt(j++))) {
/* 176 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 180 */     return j == m;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MatchQueryExp
 * JD-Core Version:    0.6.2
 */