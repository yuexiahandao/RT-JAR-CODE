/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class XmlDotQuery extends InfixExpression
/*     */ {
/*  57 */   private int rp = -1;
/*     */ 
/*     */   public XmlDotQuery() {
/*  60 */     this.type = 146;
/*     */   }
/*     */ 
/*     */   public XmlDotQuery(int paramInt)
/*     */   {
/*  67 */     super(paramInt);
/*     */ 
/*  60 */     this.type = 146;
/*     */   }
/*     */ 
/*     */   public XmlDotQuery(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     super(paramInt1, paramInt2);
/*     */ 
/*  60 */     this.type = 146;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/*  82 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/*  89 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  94 */     StringBuilder localStringBuilder = new StringBuilder();
/*  95 */     localStringBuilder.append(makeIndent(paramInt));
/*  96 */     localStringBuilder.append(getLeft().toSource(0));
/*  97 */     localStringBuilder.append(".(");
/*  98 */     localStringBuilder.append(getRight().toSource(0));
/*  99 */     localStringBuilder.append(")");
/* 100 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlDotQuery
 * JD-Core Version:    0.6.2
 */