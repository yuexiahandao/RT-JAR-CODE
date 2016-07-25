/*     */ package javax.naming.ldap;
/*     */ 
/*     */ public class SortKey
/*     */ {
/*     */   private String attrID;
/*  48 */   private boolean reverseOrder = false;
/*     */ 
/*  53 */   private String matchingRuleID = null;
/*     */ 
/*     */   public SortKey(String paramString)
/*     */   {
/*  64 */     this.attrID = paramString;
/*     */   }
/*     */ 
/*     */   public SortKey(String paramString1, boolean paramBoolean, String paramString2)
/*     */   {
/*  85 */     this.attrID = paramString1;
/*  86 */     this.reverseOrder = (!paramBoolean);
/*  87 */     this.matchingRuleID = paramString2;
/*     */   }
/*     */ 
/*     */   public String getAttributeID()
/*     */   {
/*  96 */     return this.attrID;
/*     */   }
/*     */ 
/*     */   public boolean isAscending()
/*     */   {
/* 105 */     return !this.reverseOrder;
/*     */   }
/*     */ 
/*     */   public String getMatchingRuleID()
/*     */   {
/* 116 */     return this.matchingRuleID;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.SortKey
 * JD-Core Version:    0.6.2
 */