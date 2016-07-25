/*    */ package javax.lang.model.element;
/*    */ 
/*    */ import javax.lang.model.UnknownEntityException;
/*    */ 
/*    */ public class UnknownElementException extends UnknownEntityException
/*    */ {
/*    */   private static final long serialVersionUID = 269L;
/*    */   private transient Element element;
/*    */   private transient Object parameter;
/*    */ 
/*    */   public UnknownElementException(Element paramElement, Object paramObject)
/*    */   {
/* 61 */     super("Unknown element: " + paramElement);
/* 62 */     this.element = paramElement;
/* 63 */     this.parameter = paramObject;
/*    */   }
/*    */ 
/*    */   public Element getUnknownElement()
/*    */   {
/* 74 */     return this.element;
/*    */   }
/*    */ 
/*    */   public Object getArgument()
/*    */   {
/* 83 */     return this.parameter;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.UnknownElementException
 * JD-Core Version:    0.6.2
 */