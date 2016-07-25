/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class SetOverrideTypeHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/SetOverrideType:1.0";
/*    */ 
/* 54 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, SetOverrideType paramSetOverrideType)
/*    */   {
/* 43 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 44 */     paramAny.type(type());
/* 45 */     write(localOutputStream, paramSetOverrideType);
/* 46 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static SetOverrideType extract(Any paramAny)
/*    */   {
/* 51 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 57 */     if (__typeCode == null)
/*    */     {
/* 59 */       __typeCode = ORB.init().create_enum_tc(id(), "SetOverrideType", new String[] { "SET_OVERRIDE", "ADD_OVERRIDE" });
/*    */     }
/* 61 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 66 */     return _id;
/*    */   }
/*    */ 
/*    */   public static SetOverrideType read(InputStream paramInputStream)
/*    */   {
/* 71 */     return SetOverrideType.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, SetOverrideType paramSetOverrideType)
/*    */   {
/* 76 */     paramOutputStream.write_long(paramSetOverrideType.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.SetOverrideTypeHelper
 * JD-Core Version:    0.6.2
 */