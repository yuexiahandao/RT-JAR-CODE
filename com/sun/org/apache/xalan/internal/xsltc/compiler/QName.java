/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ final class QName
/*    */ {
/*    */   private final String _localname;
/*    */   private String _prefix;
/*    */   private String _namespace;
/*    */   private String _stringRep;
/*    */   private int _hashCode;
/*    */ 
/*    */   public QName(String namespace, String prefix, String localname)
/*    */   {
/* 39 */     this._namespace = namespace;
/* 40 */     this._prefix = prefix;
/* 41 */     this._localname = localname;
/*    */ 
/* 43 */     this._stringRep = ((namespace != null) && (!namespace.equals("")) ? namespace + ':' + localname : localname);
/*    */ 
/* 47 */     this._hashCode = (this._stringRep.hashCode() + 19);
/*    */   }
/*    */ 
/*    */   public void clearNamespace() {
/* 51 */     this._namespace = "";
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 55 */     return this._stringRep;
/*    */   }
/*    */ 
/*    */   public String getStringRep() {
/* 59 */     return this._stringRep;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other) {
/* 63 */     return (this == other) || (((other instanceof QName)) && (this._stringRep.equals(((QName)other).getStringRep())));
/*    */   }
/*    */ 
/*    */   public String getLocalPart()
/*    */   {
/* 69 */     return this._localname;
/*    */   }
/*    */ 
/*    */   public String getNamespace() {
/* 73 */     return this._namespace;
/*    */   }
/*    */ 
/*    */   public String getPrefix() {
/* 77 */     return this._prefix;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 81 */     return this._hashCode;
/*    */   }
/*    */ 
/*    */   public String dump() {
/* 85 */     return "QName: " + this._namespace + "(" + this._prefix + "):" + this._localname;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.QName
 * JD-Core Version:    0.6.2
 */