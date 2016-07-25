/*     */ package com.sun.xml.internal.ws.util.exception;
/*     */ 
/*     */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableImpl;
/*     */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*     */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*     */ import com.sun.xml.internal.ws.util.localization.NullLocalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class JAXWSExceptionBase extends WebServiceException
/*     */   implements Localizable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient Localizable msg;
/*     */ 
/*     */   /** @deprecated */
/*     */   protected JAXWSExceptionBase(String key, Object[] args)
/*     */   {
/*  55 */     super(findNestedException(args));
/*  56 */     this.msg = new LocalizableImpl(key, fixNull(args), getDefaultResourceBundleName());
/*     */   }
/*     */ 
/*     */   protected JAXWSExceptionBase(String message)
/*     */   {
/*  61 */     this(new NullLocalizable(message));
/*     */   }
/*     */ 
/*     */   private static Object[] fixNull(Object[] x) {
/*  65 */     if (x == null) return new Object[0];
/*  66 */     return x;
/*     */   }
/*     */ 
/*     */   protected JAXWSExceptionBase(Throwable throwable)
/*     */   {
/*  73 */     this(new NullLocalizable(throwable.toString()), throwable);
/*     */   }
/*     */ 
/*     */   protected JAXWSExceptionBase(Localizable msg) {
/*  77 */     this.msg = msg;
/*     */   }
/*     */ 
/*     */   protected JAXWSExceptionBase(Localizable msg, Throwable cause) {
/*  81 */     super(cause);
/*  82 */     this.msg = msg;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/*  95 */     out.defaultWriteObject();
/*     */ 
/*  97 */     out.writeObject(this.msg.getResourceBundleName());
/*  98 */     out.writeObject(this.msg.getKey());
/*  99 */     Object[] args = this.msg.getArguments();
/* 100 */     if (args == null) {
/* 101 */       out.writeInt(-1);
/* 102 */       return;
/*     */     }
/* 104 */     out.writeInt(args.length);
/*     */ 
/* 106 */     for (int i = 0; i < args.length; i++)
/* 107 */       if ((args[i] == null) || ((args[i] instanceof Serializable)))
/* 108 */         out.writeObject(args[i]);
/*     */       else
/* 110 */         out.writeObject(args[i].toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 117 */     in.defaultReadObject();
/*     */ 
/* 119 */     String resourceBundleName = (String)in.readObject();
/* 120 */     String key = (String)in.readObject();
/* 121 */     int len = in.readInt();
/*     */     Object[] args;
/*     */     Object[] args;
/* 122 */     if (len == -1) {
/* 123 */       args = null;
/*     */     } else {
/* 125 */       args = new Object[len];
/* 126 */       for (int i = 0; i < args.length; i++) {
/* 127 */         args[i] = in.readObject();
/*     */       }
/*     */     }
/* 130 */     this.msg = new LocalizableMessageFactory(resourceBundleName).getMessage(key, args);
/*     */   }
/*     */ 
/*     */   private static Throwable findNestedException(Object[] args) {
/* 134 */     if (args == null) {
/* 135 */       return null;
/*     */     }
/* 137 */     for (Object o : args)
/* 138 */       if ((o instanceof Throwable))
/* 139 */         return (Throwable)o;
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public String getMessage() {
/* 144 */     Localizer localizer = new Localizer();
/* 145 */     return localizer.localize(this);
/*     */   }
/*     */ 
/*     */   protected abstract String getDefaultResourceBundleName();
/*     */ 
/*     */   public final String getKey()
/*     */   {
/* 158 */     return this.msg.getKey();
/*     */   }
/*     */ 
/*     */   public final Object[] getArguments() {
/* 162 */     return this.msg.getArguments();
/*     */   }
/*     */ 
/*     */   public final String getResourceBundleName() {
/* 166 */     return this.msg.getResourceBundleName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
 * JD-Core Version:    0.6.2
 */