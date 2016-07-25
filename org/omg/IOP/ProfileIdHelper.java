/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ProfileIdHelper
/*    */ {
/* 15 */   private static String _id = "IDL:omg.org/IOP/ProfileId:1.0";
/*    */ 
/* 30 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, int paramInt)
/*    */   {
/* 19 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 20 */     paramAny.type(type());
/* 21 */     write(localOutputStream, paramInt);
/* 22 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static int extract(Any paramAny)
/*    */   {
/* 27 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 33 */     if (__typeCode == null)
/*    */     {
/* 35 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ulong);
/* 36 */       __typeCode = ORB.init().create_alias_tc(id(), "ProfileId", __typeCode);
/*    */     }
/* 38 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 43 */     return _id;
/*    */   }
/*    */ 
/*    */   public static int read(InputStream paramInputStream)
/*    */   {
/* 48 */     int i = 0;
/* 49 */     i = paramInputStream.read_ulong();
/* 50 */     return i;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, int paramInt)
/*    */   {
/* 55 */     paramOutputStream.write_ulong(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.ProfileIdHelper
 * JD-Core Version:    0.6.2
 */