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
/*    */ public abstract class DynSequenceHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/DynamicAny/DynSequence:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynSequence paramDynSequence)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramDynSequence);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynSequence extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ORB.init().create_interface_tc(id(), "DynSequence");
/*    */     }
/* 39 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 44 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynSequence read(InputStream paramInputStream)
/*    */   {
/* 49 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynSequence paramDynSequence)
/*    */   {
/* 54 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static DynSequence narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 59 */     if (paramObject == null)
/* 60 */       return null;
/* 61 */     if ((paramObject instanceof DynSequence))
/* 62 */       return (DynSequence)paramObject;
/* 63 */     if (!paramObject._is_a(id())) {
/* 64 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 67 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 68 */     _DynSequenceStub local_DynSequenceStub = new _DynSequenceStub();
/* 69 */     local_DynSequenceStub._set_delegate(localDelegate);
/* 70 */     return local_DynSequenceStub;
/*    */   }
/*    */ 
/*    */   public static DynSequence unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 76 */     if (paramObject == null)
/* 77 */       return null;
/* 78 */     if ((paramObject instanceof DynSequence)) {
/* 79 */       return (DynSequence)paramObject;
/*    */     }
/*    */ 
/* 82 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 83 */     _DynSequenceStub local_DynSequenceStub = new _DynSequenceStub();
/* 84 */     local_DynSequenceStub._set_delegate(localDelegate);
/* 85 */     return local_DynSequenceStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynSequenceHelper
 * JD-Core Version:    0.6.2
 */