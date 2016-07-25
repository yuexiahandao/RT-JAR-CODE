/*    */ package javax.xml.bind.helpers;
/*    */ 
/*    */ import javax.xml.bind.PrintConversionEvent;
/*    */ import javax.xml.bind.ValidationEventLocator;
/*    */ 
/*    */ public class PrintConversionEventImpl extends ValidationEventImpl
/*    */   implements PrintConversionEvent
/*    */ {
/*    */   public PrintConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator)
/*    */   {
/* 64 */     super(_severity, _message, _locator);
/*    */   }
/*    */ 
/*    */   public PrintConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException)
/*    */   {
/* 83 */     super(_severity, _message, _locator, _linkedException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.helpers.PrintConversionEventImpl
 * JD-Core Version:    0.6.2
 */