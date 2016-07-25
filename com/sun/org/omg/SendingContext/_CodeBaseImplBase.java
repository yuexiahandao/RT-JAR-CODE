/*     */ package com.sun.org.omg.SendingContext;
/*     */ 
/*     */ import com.sun.org.omg.CORBA.Repository;
/*     */ import com.sun.org.omg.CORBA.RepositoryHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
/*     */ import com.sun.org.omg.SendingContext.CodeBasePackage.URLSeqHelper;
/*     */ import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ 
/*     */ public abstract class _CodeBaseImplBase extends ObjectImpl
/*     */   implements CodeBase, InvokeHandler
/*     */ {
/*  44 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 135 */   private static String[] __ids = { "IDL:omg.org/SendingContext/CodeBase:1.0", "IDL:omg.org/SendingContext/RunTime:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  59 */     OutputStream localOutputStream = paramResponseHandler.createReply();
/*  60 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  61 */     if (localInteger == null)
/*  62 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  64 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*  70 */       localObject1 = null;
/*  71 */       localObject1 = get_ir();
/*  72 */       RepositoryHelper.write(localOutputStream, (Repository)localObject1);
/*  73 */       break;
/*     */     case 1:
/*  80 */       localObject1 = RepositoryIdHelper.read(paramInputStream);
/*  81 */       localObject2 = null;
/*  82 */       localObject2 = implementation((String)localObject1);
/*  83 */       localOutputStream.write_string((String)localObject2);
/*  84 */       break;
/*     */     case 2:
/*  89 */       localObject1 = RepositoryIdSeqHelper.read(paramInputStream);
/*  90 */       localObject2 = null;
/*  91 */       localObject2 = implementations((String[])localObject1);
/*  92 */       URLSeqHelper.write(localOutputStream, (String[])localObject2);
/*  93 */       break;
/*     */     case 3:
/* 100 */       localObject1 = RepositoryIdHelper.read(paramInputStream);
/* 101 */       localObject2 = null;
/* 102 */       localObject2 = meta((String)localObject1);
/* 103 */       FullValueDescriptionHelper.write(localOutputStream, (FullValueDescription)localObject2);
/* 104 */       break;
/*     */     case 4:
/* 109 */       localObject1 = RepositoryIdSeqHelper.read(paramInputStream);
/* 110 */       localObject2 = null;
/* 111 */       localObject2 = metas((String[])localObject1);
/* 112 */       ValueDescSeqHelper.write(localOutputStream, (FullValueDescription[])localObject2);
/* 113 */       break;
/*     */     case 5:
/* 120 */       localObject1 = RepositoryIdHelper.read(paramInputStream);
/* 121 */       localObject2 = null;
/* 122 */       localObject2 = bases((String)localObject1);
/* 123 */       RepositoryIdSeqHelper.write(localOutputStream, (String[])localObject2);
/* 124 */       break;
/*     */     default:
/* 128 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 131 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 141 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  47 */     _methods.put("get_ir", new Integer(0));
/*  48 */     _methods.put("implementation", new Integer(1));
/*  49 */     _methods.put("implementations", new Integer(2));
/*  50 */     _methods.put("meta", new Integer(3));
/*  51 */     _methods.put("metas", new Integer(4));
/*  52 */     _methods.put("bases", new Integer(5));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.SendingContext._CodeBaseImplBase
 * JD-Core Version:    0.6.2
 */