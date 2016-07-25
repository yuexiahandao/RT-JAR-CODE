/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ public class XmlString extends XmlFragment
/*    */ {
/*    */   private String xml;
/*    */ 
/*    */   public XmlString()
/*    */   {
/*    */   }
/*    */ 
/*    */   public XmlString(int paramInt)
/*    */   {
/* 54 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public XmlString(int paramInt, String paramString) {
/* 58 */     super(paramInt);
/* 59 */     setXml(paramString);
/*    */   }
/*    */ 
/*    */   public void setXml(String paramString)
/*    */   {
/* 69 */     assertNotNull(paramString);
/* 70 */     this.xml = paramString;
/* 71 */     setLength(paramString.length());
/*    */   }
/*    */ 
/*    */   public String getXml()
/*    */   {
/* 79 */     return this.xml;
/*    */   }
/*    */ 
/*    */   public String toSource(int paramInt)
/*    */   {
/* 84 */     return makeIndent(paramInt) + this.xml;
/*    */   }
/*    */ 
/*    */   public void visit(NodeVisitor paramNodeVisitor)
/*    */   {
/* 92 */     paramNodeVisitor.visit(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlString
 * JD-Core Version:    0.6.2
 */