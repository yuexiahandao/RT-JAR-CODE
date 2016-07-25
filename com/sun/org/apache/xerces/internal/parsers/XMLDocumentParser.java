/*    */ package com.sun.org.apache.xerces.internal.parsers;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*    */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*    */ 
/*    */ public class XMLDocumentParser extends AbstractXMLDocumentParser
/*    */ {
/*    */   public XMLDocumentParser()
/*    */   {
/* 50 */     super(new XIncludeAwareParserConfiguration());
/*    */   }
/*    */ 
/*    */   public XMLDocumentParser(XMLParserConfiguration config)
/*    */   {
/* 57 */     super(config);
/*    */   }
/*    */ 
/*    */   public XMLDocumentParser(SymbolTable symbolTable)
/*    */   {
/* 64 */     super(new XIncludeAwareParserConfiguration());
/* 65 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*    */   }
/*    */ 
/*    */   public XMLDocumentParser(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*    */   {
/* 74 */     super(new XIncludeAwareParserConfiguration());
/* 75 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/* 76 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", grammarPool);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XMLDocumentParser
 * JD-Core Version:    0.6.2
 */