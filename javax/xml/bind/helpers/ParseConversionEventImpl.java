/*    */ package javax.xml.bind.helpers;
/*    */ 
/*    */ import javax.xml.bind.ParseConversionEvent;
/*    */ import javax.xml.bind.ValidationEventLocator;
/*    */ 
/*    */ public class ParseConversionEventImpl extends ValidationEventImpl
/*    */   implements ParseConversionEvent
/*    */ {
/*    */   public ParseConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator)
/*    */   {
/* 64 */     super(_severity, _message, _locator);
/*    */   }
/*    */ 
/*    */   public ParseConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException)
/*    */   {
/* 83 */     super(_severity, _message, _locator, _linkedException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.ParseConversionEventImpl
 * JD-Core Version:    0.6.2
 */