/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ContinueStatement extends Jump
/*     */ {
/*     */   private Name label;
/*     */   private Loop target;
/*     */ 
/*     */   public ContinueStatement()
/*     */   {
/*  56 */     this.type = 121;
/*     */   }
/*     */ 
/*     */   public ContinueStatement(int paramInt)
/*     */   {
/*  63 */     this(paramInt, -1);
/*     */   }
/*     */ 
/*     */   public ContinueStatement(int paramInt1, int paramInt2)
/*     */   {
/*  56 */     this.type = 121;
/*     */ 
/*  68 */     this.position = paramInt1;
/*  69 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public ContinueStatement(Name paramName)
/*     */   {
/*  56 */     this.type = 121;
/*     */ 
/*  73 */     setLabel(paramName);
/*     */   }
/*     */ 
/*     */   public ContinueStatement(int paramInt, Name paramName) {
/*  77 */     this(paramInt);
/*  78 */     setLabel(paramName);
/*     */   }
/*     */ 
/*     */   public ContinueStatement(int paramInt1, int paramInt2, Name paramName) {
/*  82 */     this(paramInt1, paramInt2);
/*  83 */     setLabel(paramName);
/*     */   }
/*     */ 
/*     */   public Loop getTarget()
/*     */   {
/*  90 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setTarget(Loop paramLoop)
/*     */   {
/* 100 */     assertNotNull(paramLoop);
/* 101 */     this.target = paramLoop;
/* 102 */     setJumpStatement(paramLoop);
/*     */   }
/*     */ 
/*     */   public Name getLabel()
/*     */   {
/* 111 */     return this.label;
/*     */   }
/*     */ 
/*     */   public void setLabel(Name paramName)
/*     */   {
/* 120 */     this.label = paramName;
/* 121 */     if (paramName != null)
/* 122 */       paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 127 */     StringBuilder localStringBuilder = new StringBuilder();
/* 128 */     localStringBuilder.append(makeIndent(paramInt));
/* 129 */     localStringBuilder.append("continue");
/* 130 */     if (this.label != null) {
/* 131 */       localStringBuilder.append(" ");
/* 132 */       localStringBuilder.append(this.label.toSource(0));
/*     */     }
/* 134 */     localStringBuilder.append(";\n");
/* 135 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 143 */     if ((paramNodeVisitor.visit(this)) && (this.label != null))
/* 144 */       this.label.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ContinueStatement
 * JD-Core Version:    0.6.2
 */