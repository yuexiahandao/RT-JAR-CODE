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
/*     */ final class Include extends TopLevelElement
/*     */ {
/*  51 */   private Stylesheet _included = null;
/*     */ 
/*     */   public Stylesheet getIncludedStylesheet() {
/*  54 */     return this._included;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  58 */     XSLTC xsltc = parser.getXSLTC();
/*  59 */     Stylesheet context = parser.getCurrentStylesheet();
/*     */ 
/*  61 */     String docToLoad = getAttribute("href");
/*     */     try {
/*  63 */       if (context.checkForLoop(docToLoad)) {
/*  64 */         ErrorMsg msg = new ErrorMsg("CIRCULAR_INCLUDE_ERR", docToLoad, this);
/*     */ 
/*  66 */         parser.reportError(2, msg);
/*     */       }
/*     */       else
/*     */       {
/*  70 */         InputSource input = null;
/*  71 */         XMLReader reader = null;
/*  72 */         String currLoadedDoc = context.getSystemId();
/*  73 */         SourceLoader loader = context.getSourceLoader();
/*     */ 
/*  76 */         if (loader != null) {
/*  77 */           input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
/*  78 */           if (input != null) {
/*  79 */             docToLoad = input.getSystemId();
/*  80 */             reader = xsltc.getXMLReader();
/*  81 */           } else if (parser.errorsFound())
/*     */           {
/*     */             return;
/*     */           }
/*     */         }
/*     */ 
/*  87 */         if (input == null) {
/*  88 */           docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
/*  89 */           String accessError = SecuritySupport.checkAccess(docToLoad, (String)xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalStylesheet"), "all");
/*     */ 
/*  93 */           if (accessError != null) { ErrorMsg msg = new ErrorMsg("ACCESSING_XSLT_TARGET_ERR", SecuritySupport.sanitizePath(docToLoad), accessError, this);
/*     */ 
/*  97 */             parser.reportError(2, msg);
/*     */             return;
/*     */           }
/* 100 */           input = new InputSource(docToLoad);
/*     */         }
/*     */ 
/* 104 */         if (input == null) {
/* 105 */           ErrorMsg msg = new ErrorMsg("FILE_NOT_FOUND_ERR", docToLoad, this);
/*     */ 
/* 107 */           parser.reportError(2, msg);
/*     */         }
/*     */         else
/*     */         {
/*     */           SyntaxTreeNode root;
/*     */           SyntaxTreeNode root;
/* 112 */           if (reader != null) {
/* 113 */             root = parser.parse(reader, input);
/*     */           }
/*     */           else {
/* 116 */             root = parser.parse(input);
/*     */           }
/*     */ 
/* 119 */           if (root == null) return;
/* 120 */           this._included = parser.makeStylesheet(root);
/* 121 */           if (this._included == null)
/*     */             return;
/* 123 */           this._included.setSourceLoader(loader);
/* 124 */           this._included.setSystemId(docToLoad);
/* 125 */           this._included.setParentStylesheet(context);
/* 126 */           this._included.setIncludingStylesheet(context);
/* 127 */           this._included.setTemplateInlining(context.getTemplateInlining());
/*     */ 
/* 131 */           int precedence = context.getImportPrecedence();
/* 132 */           this._included.setImportPrecedence(precedence);
/* 133 */           parser.setCurrentStylesheet(this._included);
/* 134 */           this._included.parseContents(parser);
/*     */ 
/* 136 */           Enumeration elements = this._included.elements();
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
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Include
 * JD-Core Version:    0.6.2
 */