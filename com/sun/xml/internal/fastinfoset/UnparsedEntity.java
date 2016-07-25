/*    */ package com.sun.xml.internal.fastinfoset;
/*    */ 
/*    */ public class UnparsedEntity extends Notation
/*    */ {
/*    */   public final String notationName;
/*    */ 
/*    */   public UnparsedEntity(String _name, String _systemIdentifier, String _publicIdentifier, String _notationName)
/*    */   {
/* 35 */     super(_name, _systemIdentifier, _publicIdentifier);
/* 36 */     this.notationName = _notationName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.UnparsedEntity
 * JD-Core Version:    0.6.2
 */