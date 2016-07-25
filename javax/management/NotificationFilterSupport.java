/*     */ package javax.management;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class NotificationFilterSupport
/*     */   implements NotificationFilter
/*     */ {
/*     */   private static final long serialVersionUID = 6579080007561786969L;
/*  67 */   private List<String> enabledTypes = new Vector();
/*     */ 
/*     */   public synchronized boolean isNotificationEnabled(Notification paramNotification)
/*     */   {
/*  81 */     String str1 = paramNotification.getType();
/*     */ 
/*  83 */     if (str1 == null)
/*  84 */       return false;
/*     */     try
/*     */     {
/*  87 */       for (String str2 : this.enabledTypes) {
/*  88 */         if (str1.startsWith(str2))
/*  89 */           return true;
/*     */       }
/*     */     }
/*     */     catch (NullPointerException localNullPointerException)
/*     */     {
/*  94 */       return false;
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void enableType(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 127 */     if (paramString == null) {
/* 128 */       throw new IllegalArgumentException("The prefix cannot be null.");
/*     */     }
/* 130 */     if (!this.enabledTypes.contains(paramString))
/* 131 */       this.enabledTypes.add(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void disableType(String paramString)
/*     */   {
/* 143 */     this.enabledTypes.remove(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void disableAllTypes()
/*     */   {
/* 150 */     this.enabledTypes.clear();
/*     */   }
/*     */ 
/*     */   public synchronized Vector<String> getEnabledTypes()
/*     */   {
/* 160 */     return (Vector)this.enabledTypes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationFilterSupport
 * JD-Core Version:    0.6.2
 */