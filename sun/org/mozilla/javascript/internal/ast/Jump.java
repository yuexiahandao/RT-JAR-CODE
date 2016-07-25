/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ 
/*     */ public class Jump extends AstNode
/*     */ {
/*     */   public Node target;
/*     */   private Node target2;
/*     */   private Jump jumpNode;
/*     */ 
/*     */   public Jump()
/*     */   {
/*  62 */     this.type = -1;
/*     */   }
/*     */ 
/*     */   public Jump(int paramInt) {
/*  66 */     this.type = paramInt;
/*     */   }
/*     */ 
/*     */   public Jump(int paramInt1, int paramInt2) {
/*  70 */     this(paramInt1);
/*  71 */     setLineno(paramInt2);
/*     */   }
/*     */ 
/*     */   public Jump(int paramInt, Node paramNode) {
/*  75 */     this(paramInt);
/*  76 */     addChildToBack(paramNode);
/*     */   }
/*     */ 
/*     */   public Jump(int paramInt1, Node paramNode, int paramInt2) {
/*  80 */     this(paramInt1, paramNode);
/*  81 */     setLineno(paramInt2);
/*     */   }
/*     */ 
/*     */   public Jump getJumpStatement()
/*     */   {
/*  86 */     if ((this.type != 120) && (this.type != 121)) codeBug();
/*  87 */     return this.jumpNode;
/*     */   }
/*     */ 
/*     */   public void setJumpStatement(Jump paramJump)
/*     */   {
/*  92 */     if ((this.type != 120) && (this.type != 121)) codeBug();
/*  93 */     if (paramJump == null) codeBug();
/*  94 */     if (this.jumpNode != null) codeBug();
/*  95 */     this.jumpNode = paramJump;
/*     */   }
/*     */ 
/*     */   public Node getDefault()
/*     */   {
/* 100 */     if (this.type != 114) codeBug();
/* 101 */     return this.target2;
/*     */   }
/*     */ 
/*     */   public void setDefault(Node paramNode)
/*     */   {
/* 106 */     if (this.type != 114) codeBug();
/* 107 */     if (paramNode.getType() != 131) codeBug();
/* 108 */     if (this.target2 != null) codeBug();
/* 109 */     this.target2 = paramNode;
/*     */   }
/*     */ 
/*     */   public Node getFinally()
/*     */   {
/* 114 */     if (this.type != 81) codeBug();
/* 115 */     return this.target2;
/*     */   }
/*     */ 
/*     */   public void setFinally(Node paramNode)
/*     */   {
/* 120 */     if (this.type != 81) codeBug();
/* 121 */     if (paramNode.getType() != 131) codeBug();
/* 122 */     if (this.target2 != null) codeBug();
/* 123 */     this.target2 = paramNode;
/*     */   }
/*     */ 
/*     */   public Jump getLoop()
/*     */   {
/* 128 */     if (this.type != 130) codeBug();
/* 129 */     return this.jumpNode;
/*     */   }
/*     */ 
/*     */   public void setLoop(Jump paramJump)
/*     */   {
/* 134 */     if (this.type != 130) codeBug();
/* 135 */     if (paramJump == null) codeBug();
/* 136 */     if (this.jumpNode != null) codeBug();
/* 137 */     this.jumpNode = paramJump;
/*     */   }
/*     */ 
/*     */   public Node getContinue()
/*     */   {
/* 142 */     if (this.type != 132) codeBug();
/* 143 */     return this.target2;
/*     */   }
/*     */ 
/*     */   public void setContinue(Node paramNode)
/*     */   {
/* 148 */     if (this.type != 132) codeBug();
/* 149 */     if (paramNode.getType() != 131) codeBug();
/* 150 */     if (this.target2 != null) codeBug();
/* 151 */     this.target2 = paramNode;
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 161 */     throw new UnsupportedOperationException(toString());
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 166 */     throw new UnsupportedOperationException(toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Jump
 * JD-Core Version:    0.6.2
 */