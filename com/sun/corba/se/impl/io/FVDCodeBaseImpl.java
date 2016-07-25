/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.org.omg.CORBA.Repository;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*     */ import com.sun.org.omg.SendingContext._CodeBaseImplBase;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import javax.rmi.CORBA.ValueHandler;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class FVDCodeBaseImpl extends _CodeBaseImplBase
/*     */ {
/*  58 */   private static Hashtable fvds = new Hashtable();
/*     */ 
/*  62 */   private transient ORB orb = null;
/*     */ 
/*  64 */   private transient OMGSystemException wrapper = OMGSystemException.get("rpc.encoding");
/*     */ 
/*  71 */   private transient ValueHandlerImpl vhandler = null;
/*     */ 
/*     */   void setValueHandler(ValueHandler paramValueHandler)
/*     */   {
/*  75 */     this.vhandler = ((ValueHandlerImpl)paramValueHandler);
/*     */   }
/*     */ 
/*     */   public Repository get_ir()
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   public String implementation(String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       if (this.vhandler == null) {
/*  89 */         this.vhandler = ValueHandlerImpl.getInstance(false);
/*     */       }
/*     */ 
/*  94 */       String str = Util.getCodebase(this.vhandler.getClassFromType(paramString));
/*  95 */       if (str == null) {
/*  96 */         return "";
/*     */       }
/*  98 */       return str;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 100 */       throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] implementations(String[] paramArrayOfString)
/*     */   {
/* 106 */     String[] arrayOfString = new String[paramArrayOfString.length];
/*     */ 
/* 108 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 109 */       arrayOfString[i] = implementation(paramArrayOfString[i]);
/*     */     }
/* 111 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public FullValueDescription meta(String paramString)
/*     */   {
/*     */     try {
/* 117 */       FullValueDescription localFullValueDescription = (FullValueDescription)fvds.get(paramString);
/*     */ 
/* 119 */       if (localFullValueDescription == null)
/*     */       {
/* 122 */         if (this.vhandler == null) {
/* 123 */           this.vhandler = ValueHandlerImpl.getInstance(false);
/*     */         }
/*     */         try
/*     */         {
/* 127 */           localFullValueDescription = ValueUtility.translate(_orb(), ObjectStreamClass.lookup(this.vhandler.getAnyClassFromType(paramString)), this.vhandler);
/*     */         }
/*     */         catch (Throwable localThrowable2) {
/* 130 */           if (this.orb == null)
/* 131 */             this.orb = ORB.init();
/* 132 */           localFullValueDescription = ValueUtility.translate(this.orb, ObjectStreamClass.lookup(this.vhandler.getAnyClassFromType(paramString)), this.vhandler);
/*     */         }
/*     */ 
/* 136 */         if (localFullValueDescription != null)
/* 137 */           fvds.put(paramString, localFullValueDescription);
/*     */         else {
/* 139 */           throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE);
/*     */         }
/*     */       }
/*     */ 
/* 143 */       return localFullValueDescription;
/*     */     } catch (Throwable localThrowable1) {
/* 145 */       throw this.wrapper.incompatibleValueImpl(CompletionStatus.COMPLETED_MAYBE, localThrowable1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FullValueDescription[] metas(String[] paramArrayOfString) {
/* 150 */     FullValueDescription[] arrayOfFullValueDescription = new FullValueDescription[paramArrayOfString.length];
/*     */ 
/* 152 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 153 */       arrayOfFullValueDescription[i] = meta(paramArrayOfString[i]);
/*     */     }
/* 155 */     return arrayOfFullValueDescription;
/*     */   }
/*     */ 
/*     */   public String[] bases(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       if (this.vhandler == null) {
/* 164 */         this.vhandler = ValueHandlerImpl.getInstance(false);
/*     */       }
/*     */ 
/* 167 */       Stack localStack = new Stack();
/* 168 */       Class localClass = ObjectStreamClass.lookup(this.vhandler.getClassFromType(paramString)).forClass().getSuperclass();
/*     */ 
/* 170 */       while (!localClass.equals(Object.class)) {
/* 171 */         localStack.push(this.vhandler.createForAnyType(localClass));
/* 172 */         localClass = localClass.getSuperclass();
/*     */       }
/*     */ 
/* 175 */       String[] arrayOfString = new String[localStack.size()];
/* 176 */       for (int i = arrayOfString.length - 1; i >= 0; i++) {
/* 177 */         arrayOfString[i] = ((String)localStack.pop());
/*     */       }
/* 179 */       return arrayOfString;
/*     */     } catch (Throwable localThrowable) {
/* 181 */       throw this.wrapper.missingLocalValueImpl(CompletionStatus.COMPLETED_MAYBE, localThrowable);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.FVDCodeBaseImpl
 * JD-Core Version:    0.6.2
 */