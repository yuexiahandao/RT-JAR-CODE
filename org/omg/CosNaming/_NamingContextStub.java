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
/*     */ public class _NamingContextStub extends ObjectImpl
/*     */   implements NamingContext
/*     */ {
/* 451 */   private static String[] __ids = { "IDL:omg.org/CosNaming/NamingContext:1.0" };
/*     */ 
/*     */   public void bind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*     */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*     */   {
/*  50 */     InputStream localInputStream = null;
/*     */     try {
/*  52 */       OutputStream localOutputStream = _request("bind", true);
/*  53 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/*  54 */       ObjectHelper.write(localOutputStream, paramObject);
/*  55 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  58 */       localInputStream = localApplicationException.getInputStream();
/*  59 */       String str = localApplicationException.getId();
/*  60 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/*  61 */         throw NotFoundHelper.read(localInputStream);
/*  62 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/*  63 */         throw CannotProceedHelper.read(localInputStream);
/*  64 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0"))
/*  65 */         throw InvalidNameHelper.read(localInputStream);
/*  66 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
/*  67 */         throw AlreadyBoundHelper.read(localInputStream);
/*     */       }
/*  69 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  71 */       bind(paramArrayOfNameComponent, paramObject);
/*     */     } finally {
/*  73 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*     */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*     */   {
/* 100 */     InputStream localInputStream = null;
/*     */     try {
/* 102 */       OutputStream localOutputStream = _request("bind_context", true);
/* 103 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 104 */       NamingContextHelper.write(localOutputStream, paramNamingContext);
/* 105 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 108 */       localInputStream = localApplicationException.getInputStream();
/* 109 */       String str = localApplicationException.getId();
/* 110 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 111 */         throw NotFoundHelper.read(localInputStream);
/* 112 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 113 */         throw CannotProceedHelper.read(localInputStream);
/* 114 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0"))
/* 115 */         throw InvalidNameHelper.read(localInputStream);
/* 116 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
/* 117 */         throw AlreadyBoundHelper.read(localInputStream);
/*     */       }
/* 119 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 121 */       bind_context(paramArrayOfNameComponent, paramNamingContext);
/*     */     } finally {
/* 123 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 148 */     InputStream localInputStream = null;
/*     */     try {
/* 150 */       OutputStream localOutputStream = _request("rebind", true);
/* 151 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 152 */       ObjectHelper.write(localOutputStream, paramObject);
/* 153 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 156 */       localInputStream = localApplicationException.getInputStream();
/* 157 */       String str = localApplicationException.getId();
/* 158 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 159 */         throw NotFoundHelper.read(localInputStream);
/* 160 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 161 */         throw CannotProceedHelper.read(localInputStream);
/* 162 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 163 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 165 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 167 */       rebind(paramArrayOfNameComponent, paramObject);
/*     */     } finally {
/* 169 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 194 */     InputStream localInputStream = null;
/*     */     try {
/* 196 */       OutputStream localOutputStream = _request("rebind_context", true);
/* 197 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 198 */       NamingContextHelper.write(localOutputStream, paramNamingContext);
/* 199 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 202 */       localInputStream = localApplicationException.getInputStream();
/* 203 */       String str = localApplicationException.getId();
/* 204 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 205 */         throw NotFoundHelper.read(localInputStream);
/* 206 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 207 */         throw CannotProceedHelper.read(localInputStream);
/* 208 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 209 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 211 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 213 */       rebind_context(paramArrayOfNameComponent, paramNamingContext);
/*     */     } finally {
/* 215 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object resolve(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 240 */     InputStream localInputStream = null;
/*     */     try {
/* 242 */       OutputStream localOutputStream = _request("resolve", true);
/* 243 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 244 */       localInputStream = _invoke(localOutputStream);
/* 245 */       localObject1 = ObjectHelper.read(localInputStream);
/* 246 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 248 */       localInputStream = localApplicationException.getInputStream();
/* 249 */       localObject1 = localApplicationException.getId();
/* 250 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 251 */         throw NotFoundHelper.read(localInputStream);
/* 252 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 253 */         throw CannotProceedHelper.read(localInputStream);
/* 254 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 255 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 257 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 259 */       return resolve(paramArrayOfNameComponent);
/*     */     } finally {
/* 261 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, CannotProceed, InvalidName
/*     */   {
/* 281 */     InputStream localInputStream = null;
/*     */     try {
/* 283 */       OutputStream localOutputStream = _request("unbind", true);
/* 284 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 285 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 288 */       localInputStream = localApplicationException.getInputStream();
/* 289 */       String str = localApplicationException.getId();
/* 290 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 291 */         throw NotFoundHelper.read(localInputStream);
/* 292 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 293 */         throw CannotProceedHelper.read(localInputStream);
/* 294 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 295 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 297 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 299 */       unbind(paramArrayOfNameComponent);
/*     */     } finally {
/* 301 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void list(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*     */   {
/* 328 */     InputStream localInputStream = null;
/*     */     try {
/* 330 */       OutputStream localOutputStream = _request("list", true);
/* 331 */       localOutputStream.write_ulong(paramInt);
/* 332 */       localInputStream = _invoke(localOutputStream);
/* 333 */       paramBindingListHolder.value = BindingListHelper.read(localInputStream);
/* 334 */       paramBindingIteratorHolder.value = BindingIteratorHelper.read(localInputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 337 */       localInputStream = localApplicationException.getInputStream();
/* 338 */       String str = localApplicationException.getId();
/* 339 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 341 */       list(paramInt, paramBindingListHolder, paramBindingIteratorHolder);
/*     */     } finally {
/* 343 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingContext new_context()
/*     */   {
/* 355 */     InputStream localInputStream = null;
/*     */     try {
/* 357 */       OutputStream localOutputStream = _request("new_context", true);
/* 358 */       localInputStream = _invoke(localOutputStream);
/* 359 */       localObject1 = NamingContextHelper.read(localInputStream);
/* 360 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 362 */       localInputStream = localApplicationException.getInputStream();
/* 363 */       localObject1 = localApplicationException.getId();
/* 364 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 366 */       return new_context();
/*     */     } finally {
/* 368 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingContext bind_new_context(NameComponent[] paramArrayOfNameComponent)
/*     */     throws NotFound, AlreadyBound, CannotProceed, InvalidName
/*     */   {
/* 395 */     InputStream localInputStream = null;
/*     */     try {
/* 397 */       OutputStream localOutputStream = _request("bind_new_context", true);
/* 398 */       NameHelper.write(localOutputStream, paramArrayOfNameComponent);
/* 399 */       localInputStream = _invoke(localOutputStream);
/* 400 */       localObject1 = NamingContextHelper.read(localInputStream);
/* 401 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 403 */       localInputStream = localApplicationException.getInputStream();
/* 404 */       localObject1 = localApplicationException.getId();
/* 405 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0"))
/* 406 */         throw NotFoundHelper.read(localInputStream);
/* 407 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0"))
/* 408 */         throw AlreadyBoundHelper.read(localInputStream);
/* 409 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0"))
/* 410 */         throw CannotProceedHelper.read(localInputStream);
/* 411 */       if (((String)localObject1).equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
/* 412 */         throw InvalidNameHelper.read(localInputStream);
/*     */       }
/* 414 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 416 */       return bind_new_context(paramArrayOfNameComponent);
/*     */     } finally {
/* 418 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */     throws NotEmpty
/*     */   {
/* 431 */     InputStream localInputStream = null;
/*     */     try {
/* 433 */       OutputStream localOutputStream = _request("destroy", true);
/* 434 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 437 */       localInputStream = localApplicationException.getInputStream();
/* 438 */       String str = localApplicationException.getId();
/* 439 */       if (str.equals("IDL:omg.org/CosNaming/NamingContext/NotEmpty:1.0")) {
/* 440 */         throw NotEmptyHelper.read(localInputStream);
/*     */       }
/* 442 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 444 */       destroy();
/*     */     } finally {
/* 446 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 456 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 461 */     String str = paramObjectInputStream.readUTF();
/* 462 */     String[] arrayOfString = null;
/* 463 */     Properties localProperties = null;
/* 464 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 465 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 466 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 471 */     String[] arrayOfString = null;
/* 472 */     Properties localProperties = null;
/* 473 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 474 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming._NamingContextStub
 * JD-Core Version:    0.6.2
 */