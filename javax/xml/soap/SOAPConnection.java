/*    */ package javax.xml.soap;
/*    */ 
/*    */ public abstract class SOAPConnection
/*    */ {
/*    */   public abstract SOAPMessage call(SOAPMessage paramSOAPMessage, Object paramObject)
/*    */     throws SOAPException;
/*    */ 
/*    */   public SOAPMessage get(Object to)
/*    */     throws SOAPException
/*    */   {
/* 85 */     throw new UnsupportedOperationException("All subclasses of SOAPConnection must override get()");
/*    */   }
/*    */ 
/*    */   public abstract void close()
/*    */     throws SOAPException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPConnection
 * JD-Core Version:    0.6.2
 */