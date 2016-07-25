/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ 
/*     */ public class StringLiteral extends AstNode
/*     */ {
/*     */   private String value;
/*     */   private char quoteChar;
/*     */ 
/*     */   public StringLiteral()
/*     */   {
/*  54 */     this.type = 41;
/*     */   }
/*     */ 
/*     */   public StringLiteral(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */ 
/*  54 */     this.type = 41;
/*     */   }
/*     */ 
/*     */   public StringLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 41;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  78 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getValue(boolean paramBoolean)
/*     */   {
/*  85 */     if (!paramBoolean)
/*  86 */       return this.value;
/*  87 */     return this.quoteChar + this.value + this.quoteChar;
/*     */   }
/*     */ 
/*     */   public void setValue(String paramString)
/*     */   {
/*  96 */     assertNotNull(paramString);
/*  97 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public char getQuoteCharacter()
/*     */   {
/* 104 */     return this.quoteChar;
/*     */   }
/*     */ 
/*     */   public void setQuoteCharacter(char paramChar) {
/* 108 */     this.quoteChar = paramChar;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 113 */     return makeIndent(paramInt) + this.quoteChar + ScriptRuntime.escapeString(this.value, this.quoteChar) + this.quoteChar;
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 125 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.StringLiteral
 * JD-Core Version:    0.6.2
 */