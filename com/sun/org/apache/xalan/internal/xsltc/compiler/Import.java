/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import java.util.Enumeration;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ final class Import extends TopLevelElement
/*     */ {
/*  50 */   private Stylesheet _imported = null;
/*     */ 
/*     */   public Stylesheet getImportedStylesheet() {
/*  53 */     return this._imported;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  57 */     XSLTC xsltc = parser.getXSLTC();
/*  58 */     Stylesheet context = parser.getCurrentStylesheet();
/*     */     try
/*     */     {
/*  61 */       String docToLoad = getAttribute("href");
/*  62 */       if (context.checkForLoop(docToLoad)) {
/*  63 */         ErrorMsg msg = new ErrorMsg("CIRCULAR_INCLUDE_ERR", docToLoad, this);
/*     */ 
/*  65 */         parser.reportError(2, msg);
/*     */       }
/*     */       else
/*     */       {
/*  69 */         InputSource input = null;
/*  70 */         XMLReader reader = null;
/*  71 */         String currLoadedDoc = context.getSystemId();
/*  72 */         SourceLoader loader = context.getSourceLoader();
/*     */ 
/*  75 */         if (loader != null) {
/*  76 */           input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
/*  77 */           if (input != null) {
/*  78 */             docToLoad = input.getSystemId();
/*  79 */             reader = xsltc.getXMLReader();
/*  80 */           } else if (parser.errorsFound())
/*     */           {
/*     */             return;
/*     */           }
/*     */         }
/*     */ 
/*  86 */         if (input == null) {
/*  87 */           docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
/*  88 */           String accessError = SecuritySupport.checkAccess(docToLoad, (String)xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalStylesheet"), "all");
/*     */ 
/*  92 */           if (accessError != null) { ErrorMsg msg = new ErrorMsg("ACCESSING_XSLT_TARGET_ERR", SecuritySupport.sanitizePath(docToLoad), accessError, this);
/*     */ 
/*  96 */             parser.reportError(2, msg);
/*     */             return;
/*     */           }
/*  99 */           input = new InputSource(docToLoad);
/*     */         }
/*     */ 
/* 103 */         if (input == null) {
/* 104 */           ErrorMsg msg = new ErrorMsg("FILE_NOT_FOUND_ERR", docToLoad, this);
/*     */ 
/* 106 */           parser.reportError(2, msg);
/*     */         }
/*     */         else
/*     */         {
/*     */           SyntaxTreeNode root;
/*     */           SyntaxTreeNode root;
/* 111 */           if (reader != null) {
/* 112 */             root = parser.parse(reader, input);
/*     */           }
/*     */           else {
/* 115 */             root = parser.parse(input);
/*     */           }
/*     */ 
/* 118 */           if (root == null) return;
/* 119 */           this._imported = parser.makeStylesheet(root);
/* 120 */           if (this._imported == null)
/*     */             return;
/* 122 */           this._imported.setSourceLoader(loader);
/* 123 */           this._imported.setSystemId(docToLoad);
/* 124 */           this._imported.setParentStylesheet(context);
/* 125 */           this._imported.setImportingStylesheet(context);
/* 126 */           this._imported.setTemplateInlining(context.getTemplateInlining());
/*     */ 
/* 129 */           int currPrecedence = parser.getCurrentImportPrecedence();
/* 130 */           int nextPrecedence = parser.getNextImportPrecedence();
/* 131 */           this._imported.setImportPrecedence(currPrecedence);
/* 132 */           context.setImportPrecedence(nextPrecedence);
/* 133 */           parser.setCurrentStylesheet(this._imported);
/* 134 */           this._imported.parseContents(parser);
/*     */ 
/* 136 */           Enumeration elements = this._imported.elements();
/* 137 */           Stylesheet topStylesheet = parser.getTopLevelStylesheet();
/* 138 */           while (elements.hasMoreElements()) {
/* 139 */             Object element = elements.nextElement();
/* 140 */             if ((element instanceof TopLevelElement))
/* 141 */               if ((element instanceof Variable)) {
/* 142 */                 topStylesheet.addVariable((Variable)element);
/*     */               }
/* 144 */               else if ((element instanceof Param)) {
/* 145 */                 topStylesheet.addParam((Param)element);
/*     */               }
/*     */               else
/* 148 */                 topStylesheet.addElement((TopLevelElement)element);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 154 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/* 157 */       parser.setCurrentStylesheet(context);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 162 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Import
 * JD-Core Version:    0.6.2
 */