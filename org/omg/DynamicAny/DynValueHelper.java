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
/*    */ public abstract class DynValueHelper
/*    */ {
/* 26 */   private static String _id = "IDL:omg.org/DynamicAny/DynValue:1.0";
/*    */ 
/* 41 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynValue paramDynValue)
/*    */   {
/* 30 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 31 */     paramAny.type(type());
/* 32 */     write(localOutputStream, paramDynValue);
/* 33 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynValue extract(Any paramAny)
/*    */   {
/* 38 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 44 */     if (__typeCode == null)
/*    */     {
/* 46 */       __typeCode = ORB.init().create_interface_tc(id(), "DynValue");
/*    */     }
/* 48 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 53 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynValue read(InputStream paramInputStream)
/*    */   {
/* 58 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynValue paramDynValue)
/*    */   {
/* 63 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static DynValue narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 68 */     if (paramObject == null)
/* 69 */       return null;
/* 70 */     if ((paramObject instanceof DynValue))
/* 71 */       return (DynValue)paramObject;
/* 72 */     if (!paramObject._is_a(id())) {
/* 73 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 76 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 77 */     _DynValueStub local_DynValueStub = new _DynValueStub();
/* 78 */     local_DynValueStub._set_delegate(localDelegate);
/* 79 */     return local_DynValueStub;
/*    */   }
/*    */ 
/*    */   public static DynValue unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 85 */     if (paramObject == null)
/* 86 */       return null;
/* 87 */     if ((paramObject instanceof DynValue)) {
/* 88 */       return (DynValue)paramObject;
/*    */     }
/*    */ 
/* 91 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 92 */     _DynValueStub local_DynValueStub = new _DynValueStub();
/* 93 */     local_DynValueStub._set_delegate(localDelegate);
/* 94 */     return local_DynValueStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynValueHelper
 * JD-Core Version:    0.6.2
 */