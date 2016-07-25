/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.java_cup.internal.runtime.Scanner;
/*      */ import com.sun.java_cup.internal.runtime.Symbol;
/*      */ import com.sun.java_cup.internal.runtime.lr_parser;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XPathParser extends lr_parser
/*      */ {
/*   30 */   protected static final short[][] _production_table = unpackFromStrings(new String[] { "" });
/*      */ 
/*   81 */   protected static final short[][] _action_table = unpackFromStrings(new String[] { "" });
/*      */ 
/*  663 */   protected static final short[][] _reduce_table = unpackFromStrings(new String[] { "" });
/*      */   protected CUP.XPathParser.actions action_obj;
/*  860 */   public static final Vector EmptyArgs = new Vector(0);
/*      */ 
/*  865 */   public static final VariableRef DummyVarRef = null;
/*      */   private Parser _parser;
/*      */   private XSLTC _xsltc;
/*      */   private String _expression;
/*  881 */   private int _lineNumber = 0;
/*      */   public SymbolTable _symbolTable;
/*      */ 
/*      */   public XPathParser()
/*      */   {
/*      */   }
/*      */ 
/*      */   public XPathParser(Scanner s)
/*      */   {
/*   27 */     super(s);
/*      */   }
/*      */ 
/*      */   public short[][] production_table()
/*      */   {
/*   78 */     return _production_table;
/*      */   }
/*      */ 
/*      */   public short[][] action_table()
/*      */   {
/*  660 */     return _action_table;
/*      */   }
/*      */ 
/*      */   public short[][] reduce_table()
/*      */   {
/*  821 */     return _reduce_table;
/*      */   }
/*      */ 
/*      */   protected void init_actions()
/*      */   {
/*  829 */     this.action_obj = new CUP.XPathParser.actions(this);
/*      */   }
/*      */ 
/*      */   public Symbol do_action(int act_num, lr_parser parser, Stack stack, int top)
/*      */     throws Exception
/*      */   {
/*  841 */     return this.action_obj.CUP$XPathParser$do_action(act_num, parser, stack, top);
/*      */   }
/*      */ 
/*      */   public int start_state() {
/*  845 */     return 0;
/*      */   }
/*  847 */   public int start_production() { return 0; }
/*      */ 
/*      */   public int EOF_sym() {
/*  850 */     return 0;
/*      */   }
/*      */   public int error_sym() {
/*  853 */     return 1;
/*      */   }
/*      */ 
/*      */   public XPathParser(Parser parser)
/*      */   {
/*  889 */     this._parser = parser;
/*  890 */     this._xsltc = parser.getXSLTC();
/*  891 */     this._symbolTable = parser.getSymbolTable();
/*      */   }
/*      */ 
/*      */   public int getLineNumber() {
/*  895 */     return this._lineNumber;
/*      */   }
/*      */ 
/*      */   public QName getQNameIgnoreDefaultNs(String name) {
/*  899 */     return this._parser.getQNameIgnoreDefaultNs(name);
/*      */   }
/*      */ 
/*      */   public QName getQName(String namespace, String prefix, String localname) {
/*  903 */     return this._parser.getQName(namespace, prefix, localname);
/*      */   }
/*      */ 
/*      */   public void setMultiDocument(boolean flag) {
/*  907 */     this._xsltc.setMultiDocument(flag);
/*      */   }
/*      */ 
/*      */   public void setCallsNodeset(boolean flag) {
/*  911 */     this._xsltc.setCallsNodeset(flag);
/*      */   }
/*      */ 
/*      */   public void setHasIdCall(boolean flag) {
/*  915 */     this._xsltc.setHasIdCall(flag);
/*      */   }
/*      */ 
/*      */   public StepPattern createStepPattern(int axis, Object test, Vector predicates)
/*      */   {
/*  932 */     if (test == null) {
/*  933 */       int nodeType = axis == 9 ? -1 : axis == 2 ? 2 : 1;
/*      */ 
/*  936 */       return new StepPattern(axis, nodeType, predicates);
/*      */     }
/*  938 */     if ((test instanceof Integer)) {
/*  939 */       int nodeType = ((Integer)test).intValue();
/*      */ 
/*  941 */       return new StepPattern(axis, nodeType, predicates);
/*      */     }
/*      */ 
/*  944 */     QName name = (QName)test;
/*  945 */     boolean setPriority = false;
/*      */     int nodeType;
/*      */     int nodeType;
/*  947 */     if (axis == 9) {
/*  948 */       nodeType = name.toString().equals("*") ? -1 : this._xsltc.registerNamespacePrefix(name);
/*      */     }
/*      */     else
/*      */     {
/*  952 */       String uri = name.getNamespace();
/*  953 */       String local = name.getLocalPart();
/*  954 */       QName namespace_uri = this._parser.getQNameIgnoreDefaultNs("namespace-uri");
/*      */ 
/*  958 */       if ((uri != null) && ((local.equals("*")) || (local.equals("@*")))) {
/*  959 */         if (predicates == null) {
/*  960 */           predicates = new Vector(2);
/*      */         }
/*      */ 
/*  964 */         setPriority = predicates.size() == 0;
/*      */ 
/*  966 */         predicates.add(new Predicate(new EqualityExpr(0, new NamespaceUriCall(namespace_uri), new LiteralExpr(uri))));
/*      */       }
/*      */       int nodeType;
/*  973 */       if (local.equals("*")) {
/*  974 */         nodeType = axis == 2 ? 2 : 1;
/*      */       }
/*      */       else
/*      */       {
/*      */         int nodeType;
/*  977 */         if (local.equals("@*")) {
/*  978 */           nodeType = 2;
/*      */         }
/*      */         else {
/*  981 */           nodeType = axis == 2 ? this._xsltc.registerAttribute(name) : this._xsltc.registerElement(name);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  986 */     StepPattern result = new StepPattern(axis, nodeType, predicates);
/*      */ 
/*  989 */     if (setPriority) {
/*  990 */       result.setPriority(-0.25D);
/*      */     }
/*      */ 
/*  993 */     return result;
/*      */   }
/*      */ 
/*      */   public int findNodeType(int axis, Object test)
/*      */   {
/*  998 */     if (test == null) {
/*  999 */       return axis == 9 ? -1 : axis == 2 ? 2 : 1;
/*      */     }
/*      */ 
/* 1003 */     if ((test instanceof Integer)) {
/* 1004 */       return ((Integer)test).intValue();
/*      */     }
/*      */ 
/* 1007 */     QName name = (QName)test;
/*      */ 
/* 1009 */     if (axis == 9) {
/* 1010 */       return name.toString().equals("*") ? -1 : this._xsltc.registerNamespacePrefix(name);
/*      */     }
/*      */ 
/* 1014 */     if (name.getNamespace() == null) {
/* 1015 */       String local = name.getLocalPart();
/*      */ 
/* 1017 */       if (local.equals("*")) {
/* 1018 */         return axis == 2 ? 2 : 1;
/*      */       }
/*      */ 
/* 1021 */       if (local.equals("@*")) {
/* 1022 */         return 2;
/*      */       }
/*      */     }
/*      */ 
/* 1026 */     return axis == 2 ? this._xsltc.registerAttribute(name) : this._xsltc.registerElement(name);
/*      */   }
/*      */ 
/*      */   public Symbol parse(String expression, int lineNumber)
/*      */     throws Exception
/*      */   {
/*      */     try
/*      */     {
/* 1044 */       this._expression = expression;
/* 1045 */       this._lineNumber = lineNumber;
/* 1046 */       return super.parse();
/*      */     }
/*      */     catch (IllegalCharException e) {
/* 1049 */       ErrorMsg err = new ErrorMsg("ILLEGAL_CHAR_ERR", lineNumber, e.getMessage());
/*      */ 
/* 1051 */       this._parser.reportError(2, err);
/*      */     }
/* 1053 */     return null;
/*      */   }
/*      */ 
/*      */   final SyntaxTreeNode lookupName(QName name)
/*      */   {
/* 1063 */     SyntaxTreeNode result = this._parser.lookupVariable(name);
/* 1064 */     if (result != null) {
/* 1065 */       return result;
/*      */     }
/* 1067 */     return this._symbolTable.lookupName(name);
/*      */   }
/*      */ 
/*      */   public final void addError(ErrorMsg error) {
/* 1071 */     this._parser.reportError(3, error);
/*      */   }
/*      */ 
/*      */   public void report_error(String message, Object info) {
/* 1075 */     ErrorMsg err = new ErrorMsg("SYNTAX_ERR", this._lineNumber, this._expression);
/*      */ 
/* 1077 */     this._parser.reportError(2, err);
/*      */   }
/*      */ 
/*      */   public void report_fatal_error(String message, Object info)
/*      */   {
/*      */   }
/*      */ 
/*      */   public RelativeLocationPath insertStep(Step step, RelativeLocationPath rlp) {
/* 1085 */     if ((rlp instanceof Step)) {
/* 1086 */       return new ParentLocationPath(step, (Step)rlp);
/*      */     }
/* 1088 */     if ((rlp instanceof ParentLocationPath)) {
/* 1089 */       ParentLocationPath plp = (ParentLocationPath)rlp;
/* 1090 */       RelativeLocationPath newrlp = insertStep(step, plp.getPath());
/* 1091 */       return new ParentLocationPath(newrlp, plp.getStep());
/*      */     }
/*      */ 
/* 1094 */     addError(new ErrorMsg("INTERNAL_ERR", "XPathParser.insertStep"));
/* 1095 */     return rlp;
/*      */   }
/*      */ 
/*      */   public boolean isElementAxis(int axis)
/*      */   {
/* 1105 */     return (axis == 3) || (axis == 2) || (axis == 9) || (axis == 4);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.XPathParser
 * JD-Core Version:    0.6.2
 */