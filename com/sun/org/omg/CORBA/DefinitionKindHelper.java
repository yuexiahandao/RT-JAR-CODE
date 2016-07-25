/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.DefinitionKind;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class DefinitionKindHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/DefinitionKind:1.0";
/*    */ 
/* 62 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DefinitionKind paramDefinitionKind)
/*    */   {
/* 49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 50 */     paramAny.type(type());
/* 51 */     write(localOutputStream, paramDefinitionKind);
/* 52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DefinitionKind extract(Any paramAny)
/*    */   {
/* 59 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 65 */     if (__typeCode == null)
/*    */     {
/* 67 */       __typeCode = ORB.init().create_enum_tc(id(), "DefinitionKind", new String[] { "dk_none", "dk_all", "dk_Attribute", "dk_Constant", "dk_Exception", "dk_Interface", "dk_Module", "dk_Operation", "dk_Typedef", "dk_Alias", "dk_Struct", "dk_Union", "dk_Enum", "dk_Primitive", "dk_String", "dk_Sequence", "dk_Array", "dk_Repository", "dk_Wstring", "dk_Fixed", "dk_Value", "dk_ValueBox", "dk_ValueMember", "dk_Native" });
/*    */     }
/* 69 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 74 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DefinitionKind read(InputStream paramInputStream)
/*    */   {
/* 83 */     return DefinitionKind.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DefinitionKind paramDefinitionKind)
/*    */   {
/* 90 */     paramOutputStream.write_long(paramDefinitionKind.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.DefinitionKindHelper
 * JD-Core Version:    0.6.2
 */