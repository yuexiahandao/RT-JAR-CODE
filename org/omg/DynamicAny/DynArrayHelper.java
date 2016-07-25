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
/*    */ public abstract class DynArrayHelper
/*    */ {
/* 19 */   private static String _id = "IDL:omg.org/DynamicAny/DynArray:1.0";
/*    */ 
/* 34 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynArray paramDynArray)
/*    */   {
/* 23 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 24 */     paramAny.type(type());
/* 25 */     write(localOutputStream, paramDynArray);
/* 26 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynArray extract(Any paramAny)
/*    */   {
/* 31 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 37 */     if (__typeCode == null)
/*    */     {
/* 39 */       __typeCode = ORB.init().create_interface_tc(id(), "DynArray");
/*    */     }
/* 41 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 46 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynArray read(InputStream paramInputStream)
/*    */   {
/* 51 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynArray paramDynArray)
/*    */   {
/* 56 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static DynArray narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 61 */     if (paramObject == null)
/* 62 */       return null;
/* 63 */     if ((paramObject instanceof DynArray))
/* 64 */       return (DynArray)paramObject;
/* 65 */     if (!paramObject._is_a(id())) {
/* 66 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 69 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 70 */     _DynArrayStub local_DynArrayStub = new _DynArrayStub();
/* 71 */     local_DynArrayStub._set_delegate(localDelegate);
/* 72 */     return local_DynArrayStub;
/*    */   }
/*    */ 
/*    */   public static DynArray unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 78 */     if (paramObject == null)
/* 79 */       return null;
/* 80 */     if ((paramObject instanceof DynArray)) {
/* 81 */       return (DynArray)paramObject;
/*    */     }
/*    */ 
/* 84 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 85 */     _DynArrayStub local_DynArrayStub = new _DynArrayStub();
/* 86 */     local_DynArrayStub._set_delegate(localDelegate);
/* 87 */     return local_DynArrayStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynArrayHelper
 * JD-Core Version:    0.6.2
 */