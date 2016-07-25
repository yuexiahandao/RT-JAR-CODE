/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ArrayLiteral extends AstNode
/*     */   implements DestructuringForm
/*     */ {
/*  67 */   private static final List<AstNode> NO_ELEMS = Collections.unmodifiableList(new ArrayList());
/*     */   private List<AstNode> elements;
/*     */   private int destructuringLength;
/*     */   private int skipCount;
/*     */   private boolean isDestructuring;
/*     */ 
/*     */   public ArrayLiteral()
/*     */   {
/*  76 */     this.type = 65;
/*     */   }
/*     */ 
/*     */   public ArrayLiteral(int paramInt)
/*     */   {
/*  83 */     super(paramInt);
/*     */ 
/*  76 */     this.type = 65;
/*     */   }
/*     */ 
/*     */   public ArrayLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  87 */     super(paramInt1, paramInt2);
/*     */ 
/*  76 */     this.type = 65;
/*     */   }
/*     */ 
/*     */   public List<AstNode> getElements()
/*     */   {
/*  97 */     return this.elements != null ? this.elements : NO_ELEMS;
/*     */   }
/*     */ 
/*     */   public void setElements(List<AstNode> paramList)
/*     */   {
/* 105 */     if (paramList == null) {
/* 106 */       this.elements = null;
/*     */     } else {
/* 108 */       if (this.elements != null)
/* 109 */         this.elements.clear();
/* 110 */       for (AstNode localAstNode : paramList)
/* 111 */         addElement(localAstNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addElement(AstNode paramAstNode)
/*     */   {
/* 122 */     assertNotNull(paramAstNode);
/* 123 */     if (this.elements == null)
/* 124 */       this.elements = new ArrayList();
/* 125 */     this.elements.add(paramAstNode);
/* 126 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 134 */     return this.elements == null ? 0 : this.elements.size();
/*     */   }
/*     */ 
/*     */   public AstNode getElement(int paramInt)
/*     */   {
/* 144 */     if (this.elements == null)
/* 145 */       throw new IndexOutOfBoundsException("no elements");
/* 146 */     return (AstNode)this.elements.get(paramInt);
/*     */   }
/*     */ 
/*     */   public int getDestructuringLength()
/*     */   {
/* 153 */     return this.destructuringLength;
/*     */   }
/*     */ 
/*     */   public void setDestructuringLength(int paramInt)
/*     */   {
/* 164 */     this.destructuringLength = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSkipCount()
/*     */   {
/* 172 */     return this.skipCount;
/*     */   }
/*     */ 
/*     */   public void setSkipCount(int paramInt)
/*     */   {
/* 180 */     this.skipCount = paramInt;
/*     */   }
/*     */ 
/*     */   public void setIsDestructuring(boolean paramBoolean)
/*     */   {
/* 189 */     this.isDestructuring = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isDestructuring()
/*     */   {
/* 198 */     return this.isDestructuring;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 203 */     StringBuilder localStringBuilder = new StringBuilder();
/* 204 */     localStringBuilder.append(makeIndent(paramInt));
/* 205 */     localStringBuilder.append("[");
/* 206 */     if (this.elements != null) {
/* 207 */       printList(this.elements, localStringBuilder);
/*     */     }
/* 209 */     localStringBuilder.append("]");
/* 210 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 220 */     if (paramNodeVisitor.visit(this))
/* 221 */       for (AstNode localAstNode : getElements())
/* 222 */         localAstNode.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ArrayLiteral
 * JD-Core Version:    0.6.2
 */