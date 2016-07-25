/*      */ package com.sun.org.apache.xerces.internal.impl.xpath;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XPath
/*      */ {
/*      */   private static final boolean DEBUG_ALL = false;
/*      */   private static final boolean DEBUG_XPATH_PARSE = false;
/*      */   private static final boolean DEBUG_ANY = false;
/*      */   protected String fExpression;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected LocationPath[] fLocationPaths;
/*      */ 
/*      */   public XPath(String xpath, SymbolTable symbolTable, NamespaceContext context)
/*      */     throws XPathException
/*      */   {
/*   72 */     this.fExpression = xpath;
/*   73 */     this.fSymbolTable = symbolTable;
/*   74 */     parseExpression(context);
/*      */   }
/*      */ 
/*      */   public LocationPath[] getLocationPaths()
/*      */   {
/*   86 */     LocationPath[] ret = new LocationPath[this.fLocationPaths.length];
/*   87 */     for (int i = 0; i < this.fLocationPaths.length; i++) {
/*   88 */       ret[i] = ((LocationPath)this.fLocationPaths[i].clone());
/*      */     }
/*   90 */     return ret;
/*      */   }
/*      */ 
/*      */   public LocationPath getLocationPath()
/*      */   {
/*   95 */     return (LocationPath)this.fLocationPaths[0].clone();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  104 */     StringBuffer buf = new StringBuffer();
/*  105 */     for (int i = 0; i < this.fLocationPaths.length; i++) {
/*  106 */       if (i > 0) {
/*  107 */         buf.append("|");
/*      */       }
/*  109 */       buf.append(this.fLocationPaths[i].toString());
/*      */     }
/*  111 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   private static void check(boolean b)
/*      */     throws XPathException
/*      */   {
/*  126 */     if (!b) throw new XPathException("c-general-xpath");
/*      */   }
/*      */ 
/*      */   private LocationPath buildLocationPath(Vector stepsVector)
/*      */     throws XPathException
/*      */   {
/*  135 */     int size = stepsVector.size();
/*  136 */     check(size != 0);
/*  137 */     Step[] steps = new Step[size];
/*  138 */     stepsVector.copyInto(steps);
/*  139 */     stepsVector.removeAllElements();
/*      */ 
/*  141 */     return new LocationPath(steps);
/*      */   }
/*      */ 
/*      */   private void parseExpression(NamespaceContext context)
/*      */     throws XPathException
/*      */   {
/*  152 */     Tokens xtokens = new Tokens(this.fSymbolTable);
/*      */ 
/*  155 */     Scanner scanner = new Scanner(this.fSymbolTable)
/*      */     {
/*      */       protected void addToken(XPath.Tokens tokens, int token) throws XPathException {
/*  158 */         if ((token == 6) || (token == 35) || (token == 11) || (token == 21) || (token == 4) || (token == 9) || (token == 10) || (token == 22) || (token == 23) || (token == 36) || (token == 8))
/*      */         {
/*  171 */           super.addToken(tokens, token);
/*  172 */           return;
/*      */         }
/*  174 */         throw new XPathException("c-general-xpath");
/*      */       }
/*      */     };
/*  178 */     int length = this.fExpression.length();
/*      */ 
/*  180 */     boolean success = scanner.scanExpr(this.fSymbolTable, xtokens, this.fExpression, 0, length);
/*      */ 
/*  182 */     if (!success) {
/*  183 */       throw new XPathException("c-general-xpath");
/*      */     }
/*      */ 
/*  186 */     Vector stepsVector = new Vector();
/*  187 */     Vector locationPathsVector = new Vector();
/*      */ 
/*  195 */     boolean expectingStep = true;
/*  196 */     boolean expectingDoubleColon = false;
/*      */ 
/*  198 */     while (xtokens.hasMore()) {
/*  199 */       int token = xtokens.nextToken();
/*      */ 
/*  201 */       switch (token) {
/*      */       case 23:
/*  203 */         check(!expectingStep);
/*  204 */         locationPathsVector.addElement(buildLocationPath(stepsVector));
/*  205 */         expectingStep = true;
/*  206 */         break;
/*      */       case 6:
/*  210 */         check(expectingStep);
/*  211 */         Step step = new Step(new Axis((short)2), parseNodeTest(xtokens.nextToken(), xtokens, context));
/*      */ 
/*  214 */         stepsVector.addElement(step);
/*  215 */         expectingStep = false;
/*  216 */         break;
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*  221 */         check(expectingStep);
/*  222 */         Step step = new Step(new Axis((short)1), parseNodeTest(token, xtokens, context));
/*      */ 
/*  225 */         stepsVector.addElement(step);
/*  226 */         expectingStep = false;
/*  227 */         break;
/*      */       case 4:
/*  231 */         check(expectingStep);
/*  232 */         expectingStep = false;
/*      */ 
/*  239 */         if (stepsVector.size() == 0)
/*      */         {
/*  241 */           Axis axis = new Axis((short)3);
/*  242 */           NodeTest nodeTest = new NodeTest((short)3);
/*  243 */           Step step = new Step(axis, nodeTest);
/*  244 */           stepsVector.addElement(step);
/*      */ 
/*  246 */           if ((xtokens.hasMore()) && (xtokens.peekToken() == 22))
/*      */           {
/*  249 */             xtokens.nextToken();
/*      */ 
/*  252 */             axis = new Axis((short)4);
/*  253 */             nodeTest = new NodeTest((short)3);
/*  254 */             step = new Step(axis, nodeTest);
/*  255 */             stepsVector.addElement(step);
/*  256 */             expectingStep = true;
/*      */           }
/*      */         }
/*  258 */         break;
/*      */       case 22:
/*  266 */         throw new XPathException("c-general-xpath");
/*      */       case 21:
/*  269 */         check(!expectingStep);
/*  270 */         expectingStep = true;
/*  271 */         break;
/*      */       case 35:
/*  274 */         check(expectingStep);
/*  275 */         expectingDoubleColon = true;
/*      */ 
/*  277 */         if (xtokens.nextToken() == 8) {
/*  278 */           Step step = new Step(new Axis((short)2), parseNodeTest(xtokens.nextToken(), xtokens, context));
/*      */ 
/*  281 */           stepsVector.addElement(step);
/*  282 */           expectingStep = false;
/*  283 */           expectingDoubleColon = false;
/*  284 */         }break;
/*      */       case 36:
/*  288 */         check(expectingStep);
/*  289 */         expectingDoubleColon = true;
/*  290 */         break;
/*      */       case 8:
/*  293 */         check(expectingStep);
/*  294 */         check(expectingDoubleColon);
/*  295 */         expectingDoubleColon = false;
/*  296 */         break;
/*      */       case 5:
/*      */       case 7:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       default:
/*  300 */         throw new XPathException("c-general-xpath");
/*      */       }
/*      */     }
/*      */ 
/*  304 */     check(!expectingStep);
/*      */ 
/*  306 */     locationPathsVector.addElement(buildLocationPath(stepsVector));
/*      */ 
/*  309 */     this.fLocationPaths = new LocationPath[locationPathsVector.size()];
/*  310 */     locationPathsVector.copyInto(this.fLocationPaths);
/*      */   }
/*      */ 
/*      */   private NodeTest parseNodeTest(int typeToken, Tokens xtokens, NamespaceContext context)
/*      */     throws XPathException
/*      */   {
/*  325 */     switch (typeToken) {
/*      */     case 9:
/*  327 */       return new NodeTest((short)2);
/*      */     case 10:
/*      */     case 11:
/*  332 */       String prefix = xtokens.nextTokenAsString();
/*  333 */       String uri = null;
/*  334 */       if ((context != null) && (prefix != XMLSymbols.EMPTY_STRING)) {
/*  335 */         uri = context.getURI(prefix);
/*      */       }
/*  337 */       if ((prefix != XMLSymbols.EMPTY_STRING) && (context != null) && (uri == null)) {
/*  338 */         throw new XPathException("c-general-xpath-ns");
/*      */       }
/*      */ 
/*  341 */       if (typeToken == 10) {
/*  342 */         return new NodeTest(prefix, uri);
/*      */       }
/*  344 */       String localpart = xtokens.nextTokenAsString();
/*  345 */       String rawname = prefix != XMLSymbols.EMPTY_STRING ? this.fSymbolTable.addSymbol(prefix + ':' + localpart) : localpart;
/*      */ 
/*  349 */       return new NodeTest(new QName(prefix, localpart, rawname, uri));
/*      */     }
/*      */ 
/*  353 */     throw new XPathException("c-general-xpath");
/*      */   }
/*      */ 
/*      */   public static void main(String[] argv)
/*      */     throws Exception
/*      */   {
/* 2005 */     for (int i = 0; i < argv.length; i++) {
/* 2006 */       String expression = argv[i];
/* 2007 */       System.out.println("# XPath expression: \"" + expression + '"');
/*      */       try {
/* 2009 */         SymbolTable symbolTable = new SymbolTable();
/* 2010 */         XPath xpath = new XPath(expression, symbolTable, null);
/* 2011 */         System.out.println("expanded xpath: \"" + xpath.toString() + '"');
/*      */       }
/*      */       catch (XPathException e) {
/* 2014 */         System.out.println("error: " + e.getMessage());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Axis
/*      */     implements Cloneable
/*      */   {
/*      */     public static final short CHILD = 1;
/*      */     public static final short ATTRIBUTE = 2;
/*      */     public static final short SELF = 3;
/*      */     public static final short DESCENDANT = 4;
/*      */     public short type;
/*      */ 
/*      */     public Axis(short type)
/*      */     {
/*  535 */       this.type = type;
/*      */     }
/*      */ 
/*      */     protected Axis(Axis axis)
/*      */     {
/*  540 */       this.type = axis.type;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  549 */       switch (this.type) { case 1:
/*  550 */         return "child";
/*      */       case 2:
/*  551 */         return "attribute";
/*      */       case 3:
/*  552 */         return "self";
/*      */       case 4:
/*  553 */         return "descendant";
/*      */       }
/*  555 */       return "???";
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*  560 */       return new Axis(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class LocationPath
/*      */     implements Cloneable
/*      */   {
/*      */     public XPath.Step[] steps;
/*      */ 
/*      */     public LocationPath(XPath.Step[] steps)
/*      */     {
/*  388 */       this.steps = steps;
/*      */     }
/*      */ 
/*      */     protected LocationPath(LocationPath path)
/*      */     {
/*  393 */       this.steps = new XPath.Step[path.steps.length];
/*  394 */       for (int i = 0; i < this.steps.length; i++)
/*  395 */         this.steps[i] = ((XPath.Step)path.steps[i].clone());
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  405 */       StringBuffer str = new StringBuffer();
/*  406 */       for (int i = 0; i < this.steps.length; i++) {
/*  407 */         if ((i > 0) && (this.steps[(i - 1)].axis.type != 4) && (this.steps[i].axis.type != 4))
/*      */         {
/*  409 */           str.append('/');
/*      */         }
/*  411 */         str.append(this.steps[i].toString());
/*      */       }
/*      */ 
/*  422 */       return str.toString();
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*  427 */       return new LocationPath(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class NodeTest
/*      */     implements Cloneable
/*      */   {
/*      */     public static final short QNAME = 1;
/*      */     public static final short WILDCARD = 2;
/*      */     public static final short NODE = 3;
/*      */     public static final short NAMESPACE = 4;
/*      */     public short type;
/*  599 */     public final QName name = new QName();
/*      */ 
/*      */     public NodeTest(short type)
/*      */     {
/*  607 */       this.type = type;
/*      */     }
/*      */ 
/*      */     public NodeTest(QName name)
/*      */     {
/*  612 */       this.type = 1;
/*  613 */       this.name.setValues(name);
/*      */     }
/*      */ 
/*      */     public NodeTest(String prefix, String uri) {
/*  617 */       this.type = 4;
/*  618 */       this.name.setValues(prefix, null, null, uri);
/*      */     }
/*      */ 
/*      */     public NodeTest(NodeTest nodeTest)
/*      */     {
/*  623 */       this.type = nodeTest.type;
/*  624 */       this.name.setValues(nodeTest.name);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  634 */       switch (this.type) {
/*      */       case 1:
/*  636 */         if (this.name.prefix.length() != 0) {
/*  637 */           if (this.name.uri != null) {
/*  638 */             return this.name.prefix + ':' + this.name.localpart;
/*      */           }
/*  640 */           return "{" + this.name.uri + '}' + this.name.prefix + ':' + this.name.localpart;
/*      */         }
/*  642 */         return this.name.localpart;
/*      */       case 4:
/*  645 */         if (this.name.prefix.length() != 0) {
/*  646 */           if (this.name.uri != null) {
/*  647 */             return this.name.prefix + ":*";
/*      */           }
/*  649 */           return "{" + this.name.uri + '}' + this.name.prefix + ":*";
/*      */         }
/*  651 */         return "???:*";
/*      */       case 2:
/*  654 */         return "*";
/*      */       case 3:
/*  657 */         return "node()";
/*      */       }
/*      */ 
/*  660 */       return "???";
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*  666 */       return new NodeTest(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Scanner
/*      */   {
/*      */     private static final byte CHARTYPE_INVALID = 0;
/*      */     private static final byte CHARTYPE_OTHER = 1;
/*      */     private static final byte CHARTYPE_WHITESPACE = 2;
/*      */     private static final byte CHARTYPE_EXCLAMATION = 3;
/*      */     private static final byte CHARTYPE_QUOTE = 4;
/*      */     private static final byte CHARTYPE_DOLLAR = 5;
/*      */     private static final byte CHARTYPE_OPEN_PAREN = 6;
/*      */     private static final byte CHARTYPE_CLOSE_PAREN = 7;
/*      */     private static final byte CHARTYPE_STAR = 8;
/*      */     private static final byte CHARTYPE_PLUS = 9;
/*      */     private static final byte CHARTYPE_COMMA = 10;
/*      */     private static final byte CHARTYPE_MINUS = 11;
/*      */     private static final byte CHARTYPE_PERIOD = 12;
/*      */     private static final byte CHARTYPE_SLASH = 13;
/*      */     private static final byte CHARTYPE_DIGIT = 14;
/*      */     private static final byte CHARTYPE_COLON = 15;
/*      */     private static final byte CHARTYPE_LESS = 16;
/*      */     private static final byte CHARTYPE_EQUAL = 17;
/*      */     private static final byte CHARTYPE_GREATER = 18;
/*      */     private static final byte CHARTYPE_ATSIGN = 19;
/*      */     private static final byte CHARTYPE_LETTER = 20;
/*      */     private static final byte CHARTYPE_OPEN_BRACKET = 21;
/*      */     private static final byte CHARTYPE_CLOSE_BRACKET = 22;
/*      */     private static final byte CHARTYPE_UNDERSCORE = 23;
/*      */     private static final byte CHARTYPE_UNION = 24;
/*      */     private static final byte CHARTYPE_NONASCII = 25;
/* 1263 */     private static final byte[] fASCIICharMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 1, 5, 1, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 1, 16, 17, 18, 1, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 1, 22, 1, 23, 1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 24, 1, 1, 1 };
/*      */     private SymbolTable fSymbolTable;
/* 1287 */     private static final String fAndSymbol = "and".intern();
/* 1288 */     private static final String fOrSymbol = "or".intern();
/* 1289 */     private static final String fModSymbol = "mod".intern();
/* 1290 */     private static final String fDivSymbol = "div".intern();
/*      */ 
/* 1292 */     private static final String fCommentSymbol = "comment".intern();
/* 1293 */     private static final String fTextSymbol = "text".intern();
/* 1294 */     private static final String fPISymbol = "processing-instruction".intern();
/* 1295 */     private static final String fNodeSymbol = "node".intern();
/*      */ 
/* 1297 */     private static final String fAncestorSymbol = "ancestor".intern();
/* 1298 */     private static final String fAncestorOrSelfSymbol = "ancestor-or-self".intern();
/* 1299 */     private static final String fAttributeSymbol = "attribute".intern();
/* 1300 */     private static final String fChildSymbol = "child".intern();
/* 1301 */     private static final String fDescendantSymbol = "descendant".intern();
/* 1302 */     private static final String fDescendantOrSelfSymbol = "descendant-or-self".intern();
/* 1303 */     private static final String fFollowingSymbol = "following".intern();
/* 1304 */     private static final String fFollowingSiblingSymbol = "following-sibling".intern();
/* 1305 */     private static final String fNamespaceSymbol = "namespace".intern();
/* 1306 */     private static final String fParentSymbol = "parent".intern();
/* 1307 */     private static final String fPrecedingSymbol = "preceding".intern();
/* 1308 */     private static final String fPrecedingSiblingSymbol = "preceding-sibling".intern();
/* 1309 */     private static final String fSelfSymbol = "self".intern();
/*      */ 
/*      */     public Scanner(SymbolTable symbolTable)
/*      */     {
/* 1319 */       this.fSymbolTable = symbolTable;
/*      */     }
/*      */ 
/*      */     public boolean scanExpr(SymbolTable symbolTable, XPath.Tokens tokens, String data, int currentOffset, int endOffset)
/*      */       throws XPathException
/*      */     {
/* 1333 */       boolean starIsMultiplyOperator = false;
/*      */ 
/* 1337 */       while (currentOffset != endOffset)
/*      */       {
/* 1340 */         int ch = data.charAt(currentOffset);
/*      */ 
/* 1344 */         while ((ch == 32) || (ch == 10) || (ch == 9) || (ch == 13)) {
/* 1345 */           currentOffset++; if (currentOffset == endOffset) {
/*      */             break;
/*      */           }
/* 1348 */           ch = data.charAt(currentOffset);
/*      */         }
/* 1350 */         if (currentOffset == endOffset)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1358 */         byte chartype = ch >= 128 ? 25 : fASCIICharMap[ch];
/*      */         int nameOffset;
/*      */         String nameHandle;
/*      */         String prefixHandle;
/* 1359 */         switch (chartype) {
/*      */         case 6:
/* 1361 */           addToken(tokens, 0);
/* 1362 */           starIsMultiplyOperator = false;
/* 1363 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1364 */           break;
/*      */         case 7:
/* 1368 */           addToken(tokens, 1);
/* 1369 */           starIsMultiplyOperator = true;
/* 1370 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1371 */           break;
/*      */         case 21:
/* 1375 */           addToken(tokens, 2);
/* 1376 */           starIsMultiplyOperator = false;
/* 1377 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1378 */           break;
/*      */         case 22:
/* 1382 */           addToken(tokens, 3);
/* 1383 */           starIsMultiplyOperator = true;
/* 1384 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1385 */           break;
/*      */         case 12:
/* 1393 */           if (currentOffset + 1 == endOffset) {
/* 1394 */             addToken(tokens, 4);
/* 1395 */             starIsMultiplyOperator = true;
/* 1396 */             currentOffset++;
/*      */           }
/*      */           else {
/* 1399 */             ch = data.charAt(currentOffset + 1);
/* 1400 */             if (ch == 46) {
/* 1401 */               addToken(tokens, 5);
/* 1402 */               starIsMultiplyOperator = true;
/* 1403 */               currentOffset += 2;
/* 1404 */             } else if ((ch >= 48) && (ch <= 57)) {
/* 1405 */               addToken(tokens, 47);
/* 1406 */               starIsMultiplyOperator = true;
/* 1407 */               currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
/* 1408 */             } else if (ch == 47) {
/* 1409 */               addToken(tokens, 4);
/* 1410 */               starIsMultiplyOperator = true;
/* 1411 */               currentOffset++; } else {
/* 1412 */               if (ch == 124) {
/* 1413 */                 addToken(tokens, 4);
/* 1414 */                 starIsMultiplyOperator = true;
/* 1415 */                 currentOffset++;
/* 1416 */                 break;
/* 1417 */               }if ((ch == 32) || (ch == 10) || (ch == 9) || (ch == 13))
/*      */               {
/*      */                 do {
/* 1420 */                   currentOffset++; if (currentOffset == endOffset) {
/*      */                     break;
/*      */                   }
/* 1423 */                   ch = data.charAt(currentOffset);
/* 1424 */                 }while ((ch == 32) || (ch == 10) || (ch == 9) || (ch == 13));
/* 1425 */                 if ((currentOffset == endOffset) || (ch == 124) || (ch == 47)) {
/* 1426 */                   addToken(tokens, 4);
/* 1427 */                   starIsMultiplyOperator = true;
/* 1428 */                   break;
/*      */                 }
/* 1430 */                 throw new XPathException("c-general-xpath");
/*      */               }
/* 1432 */               throw new XPathException("c-general-xpath");
/*      */             }
/* 1434 */             if (currentOffset != endOffset) break; 
/* 1435 */           }break;
/*      */         case 19:
/* 1439 */           addToken(tokens, 6);
/* 1440 */           starIsMultiplyOperator = false;
/* 1441 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1442 */           break;
/*      */         case 10:
/* 1446 */           addToken(tokens, 7);
/* 1447 */           starIsMultiplyOperator = false;
/* 1448 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1449 */           break;
/*      */         case 15:
/* 1453 */           currentOffset++; if (currentOffset == endOffset)
/*      */           {
/* 1455 */             return false;
/*      */           }
/* 1457 */           ch = data.charAt(currentOffset);
/* 1458 */           if (ch != 58)
/*      */           {
/* 1460 */             return false;
/*      */           }
/* 1462 */           addToken(tokens, 8);
/* 1463 */           starIsMultiplyOperator = false;
/* 1464 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1465 */           break;
/*      */         case 13:
/* 1469 */           currentOffset++; if (currentOffset == endOffset) {
/* 1470 */             addToken(tokens, 21);
/* 1471 */             starIsMultiplyOperator = false;
/*      */           }
/*      */           else {
/* 1474 */             ch = data.charAt(currentOffset);
/* 1475 */             if (ch == 47) {
/* 1476 */               addToken(tokens, 22);
/* 1477 */               starIsMultiplyOperator = false;
/* 1478 */               currentOffset++; if (currentOffset != endOffset)
/*      */                 break;
/*      */             }
/*      */             else {
/* 1482 */               addToken(tokens, 21);
/* 1483 */               starIsMultiplyOperator = false;
/*      */             }
/*      */           }
/* 1485 */           break;
/*      */         case 24:
/* 1487 */           addToken(tokens, 23);
/* 1488 */           starIsMultiplyOperator = false;
/* 1489 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1490 */           break;
/*      */         case 9:
/* 1494 */           addToken(tokens, 24);
/* 1495 */           starIsMultiplyOperator = false;
/* 1496 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1497 */           break;
/*      */         case 11:
/* 1501 */           addToken(tokens, 25);
/* 1502 */           starIsMultiplyOperator = false;
/* 1503 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1504 */           break;
/*      */         case 17:
/* 1508 */           addToken(tokens, 26);
/* 1509 */           starIsMultiplyOperator = false;
/* 1510 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1511 */           break;
/*      */         case 3:
/* 1515 */           currentOffset++; if (currentOffset == endOffset)
/*      */           {
/* 1517 */             return false;
/*      */           }
/* 1519 */           ch = data.charAt(currentOffset);
/* 1520 */           if (ch != 61)
/*      */           {
/* 1522 */             return false;
/*      */           }
/* 1524 */           addToken(tokens, 27);
/* 1525 */           starIsMultiplyOperator = false;
/* 1526 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1527 */           break;
/*      */         case 16:
/* 1531 */           currentOffset++; if (currentOffset == endOffset) {
/* 1532 */             addToken(tokens, 28);
/* 1533 */             starIsMultiplyOperator = false;
/*      */           }
/*      */           else {
/* 1536 */             ch = data.charAt(currentOffset);
/* 1537 */             if (ch == 61) {
/* 1538 */               addToken(tokens, 29);
/* 1539 */               starIsMultiplyOperator = false;
/* 1540 */               currentOffset++; if (currentOffset != endOffset)
/*      */                 break;
/*      */             }
/*      */             else {
/* 1544 */               addToken(tokens, 28);
/* 1545 */               starIsMultiplyOperator = false;
/*      */             }
/*      */           }
/* 1547 */           break;
/*      */         case 18:
/* 1549 */           currentOffset++; if (currentOffset == endOffset) {
/* 1550 */             addToken(tokens, 30);
/* 1551 */             starIsMultiplyOperator = false;
/*      */           }
/*      */           else {
/* 1554 */             ch = data.charAt(currentOffset);
/* 1555 */             if (ch == 61) {
/* 1556 */               addToken(tokens, 31);
/* 1557 */               starIsMultiplyOperator = false;
/* 1558 */               currentOffset++; if (currentOffset != endOffset)
/*      */                 break;
/*      */             }
/*      */             else {
/* 1562 */               addToken(tokens, 30);
/* 1563 */               starIsMultiplyOperator = false;
/*      */             }
/*      */           }
/* 1565 */           break;
/*      */         case 4:
/* 1570 */           int qchar = ch;
/* 1571 */           currentOffset++; if (currentOffset == endOffset)
/*      */           {
/* 1573 */             return false;
/*      */           }
/* 1575 */           ch = data.charAt(currentOffset);
/* 1576 */           int litOffset = currentOffset;
/* 1577 */           while (ch != qchar) {
/* 1578 */             currentOffset++; if (currentOffset == endOffset)
/*      */             {
/* 1580 */               return false;
/*      */             }
/* 1582 */             ch = data.charAt(currentOffset);
/*      */           }
/* 1584 */           int litLength = currentOffset - litOffset;
/* 1585 */           addToken(tokens, 46);
/* 1586 */           starIsMultiplyOperator = true;
/* 1587 */           tokens.addToken(symbolTable.addSymbol(data.substring(litOffset, litOffset + litLength)));
/* 1588 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1589 */           break;
/*      */         case 14:
/* 1597 */           addToken(tokens, 47);
/* 1598 */           starIsMultiplyOperator = true;
/* 1599 */           currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
/* 1600 */           break;
/*      */         case 5:
/* 1605 */           currentOffset++; if (currentOffset == endOffset)
/*      */           {
/* 1607 */             return false;
/*      */           }
/* 1609 */           nameOffset = currentOffset;
/* 1610 */           currentOffset = scanNCName(data, endOffset, currentOffset);
/* 1611 */           if (currentOffset == nameOffset)
/*      */           {
/* 1613 */             return false;
/*      */           }
/* 1615 */           if (currentOffset < endOffset) {
/* 1616 */             ch = data.charAt(currentOffset);
/*      */           }
/*      */           else {
/* 1619 */             ch = -1;
/*      */           }
/* 1621 */           nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*      */           String prefixHandle;
/* 1622 */           if (ch != 58) {
/* 1623 */             prefixHandle = XMLSymbols.EMPTY_STRING;
/*      */           } else {
/* 1625 */             prefixHandle = nameHandle;
/* 1626 */             currentOffset++; if (currentOffset == endOffset)
/*      */             {
/* 1628 */               return false;
/*      */             }
/* 1630 */             nameOffset = currentOffset;
/* 1631 */             currentOffset = scanNCName(data, endOffset, currentOffset);
/* 1632 */             if (currentOffset == nameOffset)
/*      */             {
/* 1634 */               return false;
/*      */             }
/* 1636 */             if (currentOffset < endOffset) {
/* 1637 */               ch = data.charAt(currentOffset);
/*      */             }
/*      */             else {
/* 1640 */               ch = -1;
/*      */             }
/* 1642 */             nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*      */           }
/* 1644 */           addToken(tokens, 48);
/* 1645 */           starIsMultiplyOperator = true;
/* 1646 */           tokens.addToken(prefixHandle);
/* 1647 */           tokens.addToken(nameHandle);
/* 1648 */           break;
/*      */         case 8:
/* 1662 */           if (starIsMultiplyOperator) {
/* 1663 */             addToken(tokens, 20);
/* 1664 */             starIsMultiplyOperator = false;
/*      */           } else {
/* 1666 */             addToken(tokens, 9);
/* 1667 */             starIsMultiplyOperator = true;
/*      */           }
/* 1669 */           currentOffset++; if (currentOffset != endOffset) break;
/* 1670 */           break;
/*      */         case 20:
/*      */         case 23:
/*      */         case 25:
/* 1706 */           nameOffset = currentOffset;
/* 1707 */           currentOffset = scanNCName(data, endOffset, currentOffset);
/* 1708 */           if (currentOffset == nameOffset)
/*      */           {
/* 1710 */             return false;
/*      */           }
/* 1712 */           if (currentOffset < endOffset) {
/* 1713 */             ch = data.charAt(currentOffset);
/*      */           }
/*      */           else {
/* 1716 */             ch = -1;
/*      */           }
/* 1718 */           nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/* 1719 */           boolean isNameTestNCName = false;
/* 1720 */           boolean isAxisName = false;
/* 1721 */           prefixHandle = XMLSymbols.EMPTY_STRING;
/* 1722 */           if (ch == 58) {
/* 1723 */             currentOffset++; if (currentOffset == endOffset)
/*      */             {
/* 1725 */               return false;
/*      */             }
/* 1727 */             ch = data.charAt(currentOffset);
/* 1728 */             if (ch == 42) {
/* 1729 */               currentOffset++; if (currentOffset < endOffset) {
/* 1730 */                 ch = data.charAt(currentOffset);
/*      */               }
/* 1732 */               isNameTestNCName = true;
/* 1733 */             } else if (ch == 58) {
/* 1734 */               currentOffset++; if (currentOffset < endOffset) {
/* 1735 */                 ch = data.charAt(currentOffset);
/*      */               }
/* 1737 */               isAxisName = true;
/*      */             } else {
/* 1739 */               prefixHandle = nameHandle;
/* 1740 */               nameOffset = currentOffset;
/* 1741 */               currentOffset = scanNCName(data, endOffset, currentOffset);
/* 1742 */               if (currentOffset == nameOffset)
/*      */               {
/* 1744 */                 return false;
/*      */               }
/* 1746 */               if (currentOffset < endOffset) {
/* 1747 */                 ch = data.charAt(currentOffset);
/*      */               }
/*      */               else {
/* 1750 */                 ch = -1;
/*      */               }
/* 1752 */               nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1758 */           while ((ch == 32) || (ch == 10) || (ch == 9) || (ch == 13)) {
/* 1759 */             currentOffset++; if (currentOffset == endOffset) {
/*      */               break;
/*      */             }
/* 1762 */             ch = data.charAt(currentOffset);
/*      */           }
/*      */ 
/* 1768 */           if (starIsMultiplyOperator) {
/* 1769 */             if (nameHandle == fAndSymbol) {
/* 1770 */               addToken(tokens, 16);
/* 1771 */               starIsMultiplyOperator = false;
/* 1772 */             } else if (nameHandle == fOrSymbol) {
/* 1773 */               addToken(tokens, 17);
/* 1774 */               starIsMultiplyOperator = false;
/* 1775 */             } else if (nameHandle == fModSymbol) {
/* 1776 */               addToken(tokens, 18);
/* 1777 */               starIsMultiplyOperator = false;
/* 1778 */             } else if (nameHandle == fDivSymbol) {
/* 1779 */               addToken(tokens, 19);
/* 1780 */               starIsMultiplyOperator = false;
/*      */             }
/*      */             else {
/* 1783 */               return false;
/*      */             }
/* 1785 */             if (isNameTestNCName)
/*      */             {
/* 1787 */               return false;
/* 1788 */             }if (isAxisName)
/*      */             {
/* 1790 */               return false;
/*      */             }
/*      */ 
/*      */           }
/* 1798 */           else if ((ch == 40) && (!isNameTestNCName) && (!isAxisName)) {
/* 1799 */             if (nameHandle == fCommentSymbol) {
/* 1800 */               addToken(tokens, 12);
/* 1801 */             } else if (nameHandle == fTextSymbol) {
/* 1802 */               addToken(tokens, 13);
/* 1803 */             } else if (nameHandle == fPISymbol) {
/* 1804 */               addToken(tokens, 14);
/* 1805 */             } else if (nameHandle == fNodeSymbol) {
/* 1806 */               addToken(tokens, 15);
/*      */             } else {
/* 1808 */               addToken(tokens, 32);
/* 1809 */               tokens.addToken(prefixHandle);
/* 1810 */               tokens.addToken(nameHandle);
/*      */             }
/* 1812 */             addToken(tokens, 0);
/* 1813 */             starIsMultiplyOperator = false;
/* 1814 */             currentOffset++; if (currentOffset != endOffset)
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*      */           }
/* 1823 */           else if ((isAxisName) || ((ch == 58) && (currentOffset + 1 < endOffset) && (data.charAt(currentOffset + 1) == ':')))
/*      */           {
/* 1826 */             if (nameHandle == fAncestorSymbol)
/* 1827 */               addToken(tokens, 33);
/* 1828 */             else if (nameHandle == fAncestorOrSelfSymbol)
/* 1829 */               addToken(tokens, 34);
/* 1830 */             else if (nameHandle == fAttributeSymbol)
/* 1831 */               addToken(tokens, 35);
/* 1832 */             else if (nameHandle == fChildSymbol)
/* 1833 */               addToken(tokens, 36);
/* 1834 */             else if (nameHandle == fDescendantSymbol)
/* 1835 */               addToken(tokens, 37);
/* 1836 */             else if (nameHandle == fDescendantOrSelfSymbol)
/* 1837 */               addToken(tokens, 38);
/* 1838 */             else if (nameHandle == fFollowingSymbol)
/* 1839 */               addToken(tokens, 39);
/* 1840 */             else if (nameHandle == fFollowingSiblingSymbol)
/* 1841 */               addToken(tokens, 40);
/* 1842 */             else if (nameHandle == fNamespaceSymbol)
/* 1843 */               addToken(tokens, 41);
/* 1844 */             else if (nameHandle == fParentSymbol)
/* 1845 */               addToken(tokens, 42);
/* 1846 */             else if (nameHandle == fPrecedingSymbol)
/* 1847 */               addToken(tokens, 43);
/* 1848 */             else if (nameHandle == fPrecedingSiblingSymbol)
/* 1849 */               addToken(tokens, 44);
/* 1850 */             else if (nameHandle == fSelfSymbol) {
/* 1851 */               addToken(tokens, 45);
/*      */             }
/*      */             else {
/* 1854 */               return false;
/*      */             }
/* 1856 */             if (isNameTestNCName)
/*      */             {
/* 1858 */               return false;
/*      */             }
/* 1860 */             addToken(tokens, 8);
/* 1861 */             starIsMultiplyOperator = false;
/* 1862 */             if (!isAxisName) {
/* 1863 */               currentOffset++;
/* 1864 */               currentOffset++; if (currentOffset != endOffset)
/*      */               {
/*      */                 break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/* 1874 */           else if (isNameTestNCName) {
/* 1875 */             addToken(tokens, 10);
/* 1876 */             starIsMultiplyOperator = true;
/* 1877 */             tokens.addToken(nameHandle);
/*      */           } else {
/* 1879 */             addToken(tokens, 11);
/* 1880 */             starIsMultiplyOperator = true;
/* 1881 */             tokens.addToken(prefixHandle);
/* 1882 */             tokens.addToken(nameHandle);
/*      */           }
/*      */ 
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1890 */       return true;
/*      */     }
/*      */ 
/*      */     int scanNCName(String data, int endOffset, int currentOffset)
/*      */     {
/* 1897 */       int ch = data.charAt(currentOffset);
/* 1898 */       if (ch >= 128) {
/* 1899 */         if (!XMLChar.isNameStart(ch))
/*      */         {
/* 1905 */           return currentOffset;
/*      */         }
/*      */       }
/*      */       else {
/* 1909 */         byte chartype = fASCIICharMap[ch];
/* 1910 */         if ((chartype != 20) && (chartype != 23))
/* 1911 */           return currentOffset;
/*      */       }
/*      */       while (true) {
/* 1914 */         currentOffset++; if (currentOffset >= endOffset) break;
/* 1915 */         ch = data.charAt(currentOffset);
/* 1916 */         if (ch >= 128) {
/* 1917 */           if (!XMLChar.isName(ch))
/*      */           {
/* 1923 */             break;
/*      */           }
/*      */         }
/*      */         else {
/* 1927 */           byte chartype = fASCIICharMap[ch];
/* 1928 */           if ((chartype != 20) && (chartype != 14) && (chartype != 12) && (chartype != 11) && (chartype != 23))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1936 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     private int scanNumber(XPath.Tokens tokens, String data, int endOffset, int currentOffset)
/*      */     {
/* 1943 */       int ch = data.charAt(currentOffset);
/* 1944 */       int whole = 0;
/* 1945 */       int part = 0;
/* 1946 */       while ((ch >= 48) && (ch <= 57)) {
/* 1947 */         whole = whole * 10 + (ch - 48);
/* 1948 */         currentOffset++; if (currentOffset == endOffset) {
/*      */           break;
/*      */         }
/* 1951 */         ch = data.charAt(currentOffset);
/*      */       }
/* 1953 */       if (ch == 46) {
/* 1954 */         currentOffset++; if (currentOffset < endOffset)
/*      */         {
/* 1956 */           ch = data.charAt(currentOffset);
/* 1957 */           while ((ch >= 48) && (ch <= 57)) {
/* 1958 */             part = part * 10 + (ch - 48);
/* 1959 */             currentOffset++; if (currentOffset == endOffset) {
/*      */               break;
/*      */             }
/* 1962 */             ch = data.charAt(currentOffset);
/*      */           }
/* 1964 */           if (part != 0)
/*      */           {
/* 1968 */             throw new RuntimeException("find a solution!");
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1974 */       tokens.addToken(whole);
/* 1975 */       tokens.addToken(part);
/* 1976 */       return currentOffset;
/*      */     }
/*      */ 
/*      */     protected void addToken(XPath.Tokens tokens, int token)
/*      */       throws XPathException
/*      */     {
/* 1993 */       tokens.addToken(token);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Step
/*      */     implements Cloneable
/*      */   {
/*      */     public XPath.Axis axis;
/*      */     public XPath.NodeTest nodeTest;
/*      */ 
/*      */     public Step(XPath.Axis axis, XPath.NodeTest nodeTest)
/*      */     {
/*  458 */       this.axis = axis;
/*  459 */       this.nodeTest = nodeTest;
/*      */     }
/*      */ 
/*      */     protected Step(Step step)
/*      */     {
/*  464 */       this.axis = ((XPath.Axis)step.axis.clone());
/*  465 */       this.nodeTest = ((XPath.NodeTest)step.nodeTest.clone());
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  474 */       if (this.axis.type == 3) {
/*  475 */         return ".";
/*      */       }
/*  477 */       if (this.axis.type == 2) {
/*  478 */         return "@" + this.nodeTest.toString();
/*      */       }
/*  480 */       if (this.axis.type == 1) {
/*  481 */         return this.nodeTest.toString();
/*      */       }
/*  483 */       if (this.axis.type == 4) {
/*  484 */         return "//";
/*      */       }
/*  486 */       return "??? (" + this.axis.type + ')';
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*  491 */       return new Step(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class Tokens
/*      */   {
/*      */     static final boolean DUMP_TOKENS = false;
/*      */     public static final int EXPRTOKEN_OPEN_PAREN = 0;
/*      */     public static final int EXPRTOKEN_CLOSE_PAREN = 1;
/*      */     public static final int EXPRTOKEN_OPEN_BRACKET = 2;
/*      */     public static final int EXPRTOKEN_CLOSE_BRACKET = 3;
/*      */     public static final int EXPRTOKEN_PERIOD = 4;
/*      */     public static final int EXPRTOKEN_DOUBLE_PERIOD = 5;
/*      */     public static final int EXPRTOKEN_ATSIGN = 6;
/*      */     public static final int EXPRTOKEN_COMMA = 7;
/*      */     public static final int EXPRTOKEN_DOUBLE_COLON = 8;
/*      */     public static final int EXPRTOKEN_NAMETEST_ANY = 9;
/*      */     public static final int EXPRTOKEN_NAMETEST_NAMESPACE = 10;
/*      */     public static final int EXPRTOKEN_NAMETEST_QNAME = 11;
/*      */     public static final int EXPRTOKEN_NODETYPE_COMMENT = 12;
/*      */     public static final int EXPRTOKEN_NODETYPE_TEXT = 13;
/*      */     public static final int EXPRTOKEN_NODETYPE_PI = 14;
/*      */     public static final int EXPRTOKEN_NODETYPE_NODE = 15;
/*      */     public static final int EXPRTOKEN_OPERATOR_AND = 16;
/*      */     public static final int EXPRTOKEN_OPERATOR_OR = 17;
/*      */     public static final int EXPRTOKEN_OPERATOR_MOD = 18;
/*      */     public static final int EXPRTOKEN_OPERATOR_DIV = 19;
/*      */     public static final int EXPRTOKEN_OPERATOR_MULT = 20;
/*      */     public static final int EXPRTOKEN_OPERATOR_SLASH = 21;
/*      */     public static final int EXPRTOKEN_OPERATOR_DOUBLE_SLASH = 22;
/*      */     public static final int EXPRTOKEN_OPERATOR_UNION = 23;
/*      */     public static final int EXPRTOKEN_OPERATOR_PLUS = 24;
/*      */     public static final int EXPRTOKEN_OPERATOR_MINUS = 25;
/*      */     public static final int EXPRTOKEN_OPERATOR_EQUAL = 26;
/*      */     public static final int EXPRTOKEN_OPERATOR_NOT_EQUAL = 27;
/*      */     public static final int EXPRTOKEN_OPERATOR_LESS = 28;
/*      */     public static final int EXPRTOKEN_OPERATOR_LESS_EQUAL = 29;
/*      */     public static final int EXPRTOKEN_OPERATOR_GREATER = 30;
/*      */     public static final int EXPRTOKEN_OPERATOR_GREATER_EQUAL = 31;
/*      */     public static final int EXPRTOKEN_FUNCTION_NAME = 32;
/*      */     public static final int EXPRTOKEN_AXISNAME_ANCESTOR = 33;
/*      */     public static final int EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF = 34;
/*      */     public static final int EXPRTOKEN_AXISNAME_ATTRIBUTE = 35;
/*      */     public static final int EXPRTOKEN_AXISNAME_CHILD = 36;
/*      */     public static final int EXPRTOKEN_AXISNAME_DESCENDANT = 37;
/*      */     public static final int EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF = 38;
/*      */     public static final int EXPRTOKEN_AXISNAME_FOLLOWING = 39;
/*      */     public static final int EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING = 40;
/*      */     public static final int EXPRTOKEN_AXISNAME_NAMESPACE = 41;
/*      */     public static final int EXPRTOKEN_AXISNAME_PARENT = 42;
/*      */     public static final int EXPRTOKEN_AXISNAME_PRECEDING = 43;
/*      */     public static final int EXPRTOKEN_AXISNAME_PRECEDING_SIBLING = 44;
/*      */     public static final int EXPRTOKEN_AXISNAME_SELF = 45;
/*      */     public static final int EXPRTOKEN_LITERAL = 46;
/*      */     public static final int EXPRTOKEN_NUMBER = 47;
/*      */     public static final int EXPRTOKEN_VARIABLE_REFERENCE = 48;
/*  799 */     private static final String[] fgTokenNames = { "EXPRTOKEN_OPEN_PAREN", "EXPRTOKEN_CLOSE_PAREN", "EXPRTOKEN_OPEN_BRACKET", "EXPRTOKEN_CLOSE_BRACKET", "EXPRTOKEN_PERIOD", "EXPRTOKEN_DOUBLE_PERIOD", "EXPRTOKEN_ATSIGN", "EXPRTOKEN_COMMA", "EXPRTOKEN_DOUBLE_COLON", "EXPRTOKEN_NAMETEST_ANY", "EXPRTOKEN_NAMETEST_NAMESPACE", "EXPRTOKEN_NAMETEST_QNAME", "EXPRTOKEN_NODETYPE_COMMENT", "EXPRTOKEN_NODETYPE_TEXT", "EXPRTOKEN_NODETYPE_PI", "EXPRTOKEN_NODETYPE_NODE", "EXPRTOKEN_OPERATOR_AND", "EXPRTOKEN_OPERATOR_OR", "EXPRTOKEN_OPERATOR_MOD", "EXPRTOKEN_OPERATOR_DIV", "EXPRTOKEN_OPERATOR_MULT", "EXPRTOKEN_OPERATOR_SLASH", "EXPRTOKEN_OPERATOR_DOUBLE_SLASH", "EXPRTOKEN_OPERATOR_UNION", "EXPRTOKEN_OPERATOR_PLUS", "EXPRTOKEN_OPERATOR_MINUS", "EXPRTOKEN_OPERATOR_EQUAL", "EXPRTOKEN_OPERATOR_NOT_EQUAL", "EXPRTOKEN_OPERATOR_LESS", "EXPRTOKEN_OPERATOR_LESS_EQUAL", "EXPRTOKEN_OPERATOR_GREATER", "EXPRTOKEN_OPERATOR_GREATER_EQUAL", "EXPRTOKEN_FUNCTION_NAME", "EXPRTOKEN_AXISNAME_ANCESTOR", "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF", "EXPRTOKEN_AXISNAME_ATTRIBUTE", "EXPRTOKEN_AXISNAME_CHILD", "EXPRTOKEN_AXISNAME_DESCENDANT", "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF", "EXPRTOKEN_AXISNAME_FOLLOWING", "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING", "EXPRTOKEN_AXISNAME_NAMESPACE", "EXPRTOKEN_AXISNAME_PARENT", "EXPRTOKEN_AXISNAME_PRECEDING", "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING", "EXPRTOKEN_AXISNAME_SELF", "EXPRTOKEN_LITERAL", "EXPRTOKEN_NUMBER", "EXPRTOKEN_VARIABLE_REFERENCE" };
/*      */     private static final int INITIAL_TOKEN_COUNT = 256;
/*  855 */     private int[] fTokens = new int[256];
/*  856 */     private int fTokenCount = 0;
/*      */     private SymbolTable fSymbolTable;
/*  861 */     private Hashtable fSymbolMapping = new Hashtable();
/*      */ 
/*  864 */     private Hashtable fTokenNames = new Hashtable();
/*      */     private int fCurrentTokenIndex;
/*      */ 
/*      */     public Tokens(SymbolTable symbolTable)
/*      */     {
/*  876 */       this.fSymbolTable = symbolTable;
/*  877 */       String[] symbols = { "ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace", "parent", "preceding", "preceding-sibling", "self" };
/*      */ 
/*  884 */       for (int i = 0; i < symbols.length; i++) {
/*  885 */         this.fSymbolMapping.put(this.fSymbolTable.addSymbol(symbols[i]), new Integer(i));
/*      */       }
/*  887 */       this.fTokenNames.put(new Integer(0), "EXPRTOKEN_OPEN_PAREN");
/*  888 */       this.fTokenNames.put(new Integer(1), "EXPRTOKEN_CLOSE_PAREN");
/*  889 */       this.fTokenNames.put(new Integer(2), "EXPRTOKEN_OPEN_BRACKET");
/*  890 */       this.fTokenNames.put(new Integer(3), "EXPRTOKEN_CLOSE_BRACKET");
/*  891 */       this.fTokenNames.put(new Integer(4), "EXPRTOKEN_PERIOD");
/*  892 */       this.fTokenNames.put(new Integer(5), "EXPRTOKEN_DOUBLE_PERIOD");
/*  893 */       this.fTokenNames.put(new Integer(6), "EXPRTOKEN_ATSIGN");
/*  894 */       this.fTokenNames.put(new Integer(7), "EXPRTOKEN_COMMA");
/*  895 */       this.fTokenNames.put(new Integer(8), "EXPRTOKEN_DOUBLE_COLON");
/*  896 */       this.fTokenNames.put(new Integer(9), "EXPRTOKEN_NAMETEST_ANY");
/*  897 */       this.fTokenNames.put(new Integer(10), "EXPRTOKEN_NAMETEST_NAMESPACE");
/*  898 */       this.fTokenNames.put(new Integer(11), "EXPRTOKEN_NAMETEST_QNAME");
/*  899 */       this.fTokenNames.put(new Integer(12), "EXPRTOKEN_NODETYPE_COMMENT");
/*  900 */       this.fTokenNames.put(new Integer(13), "EXPRTOKEN_NODETYPE_TEXT");
/*  901 */       this.fTokenNames.put(new Integer(14), "EXPRTOKEN_NODETYPE_PI");
/*  902 */       this.fTokenNames.put(new Integer(15), "EXPRTOKEN_NODETYPE_NODE");
/*  903 */       this.fTokenNames.put(new Integer(16), "EXPRTOKEN_OPERATOR_AND");
/*  904 */       this.fTokenNames.put(new Integer(17), "EXPRTOKEN_OPERATOR_OR");
/*  905 */       this.fTokenNames.put(new Integer(18), "EXPRTOKEN_OPERATOR_MOD");
/*  906 */       this.fTokenNames.put(new Integer(19), "EXPRTOKEN_OPERATOR_DIV");
/*  907 */       this.fTokenNames.put(new Integer(20), "EXPRTOKEN_OPERATOR_MULT");
/*  908 */       this.fTokenNames.put(new Integer(21), "EXPRTOKEN_OPERATOR_SLASH");
/*  909 */       this.fTokenNames.put(new Integer(22), "EXPRTOKEN_OPERATOR_DOUBLE_SLASH");
/*  910 */       this.fTokenNames.put(new Integer(23), "EXPRTOKEN_OPERATOR_UNION");
/*  911 */       this.fTokenNames.put(new Integer(24), "EXPRTOKEN_OPERATOR_PLUS");
/*  912 */       this.fTokenNames.put(new Integer(25), "EXPRTOKEN_OPERATOR_MINUS");
/*  913 */       this.fTokenNames.put(new Integer(26), "EXPRTOKEN_OPERATOR_EQUAL");
/*  914 */       this.fTokenNames.put(new Integer(27), "EXPRTOKEN_OPERATOR_NOT_EQUAL");
/*  915 */       this.fTokenNames.put(new Integer(28), "EXPRTOKEN_OPERATOR_LESS");
/*  916 */       this.fTokenNames.put(new Integer(29), "EXPRTOKEN_OPERATOR_LESS_EQUAL");
/*  917 */       this.fTokenNames.put(new Integer(30), "EXPRTOKEN_OPERATOR_GREATER");
/*  918 */       this.fTokenNames.put(new Integer(31), "EXPRTOKEN_OPERATOR_GREATER_EQUAL");
/*  919 */       this.fTokenNames.put(new Integer(32), "EXPRTOKEN_FUNCTION_NAME");
/*  920 */       this.fTokenNames.put(new Integer(33), "EXPRTOKEN_AXISNAME_ANCESTOR");
/*  921 */       this.fTokenNames.put(new Integer(34), "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF");
/*  922 */       this.fTokenNames.put(new Integer(35), "EXPRTOKEN_AXISNAME_ATTRIBUTE");
/*  923 */       this.fTokenNames.put(new Integer(36), "EXPRTOKEN_AXISNAME_CHILD");
/*  924 */       this.fTokenNames.put(new Integer(37), "EXPRTOKEN_AXISNAME_DESCENDANT");
/*  925 */       this.fTokenNames.put(new Integer(38), "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF");
/*  926 */       this.fTokenNames.put(new Integer(39), "EXPRTOKEN_AXISNAME_FOLLOWING");
/*  927 */       this.fTokenNames.put(new Integer(40), "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING");
/*  928 */       this.fTokenNames.put(new Integer(41), "EXPRTOKEN_AXISNAME_NAMESPACE");
/*  929 */       this.fTokenNames.put(new Integer(42), "EXPRTOKEN_AXISNAME_PARENT");
/*  930 */       this.fTokenNames.put(new Integer(43), "EXPRTOKEN_AXISNAME_PRECEDING");
/*  931 */       this.fTokenNames.put(new Integer(44), "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING");
/*  932 */       this.fTokenNames.put(new Integer(45), "EXPRTOKEN_AXISNAME_SELF");
/*  933 */       this.fTokenNames.put(new Integer(46), "EXPRTOKEN_LITERAL");
/*  934 */       this.fTokenNames.put(new Integer(47), "EXPRTOKEN_NUMBER");
/*  935 */       this.fTokenNames.put(new Integer(48), "EXPRTOKEN_VARIABLE_REFERENCE");
/*      */     }
/*      */ 
/*      */     public String getTokenString(int token)
/*      */     {
/*  949 */       return (String)this.fTokenNames.get(new Integer(token));
/*      */     }
/*      */ 
/*      */     public void addToken(String tokenStr) {
/*  953 */       Integer tokenInt = (Integer)this.fTokenNames.get(tokenStr);
/*  954 */       if (tokenInt == null) {
/*  955 */         tokenInt = new Integer(this.fTokenNames.size());
/*  956 */         this.fTokenNames.put(tokenInt, tokenStr);
/*      */       }
/*  958 */       addToken(tokenInt.intValue());
/*      */     }
/*      */ 
/*      */     public void addToken(int token) {
/*      */       try {
/*  963 */         this.fTokens[this.fTokenCount] = token;
/*      */       } catch (ArrayIndexOutOfBoundsException ex) {
/*  965 */         int[] oldList = this.fTokens;
/*  966 */         this.fTokens = new int[this.fTokenCount << 1];
/*  967 */         System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
/*  968 */         this.fTokens[this.fTokenCount] = token;
/*      */       }
/*  970 */       this.fTokenCount += 1;
/*      */     }
/*      */ 
/*      */     public void rewind()
/*      */     {
/*  983 */       this.fCurrentTokenIndex = 0;
/*      */     }
/*      */ 
/*      */     public boolean hasMore()
/*      */     {
/*  990 */       return this.fCurrentTokenIndex < this.fTokenCount;
/*      */     }
/*      */ 
/*      */     public int nextToken()
/*      */       throws XPathException
/*      */     {
/* 1000 */       if (this.fCurrentTokenIndex == this.fTokenCount)
/* 1001 */         throw new XPathException("c-general-xpath");
/* 1002 */       return this.fTokens[(this.fCurrentTokenIndex++)];
/*      */     }
/*      */ 
/*      */     public int peekToken()
/*      */       throws XPathException
/*      */     {
/* 1012 */       if (this.fCurrentTokenIndex == this.fTokenCount)
/* 1013 */         throw new XPathException("c-general-xpath");
/* 1014 */       return this.fTokens[this.fCurrentTokenIndex];
/*      */     }
/*      */ 
/*      */     public String nextTokenAsString()
/*      */       throws XPathException
/*      */     {
/* 1024 */       String s = getTokenString(nextToken());
/* 1025 */       if (s == null) throw new XPathException("c-general-xpath");
/* 1026 */       return s;
/*      */     }
/*      */ 
/*      */     public void dumpTokens()
/*      */     {
/* 1031 */       for (int i = 0; i < this.fTokenCount; i++) {
/* 1032 */         switch (this.fTokens[i]) {
/*      */         case 0:
/* 1034 */           System.out.print("<OPEN_PAREN/>");
/* 1035 */           break;
/*      */         case 1:
/* 1037 */           System.out.print("<CLOSE_PAREN/>");
/* 1038 */           break;
/*      */         case 2:
/* 1040 */           System.out.print("<OPEN_BRACKET/>");
/* 1041 */           break;
/*      */         case 3:
/* 1043 */           System.out.print("<CLOSE_BRACKET/>");
/* 1044 */           break;
/*      */         case 4:
/* 1046 */           System.out.print("<PERIOD/>");
/* 1047 */           break;
/*      */         case 5:
/* 1049 */           System.out.print("<DOUBLE_PERIOD/>");
/* 1050 */           break;
/*      */         case 6:
/* 1052 */           System.out.print("<ATSIGN/>");
/* 1053 */           break;
/*      */         case 7:
/* 1055 */           System.out.print("<COMMA/>");
/* 1056 */           break;
/*      */         case 8:
/* 1058 */           System.out.print("<DOUBLE_COLON/>");
/* 1059 */           break;
/*      */         case 9:
/* 1061 */           System.out.print("<NAMETEST_ANY/>");
/* 1062 */           break;
/*      */         case 10:
/* 1064 */           System.out.print("<NAMETEST_NAMESPACE");
/* 1065 */           System.out.print(" prefix=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1066 */           System.out.print("/>");
/* 1067 */           break;
/*      */         case 11:
/* 1069 */           System.out.print("<NAMETEST_QNAME");
/* 1070 */           if (this.fTokens[(++i)] != -1)
/* 1071 */             System.out.print(" prefix=\"" + getTokenString(this.fTokens[i]) + "\"");
/* 1072 */           System.out.print(" localpart=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1073 */           System.out.print("/>");
/* 1074 */           break;
/*      */         case 12:
/* 1076 */           System.out.print("<NODETYPE_COMMENT/>");
/* 1077 */           break;
/*      */         case 13:
/* 1079 */           System.out.print("<NODETYPE_TEXT/>");
/* 1080 */           break;
/*      */         case 14:
/* 1082 */           System.out.print("<NODETYPE_PI/>");
/* 1083 */           break;
/*      */         case 15:
/* 1085 */           System.out.print("<NODETYPE_NODE/>");
/* 1086 */           break;
/*      */         case 16:
/* 1088 */           System.out.print("<OPERATOR_AND/>");
/* 1089 */           break;
/*      */         case 17:
/* 1091 */           System.out.print("<OPERATOR_OR/>");
/* 1092 */           break;
/*      */         case 18:
/* 1094 */           System.out.print("<OPERATOR_MOD/>");
/* 1095 */           break;
/*      */         case 19:
/* 1097 */           System.out.print("<OPERATOR_DIV/>");
/* 1098 */           break;
/*      */         case 20:
/* 1100 */           System.out.print("<OPERATOR_MULT/>");
/* 1101 */           break;
/*      */         case 21:
/* 1103 */           System.out.print("<OPERATOR_SLASH/>");
/* 1104 */           if (i + 1 < this.fTokenCount) {
/* 1105 */             System.out.println();
/* 1106 */             System.out.print("  "); } break;
/*      */         case 22:
/* 1110 */           System.out.print("<OPERATOR_DOUBLE_SLASH/>");
/* 1111 */           break;
/*      */         case 23:
/* 1113 */           System.out.print("<OPERATOR_UNION/>");
/* 1114 */           break;
/*      */         case 24:
/* 1116 */           System.out.print("<OPERATOR_PLUS/>");
/* 1117 */           break;
/*      */         case 25:
/* 1119 */           System.out.print("<OPERATOR_MINUS/>");
/* 1120 */           break;
/*      */         case 26:
/* 1122 */           System.out.print("<OPERATOR_EQUAL/>");
/* 1123 */           break;
/*      */         case 27:
/* 1125 */           System.out.print("<OPERATOR_NOT_EQUAL/>");
/* 1126 */           break;
/*      */         case 28:
/* 1128 */           System.out.print("<OPERATOR_LESS/>");
/* 1129 */           break;
/*      */         case 29:
/* 1131 */           System.out.print("<OPERATOR_LESS_EQUAL/>");
/* 1132 */           break;
/*      */         case 30:
/* 1134 */           System.out.print("<OPERATOR_GREATER/>");
/* 1135 */           break;
/*      */         case 31:
/* 1137 */           System.out.print("<OPERATOR_GREATER_EQUAL/>");
/* 1138 */           break;
/*      */         case 32:
/* 1140 */           System.out.print("<FUNCTION_NAME");
/* 1141 */           if (this.fTokens[(++i)] != -1)
/* 1142 */             System.out.print(" prefix=\"" + getTokenString(this.fTokens[i]) + "\"");
/* 1143 */           System.out.print(" localpart=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1144 */           System.out.print("/>");
/* 1145 */           break;
/*      */         case 33:
/* 1147 */           System.out.print("<AXISNAME_ANCESTOR/>");
/* 1148 */           break;
/*      */         case 34:
/* 1150 */           System.out.print("<AXISNAME_ANCESTOR_OR_SELF/>");
/* 1151 */           break;
/*      */         case 35:
/* 1153 */           System.out.print("<AXISNAME_ATTRIBUTE/>");
/* 1154 */           break;
/*      */         case 36:
/* 1156 */           System.out.print("<AXISNAME_CHILD/>");
/* 1157 */           break;
/*      */         case 37:
/* 1159 */           System.out.print("<AXISNAME_DESCENDANT/>");
/* 1160 */           break;
/*      */         case 38:
/* 1162 */           System.out.print("<AXISNAME_DESCENDANT_OR_SELF/>");
/* 1163 */           break;
/*      */         case 39:
/* 1165 */           System.out.print("<AXISNAME_FOLLOWING/>");
/* 1166 */           break;
/*      */         case 40:
/* 1168 */           System.out.print("<AXISNAME_FOLLOWING_SIBLING/>");
/* 1169 */           break;
/*      */         case 41:
/* 1171 */           System.out.print("<AXISNAME_NAMESPACE/>");
/* 1172 */           break;
/*      */         case 42:
/* 1174 */           System.out.print("<AXISNAME_PARENT/>");
/* 1175 */           break;
/*      */         case 43:
/* 1177 */           System.out.print("<AXISNAME_PRECEDING/>");
/* 1178 */           break;
/*      */         case 44:
/* 1180 */           System.out.print("<AXISNAME_PRECEDING_SIBLING/>");
/* 1181 */           break;
/*      */         case 45:
/* 1183 */           System.out.print("<AXISNAME_SELF/>");
/* 1184 */           break;
/*      */         case 46:
/* 1186 */           System.out.print("<LITERAL");
/* 1187 */           System.out.print(" value=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1188 */           System.out.print("/>");
/* 1189 */           break;
/*      */         case 47:
/* 1191 */           System.out.print("<NUMBER");
/* 1192 */           System.out.print(" whole=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1193 */           System.out.print(" part=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1194 */           System.out.print("/>");
/* 1195 */           break;
/*      */         case 48:
/* 1197 */           System.out.print("<VARIABLE_REFERENCE");
/* 1198 */           if (this.fTokens[(++i)] != -1)
/* 1199 */             System.out.print(" prefix=\"" + getTokenString(this.fTokens[i]) + "\"");
/* 1200 */           System.out.print(" localpart=\"" + getTokenString(this.fTokens[(++i)]) + "\"");
/* 1201 */           System.out.print("/>");
/* 1202 */           break;
/*      */         default:
/* 1204 */           System.out.println("<???/>");
/*      */         }
/*      */       }
/* 1207 */       System.out.println();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.XPath
 * JD-Core Version:    0.6.2
 */