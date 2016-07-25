/*     */ package javax.management;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AttributeChangeNotificationFilter
/*     */   implements NotificationFilter
/*     */ {
/*     */   private static final long serialVersionUID = -6347317584796410029L;
/*  51 */   private Vector<String> enabledAttributes = new Vector();
/*     */ 
/*     */   public synchronized boolean isNotificationEnabled(Notification paramNotification)
/*     */   {
/*  66 */     String str1 = paramNotification.getType();
/*     */ 
/*  68 */     if ((str1 == null) || (!str1.equals("jmx.attribute.change")) || (!(paramNotification instanceof AttributeChangeNotification)))
/*     */     {
/*  71 */       return false;
/*     */     }
/*     */ 
/*  74 */     String str2 = ((AttributeChangeNotification)paramNotification).getAttributeName();
/*     */ 
/*  76 */     return this.enabledAttributes.contains(str2);
/*     */   }
/*     */ 
/*     */   public synchronized void enableAttribute(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/*  90 */     if (paramString == null) {
/*  91 */       throw new IllegalArgumentException("The name cannot be null.");
/*     */     }
/*  93 */     if (!this.enabledAttributes.contains(paramString))
/*  94 */       this.enabledAttributes.addElement(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void disableAttribute(String paramString)
/*     */   {
/* 107 */     this.enabledAttributes.removeElement(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void disableAllAttributes()
/*     */   {
/* 114 */     this.enabledAttributes.removeAllElements();
/*     */   }
/*     */ 
/*     */   public synchronized Vector<String> getEnabledAttributes()
/*     */   {
/* 123 */     return this.enabledAttributes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.AttributeChangeNotificationFilter
 * JD-Core Version:    0.6.2
 */