/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class PolicyListHelper
/*    */ {
/* 40 */   private static String _id = "IDL:omg.org/CORBA/PolicyList:1.0";
/*    */ 
/* 55 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Policy[] paramArrayOfPolicy)
/*    */   {
/* 44 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 45 */     paramAny.type(type());
/* 46 */     write(localOutputStream, paramArrayOfPolicy);
/* 47 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Policy[] extract(Any paramAny)
/*    */   {
/* 52 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 58 */     if (__typeCode == null)
/*    */     {
/* 60 */       __typeCode = PolicyHelper.type();
/* 61 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 62 */       __typeCode = ORB.init().create_alias_tc(id(), "PolicyList", __typeCode);
/*    */     }
/* 64 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 69 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Policy[] read(InputStream paramInputStream)
/*    */   {
/* 74 */     Policy[] arrayOfPolicy = null;
/* 75 */     int i = paramInputStream.read_long();
/* 76 */     arrayOfPolicy = new Policy[i];
/* 77 */     for (int j = 0; j < arrayOfPolicy.length; j++)
/* 78 */       arrayOfPolicy[j] = PolicyHelper.read(paramInputStream);
/* 79 */     return arrayOfPolicy;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Policy[] paramArrayOfPolicy)
/*    */   {
/* 84 */     paramOutputStream.write_long(paramArrayOfPolicy.length);
/* 85 */     for (int i = 0; i < paramArrayOfPolicy.length; i++)
/* 86 */       PolicyHelper.write(paramOutputStream, paramArrayOfPolicy[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyListHelper
 * JD-Core Version:    0.6.2
 */