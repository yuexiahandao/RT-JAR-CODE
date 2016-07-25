/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ComponentIdHelper
/*    */ {
/* 18 */   private static String _id = "IDL:omg.org/IOP/ComponentId:1.0";
/*    */ 
/* 33 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, int paramInt)
/*    */   {
/* 22 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 23 */     paramAny.type(type());
/* 24 */     write(localOutputStream, paramInt);
/* 25 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static int extract(Any paramAny)
/*    */   {
/* 30 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 36 */     if (__typeCode == null)
/*    */     {
/* 38 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ulong);
/* 39 */       __typeCode = ORB.init().create_alias_tc(id(), "ComponentId", __typeCode);
/*    */     }
/* 41 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 46 */     return _id;
/*    */   }
/*    */ 
/*    */   public static int read(InputStream paramInputStream)
/*    */   {
/* 51 */     int i = 0;
/* 52 */     i = paramInputStream.read_ulong();
/* 53 */     return i;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, int paramInt)
/*    */   {
/* 58 */     paramOutputStream.write_ulong(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.ComponentIdHelper
 * JD-Core Version:    0.6.2
 */