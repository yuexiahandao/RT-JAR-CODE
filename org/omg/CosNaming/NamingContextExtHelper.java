/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.Delegate;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class NamingContextExtHelper
/*    */ {
/* 27 */   private static String _id = "IDL:omg.org/CosNaming/NamingContextExt:1.0";
/*    */ 
/* 42 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, NamingContextExt paramNamingContextExt)
/*    */   {
/* 31 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 32 */     paramAny.type(type());
/* 33 */     write(localOutputStream, paramNamingContextExt);
/* 34 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static NamingContextExt extract(Any paramAny)
/*    */   {
/* 39 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 45 */     if (__typeCode == null)
/*    */     {
/* 47 */       __typeCode = ORB.init().create_interface_tc(id(), "NamingContextExt");
/*    */     }
/* 49 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 54 */     return _id;
/*    */   }
/*    */ 
/*    */   public static NamingContextExt read(InputStream paramInputStream)
/*    */   {
/* 59 */     return narrow(paramInputStream.read_Object(_NamingContextExtStub.class));
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, NamingContextExt paramNamingContextExt)
/*    */   {
/* 64 */     paramOutputStream.write_Object(paramNamingContextExt);
/*    */   }
/*    */ 
/*    */   public static NamingContextExt narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 69 */     if (paramObject == null)
/* 70 */       return null;
/* 71 */     if ((paramObject instanceof NamingContextExt))
/* 72 */       return (NamingContextExt)paramObject;
/* 73 */     if (!paramObject._is_a(id())) {
/* 74 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 77 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 78 */     _NamingContextExtStub local_NamingContextExtStub = new _NamingContextExtStub();
/* 79 */     local_NamingContextExtStub._set_delegate(localDelegate);
/* 80 */     return local_NamingContextExtStub;
/*    */   }
/*    */ 
/*    */   public static NamingContextExt unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 86 */     if (paramObject == null)
/* 87 */       return null;
/* 88 */     if ((paramObject instanceof NamingContextExt)) {
/* 89 */       return (NamingContextExt)paramObject;
/*    */     }
/*    */ 
/* 92 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 93 */     _NamingContextExtStub local_NamingContextExtStub = new _NamingContextExtStub();
/* 94 */     local_NamingContextExtStub._set_delegate(localDelegate);
/* 95 */     return local_NamingContextExtStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextExtHelper
 * JD-Core Version:    0.6.2
 */