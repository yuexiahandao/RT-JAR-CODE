/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class RegExpLiteral extends AstNode
/*     */ {
/*     */   private String value;
/*     */   private String flags;
/*     */ 
/*     */   public RegExpLiteral()
/*     */   {
/*  53 */     this.type = 48;
/*     */   }
/*     */ 
/*     */   public RegExpLiteral(int paramInt)
/*     */   {
/*  60 */     super(paramInt);
/*     */ 
/*  53 */     this.type = 48;
/*     */   }
/*     */ 
/*     */   public RegExpLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  64 */     super(paramInt1, paramInt2);
/*     */ 
/*  53 */     this.type = 48;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  71 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(String paramString)
/*     */   {
/*  79 */     assertNotNull(paramString);
/*  80 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public String getFlags()
/*     */   {
/*  87 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public void setFlags(String paramString)
/*     */   {
/*  94 */     this.flags = paramString;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  99 */     return makeIndent(paramInt) + "/" + this.value + "/" + (this.flags == null ? "" : this.flags);
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 108 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.RegExpLiteral
 * JD-Core Version:    0.6.2
 */