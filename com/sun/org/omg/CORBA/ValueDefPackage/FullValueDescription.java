/*    */ package com.sun.org.omg.CORBA.ValueDefPackage;
/*    */ 
/*    */ import com.sun.org.omg.CORBA.AttributeDescription;
/*    */ import com.sun.org.omg.CORBA.Initializer;
/*    */ import com.sun.org.omg.CORBA.OperationDescription;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.ValueMember;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class FullValueDescription
/*    */   implements IDLEntity
/*    */ {
/* 39 */   public String name = null;
/* 40 */   public String id = null;
/* 41 */   public boolean is_abstract = false;
/* 42 */   public boolean is_custom = false;
/* 43 */   public String defined_in = null;
/* 44 */   public String version = null;
/* 45 */   public OperationDescription[] operations = null;
/* 46 */   public AttributeDescription[] attributes = null;
/*    */ 
/* 50 */   public ValueMember[] members = null;
/*    */ 
/* 52 */   public Initializer[] initializers = null;
/* 53 */   public String[] supported_interfaces = null;
/* 54 */   public String[] abstract_base_values = null;
/* 55 */   public boolean is_truncatable = false;
/* 56 */   public String base_value = null;
/* 57 */   public TypeCode type = null;
/*    */ 
/*    */   public FullValueDescription()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FullValueDescription(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String paramString3, String paramString4, OperationDescription[] paramArrayOfOperationDescription, AttributeDescription[] paramArrayOfAttributeDescription, ValueMember[] paramArrayOfValueMember, Initializer[] paramArrayOfInitializer, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean3, String paramString5, TypeCode paramTypeCode)
/*    */   {
/* 67 */     this.name = paramString1;
/* 68 */     this.id = paramString2;
/* 69 */     this.is_abstract = paramBoolean1;
/* 70 */     this.is_custom = paramBoolean2;
/* 71 */     this.defined_in = paramString3;
/* 72 */     this.version = paramString4;
/* 73 */     this.operations = paramArrayOfOperationDescription;
/* 74 */     this.attributes = paramArrayOfAttributeDescription;
/* 75 */     this.members = paramArrayOfValueMember;
/* 76 */     this.initializers = paramArrayOfInitializer;
/* 77 */     this.supported_interfaces = paramArrayOfString1;
/* 78 */     this.abstract_base_values = paramArrayOfString2;
/* 79 */     this.is_truncatable = paramBoolean3;
/* 80 */     this.base_value = paramString5;
/* 81 */     this.type = paramTypeCode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription
 * JD-Core Version:    0.6.2
 */