/*      */ package sun.security.x509;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.security.AccessController;
/*      */ import java.text.Normalizer;
/*      */ import java.text.Normalizer.Form;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.pkcs.PKCS9Attribute;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.DerEncoder;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerOutputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ 
/*      */ public class AVA
/*      */   implements DerEncoder
/*      */ {
/*   63 */   private static final Debug debug = Debug.getInstance("x509", "\t[AVA]");
/*      */ 
/*   67 */   private static final boolean PRESERVE_OLD_DC_ENCODING = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("com.sun.security.preserveOldDCEncoding"))).booleanValue();
/*      */   static final int DEFAULT = 1;
/*      */   static final int RFC1779 = 2;
/*      */   static final int RFC2253 = 3;
/*      */   final ObjectIdentifier oid;
/*      */   final DerValue value;
/*      */   private static final String specialChars = ",+=\n<>#;";
/*      */   private static final String specialChars2253 = ",+\"\\<>;";
/*      */   private static final String specialCharsAll = ",=\n+<>#;\\\" ";
/*      */   private static final String hexDigits = "0123456789ABCDEF";
/*      */ 
/*      */   public AVA(ObjectIdentifier paramObjectIdentifier, DerValue paramDerValue)
/*      */   {
/*  115 */     if ((paramObjectIdentifier == null) || (paramDerValue == null)) {
/*  116 */       throw new NullPointerException();
/*      */     }
/*  118 */     this.oid = paramObjectIdentifier;
/*  119 */     this.value = paramDerValue;
/*      */   }
/*      */ 
/*      */   AVA(Reader paramReader)
/*      */     throws IOException
/*      */   {
/*  132 */     this(paramReader, 1);
/*      */   }
/*      */ 
/*      */   AVA(Reader paramReader, Map<String, String> paramMap)
/*      */     throws IOException
/*      */   {
/*  145 */     this(paramReader, 1, paramMap);
/*      */   }
/*      */ 
/*      */   AVA(Reader paramReader, int paramInt)
/*      */     throws IOException
/*      */   {
/*  155 */     this(paramReader, paramInt, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   AVA(Reader paramReader, int paramInt, Map<String, String> paramMap)
/*      */     throws IOException
/*      */   {
/*  177 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */     int i;
/*      */     while (true)
/*      */     {
/*  185 */       i = readChar(paramReader, "Incorrect AVA format");
/*  186 */       if (i == 61) {
/*      */         break;
/*      */       }
/*  189 */       localStringBuilder.append((char)i);
/*      */     }
/*      */ 
/*  192 */     this.oid = AVAKeyword.getOID(localStringBuilder.toString(), paramInt, paramMap);
/*      */ 
/*  199 */     localStringBuilder.setLength(0);
/*  200 */     if (paramInt == 3)
/*      */     {
/*  202 */       i = paramReader.read();
/*  203 */       if (i == 32) {
/*  204 */         throw new IOException("Incorrect AVA RFC2253 format - leading space must be escaped");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       do
/*  210 */         i = paramReader.read();
/*  211 */       while ((i == 32) || (i == 10));
/*      */     }
/*  213 */     if (i == -1)
/*      */     {
/*  215 */       this.value = new DerValue("");
/*  216 */       return;
/*      */     }
/*      */ 
/*  219 */     if (i == 35)
/*  220 */       this.value = parseHexString(paramReader, paramInt);
/*  221 */     else if ((i == 34) && (paramInt != 3))
/*  222 */       this.value = parseQuotedString(paramReader, localStringBuilder);
/*      */     else
/*  224 */       this.value = parseString(paramReader, i, paramInt, localStringBuilder);
/*      */   }
/*      */ 
/*      */   public ObjectIdentifier getObjectIdentifier()
/*      */   {
/*  232 */     return this.oid;
/*      */   }
/*      */ 
/*      */   public DerValue getDerValue()
/*      */   {
/*  239 */     return this.value;
/*      */   }
/*      */ 
/*      */   public String getValueString()
/*      */   {
/*      */     try
/*      */     {
/*  250 */       String str = this.value.getAsString();
/*  251 */       if (str == null) {
/*  252 */         throw new RuntimeException("AVA string is null");
/*      */       }
/*  254 */       return str;
/*      */     }
/*      */     catch (IOException localIOException) {
/*  257 */       throw new RuntimeException("AVA error: " + localIOException, localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static DerValue parseHexString(Reader paramReader, int paramInt)
/*      */     throws IOException
/*      */   {
/*  265 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  266 */     int j = 0;
/*  267 */     int k = 0;
/*      */     while (true) {
/*  269 */       int i = paramReader.read();
/*      */ 
/*  271 */       if (isTerminator(i, paramInt))
/*      */       {
/*      */         break;
/*      */       }
/*  275 */       int m = "0123456789ABCDEF".indexOf(Character.toUpperCase((char)i));
/*      */ 
/*  277 */       if (m == -1) {
/*  278 */         throw new IOException("AVA parse, invalid hex digit: " + (char)i);
/*      */       }
/*      */ 
/*  282 */       if (k % 2 == 1) {
/*  283 */         j = (byte)(j * 16 + (byte)m);
/*  284 */         localByteArrayOutputStream.write(j);
/*      */       } else {
/*  286 */         j = (byte)m;
/*      */       }
/*  288 */       k++;
/*      */     }
/*      */ 
/*  292 */     if (k == 0) {
/*  293 */       throw new IOException("AVA parse, zero hex digits");
/*      */     }
/*      */ 
/*  297 */     if (k % 2 == 1) {
/*  298 */       throw new IOException("AVA parse, odd number of hex digits");
/*      */     }
/*      */ 
/*  301 */     return new DerValue(localByteArrayOutputStream.toByteArray());
/*      */   }
/*      */ 
/*      */   private DerValue parseQuotedString(Reader paramReader, StringBuilder paramStringBuilder)
/*      */     throws IOException
/*      */   {
/*  312 */     int i = readChar(paramReader, "Quoted string did not end in quote");
/*      */ 
/*  314 */     ArrayList localArrayList = new ArrayList();
/*  315 */     boolean bool = true;
/*      */     Object localObject;
/*  316 */     while (i != 34) {
/*  317 */       if (i == 92) {
/*  318 */         i = readChar(paramReader, "Quoted string did not end in quote");
/*      */ 
/*  321 */         localObject = null;
/*  322 */         if ((localObject = getEmbeddedHexPair(i, paramReader)) != null)
/*      */         {
/*  325 */           bool = false;
/*      */ 
/*  329 */           localArrayList.add(localObject);
/*  330 */           i = paramReader.read();
/*      */         }
/*  334 */         else if ((i != 92) && (i != 34) && (",+=\n<>#;".indexOf((char)i) < 0))
/*      */         {
/*  336 */           throw new IOException("Invalid escaped character in AVA: " + (char)i);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  343 */         if (localArrayList.size() > 0) {
/*  344 */           localObject = getEmbeddedHexString(localArrayList);
/*  345 */           paramStringBuilder.append((String)localObject);
/*  346 */           localArrayList.clear();
/*      */         }
/*      */ 
/*  350 */         bool &= DerValue.isPrintableStringChar((char)i);
/*  351 */         paramStringBuilder.append((char)i);
/*  352 */         i = readChar(paramReader, "Quoted string did not end in quote");
/*      */       }
/*      */     }
/*      */ 
/*  356 */     if (localArrayList.size() > 0) {
/*  357 */       localObject = getEmbeddedHexString(localArrayList);
/*  358 */       paramStringBuilder.append((String)localObject);
/*  359 */       localArrayList.clear();
/*      */     }
/*      */     do
/*      */     {
/*  363 */       i = paramReader.read();
/*  364 */     }while ((i == 10) || (i == 32));
/*  365 */     if (i != -1) {
/*  366 */       throw new IOException("AVA had characters other than whitespace after terminating quote");
/*      */     }
/*      */ 
/*  372 */     if ((this.oid.equals(PKCS9Attribute.EMAIL_ADDRESS_OID)) || ((this.oid.equals(X500Name.DOMAIN_COMPONENT_OID)) && (!PRESERVE_OLD_DC_ENCODING)))
/*      */     {
/*  376 */       return new DerValue((byte)22, paramStringBuilder.toString().trim());
/*      */     }
/*  378 */     if (bool) {
/*  379 */       return new DerValue(paramStringBuilder.toString().trim());
/*      */     }
/*  381 */     return new DerValue((byte)12, paramStringBuilder.toString().trim());
/*      */   }
/*      */ 
/*      */   private DerValue parseString(Reader paramReader, int paramInt1, int paramInt2, StringBuilder paramStringBuilder)
/*      */     throws IOException
/*      */   {
/*  389 */     ArrayList localArrayList = new ArrayList();
/*  390 */     boolean bool = true;
/*  391 */     int i = 0;
/*  392 */     int j = 1;
/*  393 */     int k = 0;
/*      */     do {
/*  395 */       i = 0;
/*  396 */       if (paramInt1 == 92) {
/*  397 */         i = 1;
/*  398 */         paramInt1 = readChar(paramReader, "Invalid trailing backslash");
/*      */ 
/*  401 */         Byte localByte = null;
/*  402 */         if ((localByte = getEmbeddedHexPair(paramInt1, paramReader)) != null)
/*      */         {
/*  405 */           bool = false;
/*      */ 
/*  409 */           localArrayList.add(localByte);
/*  410 */           paramInt1 = paramReader.read();
/*  411 */           j = 0;
/*  412 */           continue;
/*      */         }
/*      */ 
/*  416 */         if (((paramInt2 == 1) && (",=\n+<>#;\\\" ".indexOf((char)paramInt1) == -1)) || ((paramInt2 == 2) && (",+=\n<>#;".indexOf((char)paramInt1) == -1) && (paramInt1 != 92) && (paramInt1 != 34)))
/*      */         {
/*  422 */           throw new IOException("Invalid escaped character in AVA: '" + (char)paramInt1 + "'");
/*      */         }
/*      */ 
/*  426 */         if (paramInt2 == 3) {
/*  427 */           if (paramInt1 == 32)
/*      */           {
/*  429 */             if ((j == 0) && (!trailingSpace(paramReader))) {
/*  430 */               throw new IOException("Invalid escaped space character in AVA.  Only a leading or trailing space character can be escaped.");
/*      */             }
/*      */ 
/*      */           }
/*  435 */           else if (paramInt1 == 35)
/*      */           {
/*  437 */             if (j == 0) {
/*  438 */               throw new IOException("Invalid escaped '#' character in AVA.  Only a leading '#' can be escaped.");
/*      */             }
/*      */ 
/*      */           }
/*  442 */           else if (",+\"\\<>;".indexOf((char)paramInt1) == -1) {
/*  443 */             throw new IOException("Invalid escaped character in AVA: '" + (char)paramInt1 + "'");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  452 */       else if ((paramInt2 == 3) && 
/*  453 */         (",+\"\\<>;".indexOf((char)paramInt1) != -1)) {
/*  454 */         throw new IOException("Character '" + (char)paramInt1 + "' in AVA appears without escape");
/*      */       }
/*      */ 
/*  462 */       if (localArrayList.size() > 0)
/*      */       {
/*  464 */         for (int m = 0; m < k; m++) {
/*  465 */           paramStringBuilder.append(" ");
/*      */         }
/*  467 */         k = 0;
/*      */ 
/*  469 */         String str1 = getEmbeddedHexString(localArrayList);
/*  470 */         paramStringBuilder.append(str1);
/*  471 */         localArrayList.clear();
/*      */       }
/*      */ 
/*  475 */       bool &= DerValue.isPrintableStringChar((char)paramInt1);
/*  476 */       if ((paramInt1 == 32) && (i == 0))
/*      */       {
/*  479 */         k++;
/*      */       }
/*      */       else {
/*  482 */         for (int n = 0; n < k; n++) {
/*  483 */           paramStringBuilder.append(" ");
/*      */         }
/*  485 */         k = 0;
/*  486 */         paramStringBuilder.append((char)paramInt1);
/*      */       }
/*  488 */       paramInt1 = paramReader.read();
/*  489 */       j = 0;
/*  490 */     }while (!isTerminator(paramInt1, paramInt2));
/*      */ 
/*  492 */     if ((paramInt2 == 3) && (k > 0)) {
/*  493 */       throw new IOException("Incorrect AVA RFC2253 format - trailing space must be escaped");
/*      */     }
/*      */ 
/*  498 */     if (localArrayList.size() > 0) {
/*  499 */       String str2 = getEmbeddedHexString(localArrayList);
/*  500 */       paramStringBuilder.append(str2);
/*  501 */       localArrayList.clear();
/*      */     }
/*      */ 
/*  506 */     if ((this.oid.equals(PKCS9Attribute.EMAIL_ADDRESS_OID)) || ((this.oid.equals(X500Name.DOMAIN_COMPONENT_OID)) && (!PRESERVE_OLD_DC_ENCODING)))
/*      */     {
/*  510 */       return new DerValue((byte)22, paramStringBuilder.toString());
/*  511 */     }if (bool) {
/*  512 */       return new DerValue(paramStringBuilder.toString());
/*      */     }
/*  514 */     return new DerValue((byte)12, paramStringBuilder.toString());
/*      */   }
/*      */ 
/*      */   private static Byte getEmbeddedHexPair(int paramInt, Reader paramReader)
/*      */     throws IOException
/*      */   {
/*  521 */     if ("0123456789ABCDEF".indexOf(Character.toUpperCase((char)paramInt)) >= 0) {
/*  522 */       int i = readChar(paramReader, "unexpected EOF - escaped hex value must include two valid digits");
/*      */ 
/*  525 */       if ("0123456789ABCDEF".indexOf(Character.toUpperCase((char)i)) >= 0) {
/*  526 */         int j = Character.digit((char)paramInt, 16);
/*  527 */         int k = Character.digit((char)i, 16);
/*  528 */         return new Byte((byte)((j << 4) + k));
/*      */       }
/*  530 */       throw new IOException("escaped hex value must include two valid digits");
/*      */     }
/*      */ 
/*  534 */     return null;
/*      */   }
/*      */ 
/*      */   private static String getEmbeddedHexString(List<Byte> paramList) throws IOException
/*      */   {
/*  539 */     int i = paramList.size();
/*  540 */     byte[] arrayOfByte = new byte[i];
/*  541 */     for (int j = 0; j < i; j++) {
/*  542 */       arrayOfByte[j] = ((Byte)paramList.get(j)).byteValue();
/*      */     }
/*  544 */     return new String(arrayOfByte, "UTF8");
/*      */   }
/*      */ 
/*      */   private static boolean isTerminator(int paramInt1, int paramInt2) {
/*  548 */     switch (paramInt1) {
/*      */     case -1:
/*      */     case 43:
/*      */     case 44:
/*  552 */       return true;
/*      */     case 59:
/*      */     case 62:
/*  555 */       return paramInt2 != 3;
/*      */     }
/*  557 */     return false;
/*      */   }
/*      */ 
/*      */   private static int readChar(Reader paramReader, String paramString) throws IOException
/*      */   {
/*  562 */     int i = paramReader.read();
/*  563 */     if (i == -1) {
/*  564 */       throw new IOException(paramString);
/*      */     }
/*  566 */     return i;
/*      */   }
/*      */ 
/*      */   private static boolean trailingSpace(Reader paramReader) throws IOException
/*      */   {
/*  571 */     boolean bool = false;
/*      */ 
/*  573 */     if (!paramReader.markSupported())
/*      */     {
/*  575 */       return true;
/*      */     }
/*      */ 
/*  580 */     paramReader.mark(9999);
/*      */     while (true) {
/*  582 */       int i = paramReader.read();
/*  583 */       if (i == -1) {
/*  584 */         bool = true;
/*  585 */         break;
/*  586 */       }if (i != 32)
/*      */       {
/*  588 */         if (i == 92) {
/*  589 */           int j = paramReader.read();
/*  590 */           if (j != 32) {
/*  591 */             bool = false;
/*  592 */             break;
/*      */           }
/*      */         } else {
/*  595 */           bool = false;
/*  596 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  600 */     paramReader.reset();
/*  601 */     return bool;
/*      */   }
/*      */ 
/*      */   AVA(DerValue paramDerValue)
/*      */     throws IOException
/*      */   {
/*  608 */     if (paramDerValue.tag != 48) {
/*  609 */       throw new IOException("AVA not a sequence");
/*      */     }
/*  611 */     this.oid = X500Name.intern(paramDerValue.data.getOID());
/*  612 */     this.value = paramDerValue.data.getDerValue();
/*      */ 
/*  614 */     if (paramDerValue.data.available() != 0)
/*  615 */       throw new IOException("AVA, extra bytes = " + paramDerValue.data.available());
/*      */   }
/*      */ 
/*      */   AVA(DerInputStream paramDerInputStream)
/*      */     throws IOException
/*      */   {
/*  621 */     this(paramDerInputStream.getDerValue());
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/*  625 */     if (this == paramObject) {
/*  626 */       return true;
/*      */     }
/*  628 */     if (!(paramObject instanceof AVA)) {
/*  629 */       return false;
/*      */     }
/*  631 */     AVA localAVA = (AVA)paramObject;
/*  632 */     return toRFC2253CanonicalString().equals(localAVA.toRFC2253CanonicalString());
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  642 */     return toRFC2253CanonicalString().hashCode();
/*      */   }
/*      */ 
/*      */   public void encode(DerOutputStream paramDerOutputStream)
/*      */     throws IOException
/*      */   {
/*  649 */     derEncode(paramDerOutputStream);
/*      */   }
/*      */ 
/*      */   public void derEncode(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*  662 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  663 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*      */ 
/*  665 */     localDerOutputStream1.putOID(this.oid);
/*  666 */     this.value.encode(localDerOutputStream1);
/*  667 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*  668 */     paramOutputStream.write(localDerOutputStream2.toByteArray());
/*      */   }
/*      */ 
/*      */   private String toKeyword(int paramInt, Map<String, String> paramMap) {
/*  672 */     return AVAKeyword.getKeyword(this.oid, paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  680 */     return toKeywordValueString(toKeyword(1, Collections.emptyMap()));
/*      */   }
/*      */ 
/*      */   public String toRFC1779String()
/*      */   {
/*  690 */     return toRFC1779String(Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   public String toRFC1779String(Map<String, String> paramMap)
/*      */   {
/*  700 */     return toKeywordValueString(toKeyword(2, paramMap));
/*      */   }
/*      */ 
/*      */   public String toRFC2253String()
/*      */   {
/*  709 */     return toRFC2253String(Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   public String toRFC2253String(Map<String, String> paramMap)
/*      */   {
/*  726 */     StringBuilder localStringBuilder1 = new StringBuilder(100);
/*  727 */     localStringBuilder1.append(toKeyword(3, paramMap));
/*  728 */     localStringBuilder1.append('=');
/*      */     Object localObject;
/*  739 */     if (((localStringBuilder1.charAt(0) >= '0') && (localStringBuilder1.charAt(0) <= '9')) || (!isDerString(this.value, false)))
/*      */     {
/*  742 */       localObject = null;
/*      */       try {
/*  744 */         localObject = this.value.toByteArray();
/*      */       } catch (IOException localIOException1) {
/*  746 */         throw new IllegalArgumentException("DER Value conversion");
/*      */       }
/*  748 */       localStringBuilder1.append('#');
/*  749 */       for (int i = 0; i < localObject.length; i++) {
/*  750 */         int j = localObject[i];
/*  751 */         localStringBuilder1.append(Character.forDigit(0xF & j >>> 4, 16));
/*  752 */         localStringBuilder1.append(Character.forDigit(0xF & j, 16));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  763 */       localObject = null;
/*      */       try {
/*  765 */         localObject = new String(this.value.getDataBytes(), "UTF8");
/*      */       } catch (IOException localIOException2) {
/*  767 */         throw new IllegalArgumentException("DER Value conversion");
/*      */       }
/*      */ 
/*  794 */       StringBuilder localStringBuilder2 = new StringBuilder();
/*      */       char c;
/*  796 */       for (int k = 0; k < ((String)localObject).length(); k++) {
/*  797 */         m = ((String)localObject).charAt(k);
/*  798 */         if ((DerValue.isPrintableStringChar(m)) || (",=+<>#;\"\\".indexOf(m) >= 0))
/*      */         {
/*  802 */           if (",=+<>#;\"\\".indexOf(m) >= 0) {
/*  803 */             localStringBuilder2.append('\\');
/*      */           }
/*      */ 
/*  807 */           localStringBuilder2.append(m);
/*      */         }
/*  809 */         else if (m == 0)
/*      */         {
/*  811 */           localStringBuilder2.append("\\00");
/*      */         }
/*  813 */         else if ((debug != null) && (Debug.isOn("ava")))
/*      */         {
/*  817 */           byte[] arrayOfByte = null;
/*      */           try {
/*  819 */             arrayOfByte = Character.toString(m).getBytes("UTF8");
/*      */           } catch (IOException localIOException3) {
/*  821 */             throw new IllegalArgumentException("DER Value conversion");
/*      */           }
/*      */ 
/*  824 */           for (i1 = 0; i1 < arrayOfByte.length; i1++) {
/*  825 */             localStringBuilder2.append('\\');
/*  826 */             c = Character.forDigit(0xF & arrayOfByte[i1] >>> 4, 16);
/*      */ 
/*  828 */             localStringBuilder2.append(Character.toUpperCase(c));
/*  829 */             c = Character.forDigit(0xF & arrayOfByte[i1], 16);
/*      */ 
/*  831 */             localStringBuilder2.append(Character.toUpperCase(c));
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  836 */           localStringBuilder2.append(m);
/*      */         }
/*      */       }
/*      */ 
/*  840 */       char[] arrayOfChar = localStringBuilder2.toString().toCharArray();
/*  841 */       localStringBuilder2 = new StringBuilder();
/*      */ 
/*  845 */       for (int m = 0; (m < arrayOfChar.length) && (
/*  846 */         (arrayOfChar[m] == ' ') || (arrayOfChar[m] == '\r')); m++);
/*  851 */       for (int n = arrayOfChar.length - 1; (n >= 0) && (
/*  852 */         (arrayOfChar[n] == ' ') || (arrayOfChar[n] == '\r')); n--);
/*  858 */       for (int i1 = 0; i1 < arrayOfChar.length; i1++) {
/*  859 */         c = arrayOfChar[i1];
/*  860 */         if ((i1 < m) || (i1 > n)) {
/*  861 */           localStringBuilder2.append('\\');
/*      */         }
/*  863 */         localStringBuilder2.append(c);
/*      */       }
/*  865 */       localStringBuilder1.append(localStringBuilder2.toString());
/*      */     }
/*  867 */     return localStringBuilder1.toString();
/*      */   }
/*      */ 
/*      */   public String toRFC2253CanonicalString()
/*      */   {
/*  878 */     StringBuilder localStringBuilder1 = new StringBuilder(40);
/*  879 */     localStringBuilder1.append(toKeyword(3, Collections.emptyMap()));
/*      */ 
/*  881 */     localStringBuilder1.append('=');
/*      */ 
/*  892 */     if (((localStringBuilder1.charAt(0) >= '0') && (localStringBuilder1.charAt(0) <= '9')) || (!isDerString(this.value, true)))
/*      */     {
/*  895 */       localObject = null;
/*      */       try {
/*  897 */         localObject = this.value.toByteArray();
/*      */       } catch (IOException localIOException1) {
/*  899 */         throw new IllegalArgumentException("DER Value conversion");
/*      */       }
/*  901 */       localStringBuilder1.append('#');
/*  902 */       for (int i = 0; i < localObject.length; i++) {
/*  903 */         int j = localObject[i];
/*  904 */         localStringBuilder1.append(Character.forDigit(0xF & j >>> 4, 16));
/*  905 */         localStringBuilder1.append(Character.forDigit(0xF & j, 16));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  916 */       localObject = null;
/*      */       try {
/*  918 */         localObject = new String(this.value.getDataBytes(), "UTF8");
/*      */       } catch (IOException localIOException2) {
/*  920 */         throw new IllegalArgumentException("DER Value conversion");
/*      */       }
/*      */ 
/*  942 */       StringBuilder localStringBuilder2 = new StringBuilder();
/*  943 */       int k = 0;
/*      */ 
/*  945 */       for (int m = 0; m < ((String)localObject).length(); m++) {
/*  946 */         char c = ((String)localObject).charAt(m);
/*      */ 
/*  948 */         if ((DerValue.isPrintableStringChar(c)) || (",+<>;\"\\".indexOf(c) >= 0) || ((m == 0) && (c == '#')))
/*      */         {
/*  953 */           if (((m == 0) && (c == '#')) || (",+<>;\"\\".indexOf(c) >= 0)) {
/*  954 */             localStringBuilder2.append('\\');
/*      */           }
/*      */ 
/*  958 */           if (!Character.isWhitespace(c)) {
/*  959 */             k = 0;
/*  960 */             localStringBuilder2.append(c);
/*      */           }
/*  962 */           else if (k == 0)
/*      */           {
/*  964 */             k = 1;
/*  965 */             localStringBuilder2.append(c);
/*      */           }
/*      */ 
/*      */         }
/*  972 */         else if ((debug != null) && (Debug.isOn("ava")))
/*      */         {
/*  977 */           k = 0;
/*      */ 
/*  979 */           byte[] arrayOfByte = null;
/*      */           try {
/*  981 */             arrayOfByte = Character.toString(c).getBytes("UTF8");
/*      */           } catch (IOException localIOException3) {
/*  983 */             throw new IllegalArgumentException("DER Value conversion");
/*      */           }
/*      */ 
/*  986 */           for (int n = 0; n < arrayOfByte.length; n++) {
/*  987 */             localStringBuilder2.append('\\');
/*  988 */             localStringBuilder2.append(Character.forDigit(0xF & arrayOfByte[n] >>> 4, 16));
/*      */ 
/*  990 */             localStringBuilder2.append(Character.forDigit(0xF & arrayOfByte[n], 16));
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  997 */           k = 0;
/*  998 */           localStringBuilder2.append(c);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1003 */       localStringBuilder1.append(localStringBuilder2.toString().trim());
/*      */     }
/*      */ 
/* 1006 */     Object localObject = localStringBuilder1.toString();
/* 1007 */     localObject = ((String)localObject).toUpperCase(Locale.US).toLowerCase(Locale.US);
/* 1008 */     return Normalizer.normalize((CharSequence)localObject, Normalizer.Form.NFKD);
/*      */   }
/*      */ 
/*      */   private static boolean isDerString(DerValue paramDerValue, boolean paramBoolean)
/*      */   {
/* 1015 */     if (paramBoolean) {
/* 1016 */       switch (paramDerValue.tag) {
/*      */       case 12:
/*      */       case 19:
/* 1019 */         return true;
/*      */       }
/* 1021 */       return false;
/*      */     }
/*      */ 
/* 1024 */     switch (paramDerValue.tag) {
/*      */     case 12:
/*      */     case 19:
/*      */     case 20:
/*      */     case 22:
/*      */     case 27:
/*      */     case 30:
/* 1031 */       return true;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 21:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 28:
/* 1033 */     case 29: } return false;
/*      */   }
/*      */ 
/*      */   boolean hasRFC2253Keyword()
/*      */   {
/* 1039 */     return AVAKeyword.hasKeyword(this.oid, 3);
/*      */   }
/*      */ 
/*      */   private String toKeywordValueString(String paramString)
/*      */   {
/* 1048 */     StringBuilder localStringBuilder1 = new StringBuilder(40);
/*      */ 
/* 1050 */     localStringBuilder1.append(paramString);
/* 1051 */     localStringBuilder1.append("=");
/*      */     try
/*      */     {
/* 1054 */       String str = this.value.getAsString();
/*      */ 
/* 1056 */       if (str == null)
/*      */       {
/* 1063 */         byte[] arrayOfByte1 = this.value.toByteArray();
/*      */ 
/* 1065 */         localStringBuilder1.append('#');
/* 1066 */         for (int j = 0; j < arrayOfByte1.length; j++) {
/* 1067 */           localStringBuilder1.append("0123456789ABCDEF".charAt(arrayOfByte1[j] >> 4 & 0xF));
/* 1068 */           localStringBuilder1.append("0123456789ABCDEF".charAt(arrayOfByte1[j] & 0xF));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1073 */         int i = 0;
/* 1074 */         StringBuilder localStringBuilder2 = new StringBuilder();
/* 1075 */         int k = 0;
/*      */ 
/* 1083 */         int m = str.length();
/* 1084 */         int n = (m > 1) && (str.charAt(0) == '"') && (str.charAt(m - 1) == '"') ? 1 : 0;
/*      */ 
/* 1088 */         for (int i1 = 0; i1 < m; i1++) {
/* 1089 */           char c1 = str.charAt(i1);
/* 1090 */           if ((n != 0) && ((i1 == 0) || (i1 == m - 1))) {
/* 1091 */             localStringBuilder2.append(c1);
/*      */           }
/* 1094 */           else if ((DerValue.isPrintableStringChar(c1)) || (",+=\n<>#;\\\"".indexOf(c1) >= 0))
/*      */           {
/* 1098 */             if ((i == 0) && (((i1 == 0) && ((c1 == ' ') || (c1 == '\n'))) || (",+=\n<>#;\\\"".indexOf(c1) >= 0)))
/*      */             {
/* 1101 */               i = 1;
/*      */             }
/*      */ 
/* 1105 */             if ((c1 != ' ') && (c1 != '\n'))
/*      */             {
/* 1107 */               if ((c1 == '"') || (c1 == '\\')) {
/* 1108 */                 localStringBuilder2.append('\\');
/*      */               }
/* 1110 */               k = 0;
/*      */             } else {
/* 1112 */               if ((i == 0) && (k != 0)) {
/* 1113 */                 i = 1;
/*      */               }
/* 1115 */               k = 1;
/*      */             }
/*      */ 
/* 1118 */             localStringBuilder2.append(c1);
/*      */           }
/* 1120 */           else if ((debug != null) && (Debug.isOn("ava")))
/*      */           {
/* 1125 */             k = 0;
/*      */ 
/* 1128 */             byte[] arrayOfByte2 = Character.toString(c1).getBytes("UTF8");
/*      */ 
/* 1130 */             for (int i2 = 0; i2 < arrayOfByte2.length; i2++) {
/* 1131 */               localStringBuilder2.append('\\');
/* 1132 */               char c2 = Character.forDigit(0xF & arrayOfByte2[i2] >>> 4, 16);
/*      */ 
/* 1134 */               localStringBuilder2.append(Character.toUpperCase(c2));
/* 1135 */               c2 = Character.forDigit(0xF & arrayOfByte2[i2], 16);
/*      */ 
/* 1137 */               localStringBuilder2.append(Character.toUpperCase(c2));
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1143 */             k = 0;
/* 1144 */             localStringBuilder2.append(c1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1149 */         if (localStringBuilder2.length() > 0) {
/* 1150 */           i1 = localStringBuilder2.charAt(localStringBuilder2.length() - 1);
/* 1151 */           if ((i1 == 32) || (i1 == 10)) {
/* 1152 */             i = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1158 */         if ((n == 0) && (i != 0))
/* 1159 */           localStringBuilder1.append("\"" + localStringBuilder2.toString() + "\"");
/*      */         else
/* 1161 */           localStringBuilder1.append(localStringBuilder2.toString());
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1165 */       throw new IllegalArgumentException("DER Value conversion");
/*      */     }
/*      */ 
/* 1168 */     return localStringBuilder1.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AVA
 * JD-Core Version:    0.6.2
 */