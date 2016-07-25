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
/*    */ public abstract class DynUnionHelper
/*    */ {
/* 24 */   private static String _id = "IDL:omg.org/DynamicAny/DynUnion:1.0";
/*    */ 
/* 39 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynUnion paramDynUnion)
/*    */   {
/* 28 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 29 */     paramAny.type(type());
/* 30 */     write(localOutputStream, paramDynUnion);
/* 31 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynUnion extract(Any paramAny)
/*    */   {
/* 36 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 42 */     if (__typeCode == null)
/*    */     {
/* 44 */       __typeCode = ORB.init().create_interface_tc(id(), "DynUnion");
/*    */     }
/* 46 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 51 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynUnion read(InputStream paramInputStream)
/*    */   {
/* 56 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynUnion paramDynUnion)
/*    */   {
/* 61 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static DynUnion narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 66 */     if (paramObject == null)
/* 67 */       return null;
/* 68 */     if ((paramObject instanceof DynUnion))
/* 69 */       return (DynUnion)paramObject;
/* 70 */     if (!paramObject._is_a(id())) {
/* 71 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 74 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 75 */     _DynUnionStub local_DynUnionStub = new _DynUnionStub();
/* 76 */     local_DynUnionStub._set_delegate(localDelegate);
/* 77 */     return local_DynUnionStub;
/*    */   }
/*    */ 
/*    */   public static DynUnion unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 83 */     if (paramObject == null)
/* 84 */       return null;
/* 85 */     if ((paramObject instanceof DynUnion)) {
/* 86 */       return (DynUnion)paramObject;
/*    */     }
/*    */ 
/* 89 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 90 */     _DynUnionStub local_DynUnionStub = new _DynUnionStub();
/* 91 */     local_DynUnionStub._set_delegate(localDelegate);
/* 92 */     return local_DynUnionStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynUnionHelper
 * JD-Core Version:    0.6.2
 */