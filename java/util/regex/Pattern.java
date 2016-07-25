/*      */ package java.util.regex;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.Normalizer.Form;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class Pattern
/*      */   implements Serializable
/*      */ {
/*      */   public static final int UNIX_LINES = 1;
/*      */   public static final int CASE_INSENSITIVE = 2;
/*      */   public static final int COMMENTS = 4;
/*      */   public static final int MULTILINE = 8;
/*      */   public static final int LITERAL = 16;
/*      */   public static final int DOTALL = 32;
/*      */   public static final int UNICODE_CASE = 64;
/*      */   public static final int CANON_EQ = 128;
/*      */   public static final int UNICODE_CHARACTER_CLASS = 256;
/*      */   private static final long serialVersionUID = 5073258162644648461L;
/*      */   private String pattern;
/*      */   private int flags;
/*  940 */   private volatile transient boolean compiled = false;
/*      */   private transient String normalizedPattern;
/*      */   transient Node root;
/*      */   transient Node matchRoot;
/*      */   transient int[] buffer;
/*      */   volatile transient Map<String, Integer> namedGroups;
/*      */   transient GroupHead[] groupNodes;
/*      */   private transient int[] temp;
/*      */   transient int capturingGroupCount;
/*      */   transient int localCount;
/*      */   private transient int cursor;
/*      */   private transient int patternLength;
/*      */   private transient boolean hasSupplementary;
/*      */   static final int MAX_REPS = 2147483647;
/*      */   static final int GREEDY = 0;
/*      */   static final int LAZY = 1;
/*      */   static final int POSSESSIVE = 2;
/*      */   static final int INDEPENDENT = 3;
/* 5005 */   static Node lookbehindEnd = new Node() {
/*      */     boolean match(Matcher paramAnonymousMatcher, int paramAnonymousInt, CharSequence paramAnonymousCharSequence) {
/* 5007 */       return paramAnonymousInt == paramAnonymousMatcher.lookbehindTo;
/*      */     }
/* 5005 */   };
/*      */ 
/* 5446 */   static Node accept = new Node();
/*      */ 
/* 5448 */   static Node lastAccept = new LastNode();
/*      */ 
/*      */   public static Pattern compile(String paramString)
/*      */   {
/* 1022 */     return new Pattern(paramString, 0);
/*      */   }
/*      */ 
/*      */   public static Pattern compile(String paramString, int paramInt)
/*      */   {
/* 1047 */     return new Pattern(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public String pattern()
/*      */   {
/* 1057 */     return this.pattern;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1069 */     return this.pattern;
/*      */   }
/*      */ 
/*      */   public Matcher matcher(CharSequence paramCharSequence)
/*      */   {
/* 1082 */     if (!this.compiled) {
/* 1083 */       synchronized (this) {
/* 1084 */         if (!this.compiled)
/* 1085 */           compile();
/*      */       }
/*      */     }
/* 1088 */     ??? = new Matcher(this, paramCharSequence);
/* 1089 */     return ???;
/*      */   }
/*      */ 
/*      */   public int flags()
/*      */   {
/* 1098 */     return this.flags;
/*      */   }
/*      */ 
/*      */   public static boolean matches(String paramString, CharSequence paramCharSequence)
/*      */   {
/* 1128 */     Pattern localPattern = compile(paramString);
/* 1129 */     Matcher localMatcher = localPattern.matcher(paramCharSequence);
/* 1130 */     return localMatcher.matches();
/*      */   }
/*      */ 
/*      */   public String[] split(CharSequence paramCharSequence, int paramInt)
/*      */   {
/* 1194 */     int i = 0;
/* 1195 */     int j = paramInt > 0 ? 1 : 0;
/* 1196 */     ArrayList localArrayList = new ArrayList();
/* 1197 */     Matcher localMatcher = matcher(paramCharSequence);
/*      */ 
/* 1200 */     while (localMatcher.find())
/*      */     {
/*      */       String str;
/* 1201 */       if ((j == 0) || (localArrayList.size() < paramInt - 1)) {
/* 1202 */         str = paramCharSequence.subSequence(i, localMatcher.start()).toString();
/* 1203 */         localArrayList.add(str);
/* 1204 */         i = localMatcher.end();
/* 1205 */       } else if (localArrayList.size() == paramInt - 1) {
/* 1206 */         str = paramCharSequence.subSequence(i, paramCharSequence.length()).toString();
/*      */ 
/* 1208 */         localArrayList.add(str);
/* 1209 */         i = localMatcher.end();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1214 */     if (i == 0) {
/* 1215 */       return new String[] { paramCharSequence.toString() };
/*      */     }
/*      */ 
/* 1218 */     if ((j == 0) || (localArrayList.size() < paramInt)) {
/* 1219 */       localArrayList.add(paramCharSequence.subSequence(i, paramCharSequence.length()).toString());
/*      */     }
/*      */ 
/* 1222 */     int k = localArrayList.size();
/* 1223 */     if (paramInt == 0)
/* 1224 */       while ((k > 0) && (((String)localArrayList.get(k - 1)).equals("")))
/* 1225 */         k--;
/* 1226 */     String[] arrayOfString = new String[k];
/* 1227 */     return (String[])localArrayList.subList(0, k).toArray(arrayOfString);
/*      */   }
/*      */ 
/*      */   public String[] split(CharSequence paramCharSequence)
/*      */   {
/* 1259 */     return split(paramCharSequence, 0);
/*      */   }
/*      */ 
/*      */   public static String quote(String paramString)
/*      */   {
/* 1277 */     int i = paramString.indexOf("\\E");
/* 1278 */     if (i == -1) {
/* 1279 */       return "\\Q" + paramString + "\\E";
/*      */     }
/* 1281 */     StringBuilder localStringBuilder = new StringBuilder(paramString.length() * 2);
/* 1282 */     localStringBuilder.append("\\Q");
/* 1283 */     i = 0;
/* 1284 */     int j = 0;
/* 1285 */     while ((i = paramString.indexOf("\\E", j)) != -1) {
/* 1286 */       localStringBuilder.append(paramString.substring(j, i));
/* 1287 */       j = i + 2;
/* 1288 */       localStringBuilder.append("\\E\\\\E\\Q");
/*      */     }
/* 1290 */     localStringBuilder.append(paramString.substring(j, paramString.length()));
/* 1291 */     localStringBuilder.append("\\E");
/* 1292 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1303 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1306 */     this.capturingGroupCount = 1;
/* 1307 */     this.localCount = 0;
/*      */ 
/* 1310 */     this.compiled = false;
/* 1311 */     if (this.pattern.length() == 0) {
/* 1312 */       this.root = new Start(lastAccept);
/* 1313 */       this.matchRoot = lastAccept;
/* 1314 */       this.compiled = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private Pattern(String paramString, int paramInt)
/*      */   {
/* 1325 */     this.pattern = paramString;
/* 1326 */     this.flags = paramInt;
/*      */ 
/* 1329 */     if ((this.flags & 0x100) != 0) {
/* 1330 */       this.flags |= 64;
/*      */     }
/*      */ 
/* 1333 */     this.capturingGroupCount = 1;
/* 1334 */     this.localCount = 0;
/*      */ 
/* 1336 */     if (this.pattern.length() > 0) {
/* 1337 */       compile();
/*      */     } else {
/* 1339 */       this.root = new Start(lastAccept);
/* 1340 */       this.matchRoot = lastAccept;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void normalize()
/*      */   {
/* 1349 */     int i = 0;
/* 1350 */     int j = -1;
/*      */ 
/* 1353 */     this.normalizedPattern = java.text.Normalizer.normalize(this.pattern, Normalizer.Form.NFD);
/* 1354 */     this.patternLength = this.normalizedPattern.length();
/*      */ 
/* 1357 */     StringBuilder localStringBuilder1 = new StringBuilder(this.patternLength);
/* 1358 */     for (int k = 0; k < this.patternLength; ) {
/* 1359 */       int m = this.normalizedPattern.codePointAt(k);
/*      */ 
/* 1361 */       if ((Character.getType(m) == 6) && (j != -1))
/*      */       {
/* 1363 */         StringBuilder localStringBuilder2 = new StringBuilder();
/* 1364 */         localStringBuilder2.appendCodePoint(j);
/* 1365 */         localStringBuilder2.appendCodePoint(m);
/* 1366 */         while (Character.getType(m) == 6) {
/* 1367 */           k += Character.charCount(m);
/* 1368 */           if (k >= this.patternLength)
/*      */             break;
/* 1370 */           m = this.normalizedPattern.codePointAt(k);
/* 1371 */           localStringBuilder2.appendCodePoint(m);
/*      */         }
/* 1373 */         String str = produceEquivalentAlternation(localStringBuilder2.toString());
/*      */ 
/* 1375 */         localStringBuilder1.setLength(localStringBuilder1.length() - Character.charCount(j));
/* 1376 */         localStringBuilder1.append("(?:").append(str).append(")");
/* 1377 */       } else if ((m == 91) && (j != 92)) {
/* 1378 */         k = normalizeCharClass(localStringBuilder1, k);
/*      */       } else {
/* 1380 */         localStringBuilder1.appendCodePoint(m);
/*      */       }
/* 1382 */       j = m;
/* 1383 */       k += Character.charCount(m);
/*      */     }
/* 1385 */     this.normalizedPattern = localStringBuilder1.toString();
/*      */   }
/*      */ 
/*      */   private int normalizeCharClass(StringBuilder paramStringBuilder, int paramInt)
/*      */   {
/* 1394 */     StringBuilder localStringBuilder1 = new StringBuilder();
/* 1395 */     StringBuilder localStringBuilder2 = null;
/* 1396 */     int i = -1;
/*      */ 
/* 1399 */     paramInt++;
/* 1400 */     localStringBuilder1.append("[");
/*      */     while (true) {
/* 1402 */       int j = this.normalizedPattern.codePointAt(paramInt);
/*      */ 
/* 1405 */       if ((j == 93) && (i != 92)) {
/* 1406 */         localStringBuilder1.append((char)j);
/* 1407 */         break;
/* 1408 */       }if (Character.getType(j) == 6) {
/* 1409 */         StringBuilder localStringBuilder3 = new StringBuilder();
/* 1410 */         localStringBuilder3.appendCodePoint(i);
/* 1411 */         while (Character.getType(j) == 6) {
/* 1412 */           localStringBuilder3.appendCodePoint(j);
/* 1413 */           paramInt += Character.charCount(j);
/* 1414 */           if (paramInt >= this.normalizedPattern.length())
/*      */             break;
/* 1416 */           j = this.normalizedPattern.codePointAt(paramInt);
/*      */         }
/* 1418 */         String str2 = produceEquivalentAlternation(localStringBuilder3.toString());
/*      */ 
/* 1421 */         localStringBuilder1.setLength(localStringBuilder1.length() - Character.charCount(i));
/* 1422 */         if (localStringBuilder2 == null)
/* 1423 */           localStringBuilder2 = new StringBuilder();
/* 1424 */         localStringBuilder2.append('|');
/* 1425 */         localStringBuilder2.append(str2);
/*      */       } else {
/* 1427 */         localStringBuilder1.appendCodePoint(j);
/* 1428 */         paramInt++;
/*      */       }
/* 1430 */       if (paramInt == this.normalizedPattern.length())
/* 1431 */         throw error("Unclosed character class");
/* 1432 */       i = j;
/*      */     }
/*      */     String str1;
/* 1435 */     if (localStringBuilder2 != null)
/* 1436 */       str1 = "(?:" + localStringBuilder1.toString() + localStringBuilder2.toString() + ")";
/*      */     else {
/* 1438 */       str1 = localStringBuilder1.toString();
/*      */     }
/*      */ 
/* 1441 */     paramStringBuilder.append(str1);
/* 1442 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private String produceEquivalentAlternation(String paramString)
/*      */   {
/* 1451 */     int i = countChars(paramString, 0, 1);
/* 1452 */     if (paramString.length() == i)
/*      */     {
/* 1454 */       return paramString;
/*      */     }
/* 1456 */     String str1 = paramString.substring(0, i);
/* 1457 */     String str2 = paramString.substring(i);
/*      */ 
/* 1459 */     String[] arrayOfString = producePermutations(str2);
/* 1460 */     StringBuilder localStringBuilder = new StringBuilder(paramString);
/*      */ 
/* 1463 */     for (int j = 0; j < arrayOfString.length; j++) {
/* 1464 */       String str3 = str1 + arrayOfString[j];
/* 1465 */       if (j > 0)
/* 1466 */         localStringBuilder.append("|" + str3);
/* 1467 */       str3 = composeOneStep(str3);
/* 1468 */       if (str3 != null)
/* 1469 */         localStringBuilder.append("|" + produceEquivalentAlternation(str3));
/*      */     }
/* 1471 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private String[] producePermutations(String paramString)
/*      */   {
/* 1484 */     if (paramString.length() == countChars(paramString, 0, 1)) {
/* 1485 */       return new String[] { paramString };
/*      */     }
/* 1487 */     if (paramString.length() == countChars(paramString, 0, 2)) {
/* 1488 */       i = Character.codePointAt(paramString, 0);
/* 1489 */       j = Character.codePointAt(paramString, Character.charCount(i));
/* 1490 */       if (getClass(j) == getClass(i)) {
/* 1491 */         return new String[] { paramString };
/*      */       }
/* 1493 */       String[] arrayOfString1 = new String[2];
/* 1494 */       arrayOfString1[0] = paramString;
/* 1495 */       localObject = new StringBuilder(2);
/* 1496 */       ((StringBuilder)localObject).appendCodePoint(j);
/* 1497 */       ((StringBuilder)localObject).appendCodePoint(i);
/* 1498 */       arrayOfString1[1] = ((StringBuilder)localObject).toString();
/* 1499 */       return arrayOfString1;
/*      */     }
/*      */ 
/* 1502 */     int i = 1;
/* 1503 */     int j = countCodePoints(paramString);
/* 1504 */     for (int k = 1; k < j; k++) {
/* 1505 */       i *= (k + 1);
/*      */     }
/* 1507 */     String[] arrayOfString2 = new String[i];
/*      */ 
/* 1509 */     Object localObject = new int[j];
/* 1510 */     int m = 0; for (int n = 0; m < j; m++) {
/* 1511 */       i1 = Character.codePointAt(paramString, n);
/* 1512 */       localObject[m] = getClass(i1);
/* 1513 */       n += Character.charCount(i1);
/*      */     }
/*      */ 
/* 1518 */     m = 0;
/*      */ 
/* 1521 */     int i1 = 0; for (int i2 = 0; i1 < j; i2 += n) {
/* 1522 */       n = countChars(paramString, i2, 1);
/* 1523 */       int i3 = 0;
/* 1524 */       for (int i4 = i1 - 1; i4 >= 0; i4--) {
/* 1525 */         if (localObject[i4] == localObject[i1]) {
/*      */           break label362;
/*      */         }
/*      */       }
/* 1529 */       StringBuilder localStringBuilder = new StringBuilder(paramString);
/* 1530 */       String str1 = localStringBuilder.delete(i2, i2 + n).toString();
/* 1531 */       String[] arrayOfString4 = producePermutations(str1);
/*      */ 
/* 1533 */       String str2 = paramString.substring(i2, i2 + n);
/* 1534 */       for (int i5 = 0; i5 < arrayOfString4.length; i5++)
/* 1535 */         arrayOfString2[(m++)] = (str2 + arrayOfString4[i5]);
/* 1521 */       label362: i1++;
/*      */     }
/*      */ 
/* 1537 */     String[] arrayOfString3 = new String[m];
/* 1538 */     for (i2 = 0; i2 < m; i2++)
/* 1539 */       arrayOfString3[i2] = arrayOfString2[i2];
/* 1540 */     return arrayOfString3;
/*      */   }
/*      */ 
/*      */   private int getClass(int paramInt) {
/* 1544 */     return sun.text.Normalizer.getCombiningClass(paramInt);
/*      */   }
/*      */ 
/*      */   private String composeOneStep(String paramString)
/*      */   {
/* 1555 */     int i = countChars(paramString, 0, 2);
/* 1556 */     String str1 = paramString.substring(0, i);
/* 1557 */     String str2 = java.text.Normalizer.normalize(str1, Normalizer.Form.NFC);
/*      */ 
/* 1559 */     if (str2.equals(str1)) {
/* 1560 */       return null;
/*      */     }
/* 1562 */     String str3 = paramString.substring(i);
/* 1563 */     return str2 + str3;
/*      */   }
/*      */ 
/*      */   private void RemoveQEQuoting()
/*      */   {
/* 1572 */     int i = this.patternLength;
/* 1573 */     int j = 0;
/* 1574 */     while (j < i - 1) {
/* 1575 */       if (this.temp[j] != 92) {
/* 1576 */         j++; } else {
/* 1577 */         if (this.temp[(j + 1)] == 81) break;
/* 1578 */         j += 2;
/*      */       }
/*      */     }
/*      */ 
/* 1582 */     if (j >= i - 1)
/* 1583 */       return;
/* 1584 */     int k = j;
/* 1585 */     j += 2;
/* 1586 */     int[] arrayOfInt = new int[k + 2 * (i - j) + 2];
/* 1587 */     System.arraycopy(this.temp, 0, arrayOfInt, 0, k);
/*      */ 
/* 1589 */     int m = 1;
/* 1590 */     while (j < i) {
/* 1591 */       int n = this.temp[(j++)];
/* 1592 */       if ((!ASCII.isAscii(n)) || (ASCII.isAlnum(n))) {
/* 1593 */         arrayOfInt[(k++)] = n;
/* 1594 */       } else if (n != 92) {
/* 1595 */         if (m != 0) arrayOfInt[(k++)] = 92;
/* 1596 */         arrayOfInt[(k++)] = n;
/* 1597 */       } else if (m != 0) {
/* 1598 */         if (this.temp[j] == 69) {
/* 1599 */           j++;
/* 1600 */           m = 0;
/*      */         } else {
/* 1602 */           arrayOfInt[(k++)] = 92;
/* 1603 */           arrayOfInt[(k++)] = 92;
/*      */         }
/*      */       }
/* 1606 */       else if (this.temp[j] == 81) {
/* 1607 */         j++;
/* 1608 */         m = 1;
/*      */       } else {
/* 1610 */         arrayOfInt[(k++)] = n;
/* 1611 */         if (j != i) {
/* 1612 */           arrayOfInt[(k++)] = this.temp[(j++)];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1617 */     this.patternLength = k;
/* 1618 */     this.temp = Arrays.copyOf(arrayOfInt, k + 2);
/*      */   }
/*      */ 
/*      */   private void compile()
/*      */   {
/* 1627 */     if ((has(128)) && (!has(16)))
/* 1628 */       normalize();
/*      */     else {
/* 1630 */       this.normalizedPattern = this.pattern;
/*      */     }
/* 1632 */     this.patternLength = this.normalizedPattern.length();
/*      */ 
/* 1636 */     this.temp = new int[this.patternLength + 2];
/*      */ 
/* 1638 */     this.hasSupplementary = false;
/* 1639 */     int j = 0;
/*      */     int i;
/* 1641 */     for (int k = 0; k < this.patternLength; k += Character.charCount(i)) {
/* 1642 */       i = this.normalizedPattern.codePointAt(k);
/* 1643 */       if (isSupplementary(i)) {
/* 1644 */         this.hasSupplementary = true;
/*      */       }
/* 1646 */       this.temp[(j++)] = i;
/*      */     }
/*      */ 
/* 1649 */     this.patternLength = j;
/*      */ 
/* 1651 */     if (!has(16)) {
/* 1652 */       RemoveQEQuoting();
/*      */     }
/*      */ 
/* 1655 */     this.buffer = new int[32];
/* 1656 */     this.groupNodes = new GroupHead[10];
/* 1657 */     this.namedGroups = null;
/*      */ 
/* 1659 */     if (has(16))
/*      */     {
/* 1661 */       this.matchRoot = newSlice(this.temp, this.patternLength, this.hasSupplementary);
/* 1662 */       this.matchRoot.next = lastAccept;
/*      */     }
/*      */     else {
/* 1665 */       this.matchRoot = expr(lastAccept);
/*      */ 
/* 1667 */       if (this.patternLength != this.cursor) {
/* 1668 */         if (peek() == 41) {
/* 1669 */           throw error("Unmatched closing ')'");
/*      */         }
/* 1671 */         throw error("Unexpected internal error");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1677 */     if ((this.matchRoot instanceof Slice)) {
/* 1678 */       this.root = BnM.optimize(this.matchRoot);
/* 1679 */       if (this.root == this.matchRoot)
/* 1680 */         this.root = (this.hasSupplementary ? new StartS(this.matchRoot) : new Start(this.matchRoot));
/*      */     }
/* 1682 */     else if (((this.matchRoot instanceof Begin)) || ((this.matchRoot instanceof First))) {
/* 1683 */       this.root = this.matchRoot;
/*      */     } else {
/* 1685 */       this.root = (this.hasSupplementary ? new StartS(this.matchRoot) : new Start(this.matchRoot));
/*      */     }
/*      */ 
/* 1689 */     this.temp = null;
/* 1690 */     this.buffer = null;
/* 1691 */     this.groupNodes = null;
/* 1692 */     this.patternLength = 0;
/* 1693 */     this.compiled = true;
/*      */   }
/*      */ 
/*      */   Map<String, Integer> namedGroups() {
/* 1697 */     if (this.namedGroups == null)
/* 1698 */       this.namedGroups = new HashMap(2);
/* 1699 */     return this.namedGroups;
/*      */   }
/*      */ 
/*      */   private static void printObjectTree(Node paramNode)
/*      */   {
/* 1706 */     while (paramNode != null) {
/* 1707 */       if ((paramNode instanceof Prolog)) {
/* 1708 */         System.out.println(paramNode);
/* 1709 */         printObjectTree(((Prolog)paramNode).loop);
/* 1710 */         System.out.println("**** end contents prolog loop");
/* 1711 */       } else if ((paramNode instanceof Loop)) {
/* 1712 */         System.out.println(paramNode);
/* 1713 */         printObjectTree(((Loop)paramNode).body);
/* 1714 */         System.out.println("**** end contents Loop body");
/* 1715 */       } else if ((paramNode instanceof Curly)) {
/* 1716 */         System.out.println(paramNode);
/* 1717 */         printObjectTree(((Curly)paramNode).atom);
/* 1718 */         System.out.println("**** end contents Curly body");
/* 1719 */       } else if ((paramNode instanceof GroupCurly)) {
/* 1720 */         System.out.println(paramNode);
/* 1721 */         printObjectTree(((GroupCurly)paramNode).atom);
/* 1722 */         System.out.println("**** end contents GroupCurly body"); } else {
/* 1723 */         if ((paramNode instanceof GroupTail)) {
/* 1724 */           System.out.println(paramNode);
/* 1725 */           System.out.println("Tail next is " + paramNode.next);
/* 1726 */           return;
/*      */         }
/* 1728 */         System.out.println(paramNode);
/*      */       }
/* 1730 */       paramNode = paramNode.next;
/* 1731 */       if (paramNode != null)
/* 1732 */         System.out.println("->next:");
/* 1733 */       if (paramNode == accept) {
/* 1734 */         System.out.println("Accept Node");
/* 1735 */         paramNode = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean has(int paramInt)
/*      */   {
/* 1771 */     return (this.flags & paramInt) != 0;
/*      */   }
/*      */ 
/*      */   private void accept(int paramInt, String paramString)
/*      */   {
/* 1778 */     int i = this.temp[(this.cursor++)];
/* 1779 */     if (has(4))
/* 1780 */       i = parsePastWhitespace(i);
/* 1781 */     if (paramInt != i)
/* 1782 */       throw error(paramString);
/*      */   }
/*      */ 
/*      */   private void mark(int paramInt)
/*      */   {
/* 1790 */     this.temp[this.patternLength] = paramInt;
/*      */   }
/*      */ 
/*      */   private int peek()
/*      */   {
/* 1797 */     int i = this.temp[this.cursor];
/* 1798 */     if (has(4))
/* 1799 */       i = peekPastWhitespace(i);
/* 1800 */     return i;
/*      */   }
/*      */ 
/*      */   private int read()
/*      */   {
/* 1807 */     int i = this.temp[(this.cursor++)];
/* 1808 */     if (has(4))
/* 1809 */       i = parsePastWhitespace(i);
/* 1810 */     return i;
/*      */   }
/*      */ 
/*      */   private int readEscaped()
/*      */   {
/* 1818 */     int i = this.temp[(this.cursor++)];
/* 1819 */     return i;
/*      */   }
/*      */ 
/*      */   private int next()
/*      */   {
/* 1826 */     int i = this.temp[(++this.cursor)];
/* 1827 */     if (has(4))
/* 1828 */       i = peekPastWhitespace(i);
/* 1829 */     return i;
/*      */   }
/*      */ 
/*      */   private int nextEscaped()
/*      */   {
/* 1837 */     int i = this.temp[(++this.cursor)];
/* 1838 */     return i;
/*      */   }
/*      */ 
/*      */   private int peekPastWhitespace(int paramInt)
/*      */   {
/* 1845 */     while ((ASCII.isSpace(paramInt)) || (paramInt == 35)) {
/* 1846 */       while (ASCII.isSpace(paramInt))
/* 1847 */         paramInt = this.temp[(++this.cursor)];
/* 1848 */       if (paramInt == 35) {
/* 1849 */         paramInt = peekPastLine();
/*      */       }
/*      */     }
/* 1852 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int parsePastWhitespace(int paramInt)
/*      */   {
/* 1859 */     while ((ASCII.isSpace(paramInt)) || (paramInt == 35)) {
/* 1860 */       while (ASCII.isSpace(paramInt))
/* 1861 */         paramInt = this.temp[(this.cursor++)];
/* 1862 */       if (paramInt == 35)
/* 1863 */         paramInt = parsePastLine();
/*      */     }
/* 1865 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int parsePastLine()
/*      */   {
/* 1872 */     int i = this.temp[(this.cursor++)];
/* 1873 */     while ((i != 0) && (!isLineSeparator(i)))
/* 1874 */       i = this.temp[(this.cursor++)];
/* 1875 */     return i;
/*      */   }
/*      */ 
/*      */   private int peekPastLine()
/*      */   {
/* 1882 */     int i = this.temp[(++this.cursor)];
/* 1883 */     while ((i != 0) && (!isLineSeparator(i)))
/* 1884 */       i = this.temp[(++this.cursor)];
/* 1885 */     return i;
/*      */   }
/*      */ 
/*      */   private boolean isLineSeparator(int paramInt)
/*      */   {
/* 1892 */     if (has(1)) {
/* 1893 */       return paramInt == 10;
/*      */     }
/* 1895 */     return (paramInt == 10) || (paramInt == 13) || ((paramInt | 0x1) == 8233) || (paramInt == 133);
/*      */   }
/*      */ 
/*      */   private int skip()
/*      */   {
/* 1906 */     int i = this.cursor;
/* 1907 */     int j = this.temp[(i + 1)];
/* 1908 */     this.cursor = (i + 2);
/* 1909 */     return j;
/*      */   }
/*      */ 
/*      */   private void unread()
/*      */   {
/* 1916 */     this.cursor -= 1;
/*      */   }
/*      */ 
/*      */   private PatternSyntaxException error(String paramString)
/*      */   {
/* 1924 */     return new PatternSyntaxException(paramString, this.normalizedPattern, this.cursor - 1);
/*      */   }
/*      */ 
/*      */   private boolean findSupplementary(int paramInt1, int paramInt2)
/*      */   {
/* 1932 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 1933 */       if (isSupplementary(this.temp[i]))
/* 1934 */         return true;
/*      */     }
/* 1936 */     return false;
/*      */   }
/*      */ 
/*      */   private static final boolean isSupplementary(int paramInt)
/*      */   {
/* 1944 */     return (paramInt >= 65536) || (Character.isSurrogate((char)paramInt));
/*      */   }
/*      */ 
/*      */   private Node expr(Node paramNode)
/*      */   {
/* 1959 */     Object localObject1 = null;
/* 1960 */     Object localObject2 = null;
/* 1961 */     BranchConn localBranchConn = null;
/*      */     while (true)
/*      */     {
/* 1964 */       Node localNode1 = sequence(paramNode);
/* 1965 */       Node localNode2 = this.root;
/* 1966 */       if (localObject1 == null) {
/* 1967 */         localObject1 = localNode1;
/* 1968 */         localObject2 = localNode2;
/*      */       }
/*      */       else {
/* 1971 */         if (localBranchConn == null) {
/* 1972 */           localBranchConn = new BranchConn();
/* 1973 */           localBranchConn.next = paramNode;
/*      */         }
/* 1975 */         if (localNode1 == paramNode)
/*      */         {
/* 1979 */           localNode1 = null;
/*      */         }
/*      */         else {
/* 1982 */           localNode2.next = localBranchConn;
/*      */         }
/* 1984 */         if ((localObject1 instanceof Branch)) {
/* 1985 */           ((Branch)localObject1).add(localNode1);
/*      */         } else {
/* 1987 */           if (localObject1 == paramNode) {
/* 1988 */             localObject1 = null;
/*      */           }
/*      */           else
/*      */           {
/* 1992 */             localObject2.next = localBranchConn;
/*      */           }
/* 1994 */           localObject1 = new Branch((Node)localObject1, localNode1, localBranchConn);
/*      */         }
/*      */       }
/* 1997 */       if (peek() != 124) {
/* 1998 */         return localObject1;
/*      */       }
/* 2000 */       next();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node sequence(Node paramNode)
/*      */   {
/* 2008 */     Object localObject1 = null;
/* 2009 */     Object localObject2 = null;
/* 2010 */     Object localObject3 = null;
/*      */     while (true)
/*      */     {
/* 2013 */       int i = peek();
/* 2014 */       switch (i)
/*      */       {
/*      */       case 40:
/* 2018 */         localObject3 = group0();
/*      */ 
/* 2020 */         if (localObject3 != null)
/*      */         {
/* 2022 */           if (localObject1 == null)
/* 2023 */             localObject1 = localObject3;
/*      */           else {
/* 2025 */             ((Node)localObject2).next = ((Node)localObject3);
/*      */           }
/* 2027 */           localObject2 = this.root;
/* 2028 */         }break;
/*      */       case 91:
/* 2030 */         localObject3 = clazz(true);
/* 2031 */         break;
/*      */       case 92:
/* 2033 */         i = nextEscaped();
/* 2034 */         if ((i == 112) || (i == 80)) {
/* 2035 */           boolean bool1 = true;
/* 2036 */           boolean bool2 = i == 80;
/* 2037 */           i = next();
/* 2038 */           if (i != 123)
/* 2039 */             unread();
/*      */           else {
/* 2041 */             bool1 = false;
/*      */           }
/* 2043 */           localObject3 = family(bool1, bool2);
/*      */         } else {
/* 2045 */           unread();
/* 2046 */           localObject3 = atom();
/*      */         }
/* 2048 */         break;
/*      */       case 94:
/* 2050 */         next();
/* 2051 */         if (has(8)) {
/* 2052 */           if (has(1))
/* 2053 */             localObject3 = new UnixCaret();
/*      */           else
/* 2055 */             localObject3 = new Caret();
/*      */         }
/* 2057 */         else localObject3 = new Begin();
/*      */ 
/* 2059 */         break;
/*      */       case 36:
/* 2061 */         next();
/* 2062 */         if (has(1))
/* 2063 */           localObject3 = new UnixDollar(has(8));
/*      */         else
/* 2065 */           localObject3 = new Dollar(has(8));
/* 2066 */         break;
/*      */       case 46:
/* 2068 */         next();
/* 2069 */         if (has(32)) {
/* 2070 */           localObject3 = new All();
/*      */         }
/* 2072 */         else if (has(1))
/* 2073 */           localObject3 = new UnixDot();
/*      */         else {
/* 2075 */           localObject3 = new Dot();
/*      */         }
/*      */ 
/* 2078 */         break;
/*      */       case 41:
/*      */       case 124:
/* 2081 */         break;
/*      */       case 93:
/*      */       case 125:
/* 2084 */         localObject3 = atom();
/* 2085 */         break;
/*      */       case 42:
/*      */       case 43:
/*      */       case 63:
/* 2089 */         next();
/* 2090 */         throw error("Dangling meta character '" + (char)i + "'");
/*      */       case 0:
/* 2092 */         if (this.cursor >= this.patternLength)
/*      */         {
/*      */           break label535;
/*      */         }
/*      */       default:
/* 2097 */         localObject3 = atom();
/*      */ 
/* 2101 */         localObject3 = closure((Node)localObject3);
/*      */ 
/* 2103 */         if (localObject1 == null) {
/* 2104 */           localObject1 = localObject2 = localObject3;
/*      */         } else {
/* 2106 */           ((Node)localObject2).next = ((Node)localObject3);
/* 2107 */           localObject2 = localObject3; } break;
/*      */       }
/*      */     }
/* 2110 */     label535: if (localObject1 == null) {
/* 2111 */       return paramNode;
/*      */     }
/* 2113 */     ((Node)localObject2).next = paramNode;
/* 2114 */     this.root = ((Node)localObject2);
/* 2115 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Node atom()
/*      */   {
/* 2122 */     int i = 0;
/* 2123 */     int j = -1;
/* 2124 */     boolean bool1 = false;
/* 2125 */     int k = peek();
/*      */     while (true) {
/* 2127 */       switch (k) {
/*      */       case 42:
/*      */       case 43:
/*      */       case 63:
/*      */       case 123:
/* 2132 */         if (i <= 1) break label351;
/* 2133 */         this.cursor = j;
/* 2134 */         i--; break;
/*      */       case 36:
/*      */       case 40:
/*      */       case 41:
/*      */       case 46:
/*      */       case 91:
/*      */       case 94:
/*      */       case 124:
/* 2144 */         break;
/*      */       case 92:
/* 2146 */         k = nextEscaped();
/* 2147 */         if ((k == 112) || (k == 80)) {
/* 2148 */           if (i > 0) {
/* 2149 */             unread();
/*      */           }
/*      */           else {
/* 2152 */             boolean bool2 = k == 80;
/* 2153 */             boolean bool3 = true;
/* 2154 */             k = next();
/* 2155 */             if (k != 123)
/* 2156 */               unread();
/*      */             else
/* 2158 */               bool3 = false;
/* 2159 */             return family(bool3, bool2);
/*      */           }
/*      */         } else {
/* 2162 */           unread();
/* 2163 */           j = this.cursor;
/* 2164 */           k = escape(false, i == 0);
/* 2165 */           if (k >= 0) {
/* 2166 */             append(k, i);
/* 2167 */             i++;
/* 2168 */             if (isSupplementary(k)) {
/* 2169 */               bool1 = true;
/*      */             }
/* 2171 */             k = peek();
/* 2172 */             continue;
/* 2173 */           }if (i == 0) {
/* 2174 */             return this.root;
/*      */           }
/*      */ 
/* 2177 */           this.cursor = j;
/* 2178 */         }break;
/*      */       case 0:
/* 2180 */         if (this.cursor >= this.patternLength)
/*      */         {
/*      */           break label351;
/*      */         }
/*      */       default:
/* 2185 */         j = this.cursor;
/* 2186 */         append(k, i);
/* 2187 */         i++;
/* 2188 */         if (isSupplementary(k)) {
/* 2189 */           bool1 = true;
/*      */         }
/* 2191 */         k = next();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2196 */     label351: if (i == 1) {
/* 2197 */       return newSingle(this.buffer[0]);
/*      */     }
/* 2199 */     return newSlice(this.buffer, i, bool1);
/*      */   }
/*      */ 
/*      */   private void append(int paramInt1, int paramInt2)
/*      */   {
/* 2204 */     if (paramInt2 >= this.buffer.length) {
/* 2205 */       int[] arrayOfInt = new int[paramInt2 + paramInt2];
/* 2206 */       System.arraycopy(this.buffer, 0, arrayOfInt, 0, paramInt2);
/* 2207 */       this.buffer = arrayOfInt;
/*      */     }
/* 2209 */     this.buffer[paramInt2] = paramInt1;
/*      */   }
/*      */ 
/*      */   private Node ref(int paramInt)
/*      */   {
/* 2219 */     int i = 0;
/* 2220 */     while (i == 0) {
/* 2221 */       int j = peek();
/* 2222 */       switch (j) {
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/* 2233 */         int k = paramInt * 10 + (j - 48);
/*      */ 
/* 2236 */         if (this.capturingGroupCount - 1 < k) {
/* 2237 */           i = 1;
/*      */         }
/*      */         else {
/* 2240 */           paramInt = k;
/* 2241 */           read();
/* 2242 */         }break;
/*      */       default:
/* 2244 */         i = 1;
/*      */       }
/*      */     }
/*      */ 
/* 2248 */     if (has(2)) {
/* 2249 */       return new CIBackRef(paramInt, has(64));
/*      */     }
/* 2251 */     return new BackRef(paramInt);
/*      */   }
/*      */ 
/*      */   private int escape(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 2263 */     int i = skip();
/* 2264 */     switch (i) {
/*      */     case 48:
/* 2266 */       return o();
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/* 2276 */       if (!paramBoolean1) {
/* 2277 */         if (paramBoolean2) {
/* 2278 */           this.root = ref(i - 48);
/*      */         }
/* 2280 */         return -1; } break;
/*      */     case 65:
/* 2282 */       if (!paramBoolean1) {
/* 2283 */         if (paramBoolean2) this.root = new Begin();
/* 2284 */         return -1; } break;
/*      */     case 66:
/* 2286 */       if (!paramBoolean1) {
/* 2287 */         if (paramBoolean2) this.root = new Bound(Bound.NONE, has(256));
/* 2288 */         return -1; } break;
/*      */     case 67:
/* 2290 */       break;
/*      */     case 68:
/* 2292 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.DIGIT).complement() : new Ctype(1024).complement());
/*      */ 
/* 2295 */       return -1;
/*      */     case 69:
/*      */     case 70:
/* 2298 */       break;
/*      */     case 71:
/* 2300 */       if (!paramBoolean1) {
/* 2301 */         if (paramBoolean2) this.root = new LastMatch();
/* 2302 */         return -1; } break;
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/* 2314 */       break;
/*      */     case 83:
/* 2316 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.WHITE_SPACE).complement() : new Ctype(2048).complement());
/*      */ 
/* 2319 */       return -1;
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/* 2323 */       break;
/*      */     case 87:
/* 2325 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.WORD).complement() : new Ctype(67328).complement());
/*      */ 
/* 2328 */       return -1;
/*      */     case 88:
/*      */     case 89:
/* 2331 */       break;
/*      */     case 90:
/* 2333 */       if (!paramBoolean1) {
/* 2334 */         if (paramBoolean2) {
/* 2335 */           if (has(1))
/* 2336 */             this.root = new UnixDollar(false);
/*      */           else
/* 2338 */             this.root = new Dollar(false);
/*      */         }
/* 2340 */         return -1; } break;
/*      */     case 97:
/* 2342 */       return 7;
/*      */     case 98:
/* 2344 */       if (!paramBoolean1) {
/* 2345 */         if (paramBoolean2) this.root = new Bound(Bound.BOTH, has(256));
/* 2346 */         return -1; } break;
/*      */     case 99:
/* 2348 */       return c();
/*      */     case 100:
/* 2350 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.DIGIT) : new Ctype(1024));
/*      */ 
/* 2353 */       return -1;
/*      */     case 101:
/* 2355 */       return 27;
/*      */     case 102:
/* 2357 */       return 12;
/*      */     case 103:
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/* 2362 */       break;
/*      */     case 107:
/* 2364 */       if (!paramBoolean1)
/*      */       {
/* 2366 */         if (read() != 60)
/* 2367 */           throw error("\\k is not followed by '<' for named capturing group");
/* 2368 */         String str = groupname(read());
/* 2369 */         if (!namedGroups().containsKey(str))
/* 2370 */           throw error("(named capturing group <" + str + "> does not exit");
/* 2371 */         if (paramBoolean2) {
/* 2372 */           if (has(2))
/* 2373 */             this.root = new CIBackRef(((Integer)namedGroups().get(str)).intValue(), has(64));
/*      */           else
/* 2375 */             this.root = new BackRef(((Integer)namedGroups().get(str)).intValue());
/*      */         }
/* 2377 */         return -1; } break;
/*      */     case 108:
/*      */     case 109:
/* 2380 */       break;
/*      */     case 110:
/* 2382 */       return 10;
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/* 2386 */       break;
/*      */     case 114:
/* 2388 */       return 13;
/*      */     case 115:
/* 2390 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.WHITE_SPACE) : new Ctype(2048));
/*      */ 
/* 2393 */       return -1;
/*      */     case 116:
/* 2395 */       return 9;
/*      */     case 117:
/* 2397 */       return u();
/*      */     case 118:
/* 2399 */       return 11;
/*      */     case 119:
/* 2401 */       if (paramBoolean2) this.root = (has(256) ? new Utype(UnicodeProp.WORD) : new Ctype(67328));
/*      */ 
/* 2404 */       return -1;
/*      */     case 120:
/* 2406 */       return x();
/*      */     case 121:
/* 2408 */       break;
/*      */     case 122:
/* 2410 */       if (!paramBoolean1) {
/* 2411 */         if (paramBoolean2) this.root = new End();
/* 2412 */         return -1; } break;
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*      */     case 96:
/*      */     default:
/* 2414 */       return i;
/*      */     }
/* 2416 */     throw error("Illegal/unsupported escape sequence");
/*      */   }
/*      */ 
/*      */   private CharProperty clazz(boolean paramBoolean)
/*      */   {
/* 2427 */     Object localObject1 = null;
/* 2428 */     Object localObject2 = null;
/* 2429 */     BitClass localBitClass = new BitClass();
/* 2430 */     int i = 1;
/* 2431 */     int j = 1;
/* 2432 */     int k = next();
/*      */     while (true)
/* 2434 */       switch (k)
/*      */       {
/*      */       case 94:
/* 2437 */         if ((j != 0) && 
/* 2438 */           (this.temp[(this.cursor - 1)] == 91))
/*      */         {
/* 2440 */           k = next();
/* 2441 */           i = i == 0 ? 1 : 0;
/* 2442 */         }break;
/*      */       case 91:
/* 2448 */         j = 0;
/* 2449 */         localObject2 = clazz(true);
/* 2450 */         if (localObject1 == null)
/* 2451 */           localObject1 = localObject2;
/*      */         else
/* 2453 */           localObject1 = union((CharProperty)localObject1, (CharProperty)localObject2);
/* 2454 */         k = peek();
/* 2455 */         break;
/*      */       case 38:
/* 2457 */         j = 0;
/* 2458 */         k = next();
/* 2459 */         if (k == 38) {
/* 2460 */           k = next();
/* 2461 */           CharProperty localCharProperty = null;
/* 2462 */           while ((k != 93) && (k != 38)) {
/* 2463 */             if (k == 91) {
/* 2464 */               if (localCharProperty == null)
/* 2465 */                 localCharProperty = clazz(true);
/*      */               else
/* 2467 */                 localCharProperty = union(localCharProperty, clazz(true));
/*      */             } else {
/* 2469 */               unread();
/* 2470 */               localCharProperty = clazz(false);
/*      */             }
/* 2472 */             k = peek();
/*      */           }
/* 2474 */           if (localCharProperty != null)
/* 2475 */             localObject2 = localCharProperty;
/* 2476 */           if (localObject1 == null) {
/* 2477 */             if (localCharProperty == null) {
/* 2478 */               throw error("Bad class syntax");
/*      */             }
/* 2480 */             localObject1 = localCharProperty;
/*      */           } else {
/* 2482 */             localObject1 = intersection((CharProperty)localObject1, (CharProperty)localObject2);
/*      */           }
/*      */         }
/*      */         else {
/* 2486 */           unread();
/* 2487 */         }break;
/*      */       case 0:
/* 2491 */         j = 0;
/* 2492 */         if (this.cursor >= this.patternLength) {
/* 2493 */           throw error("Unclosed character class");
/*      */         }
/*      */       case 93:
/* 2496 */         j = 0;
/* 2497 */         if (localObject1 != null) {
/* 2498 */           if (paramBoolean)
/* 2499 */             next();
/* 2500 */           return localObject1;
/*      */         }
/*      */ 
/*      */       default:
/* 2504 */         j = 0;
/*      */ 
/* 2507 */         localObject2 = range(localBitClass);
/* 2508 */         if (i != 0) {
/* 2509 */           if (localObject1 == null) {
/* 2510 */             localObject1 = localObject2;
/*      */           }
/* 2512 */           else if (localObject1 != localObject2) {
/* 2513 */             localObject1 = union((CharProperty)localObject1, (CharProperty)localObject2);
/*      */           }
/*      */         }
/* 2516 */         else if (localObject1 == null) {
/* 2517 */           localObject1 = ((CharProperty)localObject2).complement();
/*      */         }
/* 2519 */         else if (localObject1 != localObject2) {
/* 2520 */           localObject1 = setDifference((CharProperty)localObject1, (CharProperty)localObject2);
/*      */         }
/*      */ 
/* 2523 */         k = peek();
/*      */       }
/*      */   }
/*      */ 
/*      */   private CharProperty bitsOrSingle(BitClass paramBitClass, int paramInt)
/*      */   {
/* 2546 */     if ((paramInt < 256) && ((!has(2)) || (!has(64)) || ((paramInt != 255) && (paramInt != 181) && (paramInt != 73) && (paramInt != 105) && (paramInt != 83) && (paramInt != 115) && (paramInt != 75) && (paramInt != 107) && (paramInt != 197) && (paramInt != 229))))
/*      */     {
/* 2553 */       return paramBitClass.add(paramInt, flags());
/* 2554 */     }return newSingle(paramInt);
/*      */   }
/*      */ 
/*      */   private CharProperty range(BitClass paramBitClass)
/*      */   {
/* 2562 */     int i = peek();
/* 2563 */     if (i == 92) {
/* 2564 */       i = nextEscaped();
/* 2565 */       if ((i == 112) || (i == 80)) {
/* 2566 */         boolean bool1 = i == 80;
/* 2567 */         boolean bool2 = true;
/*      */ 
/* 2569 */         i = next();
/* 2570 */         if (i != 123)
/* 2571 */           unread();
/*      */         else
/* 2573 */           bool2 = false;
/* 2574 */         return family(bool2, bool1);
/*      */       }
/* 2576 */       unread();
/* 2577 */       i = escape(true, true);
/* 2578 */       if (i == -1)
/* 2579 */         return (CharProperty)this.root;
/*      */     }
/*      */     else {
/* 2582 */       i = single();
/*      */     }
/* 2584 */     if (i >= 0) {
/* 2585 */       if (peek() == 45) {
/* 2586 */         int j = this.temp[(this.cursor + 1)];
/* 2587 */         if (j == 91) {
/* 2588 */           return bitsOrSingle(paramBitClass, i);
/*      */         }
/* 2590 */         if (j != 93) {
/* 2591 */           next();
/* 2592 */           int k = single();
/* 2593 */           if (k < i)
/* 2594 */             throw error("Illegal character range");
/* 2595 */           if (has(2)) {
/* 2596 */             return caseInsensitiveRangeFor(i, k);
/*      */           }
/* 2598 */           return rangeFor(i, k);
/*      */         }
/*      */       }
/* 2601 */       return bitsOrSingle(paramBitClass, i);
/*      */     }
/* 2603 */     throw error("Unexpected character '" + (char)i + "'");
/*      */   }
/*      */ 
/*      */   private int single() {
/* 2607 */     int i = peek();
/* 2608 */     switch (i) {
/*      */     case 92:
/* 2610 */       return escape(true, false);
/*      */     }
/* 2612 */     next();
/* 2613 */     return i;
/*      */   }
/*      */ 
/*      */   private CharProperty family(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 2623 */     next();
/*      */ 
/* 2625 */     Object localObject1 = null;
/*      */     String str;
/* 2627 */     if (paramBoolean1) {
/* 2628 */       i = this.temp[this.cursor];
/* 2629 */       if (!Character.isSupplementaryCodePoint(i))
/* 2630 */         str = String.valueOf((char)i);
/*      */       else {
/* 2632 */         str = new String(this.temp, this.cursor, 1);
/*      */       }
/* 2634 */       read();
/*      */     } else {
/* 2636 */       i = this.cursor;
/*      */ 
/* 2638 */       while (read() != 125);
/* 2640 */       mark(0);
/* 2641 */       int j = this.cursor;
/* 2642 */       if (j > this.patternLength)
/* 2643 */         throw error("Unclosed character family");
/* 2644 */       if (i + 1 >= j)
/* 2645 */         throw error("Empty character family");
/* 2646 */       str = new String(this.temp, i, j - i - 1);
/*      */     }
/*      */ 
/* 2649 */     int i = str.indexOf('=');
/*      */     Object localObject2;
/* 2650 */     if (i != -1)
/*      */     {
/* 2652 */       localObject2 = str.substring(i + 1);
/* 2653 */       str = str.substring(0, i).toLowerCase(Locale.ENGLISH);
/* 2654 */       if (("sc".equals(str)) || ("script".equals(str)))
/* 2655 */         localObject1 = unicodeScriptPropertyFor((String)localObject2);
/* 2656 */       else if (("blk".equals(str)) || ("block".equals(str)))
/* 2657 */         localObject1 = unicodeBlockPropertyFor((String)localObject2);
/* 2658 */       else if (("gc".equals(str)) || ("general_category".equals(str)))
/* 2659 */         localObject1 = charPropertyNodeFor((String)localObject2);
/*      */       else {
/* 2661 */         throw error("Unknown Unicode property {name=<" + str + ">, " + "value=<" + (String)localObject2 + ">}");
/*      */       }
/*      */ 
/*      */     }
/* 2665 */     else if (str.startsWith("In"))
/*      */     {
/* 2667 */       localObject1 = unicodeBlockPropertyFor(str.substring(2));
/* 2668 */     } else if (str.startsWith("Is"))
/*      */     {
/* 2670 */       str = str.substring(2);
/* 2671 */       localObject2 = UnicodeProp.forName(str);
/* 2672 */       if (localObject2 != null)
/* 2673 */         localObject1 = new Utype((UnicodeProp)localObject2);
/* 2674 */       if (localObject1 == null)
/* 2675 */         localObject1 = CharPropertyNames.charPropertyFor(str);
/* 2676 */       if (localObject1 == null)
/* 2677 */         localObject1 = unicodeScriptPropertyFor(str);
/*      */     } else {
/* 2679 */       if (has(256)) {
/* 2680 */         localObject2 = UnicodeProp.forPOSIXName(str);
/* 2681 */         if (localObject2 != null)
/* 2682 */           localObject1 = new Utype((UnicodeProp)localObject2);
/*      */       }
/* 2684 */       if (localObject1 == null) {
/* 2685 */         localObject1 = charPropertyNodeFor(str);
/*      */       }
/*      */     }
/* 2688 */     if (paramBoolean2) {
/* 2689 */       if (((localObject1 instanceof Category)) || ((localObject1 instanceof Block)))
/* 2690 */         this.hasSupplementary = true;
/* 2691 */       localObject1 = ((CharProperty)localObject1).complement();
/*      */     }
/* 2693 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private CharProperty unicodeScriptPropertyFor(String paramString)
/*      */   {
/*      */     Character.UnicodeScript localUnicodeScript;
/*      */     try
/*      */     {
/* 2704 */       localUnicodeScript = Character.UnicodeScript.forName(paramString);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 2706 */       throw error("Unknown character script name {" + paramString + "}");
/*      */     }
/* 2708 */     return new Script(localUnicodeScript);
/*      */   }
/*      */ 
/*      */   private CharProperty unicodeBlockPropertyFor(String paramString)
/*      */   {
/*      */     Character.UnicodeBlock localUnicodeBlock;
/*      */     try
/*      */     {
/* 2717 */       localUnicodeBlock = Character.UnicodeBlock.forName(paramString);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 2719 */       throw error("Unknown character block name {" + paramString + "}");
/*      */     }
/* 2721 */     return new Block(localUnicodeBlock);
/*      */   }
/*      */ 
/*      */   private CharProperty charPropertyNodeFor(String paramString)
/*      */   {
/* 2728 */     CharProperty localCharProperty = CharPropertyNames.charPropertyFor(paramString);
/* 2729 */     if (localCharProperty == null)
/* 2730 */       throw error("Unknown character property name {" + paramString + "}");
/* 2731 */     return localCharProperty;
/*      */   }
/*      */ 
/*      */   private String groupname(int paramInt)
/*      */   {
/* 2739 */     StringBuilder localStringBuilder = new StringBuilder();
/* 2740 */     localStringBuilder.append(Character.toChars(paramInt));
/* 2741 */     while ((ASCII.isLower(paramInt = read())) || (ASCII.isUpper(paramInt)) || (ASCII.isDigit(paramInt)))
/*      */     {
/* 2743 */       localStringBuilder.append(Character.toChars(paramInt));
/*      */     }
/* 2745 */     if (localStringBuilder.length() == 0)
/* 2746 */       throw error("named capturing group has 0 length name");
/* 2747 */     if (paramInt != 62)
/* 2748 */       throw error("named capturing group is missing trailing '>'");
/* 2749 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private Node group0()
/*      */   {
/* 2758 */     boolean bool1 = false;
/* 2759 */     Object localObject1 = null;
/* 2760 */     Object localObject2 = null;
/* 2761 */     int i = this.flags;
/* 2762 */     this.root = null;
/* 2763 */     int j = next();
/*      */     Object localObject3;
/* 2764 */     if (j == 63) {
/* 2765 */       j = skip();
/* 2766 */       switch (j) {
/*      */       case 58:
/* 2768 */         localObject1 = createGroup(true);
/* 2769 */         localObject2 = this.root;
/* 2770 */         ((Node)localObject1).next = expr((Node)localObject2);
/* 2771 */         break;
/*      */       case 33:
/*      */       case 61:
/* 2774 */         localObject1 = createGroup(true);
/* 2775 */         localObject2 = this.root;
/* 2776 */         ((Node)localObject1).next = expr((Node)localObject2);
/* 2777 */         if (j == 61)
/* 2778 */           localObject1 = localObject2 = new Pos((Node)localObject1);
/*      */         else {
/* 2780 */           localObject1 = localObject2 = new Neg((Node)localObject1);
/*      */         }
/* 2782 */         break;
/*      */       case 62:
/* 2784 */         localObject1 = createGroup(true);
/* 2785 */         localObject2 = this.root;
/* 2786 */         ((Node)localObject1).next = expr((Node)localObject2);
/* 2787 */         localObject1 = localObject2 = new Ques((Node)localObject1, 3);
/* 2788 */         break;
/*      */       case 60:
/* 2790 */         j = read();
/* 2791 */         if ((ASCII.isLower(j)) || (ASCII.isUpper(j)))
/*      */         {
/* 2793 */           String str = groupname(j);
/* 2794 */           if (namedGroups().containsKey(str)) {
/* 2795 */             throw error("Named capturing group <" + str + "> is already defined");
/*      */           }
/* 2797 */           bool1 = true;
/* 2798 */           localObject1 = createGroup(false);
/* 2799 */           localObject2 = this.root;
/* 2800 */           namedGroups().put(str, Integer.valueOf(this.capturingGroupCount - 1));
/* 2801 */           ((Node)localObject1).next = expr((Node)localObject2);
/*      */         }
/*      */         else {
/* 2804 */           int k = this.cursor;
/* 2805 */           localObject1 = createGroup(true);
/* 2806 */           localObject2 = this.root;
/* 2807 */           ((Node)localObject1).next = expr((Node)localObject2);
/* 2808 */           ((Node)localObject2).next = lookbehindEnd;
/* 2809 */           localObject3 = new TreeInfo();
/* 2810 */           ((Node)localObject1).study((TreeInfo)localObject3);
/* 2811 */           if (!((TreeInfo)localObject3).maxValid) {
/* 2812 */             throw error("Look-behind group does not have an obvious maximum length");
/*      */           }
/*      */ 
/* 2815 */           boolean bool2 = findSupplementary(k, this.patternLength);
/* 2816 */           if (j == 61) {
/* 2817 */             localObject1 = localObject2 = bool2 ? new BehindS((Node)localObject1, ((TreeInfo)localObject3).maxLength, ((TreeInfo)localObject3).minLength) : new Behind((Node)localObject1, ((TreeInfo)localObject3).maxLength, ((TreeInfo)localObject3).minLength);
/*      */           }
/* 2822 */           else if (j == 33) {
/* 2823 */             localObject1 = localObject2 = bool2 ? new NotBehindS((Node)localObject1, ((TreeInfo)localObject3).maxLength, ((TreeInfo)localObject3).minLength) : new NotBehind((Node)localObject1, ((TreeInfo)localObject3).maxLength, ((TreeInfo)localObject3).minLength);
/*      */           }
/*      */           else
/*      */           {
/* 2829 */             throw error("Unknown look-behind group");
/*      */           }
/*      */         }
/*      */         break;
/*      */       case 36:
/*      */       case 64:
/* 2834 */         throw error("Unknown group type");
/*      */       default:
/* 2836 */         unread();
/* 2837 */         addFlag();
/* 2838 */         j = read();
/* 2839 */         if (j == 41) {
/* 2840 */           return null;
/*      */         }
/* 2842 */         if (j != 58) {
/* 2843 */           throw error("Unknown inline modifier");
/*      */         }
/* 2845 */         localObject1 = createGroup(true);
/* 2846 */         localObject2 = this.root;
/* 2847 */         ((Node)localObject1).next = expr((Node)localObject2);
/*      */       }
/*      */     }
/*      */     else {
/* 2851 */       bool1 = true;
/* 2852 */       localObject1 = createGroup(false);
/* 2853 */       localObject2 = this.root;
/* 2854 */       ((Node)localObject1).next = expr((Node)localObject2);
/*      */     }
/*      */ 
/* 2857 */     accept(41, "Unclosed group");
/* 2858 */     this.flags = i;
/*      */ 
/* 2861 */     Node localNode = closure((Node)localObject1);
/* 2862 */     if (localNode == localObject1) {
/* 2863 */       this.root = ((Node)localObject2);
/* 2864 */       return localNode;
/*      */     }
/* 2866 */     if (localObject1 == localObject2) {
/* 2867 */       this.root = localNode;
/* 2868 */       return localNode;
/*      */     }
/*      */ 
/* 2871 */     if ((localNode instanceof Ques)) {
/* 2872 */       localObject3 = (Ques)localNode;
/* 2873 */       if (((Ques)localObject3).type == 2) {
/* 2874 */         this.root = localNode;
/* 2875 */         return localNode;
/*      */       }
/* 2877 */       ((Node)localObject2).next = new BranchConn();
/* 2878 */       localObject2 = ((Node)localObject2).next;
/* 2879 */       if (((Ques)localObject3).type == 0)
/* 2880 */         localObject1 = new Branch((Node)localObject1, null, (Node)localObject2);
/*      */       else {
/* 2882 */         localObject1 = new Branch(null, (Node)localObject1, (Node)localObject2);
/*      */       }
/* 2884 */       this.root = ((Node)localObject2);
/* 2885 */       return localObject1;
/* 2886 */     }if ((localNode instanceof Curly)) {
/* 2887 */       localObject3 = (Curly)localNode;
/* 2888 */       if (((Curly)localObject3).type == 2) {
/* 2889 */         this.root = localNode;
/* 2890 */         return localNode;
/*      */       }
/*      */ 
/* 2893 */       TreeInfo localTreeInfo = new TreeInfo();
/* 2894 */       if (((Node)localObject1).study(localTreeInfo)) {
/* 2895 */         GroupTail localGroupTail = (GroupTail)localObject2;
/* 2896 */         localObject1 = this.root = new GroupCurly(((Node)localObject1).next, ((Curly)localObject3).cmin, ((Curly)localObject3).cmax, ((Curly)localObject3).type, ((GroupTail)localObject2).localIndex, ((GroupTail)localObject2).groupIndex, bool1);
/*      */ 
/* 2901 */         return localObject1;
/*      */       }
/* 2903 */       int m = ((GroupHead)localObject1).localIndex;
/*      */       Object localObject4;
/* 2905 */       if (((Curly)localObject3).type == 0)
/* 2906 */         localObject4 = new Loop(this.localCount, m);
/*      */       else
/* 2908 */         localObject4 = new LazyLoop(this.localCount, m);
/* 2909 */       Prolog localProlog = new Prolog((Loop)localObject4);
/* 2910 */       this.localCount += 1;
/* 2911 */       ((Loop)localObject4).cmin = ((Curly)localObject3).cmin;
/* 2912 */       ((Loop)localObject4).cmax = ((Curly)localObject3).cmax;
/* 2913 */       ((Loop)localObject4).body = ((Node)localObject1);
/* 2914 */       ((Node)localObject2).next = ((Node)localObject4);
/* 2915 */       this.root = ((Node)localObject4);
/* 2916 */       return localProlog;
/*      */     }
/*      */ 
/* 2919 */     throw error("Internal logic error");
/*      */   }
/*      */ 
/*      */   private Node createGroup(boolean paramBoolean)
/*      */   {
/* 2928 */     int i = this.localCount++;
/* 2929 */     int j = 0;
/* 2930 */     if (!paramBoolean)
/* 2931 */       j = this.capturingGroupCount++;
/* 2932 */     GroupHead localGroupHead = new GroupHead(i);
/* 2933 */     this.root = new GroupTail(i, j);
/* 2934 */     if ((!paramBoolean) && (j < 10))
/* 2935 */       this.groupNodes[j] = localGroupHead;
/* 2936 */     return localGroupHead;
/*      */   }
/*      */ 
/*      */   private void addFlag()
/*      */   {
/* 2943 */     int i = peek();
/*      */     while (true) {
/* 2945 */       switch (i) {
/*      */       case 105:
/* 2947 */         this.flags |= 2;
/* 2948 */         break;
/*      */       case 109:
/* 2950 */         this.flags |= 8;
/* 2951 */         break;
/*      */       case 115:
/* 2953 */         this.flags |= 32;
/* 2954 */         break;
/*      */       case 100:
/* 2956 */         this.flags |= 1;
/* 2957 */         break;
/*      */       case 117:
/* 2959 */         this.flags |= 64;
/* 2960 */         break;
/*      */       case 99:
/* 2962 */         this.flags |= 128;
/* 2963 */         break;
/*      */       case 120:
/* 2965 */         this.flags |= 4;
/* 2966 */         break;
/*      */       case 85:
/* 2968 */         this.flags |= 320;
/* 2969 */         break;
/*      */       case 45:
/* 2971 */         i = next();
/* 2972 */         subFlag();
/*      */       default:
/* 2974 */         return;
/*      */       }
/* 2976 */       i = next();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void subFlag()
/*      */   {
/* 2985 */     int i = peek();
/*      */     while (true) {
/* 2987 */       switch (i) {
/*      */       case 105:
/* 2989 */         this.flags &= -3;
/* 2990 */         break;
/*      */       case 109:
/* 2992 */         this.flags &= -9;
/* 2993 */         break;
/*      */       case 115:
/* 2995 */         this.flags &= -33;
/* 2996 */         break;
/*      */       case 100:
/* 2998 */         this.flags &= -2;
/* 2999 */         break;
/*      */       case 117:
/* 3001 */         this.flags &= -65;
/* 3002 */         break;
/*      */       case 99:
/* 3004 */         this.flags &= -129;
/* 3005 */         break;
/*      */       case 120:
/* 3007 */         this.flags &= -5;
/* 3008 */         break;
/*      */       case 85:
/* 3010 */         this.flags &= -321;
/*      */       default:
/* 3012 */         return;
/*      */       }
/* 3014 */       i = next();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node closure(Node paramNode)
/*      */   {
/* 3035 */     int i = peek();
/* 3036 */     switch (i) {
/*      */     case 63:
/* 3038 */       i = next();
/* 3039 */       if (i == 63) {
/* 3040 */         next();
/* 3041 */         return new Ques(paramNode, 1);
/* 3042 */       }if (i == 43) {
/* 3043 */         next();
/* 3044 */         return new Ques(paramNode, 2);
/*      */       }
/* 3046 */       return new Ques(paramNode, 0);
/*      */     case 42:
/* 3048 */       i = next();
/* 3049 */       if (i == 63) {
/* 3050 */         next();
/* 3051 */         return new Curly(paramNode, 0, 2147483647, 1);
/* 3052 */       }if (i == 43) {
/* 3053 */         next();
/* 3054 */         return new Curly(paramNode, 0, 2147483647, 2);
/*      */       }
/* 3056 */       return new Curly(paramNode, 0, 2147483647, 0);
/*      */     case 43:
/* 3058 */       i = next();
/* 3059 */       if (i == 63) {
/* 3060 */         next();
/* 3061 */         return new Curly(paramNode, 1, 2147483647, 1);
/* 3062 */       }if (i == 43) {
/* 3063 */         next();
/* 3064 */         return new Curly(paramNode, 1, 2147483647, 2);
/*      */       }
/* 3066 */       return new Curly(paramNode, 1, 2147483647, 0);
/*      */     case 123:
/* 3068 */       i = this.temp[(this.cursor + 1)];
/* 3069 */       if (ASCII.isDigit(i)) {
/* 3070 */         skip();
/* 3071 */         int j = 0;
/*      */         do
/* 3073 */           j = j * 10 + (i - 48);
/* 3074 */         while (ASCII.isDigit(i = read()));
/* 3075 */         int k = j;
/* 3076 */         if (i == 44) {
/* 3077 */           i = read();
/* 3078 */           k = 2147483647;
/* 3079 */           if (i != 125) {
/* 3080 */             k = 0;
/* 3081 */             while (ASCII.isDigit(i)) {
/* 3082 */               k = k * 10 + (i - 48);
/* 3083 */               i = read();
/*      */             }
/*      */           }
/*      */         }
/* 3087 */         if (i != 125)
/* 3088 */           throw error("Unclosed counted closure");
/* 3089 */         if ((j | k | k - j) < 0) {
/* 3090 */           throw error("Illegal repetition range");
/*      */         }
/* 3092 */         i = peek();
/*      */         Curly localCurly;
/* 3093 */         if (i == 63) {
/* 3094 */           next();
/* 3095 */           localCurly = new Curly(paramNode, j, k, 1);
/* 3096 */         } else if (i == 43) {
/* 3097 */           next();
/* 3098 */           localCurly = new Curly(paramNode, j, k, 2);
/*      */         } else {
/* 3100 */           localCurly = new Curly(paramNode, j, k, 0);
/*      */         }
/* 3102 */         return localCurly;
/*      */       }
/* 3104 */       throw error("Illegal repetition");
/*      */     }
/*      */ 
/* 3107 */     return paramNode;
/*      */   }
/*      */ 
/*      */   private int c()
/*      */   {
/* 3115 */     if (this.cursor < this.patternLength) {
/* 3116 */       return read() ^ 0x40;
/*      */     }
/* 3118 */     throw error("Illegal control escape sequence");
/*      */   }
/*      */ 
/*      */   private int o()
/*      */   {
/* 3125 */     int i = read();
/* 3126 */     if ((i - 48 | 55 - i) >= 0) {
/* 3127 */       int j = read();
/* 3128 */       if ((j - 48 | 55 - j) >= 0) {
/* 3129 */         int k = read();
/* 3130 */         if (((k - 48 | 55 - k) >= 0) && ((i - 48 | 51 - i) >= 0)) {
/* 3131 */           return (i - 48) * 64 + (j - 48) * 8 + (k - 48);
/*      */         }
/* 3133 */         unread();
/* 3134 */         return (i - 48) * 8 + (j - 48);
/*      */       }
/* 3136 */       unread();
/* 3137 */       return i - 48;
/*      */     }
/* 3139 */     throw error("Illegal octal escape sequence");
/*      */   }
/*      */ 
/*      */   private int x()
/*      */   {
/* 3146 */     int i = read();
/*      */     int j;
/* 3147 */     if (ASCII.isHexDigit(i)) {
/* 3148 */       j = read();
/* 3149 */       if (ASCII.isHexDigit(j))
/* 3150 */         return ASCII.toDigit(i) * 16 + ASCII.toDigit(j);
/*      */     }
/* 3152 */     else if ((i == 123) && (ASCII.isHexDigit(peek()))) {
/* 3153 */       j = 0;
/* 3154 */       while (ASCII.isHexDigit(i = read())) {
/* 3155 */         j = (j << 4) + ASCII.toDigit(i);
/* 3156 */         if (j > 1114111)
/* 3157 */           throw error("Hexadecimal codepoint is too big");
/*      */       }
/* 3159 */       if (i != 125)
/* 3160 */         throw error("Unclosed hexadecimal escape sequence");
/* 3161 */       return j;
/*      */     }
/* 3163 */     throw error("Illegal hexadecimal escape sequence");
/*      */   }
/*      */ 
/*      */   private int cursor()
/*      */   {
/* 3170 */     return this.cursor;
/*      */   }
/*      */ 
/*      */   private void setcursor(int paramInt) {
/* 3174 */     this.cursor = paramInt;
/*      */   }
/*      */ 
/*      */   private int uxxxx() {
/* 3178 */     int i = 0;
/* 3179 */     for (int j = 0; j < 4; j++) {
/* 3180 */       int k = read();
/* 3181 */       if (!ASCII.isHexDigit(k)) {
/* 3182 */         throw error("Illegal Unicode escape sequence");
/*      */       }
/* 3184 */       i = i * 16 + ASCII.toDigit(k);
/*      */     }
/* 3186 */     return i;
/*      */   }
/*      */ 
/*      */   private int u() {
/* 3190 */     int i = uxxxx();
/* 3191 */     if (Character.isHighSurrogate((char)i)) {
/* 3192 */       int j = cursor();
/* 3193 */       if ((read() == 92) && (read() == 117)) {
/* 3194 */         int k = uxxxx();
/* 3195 */         if (Character.isLowSurrogate((char)k))
/* 3196 */           return Character.toCodePoint((char)i, (char)k);
/*      */       }
/* 3198 */       setcursor(j);
/*      */     }
/* 3200 */     return i;
/*      */   }
/*      */ 
/*      */   private static final int countChars(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 3210 */     if ((paramInt2 == 1) && (!Character.isHighSurrogate(paramCharSequence.charAt(paramInt1)))) {
/* 3211 */       assert ((paramInt1 >= 0) && (paramInt1 < paramCharSequence.length()));
/* 3212 */       return 1;
/*      */     }
/* 3214 */     int i = paramCharSequence.length();
/* 3215 */     int j = paramInt1;
/* 3216 */     if (paramInt2 >= 0) {
/* 3217 */       assert ((paramInt1 >= 0) && (paramInt1 < i));
/* 3218 */       for (k = 0; (j < i) && (k < paramInt2); k++) {
/* 3219 */         if ((Character.isHighSurrogate(paramCharSequence.charAt(j++))) && 
/* 3220 */           (j < i) && (Character.isLowSurrogate(paramCharSequence.charAt(j)))) {
/* 3221 */           j++;
/*      */         }
/*      */       }
/*      */ 
/* 3225 */       return j - paramInt1;
/*      */     }
/*      */ 
/* 3228 */     assert ((paramInt1 >= 0) && (paramInt1 <= i));
/* 3229 */     if (paramInt1 == 0) {
/* 3230 */       return 0;
/*      */     }
/* 3232 */     int k = -paramInt2;
/* 3233 */     for (int m = 0; (j > 0) && (m < k); m++) {
/* 3234 */       if ((Character.isLowSurrogate(paramCharSequence.charAt(--j))) && 
/* 3235 */         (j > 0) && (Character.isHighSurrogate(paramCharSequence.charAt(j - 1)))) {
/* 3236 */         j--;
/*      */       }
/*      */     }
/*      */ 
/* 3240 */     return paramInt1 - j;
/*      */   }
/*      */ 
/*      */   private static final int countCodePoints(CharSequence paramCharSequence) {
/* 3244 */     int i = paramCharSequence.length();
/* 3245 */     int j = 0;
/* 3246 */     for (int k = 0; k < i; ) {
/* 3247 */       j++;
/* 3248 */       if ((Character.isHighSurrogate(paramCharSequence.charAt(k++))) && 
/* 3249 */         (k < i) && (Character.isLowSurrogate(paramCharSequence.charAt(k)))) {
/* 3250 */         k++;
/*      */       }
/*      */     }
/*      */ 
/* 3254 */     return j;
/*      */   }
/*      */ 
/*      */   private CharProperty newSingle(int paramInt)
/*      */   {
/* 3289 */     if (has(2))
/*      */     {
/*      */       int j;
/*      */       int i;
/* 3291 */       if (has(64)) {
/* 3292 */         j = Character.toUpperCase(paramInt);
/* 3293 */         i = Character.toLowerCase(j);
/* 3294 */         if (j != i)
/* 3295 */           return new SingleU(i);
/* 3296 */       } else if (ASCII.isAscii(paramInt)) {
/* 3297 */         i = ASCII.toLower(paramInt);
/* 3298 */         j = ASCII.toUpper(paramInt);
/* 3299 */         if (i != j)
/* 3300 */           return new SingleI(i, j);
/*      */       }
/*      */     }
/* 3303 */     if (isSupplementary(paramInt))
/* 3304 */       return new SingleS(paramInt);
/* 3305 */     return new Single(paramInt);
/*      */   }
/*      */ 
/*      */   private Node newSlice(int[] paramArrayOfInt, int paramInt, boolean paramBoolean)
/*      */   {
/* 3312 */     int[] arrayOfInt = new int[paramInt];
/* 3313 */     if (has(2)) {
/* 3314 */       if (has(64)) {
/* 3315 */         for (i = 0; i < paramInt; i++) {
/* 3316 */           arrayOfInt[i] = Character.toLowerCase(Character.toUpperCase(paramArrayOfInt[i]));
/*      */         }
/*      */ 
/* 3319 */         return paramBoolean ? new SliceUS(arrayOfInt) : new SliceU(arrayOfInt);
/*      */       }
/* 3321 */       for (i = 0; i < paramInt; i++) {
/* 3322 */         arrayOfInt[i] = ASCII.toLower(paramArrayOfInt[i]);
/*      */       }
/* 3324 */       return paramBoolean ? new SliceIS(arrayOfInt) : new SliceI(arrayOfInt);
/*      */     }
/* 3326 */     for (int i = 0; i < paramInt; i++) {
/* 3327 */       arrayOfInt[i] = paramArrayOfInt[i];
/*      */     }
/* 3329 */     return paramBoolean ? new SliceS(arrayOfInt) : new Slice(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private static boolean inRange(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 3999 */     return (paramInt1 <= paramInt2) && (paramInt2 <= paramInt3);
/*      */   }
/*      */ 
/*      */   private static CharProperty rangeFor(int paramInt1, final int paramInt2)
/*      */   {
/* 4007 */     return new CharProperty(paramInt1) {
/*      */       boolean isSatisfiedBy(int paramAnonymousInt) {
/* 4009 */         return Pattern.inRange(this.val$lower, paramAnonymousInt, paramInt2);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private CharProperty caseInsensitiveRangeFor(final int paramInt1, final int paramInt2)
/*      */   {
/* 4018 */     if (has(64))
/* 4019 */       return new CharProperty(paramInt1) {
/*      */         boolean isSatisfiedBy(int paramAnonymousInt) {
/* 4021 */           if (Pattern.inRange(paramInt1, paramAnonymousInt, paramInt2))
/* 4022 */             return true;
/* 4023 */           int i = Character.toUpperCase(paramAnonymousInt);
/* 4024 */           return (Pattern.inRange(paramInt1, i, paramInt2)) || (Pattern.inRange(paramInt1, Character.toLowerCase(i), paramInt2));
/*      */         } } ;
/* 4026 */     return new CharProperty(paramInt1) {
/*      */       boolean isSatisfiedBy(int paramAnonymousInt) {
/* 4028 */         return (Pattern.inRange(paramInt1, paramAnonymousInt, paramInt2)) || ((ASCII.isAscii(paramAnonymousInt)) && ((Pattern.inRange(paramInt1, ASCII.toUpper(paramAnonymousInt), paramInt2)) || (Pattern.inRange(paramInt1, ASCII.toLower(paramAnonymousInt), paramInt2))));
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private static CharProperty union(CharProperty paramCharProperty1, final CharProperty paramCharProperty2)
/*      */   {
/* 5149 */     return new CharProperty(paramCharProperty1) {
/*      */       boolean isSatisfiedBy(int paramAnonymousInt) {
/* 5151 */         return (this.val$lhs.isSatisfiedBy(paramAnonymousInt)) || (paramCharProperty2.isSatisfiedBy(paramAnonymousInt));
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private static CharProperty intersection(CharProperty paramCharProperty1, final CharProperty paramCharProperty2)
/*      */   {
/* 5159 */     return new CharProperty(paramCharProperty1) {
/*      */       boolean isSatisfiedBy(int paramAnonymousInt) {
/* 5161 */         return (this.val$lhs.isSatisfiedBy(paramAnonymousInt)) && (paramCharProperty2.isSatisfiedBy(paramAnonymousInt));
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private static CharProperty setDifference(final CharProperty paramCharProperty1, CharProperty paramCharProperty2)
/*      */   {
/* 5169 */     return new CharProperty(paramCharProperty2) {
/*      */       boolean isSatisfiedBy(int paramAnonymousInt) {
/* 5171 */         return (!this.val$rhs.isSatisfiedBy(paramAnonymousInt)) && (paramCharProperty1.isSatisfiedBy(paramAnonymousInt));
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private static boolean hasBaseCharacter(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */   {
/* 5240 */     int i = !paramMatcher.transparentBounds ? paramMatcher.from : 0;
/*      */ 
/* 5242 */     for (int j = paramInt; j >= i; j--) {
/* 5243 */       int k = Character.codePointAt(paramCharSequence, j);
/* 5244 */       if (Character.isLetterOrDigit(k))
/* 5245 */         return true;
/* 5246 */       if (Character.getType(k) != 6)
/*      */       {
/* 5248 */         return false;
/*      */       }
/*      */     }
/* 5250 */     return false;
/*      */   }
/*      */ 
/*      */   static final class All extends Pattern.CharProperty
/*      */   {
/*      */     All()
/*      */     {
/* 4039 */       super();
/*      */     }
/* 4041 */     boolean isSatisfiedBy(int paramInt) { return true; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class BackRef extends Pattern.Node
/*      */   {
/*      */     int groupIndex;
/*      */ 
/*      */     BackRef(int paramInt)
/*      */     {
/* 4786 */       this.groupIndex = (paramInt + paramInt);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4789 */       int i = paramMatcher.groups[this.groupIndex];
/* 4790 */       int j = paramMatcher.groups[(this.groupIndex + 1)];
/*      */ 
/* 4792 */       int k = j - i;
/*      */ 
/* 4795 */       if (i < 0) {
/* 4796 */         return false;
/*      */       }
/*      */ 
/* 4799 */       if (paramInt + k > paramMatcher.to) {
/* 4800 */         paramMatcher.hitEnd = true;
/* 4801 */         return false;
/*      */       }
/*      */ 
/* 4806 */       for (int m = 0; m < k; m++) {
/* 4807 */         if (paramCharSequence.charAt(paramInt + m) != paramCharSequence.charAt(i + m))
/* 4808 */           return false;
/*      */       }
/* 4810 */       return this.next.match(paramMatcher, paramInt + k, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4813 */       paramTreeInfo.maxValid = false;
/* 4814 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Begin extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3470 */       int i = paramMatcher.anchoringBounds ? paramMatcher.from : 0;
/*      */ 
/* 3472 */       if ((paramInt == i) && (this.next.match(paramMatcher, paramInt, paramCharSequence))) {
/* 3473 */         paramMatcher.first = paramInt;
/* 3474 */         paramMatcher.groups[0] = paramInt;
/* 3475 */         paramMatcher.groups[1] = paramMatcher.last;
/* 3476 */         return true;
/*      */       }
/* 3478 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Behind extends Pattern.Node
/*      */   {
/*      */     Pattern.Node cond;
/*      */     int rmax;
/*      */     int rmin;
/*      */ 
/*      */     Behind(Pattern.Node paramNode, int paramInt1, int paramInt2)
/*      */     {
/* 5018 */       this.cond = paramNode;
/* 5019 */       this.rmax = paramInt1;
/* 5020 */       this.rmin = paramInt2;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5024 */       int i = paramMatcher.from;
/* 5025 */       boolean bool = false;
/* 5026 */       int j = !paramMatcher.transparentBounds ? paramMatcher.from : 0;
/*      */ 
/* 5028 */       int k = Math.max(paramInt - this.rmax, j);
/*      */ 
/* 5030 */       int m = paramMatcher.lookbehindTo;
/* 5031 */       paramMatcher.lookbehindTo = paramInt;
/*      */ 
/* 5033 */       if (paramMatcher.transparentBounds)
/* 5034 */         paramMatcher.from = 0;
/* 5035 */       for (int n = paramInt - this.rmin; (!bool) && (n >= k); n--) {
/* 5036 */         bool = this.cond.match(paramMatcher, n, paramCharSequence);
/*      */       }
/* 5038 */       paramMatcher.from = i;
/* 5039 */       paramMatcher.lookbehindTo = m;
/* 5040 */       return (bool) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class BehindS extends Pattern.Behind
/*      */   {
/*      */     BehindS(Pattern.Node paramNode, int paramInt1, int paramInt2)
/*      */     {
/* 5050 */       super(paramInt1, paramInt2);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5053 */       int i = Pattern.countChars(paramCharSequence, paramInt, -this.rmax);
/* 5054 */       int j = Pattern.countChars(paramCharSequence, paramInt, -this.rmin);
/* 5055 */       int k = paramMatcher.from;
/* 5056 */       int m = !paramMatcher.transparentBounds ? paramMatcher.from : 0;
/*      */ 
/* 5058 */       boolean bool = false;
/* 5059 */       int n = Math.max(paramInt - i, m);
/*      */ 
/* 5061 */       int i1 = paramMatcher.lookbehindTo;
/* 5062 */       paramMatcher.lookbehindTo = paramInt;
/*      */ 
/* 5064 */       if (paramMatcher.transparentBounds) {
/* 5065 */         paramMatcher.from = 0;
/*      */       }
/* 5067 */       int i2 = paramInt - j;
/*      */ 
/* 5069 */       for (; (!bool) && (i2 >= n); 
/* 5069 */         i2 -= (i2 > n ? Pattern.countChars(paramCharSequence, i2, -1) : 1)) {
/* 5070 */         bool = this.cond.match(paramMatcher, i2, paramCharSequence);
/*      */       }
/* 5072 */       paramMatcher.from = k;
/* 5073 */       paramMatcher.lookbehindTo = i1;
/* 5074 */       return (bool) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class BitClass extends Pattern.BmpCharProperty
/*      */   {
/*      */     final boolean[] bits;
/*      */ 
/*      */     BitClass()
/*      */     {
/* 3264 */       super(); this.bits = new boolean[256]; } 
/* 3265 */     private BitClass(boolean[] paramArrayOfBoolean) { super(); this.bits = paramArrayOfBoolean; } 
/*      */     BitClass add(int paramInt1, int paramInt2) {
/* 3267 */       assert ((paramInt1 >= 0) && (paramInt1 <= 255));
/* 3268 */       if ((paramInt2 & 0x2) != 0) {
/* 3269 */         if (ASCII.isAscii(paramInt1)) {
/* 3270 */           this.bits[ASCII.toUpper(paramInt1)] = true;
/* 3271 */           this.bits[ASCII.toLower(paramInt1)] = true;
/* 3272 */         } else if ((paramInt2 & 0x40) != 0) {
/* 3273 */           this.bits[Character.toLowerCase(paramInt1)] = true;
/* 3274 */           this.bits[Character.toUpperCase(paramInt1)] = true;
/*      */         }
/*      */       }
/* 3277 */       this.bits[paramInt1] = true;
/* 3278 */       return this;
/*      */     }
/*      */     boolean isSatisfiedBy(int paramInt) {
/* 3281 */       return (paramInt < 256) && (this.bits[paramInt] != 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Block extends Pattern.CharProperty
/*      */   {
/*      */     final Character.UnicodeBlock block;
/*      */ 
/*      */     Block(Character.UnicodeBlock paramUnicodeBlock)
/*      */     {
/* 3781 */       super();
/* 3782 */       this.block = paramUnicodeBlock;
/*      */     }
/*      */     boolean isSatisfiedBy(int paramInt) {
/* 3785 */       return this.block == Character.UnicodeBlock.of(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract class BmpCharProperty extends Pattern.CharProperty
/*      */   {
/*      */     private BmpCharProperty()
/*      */     {
/* 3712 */       super();
/*      */     }
/* 3714 */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) { if (paramInt < paramMatcher.to) {
/* 3715 */         return (isSatisfiedBy(paramCharSequence.charAt(paramInt))) && (this.next.match(paramMatcher, paramInt + 1, paramCharSequence));
/*      */       }
/*      */ 
/* 3718 */       paramMatcher.hitEnd = true;
/* 3719 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BnM extends Pattern.Node
/*      */   {
/*      */     int[] buffer;
/*      */     int[] lastOcc;
/*      */     int[] optoSft;
/*      */ 
/*      */     static Pattern.Node optimize(Pattern.Node paramNode)
/*      */     {
/* 5294 */       if (!(paramNode instanceof Pattern.Slice)) {
/* 5295 */         return paramNode;
/*      */       }
/*      */ 
/* 5298 */       int[] arrayOfInt1 = ((Pattern.Slice)paramNode).buffer;
/* 5299 */       int i = arrayOfInt1.length;
/*      */ 
/* 5304 */       if (i < 4) {
/* 5305 */         return paramNode;
/*      */       }
/*      */ 
/* 5308 */       int[] arrayOfInt2 = new int[''];
/* 5309 */       int[] arrayOfInt3 = new int[i];
/*      */ 
/* 5313 */       for (int j = 0; j < i; j++) {
/* 5314 */         arrayOfInt2[(arrayOfInt1[j] & 0x7F)] = (j + 1);
/*      */       }
/*      */ 
/* 5318 */       label125: for (j = i; j > 0; j--)
/*      */       {
/* 5320 */         for (int k = i - 1; k >= j; k--)
/*      */         {
/* 5322 */           if (arrayOfInt1[k] != arrayOfInt1[(k - j)])
/*      */             break label125;
/* 5324 */           arrayOfInt3[(k - 1)] = j;
/*      */         }
/*      */ 
/* 5334 */         while (k > 0) {
/* 5335 */           arrayOfInt3[(--k)] = j;
/*      */         }
/*      */       }
/*      */ 
/* 5339 */       arrayOfInt3[(i - 1)] = 1;
/* 5340 */       if ((paramNode instanceof Pattern.SliceS))
/* 5341 */         return new Pattern.BnMS(arrayOfInt1, arrayOfInt2, arrayOfInt3, paramNode.next);
/* 5342 */       return new BnM(arrayOfInt1, arrayOfInt2, arrayOfInt3, paramNode.next);
/*      */     }
/*      */     BnM(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, Pattern.Node paramNode) {
/* 5345 */       this.buffer = paramArrayOfInt1;
/* 5346 */       this.lastOcc = paramArrayOfInt2;
/* 5347 */       this.optoSft = paramArrayOfInt3;
/* 5348 */       this.next = paramNode;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5351 */       int[] arrayOfInt = this.buffer;
/* 5352 */       int i = arrayOfInt.length;
/* 5353 */       int j = paramMatcher.to - i;
/*      */ 
/* 5356 */       while (paramInt <= j)
/*      */       {
/* 5358 */         for (int k = i - 1; ; k--) { if (k < 0) break label96;
/* 5359 */           int m = paramCharSequence.charAt(paramInt + k);
/* 5360 */           if (m != arrayOfInt[k])
/*      */           {
/* 5363 */             paramInt += Math.max(k + 1 - this.lastOcc[(m & 0x7F)], this.optoSft[k]);
/* 5364 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 5368 */         label96: paramMatcher.first = paramInt;
/* 5369 */         boolean bool = this.next.match(paramMatcher, paramInt + i, paramCharSequence);
/* 5370 */         if (bool) {
/* 5371 */           paramMatcher.first = paramInt;
/* 5372 */           paramMatcher.groups[0] = paramMatcher.first;
/* 5373 */           paramMatcher.groups[1] = paramMatcher.last;
/* 5374 */           return true;
/*      */         }
/* 5376 */         paramInt++;
/*      */       }
/*      */ 
/* 5381 */       paramMatcher.hitEnd = true;
/* 5382 */       return false;
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 5385 */       paramTreeInfo.minLength += this.buffer.length;
/* 5386 */       paramTreeInfo.maxValid = false;
/* 5387 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class BnMS extends Pattern.BnM
/*      */   {
/*      */     int lengthInChars;
/*      */ 
/*      */     BnMS(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, Pattern.Node paramNode)
/*      */     {
/* 5399 */       super(paramArrayOfInt2, paramArrayOfInt3, paramNode);
/* 5400 */       for (int i = 0; i < this.buffer.length; i++)
/* 5401 */         this.lengthInChars += Character.charCount(this.buffer[i]);
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5405 */       int[] arrayOfInt = this.buffer;
/* 5406 */       int i = arrayOfInt.length;
/* 5407 */       int j = paramMatcher.to - this.lengthInChars;
/*      */ 
/* 5410 */       while (paramInt <= j)
/*      */       {
/* 5413 */         int m = Pattern.countChars(paramCharSequence, paramInt, i); int n = i - 1;
/*      */         while (true) { if (m <= 0) break label124;
/* 5415 */           int k = Character.codePointBefore(paramCharSequence, paramInt + m);
/* 5416 */           if (k != arrayOfInt[n])
/*      */           {
/* 5419 */             int i1 = Math.max(n + 1 - this.lastOcc[(k & 0x7F)], this.optoSft[n]);
/* 5420 */             paramInt += Pattern.countChars(paramCharSequence, paramInt, i1);
/* 5421 */             break;
/*      */           }
/* 5414 */           m -= Character.charCount(k); n--;
/*      */         }
/*      */ 
/* 5425 */         label124: paramMatcher.first = paramInt;
/* 5426 */         boolean bool = this.next.match(paramMatcher, paramInt + this.lengthInChars, paramCharSequence);
/* 5427 */         if (bool) {
/* 5428 */           paramMatcher.first = paramInt;
/* 5429 */           paramMatcher.groups[0] = paramMatcher.first;
/* 5430 */           paramMatcher.groups[1] = paramMatcher.last;
/* 5431 */           return true;
/*      */         }
/* 5433 */         paramInt += Pattern.countChars(paramCharSequence, paramInt, 1);
/*      */       }
/* 5435 */       paramMatcher.hitEnd = true;
/* 5436 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Bound extends Pattern.Node
/*      */   {
/* 5182 */     static int LEFT = 1;
/* 5183 */     static int RIGHT = 2;
/* 5184 */     static int BOTH = 3;
/* 5185 */     static int NONE = 4;
/*      */     int type;
/*      */     boolean useUWORD;
/*      */ 
/*      */     Bound(int paramInt, boolean paramBoolean)
/*      */     {
/* 5189 */       this.type = paramInt;
/* 5190 */       this.useUWORD = paramBoolean;
/*      */     }
/*      */ 
/*      */     boolean isWord(int paramInt) {
/* 5194 */       return (paramInt == 95) || (Character.isLetterOrDigit(paramInt)) ? true : this.useUWORD ? UnicodeProp.WORD.is(paramInt) : false;
/*      */     }
/*      */ 
/*      */     int check(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 5200 */       int j = 0;
/* 5201 */       int k = paramMatcher.from;
/* 5202 */       int m = paramMatcher.to;
/* 5203 */       if (paramMatcher.transparentBounds) {
/* 5204 */         k = 0;
/* 5205 */         m = paramMatcher.getTextLength();
/*      */       }
/*      */       int i;
/* 5207 */       if (paramInt > k) {
/* 5208 */         i = Character.codePointBefore(paramCharSequence, paramInt);
/* 5209 */         j = (isWord(i)) || ((Character.getType(i) == 6) && (Pattern.hasBaseCharacter(paramMatcher, paramInt - 1, paramCharSequence))) ? 1 : 0;
/*      */       }
/*      */ 
/* 5213 */       int n = 0;
/* 5214 */       if (paramInt < m) {
/* 5215 */         i = Character.codePointAt(paramCharSequence, paramInt);
/* 5216 */         n = (isWord(i)) || ((Character.getType(i) == 6) && (Pattern.hasBaseCharacter(paramMatcher, paramInt, paramCharSequence))) ? 1 : 0;
/*      */       }
/*      */       else
/*      */       {
/* 5221 */         paramMatcher.hitEnd = true;
/*      */ 
/* 5223 */         paramMatcher.requireEnd = true;
/*      */       }
/* 5225 */       return (j ^ n) != 0 ? RIGHT : n != 0 ? LEFT : NONE;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5228 */       return ((check(paramMatcher, paramInt, paramCharSequence) & this.type) > 0) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Branch extends Pattern.Node
/*      */   {
/* 4479 */     Pattern.Node[] atoms = new Pattern.Node[2];
/* 4480 */     int size = 2;
/*      */     Pattern.Node conn;
/*      */ 
/*      */     Branch(Pattern.Node paramNode1, Pattern.Node paramNode2, Pattern.Node paramNode3)
/*      */     {
/* 4483 */       this.conn = paramNode3;
/* 4484 */       this.atoms[0] = paramNode1;
/* 4485 */       this.atoms[1] = paramNode2;
/*      */     }
/*      */ 
/*      */     void add(Pattern.Node paramNode) {
/* 4489 */       if (this.size >= this.atoms.length) {
/* 4490 */         Pattern.Node[] arrayOfNode = new Pattern.Node[this.atoms.length * 2];
/* 4491 */         System.arraycopy(this.atoms, 0, arrayOfNode, 0, this.atoms.length);
/* 4492 */         this.atoms = arrayOfNode;
/*      */       }
/* 4494 */       this.atoms[(this.size++)] = paramNode;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4498 */       for (int i = 0; i < this.size; i++) {
/* 4499 */         if (this.atoms[i] == null) {
/* 4500 */           if (this.conn.next.match(paramMatcher, paramInt, paramCharSequence))
/* 4501 */             return true;
/* 4502 */         } else if (this.atoms[i].match(paramMatcher, paramInt, paramCharSequence)) {
/* 4503 */           return true;
/*      */         }
/*      */       }
/* 4506 */       return false;
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4510 */       int i = paramTreeInfo.minLength;
/* 4511 */       int j = paramTreeInfo.maxLength;
/* 4512 */       boolean bool = paramTreeInfo.maxValid;
/*      */ 
/* 4514 */       int k = 2147483647;
/* 4515 */       int m = -1;
/* 4516 */       for (int n = 0; n < this.size; n++) {
/* 4517 */         paramTreeInfo.reset();
/* 4518 */         if (this.atoms[n] != null)
/* 4519 */           this.atoms[n].study(paramTreeInfo);
/* 4520 */         k = Math.min(k, paramTreeInfo.minLength);
/* 4521 */         m = Math.max(m, paramTreeInfo.maxLength);
/* 4522 */         bool &= paramTreeInfo.maxValid;
/*      */       }
/*      */ 
/* 4525 */       i += k;
/* 4526 */       j += m;
/*      */ 
/* 4528 */       paramTreeInfo.reset();
/* 4529 */       this.conn.next.study(paramTreeInfo);
/*      */ 
/* 4531 */       paramTreeInfo.minLength += i;
/* 4532 */       paramTreeInfo.maxLength += j;
/* 4533 */       paramTreeInfo.maxValid &= bool;
/* 4534 */       paramTreeInfo.deterministic = false;
/* 4535 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class BranchConn extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 4466 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4469 */       return paramTreeInfo.deterministic;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CIBackRef extends Pattern.Node
/*      */   {
/*      */     int groupIndex;
/*      */     boolean doUnicodeCase;
/*      */ 
/*      */     CIBackRef(int paramInt, boolean paramBoolean)
/*      */     {
/* 4823 */       this.groupIndex = (paramInt + paramInt);
/* 4824 */       this.doUnicodeCase = paramBoolean;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4827 */       int i = paramMatcher.groups[this.groupIndex];
/* 4828 */       int j = paramMatcher.groups[(this.groupIndex + 1)];
/*      */ 
/* 4830 */       int k = j - i;
/*      */ 
/* 4833 */       if (i < 0) {
/* 4834 */         return false;
/*      */       }
/*      */ 
/* 4837 */       if (paramInt + k > paramMatcher.to) {
/* 4838 */         paramMatcher.hitEnd = true;
/* 4839 */         return false;
/*      */       }
/*      */ 
/* 4844 */       int m = paramInt;
/* 4845 */       for (int n = 0; n < k; n++) {
/* 4846 */         int i1 = Character.codePointAt(paramCharSequence, m);
/* 4847 */         int i2 = Character.codePointAt(paramCharSequence, i);
/* 4848 */         if (i1 != i2) {
/* 4849 */           if (this.doUnicodeCase) {
/* 4850 */             int i3 = Character.toUpperCase(i1);
/* 4851 */             int i4 = Character.toUpperCase(i2);
/* 4852 */             if ((i3 != i4) && (Character.toLowerCase(i3) != Character.toLowerCase(i4)))
/*      */             {
/* 4855 */               return false;
/*      */             }
/* 4857 */           } else if (ASCII.toLower(i1) != ASCII.toLower(i2)) {
/* 4858 */             return false;
/*      */           }
/*      */         }
/* 4861 */         m += Character.charCount(i1);
/* 4862 */         i += Character.charCount(i2);
/*      */       }
/*      */ 
/* 4865 */       return this.next.match(paramMatcher, paramInt + k, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4868 */       paramTreeInfo.maxValid = false;
/* 4869 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Caret extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3505 */       int i = paramMatcher.from;
/* 3506 */       int j = paramMatcher.to;
/* 3507 */       if (!paramMatcher.anchoringBounds) {
/* 3508 */         i = 0;
/* 3509 */         j = paramMatcher.getTextLength();
/*      */       }
/*      */ 
/* 3512 */       if (paramInt == j) {
/* 3513 */         paramMatcher.hitEnd = true;
/* 3514 */         return false;
/*      */       }
/* 3516 */       if (paramInt > i) {
/* 3517 */         int k = paramCharSequence.charAt(paramInt - 1);
/* 3518 */         if ((k != 10) && (k != 13) && ((k | 0x1) != 8233) && (k != 133))
/*      */         {
/* 3521 */           return false;
/*      */         }
/*      */ 
/* 3524 */         if ((k == 13) && (paramCharSequence.charAt(paramInt) == '\n'))
/* 3525 */           return false;
/*      */       }
/* 3527 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Category extends Pattern.CharProperty
/*      */   {
/*      */     final int typeMask;
/*      */ 
/*      */     Category(int paramInt)
/*      */     {
/* 3807 */       super(); this.typeMask = paramInt;
/*      */     }
/* 3809 */     boolean isSatisfiedBy(int paramInt) { return (this.typeMask & 1 << Character.getType(paramInt)) != 0; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static abstract class CharProperty extends Pattern.Node
/*      */   {
/*      */     abstract boolean isSatisfiedBy(int paramInt);
/*      */ 
/*      */     CharProperty complement()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: new 37	java/util/regex/Pattern$CharProperty$1
/*      */       //   3: dup
/*      */       //   4: aload_0
/*      */       //   5: invokespecial 68	java/util/regex/Pattern$CharProperty$1:<init>	(Ljava/util/regex/Pattern$CharProperty;)V
/*      */       //   8: areturn
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3692 */       if (paramInt < paramMatcher.to) {
/* 3693 */         int i = Character.codePointAt(paramCharSequence, paramInt);
/* 3694 */         return (isSatisfiedBy(i)) && (this.next.match(paramMatcher, paramInt + Character.charCount(i), paramCharSequence));
/*      */       }
/*      */ 
/* 3697 */       paramMatcher.hitEnd = true;
/* 3698 */       return false;
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 3702 */       paramTreeInfo.minLength += 1;
/* 3703 */       paramTreeInfo.maxLength += 1;
/* 3704 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CharPropertyNames
/*      */   {
/* 5497 */     private static final HashMap<String, CharPropertyFactory> map = new HashMap();
/*      */ 
/*      */     static Pattern.CharProperty charPropertyFor(String paramString)
/*      */     {
/* 5453 */       CharPropertyFactory localCharPropertyFactory = (CharPropertyFactory)map.get(paramString);
/* 5454 */       return localCharPropertyFactory == null ? null : localCharPropertyFactory.make();
/*      */     }
/*      */ 
/*      */     private static void defCategory(String paramString, int paramInt)
/*      */     {
/* 5463 */       map.put(paramString, new CharPropertyFactory(paramInt) {
/* 5464 */         Pattern.CharProperty make() { return new Pattern.Category(this.val$typeMask); }
/*      */       });
/*      */     }
/*      */ 
/*      */     private static void defRange(String paramString, int paramInt1, final int paramInt2) {
/* 5469 */       map.put(paramString, new CharPropertyFactory(paramInt1) {
/* 5470 */         Pattern.CharProperty make() { return Pattern.rangeFor(this.val$lower, paramInt2); }
/*      */       });
/*      */     }
/*      */ 
/*      */     private static void defCtype(String paramString, int paramInt) {
/* 5475 */       map.put(paramString, new CharPropertyFactory(paramInt) {
/* 5476 */         Pattern.CharProperty make() { return new Pattern.Ctype(this.val$ctype); }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*      */     private static void defClone(String paramString, CloneableProperty paramCloneableProperty)
/*      */     {
/* 5493 */       map.put(paramString, new CharPropertyFactory(paramCloneableProperty) {
/* 5494 */         Pattern.CharProperty make() { return this.val$p.clone(); }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       // Byte code:
/*      */       //   0: new 215	java/util/HashMap
/*      */       //   3: dup
/*      */       //   4: invokespecial 267	java/util/HashMap:<init>	()V
/*      */       //   7: putstatic 265	java/util/regex/Pattern$CharPropertyNames:map	Ljava/util/HashMap;
/*      */       //   10: ldc 26
/*      */       //   12: iconst_1
/*      */       //   13: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   16: ldc 41
/*      */       //   18: iconst_2
/*      */       //   19: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   22: ldc 36
/*      */       //   24: iconst_4
/*      */       //   25: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   28: ldc 40
/*      */       //   30: bipush 8
/*      */       //   32: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   35: ldc 37
/*      */       //   37: bipush 16
/*      */       //   39: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   42: ldc 38
/*      */       //   44: bipush 32
/*      */       //   46: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   49: ldc 45
/*      */       //   51: bipush 64
/*      */       //   53: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   56: ldc 44
/*      */       //   58: sipush 128
/*      */       //   61: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   64: ldc 43
/*      */       //   66: sipush 256
/*      */       //   69: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   72: ldc 47
/*      */       //   74: sipush 512
/*      */       //   77: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   80: ldc 48
/*      */       //   82: sipush 1024
/*      */       //   85: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   88: ldc 49
/*      */       //   90: sipush 2048
/*      */       //   93: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   96: ldc 71
/*      */       //   98: sipush 4096
/*      */       //   101: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   104: ldc 69
/*      */       //   106: sipush 8192
/*      */       //   109: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   112: ldc 70
/*      */       //   114: sipush 16384
/*      */       //   117: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   120: ldc 24
/*      */       //   122: ldc 1
/*      */       //   124: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   127: ldc 25
/*      */       //   129: ldc 2
/*      */       //   131: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   134: ldc 28
/*      */       //   136: ldc 3
/*      */       //   138: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   141: ldc 29
/*      */       //   143: ldc 4
/*      */       //   145: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   148: ldc 52
/*      */       //   150: ldc 6
/*      */       //   152: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   155: ldc 58
/*      */       //   157: ldc 7
/*      */       //   159: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   162: ldc 53
/*      */       //   164: ldc 8
/*      */       //   166: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   169: ldc 51
/*      */       //   171: ldc 9
/*      */       //   173: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   176: ldc 56
/*      */       //   178: ldc 10
/*      */       //   180: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   183: ldc 63
/*      */       //   185: ldc 11
/*      */       //   187: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   190: ldc 61
/*      */       //   192: ldc 12
/*      */       //   194: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   197: ldc 62
/*      */       //   199: ldc 13
/*      */       //   201: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   204: ldc 64
/*      */       //   206: ldc 14
/*      */       //   208: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   211: ldc 55
/*      */       //   213: ldc 16
/*      */       //   215: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   218: ldc 54
/*      */       //   220: ldc 17
/*      */       //   222: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   225: ldc 32
/*      */       //   227: bipush 62
/*      */       //   229: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   232: ldc 42
/*      */       //   234: sipush 448
/*      */       //   237: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   240: ldc 46
/*      */       //   242: sipush 3584
/*      */       //   245: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   248: ldc 68
/*      */       //   250: sipush 28672
/*      */       //   253: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   256: ldc 23
/*      */       //   258: ldc 5
/*      */       //   260: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   263: ldc 50
/*      */       //   265: ldc 18
/*      */       //   267: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   270: ldc 60
/*      */       //   272: ldc 15
/*      */       //   274: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   277: ldc 34
/*      */       //   279: bipush 14
/*      */       //   281: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   284: ldc 35
/*      */       //   286: sipush 574
/*      */       //   289: invokestatic 270	java/util/regex/Pattern$CharPropertyNames:defCategory	(Ljava/lang/String;I)V
/*      */       //   292: ldc 33
/*      */       //   294: iconst_0
/*      */       //   295: sipush 255
/*      */       //   298: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   301: getstatic 265	java/util/regex/Pattern$CharPropertyNames:map	Ljava/util/HashMap;
/*      */       //   304: ldc 72
/*      */       //   306: new 237	java/util/regex/Pattern$CharPropertyNames$5
/*      */       //   309: dup
/*      */       //   310: invokespecial 292	java/util/regex/Pattern$CharPropertyNames$5:<init>	()V
/*      */       //   313: invokevirtual 269	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*      */       //   316: pop
/*      */       //   317: ldc 19
/*      */       //   319: iconst_0
/*      */       //   320: bipush 127
/*      */       //   322: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   325: ldc 20
/*      */       //   327: sipush 1792
/*      */       //   330: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   333: ldc 21
/*      */       //   335: sipush 768
/*      */       //   338: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   341: ldc 22
/*      */       //   343: sipush 16384
/*      */       //   346: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   349: ldc 27
/*      */       //   351: sipush 8192
/*      */       //   354: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   357: ldc 30
/*      */       //   359: bipush 48
/*      */       //   361: bipush 57
/*      */       //   363: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   366: ldc 31
/*      */       //   368: sipush 5888
/*      */       //   371: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   374: ldc 39
/*      */       //   376: bipush 97
/*      */       //   378: bipush 122
/*      */       //   380: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   383: ldc 57
/*      */       //   385: bipush 32
/*      */       //   387: bipush 126
/*      */       //   389: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   392: ldc 59
/*      */       //   394: sipush 4096
/*      */       //   397: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   400: ldc 65
/*      */       //   402: sipush 2048
/*      */       //   405: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   408: ldc 66
/*      */       //   410: bipush 65
/*      */       //   412: bipush 90
/*      */       //   414: invokestatic 272	java/util/regex/Pattern$CharPropertyNames:defRange	(Ljava/lang/String;II)V
/*      */       //   417: ldc 67
/*      */       //   419: ldc 1
/*      */       //   421: invokestatic 271	java/util/regex/Pattern$CharPropertyNames:defCtype	(Ljava/lang/String;I)V
/*      */       //   424: ldc 83
/*      */       //   426: new 238	java/util/regex/Pattern$CharPropertyNames$6
/*      */       //   429: dup
/*      */       //   430: invokespecial 293	java/util/regex/Pattern$CharPropertyNames$6:<init>	()V
/*      */       //   433: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   436: ldc 89
/*      */       //   438: new 239	java/util/regex/Pattern$CharPropertyNames$7
/*      */       //   441: dup
/*      */       //   442: invokespecial 294	java/util/regex/Pattern$CharPropertyNames$7:<init>	()V
/*      */       //   445: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   448: ldc 73
/*      */       //   450: new 240	java/util/regex/Pattern$CharPropertyNames$8
/*      */       //   453: dup
/*      */       //   454: invokespecial 295	java/util/regex/Pattern$CharPropertyNames$8:<init>	()V
/*      */       //   457: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   460: ldc 78
/*      */       //   462: new 241	java/util/regex/Pattern$CharPropertyNames$9
/*      */       //   465: dup
/*      */       //   466: invokespecial 296	java/util/regex/Pattern$CharPropertyNames$9:<init>	()V
/*      */       //   469: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   472: ldc 86
/*      */       //   474: new 220	java/util/regex/Pattern$CharPropertyNames$10
/*      */       //   477: dup
/*      */       //   478: invokespecial 275	java/util/regex/Pattern$CharPropertyNames$10:<init>	()V
/*      */       //   481: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   484: ldc 75
/*      */       //   486: new 221	java/util/regex/Pattern$CharPropertyNames$11
/*      */       //   489: dup
/*      */       //   490: invokespecial 276	java/util/regex/Pattern$CharPropertyNames$11:<init>	()V
/*      */       //   493: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   496: ldc 74
/*      */       //   498: new 222	java/util/regex/Pattern$CharPropertyNames$12
/*      */       //   501: dup
/*      */       //   502: invokespecial 277	java/util/regex/Pattern$CharPropertyNames$12:<init>	()V
/*      */       //   505: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   508: ldc 81
/*      */       //   510: new 223	java/util/regex/Pattern$CharPropertyNames$13
/*      */       //   513: dup
/*      */       //   514: invokespecial 278	java/util/regex/Pattern$CharPropertyNames$13:<init>	()V
/*      */       //   517: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   520: ldc 82
/*      */       //   522: new 224	java/util/regex/Pattern$CharPropertyNames$14
/*      */       //   525: dup
/*      */       //   526: invokespecial 279	java/util/regex/Pattern$CharPropertyNames$14:<init>	()V
/*      */       //   529: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   532: ldc 80
/*      */       //   534: new 225	java/util/regex/Pattern$CharPropertyNames$15
/*      */       //   537: dup
/*      */       //   538: invokespecial 280	java/util/regex/Pattern$CharPropertyNames$15:<init>	()V
/*      */       //   541: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   544: ldc 79
/*      */       //   546: new 226	java/util/regex/Pattern$CharPropertyNames$16
/*      */       //   549: dup
/*      */       //   550: invokespecial 281	java/util/regex/Pattern$CharPropertyNames$16:<init>	()V
/*      */       //   553: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   556: ldc 88
/*      */       //   558: new 227	java/util/regex/Pattern$CharPropertyNames$17
/*      */       //   561: dup
/*      */       //   562: invokespecial 282	java/util/regex/Pattern$CharPropertyNames$17:<init>	()V
/*      */       //   565: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   568: ldc 87
/*      */       //   570: new 228	java/util/regex/Pattern$CharPropertyNames$18
/*      */       //   573: dup
/*      */       //   574: invokespecial 283	java/util/regex/Pattern$CharPropertyNames$18:<init>	()V
/*      */       //   577: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   580: ldc 77
/*      */       //   582: new 229	java/util/regex/Pattern$CharPropertyNames$19
/*      */       //   585: dup
/*      */       //   586: invokespecial 284	java/util/regex/Pattern$CharPropertyNames$19:<init>	()V
/*      */       //   589: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   592: ldc 85
/*      */       //   594: new 231	java/util/regex/Pattern$CharPropertyNames$20
/*      */       //   597: dup
/*      */       //   598: invokespecial 286	java/util/regex/Pattern$CharPropertyNames$20:<init>	()V
/*      */       //   601: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   604: ldc 90
/*      */       //   606: new 232	java/util/regex/Pattern$CharPropertyNames$21
/*      */       //   609: dup
/*      */       //   610: invokespecial 287	java/util/regex/Pattern$CharPropertyNames$21:<init>	()V
/*      */       //   613: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   616: ldc 76
/*      */       //   618: new 233	java/util/regex/Pattern$CharPropertyNames$22
/*      */       //   621: dup
/*      */       //   622: invokespecial 288	java/util/regex/Pattern$CharPropertyNames$22:<init>	()V
/*      */       //   625: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   628: ldc 84
/*      */       //   630: new 234	java/util/regex/Pattern$CharPropertyNames$23
/*      */       //   633: dup
/*      */       //   634: invokespecial 289	java/util/regex/Pattern$CharPropertyNames$23:<init>	()V
/*      */       //   637: invokestatic 273	java/util/regex/Pattern$CharPropertyNames:defClone	(Ljava/lang/String;Ljava/util/regex/Pattern$CharPropertyNames$CloneableProperty;)V
/*      */       //   640: return
/*      */     }
/*      */ 
/*      */     private static abstract class CharPropertyFactory
/*      */     {
/*      */       abstract Pattern.CharProperty make();
/*      */     }
/*      */ 
/*      */     private static abstract class CloneableProperty extends Pattern.CharProperty
/*      */       implements Cloneable
/*      */     {
/*      */       private CloneableProperty()
/*      */       {
/* 5479 */         super();
/*      */       }
/*      */ 
/*      */       public CloneableProperty clone() {
/*      */         try {
/* 5484 */           return (CloneableProperty)super.clone();
/*      */         } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 5486 */           throw new AssertionError(localCloneNotSupportedException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Conditional extends Pattern.Node
/*      */   {
/*      */     Pattern.Node cond;
/*      */     Pattern.Node yes;
/*      */     Pattern.Node not;
/*      */ 
/*      */     Conditional(Pattern.Node paramNode1, Pattern.Node paramNode2, Pattern.Node paramNode3)
/*      */     {
/* 4912 */       this.cond = paramNode1;
/* 4913 */       this.yes = paramNode2;
/* 4914 */       this.not = paramNode3;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4917 */       if (this.cond.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4918 */         return this.yes.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/* 4920 */       return this.not.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4924 */       int i = paramTreeInfo.minLength;
/* 4925 */       int j = paramTreeInfo.maxLength;
/* 4926 */       boolean bool1 = paramTreeInfo.maxValid;
/* 4927 */       paramTreeInfo.reset();
/* 4928 */       this.yes.study(paramTreeInfo);
/*      */ 
/* 4930 */       int k = paramTreeInfo.minLength;
/* 4931 */       int m = paramTreeInfo.maxLength;
/* 4932 */       boolean bool2 = paramTreeInfo.maxValid;
/* 4933 */       paramTreeInfo.reset();
/* 4934 */       this.not.study(paramTreeInfo);
/*      */ 
/* 4936 */       paramTreeInfo.minLength = (i + Math.min(k, paramTreeInfo.minLength));
/* 4937 */       paramTreeInfo.maxLength = (j + Math.max(m, paramTreeInfo.maxLength));
/* 4938 */       paramTreeInfo.maxValid = (bool1 & bool2 & paramTreeInfo.maxValid);
/* 4939 */       paramTreeInfo.deterministic = false;
/* 4940 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Ctype extends Pattern.BmpCharProperty
/*      */   {
/*      */     final int ctype;
/*      */ 
/*      */     Ctype(int paramInt)
/*      */     {
/* 3830 */       super(); this.ctype = paramInt;
/*      */     }
/* 3832 */     boolean isSatisfiedBy(int paramInt) { return (paramInt < 128) && (ASCII.isType(paramInt, this.ctype)); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class Curly extends Pattern.Node
/*      */   {
/*      */     Pattern.Node atom;
/*      */     int type;
/*      */     int cmin;
/*      */     int cmax;
/*      */ 
/*      */     Curly(Pattern.Node paramNode, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 4117 */       this.atom = paramNode;
/* 4118 */       this.type = paramInt3;
/* 4119 */       this.cmin = paramInt1;
/* 4120 */       this.cmax = paramInt2;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4124 */       for (int i = 0; i < this.cmin; i++) {
/* 4125 */         if (this.atom.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4126 */           paramInt = paramMatcher.last;
/*      */         }
/*      */         else
/* 4129 */           return false;
/*      */       }
/* 4131 */       if (this.type == 0)
/* 4132 */         return match0(paramMatcher, paramInt, i, paramCharSequence);
/* 4133 */       if (this.type == 1) {
/* 4134 */         return match1(paramMatcher, paramInt, i, paramCharSequence);
/*      */       }
/* 4136 */       return match2(paramMatcher, paramInt, i, paramCharSequence);
/*      */     }
/*      */ 
/*      */     boolean match0(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence)
/*      */     {
/* 4142 */       if (paramInt2 >= this.cmax)
/*      */       {
/* 4145 */         return this.next.match(paramMatcher, paramInt1, paramCharSequence);
/*      */       }
/* 4147 */       int i = paramInt2;
/* 4148 */       if (this.atom.match(paramMatcher, paramInt1, paramCharSequence))
/*      */       {
/* 4150 */         int j = paramMatcher.last - paramInt1;
/* 4151 */         if (j != 0)
/*      */         {
/* 4154 */           paramInt1 = paramMatcher.last;
/* 4155 */           paramInt2++;
/*      */ 
/* 4157 */           while ((paramInt2 < this.cmax) && 
/* 4158 */             (this.atom.match(paramMatcher, paramInt1, paramCharSequence)))
/*      */           {
/* 4160 */             if (paramInt1 + j != paramMatcher.last) {
/* 4161 */               if (!match0(paramMatcher, paramMatcher.last, paramInt2 + 1, paramCharSequence)) break;
/* 4162 */               return true;
/*      */             }
/*      */ 
/* 4165 */             paramInt1 += j;
/* 4166 */             paramInt2++;
/*      */           }
/*      */ 
/* 4169 */           while (paramInt2 >= i) {
/* 4170 */             if (this.next.match(paramMatcher, paramInt1, paramCharSequence))
/* 4171 */               return true;
/* 4172 */             paramInt1 -= j;
/* 4173 */             paramInt2--;
/*      */           }
/* 4175 */           return false;
/*      */         }
/*      */       }
/* 4177 */       return this.next.match(paramMatcher, paramInt1, paramCharSequence);
/*      */     }
/*      */ 
/*      */     boolean match1(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence)
/*      */     {
/*      */       while (true)
/*      */       {
/* 4185 */         if (this.next.match(paramMatcher, paramInt1, paramCharSequence)) {
/* 4186 */           return true;
/*      */         }
/* 4188 */         if (paramInt2 >= this.cmax) {
/* 4189 */           return false;
/*      */         }
/* 4191 */         if (!this.atom.match(paramMatcher, paramInt1, paramCharSequence)) {
/* 4192 */           return false;
/*      */         }
/* 4194 */         if (paramInt1 == paramMatcher.last) {
/* 4195 */           return false;
/*      */         }
/* 4197 */         paramInt1 = paramMatcher.last;
/* 4198 */         paramInt2++;
/*      */       }
/*      */     }
/*      */ 
/* 4202 */     boolean match2(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence) { for (; (paramInt2 < this.cmax) && 
/* 4203 */         (this.atom.match(paramMatcher, paramInt1, paramCharSequence)); paramInt2++)
/*      */       {
/* 4205 */         if (paramInt1 == paramMatcher.last)
/*      */           break;
/* 4207 */         paramInt1 = paramMatcher.last;
/*      */       }
/* 4209 */       return this.next.match(paramMatcher, paramInt1, paramCharSequence); }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo)
/*      */     {
/* 4213 */       int i = paramTreeInfo.minLength;
/* 4214 */       int j = paramTreeInfo.maxLength;
/* 4215 */       boolean bool1 = paramTreeInfo.maxValid;
/* 4216 */       boolean bool2 = paramTreeInfo.deterministic;
/* 4217 */       paramTreeInfo.reset();
/*      */ 
/* 4219 */       this.atom.study(paramTreeInfo);
/*      */ 
/* 4221 */       int k = paramTreeInfo.minLength * this.cmin + i;
/* 4222 */       if (k < i) {
/* 4223 */         k = 268435455;
/*      */       }
/* 4225 */       paramTreeInfo.minLength = k;
/*      */ 
/* 4227 */       if ((bool1 & paramTreeInfo.maxValid)) {
/* 4228 */         k = paramTreeInfo.maxLength * this.cmax + j;
/* 4229 */         paramTreeInfo.maxLength = k;
/* 4230 */         if (k < j)
/* 4231 */           paramTreeInfo.maxValid = false;
/*      */       }
/*      */       else {
/* 4234 */         paramTreeInfo.maxValid = false;
/*      */       }
/*      */ 
/* 4237 */       if ((paramTreeInfo.deterministic) && (this.cmin == this.cmax))
/* 4238 */         paramTreeInfo.deterministic = bool2;
/*      */       else {
/* 4240 */         paramTreeInfo.deterministic = false;
/*      */       }
/* 4242 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Dollar extends Pattern.Node
/*      */   {
/*      */     boolean multiline;
/*      */ 
/*      */     Dollar(boolean paramBoolean)
/*      */     {
/* 3585 */       this.multiline = paramBoolean;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3588 */       int i = paramMatcher.anchoringBounds ? paramMatcher.to : paramMatcher.getTextLength();
/*      */       int j;
/* 3590 */       if (!this.multiline) {
/* 3591 */         if (paramInt < i - 2)
/* 3592 */           return false;
/* 3593 */         if (paramInt == i - 2) {
/* 3594 */           j = paramCharSequence.charAt(paramInt);
/* 3595 */           if (j != 13)
/* 3596 */             return false;
/* 3597 */           j = paramCharSequence.charAt(paramInt + 1);
/* 3598 */           if (j != 10) {
/* 3599 */             return false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3610 */       if (paramInt < i) {
/* 3611 */         j = paramCharSequence.charAt(paramInt);
/* 3612 */         if (j == 10)
/*      */         {
/* 3614 */           if ((paramInt > 0) && (paramCharSequence.charAt(paramInt - 1) == '\r'))
/* 3615 */             return false;
/* 3616 */           if (this.multiline)
/* 3617 */             return this.next.match(paramMatcher, paramInt, paramCharSequence);
/* 3618 */         } else if ((j == 13) || (j == 133) || ((j | 0x1) == 8233))
/*      */         {
/* 3620 */           if (this.multiline)
/* 3621 */             return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */         } else {
/* 3623 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 3627 */       paramMatcher.hitEnd = true;
/*      */ 
/* 3630 */       paramMatcher.requireEnd = true;
/* 3631 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 3634 */       this.next.study(paramTreeInfo);
/* 3635 */       return paramTreeInfo.deterministic;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Dot extends Pattern.CharProperty
/*      */   {
/*      */     Dot()
/*      */     {
/* 4048 */       super();
/*      */     }
/* 4050 */     boolean isSatisfiedBy(int paramInt) { return (paramInt != 10) && (paramInt != 13) && ((paramInt | 0x1) != 8233) && (paramInt != 133); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class End extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3489 */       int i = paramMatcher.anchoringBounds ? paramMatcher.to : paramMatcher.getTextLength();
/*      */ 
/* 3491 */       if (paramInt == i) {
/* 3492 */         paramMatcher.hitEnd = true;
/* 3493 */         return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/* 3495 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class First extends Pattern.Node
/*      */   {
/*      */     Pattern.Node atom;
/*      */ 
/*      */     First(Pattern.Node paramNode)
/*      */     {
/* 4882 */       this.atom = Pattern.BnM.optimize(paramNode);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4885 */       if ((this.atom instanceof Pattern.BnM)) {
/* 4886 */         return (this.atom.match(paramMatcher, paramInt, paramCharSequence)) && (this.next.match(paramMatcher, paramMatcher.last, paramCharSequence));
/*      */       }
/*      */       while (true)
/*      */       {
/* 4890 */         if (paramInt > paramMatcher.to) {
/* 4891 */           paramMatcher.hitEnd = true;
/* 4892 */           return false;
/*      */         }
/* 4894 */         if (this.atom.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4895 */           return this.next.match(paramMatcher, paramMatcher.last, paramCharSequence);
/*      */         }
/* 4897 */         paramInt += Pattern.countChars(paramCharSequence, paramInt, 1);
/* 4898 */         paramMatcher.first += 1;
/*      */       }
/*      */     }
/*      */ 
/* 4902 */     boolean study(Pattern.TreeInfo paramTreeInfo) { this.atom.study(paramTreeInfo);
/* 4903 */       paramTreeInfo.maxValid = false;
/* 4904 */       paramTreeInfo.deterministic = false;
/* 4905 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class GroupCurly extends Pattern.Node
/*      */   {
/*      */     Pattern.Node atom;
/*      */     int type;
/*      */     int cmin;
/*      */     int cmax;
/*      */     int localIndex;
/*      */     int groupIndex;
/*      */     boolean capture;
/*      */ 
/*      */     GroupCurly(Pattern.Node paramNode, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
/*      */     {
/* 4265 */       this.atom = paramNode;
/* 4266 */       this.type = paramInt3;
/* 4267 */       this.cmin = paramInt1;
/* 4268 */       this.cmax = paramInt2;
/* 4269 */       this.localIndex = paramInt4;
/* 4270 */       this.groupIndex = paramInt5;
/* 4271 */       this.capture = paramBoolean;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4274 */       int[] arrayOfInt1 = paramMatcher.groups;
/* 4275 */       int[] arrayOfInt2 = paramMatcher.locals;
/* 4276 */       int i = arrayOfInt2[this.localIndex];
/* 4277 */       int j = 0;
/* 4278 */       int k = 0;
/*      */ 
/* 4280 */       if (this.capture) {
/* 4281 */         j = arrayOfInt1[this.groupIndex];
/* 4282 */         k = arrayOfInt1[(this.groupIndex + 1)];
/*      */       }
/*      */ 
/* 4287 */       arrayOfInt2[this.localIndex] = -1;
/*      */ 
/* 4289 */       boolean bool = true;
/* 4290 */       for (int m = 0; m < this.cmin; m++) {
/* 4291 */         if (this.atom.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4292 */           if (this.capture) {
/* 4293 */             arrayOfInt1[this.groupIndex] = paramInt;
/* 4294 */             arrayOfInt1[(this.groupIndex + 1)] = paramMatcher.last;
/*      */           }
/* 4296 */           paramInt = paramMatcher.last;
/*      */         } else {
/* 4298 */           bool = false;
/* 4299 */           break;
/*      */         }
/*      */       }
/* 4302 */       if (bool) {
/* 4303 */         if (this.type == 0)
/* 4304 */           bool = match0(paramMatcher, paramInt, this.cmin, paramCharSequence);
/* 4305 */         else if (this.type == 1)
/* 4306 */           bool = match1(paramMatcher, paramInt, this.cmin, paramCharSequence);
/*      */         else {
/* 4308 */           bool = match2(paramMatcher, paramInt, this.cmin, paramCharSequence);
/*      */         }
/*      */       }
/* 4311 */       if (!bool) {
/* 4312 */         arrayOfInt2[this.localIndex] = i;
/* 4313 */         if (this.capture) {
/* 4314 */           arrayOfInt1[this.groupIndex] = j;
/* 4315 */           arrayOfInt1[(this.groupIndex + 1)] = k;
/*      */         }
/*      */       }
/* 4318 */       return bool;
/*      */     }
/*      */ 
/*      */     boolean match0(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence) {
/* 4322 */       int[] arrayOfInt = paramMatcher.groups;
/* 4323 */       int i = 0;
/* 4324 */       int j = 0;
/* 4325 */       if (this.capture) {
/* 4326 */         i = arrayOfInt[this.groupIndex];
/* 4327 */         j = arrayOfInt[(this.groupIndex + 1)];
/*      */       }
/*      */ 
/* 4330 */       if (paramInt2 < this.cmax)
/*      */       {
/* 4332 */         if (this.atom.match(paramMatcher, paramInt1, paramCharSequence))
/*      */         {
/* 4334 */           int k = paramMatcher.last - paramInt1;
/* 4335 */           if (k <= 0) {
/* 4336 */             if (this.capture) {
/* 4337 */               arrayOfInt[this.groupIndex] = paramInt1;
/* 4338 */               arrayOfInt[(this.groupIndex + 1)] = (paramInt1 + k);
/*      */             }
/* 4340 */             paramInt1 += k;
/*      */           }
/*      */           else {
/*      */             do {
/* 4344 */               if (this.capture) {
/* 4345 */                 arrayOfInt[this.groupIndex] = paramInt1;
/* 4346 */                 arrayOfInt[(this.groupIndex + 1)] = (paramInt1 + k);
/*      */               }
/* 4348 */               paramInt1 += k;
/* 4349 */               paramInt2++; if (paramInt2 >= this.cmax)
/*      */                 break;
/* 4351 */               if (!this.atom.match(paramMatcher, paramInt1, paramCharSequence)) break;
/*      */             }
/* 4353 */             while (paramInt1 + k == paramMatcher.last);
/* 4354 */             if (match0(paramMatcher, paramInt1, paramInt2, paramCharSequence)) {
/* 4355 */               return true;
/*      */             }
/*      */ 
/* 4359 */             while (paramInt2 > this.cmin) {
/* 4360 */               if (this.next.match(paramMatcher, paramInt1, paramCharSequence)) {
/* 4361 */                 if (this.capture) {
/* 4362 */                   arrayOfInt[(this.groupIndex + 1)] = paramInt1;
/* 4363 */                   arrayOfInt[this.groupIndex] = (paramInt1 - k);
/*      */                 }
/* 4365 */                 paramInt1 -= k;
/* 4366 */                 return true;
/*      */               }
/*      */ 
/* 4369 */               if (this.capture) {
/* 4370 */                 arrayOfInt[(this.groupIndex + 1)] = paramInt1;
/* 4371 */                 arrayOfInt[this.groupIndex] = (paramInt1 - k);
/*      */               }
/* 4373 */               paramInt1 -= k;
/* 4374 */               paramInt2--;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 4378 */       if (this.capture) {
/* 4379 */         arrayOfInt[this.groupIndex] = i;
/* 4380 */         arrayOfInt[(this.groupIndex + 1)] = j;
/*      */       }
/* 4382 */       return this.next.match(paramMatcher, paramInt1, paramCharSequence);
/*      */     }
/*      */ 
/*      */     boolean match1(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence) {
/*      */       while (true) {
/* 4387 */         if (this.next.match(paramMatcher, paramInt1, paramCharSequence))
/* 4388 */           return true;
/* 4389 */         if (paramInt2 >= this.cmax)
/* 4390 */           return false;
/* 4391 */         if (!this.atom.match(paramMatcher, paramInt1, paramCharSequence))
/* 4392 */           return false;
/* 4393 */         if (paramInt1 == paramMatcher.last)
/* 4394 */           return false;
/* 4395 */         if (this.capture) {
/* 4396 */           paramMatcher.groups[this.groupIndex] = paramInt1;
/* 4397 */           paramMatcher.groups[(this.groupIndex + 1)] = paramMatcher.last;
/*      */         }
/* 4399 */         paramInt1 = paramMatcher.last;
/* 4400 */         paramInt2++;
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean match2(Matcher paramMatcher, int paramInt1, int paramInt2, CharSequence paramCharSequence) {
/* 4405 */       for (; (paramInt2 < this.cmax) && 
/* 4406 */         (this.atom.match(paramMatcher, paramInt1, paramCharSequence)); paramInt2++)
/*      */       {
/* 4409 */         if (this.capture) {
/* 4410 */           paramMatcher.groups[this.groupIndex] = paramInt1;
/* 4411 */           paramMatcher.groups[(this.groupIndex + 1)] = paramMatcher.last;
/*      */         }
/* 4413 */         if (paramInt1 == paramMatcher.last) {
/*      */           break;
/*      */         }
/* 4416 */         paramInt1 = paramMatcher.last;
/*      */       }
/* 4418 */       return this.next.match(paramMatcher, paramInt1, paramCharSequence);
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4422 */       int i = paramTreeInfo.minLength;
/* 4423 */       int j = paramTreeInfo.maxLength;
/* 4424 */       boolean bool1 = paramTreeInfo.maxValid;
/* 4425 */       boolean bool2 = paramTreeInfo.deterministic;
/* 4426 */       paramTreeInfo.reset();
/*      */ 
/* 4428 */       this.atom.study(paramTreeInfo);
/*      */ 
/* 4430 */       int k = paramTreeInfo.minLength * this.cmin + i;
/* 4431 */       if (k < i) {
/* 4432 */         k = 268435455;
/*      */       }
/* 4434 */       paramTreeInfo.minLength = k;
/*      */ 
/* 4436 */       if ((bool1 & paramTreeInfo.maxValid)) {
/* 4437 */         k = paramTreeInfo.maxLength * this.cmax + j;
/* 4438 */         paramTreeInfo.maxLength = k;
/* 4439 */         if (k < j)
/* 4440 */           paramTreeInfo.maxValid = false;
/*      */       }
/*      */       else {
/* 4443 */         paramTreeInfo.maxValid = false;
/*      */       }
/*      */ 
/* 4446 */       if ((paramTreeInfo.deterministic) && (this.cmin == this.cmax))
/* 4447 */         paramTreeInfo.deterministic = bool2;
/*      */       else {
/* 4449 */         paramTreeInfo.deterministic = false;
/*      */       }
/*      */ 
/* 4452 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class GroupHead extends Pattern.Node
/*      */   {
/*      */     int localIndex;
/*      */ 
/*      */     GroupHead(int paramInt)
/*      */     {
/* 4551 */       this.localIndex = paramInt;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4554 */       int i = paramMatcher.locals[this.localIndex];
/* 4555 */       paramMatcher.locals[this.localIndex] = paramInt;
/* 4556 */       boolean bool = this.next.match(paramMatcher, paramInt, paramCharSequence);
/* 4557 */       paramMatcher.locals[this.localIndex] = i;
/* 4558 */       return bool;
/*      */     }
/*      */     boolean matchRef(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4561 */       int i = paramMatcher.locals[this.localIndex];
/* 4562 */       paramMatcher.locals[this.localIndex] = (paramInt ^ 0xFFFFFFFF);
/* 4563 */       boolean bool = this.next.match(paramMatcher, paramInt, paramCharSequence);
/* 4564 */       paramMatcher.locals[this.localIndex] = i;
/* 4565 */       return bool;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class GroupRef extends Pattern.Node
/*      */   {
/*      */     Pattern.GroupHead head;
/*      */ 
/*      */     GroupRef(Pattern.GroupHead paramGroupHead)
/*      */     {
/* 4577 */       this.head = paramGroupHead;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4580 */       return (this.head.matchRef(paramMatcher, paramInt, paramCharSequence)) && (this.next.match(paramMatcher, paramMatcher.last, paramCharSequence));
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4584 */       paramTreeInfo.maxValid = false;
/* 4585 */       paramTreeInfo.deterministic = false;
/* 4586 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class GroupTail extends Pattern.Node
/*      */   {
/*      */     int localIndex;
/*      */     int groupIndex;
/*      */ 
/*      */     GroupTail(int paramInt1, int paramInt2)
/*      */     {
/* 4602 */       this.localIndex = paramInt1;
/* 4603 */       this.groupIndex = (paramInt2 + paramInt2);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4606 */       int i = paramMatcher.locals[this.localIndex];
/* 4607 */       if (i >= 0)
/*      */       {
/* 4610 */         int j = paramMatcher.groups[this.groupIndex];
/* 4611 */         int k = paramMatcher.groups[(this.groupIndex + 1)];
/*      */ 
/* 4613 */         paramMatcher.groups[this.groupIndex] = i;
/* 4614 */         paramMatcher.groups[(this.groupIndex + 1)] = paramInt;
/* 4615 */         if (this.next.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4616 */           return true;
/*      */         }
/* 4618 */         paramMatcher.groups[this.groupIndex] = j;
/* 4619 */         paramMatcher.groups[(this.groupIndex + 1)] = k;
/* 4620 */         return false;
/*      */       }
/*      */ 
/* 4624 */       paramMatcher.last = paramInt;
/* 4625 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class LastMatch extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3563 */       if (paramInt != paramMatcher.oldLast)
/* 3564 */         return false;
/* 3565 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LastNode extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3378 */       if ((paramMatcher.acceptMode == 1) && (paramInt != paramMatcher.to))
/* 3379 */         return false;
/* 3380 */       paramMatcher.last = paramInt;
/* 3381 */       paramMatcher.groups[0] = paramMatcher.first;
/* 3382 */       paramMatcher.groups[1] = paramMatcher.last;
/* 3383 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class LazyLoop extends Pattern.Loop
/*      */   {
/*      */     LazyLoop(int paramInt1, int paramInt2)
/*      */     {
/* 4726 */       super(paramInt2);
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4730 */       if (paramInt > paramMatcher.locals[this.beginIndex]) {
/* 4731 */         int i = paramMatcher.locals[this.countIndex];
/*      */         boolean bool;
/* 4732 */         if (i < this.cmin) {
/* 4733 */           paramMatcher.locals[this.countIndex] = (i + 1);
/* 4734 */           bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/*      */ 
/* 4737 */           if (!bool)
/* 4738 */             paramMatcher.locals[this.countIndex] = i;
/* 4739 */           return bool;
/*      */         }
/* 4741 */         if (this.next.match(paramMatcher, paramInt, paramCharSequence))
/* 4742 */           return true;
/* 4743 */         if (i < this.cmax) {
/* 4744 */           paramMatcher.locals[this.countIndex] = (i + 1);
/* 4745 */           bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/*      */ 
/* 4748 */           if (!bool)
/* 4749 */             paramMatcher.locals[this.countIndex] = i;
/* 4750 */           return bool;
/*      */         }
/* 4752 */         return false;
/*      */       }
/* 4754 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean matchInit(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4757 */       int i = paramMatcher.locals[this.countIndex];
/* 4758 */       boolean bool = false;
/* 4759 */       if (0 < this.cmin) {
/* 4760 */         paramMatcher.locals[this.countIndex] = 1;
/* 4761 */         bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/* 4762 */       } else if (this.next.match(paramMatcher, paramInt, paramCharSequence)) {
/* 4763 */         bool = true;
/* 4764 */       } else if (0 < this.cmax) {
/* 4765 */         paramMatcher.locals[this.countIndex] = 1;
/* 4766 */         bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/* 4768 */       paramMatcher.locals[this.countIndex] = i;
/* 4769 */       return bool;
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4772 */       paramTreeInfo.maxValid = false;
/* 4773 */       paramTreeInfo.deterministic = false;
/* 4774 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Loop extends Pattern.Node
/*      */   {
/*      */     Pattern.Node body;
/*      */     int countIndex;
/*      */     int beginIndex;
/*      */     int cmin;
/*      */     int cmax;
/*      */ 
/*      */     Loop(int paramInt1, int paramInt2)
/*      */     {
/* 4658 */       this.countIndex = paramInt1;
/* 4659 */       this.beginIndex = paramInt2;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4663 */       if (paramInt > paramMatcher.locals[this.beginIndex]) {
/* 4664 */         int i = paramMatcher.locals[this.countIndex];
/*      */         boolean bool;
/* 4668 */         if (i < this.cmin) {
/* 4669 */           paramMatcher.locals[this.countIndex] = (i + 1);
/* 4670 */           bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/*      */ 
/* 4673 */           if (!bool) {
/* 4674 */             paramMatcher.locals[this.countIndex] = i;
/*      */           }
/*      */ 
/* 4677 */           return bool;
/*      */         }
/*      */ 
/* 4681 */         if (i < this.cmax) {
/* 4682 */           paramMatcher.locals[this.countIndex] = (i + 1);
/* 4683 */           bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/*      */ 
/* 4686 */           if (!bool)
/* 4687 */             paramMatcher.locals[this.countIndex] = i;
/*      */           else
/* 4689 */             return true;
/*      */         }
/*      */       }
/* 4692 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean matchInit(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4695 */       int i = paramMatcher.locals[this.countIndex];
/* 4696 */       boolean bool = false;
/* 4697 */       if (0 < this.cmin) {
/* 4698 */         paramMatcher.locals[this.countIndex] = 1;
/* 4699 */         bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/* 4700 */       } else if (0 < this.cmax) {
/* 4701 */         paramMatcher.locals[this.countIndex] = 1;
/* 4702 */         bool = this.body.match(paramMatcher, paramInt, paramCharSequence);
/* 4703 */         if (!bool)
/* 4704 */           bool = this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */       } else {
/* 4706 */         bool = this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/* 4708 */       paramMatcher.locals[this.countIndex] = i;
/* 4709 */       return bool;
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4712 */       paramTreeInfo.maxValid = false;
/* 4713 */       paramTreeInfo.deterministic = false;
/* 4714 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Neg extends Pattern.Node
/*      */   {
/*      */     Pattern.Node cond;
/*      */ 
/*      */     Neg(Pattern.Node paramNode)
/*      */     {
/* 4975 */       this.cond = paramNode;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4978 */       int i = paramMatcher.to;
/* 4979 */       int j = 0;
/*      */ 
/* 4982 */       if (paramMatcher.transparentBounds)
/* 4983 */         paramMatcher.to = paramMatcher.getTextLength();
/*      */       try {
/* 4985 */         if (paramInt < paramMatcher.to) {
/* 4986 */           j = !this.cond.match(paramMatcher, paramInt, paramCharSequence) ? 1 : 0;
/*      */         }
/*      */         else
/*      */         {
/* 4990 */           paramMatcher.requireEnd = true;
/* 4991 */           j = !this.cond.match(paramMatcher, paramInt, paramCharSequence) ? 1 : 0;
/*      */         }
/*      */       }
/*      */       finally {
/* 4995 */         paramMatcher.to = i;
/*      */       }
/* 4997 */       return (j != 0) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Node
/*      */   {
/*      */     Node next;
/*      */ 
/*      */     Node()
/*      */     {
/* 3348 */       this.next = Pattern.accept;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3354 */       paramMatcher.last = paramInt;
/* 3355 */       paramMatcher.groups[0] = paramMatcher.first;
/* 3356 */       paramMatcher.groups[1] = paramMatcher.last;
/* 3357 */       return true;
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo)
/*      */     {
/* 3363 */       if (this.next != null) {
/* 3364 */         return this.next.study(paramTreeInfo);
/*      */       }
/* 3366 */       return paramTreeInfo.deterministic;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NotBehind extends Pattern.Node
/*      */   {
/*      */     Pattern.Node cond;
/*      */     int rmax;
/*      */     int rmin;
/*      */ 
/*      */     NotBehind(Pattern.Node paramNode, int paramInt1, int paramInt2)
/*      */     {
/* 5085 */       this.cond = paramNode;
/* 5086 */       this.rmax = paramInt1;
/* 5087 */       this.rmin = paramInt2;
/*      */     }
/*      */ 
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5091 */       int i = paramMatcher.lookbehindTo;
/* 5092 */       int j = paramMatcher.from;
/* 5093 */       boolean bool = false;
/* 5094 */       int k = !paramMatcher.transparentBounds ? paramMatcher.from : 0;
/*      */ 
/* 5096 */       int m = Math.max(paramInt - this.rmax, k);
/* 5097 */       paramMatcher.lookbehindTo = paramInt;
/*      */ 
/* 5099 */       if (paramMatcher.transparentBounds)
/* 5100 */         paramMatcher.from = 0;
/* 5101 */       for (int n = paramInt - this.rmin; (!bool) && (n >= m); n--) {
/* 5102 */         bool = this.cond.match(paramMatcher, n, paramCharSequence);
/*      */       }
/*      */ 
/* 5105 */       paramMatcher.from = j;
/* 5106 */       paramMatcher.lookbehindTo = i;
/* 5107 */       return (!bool) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class NotBehindS extends Pattern.NotBehind
/*      */   {
/*      */     NotBehindS(Pattern.Node paramNode, int paramInt1, int paramInt2)
/*      */     {
/* 5117 */       super(paramInt1, paramInt2);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 5120 */       int i = Pattern.countChars(paramCharSequence, paramInt, -this.rmax);
/* 5121 */       int j = Pattern.countChars(paramCharSequence, paramInt, -this.rmin);
/* 5122 */       int k = paramMatcher.from;
/* 5123 */       int m = paramMatcher.lookbehindTo;
/* 5124 */       boolean bool = false;
/* 5125 */       int n = !paramMatcher.transparentBounds ? paramMatcher.from : 0;
/*      */ 
/* 5127 */       int i1 = Math.max(paramInt - i, n);
/* 5128 */       paramMatcher.lookbehindTo = paramInt;
/*      */ 
/* 5130 */       if (paramMatcher.transparentBounds)
/* 5131 */         paramMatcher.from = 0;
/* 5132 */       int i2 = paramInt - j;
/*      */ 
/* 5134 */       for (; (!bool) && (i2 >= i1); 
/* 5134 */         i2 -= (i2 > i1 ? Pattern.countChars(paramCharSequence, i2, -1) : 1)) {
/* 5135 */         bool = this.cond.match(paramMatcher, i2, paramCharSequence);
/*      */       }
/*      */ 
/* 5138 */       paramMatcher.from = k;
/* 5139 */       paramMatcher.lookbehindTo = m;
/* 5140 */       return (!bool) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Pos extends Pattern.Node
/*      */   {
/*      */     Pattern.Node cond;
/*      */ 
/*      */     Pos(Pattern.Node paramNode)
/*      */     {
/* 4950 */       this.cond = paramNode;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4953 */       int i = paramMatcher.to;
/* 4954 */       boolean bool = false;
/*      */ 
/* 4957 */       if (paramMatcher.transparentBounds)
/* 4958 */         paramMatcher.to = paramMatcher.getTextLength();
/*      */       try {
/* 4960 */         bool = this.cond.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/*      */       finally {
/* 4963 */         paramMatcher.to = i;
/*      */       }
/* 4965 */       return (bool) && (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Prolog extends Pattern.Node
/*      */   {
/*      */     Pattern.Loop loop;
/*      */ 
/*      */     Prolog(Pattern.Loop paramLoop)
/*      */     {
/* 4636 */       this.loop = paramLoop;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4639 */       return this.loop.matchInit(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4642 */       return this.loop.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Ques extends Pattern.Node
/*      */   {
/*      */     Pattern.Node atom;
/*      */     int type;
/*      */ 
/*      */     Ques(Pattern.Node paramNode, int paramInt)
/*      */     {
/* 4073 */       this.atom = paramNode;
/* 4074 */       this.type = paramInt;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 4077 */       switch (this.type) {
/*      */       case 0:
/* 4079 */         return ((this.atom.match(paramMatcher, paramInt, paramCharSequence)) && (this.next.match(paramMatcher, paramMatcher.last, paramCharSequence))) || (this.next.match(paramMatcher, paramInt, paramCharSequence));
/*      */       case 1:
/* 4082 */         return (this.next.match(paramMatcher, paramInt, paramCharSequence)) || ((this.atom.match(paramMatcher, paramInt, paramCharSequence)) && (this.next.match(paramMatcher, paramMatcher.last, paramCharSequence)));
/*      */       case 2:
/* 4085 */         if (this.atom.match(paramMatcher, paramInt, paramCharSequence)) paramInt = paramMatcher.last;
/* 4086 */         return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */       }
/* 4088 */       return (this.atom.match(paramMatcher, paramInt, paramCharSequence)) && (this.next.match(paramMatcher, paramMatcher.last, paramCharSequence));
/*      */     }
/*      */ 
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 4092 */       if (this.type != 3) {
/* 4093 */         int i = paramTreeInfo.minLength;
/* 4094 */         this.atom.study(paramTreeInfo);
/* 4095 */         paramTreeInfo.minLength = i;
/* 4096 */         paramTreeInfo.deterministic = false;
/* 4097 */         return this.next.study(paramTreeInfo);
/*      */       }
/* 4099 */       this.atom.study(paramTreeInfo);
/* 4100 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Script extends Pattern.CharProperty
/*      */   {
/*      */     final Character.UnicodeScript script;
/*      */ 
/*      */     Script(Character.UnicodeScript paramUnicodeScript)
/*      */     {
/* 3794 */       super();
/* 3795 */       this.script = paramUnicodeScript;
/*      */     }
/*      */     boolean isSatisfiedBy(int paramInt) {
/* 3798 */       return this.script == Character.UnicodeScript.of(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Single extends Pattern.BmpCharProperty
/*      */   {
/*      */     final int c;
/*      */ 
/*      */     Single(int paramInt)
/*      */     {
/* 3740 */       super(); this.c = paramInt;
/*      */     }
/* 3742 */     boolean isSatisfiedBy(int paramInt) { return paramInt == this.c; }
/*      */   }
/*      */ 
/*      */   static final class SingleI extends Pattern.BmpCharProperty
/*      */   {
/*      */     final int lower;
/*      */     final int upper;
/*      */ 
/*      */     SingleI(int paramInt1, int paramInt2) {
/* 3752 */       super();
/* 3753 */       this.lower = paramInt1;
/* 3754 */       this.upper = paramInt2;
/*      */     }
/*      */     boolean isSatisfiedBy(int paramInt) {
/* 3757 */       return (paramInt == this.lower) || (paramInt == this.upper);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SingleS extends Pattern.CharProperty
/*      */   {
/*      */     final int c;
/*      */ 
/*      */     SingleS(int paramInt)
/*      */     {
/* 3729 */       super(); this.c = paramInt;
/*      */     }
/* 3731 */     boolean isSatisfiedBy(int paramInt) { return paramInt == this.c; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class SingleU extends Pattern.CharProperty
/*      */   {
/*      */     final int lower;
/*      */ 
/*      */     SingleU(int paramInt)
/*      */     {
/* 3766 */       super();
/* 3767 */       this.lower = paramInt;
/*      */     }
/*      */     boolean isSatisfiedBy(int paramInt) {
/* 3770 */       return (this.lower == paramInt) || (this.lower == Character.toLowerCase(Character.toUpperCase(paramInt)));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Slice extends Pattern.SliceNode
/*      */   {
/*      */     Slice(int[] paramArrayOfInt)
/*      */     {
/* 3857 */       super();
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3860 */       int[] arrayOfInt = this.buffer;
/* 3861 */       int i = arrayOfInt.length;
/* 3862 */       for (int j = 0; j < i; j++) {
/* 3863 */         if (paramInt + j >= paramMatcher.to) {
/* 3864 */           paramMatcher.hitEnd = true;
/* 3865 */           return false;
/*      */         }
/* 3867 */         if (arrayOfInt[j] != paramCharSequence.charAt(paramInt + j))
/* 3868 */           return false;
/*      */       }
/* 3870 */       return this.next.match(paramMatcher, paramInt + i, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SliceI extends Pattern.SliceNode
/*      */   {
/*      */     SliceI(int[] paramArrayOfInt)
/*      */     {
/* 3880 */       super();
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3883 */       int[] arrayOfInt = this.buffer;
/* 3884 */       int i = arrayOfInt.length;
/* 3885 */       for (int j = 0; j < i; j++) {
/* 3886 */         if (paramInt + j >= paramMatcher.to) {
/* 3887 */           paramMatcher.hitEnd = true;
/* 3888 */           return false;
/*      */         }
/* 3890 */         int k = paramCharSequence.charAt(paramInt + j);
/* 3891 */         if ((arrayOfInt[j] != k) && (arrayOfInt[j] != ASCII.toLower(k)))
/*      */         {
/* 3893 */           return false;
/*      */         }
/*      */       }
/* 3895 */       return this.next.match(paramMatcher, paramInt + i, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SliceIS extends Pattern.SliceNode
/*      */   {
/*      */     SliceIS(int[] paramArrayOfInt)
/*      */     {
/* 3959 */       super();
/*      */     }
/*      */     int toLower(int paramInt) {
/* 3962 */       return ASCII.toLower(paramInt);
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3965 */       int[] arrayOfInt = this.buffer;
/* 3966 */       int i = paramInt;
/* 3967 */       for (int j = 0; j < arrayOfInt.length; j++) {
/* 3968 */         if (i >= paramMatcher.to) {
/* 3969 */           paramMatcher.hitEnd = true;
/* 3970 */           return false;
/*      */         }
/* 3972 */         int k = Character.codePointAt(paramCharSequence, i);
/* 3973 */         if ((arrayOfInt[j] != k) && (arrayOfInt[j] != toLower(k)))
/* 3974 */           return false;
/* 3975 */         i += Character.charCount(k);
/* 3976 */         if (i > paramMatcher.to) {
/* 3977 */           paramMatcher.hitEnd = true;
/* 3978 */           return false;
/*      */         }
/*      */       }
/* 3981 */       return this.next.match(paramMatcher, i, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SliceNode extends Pattern.Node
/*      */   {
/*      */     int[] buffer;
/*      */ 
/*      */     SliceNode(int[] paramArrayOfInt)
/*      */     {
/* 3842 */       this.buffer = paramArrayOfInt;
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 3845 */       paramTreeInfo.minLength += this.buffer.length;
/* 3846 */       paramTreeInfo.maxLength += this.buffer.length;
/* 3847 */       return this.next.study(paramTreeInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SliceS extends Pattern.SliceNode
/*      */   {
/*      */     SliceS(int[] paramArrayOfInt)
/*      */     {
/* 3930 */       super();
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3933 */       int[] arrayOfInt = this.buffer;
/* 3934 */       int i = paramInt;
/* 3935 */       for (int j = 0; j < arrayOfInt.length; j++) {
/* 3936 */         if (i >= paramMatcher.to) {
/* 3937 */           paramMatcher.hitEnd = true;
/* 3938 */           return false;
/*      */         }
/* 3940 */         int k = Character.codePointAt(paramCharSequence, i);
/* 3941 */         if (arrayOfInt[j] != k)
/* 3942 */           return false;
/* 3943 */         i += Character.charCount(k);
/* 3944 */         if (i > paramMatcher.to) {
/* 3945 */           paramMatcher.hitEnd = true;
/* 3946 */           return false;
/*      */         }
/*      */       }
/* 3949 */       return this.next.match(paramMatcher, i, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SliceU extends Pattern.SliceNode
/*      */   {
/*      */     SliceU(int[] paramArrayOfInt)
/*      */     {
/* 3905 */       super();
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3908 */       int[] arrayOfInt = this.buffer;
/* 3909 */       int i = arrayOfInt.length;
/* 3910 */       for (int j = 0; j < i; j++) {
/* 3911 */         if (paramInt + j >= paramMatcher.to) {
/* 3912 */           paramMatcher.hitEnd = true;
/* 3913 */           return false;
/*      */         }
/* 3915 */         int k = paramCharSequence.charAt(paramInt + j);
/* 3916 */         if ((arrayOfInt[j] != k) && (arrayOfInt[j] != Character.toLowerCase(Character.toUpperCase(k))))
/*      */         {
/* 3918 */           return false;
/*      */         }
/*      */       }
/* 3920 */       return this.next.match(paramMatcher, paramInt + i, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SliceUS extends Pattern.SliceIS
/*      */   {
/*      */     SliceUS(int[] paramArrayOfInt)
/*      */     {
/* 3991 */       super();
/*      */     }
/*      */     int toLower(int paramInt) {
/* 3994 */       return Character.toLowerCase(Character.toUpperCase(paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Start extends Pattern.Node
/*      */   {
/*      */     int minLength;
/*      */ 
/*      */     Start(Pattern.Node paramNode)
/*      */     {
/* 3396 */       this.next = paramNode;
/* 3397 */       Pattern.TreeInfo localTreeInfo = new Pattern.TreeInfo();
/* 3398 */       this.next.study(localTreeInfo);
/* 3399 */       this.minLength = localTreeInfo.minLength;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3402 */       if (paramInt > paramMatcher.to - this.minLength) {
/* 3403 */         paramMatcher.hitEnd = true;
/* 3404 */         return false;
/*      */       }
/* 3406 */       int i = paramMatcher.to - this.minLength;
/* 3407 */       for (; paramInt <= i; paramInt++) {
/* 3408 */         if (this.next.match(paramMatcher, paramInt, paramCharSequence)) {
/* 3409 */           paramMatcher.first = paramInt;
/* 3410 */           paramMatcher.groups[0] = paramMatcher.first;
/* 3411 */           paramMatcher.groups[1] = paramMatcher.last;
/* 3412 */           return true;
/*      */         }
/*      */       }
/* 3415 */       paramMatcher.hitEnd = true;
/* 3416 */       return false;
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 3419 */       this.next.study(paramTreeInfo);
/* 3420 */       paramTreeInfo.maxValid = false;
/* 3421 */       paramTreeInfo.deterministic = false;
/* 3422 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class StartS extends Pattern.Start
/*      */   {
/*      */     StartS(Pattern.Node paramNode)
/*      */     {
/* 3431 */       super();
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3434 */       if (paramInt > paramMatcher.to - this.minLength) {
/* 3435 */         paramMatcher.hitEnd = true;
/* 3436 */         return false;
/*      */       }
/* 3438 */       int i = paramMatcher.to - this.minLength;
/* 3439 */       while (paramInt <= i)
/*      */       {
/* 3441 */         if (this.next.match(paramMatcher, paramInt, paramCharSequence)) {
/* 3442 */           paramMatcher.first = paramInt;
/* 3443 */           paramMatcher.groups[0] = paramMatcher.first;
/* 3444 */           paramMatcher.groups[1] = paramMatcher.last;
/* 3445 */           return true;
/*      */         }
/* 3447 */         if (paramInt == i)
/*      */         {
/*      */           break;
/*      */         }
/* 3451 */         if ((Character.isHighSurrogate(paramCharSequence.charAt(paramInt++))) && 
/* 3452 */           (paramInt < paramCharSequence.length()) && (Character.isLowSurrogate(paramCharSequence.charAt(paramInt))))
/*      */         {
/* 3454 */           paramInt++;
/*      */         }
/*      */       }
/*      */ 
/* 3458 */       paramMatcher.hitEnd = true;
/* 3459 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class TreeInfo
/*      */   {
/*      */     int minLength;
/*      */     int maxLength;
/*      */     boolean maxValid;
/*      */     boolean deterministic;
/*      */ 
/*      */     TreeInfo()
/*      */     {
/* 1751 */       reset();
/*      */     }
/*      */     void reset() {
/* 1754 */       this.minLength = 0;
/* 1755 */       this.maxLength = 0;
/* 1756 */       this.maxValid = true;
/* 1757 */       this.deterministic = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class UnixCaret extends Pattern.Node
/*      */   {
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence)
/*      */     {
/* 3536 */       int i = paramMatcher.from;
/* 3537 */       int j = paramMatcher.to;
/* 3538 */       if (!paramMatcher.anchoringBounds) {
/* 3539 */         i = 0;
/* 3540 */         j = paramMatcher.getTextLength();
/*      */       }
/*      */ 
/* 3543 */       if (paramInt == j) {
/* 3544 */         paramMatcher.hitEnd = true;
/* 3545 */         return false;
/*      */       }
/* 3547 */       if (paramInt > i) {
/* 3548 */         int k = paramCharSequence.charAt(paramInt - 1);
/* 3549 */         if (k != 10) {
/* 3550 */           return false;
/*      */         }
/*      */       }
/* 3553 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class UnixDollar extends Pattern.Node
/*      */   {
/*      */     boolean multiline;
/*      */ 
/*      */     UnixDollar(boolean paramBoolean)
/*      */     {
/* 3646 */       this.multiline = paramBoolean;
/*      */     }
/*      */     boolean match(Matcher paramMatcher, int paramInt, CharSequence paramCharSequence) {
/* 3649 */       int i = paramMatcher.anchoringBounds ? paramMatcher.to : paramMatcher.getTextLength();
/*      */ 
/* 3651 */       if (paramInt < i) {
/* 3652 */         int j = paramCharSequence.charAt(paramInt);
/* 3653 */         if (j == 10)
/*      */         {
/* 3656 */           if ((!this.multiline) && (paramInt != i - 1)) {
/* 3657 */             return false;
/*      */           }
/*      */ 
/* 3660 */           if (this.multiline)
/* 3661 */             return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */         } else {
/* 3663 */           return false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3668 */       paramMatcher.hitEnd = true;
/*      */ 
/* 3671 */       paramMatcher.requireEnd = true;
/* 3672 */       return this.next.match(paramMatcher, paramInt, paramCharSequence);
/*      */     }
/*      */     boolean study(Pattern.TreeInfo paramTreeInfo) {
/* 3675 */       this.next.study(paramTreeInfo);
/* 3676 */       return paramTreeInfo.deterministic;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class UnixDot extends Pattern.CharProperty
/*      */   {
/*      */     UnixDot()
/*      */     {
/* 4060 */       super();
/*      */     }
/* 4062 */     boolean isSatisfiedBy(int paramInt) { return paramInt != 10; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class Utype extends Pattern.CharProperty
/*      */   {
/*      */     final UnicodeProp uprop;
/*      */ 
/*      */     Utype(UnicodeProp paramUnicodeProp)
/*      */     {
/* 3818 */       super(); this.uprop = paramUnicodeProp;
/*      */     }
/* 3820 */     boolean isSatisfiedBy(int paramInt) { return this.uprop.is(paramInt); }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.regex.Pattern
 * JD-Core Version:    0.6.2
 */