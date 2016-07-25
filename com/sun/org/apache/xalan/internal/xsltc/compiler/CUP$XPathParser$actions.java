/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.java_cup.internal.runtime.Symbol;
/*      */ import com.sun.java_cup.internal.runtime.lr_parser;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class CUP$XPathParser$actions
/*      */ {
/*      */   private final XPathParser parser;
/*      */ 
/*      */   CUP$XPathParser$actions(XPathParser parser)
/*      */   {
/* 1117 */     this.parser = parser;
/*      */   }
/*      */ 
/*      */   public final Symbol CUP$XPathParser$do_action(int CUP$XPathParser$act_num, lr_parser CUP$XPathParser$parser, Stack CUP$XPathParser$stack, int CUP$XPathParser$top)
/*      */     throws Exception
/*      */   {
/*      */     Symbol CUP$XPathParser$result;
/* 1132 */     switch (CUP$XPathParser$act_num)
/*      */     {
/*      */     case 140:
/* 1137 */       QName RESULT = null;
/* 1138 */       RESULT = this.parser.getQNameIgnoreDefaultNs("id");
/* 1139 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1141 */       return CUP$XPathParser$result;
/*      */     case 139:
/* 1146 */       QName RESULT = null;
/* 1147 */       RESULT = this.parser.getQNameIgnoreDefaultNs("self");
/* 1148 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1150 */       return CUP$XPathParser$result;
/*      */     case 138:
/* 1155 */       QName RESULT = null;
/* 1156 */       RESULT = this.parser.getQNameIgnoreDefaultNs("preceding-sibling");
/* 1157 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1159 */       return CUP$XPathParser$result;
/*      */     case 137:
/* 1164 */       QName RESULT = null;
/* 1165 */       RESULT = this.parser.getQNameIgnoreDefaultNs("preceding");
/* 1166 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1168 */       return CUP$XPathParser$result;
/*      */     case 136:
/* 1173 */       QName RESULT = null;
/* 1174 */       RESULT = this.parser.getQNameIgnoreDefaultNs("parent");
/* 1175 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1177 */       return CUP$XPathParser$result;
/*      */     case 135:
/* 1182 */       QName RESULT = null;
/* 1183 */       RESULT = this.parser.getQNameIgnoreDefaultNs("namespace");
/* 1184 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1186 */       return CUP$XPathParser$result;
/*      */     case 134:
/* 1191 */       QName RESULT = null;
/* 1192 */       RESULT = this.parser.getQNameIgnoreDefaultNs("following-sibling");
/* 1193 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1195 */       return CUP$XPathParser$result;
/*      */     case 133:
/* 1200 */       QName RESULT = null;
/* 1201 */       RESULT = this.parser.getQNameIgnoreDefaultNs("following");
/* 1202 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1204 */       return CUP$XPathParser$result;
/*      */     case 132:
/* 1209 */       QName RESULT = null;
/* 1210 */       RESULT = this.parser.getQNameIgnoreDefaultNs("decendant-or-self");
/* 1211 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1213 */       return CUP$XPathParser$result;
/*      */     case 131:
/* 1218 */       QName RESULT = null;
/* 1219 */       RESULT = this.parser.getQNameIgnoreDefaultNs("decendant");
/* 1220 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1222 */       return CUP$XPathParser$result;
/*      */     case 130:
/* 1227 */       QName RESULT = null;
/* 1228 */       RESULT = this.parser.getQNameIgnoreDefaultNs("child");
/* 1229 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1231 */       return CUP$XPathParser$result;
/*      */     case 129:
/* 1236 */       QName RESULT = null;
/* 1237 */       RESULT = this.parser.getQNameIgnoreDefaultNs("attribute");
/* 1238 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1240 */       return CUP$XPathParser$result;
/*      */     case 128:
/* 1245 */       QName RESULT = null;
/* 1246 */       RESULT = this.parser.getQNameIgnoreDefaultNs("ancestor-or-self");
/* 1247 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1249 */       return CUP$XPathParser$result;
/*      */     case 127:
/* 1254 */       QName RESULT = null;
/* 1255 */       RESULT = this.parser.getQNameIgnoreDefaultNs("child");
/* 1256 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1258 */       return CUP$XPathParser$result;
/*      */     case 126:
/* 1263 */       QName RESULT = null;
/* 1264 */       RESULT = this.parser.getQNameIgnoreDefaultNs("key");
/* 1265 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1267 */       return CUP$XPathParser$result;
/*      */     case 125:
/* 1272 */       QName RESULT = null;
/* 1273 */       RESULT = this.parser.getQNameIgnoreDefaultNs("mod");
/* 1274 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1276 */       return CUP$XPathParser$result;
/*      */     case 124:
/* 1281 */       QName RESULT = null;
/* 1282 */       RESULT = this.parser.getQNameIgnoreDefaultNs("div");
/* 1283 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1285 */       return CUP$XPathParser$result;
/*      */     case 123:
/* 1290 */       QName RESULT = null;
/* 1291 */       int qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1292 */       int qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1293 */       String qname = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1294 */       RESULT = this.parser.getQNameIgnoreDefaultNs(qname);
/* 1295 */       CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1297 */       return CUP$XPathParser$result;
/*      */     case 122:
/* 1302 */       Object RESULT = null;
/* 1303 */       int qnleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1304 */       int qnright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1305 */       QName qn = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1306 */       RESULT = qn;
/* 1307 */       CUP$XPathParser$result = new Symbol(26, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1309 */       return CUP$XPathParser$result;
/*      */     case 121:
/* 1314 */       Object RESULT = null;
/* 1315 */       RESULT = null;
/* 1316 */       CUP$XPathParser$result = new Symbol(26, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1318 */       return CUP$XPathParser$result;
/*      */     case 120:
/* 1323 */       Object RESULT = null;
/* 1324 */       RESULT = new Integer(7);
/* 1325 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1327 */       return CUP$XPathParser$result;
/*      */     case 119:
/* 1332 */       Object RESULT = null;
/* 1333 */       int lleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1334 */       int lright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1335 */       String l = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/*      */ 
/* 1337 */       QName name = this.parser.getQNameIgnoreDefaultNs("name");
/* 1338 */       Expression exp = new EqualityExpr(0, new NameCall(name), new LiteralExpr(l));
/*      */ 
/* 1341 */       Vector predicates = new Vector();
/* 1342 */       predicates.addElement(new Predicate(exp));
/* 1343 */       RESULT = new Step(3, 7, predicates);
/*      */ 
/* 1345 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1347 */       return CUP$XPathParser$result;
/*      */     case 118:
/* 1352 */       Object RESULT = null;
/* 1353 */       RESULT = new Integer(8);
/* 1354 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1356 */       return CUP$XPathParser$result;
/*      */     case 117:
/* 1361 */       Object RESULT = null;
/* 1362 */       RESULT = new Integer(3);
/* 1363 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1365 */       return CUP$XPathParser$result;
/*      */     case 116:
/* 1370 */       Object RESULT = null;
/* 1371 */       RESULT = new Integer(-1);
/* 1372 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1374 */       return CUP$XPathParser$result;
/*      */     case 115:
/* 1379 */       Object RESULT = null;
/* 1380 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1381 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1382 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1383 */       RESULT = nt;
/* 1384 */       CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1386 */       return CUP$XPathParser$result;
/*      */     case 114:
/* 1391 */       Expression RESULT = null;
/* 1392 */       int exleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1393 */       int exright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1394 */       Expression ex = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1395 */       RESULT = ex;
/* 1396 */       CUP$XPathParser$result = new Symbol(3, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1398 */       return CUP$XPathParser$result;
/*      */     case 113:
/* 1403 */       QName RESULT = null;
/* 1404 */       int vnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1405 */       int vnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1406 */       QName vname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1408 */       RESULT = vname;
/*      */ 
/* 1410 */       CUP$XPathParser$result = new Symbol(39, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1412 */       return CUP$XPathParser$result;
/*      */     case 112:
/* 1417 */       QName RESULT = null;
/* 1418 */       int fnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1419 */       int fnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1420 */       QName fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1422 */       RESULT = fname;
/*      */ 
/* 1424 */       CUP$XPathParser$result = new Symbol(38, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1426 */       return CUP$XPathParser$result;
/*      */     case 111:
/* 1431 */       Vector RESULT = null;
/* 1432 */       int argleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 1433 */       int argright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 1434 */       Expression arg = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 1435 */       int arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1436 */       int arglright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1437 */       Vector argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1438 */       argl.insertElementAt(arg, 0); RESULT = argl;
/* 1439 */       CUP$XPathParser$result = new Symbol(36, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1441 */       return CUP$XPathParser$result;
/*      */     case 110:
/* 1446 */       Vector RESULT = null;
/* 1447 */       int argleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1448 */       int argright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1449 */       Expression arg = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1451 */       Vector temp = new Vector();
/* 1452 */       temp.addElement(arg);
/* 1453 */       RESULT = temp;
/*      */ 
/* 1455 */       CUP$XPathParser$result = new Symbol(36, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1457 */       return CUP$XPathParser$result;
/*      */     case 109:
/* 1462 */       Expression RESULT = null;
/* 1463 */       int fnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left;
/* 1464 */       int fnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).right;
/* 1465 */       QName fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).value;
/* 1466 */       int arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1467 */       int arglright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1468 */       Vector argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/*      */ 
/* 1470 */       if (fname == this.parser.getQNameIgnoreDefaultNs("concat")) {
/* 1471 */         RESULT = new ConcatCall(fname, argl);
/*      */       }
/* 1473 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("number")) {
/* 1474 */         RESULT = new NumberCall(fname, argl);
/*      */       }
/* 1476 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("document")) {
/* 1477 */         this.parser.setMultiDocument(true);
/* 1478 */         RESULT = new DocumentCall(fname, argl);
/*      */       }
/* 1480 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("string")) {
/* 1481 */         RESULT = new StringCall(fname, argl);
/*      */       }
/* 1483 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("boolean")) {
/* 1484 */         RESULT = new BooleanCall(fname, argl);
/*      */       }
/* 1486 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("name")) {
/* 1487 */         RESULT = new NameCall(fname, argl);
/*      */       }
/* 1489 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("generate-id")) {
/* 1490 */         RESULT = new GenerateIdCall(fname, argl);
/*      */       }
/* 1492 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("not")) {
/* 1493 */         RESULT = new NotCall(fname, argl);
/*      */       }
/* 1495 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("format-number")) {
/* 1496 */         RESULT = new FormatNumberCall(fname, argl);
/*      */       }
/* 1498 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("unparsed-entity-uri")) {
/* 1499 */         RESULT = new UnparsedEntityUriCall(fname, argl);
/*      */       }
/* 1501 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("key")) {
/* 1502 */         RESULT = new KeyCall(fname, argl);
/*      */       }
/* 1504 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("id")) {
/* 1505 */         RESULT = new KeyCall(fname, argl);
/* 1506 */         this.parser.setHasIdCall(true);
/*      */       }
/* 1508 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("ceiling")) {
/* 1509 */         RESULT = new CeilingCall(fname, argl);
/*      */       }
/* 1511 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("round")) {
/* 1512 */         RESULT = new RoundCall(fname, argl);
/*      */       }
/* 1514 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("floor")) {
/* 1515 */         RESULT = new FloorCall(fname, argl);
/*      */       }
/* 1517 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("contains")) {
/* 1518 */         RESULT = new ContainsCall(fname, argl);
/*      */       }
/* 1520 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("string-length")) {
/* 1521 */         RESULT = new StringLengthCall(fname, argl);
/*      */       }
/* 1523 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("starts-with")) {
/* 1524 */         RESULT = new StartsWithCall(fname, argl);
/*      */       }
/* 1526 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("function-available")) {
/* 1527 */         RESULT = new FunctionAvailableCall(fname, argl);
/*      */       }
/* 1529 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("element-available")) {
/* 1530 */         RESULT = new ElementAvailableCall(fname, argl);
/*      */       }
/* 1532 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("local-name")) {
/* 1533 */         RESULT = new LocalNameCall(fname, argl);
/*      */       }
/* 1535 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("lang")) {
/* 1536 */         RESULT = new LangCall(fname, argl);
/*      */       }
/* 1538 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("namespace-uri")) {
/* 1539 */         RESULT = new NamespaceUriCall(fname, argl);
/*      */       }
/* 1541 */       else if (fname == this.parser.getQName("http://xml.apache.org/xalan/xsltc", "xsltc", "cast")) {
/* 1542 */         RESULT = new CastCall(fname, argl);
/*      */       }
/* 1545 */       else if ((fname.getLocalPart().equals("nodeset")) || (fname.getLocalPart().equals("node-set"))) {
/* 1546 */         this.parser.setCallsNodeset(true);
/* 1547 */         RESULT = new FunctionCall(fname, argl);
/*      */       }
/*      */       else {
/* 1550 */         RESULT = new FunctionCall(fname, argl);
/*      */       }
/*      */ 
/* 1553 */       CUP$XPathParser$result = new Symbol(16, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1555 */       return CUP$XPathParser$result;
/*      */     case 108:
/* 1560 */       Expression RESULT = null;
/* 1561 */       int fnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 1562 */       int fnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 1563 */       QName fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/*      */ 
/* 1566 */       if (fname == this.parser.getQNameIgnoreDefaultNs("current")) {
/* 1567 */         RESULT = new CurrentCall(fname);
/*      */       }
/* 1569 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("number")) {
/* 1570 */         RESULT = new NumberCall(fname, XPathParser.EmptyArgs);
/*      */       }
/* 1572 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("string")) {
/* 1573 */         RESULT = new StringCall(fname, XPathParser.EmptyArgs);
/*      */       }
/* 1575 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("concat")) {
/* 1576 */         RESULT = new ConcatCall(fname, XPathParser.EmptyArgs);
/*      */       }
/* 1578 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("true")) {
/* 1579 */         RESULT = new BooleanExpr(true);
/*      */       }
/* 1581 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("false")) {
/* 1582 */         RESULT = new BooleanExpr(false);
/*      */       }
/* 1584 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("name")) {
/* 1585 */         RESULT = new NameCall(fname);
/*      */       }
/* 1587 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("generate-id")) {
/* 1588 */         RESULT = new GenerateIdCall(fname, XPathParser.EmptyArgs);
/*      */       }
/* 1590 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("string-length")) {
/* 1591 */         RESULT = new StringLengthCall(fname, XPathParser.EmptyArgs);
/*      */       }
/* 1593 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("position")) {
/* 1594 */         RESULT = new PositionCall(fname);
/*      */       }
/* 1596 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("last")) {
/* 1597 */         RESULT = new LastCall(fname);
/*      */       }
/* 1599 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("local-name")) {
/* 1600 */         RESULT = new LocalNameCall(fname);
/*      */       }
/* 1602 */       else if (fname == this.parser.getQNameIgnoreDefaultNs("namespace-uri")) {
/* 1603 */         RESULT = new NamespaceUriCall(fname);
/*      */       }
/*      */       else {
/* 1606 */         RESULT = new FunctionCall(fname, XPathParser.EmptyArgs);
/*      */       }
/*      */ 
/* 1609 */       CUP$XPathParser$result = new Symbol(16, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1611 */       return CUP$XPathParser$result;
/*      */     case 107:
/* 1616 */       Expression RESULT = null;
/* 1617 */       int varNameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1618 */       int varNameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1619 */       QName varName = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1623 */       SyntaxTreeNode node = this.parser.lookupName(varName);
/*      */ 
/* 1625 */       if (node != null) {
/* 1626 */         if ((node instanceof Variable)) {
/* 1627 */           RESULT = new VariableRef((Variable)node);
/*      */         }
/* 1629 */         else if ((node instanceof Param)) {
/* 1630 */           RESULT = new ParameterRef((Param)node);
/*      */         }
/*      */         else {
/* 1633 */           RESULT = new UnresolvedRef(varName);
/*      */         }
/*      */       }
/*      */ 
/* 1637 */       if (node == null) {
/* 1638 */         RESULT = new UnresolvedRef(varName);
/*      */       }
/*      */ 
/* 1641 */       CUP$XPathParser$result = new Symbol(15, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1643 */       return CUP$XPathParser$result;
/*      */     case 106:
/* 1648 */       Expression RESULT = null;
/* 1649 */       int fcleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1650 */       int fcright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1651 */       Expression fc = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1652 */       RESULT = fc;
/* 1653 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1655 */       return CUP$XPathParser$result;
/*      */     case 105:
/* 1660 */       Expression RESULT = null;
/* 1661 */       int numleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1662 */       int numright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1663 */       Double num = (Double)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1664 */       RESULT = new RealExpr(num.doubleValue());
/* 1665 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1667 */       return CUP$XPathParser$result;
/*      */     case 104:
/* 1672 */       Expression RESULT = null;
/* 1673 */       int numleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1674 */       int numright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1675 */       Long num = (Long)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1677 */       long value = num.longValue();
/* 1678 */       if ((value < -2147483648L) || (value > 2147483647L)) {
/* 1679 */         RESULT = new RealExpr(value);
/*      */       }
/* 1682 */       else if (num.doubleValue() == 0.0D)
/* 1683 */         RESULT = new RealExpr(num.doubleValue());
/* 1684 */       else if (num.intValue() == 0)
/* 1685 */         RESULT = new IntExpr(num.intValue());
/* 1686 */       else if (num.doubleValue() == 0.0D)
/* 1687 */         RESULT = new RealExpr(num.doubleValue());
/*      */       else {
/* 1689 */         RESULT = new IntExpr(num.intValue());
/*      */       }
/*      */ 
/* 1692 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1694 */       return CUP$XPathParser$result;
/*      */     case 103:
/* 1699 */       Expression RESULT = null;
/* 1700 */       int stringleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1701 */       int stringright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1702 */       String string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1710 */       String namespace = null;
/* 1711 */       int index = string.lastIndexOf(':');
/*      */ 
/* 1713 */       if (index > 0) {
/* 1714 */         String prefix = string.substring(0, index);
/* 1715 */         namespace = this.parser._symbolTable.lookupNamespace(prefix);
/*      */       }
/* 1717 */       RESULT = namespace == null ? new LiteralExpr(string) : new LiteralExpr(string, namespace);
/*      */ 
/* 1720 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1722 */       return CUP$XPathParser$result;
/*      */     case 102:
/* 1727 */       Expression RESULT = null;
/* 1728 */       int exleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1729 */       int exright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1730 */       Expression ex = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1731 */       RESULT = ex;
/* 1732 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1734 */       return CUP$XPathParser$result;
/*      */     case 101:
/* 1739 */       Expression RESULT = null;
/* 1740 */       int vrleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1741 */       int vrright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1742 */       Expression vr = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1743 */       RESULT = vr;
/* 1744 */       CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1746 */       return CUP$XPathParser$result;
/*      */     case 100:
/* 1751 */       Expression RESULT = null;
/* 1752 */       int primaryleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1753 */       int primaryright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1754 */       Expression primary = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1755 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1756 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1757 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1758 */       RESULT = new FilterExpr(primary, pp);
/* 1759 */       CUP$XPathParser$result = new Symbol(6, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1761 */       return CUP$XPathParser$result;
/*      */     case 99:
/* 1766 */       Expression RESULT = null;
/* 1767 */       int primaryleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1768 */       int primaryright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1769 */       Expression primary = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1770 */       RESULT = primary;
/* 1771 */       CUP$XPathParser$result = new Symbol(6, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1773 */       return CUP$XPathParser$result;
/*      */     case 98:
/* 1778 */       Expression RESULT = null;
/* 1779 */       RESULT = new Step(10, -1, null);
/* 1780 */       CUP$XPathParser$result = new Symbol(20, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1782 */       return CUP$XPathParser$result;
/*      */     case 97:
/* 1787 */       Expression RESULT = null;
/* 1788 */       RESULT = new Step(13, -1, null);
/* 1789 */       CUP$XPathParser$result = new Symbol(20, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1791 */       return CUP$XPathParser$result;
/*      */     case 96:
/* 1796 */       Integer RESULT = null;
/* 1797 */       RESULT = new Integer(13);
/* 1798 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1800 */       return CUP$XPathParser$result;
/*      */     case 95:
/* 1805 */       Integer RESULT = null;
/* 1806 */       RESULT = new Integer(12);
/* 1807 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1809 */       return CUP$XPathParser$result;
/*      */     case 94:
/* 1814 */       Integer RESULT = null;
/* 1815 */       RESULT = new Integer(11);
/* 1816 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1818 */       return CUP$XPathParser$result;
/*      */     case 93:
/* 1823 */       Integer RESULT = null;
/* 1824 */       RESULT = new Integer(10);
/* 1825 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1827 */       return CUP$XPathParser$result;
/*      */     case 92:
/* 1832 */       Integer RESULT = null;
/* 1833 */       RESULT = new Integer(9);
/* 1834 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1836 */       return CUP$XPathParser$result;
/*      */     case 91:
/* 1841 */       Integer RESULT = null;
/* 1842 */       RESULT = new Integer(7);
/* 1843 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1845 */       return CUP$XPathParser$result;
/*      */     case 90:
/* 1850 */       Integer RESULT = null;
/* 1851 */       RESULT = new Integer(6);
/* 1852 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1854 */       return CUP$XPathParser$result;
/*      */     case 89:
/* 1859 */       Integer RESULT = null;
/* 1860 */       RESULT = new Integer(5);
/* 1861 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1863 */       return CUP$XPathParser$result;
/*      */     case 88:
/* 1868 */       Integer RESULT = null;
/* 1869 */       RESULT = new Integer(4);
/* 1870 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1872 */       return CUP$XPathParser$result;
/*      */     case 87:
/* 1877 */       Integer RESULT = null;
/* 1878 */       RESULT = new Integer(3);
/* 1879 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1881 */       return CUP$XPathParser$result;
/*      */     case 86:
/* 1886 */       Integer RESULT = null;
/* 1887 */       RESULT = new Integer(2);
/* 1888 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1890 */       return CUP$XPathParser$result;
/*      */     case 85:
/* 1895 */       Integer RESULT = null;
/* 1896 */       RESULT = new Integer(1);
/* 1897 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1899 */       return CUP$XPathParser$result;
/*      */     case 84:
/* 1904 */       Integer RESULT = null;
/* 1905 */       RESULT = new Integer(0);
/* 1906 */       CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1908 */       return CUP$XPathParser$result;
/*      */     case 83:
/* 1913 */       Integer RESULT = null;
/* 1914 */       RESULT = new Integer(2);
/* 1915 */       CUP$XPathParser$result = new Symbol(41, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1917 */       return CUP$XPathParser$result;
/*      */     case 82:
/* 1922 */       Integer RESULT = null;
/* 1923 */       int anleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1924 */       int anright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1925 */       Integer an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1926 */       RESULT = an;
/* 1927 */       CUP$XPathParser$result = new Symbol(41, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1929 */       return CUP$XPathParser$result;
/*      */     case 81:
/* 1934 */       Expression RESULT = null;
/* 1935 */       int abbrevleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1936 */       int abbrevright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1937 */       Expression abbrev = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1938 */       RESULT = abbrev;
/* 1939 */       CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1941 */       return CUP$XPathParser$result;
/*      */     case 80:
/* 1946 */       Expression RESULT = null;
/* 1947 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1948 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1949 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1950 */       int ntestleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1951 */       int ntestright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1952 */       Object ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1953 */       RESULT = new Step(axis.intValue(), this.parser.findNodeType(axis.intValue(), ntest), null);
/*      */ 
/* 1957 */       CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1959 */       return CUP$XPathParser$result;
/*      */     case 79:
/* 1964 */       Expression RESULT = null;
/* 1965 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 1966 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 1967 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 1968 */       int ntestleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1969 */       int ntestright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1970 */       Object ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1971 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1972 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1973 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 1974 */       RESULT = new Step(axis.intValue(), this.parser.findNodeType(axis.intValue(), ntest), pp);
/*      */ 
/* 1978 */       CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 1980 */       return CUP$XPathParser$result;
/*      */     case 78:
/* 1985 */       Expression RESULT = null;
/* 1986 */       int ntestleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 1987 */       int ntestright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 1988 */       Object ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 1989 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 1990 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 1991 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 1993 */       if ((ntest instanceof Step)) {
/* 1994 */         Step step = (Step)ntest;
/* 1995 */         step.addPredicates(pp);
/* 1996 */         RESULT = (Step)ntest;
/*      */       }
/*      */       else {
/* 1999 */         RESULT = new Step(3, this.parser.findNodeType(3, ntest), pp);
/*      */       }
/*      */ 
/* 2003 */       CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2005 */       return CUP$XPathParser$result;
/*      */     case 77:
/* 2010 */       Expression RESULT = null;
/* 2011 */       int ntestleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2012 */       int ntestright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2013 */       Object ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2015 */       if ((ntest instanceof Step)) {
/* 2016 */         RESULT = (Step)ntest;
/*      */       }
/*      */       else {
/* 2019 */         RESULT = new Step(3, this.parser.findNodeType(3, ntest), null);
/*      */       }
/*      */ 
/* 2024 */       CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2026 */       return CUP$XPathParser$result;
/*      */     case 76:
/* 2031 */       Expression RESULT = null;
/* 2032 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2033 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2034 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2040 */       int nodeType = -1;
/* 2041 */       if (((rlp instanceof Step)) && (this.parser.isElementAxis(((Step)rlp).getAxis())))
/*      */       {
/* 2044 */         nodeType = 1;
/*      */       }
/* 2046 */       Step step = new Step(5, nodeType, null);
/* 2047 */       RESULT = new AbsoluteLocationPath(this.parser.insertStep(step, (RelativeLocationPath)rlp));
/*      */ 
/* 2050 */       CUP$XPathParser$result = new Symbol(24, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2052 */       return CUP$XPathParser$result;
/*      */     case 75:
/* 2057 */       Expression RESULT = null;
/* 2058 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2059 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2060 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2061 */       int stepleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2062 */       int stepright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2063 */       Expression step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2065 */       Step right = (Step)step;
/* 2066 */       int axis = right.getAxis();
/* 2067 */       int type = right.getNodeType();
/* 2068 */       Vector predicates = right.getPredicates();
/*      */ 
/* 2070 */       if ((axis == 3) && (type != 2))
/*      */       {
/* 2072 */         if (predicates == null) {
/* 2073 */           right.setAxis(4);
/* 2074 */           if (((rlp instanceof Step)) && (((Step)rlp).isAbbreviatedDot())) {
/* 2075 */             RESULT = right;
/*      */           }
/*      */           else
/*      */           {
/* 2079 */             RelativeLocationPath left = (RelativeLocationPath)rlp;
/* 2080 */             RESULT = new ParentLocationPath(left, right);
/*      */           }
/*      */ 
/*      */         }
/* 2085 */         else if (((rlp instanceof Step)) && (((Step)rlp).isAbbreviatedDot())) {
/* 2086 */           Step left = new Step(5, 1, null);
/*      */ 
/* 2088 */           RESULT = new ParentLocationPath(left, right);
/*      */         }
/*      */         else
/*      */         {
/* 2092 */           RelativeLocationPath left = (RelativeLocationPath)rlp;
/* 2093 */           Step mid = new Step(5, 1, null);
/*      */ 
/* 2095 */           ParentLocationPath ppl = new ParentLocationPath(mid, right);
/* 2096 */           RESULT = new ParentLocationPath(left, ppl);
/*      */         }
/*      */ 
/*      */       }
/* 2100 */       else if ((axis == 2) || (type == 2))
/*      */       {
/* 2102 */         RelativeLocationPath left = (RelativeLocationPath)rlp;
/* 2103 */         Step middle = new Step(5, 1, null);
/*      */ 
/* 2105 */         ParentLocationPath ppl = new ParentLocationPath(middle, right);
/* 2106 */         RESULT = new ParentLocationPath(left, ppl);
/*      */       }
/*      */       else
/*      */       {
/* 2110 */         RelativeLocationPath left = (RelativeLocationPath)rlp;
/* 2111 */         Step middle = new Step(5, -1, null);
/*      */ 
/* 2113 */         ParentLocationPath ppl = new ParentLocationPath(middle, right);
/* 2114 */         RESULT = new ParentLocationPath(left, ppl);
/*      */       }
/*      */ 
/* 2117 */       CUP$XPathParser$result = new Symbol(22, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2119 */       return CUP$XPathParser$result;
/*      */     case 74:
/* 2124 */       Expression RESULT = null;
/* 2125 */       int aalpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2126 */       int aalpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2127 */       Expression aalp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2128 */       RESULT = aalp;
/* 2129 */       CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2131 */       return CUP$XPathParser$result;
/*      */     case 73:
/* 2136 */       Expression RESULT = null;
/* 2137 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2138 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2139 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2140 */       RESULT = new AbsoluteLocationPath(rlp);
/* 2141 */       CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2143 */       return CUP$XPathParser$result;
/*      */     case 72:
/* 2148 */       Expression RESULT = null;
/* 2149 */       RESULT = new AbsoluteLocationPath();
/* 2150 */       CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2152 */       return CUP$XPathParser$result;
/*      */     case 71:
/* 2157 */       Expression RESULT = null;
/* 2158 */       int arlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2159 */       int arlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2160 */       Expression arlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2161 */       RESULT = arlp;
/* 2162 */       CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2164 */       return CUP$XPathParser$result;
/*      */     case 70:
/* 2169 */       Expression RESULT = null;
/* 2170 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2171 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2172 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2173 */       int stepleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2174 */       int stepright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2175 */       Expression step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2177 */       if (((rlp instanceof Step)) && (((Step)rlp).isAbbreviatedDot())) {
/* 2178 */         RESULT = step;
/*      */       }
/* 2180 */       else if (((Step)step).isAbbreviatedDot()) {
/* 2181 */         RESULT = rlp;
/*      */       }
/*      */       else {
/* 2184 */         RESULT = new ParentLocationPath((RelativeLocationPath)rlp, step);
/*      */       }
/*      */ 
/* 2188 */       CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2190 */       return CUP$XPathParser$result;
/*      */     case 69:
/* 2195 */       Expression RESULT = null;
/* 2196 */       int stepleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2197 */       int stepright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2198 */       Expression step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2199 */       RESULT = step;
/* 2200 */       CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2202 */       return CUP$XPathParser$result;
/*      */     case 68:
/* 2207 */       Expression RESULT = null;
/* 2208 */       int alpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2209 */       int alpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2210 */       Expression alp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2211 */       RESULT = alp;
/* 2212 */       CUP$XPathParser$result = new Symbol(4, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2214 */       return CUP$XPathParser$result;
/*      */     case 67:
/* 2219 */       Expression RESULT = null;
/* 2220 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2221 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2222 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2223 */       RESULT = rlp;
/* 2224 */       CUP$XPathParser$result = new Symbol(4, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2226 */       return CUP$XPathParser$result;
/*      */     case 66:
/* 2231 */       Expression RESULT = null;
/* 2232 */       int fexpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2233 */       int fexpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2234 */       Expression fexp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2235 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2236 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2237 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2243 */       int nodeType = -1;
/* 2244 */       if (((rlp instanceof Step)) && (this.parser.isElementAxis(((Step)rlp).getAxis())))
/*      */       {
/* 2247 */         nodeType = 1;
/*      */       }
/* 2249 */       Step step = new Step(5, nodeType, null);
/* 2250 */       FilterParentPath fpp = new FilterParentPath(fexp, step);
/* 2251 */       fpp = new FilterParentPath(fpp, rlp);
/* 2252 */       if (!(fexp instanceof KeyCall)) {
/* 2253 */         fpp.setDescendantAxis();
/*      */       }
/* 2255 */       RESULT = fpp;
/*      */ 
/* 2257 */       CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2259 */       return CUP$XPathParser$result;
/*      */     case 65:
/* 2264 */       Expression RESULT = null;
/* 2265 */       int fexpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2266 */       int fexpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2267 */       Expression fexp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2268 */       int rlpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2269 */       int rlpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2270 */       Expression rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2271 */       RESULT = new FilterParentPath(fexp, rlp);
/* 2272 */       CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2274 */       return CUP$XPathParser$result;
/*      */     case 64:
/* 2279 */       Expression RESULT = null;
/* 2280 */       int fexpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2281 */       int fexpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2282 */       Expression fexp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2283 */       RESULT = fexp;
/* 2284 */       CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2286 */       return CUP$XPathParser$result;
/*      */     case 63:
/* 2291 */       Expression RESULT = null;
/* 2292 */       int lpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2293 */       int lpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2294 */       Expression lp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2295 */       RESULT = lp;
/* 2296 */       CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2298 */       return CUP$XPathParser$result;
/*      */     case 62:
/* 2303 */       Expression RESULT = null;
/* 2304 */       int peleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2305 */       int peright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2306 */       Expression pe = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2307 */       int restleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2308 */       int restright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2309 */       Expression rest = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2310 */       RESULT = new UnionPathExpr(pe, rest);
/* 2311 */       CUP$XPathParser$result = new Symbol(18, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2313 */       return CUP$XPathParser$result;
/*      */     case 61:
/* 2318 */       Expression RESULT = null;
/* 2319 */       int peleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2320 */       int peright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2321 */       Expression pe = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2322 */       RESULT = pe;
/* 2323 */       CUP$XPathParser$result = new Symbol(18, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2325 */       return CUP$XPathParser$result;
/*      */     case 60:
/* 2330 */       Expression RESULT = null;
/* 2331 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2332 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2333 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2334 */       RESULT = new UnaryOpExpr(ue);
/* 2335 */       CUP$XPathParser$result = new Symbol(14, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2337 */       return CUP$XPathParser$result;
/*      */     case 59:
/* 2342 */       Expression RESULT = null;
/* 2343 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2344 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2345 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2346 */       RESULT = ue;
/* 2347 */       CUP$XPathParser$result = new Symbol(14, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2349 */       return CUP$XPathParser$result;
/*      */     case 58:
/* 2354 */       Expression RESULT = null;
/* 2355 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2356 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2357 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2358 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2359 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2360 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2361 */       RESULT = new BinOpExpr(4, me, ue);
/* 2362 */       CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2364 */       return CUP$XPathParser$result;
/*      */     case 57:
/* 2369 */       Expression RESULT = null;
/* 2370 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2371 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2372 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2373 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2374 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2375 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2376 */       RESULT = new BinOpExpr(3, me, ue);
/* 2377 */       CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2379 */       return CUP$XPathParser$result;
/*      */     case 56:
/* 2384 */       Expression RESULT = null;
/* 2385 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2386 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2387 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2388 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2389 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2390 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2391 */       RESULT = new BinOpExpr(2, me, ue);
/* 2392 */       CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2394 */       return CUP$XPathParser$result;
/*      */     case 55:
/* 2399 */       Expression RESULT = null;
/* 2400 */       int ueleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2401 */       int ueright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2402 */       Expression ue = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2403 */       RESULT = ue;
/* 2404 */       CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2406 */       return CUP$XPathParser$result;
/*      */     case 54:
/* 2411 */       Expression RESULT = null;
/* 2412 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2413 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2414 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2415 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2416 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2417 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2418 */       RESULT = new BinOpExpr(1, ae, me);
/* 2419 */       CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2421 */       return CUP$XPathParser$result;
/*      */     case 53:
/* 2426 */       Expression RESULT = null;
/* 2427 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2428 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2429 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2430 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2431 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2432 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2433 */       RESULT = new BinOpExpr(0, ae, me);
/* 2434 */       CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2436 */       return CUP$XPathParser$result;
/*      */     case 52:
/* 2441 */       Expression RESULT = null;
/* 2442 */       int meleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2443 */       int meright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2444 */       Expression me = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2445 */       RESULT = me;
/* 2446 */       CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2448 */       return CUP$XPathParser$result;
/*      */     case 51:
/* 2453 */       Expression RESULT = null;
/* 2454 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2455 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2456 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2457 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2458 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2459 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2460 */       RESULT = new RelationalExpr(4, re, ae);
/* 2461 */       CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2463 */       return CUP$XPathParser$result;
/*      */     case 50:
/* 2468 */       Expression RESULT = null;
/* 2469 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2470 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2471 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2472 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2473 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2474 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2475 */       RESULT = new RelationalExpr(5, re, ae);
/* 2476 */       CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2478 */       return CUP$XPathParser$result;
/*      */     case 49:
/* 2483 */       Expression RESULT = null;
/* 2484 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2485 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2486 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2487 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2488 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2489 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2490 */       RESULT = new RelationalExpr(2, re, ae);
/* 2491 */       CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2493 */       return CUP$XPathParser$result;
/*      */     case 48:
/* 2498 */       Expression RESULT = null;
/* 2499 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2500 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2501 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2502 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2503 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2504 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2505 */       RESULT = new RelationalExpr(3, re, ae);
/* 2506 */       CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2508 */       return CUP$XPathParser$result;
/*      */     case 47:
/* 2513 */       Expression RESULT = null;
/* 2514 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2515 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2516 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2517 */       RESULT = ae;
/* 2518 */       CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2520 */       return CUP$XPathParser$result;
/*      */     case 46:
/* 2525 */       Expression RESULT = null;
/* 2526 */       int eeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2527 */       int eeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2528 */       Expression ee = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2529 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2530 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2531 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2532 */       RESULT = new EqualityExpr(1, ee, re);
/* 2533 */       CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2535 */       return CUP$XPathParser$result;
/*      */     case 45:
/* 2540 */       Expression RESULT = null;
/* 2541 */       int eeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2542 */       int eeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2543 */       Expression ee = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2544 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2545 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2546 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2547 */       RESULT = new EqualityExpr(0, ee, re);
/* 2548 */       CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2550 */       return CUP$XPathParser$result;
/*      */     case 44:
/* 2555 */       Expression RESULT = null;
/* 2556 */       int releft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2557 */       int reright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2558 */       Expression re = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2559 */       RESULT = re;
/* 2560 */       CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2562 */       return CUP$XPathParser$result;
/*      */     case 43:
/* 2567 */       Expression RESULT = null;
/* 2568 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2569 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2570 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2571 */       int eeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2572 */       int eeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2573 */       Expression ee = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2574 */       RESULT = new LogicalExpr(1, ae, ee);
/* 2575 */       CUP$XPathParser$result = new Symbol(9, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2577 */       return CUP$XPathParser$result;
/*      */     case 42:
/* 2582 */       Expression RESULT = null;
/* 2583 */       int eleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2584 */       int eright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2585 */       Expression e = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2586 */       RESULT = e;
/* 2587 */       CUP$XPathParser$result = new Symbol(9, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2589 */       return CUP$XPathParser$result;
/*      */     case 41:
/* 2594 */       Expression RESULT = null;
/* 2595 */       int oeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2596 */       int oeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2597 */       Expression oe = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2598 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2599 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2600 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2601 */       RESULT = new LogicalExpr(0, oe, ae);
/* 2602 */       CUP$XPathParser$result = new Symbol(8, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2604 */       return CUP$XPathParser$result;
/*      */     case 40:
/* 2609 */       Expression RESULT = null;
/* 2610 */       int aeleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2611 */       int aeright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2612 */       Expression ae = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2613 */       RESULT = ae;
/* 2614 */       CUP$XPathParser$result = new Symbol(8, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2616 */       return CUP$XPathParser$result;
/*      */     case 39:
/* 2621 */       Expression RESULT = null;
/* 2622 */       int exleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2623 */       int exright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2624 */       Expression ex = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2625 */       RESULT = ex;
/* 2626 */       CUP$XPathParser$result = new Symbol(2, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2628 */       return CUP$XPathParser$result;
/*      */     case 38:
/* 2633 */       Expression RESULT = null;
/* 2634 */       int eleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2635 */       int eright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2636 */       Expression e = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/*      */ 
/* 2638 */       RESULT = new Predicate(e);
/*      */ 
/* 2640 */       CUP$XPathParser$result = new Symbol(5, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2642 */       return CUP$XPathParser$result;
/*      */     case 37:
/* 2647 */       Vector RESULT = null;
/* 2648 */       int pleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2649 */       int pright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2650 */       Expression p = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2651 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2652 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2653 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2654 */       pp.insertElementAt(p, 0); RESULT = pp;
/* 2655 */       CUP$XPathParser$result = new Symbol(35, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2657 */       return CUP$XPathParser$result;
/*      */     case 36:
/* 2662 */       Vector RESULT = null;
/* 2663 */       int pleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2664 */       int pright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2665 */       Expression p = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2667 */       Vector temp = new Vector();
/* 2668 */       temp.addElement(p);
/* 2669 */       RESULT = temp;
/*      */ 
/* 2671 */       CUP$XPathParser$result = new Symbol(35, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2673 */       return CUP$XPathParser$result;
/*      */     case 35:
/* 2678 */       Integer RESULT = null;
/* 2679 */       RESULT = new Integer(2);
/* 2680 */       CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2682 */       return CUP$XPathParser$result;
/*      */     case 34:
/* 2687 */       Integer RESULT = null;
/* 2688 */       RESULT = new Integer(3);
/* 2689 */       CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2691 */       return CUP$XPathParser$result;
/*      */     case 33:
/* 2696 */       Integer RESULT = null;
/* 2697 */       RESULT = new Integer(2);
/* 2698 */       CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2700 */       return CUP$XPathParser$result;
/*      */     case 32:
/* 2705 */       Object RESULT = null;
/* 2706 */       int qnleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2707 */       int qnright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2708 */       QName qn = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2709 */       RESULT = qn;
/* 2710 */       CUP$XPathParser$result = new Symbol(34, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2712 */       return CUP$XPathParser$result;
/*      */     case 31:
/* 2717 */       Object RESULT = null;
/* 2718 */       RESULT = null;
/* 2719 */       CUP$XPathParser$result = new Symbol(34, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2721 */       return CUP$XPathParser$result;
/*      */     case 30:
/* 2726 */       Object RESULT = null;
/* 2727 */       RESULT = new Integer(7);
/* 2728 */       CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2730 */       return CUP$XPathParser$result;
/*      */     case 29:
/* 2735 */       Object RESULT = null;
/* 2736 */       RESULT = new Integer(8);
/* 2737 */       CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2739 */       return CUP$XPathParser$result;
/*      */     case 28:
/* 2744 */       Object RESULT = null;
/* 2745 */       RESULT = new Integer(3);
/* 2746 */       CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2748 */       return CUP$XPathParser$result;
/*      */     case 27:
/* 2753 */       Object RESULT = null;
/* 2754 */       RESULT = new Integer(-1);
/* 2755 */       CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2757 */       return CUP$XPathParser$result;
/*      */     case 26:
/* 2762 */       Object RESULT = null;
/* 2763 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2764 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2765 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2766 */       RESULT = nt;
/* 2767 */       CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2769 */       return CUP$XPathParser$result;
/*      */     case 25:
/* 2774 */       StepPattern RESULT = null;
/* 2775 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2776 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2777 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2778 */       int pipleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2779 */       int pipright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2780 */       StepPattern pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2781 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2782 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2783 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2786 */       RESULT = (ProcessingInstructionPattern)pip.setPredicates(pp);
/*      */ 
/* 2788 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2790 */       return CUP$XPathParser$result;
/*      */     case 24:
/* 2795 */       StepPattern RESULT = null;
/* 2796 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2797 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2798 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2799 */       int pipleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2800 */       int pipright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2801 */       StepPattern pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2803 */       RESULT = pip;
/*      */ 
/* 2805 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2807 */       return CUP$XPathParser$result;
/*      */     case 23:
/* 2812 */       StepPattern RESULT = null;
/* 2813 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2814 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2815 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2816 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2817 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2818 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2819 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2820 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2821 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2823 */       RESULT = this.parser.createStepPattern(axis.intValue(), nt, pp);
/*      */ 
/* 2825 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2827 */       return CUP$XPathParser$result;
/*      */     case 22:
/* 2832 */       StepPattern RESULT = null;
/* 2833 */       int axisleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2834 */       int axisright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2835 */       Integer axis = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2836 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2837 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2838 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2840 */       RESULT = this.parser.createStepPattern(axis.intValue(), nt, null);
/*      */ 
/* 2842 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2844 */       return CUP$XPathParser$result;
/*      */     case 21:
/* 2849 */       StepPattern RESULT = null;
/* 2850 */       int pipleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2851 */       int pipright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2852 */       StepPattern pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2853 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2854 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2855 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2856 */       RESULT = (ProcessingInstructionPattern)pip.setPredicates(pp);
/* 2857 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2859 */       return CUP$XPathParser$result;
/*      */     case 20:
/* 2864 */       StepPattern RESULT = null;
/* 2865 */       int pipleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2866 */       int pipright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2867 */       StepPattern pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2868 */       RESULT = pip;
/* 2869 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2871 */       return CUP$XPathParser$result;
/*      */     case 19:
/* 2876 */       StepPattern RESULT = null;
/* 2877 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2878 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2879 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2880 */       int ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2881 */       int ppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2882 */       Vector pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2884 */       RESULT = this.parser.createStepPattern(3, nt, pp);
/*      */ 
/* 2886 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2888 */       return CUP$XPathParser$result;
/*      */     case 18:
/* 2893 */       StepPattern RESULT = null;
/* 2894 */       int ntleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2895 */       int ntright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2896 */       Object nt = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/*      */ 
/* 2898 */       RESULT = this.parser.createStepPattern(3, nt, null);
/*      */ 
/* 2900 */       CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2902 */       return CUP$XPathParser$result;
/*      */     case 17:
/* 2907 */       RelativePathPattern RESULT = null;
/* 2908 */       int spleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2909 */       int spright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2910 */       StepPattern sp = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2911 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2912 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2913 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2914 */       RESULT = new AncestorPattern(sp, rpp);
/* 2915 */       CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2917 */       return CUP$XPathParser$result;
/*      */     case 16:
/* 2922 */       RelativePathPattern RESULT = null;
/* 2923 */       int spleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 2924 */       int spright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 2925 */       StepPattern sp = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 2926 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2927 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2928 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2929 */       RESULT = new ParentPattern(sp, rpp);
/* 2930 */       CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2932 */       return CUP$XPathParser$result;
/*      */     case 15:
/* 2937 */       RelativePathPattern RESULT = null;
/* 2938 */       int spleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2939 */       int spright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2940 */       StepPattern sp = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2941 */       RESULT = sp;
/* 2942 */       CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2944 */       return CUP$XPathParser$result;
/*      */     case 14:
/* 2949 */       StepPattern RESULT = null;
/* 2950 */       int lleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2951 */       int lright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2952 */       String l = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2953 */       RESULT = new ProcessingInstructionPattern(l);
/* 2954 */       CUP$XPathParser$result = new Symbol(30, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2956 */       return CUP$XPathParser$result;
/*      */     case 13:
/* 2961 */       IdKeyPattern RESULT = null;
/* 2962 */       int l1left = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left;
/* 2963 */       int l1right = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).right;
/* 2964 */       String l1 = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).value;
/* 2965 */       int l2left = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2966 */       int l2right = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2967 */       String l2 = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2968 */       RESULT = new KeyPattern(l1, l2);
/* 2969 */       CUP$XPathParser$result = new Symbol(27, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 5)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2971 */       return CUP$XPathParser$result;
/*      */     case 12:
/* 2976 */       IdKeyPattern RESULT = null;
/* 2977 */       int lleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 2978 */       int lright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 2979 */       String l = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 2980 */       RESULT = new IdPattern(l);
/* 2981 */       this.parser.setHasIdCall(true);
/*      */ 
/* 2983 */       CUP$XPathParser$result = new Symbol(27, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2985 */       return CUP$XPathParser$result;
/*      */     case 11:
/* 2990 */       Pattern RESULT = null;
/* 2991 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 2992 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 2993 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 2994 */       RESULT = rpp;
/* 2995 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 2997 */       return CUP$XPathParser$result;
/*      */     case 10:
/* 3002 */       Pattern RESULT = null;
/* 3003 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3004 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3005 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3006 */       RESULT = new AncestorPattern(rpp);
/* 3007 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3009 */       return CUP$XPathParser$result;
/*      */     case 9:
/* 3014 */       Pattern RESULT = null;
/* 3015 */       int ikpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 3016 */       int ikpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 3017 */       IdKeyPattern ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 3018 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3019 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3020 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3021 */       RESULT = new AncestorPattern(ikp, rpp);
/* 3022 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3024 */       return CUP$XPathParser$result;
/*      */     case 8:
/* 3029 */       Pattern RESULT = null;
/* 3030 */       int ikpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 3031 */       int ikpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 3032 */       IdKeyPattern ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 3033 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3034 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3035 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3036 */       RESULT = new ParentPattern(ikp, rpp);
/* 3037 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3039 */       return CUP$XPathParser$result;
/*      */     case 7:
/* 3044 */       Pattern RESULT = null;
/* 3045 */       int ikpleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3046 */       int ikpright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3047 */       IdKeyPattern ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3048 */       RESULT = ikp;
/* 3049 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3051 */       return CUP$XPathParser$result;
/*      */     case 6:
/* 3056 */       Pattern RESULT = null;
/* 3057 */       int rppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3058 */       int rppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3059 */       RelativePathPattern rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3060 */       RESULT = new AbsolutePathPattern(rpp);
/* 3061 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3063 */       return CUP$XPathParser$result;
/*      */     case 5:
/* 3068 */       Pattern RESULT = null;
/* 3069 */       RESULT = new AbsolutePathPattern(null);
/* 3070 */       CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3072 */       return CUP$XPathParser$result;
/*      */     case 4:
/* 3077 */       Pattern RESULT = null;
/* 3078 */       int lppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
/* 3079 */       int lppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
/* 3080 */       Pattern lpp = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
/* 3081 */       int pleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3082 */       int pright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3083 */       Pattern p = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3084 */       RESULT = new AlternativePattern(lpp, p);
/* 3085 */       CUP$XPathParser$result = new Symbol(28, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3087 */       return CUP$XPathParser$result;
/*      */     case 3:
/* 3092 */       Pattern RESULT = null;
/* 3093 */       int lppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3094 */       int lppright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3095 */       Pattern lpp = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3096 */       RESULT = lpp;
/* 3097 */       CUP$XPathParser$result = new Symbol(28, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3099 */       return CUP$XPathParser$result;
/*      */     case 2:
/* 3104 */       SyntaxTreeNode RESULT = null;
/* 3105 */       int exprleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3106 */       int exprright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3107 */       Expression expr = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3108 */       RESULT = expr;
/* 3109 */       CUP$XPathParser$result = new Symbol(1, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3111 */       return CUP$XPathParser$result;
/*      */     case 1:
/* 3116 */       SyntaxTreeNode RESULT = null;
/* 3117 */       int patternleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
/* 3118 */       int patternright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
/* 3119 */       Pattern pattern = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
/* 3120 */       RESULT = pattern;
/* 3121 */       CUP$XPathParser$result = new Symbol(1, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3123 */       return CUP$XPathParser$result;
/*      */     case 0:
/* 3128 */       Object RESULT = null;
/* 3129 */       int start_valleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
/* 3130 */       int start_valright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
/* 3131 */       SyntaxTreeNode start_val = (SyntaxTreeNode)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
/* 3132 */       RESULT = start_val;
/* 3133 */       CUP$XPathParser$result = new Symbol(0, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
/*      */ 
/* 3136 */       CUP$XPathParser$parser.done_parsing();
/* 3137 */       return CUP$XPathParser$result;
/*      */     }
/*      */ 
/* 3141 */     throw new Exception("Invalid action number found in internal parse table");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.CUP.XPathParser.actions
 * JD-Core Version:    0.6.2
 */