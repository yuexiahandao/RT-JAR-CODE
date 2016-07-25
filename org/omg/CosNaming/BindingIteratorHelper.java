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
/*    */ public abstract class BindingIteratorHelper
/*    */ {
/* 22 */   private static String _id = "IDL:omg.org/CosNaming/BindingIterator:1.0";
/*    */ 
/* 37 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, BindingIterator paramBindingIterator)
/*    */   {
/* 26 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 27 */     paramAny.type(type());
/* 28 */     write(localOutputStream, paramBindingIterator);
/* 29 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static BindingIterator extract(Any paramAny)
/*    */   {
/* 34 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 40 */     if (__typeCode == null)
/*    */     {
/* 42 */       __typeCode = ORB.init().create_interface_tc(id(), "BindingIterator");
/*    */     }
/* 44 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 49 */     return _id;
/*    */   }
/*    */ 
/*    */   public static BindingIterator read(InputStream paramInputStream)
/*    */   {
/* 54 */     return narrow(paramInputStream.read_Object(_BindingIteratorStub.class));
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, BindingIterator paramBindingIterator)
/*    */   {
/* 59 */     paramOutputStream.write_Object(paramBindingIterator);
/*    */   }
/*    */ 
/*    */   public static BindingIterator narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 64 */     if (paramObject == null)
/* 65 */       return null;
/* 66 */     if ((paramObject instanceof BindingIterator))
/* 67 */       return (BindingIterator)paramObject;
/* 68 */     if (!paramObject._is_a(id())) {
/* 69 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 72 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 73 */     _BindingIteratorStub local_BindingIteratorStub = new _BindingIteratorStub();
/* 74 */     local_BindingIteratorStub._set_delegate(localDelegate);
/* 75 */     return local_BindingIteratorStub;
/*    */   }
/*    */ 
/*    */   public static BindingIterator unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 81 */     if (paramObject == null)
/* 82 */       return null;
/* 83 */     if ((paramObject instanceof BindingIterator)) {
/* 84 */       return (BindingIterator)paramObject;
/*    */     }
/*    */ 
/* 87 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 88 */     _BindingIteratorStub local_BindingIteratorStub = new _BindingIteratorStub();
/* 89 */     local_BindingIteratorStub._set_delegate(localDelegate);
/* 90 */     return local_BindingIteratorStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingIteratorHelper
 * JD-Core Version:    0.6.2
 */