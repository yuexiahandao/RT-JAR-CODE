/*     */ package java.text;
/*     */ 
/*     */ final class RuleBasedCollationKey extends CollationKey
/*     */ {
/* 121 */   private String key = null;
/*     */ 
/*     */   public int compareTo(CollationKey paramCollationKey)
/*     */   {
/*  59 */     int i = this.key.compareTo(((RuleBasedCollationKey)paramCollationKey).key);
/*  60 */     if (i <= -1)
/*  61 */       return -1;
/*  62 */     if (i >= 1)
/*  63 */       return 1;
/*  64 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  76 */     if (this == paramObject) return true;
/*  77 */     if ((paramObject == null) || (!getClass().equals(paramObject.getClass()))) {
/*  78 */       return false;
/*     */     }
/*  80 */     RuleBasedCollationKey localRuleBasedCollationKey = (RuleBasedCollationKey)paramObject;
/*  81 */     return this.key.equals(localRuleBasedCollationKey.key);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  93 */     return this.key.hashCode();
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 104 */     char[] arrayOfChar = this.key.toCharArray();
/* 105 */     byte[] arrayOfByte = new byte[2 * arrayOfChar.length];
/* 106 */     int i = 0;
/* 107 */     for (int j = 0; j < arrayOfChar.length; j++) {
/* 108 */       arrayOfByte[(i++)] = ((byte)(arrayOfChar[j] >>> '\b'));
/* 109 */       arrayOfByte[(i++)] = ((byte)(arrayOfChar[j] & 0xFF));
/*     */     }
/* 111 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   RuleBasedCollationKey(String paramString1, String paramString2)
/*     */   {
/* 118 */     super(paramString1);
/* 119 */     this.key = paramString2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.RuleBasedCollationKey
 * JD-Core Version:    0.6.2
 */