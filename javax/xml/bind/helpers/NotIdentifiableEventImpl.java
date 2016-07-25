/*    */ package javax.xml.bind.helpers;
/*    */ 
/*    */ import javax.xml.bind.NotIdentifiableEvent;
/*    */ import javax.xml.bind.ValidationEventLocator;
/*    */ 
/*    */ public class NotIdentifiableEventImpl extends ValidationEventImpl
/*    */   implements NotIdentifiableEvent
/*    */ {
/*    */   public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator)
/*    */   {
/* 63 */     super(_severity, _message, _locator);
/*    */   }
/*    */ 
/*    */   public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException)
/*    */   {
/* 82 */     super(_severity, _message, _locator, _linkedException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.NotIdentifiableEventImpl
 * JD-Core Version:    0.6.2
 */