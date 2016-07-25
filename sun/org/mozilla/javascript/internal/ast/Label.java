/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class Label extends Jump
/*     */ {
/*     */   private String name;
/*     */ 
/*     */   public Label()
/*     */   {
/*  53 */     this.type = 130;
/*     */   }
/*     */ 
/*     */   public Label(int paramInt)
/*     */   {
/*  60 */     this(paramInt, -1);
/*     */   }
/*     */ 
/*     */   public Label(int paramInt1, int paramInt2)
/*     */   {
/*  53 */     this.type = 130;
/*     */ 
/*  65 */     this.position = paramInt1;
/*  66 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public Label(int paramInt1, int paramInt2, String paramString) {
/*  70 */     this(paramInt1, paramInt2);
/*  71 */     setName(paramString);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  78 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString)
/*     */   {
/*  87 */     paramString = paramString == null ? null : paramString.trim();
/*  88 */     if ((paramString == null) || ("".equals(paramString)))
/*  89 */       throw new IllegalArgumentException("invalid label name");
/*  90 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  95 */     StringBuilder localStringBuilder = new StringBuilder();
/*  96 */     localStringBuilder.append(makeIndent(paramInt));
/*  97 */     localStringBuilder.append(this.name);
/*  98 */     localStringBuilder.append(":\n");
/*  99 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 107 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Label
 * JD-Core Version:    0.6.2
 */