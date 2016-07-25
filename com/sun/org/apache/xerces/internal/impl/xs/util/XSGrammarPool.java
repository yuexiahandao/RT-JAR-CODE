/*    */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*    */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelImpl;
/*    */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*    */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl.Entry;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*    */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class XSGrammarPool extends XMLGrammarPoolImpl
/*    */ {
/*    */   public XSModel toXSModel()
/*    */   {
/* 50 */     return toXSModel((short)1);
/*    */   }
/*    */ 
/*    */   public XSModel toXSModel(short schemaVersion) {
/* 54 */     ArrayList list = new ArrayList();
/* 55 */     for (int i = 0; i < this.fGrammars.length; i++) {
/* 56 */       for (XMLGrammarPoolImpl.Entry entry = this.fGrammars[i]; entry != null; entry = entry.next) {
/* 57 */         if (entry.desc.getGrammarType().equals("http://www.w3.org/2001/XMLSchema")) {
/* 58 */           list.add(entry.grammar);
/*    */         }
/*    */       }
/*    */     }
/* 62 */     int size = list.size();
/* 63 */     if (size == 0) {
/* 64 */       return toXSModel(new SchemaGrammar[0], schemaVersion);
/*    */     }
/* 66 */     SchemaGrammar[] gs = (SchemaGrammar[])list.toArray(new SchemaGrammar[size]);
/* 67 */     return toXSModel(gs, schemaVersion);
/*    */   }
/*    */ 
/*    */   protected XSModel toXSModel(SchemaGrammar[] grammars, short schemaVersion) {
/* 71 */     return new XSModelImpl(grammars, schemaVersion);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.XSGrammarPool
 * JD-Core Version:    0.6.2
 */