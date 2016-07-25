/*    */ package javax.xml.bind;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract class JAXBIntrospector
/*    */ {
/*    */   public abstract boolean isElement(Object paramObject);
/*    */ 
/*    */   public abstract QName getElementName(Object paramObject);
/*    */ 
/*    */   public static Object getValue(Object jaxbElement)
/*    */   {
/* 84 */     if ((jaxbElement instanceof JAXBElement)) {
/* 85 */       return ((JAXBElement)jaxbElement).getValue();
/*    */     }
/*    */ 
/* 89 */     return jaxbElement;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.JAXBIntrospector
 * JD-Core Version:    0.6.2
 */