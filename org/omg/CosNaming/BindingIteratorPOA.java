/*     */ package org.omg.CosNaming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class BindingIteratorPOA extends Servant
/*     */   implements BindingIteratorOperations, InvokeHandler
/*     */ {
/*  26 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 102 */   private static String[] __ids = { "IDL:omg.org/CosNaming/BindingIterator:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  38 */     OutputStream localOutputStream = null;
/*  39 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  40 */     if (localInteger == null) {
/*  41 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*  43 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*  54 */       BindingHolder localBindingHolder = new BindingHolder();
/*  55 */       boolean bool1 = false;
/*  56 */       bool1 = next_one(localBindingHolder);
/*  57 */       localOutputStream = paramResponseHandler.createReply();
/*  58 */       localOutputStream.write_boolean(bool1);
/*  59 */       BindingHelper.write(localOutputStream, localBindingHolder.value);
/*  60 */       break;
/*     */     case 1:
/*  73 */       int i = paramInputStream.read_ulong();
/*  74 */       BindingListHolder localBindingListHolder = new BindingListHolder();
/*  75 */       boolean bool2 = false;
/*  76 */       bool2 = next_n(i, localBindingListHolder);
/*  77 */       localOutputStream = paramResponseHandler.createReply();
/*  78 */       localOutputStream.write_boolean(bool2);
/*  79 */       BindingListHelper.write(localOutputStream, localBindingListHolder.value);
/*  80 */       break;
/*     */     case 2:
/*  89 */       destroy();
/*  90 */       localOutputStream = paramResponseHandler.createReply();
/*  91 */       break;
/*     */     default:
/*  95 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/*  98 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*     */   {
/* 107 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   public BindingIterator _this()
/*     */   {
/* 112 */     return BindingIteratorHelper.narrow(super._this_object());
/*     */   }
/*     */ 
/*     */   public BindingIterator _this(ORB paramORB)
/*     */   {
/* 118 */     return BindingIteratorHelper.narrow(super._this_object(paramORB));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  29 */     _methods.put("next_one", new Integer(0));
/*  30 */     _methods.put("next_n", new Integer(1));
/*  31 */     _methods.put("destroy", new Integer(2));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingIteratorPOA
 * JD-Core Version:    0.6.2
 */