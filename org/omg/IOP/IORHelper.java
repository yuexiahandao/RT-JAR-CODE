/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class IORHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/IOP/IOR:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, IOR paramIOR)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramIOR);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static IOR extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 32 */     if (__typeCode == null)
/*    */     {
/* 34 */       synchronized (TypeCode.class)
/*    */       {
/* 36 */         if (__typeCode == null)
/*    */         {
/* 38 */           if (__active)
/*    */           {
/* 40 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 42 */           __active = true;
/* 43 */           StructMember[] arrayOfStructMember = new StructMember[2];
/* 44 */           TypeCode localTypeCode = null;
/* 45 */           localTypeCode = ORB.init().create_string_tc(0);
/* 46 */           arrayOfStructMember[0] = new StructMember("type_id", localTypeCode, null);
/*    */ 
/* 50 */           localTypeCode = TaggedProfileHelper.type();
/* 51 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 52 */           arrayOfStructMember[1] = new StructMember("profiles", localTypeCode, null);
/*    */ 
/* 56 */           __typeCode = ORB.init().create_struct_tc(id(), "IOR", arrayOfStructMember);
/* 57 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 61 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 66 */     return _id;
/*    */   }
/*    */ 
/*    */   public static IOR read(InputStream paramInputStream)
/*    */   {
/* 71 */     IOR localIOR = new IOR();
/* 72 */     localIOR.type_id = paramInputStream.read_string();
/* 73 */     int i = paramInputStream.read_long();
/* 74 */     localIOR.profiles = new TaggedProfile[i];
/* 75 */     for (int j = 0; j < localIOR.profiles.length; j++)
/* 76 */       localIOR.profiles[j] = TaggedProfileHelper.read(paramInputStream);
/* 77 */     return localIOR;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, IOR paramIOR)
/*    */   {
/* 82 */     paramOutputStream.write_string(paramIOR.type_id);
/* 83 */     paramOutputStream.write_long(paramIOR.profiles.length);
/* 84 */     for (int i = 0; i < paramIOR.profiles.length; i++)
/* 85 */       TaggedProfileHelper.write(paramOutputStream, paramIOR.profiles[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.IORHelper
 * JD-Core Version:    0.6.2
 */