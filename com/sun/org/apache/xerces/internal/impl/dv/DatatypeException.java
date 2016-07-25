/*     */ package com.sun.org.apache.xerces.internal.impl.dv;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class DatatypeException extends Exception
/*     */ {
/*     */   static final long serialVersionUID = 1940805832730465578L;
/*     */   protected String key;
/*     */   protected Object[] args;
/*     */ 
/*     */   public DatatypeException(String key, Object[] args)
/*     */   {
/*  57 */     super(key);
/*  58 */     this.key = key;
/*  59 */     this.args = args;
/*     */   }
/*     */ 
/*     */   public String getKey()
/*     */   {
/*  68 */     return this.key;
/*     */   }
/*     */ 
/*     */   public Object[] getArgs()
/*     */   {
/*  77 */     return this.args;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  88 */     ResourceBundle resourceBundle = null;
/*  89 */     resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages");
/*  90 */     if (resourceBundle == null) {
/*  91 */       throw new MissingResourceException("Property file not found!", "com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", this.key);
/*     */     }
/*  93 */     String msg = resourceBundle.getString(this.key);
/*  94 */     if (msg == null) {
/*  95 */       msg = resourceBundle.getString("BadMessageKey");
/*  96 */       throw new MissingResourceException(msg, "com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", this.key);
/*     */     }
/*     */ 
/*  99 */     if (this.args != null) {
/*     */       try {
/* 101 */         msg = MessageFormat.format(msg, this.args);
/*     */       } catch (Exception e) {
/* 103 */         msg = resourceBundle.getString("FormatFailed");
/* 104 */         msg = msg + " " + resourceBundle.getString(this.key);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     return msg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.DatatypeException
 * JD-Core Version:    0.6.2
 */