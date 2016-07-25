/*     */ package sun.applet;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ class AppletMessageHandler
/*     */ {
/*     */   private static ResourceBundle rb;
/*  39 */   private String baseKey = null;
/*     */ 
/*     */   AppletMessageHandler(String paramString)
/*     */   {
/*  52 */     this.baseKey = paramString;
/*     */   }
/*     */ 
/*     */   String getMessage(String paramString) {
/*  56 */     return rb.getString(getQualifiedKey(paramString));
/*     */   }
/*     */ 
/*     */   String getMessage(String paramString, Object paramObject) {
/*  60 */     String str = rb.getString(getQualifiedKey(paramString));
/*  61 */     MessageFormat localMessageFormat = new MessageFormat(str);
/*  62 */     Object[] arrayOfObject = new Object[1];
/*  63 */     if (paramObject == null) {
/*  64 */       paramObject = "null";
/*     */     }
/*  66 */     arrayOfObject[0] = paramObject;
/*  67 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   String getMessage(String paramString, Object paramObject1, Object paramObject2) {
/*  71 */     String str = rb.getString(getQualifiedKey(paramString));
/*  72 */     MessageFormat localMessageFormat = new MessageFormat(str);
/*  73 */     Object[] arrayOfObject = new Object[2];
/*  74 */     if (paramObject1 == null) {
/*  75 */       paramObject1 = "null";
/*     */     }
/*  77 */     if (paramObject2 == null) {
/*  78 */       paramObject2 = "null";
/*     */     }
/*  80 */     arrayOfObject[0] = paramObject1;
/*  81 */     arrayOfObject[1] = paramObject2;
/*  82 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   String getMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/*  86 */     String str = rb.getString(getQualifiedKey(paramString));
/*  87 */     MessageFormat localMessageFormat = new MessageFormat(str);
/*  88 */     Object[] arrayOfObject = new Object[3];
/*  89 */     if (paramObject1 == null) {
/*  90 */       paramObject1 = "null";
/*     */     }
/*  92 */     if (paramObject2 == null) {
/*  93 */       paramObject2 = "null";
/*     */     }
/*  95 */     if (paramObject3 == null) {
/*  96 */       paramObject3 = "null";
/*     */     }
/*  98 */     arrayOfObject[0] = paramObject1;
/*  99 */     arrayOfObject[1] = paramObject2;
/* 100 */     arrayOfObject[2] = paramObject3;
/* 101 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   String getMessage(String paramString, Object[] paramArrayOfObject) {
/* 105 */     String str = rb.getString(getQualifiedKey(paramString));
/* 106 */     MessageFormat localMessageFormat = new MessageFormat(str);
/* 107 */     return localMessageFormat.format(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   String getQualifiedKey(String paramString) {
/* 111 */     return this.baseKey + "." + paramString;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  43 */       rb = ResourceBundle.getBundle("sun.applet.resources.MsgAppletViewer");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/*  46 */       System.out.println(localMissingResourceException.getMessage());
/*  47 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletMessageHandler
 * JD-Core Version:    0.6.2
 */