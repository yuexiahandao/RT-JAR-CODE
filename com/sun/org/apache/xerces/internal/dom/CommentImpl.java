/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import org.w3c.dom.CharacterData;
/*    */ import org.w3c.dom.Comment;
/*    */ 
/*    */ public class CommentImpl extends CharacterDataImpl
/*    */   implements CharacterData, Comment
/*    */ {
/*    */   static final long serialVersionUID = -2685736833408134044L;
/*    */ 
/*    */   public CommentImpl(CoreDocumentImpl ownerDoc, String data)
/*    */   {
/* 51 */     super(ownerDoc, data);
/*    */   }
/*    */ 
/*    */   public short getNodeType()
/*    */   {
/* 63 */     return 8;
/*    */   }
/*    */ 
/*    */   public String getNodeName()
/*    */   {
/* 68 */     return "#comment";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.CommentImpl
 * JD-Core Version:    0.6.2
 */