/*    */ package javax.xml.ws;
/*    */ 
/*    */ public abstract class WebServiceFeature
/*    */ {
/* 67 */   protected boolean enabled = false;
/*    */ 
/*    */   public abstract String getID();
/*    */ 
/*    */   public boolean isEnabled()
/*    */   {
/* 79 */     return this.enabled;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.WebServiceFeature
 * JD-Core Version:    0.6.2
 */