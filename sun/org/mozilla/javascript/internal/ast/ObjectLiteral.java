/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ObjectLiteral extends AstNode
/*     */   implements DestructuringForm
/*     */ {
/*  67 */   private static final List<ObjectProperty> NO_ELEMS = Collections.unmodifiableList(new ArrayList());
/*     */   private List<ObjectProperty> elements;
/*     */   boolean isDestructuring;
/*     */ 
/*     */   public ObjectLiteral()
/*     */   {
/*  74 */     this.type = 66;
/*     */   }
/*     */ 
/*     */   public ObjectLiteral(int paramInt)
/*     */   {
/*  81 */     super(paramInt);
/*     */ 
/*  74 */     this.type = 66;
/*     */   }
/*     */ 
/*     */   public ObjectLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  85 */     super(paramInt1, paramInt2);
/*     */ 
/*  74 */     this.type = 66;
/*     */   }
/*     */ 
/*     */   public List<ObjectProperty> getElements()
/*     */   {
/*  93 */     return this.elements != null ? this.elements : NO_ELEMS;
/*     */   }
/*     */ 
/*     */   public void setElements(List<ObjectProperty> paramList)
/*     */   {
/* 102 */     if (paramList == null) {
/* 103 */       this.elements = null;
/*     */     } else {
/* 105 */       if (this.elements != null)
/* 106 */         this.elements.clear();
/* 107 */       for (ObjectProperty localObjectProperty : paramList)
/* 108 */         addElement(localObjectProperty);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addElement(ObjectProperty paramObjectProperty)
/*     */   {
/* 118 */     assertNotNull(paramObjectProperty);
/* 119 */     if (this.elements == null) {
/* 120 */       this.elements = new ArrayList();
/*     */     }
/* 122 */     this.elements.add(paramObjectProperty);
/* 123 */     paramObjectProperty.setParent(this);
/*     */   }
/*     */ 
/*     */   public void setIsDestructuring(boolean paramBoolean)
/*     */   {
/* 132 */     this.isDestructuring = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isDestructuring()
/*     */   {
/* 141 */     return this.isDestructuring;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 146 */     StringBuilder localStringBuilder = new StringBuilder();
/* 147 */     localStringBuilder.append(makeIndent(paramInt));
/* 148 */     localStringBuilder.append("{");
/* 149 */     if (this.elements != null) {
/* 150 */       printList(this.elements, localStringBuilder);
/*     */     }
/* 152 */     localStringBuilder.append("}");
/* 153 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 162 */     if (paramNodeVisitor.visit(this))
/* 163 */       for (ObjectProperty localObjectProperty : getElements())
/* 164 */         localObjectProperty.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ObjectLiteral
 * JD-Core Version:    0.6.2
 */