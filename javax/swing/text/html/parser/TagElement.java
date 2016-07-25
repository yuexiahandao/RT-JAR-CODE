/*    */ package javax.swing.text.html.parser;
/*    */ 
/*    */ import javax.swing.text.html.HTML;
/*    */ import javax.swing.text.html.HTML.Tag;
/*    */ import javax.swing.text.html.HTML.UnknownTag;
/*    */ 
/*    */ public class TagElement
/*    */ {
/*    */   Element elem;
/*    */   HTML.Tag htmlTag;
/*    */   boolean insertedByErrorRecovery;
/*    */ 
/*    */   public TagElement(Element paramElement)
/*    */   {
/* 43 */     this(paramElement, false);
/*    */   }
/*    */ 
/*    */   public TagElement(Element paramElement, boolean paramBoolean) {
/* 47 */     this.elem = paramElement;
/* 48 */     this.htmlTag = HTML.getTag(paramElement.getName());
/* 49 */     if (this.htmlTag == null) {
/* 50 */       this.htmlTag = new HTML.UnknownTag(paramElement.getName());
/*    */     }
/* 52 */     this.insertedByErrorRecovery = paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean breaksFlow() {
/* 56 */     return this.htmlTag.breaksFlow();
/*    */   }
/*    */ 
/*    */   public boolean isPreformatted() {
/* 60 */     return this.htmlTag.isPreformatted();
/*    */   }
/*    */ 
/*    */   public Element getElement() {
/* 64 */     return this.elem;
/*    */   }
/*    */ 
/*    */   public HTML.Tag getHTMLTag() {
/* 68 */     return this.htmlTag;
/*    */   }
/*    */ 
/*    */   public boolean fictional() {
/* 72 */     return this.insertedByErrorRecovery;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.TagElement
 * JD-Core Version:    0.6.2
 */