/*    */ package com.sun.java.util.jar.pack;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.SortedMap;
/*    */ 
/*    */ class TLGlobals
/*    */ {
/*    */   final PropMap props;
/*    */   private final Map<String, ConstantPool.Utf8Entry> utf8Entries;
/*    */   private final Map<String, ConstantPool.ClassEntry> classEntries;
/*    */   private final Map<Object, ConstantPool.LiteralEntry> literalEntries;
/*    */   private final Map<String, ConstantPool.SignatureEntry> signatureEntries;
/*    */   private final Map<String, ConstantPool.DescriptorEntry> descriptorEntries;
/*    */   private final Map<String, ConstantPool.MemberEntry> memberEntries;
/*    */ 
/*    */   TLGlobals()
/*    */   {
/* 61 */     this.utf8Entries = new HashMap();
/* 62 */     this.classEntries = new HashMap();
/* 63 */     this.literalEntries = new HashMap();
/* 64 */     this.signatureEntries = new HashMap();
/* 65 */     this.descriptorEntries = new HashMap();
/* 66 */     this.memberEntries = new HashMap();
/* 67 */     this.props = new PropMap();
/*    */   }
/*    */ 
/*    */   SortedMap<Object, Object> getPropMap() {
/* 71 */     return this.props;
/*    */   }
/*    */ 
/*    */   Map<String, ConstantPool.Utf8Entry> getUtf8Entries() {
/* 75 */     return this.utf8Entries;
/*    */   }
/*    */ 
/*    */   Map<String, ConstantPool.ClassEntry> getClassEntries() {
/* 79 */     return this.classEntries;
/*    */   }
/*    */ 
/*    */   Map<Object, ConstantPool.LiteralEntry> getLiteralEntries() {
/* 83 */     return this.literalEntries;
/*    */   }
/*    */ 
/*    */   Map<String, ConstantPool.DescriptorEntry> getDescriptorEntries() {
/* 87 */     return this.descriptorEntries;
/*    */   }
/*    */ 
/*    */   Map<String, ConstantPool.SignatureEntry> getSignatureEntries() {
/* 91 */     return this.signatureEntries;
/*    */   }
/*    */ 
/*    */   Map<String, ConstantPool.MemberEntry> getMemberEntries() {
/* 95 */     return this.memberEntries;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.TLGlobals
 * JD-Core Version:    0.6.2
 */