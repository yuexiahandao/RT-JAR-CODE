/*     */ package org.omg.CosNaming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.ObjectHelper;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
/*     */ import org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper;
/*     */ import org.omg.CosNaming.NamingContextPackage.CannotProceed;
/*     */ import org.omg.CosNaming.NamingContextPackage.CannotProceedHelper;
/*     */ import org.omg.CosNaming.NamingContextPackage.InvalidName;
/*     */ import org.omg.CosNaming.NamingContextPackage.InvalidNameHelper;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotEmpty;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotFound;
/*     */ import org.omg.CosNaming.NamingContextPackage.NotFoundHelper;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class NamingContextPOA extends Servant
/*     */   implements NamingContextOperations, InvokeHandler
/*     */ {
/*  27 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 415 */   private static String[] __ids = { "IDL:omg.org/CosNaming/NamingContext:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  46 */     OutputStream localOutputStream = null;
/*  47 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  48 */     if (localInteger == null)
/*  49 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     java.lang.Object localObject2;
/*     */     java.lang.Object localObject1;
/*  51 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  80 */         NameComponent[] arrayOfNameComponent1 = NameHelper.read(paramInputStream);
/*  81 */         localObject2 = ObjectHelper.read(paramInputStream);
/*  82 */         bind(arrayOfNameComponent1, (org.omg.CORBA.Object)localObject2);
/*  83 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound1) {
/*  85 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  86 */         NotFoundHelper.write(localOutputStream, localNotFound1);
/*     */       } catch (CannotProceed localCannotProceed1) {
/*  88 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  89 */         CannotProceedHelper.write(localOutputStream, localCannotProceed1);
/*     */       } catch (InvalidName localInvalidName1) {
/*  91 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  92 */         InvalidNameHelper.write(localOutputStream, localInvalidName1);
/*     */       } catch (AlreadyBound localAlreadyBound1) {
/*  94 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  95 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound1);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/* 124 */         NameComponent[] arrayOfNameComponent2 = NameHelper.read(paramInputStream);
/* 125 */         localObject2 = NamingContextHelper.read(paramInputStream);
/* 126 */         bind_context(arrayOfNameComponent2, (NamingContext)localObject2);
/* 127 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound2) {
/* 129 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 130 */         NotFoundHelper.write(localOutputStream, localNotFound2);
/*     */       } catch (CannotProceed localCannotProceed2) {
/* 132 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 133 */         CannotProceedHelper.write(localOutputStream, localCannotProceed2);
/*     */       } catch (InvalidName localInvalidName2) {
/* 135 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 136 */         InvalidNameHelper.write(localOutputStream, localInvalidName2);
/*     */       } catch (AlreadyBound localAlreadyBound2) {
/* 138 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 139 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound2);
/*     */       }
/*     */ 
/*     */     case 2:
/*     */       try
/*     */       {
/* 166 */         NameComponent[] arrayOfNameComponent3 = NameHelper.read(paramInputStream);
/* 167 */         localObject2 = ObjectHelper.read(paramInputStream);
/* 168 */         rebind(arrayOfNameComponent3, (org.omg.CORBA.Object)localObject2);
/* 169 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound3) {
/* 171 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 172 */         NotFoundHelper.write(localOutputStream, localNotFound3);
/*     */       } catch (CannotProceed localCannotProceed3) {
/* 174 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 175 */         CannotProceedHelper.write(localOutputStream, localCannotProceed3);
/*     */       } catch (InvalidName localInvalidName3) {
/* 177 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 178 */         InvalidNameHelper.write(localOutputStream, localInvalidName3);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 205 */         NameComponent[] arrayOfNameComponent4 = NameHelper.read(paramInputStream);
/* 206 */         localObject2 = NamingContextHelper.read(paramInputStream);
/* 207 */         rebind_context(arrayOfNameComponent4, (NamingContext)localObject2);
/* 208 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound4) {
/* 210 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 211 */         NotFoundHelper.write(localOutputStream, localNotFound4);
/*     */       } catch (CannotProceed localCannotProceed4) {
/* 213 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 214 */         CannotProceedHelper.write(localOutputStream, localCannotProceed4);
/*     */       } catch (InvalidName localInvalidName4) {
/* 216 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 217 */         InvalidNameHelper.write(localOutputStream, localInvalidName4);
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 244 */         NameComponent[] arrayOfNameComponent5 = NameHelper.read(paramInputStream);
/* 245 */         localObject2 = null;
/* 246 */         localObject2 = resolve(arrayOfNameComponent5);
/* 247 */         localOutputStream = paramResponseHandler.createReply();
/* 248 */         ObjectHelper.write(localOutputStream, (org.omg.CORBA.Object)localObject2);
/*     */       } catch (NotFound localNotFound5) {
/* 250 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 251 */         NotFoundHelper.write(localOutputStream, localNotFound5);
/*     */       } catch (CannotProceed localCannotProceed5) {
/* 253 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 254 */         CannotProceedHelper.write(localOutputStream, localCannotProceed5);
/*     */       } catch (InvalidName localInvalidName5) {
/* 256 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 257 */         InvalidNameHelper.write(localOutputStream, localInvalidName5);
/*     */       }
/*     */ 
/*     */     case 5:
/*     */       try
/*     */       {
/* 279 */         NameComponent[] arrayOfNameComponent6 = NameHelper.read(paramInputStream);
/* 280 */         unbind(arrayOfNameComponent6);
/* 281 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound6) {
/* 283 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 284 */         NotFoundHelper.write(localOutputStream, localNotFound6);
/*     */       } catch (CannotProceed localCannotProceed6) {
/* 286 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 287 */         CannotProceedHelper.write(localOutputStream, localCannotProceed6);
/*     */       } catch (InvalidName localInvalidName6) {
/* 289 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 290 */         InvalidNameHelper.write(localOutputStream, localInvalidName6);
/*     */       }
/*     */ 
/*     */     case 6:
/* 318 */       int i = paramInputStream.read_ulong();
/* 319 */       localObject2 = new BindingListHolder();
/* 320 */       BindingIteratorHolder localBindingIteratorHolder = new BindingIteratorHolder();
/* 321 */       list(i, (BindingListHolder)localObject2, localBindingIteratorHolder);
/* 322 */       localOutputStream = paramResponseHandler.createReply();
/* 323 */       BindingListHelper.write(localOutputStream, ((BindingListHolder)localObject2).value);
/* 324 */       BindingIteratorHelper.write(localOutputStream, localBindingIteratorHolder.value);
/* 325 */       break;
/*     */     case 7:
/* 336 */       localObject1 = null;
/* 337 */       localObject1 = new_context();
/* 338 */       localOutputStream = paramResponseHandler.createReply();
/* 339 */       NamingContextHelper.write(localOutputStream, (NamingContext)localObject1);
/* 340 */       break;
/*     */     case 8:
/*     */       try
/*     */       {
/* 367 */         localObject1 = NameHelper.read(paramInputStream);
/* 368 */         localObject2 = null;
/* 369 */         localObject2 = bind_new_context((NameComponent[])localObject1);
/* 370 */         localOutputStream = paramResponseHandler.createReply();
/* 371 */         NamingContextHelper.write(localOutputStream, (NamingContext)localObject2);
/*     */       } catch (NotFound localNotFound7) {
/* 373 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 374 */         NotFoundHelper.write(localOutputStream, localNotFound7);
/*     */       } catch (AlreadyBound localAlreadyBound3) {
/* 376 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 377 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound3);
/*     */       } catch (CannotProceed localCannotProceed7) {
/* 379 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 380 */         CannotProceedHelper.write(localOutputStream, localCannotProceed7);
/*     */       } catch (InvalidName localInvalidName7) {
/* 382 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 383 */         InvalidNameHelper.write(localOutputStream, localInvalidName7);
/*     */       }
/*     */ 
/*     */     case 9:
/*     */       try
/*     */       {
/* 398 */         destroy();
/* 399 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotEmpty localNotEmpty) {
/* 401 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 402 */         NotEmptyHelper.write(localOutputStream, localNotEmpty);
/*     */       }
/*     */ 
/*     */     default:
/* 408 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 411 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*     */   {
/* 420 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   public NamingContext _this()
/*     */   {
/* 425 */     return NamingContextHelper.narrow(super._this_object());
/*     */   }
/*     */ 
/*     */   public NamingContext _this(ORB paramORB)
/*     */   {
/* 431 */     return NamingContextHelper.narrow(super._this_object(paramORB));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  30 */     _methods.put("bind", new Integer(0));
/*  31 */     _methods.put("bind_context", new Integer(1));
/*  32 */     _methods.put("rebind", new Integer(2));
/*  33 */     _methods.put("rebind_context", new Integer(3));
/*  34 */     _methods.put("resolve", new Integer(4));
/*  35 */     _methods.put("unbind", new Integer(5));
/*  36 */     _methods.put("list", new Integer(6));
/*  37 */     _methods.put("new_context", new Integer(7));
/*  38 */     _methods.put("bind_new_context", new Integer(8));
/*  39 */     _methods.put("destroy", new Integer(9));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPOA
 * JD-Core Version:    0.6.2
 */