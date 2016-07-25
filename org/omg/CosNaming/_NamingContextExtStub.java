/*     */ package org.omg.CosNaming;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.ObjectHelper;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.URLStringHelper;
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
/*     */ 
/*     */ public class _NamingContextExtStub extends ObjectImpl
/*     */   implements NamingContextExt
/*     */ {
/* 606 */   private static String[] __ids = { "IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0" };
/*     */ 
/*     */   public String to_string(NameComponent[] paramArrayOfNameComponent)
/*     */     throws InvalidName
/*     */   {
/*  41 */     InputStream localInputStream = null;
/*     */     try {
/*  43 */       OutputStream localOutputStream = _request("to_string", true);
/*  44 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/*  45 */       localInputStream = _invoke(localOutputStream);
/*  46 */       str1 = StringNameHelper.read(localInputStream);
/*  47 */       return str1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  49 */       localInputStream = localApplicationException.getInputStream();
/*  50 */       str1 = localApplicationException.getId();
/*  51 */       if (str1.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/*  52 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/*  54 */       throw new MARSHAL(str1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       String str1;
/*  56 */       return to_string(paramArrayOfNameComponent);
/*     */     } finally {
/*  58 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NameComponent[] to_name(String paramString)
/*     */     throws InvalidName
/*     */   {
/*  75 */     InputStream localInputStream = null;
/*     */     try {
/*  77 */       OutputStream localOutputStream = _request("to_name", true);
/*  78 */       StringNameHelper.write(localOutputStream, paramString);
/*  79 */       localInputStream = _invoke(localOutputStream);
/*  80 */       localObject1 = NameHelper.read(localInputStream);
/*  81 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  83 */       localInputStream = localApplicationException.getInputStream();
/*  84 */       localObject1 = localApplicationException.getId();
/*  85 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/*  86 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/*  88 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  90 */       return to_name(paramString);
/*     */     } finally {
/*  92 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String to_url(String paramString1, String paramString2)
/*     */     throws InvalidAddress, InvalidName
/*     */   {
/* 113 */     InputStream localInputStream = null;
/*     */     try {
/* 115 */       OutputStream localOutputStream = _request("to_url", true);
/* 116 */       AddressHelper.write(localOutputStream, paramString1);
/* 117 */       StringNameHelper.write(localOutputStream, paramString2);
/* 118 */       localInputStream = _invoke(localOutputStream);
/* 119 */       str1 = URLStringHelper.read(localInputStream);
/* 120 */       return str1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 122 */       localInputStream = localApplicationException.getInputStream();
/* 123 */       str1 = localApplicationException.getId();
/* 124 */       if (str1.equals("IDL:omg.org/CosNaming/NamingContextExt/InvalidAddress:1.0"))
/* 125 */         throw InvalidAddressHelper.read(localInputStream);
/* 126 */       if (str1.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 127 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 129 */       throw new MARSHAL(str1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       String str1;
/* 131 */       return to_url(paramString1, paramString2);
/*     */     } finally {
/* 133 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object resolve_str(String paramString)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 154 */     InputStream localInputStream = null;
/*     */     try {
/* 156 */       OutputStream localOutputStream = _request("resolve_str", true);
/* 157 */       StringNameHelper.write(localOutputStream, paramString);
/* 158 */       localInputStream = _invoke(localOutputStream);
/* 159 */       localObject1 = ObjectHelper.read(localInputStream);
/* 160 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 162 */       localInputStream = localApplicationException.getInputStream();
/* 163 */       localObject1 = localApplicationException.getId();
/* 164 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 165 */         throw NotFoundHelper.read(localInputStream);
/* 166 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 167 */         throw CannotProceedHelper.read(localInputStream);
/* 168 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 169 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 171 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 173 */       return resolve_str(paramString);
/*     */     } finally {
/* 175 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*     */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*     */   {
/* 205 */     InputStream localInputStream = null;
/*     */     try {
/* 207 */       OutputStream localOutputStream = _request("bind", true);
/* 208 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 209 */       ObjectHelper.write(localOutputStream, paramObject);
/* 210 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 213 */       localInputStream = localApplicationException.getInputStream();
/* 214 */       String str = localApplicationException.getId();
/* 215 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 216 */         throw NotFoundHelper.read(localInputStream);
/* 217 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 218 */         throw CannotProceedHelper.read(localInputStream);
/* 219 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0"))
/* 220 */         throw InvalidNameHelper.read(localInputStream);
/* 221 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
/* 222 */         throw AlreadyBoundHelper.read(localInputStream);
/*     */       }
/* 224 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 226 */       bind(paramArrayOfNameComponent, paramObject);
/*     */     } finally {
/* 228 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*     */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*     */   {
/* 255 */     InputStream localInputStream = null;
/*     */     try {
/* 257 */       OutputStream localOutputStream = _request("bind_context", true);
/* 258 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 259 */       NamingContextHelper.write(localOutputStream, paramNamingContext);
/* 260 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 263 */       localInputStream = localApplicationException.getInputStream();
/* 264 */       String str = localApplicationException.getId();
/* 265 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 266 */         throw NotFoundHelper.read(localInputStream);
/* 267 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 268 */         throw CannotProceedHelper.read(localInputStream);
/* 269 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0"))
/* 270 */         throw InvalidNameHelper.read(localInputStream);
/* 271 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
/* 272 */         throw AlreadyBoundHelper.read(localInputStream);
/*     */       }
/* 274 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 276 */       bind_context(paramArrayOfNameComponent, paramNamingContext);
/*     */     } finally {
/* 278 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 303 */     InputStream localInputStream = null;
/*     */     try {
/* 305 */       OutputStream localOutputStream = _request("rebind", true);
/* 306 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 307 */       ObjectHelper.write(localOutputStream, paramObject);
/* 308 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 311 */       localInputStream = localApplicationException.getInputStream();
/* 312 */       String str = localApplicationException.getId();
/* 313 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 314 */         throw NotFoundHelper.read(localInputStream);
/* 315 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 316 */         throw CannotProceedHelper.read(localInputStream);
/* 317 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 318 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 320 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 322 */       rebind(paramArrayOfNameComponent, paramObject);
/*     */     } finally {
/* 324 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 349 */     InputStream localInputStream = null;
/*     */     try {
/* 351 */       OutputStream localOutputStream = _request("rebind_context", true);
/* 352 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 353 */       NamingContextHelper.write(localOutputStream, paramNamingContext);
/* 354 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 357 */       localInputStream = localApplicationException.getInputStream();
/* 358 */       String str = localApplicationException.getId();
/* 359 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 360 */         throw NotFoundHelper.read(localInputStream);
/* 361 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 362 */         throw CannotProceedHelper.read(localInputStream);
/* 363 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 364 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 366 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 368 */       rebind_context(paramArrayOfNameComponent, paramNamingContext);
/*     */     } finally {
/* 370 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object resolve(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 395 */     InputStream localInputStream = null;
/*     */     try {
/* 397 */       OutputStream localOutputStream = _request("resolve", true);
/* 398 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 399 */       localInputStream = _invoke(localOutputStream);
/* 400 */       localObject1 = ObjectHelper.read(localInputStream);
/* 401 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 403 */       localInputStream = localApplicationException.getInputStream();
/* 404 */       localObject1 = localApplicationException.getId();
/* 405 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 406 */         throw NotFoundHelper.read(localInputStream);
/* 407 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 408 */         throw CannotProceedHelper.read(localInputStream);
/* 409 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 410 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 412 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 414 */       return resolve(paramArrayOfNameComponent);
/*     */     } finally {
/* 416 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 436 */     InputStream localInputStream = null;
/*     */     try {
/* 438 */       OutputStream localOutputStream = _request("unbind", true);
/* 439 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 440 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 443 */       localInputStream = localApplicationException.getInputStream();
/* 444 */       String str = localApplicationException.getId();
/* 445 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 446 */         throw NotFoundHelper.read(localInputStream);
/* 447 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 448 */         throw CannotProceedHelper.read(localInputStream);
/* 449 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 450 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 452 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 454 */       unbind(paramArrayOfNameComponent);
/*     */     } finally {
/* 456 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void list(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*     */   {
/* 483 */     InputStream localInputStream = null;
/*     */     try {
/* 485 */       OutputStream localOutputStream = _request("list", true);
/* 486 */       localOutputStream.write_ulong(paramInt);
/* 487 */       localInputStream = _invoke(localOutputStream);
/* 488 */       paramBindingListHolder.value = BindingListHelper.read(localInputStream);
/* 489 */       paramBindingIteratorHolder.value = BindingIteratorHelper.read(localInputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 492 */       localInputStream = localApplicationException.getInputStream();
/* 493 */       String str = localApplicationException.getId();
/* 494 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 496 */       list(paramInt, paramBindingListHolder, paramBindingIteratorHolder);
/*     */     } finally {
/* 498 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingContext new_context()
/*     */   {
/* 510 */     InputStream localInputStream = null;
/*     */     try {
/* 512 */       OutputStream localOutputStream = _request("new_context", true);
/* 513 */       localInputStream = _invoke(localOutputStream);
/* 514 */       localObject1 = NamingContextHelper.read(localInputStream);
/* 515 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 517 */       localInputStream = localApplicationException.getInputStream();
/* 518 */       localObject1 = localApplicationException.getId();
/* 519 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 521 */       return new_context();
/*     */     } finally {
/* 523 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingContext bind_new_context(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, AlreadyBound, CannotProceed, InvalidName
/*     */   {
/* 550 */     InputStream localInputStream = null;
/*     */     try {
/* 552 */       OutputStream localOutputStream = _request("bind_new_context", true);
/* 553 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 554 */       localInputStream = _invoke(localOutputStream);
/* 555 */       localObject1 = NamingContextHelper.read(localInputStream);
/* 556 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 558 */       localInputStream = localApplicationException.getInputStream();
/* 559 */       localObject1 = localApplicationException.getId();
/* 560 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 561 */         throw NotFoundHelper.read(localInputStream);
/* 562 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0"))
/* 563 */         throw AlreadyBoundHelper.read(localInputStream);
/* 564 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 565 */         throw CannotProceedHelper.read(localInputStream);
/* 566 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 567 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 569 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 571 */       return bind_new_context(paramArrayOfNameComponent);
/*     */     } finally {
/* 573 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */     throws NotEmpty
/*     */   {
/* 586 */     InputStream localInputStream = null;
/*     */     try {
/* 588 */       OutputStream localOutputStream = _request("destroy", true);
/* 589 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 592 */       localInputStream = localApplicationException.getInputStream();
/* 593 */       String str = localApplicationException.getId();
/* 594 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotEmpty:1.0")) {
/* 595 */         throw NotEmptyHelper.read(localInputStream);
/*     */       }
/* 597 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 599 */       destroy();
/*     */     } finally {
/* 601 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 612 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 617 */     String str = paramObjectInputStream.readUTF();
/* 618 */     String[] arrayOfString = null;
/* 619 */     Properties localProperties = null;
/* 620 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 621 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 622 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 627 */     String[] arrayOfString = null;
/* 628 */     Properties localProperties = null;
/* 629 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 630 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming._NamingContextExtStub
 * JD-Core Version:    0.6.2
 */