/*     */ package org.omg.PortableServer;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class ServantLocatorHelper
/*     */ {
/*  32 */   private static String _id = "IDL:omg.org/PortableServer/ServantLocator:1.0";
/*     */ 
/*  47 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, ServantLocator paramServantLocator)
/*     */   {
/*  36 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  37 */     paramAny.type(type());
/*  38 */     write(localOutputStream, paramServantLocator);
/*  39 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ServantLocator extract(Any paramAny)
/*     */   {
/*  44 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  50 */     if (__typeCode == null)
/*     */     {
/*  52 */       __typeCode = ORB.init().create_interface_tc(id(), "ServantLocator");
/*     */     }
/*  54 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  59 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ServantLocator read(InputStream paramInputStream)
/*     */   {
/*  64 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ServantLocator paramServantLocator)
/*     */   {
/*  69 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static ServantLocator narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/*  74 */     if (paramObject == null)
/*  75 */       return null;
/*  76 */     if ((paramObject instanceof ServantLocator))
/*  77 */       return (ServantLocator)paramObject;
/*  78 */     if (!paramObject._is_a(id())) {
/*  79 */       throw new BAD_PARAM();
/*     */     }
/*     */ 
/*  82 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/*  83 */     _ServantLocatorStub local_ServantLocatorStub = new _ServantLocatorStub();
/*  84 */     local_ServantLocatorStub._set_delegate(localDelegate);
/*  85 */     return local_ServantLocatorStub;
/*     */   }
/*     */ 
/*     */   public static ServantLocator unchecked_narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/*  91 */     if (paramObject == null)
/*  92 */       return null;
/*  93 */     if ((paramObject instanceof ServantLocator)) {
/*  94 */       return (ServantLocator)paramObject;
/*     */     }
/*     */ 
/*  97 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/*  98 */     _ServantLocatorStub local_ServantLocatorStub = new _ServantLocatorStub();
/*  99 */     local_ServantLocatorStub._set_delegate(localDelegate);
/* 100 */     return local_ServantLocatorStub;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantLocatorHelper
 * JD-Core Version:    0.6.2
 */