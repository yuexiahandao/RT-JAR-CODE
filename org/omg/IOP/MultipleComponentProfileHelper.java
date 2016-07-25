/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class MultipleComponentProfileHelper
/*    */ {
/* 15 */   private static String _id = "IDL:omg.org/IOP/MultipleComponentProfile:1.0";
/*    */ 
/* 30 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, TaggedComponent[] paramArrayOfTaggedComponent)
/*    */   {
/* 19 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 20 */     paramAny.type(type());
/* 21 */     write(localOutputStream, paramArrayOfTaggedComponent);
/* 22 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static TaggedComponent[] extract(Any paramAny)
/*    */   {
/* 27 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 33 */     if (__typeCode == null)
/*    */     {
/* 35 */       __typeCode = TaggedComponentHelper.type();
/* 36 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 37 */       __typeCode = ORB.init().create_alias_tc(id(), "MultipleComponentProfile", __typeCode);
/*    */     }
/* 39 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 44 */     return _id;
/*    */   }
/*    */ 
/*    */   public static TaggedComponent[] read(InputStream paramInputStream)
/*    */   {
/* 49 */     TaggedComponent[] arrayOfTaggedComponent = null;
/* 50 */     int i = paramInputStream.read_long();
/* 51 */     arrayOfTaggedComponent = new TaggedComponent[i];
/* 52 */     for (int j = 0; j < arrayOfTaggedComponent.length; j++)
/* 53 */       arrayOfTaggedComponent[j] = TaggedComponentHelper.read(paramInputStream);
/* 54 */     return arrayOfTaggedComponent;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, TaggedComponent[] paramArrayOfTaggedComponent)
/*    */   {
/* 59 */     paramOutputStream.write_long(paramArrayOfTaggedComponent.length);
/* 60 */     for (int i = 0; i < paramArrayOfTaggedComponent.length; i++)
/* 61 */       TaggedComponentHelper.write(paramOutputStream, paramArrayOfTaggedComponent[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.MultipleComponentProfileHelper
 * JD-Core Version:    0.6.2
 */