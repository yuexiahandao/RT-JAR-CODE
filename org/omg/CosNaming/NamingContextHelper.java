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
/*    */ public abstract class NamingContextHelper
/*    */ {
/* 23 */   private static String _id = "IDL:omg.org/CosNaming/NamingContext:1.0";
/*    */ 
/* 38 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, NamingContext paramNamingContext)
/*    */   {
/* 27 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 28 */     paramAny.type(type());
/* 29 */     write(localOutputStream, paramNamingContext);
/* 30 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static NamingContext extract(Any paramAny)
/*    */   {
/* 35 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 41 */     if (__typeCode == null)
/*    */     {
/* 43 */       __typeCode = ORB.init().create_interface_tc(id(), "NamingContext");
/*    */     }
/* 45 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 50 */     return _id;
/*    */   }
/*    */ 
/*    */   public static NamingContext read(InputStream paramInputStream)
/*    */   {
/* 55 */     return narrow(paramInputStream.read_Object(_NamingContextStub.class));
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, NamingContext paramNamingContext)
/*    */   {
/* 60 */     paramOutputStream.write_Object(paramNamingContext);
/*    */   }
/*    */ 
/*    */   public static NamingContext narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 65 */     if (paramObject == null)
/* 66 */       return null;
/* 67 */     if ((paramObject instanceof NamingContext))
/* 68 */       return (NamingContext)paramObject;
/* 69 */     if (!paramObject._is_a(id())) {
/* 70 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 73 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 74 */     _NamingContextStub local_NamingContextStub = new _NamingContextStub();
/* 75 */     local_NamingContextStub._set_delegate(localDelegate);
/* 76 */     return local_NamingContextStub;
/*    */   }
/*    */ 
/*    */   public static NamingContext unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 82 */     if (paramObject == null)
/* 83 */       return null;
/* 84 */     if ((paramObject instanceof NamingContext)) {
/* 85 */       return (NamingContext)paramObject;
/*    */     }
/*    */ 
/* 88 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 89 */     _NamingContextStub local_NamingContextStub = new _NamingContextStub();
/* 90 */     local_NamingContextStub._set_delegate(localDelegate);
/* 91 */     return local_NamingContextStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextHelper
 * JD-Core Version:    0.6.2
 */