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
/*     */ import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
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
/*     */ public abstract class NamingContextExtPOA extends Servant
/*     */   implements NamingContextExtOperations, InvokeHandler
/*     */ {
/*  31 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 545 */   private static String[] __ids = { "IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  54 */     OutputStream localOutputStream = null;
/*  55 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  56 */     if (localInteger == null)
/*  57 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     java.lang.Object localObject2;
/*     */     java.lang.Object localObject3;
/*     */     java.lang.Object localObject1;
/*  59 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  75 */         NameComponent[] arrayOfNameComponent1 = NameHelper.read(paramInputStream);
/*  76 */         localObject2 = null;
/*  77 */         localObject2 = to_string(arrayOfNameComponent1);
/*  78 */         localOutputStream = paramResponseHandler.createReply();
/*  79 */         localOutputStream.write_string((String)localObject2);
/*     */       } catch (InvalidName localInvalidName1) {
/*  81 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  82 */         InvalidNameHelper.write(localOutputStream, localInvalidName1);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/* 101 */         String str1 = StringNameHelper.read(paramInputStream);
/* 102 */         localObject2 = null;
/* 103 */         localObject2 = to_name(str1);
/* 104 */         localOutputStream = paramResponseHandler.createReply();
/* 105 */         NameHelper.write(localOutputStream, (NameComponent[])localObject2);
/*     */       } catch (InvalidName localInvalidName2) {
/* 107 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 108 */         InvalidNameHelper.write(localOutputStream, localInvalidName2);
/*     */       }
/*     */ 
/*     */     case 2:
/*     */       try
/*     */       {
/* 131 */         String str2 = AddressHelper.read(paramInputStream);
/* 132 */         localObject2 = StringNameHelper.read(paramInputStream);
/* 133 */         localObject3 = null;
/* 134 */         localObject3 = to_url(str2, (String)localObject2);
/* 135 */         localOutputStream = paramResponseHandler.createReply();
/* 136 */         localOutputStream.write_string((String)localObject3);
/*     */       } catch (InvalidAddress localInvalidAddress) {
/* 138 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 139 */         InvalidAddressHelper.write(localOutputStream, localInvalidAddress);
/*     */       } catch (InvalidName localInvalidName3) {
/* 141 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 142 */         InvalidNameHelper.write(localOutputStream, localInvalidName3);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 165 */         String str3 = StringNameHelper.read(paramInputStream);
/* 166 */         localObject2 = null;
/* 167 */         localObject2 = resolve_str(str3);
/* 168 */         localOutputStream = paramResponseHandler.createReply();
/* 169 */         ObjectHelper.write(localOutputStream, (org.omg.CORBA.Object)localObject2);
/*     */       } catch (NotFound localNotFound1) {
/* 171 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 172 */         NotFoundHelper.write(localOutputStream, localNotFound1);
/*     */       } catch (CannotProceed localCannotProceed1) {
/* 174 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 175 */         CannotProceedHelper.write(localOutputStream, localCannotProceed1);
/*     */       } catch (InvalidName localInvalidName4) {
/* 177 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 178 */         InvalidNameHelper.write(localOutputStream, localInvalidName4);
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 210 */         NameComponent[] arrayOfNameComponent2 = NameHelper.read(paramInputStream);
/* 211 */         localObject2 = ObjectHelper.read(paramInputStream);
/* 212 */         bind(arrayOfNameComponent2, (org.omg.CORBA.Object)localObject2);
/* 213 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound2) {
/* 215 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 216 */         NotFoundHelper.write(localOutputStream, localNotFound2);
/*     */       } catch (CannotProceed localCannotProceed2) {
/* 218 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 219 */         CannotProceedHelper.write(localOutputStream, localCannotProceed2);
/*     */       } catch (InvalidName localInvalidName5) {
/* 221 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 222 */         InvalidNameHelper.write(localOutputStream, localInvalidName5);
/*     */       } catch (AlreadyBound localAlreadyBound1) {
/* 224 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 225 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound1);
/*     */       }
/*     */ 
/*     */     case 5:
/*     */       try
/*     */       {
/* 254 */         NameComponent[] arrayOfNameComponent3 = NameHelper.read(paramInputStream);
/* 255 */         localObject2 = NamingContextHelper.read(paramInputStream);
/* 256 */         bind_context(arrayOfNameComponent3, (NamingContext)localObject2);
/* 257 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound3) {
/* 259 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 260 */         NotFoundHelper.write(localOutputStream, localNotFound3);
/*     */       } catch (CannotProceed localCannotProceed3) {
/* 262 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 263 */         CannotProceedHelper.write(localOutputStream, localCannotProceed3);
/*     */       } catch (InvalidName localInvalidName6) {
/* 265 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 266 */         InvalidNameHelper.write(localOutputStream, localInvalidName6);
/*     */       } catch (AlreadyBound localAlreadyBound2) {
/* 268 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 269 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound2);
/*     */       }
/*     */ 
/*     */     case 6:
/*     */       try
/*     */       {
/* 296 */         NameComponent[] arrayOfNameComponent4 = NameHelper.read(paramInputStream);
/* 297 */         localObject2 = ObjectHelper.read(paramInputStream);
/* 298 */         rebind(arrayOfNameComponent4, (org.omg.CORBA.Object)localObject2);
/* 299 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound4) {
/* 301 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 302 */         NotFoundHelper.write(localOutputStream, localNotFound4);
/*     */       } catch (CannotProceed localCannotProceed4) {
/* 304 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 305 */         CannotProceedHelper.write(localOutputStream, localCannotProceed4);
/*     */       } catch (InvalidName localInvalidName7) {
/* 307 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 308 */         InvalidNameHelper.write(localOutputStream, localInvalidName7);
/*     */       }
/*     */ 
/*     */     case 7:
/*     */       try
/*     */       {
/* 335 */         NameComponent[] arrayOfNameComponent5 = NameHelper.read(paramInputStream);
/* 336 */         localObject2 = NamingContextHelper.read(paramInputStream);
/* 337 */         rebind_context(arrayOfNameComponent5, (NamingContext)localObject2);
/* 338 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound5) {
/* 340 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 341 */         NotFoundHelper.write(localOutputStream, localNotFound5);
/*     */       } catch (CannotProceed localCannotProceed5) {
/* 343 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 344 */         CannotProceedHelper.write(localOutputStream, localCannotProceed5);
/*     */       } catch (InvalidName localInvalidName8) {
/* 346 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 347 */         InvalidNameHelper.write(localOutputStream, localInvalidName8);
/*     */       }
/*     */ 
/*     */     case 8:
/*     */       try
/*     */       {
/* 374 */         NameComponent[] arrayOfNameComponent6 = NameHelper.read(paramInputStream);
/* 375 */         localObject2 = null;
/* 376 */         localObject2 = resolve(arrayOfNameComponent6);
/* 377 */         localOutputStream = paramResponseHandler.createReply();
/* 378 */         ObjectHelper.write(localOutputStream, (org.omg.CORBA.Object)localObject2);
/*     */       } catch (NotFound localNotFound6) {
/* 380 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 381 */         NotFoundHelper.write(localOutputStream, localNotFound6);
/*     */       } catch (CannotProceed localCannotProceed6) {
/* 383 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 384 */         CannotProceedHelper.write(localOutputStream, localCannotProceed6);
/*     */       } catch (InvalidName localInvalidName9) {
/* 386 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 387 */         InvalidNameHelper.write(localOutputStream, localInvalidName9);
/*     */       }
/*     */ 
/*     */     case 9:
/*     */       try
/*     */       {
/* 409 */         NameComponent[] arrayOfNameComponent7 = NameHelper.read(paramInputStream);
/* 410 */         unbind(arrayOfNameComponent7);
/* 411 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotFound localNotFound7) {
/* 413 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 414 */         NotFoundHelper.write(localOutputStream, localNotFound7);
/*     */       } catch (CannotProceed localCannotProceed7) {
/* 416 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 417 */         CannotProceedHelper.write(localOutputStream, localCannotProceed7);
/*     */       } catch (InvalidName localInvalidName10) {
/* 419 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 420 */         InvalidNameHelper.write(localOutputStream, localInvalidName10);
/*     */       }
/*     */ 
/*     */     case 10:
/* 448 */       int i = paramInputStream.read_ulong();
/* 449 */       localObject2 = new BindingListHolder();
/* 450 */       localObject3 = new BindingIteratorHolder();
/* 451 */       list(i, (BindingListHolder)localObject2, (BindingIteratorHolder)localObject3);
/* 452 */       localOutputStream = paramResponseHandler.createReply();
/* 453 */       BindingListHelper.write(localOutputStream, ((BindingListHolder)localObject2).value);
/* 454 */       BindingIteratorHelper.write(localOutputStream, ((BindingIteratorHolder)localObject3).value);
/* 455 */       break;
/*     */     case 11:
/* 466 */       localObject1 = null;
/* 467 */       localObject1 = new_context();
/* 468 */       localOutputStream = paramResponseHandler.createReply();
/* 469 */       NamingContextHelper.write(localOutputStream, (NamingContext)localObject1);
/* 470 */       break;
/*     */     case 12:
/*     */       try
/*     */       {
/* 497 */         localObject1 = NameHelper.read(paramInputStream);
/* 498 */         localObject2 = null;
/* 499 */         localObject2 = bind_new_context((NameComponent[])localObject1);
/* 500 */         localOutputStream = paramResponseHandler.createReply();
/* 501 */         NamingContextHelper.write(localOutputStream, (NamingContext)localObject2);
/*     */       } catch (NotFound localNotFound8) {
/* 503 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 504 */         NotFoundHelper.write(localOutputStream, localNotFound8);
/*     */       } catch (AlreadyBound localAlreadyBound3) {
/* 506 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 507 */         AlreadyBoundHelper.write(localOutputStream, localAlreadyBound3);
/*     */       } catch (CannotProceed localCannotProceed8) {
/* 509 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 510 */         CannotProceedHelper.write(localOutputStream, localCannotProceed8);
/*     */       } catch (InvalidName localInvalidName11) {
/* 512 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 513 */         InvalidNameHelper.write(localOutputStream, localInvalidName11);
/*     */       }
/*     */ 
/*     */     case 13:
/*     */       try
/*     */       {
/* 528 */         destroy();
/* 529 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (NotEmpty localNotEmpty) {
/* 531 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 532 */         NotEmptyHelper.write(localOutputStream, localNotEmpty);
/*     */       }
/*     */ 
/*     */     default:
/* 538 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 541 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*     */   {
/* 551 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   public NamingContextExt _this()
/*     */   {
/* 556 */     return NamingContextExtHelper.narrow(super._this_object());
/*     */   }
/*     */ 
/*     */   public NamingContextExt _this(ORB paramORB)
/*     */   {
/* 562 */     return NamingContextExtHelper.narrow(super._this_object(paramORB));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  34 */     _methods.put("to_string", new Integer(0));
/*  35 */     _methods.put("to_name", new Integer(1));
/*  36 */     _methods.put("to_url", new Integer(2));
/*  37 */     _methods.put("resolve_str", new Integer(3));
/*  38 */     _methods.put("bind", new Integer(4));
/*  39 */     _methods.put("bind_context", new Integer(5));
/*  40 */     _methods.put("rebind", new Integer(6));
/*  41 */     _methods.put("rebind_context", new Integer(7));
/*  42 */     _methods.put("resolve", new Integer(8));
/*  43 */     _methods.put("unbind", new Integer(9));
/*  44 */     _methods.put("list", new Integer(10));
/*  45 */     _methods.put("new_context", new Integer(11));
/*  46 */     _methods.put("bind_new_context", new Integer(12));
/*  47 */     _methods.put("destroy", new Integer(13));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextExtPOA
 * JD-Core Version:    0.6.2
 */