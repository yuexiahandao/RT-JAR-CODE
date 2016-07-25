/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class NumberLiteral extends AstNode
/*     */ {
/*     */   private String value;
/*     */   private double number;
/*     */ 
/*     */   public NumberLiteral()
/*     */   {
/*  52 */     this.type = 40;
/*     */   }
/*     */ 
/*     */   public NumberLiteral(int paramInt)
/*     */   {
/*  59 */     super(paramInt);
/*     */ 
/*  52 */     this.type = 40;
/*     */   }
/*     */ 
/*     */   public NumberLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*     */ 
/*  52 */     this.type = 40;
/*     */   }
/*     */ 
/*     */   public NumberLiteral(int paramInt, String paramString)
/*     */   {
/*  70 */     super(paramInt);
/*     */ 
/*  52 */     this.type = 40;
/*     */ 
/*  71 */     setValue(paramString);
/*  72 */     setLength(paramString.length());
/*     */   }
/*     */ 
/*     */   public NumberLiteral(int paramInt, String paramString, double paramDouble)
/*     */   {
/*  79 */     this(paramInt, paramString);
/*  80 */     setDouble(paramDouble);
/*     */   }
/*     */ 
/*     */   public NumberLiteral(double paramDouble)
/*     */   {
/*  52 */     this.type = 40;
/*     */ 
/*  84 */     setDouble(paramDouble);
/*  85 */     setValue(Double.toString(paramDouble));
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  92 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(String paramString)
/*     */   {
/* 100 */     assertNotNull(paramString);
/* 101 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public double getNumber()
/*     */   {
/* 108 */     return this.number;
/*     */   }
/*     */ 
/*     */   public void setNumber(double paramDouble)
/*     */   {
/* 115 */     this.number = paramDouble;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 120 */     return makeIndent(paramInt) + (this.value == null ? "<null>" : this.value);
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 128 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.NumberLiteral
 * JD-Core Version:    0.6.2
 */