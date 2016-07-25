/*    */ package javax.swing.text.html;
/*    */ 
/*    */ import java.net.URL;
/*    */ import javax.swing.event.HyperlinkEvent.EventType;
/*    */ import javax.swing.text.Element;
/*    */ 
/*    */ public class FormSubmitEvent extends HTMLFrameHyperlinkEvent
/*    */ {
/*    */   private MethodType method;
/*    */   private String data;
/*    */ 
/*    */   FormSubmitEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, Element paramElement, String paramString1, MethodType paramMethodType, String paramString2)
/*    */   {
/* 65 */     super(paramObject, paramEventType, paramURL, paramElement, paramString1);
/* 66 */     this.method = paramMethodType;
/* 67 */     this.data = paramString2;
/*    */   }
/*    */ 
/*    */   public MethodType getMethod()
/*    */   {
/* 78 */     return this.method;
/*    */   }
/*    */ 
/*    */   public String getData()
/*    */   {
/* 87 */     return this.data;
/*    */   }
/*    */ 
/*    */   public static enum MethodType
/*    */   {
/* 48 */     GET, POST;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.FormSubmitEvent
 * JD-Core Version:    0.6.2
 */