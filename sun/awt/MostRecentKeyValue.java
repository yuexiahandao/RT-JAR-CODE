/*     */ package sun.awt;
/*     */ 
/*     */ final class MostRecentKeyValue
/*     */ {
/*     */   Object key;
/*     */   Object value;
/*     */ 
/*     */   MostRecentKeyValue(Object paramObject1, Object paramObject2)
/*     */   {
/* 929 */     this.key = paramObject1;
/* 930 */     this.value = paramObject2;
/*     */   }
/*     */   void setPair(Object paramObject1, Object paramObject2) {
/* 933 */     this.key = paramObject1;
/* 934 */     this.value = paramObject2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.MostRecentKeyValue
 * JD-Core Version:    0.6.2
 */