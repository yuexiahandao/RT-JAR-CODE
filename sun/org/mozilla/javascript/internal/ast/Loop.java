/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public abstract class Loop extends Scope
/*     */ {
/*     */   protected AstNode body;
/*  47 */   protected int lp = -1;
/*  48 */   protected int rp = -1;
/*     */ 
/*     */   public Loop() {
/*     */   }
/*     */ 
/*     */   public Loop(int paramInt) {
/*  54 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public Loop(int paramInt1, int paramInt2) {
/*  58 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public AstNode getBody()
/*     */   {
/*  65 */     return this.body;
/*     */   }
/*     */ 
/*     */   public void setBody(AstNode paramAstNode)
/*     */   {
/*  74 */     this.body = paramAstNode;
/*  75 */     int i = paramAstNode.getPosition() + paramAstNode.getLength();
/*  76 */     setLength(i - getPosition());
/*  77 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/*  84 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/*  91 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/*  98 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 105 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 112 */     this.lp = paramInt1;
/* 113 */     this.rp = paramInt2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Loop
 * JD-Core Version:    0.6.2
 */