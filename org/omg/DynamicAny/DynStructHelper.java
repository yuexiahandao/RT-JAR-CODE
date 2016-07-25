/*    */ package org.omg.DynamicAny;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.MARSHAL;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.Delegate;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class DynStructHelper
/*    */ {
/* 18 */   private static String _id = "IDL:omg.org/DynamicAny/DynStruct:1.0";
/*    */ 
/* 33 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynStruct paramDynStruct)
/*    */   {
/* 22 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 23 */     paramAny.type(type());
/* 24 */     write(localOutputStream, paramDynStruct);
/* 25 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynStruct extract(Any paramAny)
/*    */   {
/* 30 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 36 */     if (__typeCode == null)
/*    */     {
/* 38 */       __typeCode = ORB.init().create_interface_tc(id(), "DynStruct");
/*    */     }
/* 40 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 45 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynStruct read(InputStream paramInputStream)
/*    */   {
/* 50 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynStruct paramDynStruct)
/*    */   {
/* 55 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static DynStruct narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 60 */     if (paramObject == null)
/* 61 */       return null;
/* 62 */     if ((paramObject instanceof DynStruct))
/* 63 */       return (DynStruct)paramObject;
/* 64 */     if (!paramObject._is_a(id())) {
/* 65 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 68 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 69 */     _DynStructStub local_DynStructStub = new _DynStructStub();
/* 70 */     local_DynStructStub._set_delegate(localDelegate);
/* 71 */     return local_DynStructStub;
/*    */   }
/*    */ 
/*    */   public static DynStruct unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 77 */     if (paramObject == null)
/* 78 */       return null;
/* 79 */     if ((paramObject instanceof DynStruct)) {
/* 80 */       return (DynStruct)paramObject;
/*    */     }
/*    */ 
/* 83 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 84 */     _DynStructStub local_DynStructStub = new _DynStructStub();
/* 85 */     local_DynStructStub._set_delegate(localDelegate);
/* 86 */     return local_DynStructStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynStructHelper
 * JD-Core Version:    0.6.2
 */