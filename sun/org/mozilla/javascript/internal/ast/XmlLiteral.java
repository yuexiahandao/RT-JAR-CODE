/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class XmlLiteral extends AstNode
/*     */ {
/*  55 */   private List<XmlFragment> fragments = new ArrayList();
/*     */ 
/*     */   public XmlLiteral() {
/*  58 */     this.type = 145;
/*     */   }
/*     */ 
/*     */   public XmlLiteral(int paramInt)
/*     */   {
/*  65 */     super(paramInt);
/*     */ 
/*  58 */     this.type = 145;
/*     */   }
/*     */ 
/*     */   public XmlLiteral(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*     */ 
/*  58 */     this.type = 145;
/*     */   }
/*     */ 
/*     */   public List<XmlFragment> getFragments()
/*     */   {
/*  76 */     return this.fragments;
/*     */   }
/*     */ 
/*     */   public void setFragments(List<XmlFragment> paramList)
/*     */   {
/*  86 */     assertNotNull(paramList);
/*  87 */     this.fragments.clear();
/*  88 */     for (XmlFragment localXmlFragment : paramList)
/*  89 */       addFragment(localXmlFragment);
/*     */   }
/*     */ 
/*     */   public void addFragment(XmlFragment paramXmlFragment)
/*     */   {
/*  97 */     assertNotNull(paramXmlFragment);
/*  98 */     this.fragments.add(paramXmlFragment);
/*  99 */     paramXmlFragment.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 104 */     StringBuilder localStringBuilder = new StringBuilder(250);
/* 105 */     for (XmlFragment localXmlFragment : this.fragments) {
/* 106 */       localStringBuilder.append(localXmlFragment.toSource(0));
/*     */     }
/* 108 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 116 */     if (paramNodeVisitor.visit(this))
/* 117 */       for (XmlFragment localXmlFragment : this.fragments)
/* 118 */         localXmlFragment.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlLiteral
 * JD-Core Version:    0.6.2
 */