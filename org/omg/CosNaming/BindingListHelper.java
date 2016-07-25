/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class BindingListHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/CosNaming/BindingList:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Binding[] paramArrayOfBinding)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramArrayOfBinding);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Binding[] extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = BindingHelper.type();
/* 38 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 39 */       __typeCode = ORB.init().create_alias_tc(id(), "BindingList", __typeCode);
/*    */     }
/* 41 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 46 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Binding[] read(InputStream paramInputStream)
/*    */   {
/* 51 */     Binding[] arrayOfBinding = null;
/* 52 */     int i = paramInputStream.read_long();
/* 53 */     arrayOfBinding = new Binding[i];
/* 54 */     for (int j = 0; j < arrayOfBinding.length; j++)
/* 55 */       arrayOfBinding[j] = BindingHelper.read(paramInputStream);
/* 56 */     return arrayOfBinding;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Binding[] paramArrayOfBinding)
/*    */   {
/* 61 */     paramOutputStream.write_long(paramArrayOfBinding.length);
/* 62 */     for (int i = 0; i < paramArrayOfBinding.length; i++)
/* 63 */       BindingHelper.write(paramOutputStream, paramArrayOfBinding[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingListHelper
 * JD-Core Version:    0.6.2
 */