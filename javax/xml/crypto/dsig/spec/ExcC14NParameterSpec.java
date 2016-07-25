/*     */ package javax.xml.crypto.dsig.spec;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class ExcC14NParameterSpec
/*     */   implements C14NMethodParameterSpec
/*     */ {
/*     */   private List preList;
/*     */   public static final String DEFAULT = "#default";
/*     */ 
/*     */   public ExcC14NParameterSpec()
/*     */   {
/*  74 */     this.preList = Collections.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public ExcC14NParameterSpec(List paramList)
/*     */   {
/*  90 */     if (paramList == null) {
/*  91 */       throw new NullPointerException("prefixList cannot be null");
/*     */     }
/*  93 */     this.preList = new ArrayList(paramList);
/*  94 */     int i = 0; for (int j = this.preList.size(); i < j; i++) {
/*  95 */       if (!(this.preList.get(i) instanceof String)) {
/*  96 */         throw new ClassCastException("not a String");
/*     */       }
/*     */     }
/*  99 */     this.preList = Collections.unmodifiableList(this.preList);
/*     */   }
/*     */ 
/*     */   public List getPrefixList()
/*     */   {
/* 113 */     return this.preList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.spec.ExcC14NParameterSpec
 * JD-Core Version:    0.6.2
 */