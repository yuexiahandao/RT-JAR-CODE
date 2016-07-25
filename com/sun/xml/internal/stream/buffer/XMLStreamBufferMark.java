/*    */ package com.sun.xml.internal.stream.buffer;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ public class XMLStreamBufferMark extends XMLStreamBuffer
/*    */ {
/*    */   public XMLStreamBufferMark(Map<String, String> inscopeNamespaces, AbstractCreatorProcessor src)
/*    */   {
/* 62 */     if (inscopeNamespaces != null) {
/* 63 */       this._inscopeNamespaces = inscopeNamespaces;
/*    */     }
/*    */ 
/* 66 */     this._structure = src._currentStructureFragment;
/* 67 */     this._structurePtr = src._structurePtr;
/*    */ 
/* 69 */     this._structureStrings = src._currentStructureStringFragment;
/* 70 */     this._structureStringsPtr = src._structureStringsPtr;
/*    */ 
/* 72 */     this._contentCharactersBuffer = src._currentContentCharactersBufferFragment;
/* 73 */     this._contentCharactersBufferPtr = src._contentCharactersBufferPtr;
/*    */ 
/* 75 */     this._contentObjects = src._currentContentObjectFragment;
/* 76 */     this._contentObjectsPtr = src._contentObjectsPtr;
/* 77 */     this.treeCount = 1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.XMLStreamBufferMark
 * JD-Core Version:    0.6.2
 */