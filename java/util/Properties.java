/*      */ package java.util;
/*      */ 
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ 
/*      */ public class Properties extends Hashtable<Object, Object>
/*      */ {
/*      */   private static final long serialVersionUID = 4112578634029874840L;
/*      */   protected Properties defaults;
/* 1111 */   private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/*      */   public Properties()
/*      */   {
/*  135 */     this(null);
/*      */   }
/*      */ 
/*      */   public Properties(Properties paramProperties)
/*      */   {
/*  144 */     this.defaults = paramProperties;
/*      */   }
/*      */ 
/*      */   public synchronized Object setProperty(String paramString1, String paramString2)
/*      */   {
/*  161 */     return put(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized void load(Reader paramReader)
/*      */     throws IOException
/*      */   {
/*  317 */     load0(new LineReader(paramReader));
/*      */   }
/*      */ 
/*      */   public synchronized void load(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  341 */     load0(new LineReader(paramInputStream));
/*      */   }
/*      */ 
/*      */   private void load0(LineReader paramLineReader) throws IOException {
/*  345 */     char[] arrayOfChar = new char[1024];
/*      */     int i;
/*  353 */     while ((i = paramLineReader.readLine()) >= 0) {
/*  354 */       int m = 0;
/*  355 */       int j = 0;
/*  356 */       int k = i;
/*  357 */       int n = 0;
/*      */ 
/*  360 */       int i1 = 0;
/*  361 */       while (j < i) {
/*  362 */         m = paramLineReader.lineBuf[j];
/*      */ 
/*  364 */         if (((m == 61) || (m == 58)) && (i1 == 0)) {
/*  365 */           k = j + 1;
/*  366 */           n = 1;
/*  367 */           break;
/*  368 */         }if (((m == 32) || (m == 9) || (m == 12)) && (i1 == 0)) {
/*  369 */           k = j + 1;
/*  370 */           break;
/*      */         }
/*  372 */         if (m == 92)
/*  373 */           i1 = i1 == 0 ? 1 : 0;
/*      */         else {
/*  375 */           i1 = 0;
/*      */         }
/*  377 */         j++;
/*      */       }
/*  379 */       while (k < i) {
/*  380 */         m = paramLineReader.lineBuf[k];
/*  381 */         if ((m != 32) && (m != 9) && (m != 12)) {
/*  382 */           if ((n != 0) || ((m != 61) && (m != 58))) break;
/*  383 */           n = 1;
/*      */         }
/*      */ 
/*  388 */         k++;
/*      */       }
/*  390 */       String str1 = loadConvert(paramLineReader.lineBuf, 0, j, arrayOfChar);
/*  391 */       String str2 = loadConvert(paramLineReader.lineBuf, k, i - k, arrayOfChar);
/*  392 */       put(str1, str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String loadConvert(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2)
/*      */   {
/*      */     int i;
/*  533 */     if (paramArrayOfChar2.length < paramInt2) {
/*  534 */       i = paramInt2 * 2;
/*  535 */       if (i < 0) {
/*  536 */         i = 2147483647;
/*      */       }
/*  538 */       paramArrayOfChar2 = new char[i];
/*      */     }
/*      */ 
/*  541 */     char[] arrayOfChar = paramArrayOfChar2;
/*  542 */     int j = 0;
/*  543 */     int k = paramInt1 + paramInt2;
/*      */ 
/*  545 */     while (paramInt1 < k) {
/*  546 */       i = paramArrayOfChar1[(paramInt1++)];
/*  547 */       if (i == 92) {
/*  548 */         i = paramArrayOfChar1[(paramInt1++)];
/*  549 */         if (i == 117)
/*      */         {
/*  551 */           int m = 0;
/*  552 */           for (int n = 0; n < 4; n++) {
/*  553 */             i = paramArrayOfChar1[(paramInt1++)];
/*  554 */             switch (i) { case 48:
/*      */             case 49:
/*      */             case 50:
/*      */             case 51:
/*      */             case 52:
/*      */             case 53:
/*      */             case 54:
/*      */             case 55:
/*      */             case 56:
/*      */             case 57:
/*  557 */               m = (m << 4) + i - 48;
/*  558 */               break;
/*      */             case 97:
/*      */             case 98:
/*      */             case 99:
/*      */             case 100:
/*      */             case 101:
/*      */             case 102:
/*  561 */               m = (m << 4) + 10 + i - 97;
/*  562 */               break;
/*      */             case 65:
/*      */             case 66:
/*      */             case 67:
/*      */             case 68:
/*      */             case 69:
/*      */             case 70:
/*  565 */               m = (m << 4) + 10 + i - 65;
/*  566 */               break;
/*      */             case 58:
/*      */             case 59:
/*      */             case 60:
/*      */             case 61:
/*      */             case 62:
/*      */             case 63:
/*      */             case 64:
/*      */             case 71:
/*      */             case 72:
/*      */             case 73:
/*      */             case 74:
/*      */             case 75:
/*      */             case 76:
/*      */             case 77:
/*      */             case 78:
/*      */             case 79:
/*      */             case 80:
/*      */             case 81:
/*      */             case 82:
/*      */             case 83:
/*      */             case 84:
/*      */             case 85:
/*      */             case 86:
/*      */             case 87:
/*      */             case 88:
/*      */             case 89:
/*      */             case 90:
/*      */             case 91:
/*      */             case 92:
/*      */             case 93:
/*      */             case 94:
/*      */             case 95:
/*      */             case 96:
/*      */             default:
/*  568 */               throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
/*      */             }
/*      */           }
/*      */ 
/*  572 */           arrayOfChar[(j++)] = ((char)m);
/*      */         } else {
/*  574 */           if (i == 116) i = 9;
/*  575 */           else if (i == 114) i = 13;
/*  576 */           else if (i == 110) i = 10;
/*  577 */           else if (i == 102) i = 12;
/*  578 */           arrayOfChar[(j++)] = i;
/*      */         }
/*      */       } else {
/*  581 */         arrayOfChar[(j++)] = i;
/*      */       }
/*      */     }
/*  584 */     return new String(arrayOfChar, 0, j);
/*      */   }
/*      */ 
/*      */   private String saveConvert(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  594 */     int i = paramString.length();
/*  595 */     int j = i * 2;
/*  596 */     if (j < 0) {
/*  597 */       j = 2147483647;
/*      */     }
/*  599 */     StringBuffer localStringBuffer = new StringBuffer(j);
/*      */ 
/*  601 */     for (int k = 0; k < i; k++) {
/*  602 */       char c = paramString.charAt(k);
/*      */ 
/*  605 */       if ((c > '=') && (c < '')) {
/*  606 */         if (c == '\\') {
/*  607 */           localStringBuffer.append('\\'); localStringBuffer.append('\\');
/*      */         }
/*      */         else {
/*  610 */           localStringBuffer.append(c);
/*      */         }
/*      */       }
/*  613 */       else switch (c) {
/*      */         case ' ':
/*  615 */           if ((k == 0) || (paramBoolean1))
/*  616 */             localStringBuffer.append('\\');
/*  617 */           localStringBuffer.append(' ');
/*  618 */           break;
/*      */         case '\t':
/*  619 */           localStringBuffer.append('\\'); localStringBuffer.append('t');
/*  620 */           break;
/*      */         case '\n':
/*  621 */           localStringBuffer.append('\\'); localStringBuffer.append('n');
/*  622 */           break;
/*      */         case '\r':
/*  623 */           localStringBuffer.append('\\'); localStringBuffer.append('r');
/*  624 */           break;
/*      */         case '\f':
/*  625 */           localStringBuffer.append('\\'); localStringBuffer.append('f');
/*  626 */           break;
/*      */         case '!':
/*      */         case '#':
/*      */         case ':':
/*      */         case '=':
/*  631 */           localStringBuffer.append('\\'); localStringBuffer.append(c);
/*  632 */           break;
/*      */         default:
/*  634 */           if ((((c < ' ') || (c > '~')) & paramBoolean2)) {
/*  635 */             localStringBuffer.append('\\');
/*  636 */             localStringBuffer.append('u');
/*  637 */             localStringBuffer.append(toHex(c >> '\f' & 0xF));
/*  638 */             localStringBuffer.append(toHex(c >> '\b' & 0xF));
/*  639 */             localStringBuffer.append(toHex(c >> '\004' & 0xF));
/*  640 */             localStringBuffer.append(toHex(c & 0xF));
/*      */           } else {
/*  642 */             localStringBuffer.append(c);
/*      */           }
/*      */           break;
/*      */         } 
/*      */     }
/*  646 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static void writeComments(BufferedWriter paramBufferedWriter, String paramString) throws IOException
/*      */   {
/*  651 */     paramBufferedWriter.write("#");
/*  652 */     int i = paramString.length();
/*  653 */     int j = 0;
/*  654 */     int k = 0;
/*  655 */     char[] arrayOfChar = new char[6];
/*  656 */     arrayOfChar[0] = '\\';
/*  657 */     arrayOfChar[1] = 'u';
/*  658 */     while (j < i) {
/*  659 */       int m = paramString.charAt(j);
/*  660 */       if ((m > 255) || (m == 10) || (m == 13)) {
/*  661 */         if (k != j)
/*  662 */           paramBufferedWriter.write(paramString.substring(k, j));
/*  663 */         if (m > 255) {
/*  664 */           arrayOfChar[2] = toHex(m >> 12 & 0xF);
/*  665 */           arrayOfChar[3] = toHex(m >> 8 & 0xF);
/*  666 */           arrayOfChar[4] = toHex(m >> 4 & 0xF);
/*  667 */           arrayOfChar[5] = toHex(m & 0xF);
/*  668 */           paramBufferedWriter.write(new String(arrayOfChar));
/*      */         } else {
/*  670 */           paramBufferedWriter.newLine();
/*  671 */           if ((m == 13) && (j != i - 1) && (paramString.charAt(j + 1) == '\n'))
/*      */           {
/*  674 */             j++;
/*      */           }
/*  676 */           if ((j == i - 1) || ((paramString.charAt(j + 1) != '#') && (paramString.charAt(j + 1) != '!')))
/*      */           {
/*  679 */             paramBufferedWriter.write("#");
/*      */           }
/*      */         }
/*  681 */         k = j + 1;
/*      */       }
/*  683 */       j++;
/*      */     }
/*  685 */     if (k != j)
/*  686 */       paramBufferedWriter.write(paramString.substring(k, j));
/*  687 */     paramBufferedWriter.newLine();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void save(OutputStream paramOutputStream, String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  709 */       store(paramOutputStream, paramString);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void store(Writer paramWriter, String paramString)
/*      */     throws IOException
/*      */   {
/*  765 */     store0((paramWriter instanceof BufferedWriter) ? (BufferedWriter)paramWriter : new BufferedWriter(paramWriter), paramString, false);
/*      */   }
/*      */ 
/*      */   public void store(OutputStream paramOutputStream, String paramString)
/*      */     throws IOException
/*      */   {
/*  812 */     store0(new BufferedWriter(new OutputStreamWriter(paramOutputStream, "8859_1")), paramString, true);
/*      */   }
/*      */ 
/*      */   private void store0(BufferedWriter paramBufferedWriter, String paramString, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  820 */     if (paramString != null) {
/*  821 */       writeComments(paramBufferedWriter, paramString);
/*      */     }
/*  823 */     paramBufferedWriter.write("#" + new Date().toString());
/*  824 */     paramBufferedWriter.newLine();
/*      */     Enumeration localEnumeration;
/*  825 */     synchronized (this) {
/*  826 */       for (localEnumeration = keys(); localEnumeration.hasMoreElements(); ) {
/*  827 */         String str1 = (String)localEnumeration.nextElement();
/*  828 */         String str2 = (String)get(str1);
/*  829 */         str1 = saveConvert(str1, true, paramBoolean);
/*      */ 
/*  833 */         str2 = saveConvert(str2, false, paramBoolean);
/*  834 */         paramBufferedWriter.write(str1 + "=" + str2);
/*  835 */         paramBufferedWriter.newLine();
/*      */       }
/*      */     }
/*  838 */     paramBufferedWriter.flush();
/*      */   }
/*      */ 
/*      */   public synchronized void loadFromXML(InputStream paramInputStream)
/*      */     throws IOException, InvalidPropertiesFormatException
/*      */   {
/*  866 */     if (paramInputStream == null)
/*  867 */       throw new NullPointerException();
/*  868 */     XMLUtils.load(this, paramInputStream);
/*  869 */     paramInputStream.close();
/*      */   }
/*      */ 
/*      */   public void storeToXML(OutputStream paramOutputStream, String paramString)
/*      */     throws IOException
/*      */   {
/*  895 */     if (paramOutputStream == null)
/*  896 */       throw new NullPointerException();
/*  897 */     storeToXML(paramOutputStream, paramString, "UTF-8");
/*      */   }
/*      */ 
/*      */   public void storeToXML(OutputStream paramOutputStream, String paramString1, String paramString2)
/*      */     throws IOException
/*      */   {
/*  934 */     if (paramOutputStream == null)
/*  935 */       throw new NullPointerException();
/*  936 */     XMLUtils.save(this, paramOutputStream, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public String getProperty(String paramString)
/*      */   {
/*  951 */     Object localObject = super.get(paramString);
/*  952 */     String str = (localObject instanceof String) ? (String)localObject : null;
/*  953 */     return (str == null) && (this.defaults != null) ? this.defaults.getProperty(paramString) : str;
/*      */   }
/*      */ 
/*      */   public String getProperty(String paramString1, String paramString2)
/*      */   {
/*  970 */     String str = getProperty(paramString1);
/*  971 */     return str == null ? paramString2 : str;
/*      */   }
/*      */ 
/*      */   public Enumeration<?> propertyNames()
/*      */   {
/*  989 */     Hashtable localHashtable = new Hashtable();
/*  990 */     enumerate(localHashtable);
/*  991 */     return localHashtable.keys();
/*      */   }
/*      */ 
/*      */   public Set<String> stringPropertyNames()
/*      */   {
/* 1013 */     Hashtable localHashtable = new Hashtable();
/* 1014 */     enumerateStringProperties(localHashtable);
/* 1015 */     return localHashtable.keySet();
/*      */   }
/*      */ 
/*      */   public void list(PrintStream paramPrintStream)
/*      */   {
/* 1027 */     paramPrintStream.println("-- listing properties --");
/* 1028 */     Hashtable localHashtable = new Hashtable();
/* 1029 */     enumerate(localHashtable);
/* 1030 */     for (Enumeration localEnumeration = localHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 1031 */       String str1 = (String)localEnumeration.nextElement();
/* 1032 */       String str2 = (String)localHashtable.get(str1);
/* 1033 */       if (str2.length() > 40) {
/* 1034 */         str2 = str2.substring(0, 37) + "...";
/*      */       }
/* 1036 */       paramPrintStream.println(str1 + "=" + str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void list(PrintWriter paramPrintWriter)
/*      */   {
/* 1055 */     paramPrintWriter.println("-- listing properties --");
/* 1056 */     Hashtable localHashtable = new Hashtable();
/* 1057 */     enumerate(localHashtable);
/* 1058 */     for (Enumeration localEnumeration = localHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 1059 */       String str1 = (String)localEnumeration.nextElement();
/* 1060 */       String str2 = (String)localHashtable.get(str1);
/* 1061 */       if (str2.length() > 40) {
/* 1062 */         str2 = str2.substring(0, 37) + "...";
/*      */       }
/* 1064 */       paramPrintWriter.println(str1 + "=" + str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void enumerate(Hashtable paramHashtable)
/*      */   {
/* 1075 */     if (this.defaults != null) {
/* 1076 */       this.defaults.enumerate(paramHashtable);
/*      */     }
/* 1078 */     for (Enumeration localEnumeration = keys(); localEnumeration.hasMoreElements(); ) {
/* 1079 */       String str = (String)localEnumeration.nextElement();
/* 1080 */       paramHashtable.put(str, get(str));
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void enumerateStringProperties(Hashtable<String, String> paramHashtable)
/*      */   {
/* 1090 */     if (this.defaults != null) {
/* 1091 */       this.defaults.enumerateStringProperties(paramHashtable);
/*      */     }
/* 1093 */     for (Enumeration localEnumeration = keys(); localEnumeration.hasMoreElements(); ) {
/* 1094 */       Object localObject1 = localEnumeration.nextElement();
/* 1095 */       Object localObject2 = get(localObject1);
/* 1096 */       if (((localObject1 instanceof String)) && ((localObject2 instanceof String)))
/* 1097 */         paramHashtable.put((String)localObject1, (String)localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static char toHex(int paramInt)
/*      */   {
/* 1107 */     return hexDigit[(paramInt & 0xF)];
/*      */   }
/*      */ 
/*      */   class LineReader
/*      */   {
/*      */     byte[] inByteBuf;
/*      */     char[] inCharBuf;
/*  415 */     char[] lineBuf = new char[1024];
/*  416 */     int inLimit = 0;
/*  417 */     int inOff = 0;
/*      */     InputStream inStream;
/*      */     Reader reader;
/*      */ 
/*      */     public LineReader(InputStream arg2)
/*      */     {
/*      */       Object localObject;
/*  404 */       this.inStream = localObject;
/*  405 */       this.inByteBuf = new byte[8192];
/*      */     }
/*      */ 
/*      */     public LineReader(Reader arg2)
/*      */     {
/*      */       Object localObject;
/*  409 */       this.reader = localObject;
/*  410 */       this.inCharBuf = new char[8192];
/*      */     }
/*      */ 
/*      */     int readLine()
/*      */       throws IOException
/*      */     {
/*  422 */       int i = 0;
/*  423 */       int j = 0;
/*      */ 
/*  425 */       int k = 1;
/*  426 */       int m = 0;
/*  427 */       int n = 1;
/*  428 */       int i1 = 0;
/*  429 */       int i2 = 0;
/*  430 */       int i3 = 0;
/*      */       while (true)
/*      */       {
/*  433 */         if (this.inOff >= this.inLimit) {
/*  434 */           this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf));
/*      */ 
/*  436 */           this.inOff = 0;
/*  437 */           if (this.inLimit <= 0) {
/*  438 */             if ((i == 0) || (m != 0)) {
/*  439 */               return -1;
/*      */             }
/*  441 */             return i;
/*      */           }
/*      */         }
/*  444 */         if (this.inStream != null)
/*      */         {
/*  447 */           j = (char)(0xFF & this.inByteBuf[(this.inOff++)]);
/*      */         }
/*  449 */         else j = this.inCharBuf[(this.inOff++)];
/*      */ 
/*  451 */         if (i3 != 0)
/*      */         {
/*  452 */           i3 = 0;
/*  453 */           if (j == 10);
/*      */         }
/*  457 */         else if (k != 0) {
/*  458 */           if ((j != 32) && (j != 9) && (j != 12) && (
/*  461 */             (i1 != 0) || ((j != 13) && (j != 10))))
/*      */           {
/*  464 */             k = 0;
/*  465 */             i1 = 0;
/*      */           }
/*  467 */         } else if (n != 0) {
/*  468 */           n = 0;
/*  469 */           if ((j == 35) || (j == 33)) {
/*  470 */             m = 1;
/*      */           }
/*      */ 
/*      */         }
/*  475 */         else if ((j != 10) && (j != 13)) {
/*  476 */           this.lineBuf[(i++)] = j;
/*  477 */           if (i == this.lineBuf.length) {
/*  478 */             int i4 = this.lineBuf.length * 2;
/*  479 */             if (i4 < 0) {
/*  480 */               i4 = 2147483647;
/*      */             }
/*  482 */             char[] arrayOfChar = new char[i4];
/*  483 */             System.arraycopy(this.lineBuf, 0, arrayOfChar, 0, this.lineBuf.length);
/*  484 */             this.lineBuf = arrayOfChar;
/*      */           }
/*      */ 
/*  487 */           if (j == 92)
/*  488 */             i2 = i2 == 0 ? 1 : 0;
/*      */           else {
/*  490 */             i2 = 0;
/*      */           }
/*      */ 
/*      */         }
/*  495 */         else if ((m != 0) || (i == 0)) {
/*  496 */           m = 0;
/*  497 */           n = 1;
/*  498 */           k = 1;
/*  499 */           i = 0;
/*      */         }
/*      */         else {
/*  502 */           if (this.inOff >= this.inLimit) {
/*  503 */             this.inLimit = (this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf));
/*      */ 
/*  506 */             this.inOff = 0;
/*  507 */             if (this.inLimit <= 0) {
/*  508 */               return i;
/*      */             }
/*      */           }
/*  511 */           if (i2 == 0) break;
/*  512 */           i--;
/*      */ 
/*  514 */           k = 1;
/*  515 */           i1 = 1;
/*  516 */           i2 = 0;
/*  517 */           if (j == 13)
/*  518 */             i3 = 1;
/*      */         }
/*      */       }
/*  521 */       return i;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Properties
 * JD-Core Version:    0.6.2
 */