/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class PolicyErrorHelper
/*    */ {
/* 18 */   private static String _id = "IDL:omg.org/CORBA/PolicyError:1.0";
/*    */ 
/* 33 */   private static TypeCode __typeCode = null;
/* 34 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, PolicyError paramPolicyError)
/*    */   {
/* 22 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 23 */     paramAny.type(type());
/* 24 */     write(localOutputStream, paramPolicyError);
/* 25 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static PolicyError extract(Any paramAny)
/*    */   {
/* 30 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 37 */     if (__typeCode == null)
/*    */     {
/* 39 */       synchronized (TypeCode.class)
/*    */       {
/* 41 */         if (__typeCode == null)
/*    */         {
/* 43 */           if (__active)
/*    */           {
/* 45 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 47 */           __active = true;
/* 48 */           StructMember[] arrayOfStructMember = new StructMember[1];
/* 49 */           TypeCode localTypeCode = null;
/* 50 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 51 */           localTypeCode = ORB.init().create_alias_tc(PolicyErrorCodeHelper.id(), "PolicyErrorCode", localTypeCode);
/* 52 */           arrayOfStructMember[0] = new StructMember("reason", localTypeCode, null);
/*    */ 
/* 56 */           __typeCode = ORB.init().create_exception_tc(id(), "PolicyError", arrayOfStructMember);
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
/*    */   public static PolicyError read(InputStream paramInputStream)
/*    */   {
/* 71 */     PolicyError localPolicyError = new PolicyError();
/*    */ 
/* 73 */     paramInputStream.read_string();
/* 74 */     localPolicyError.reason = paramInputStream.read_short();
/* 75 */     return localPolicyError;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, PolicyError paramPolicyError)
/*    */   {
/* 81 */     paramOutputStream.write_string(id());
/* 82 */     paramOutputStream.write_short(paramPolicyError.reason);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyErrorHelper
 * JD-Core Version:    0.6.2
 */