/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ class Attribute
/*      */   implements Comparable
/*      */ {
/*      */   Layout def;
/*      */   byte[] bytes;
/*      */   Object fixups;
/*      */   private static final Map<List<Attribute>, List<Attribute>> canonLists;
/*      */   private static final Map<Layout, Attribute> attributes;
/*      */   private static final Map<Layout, Attribute> standardDefs;
/*      */   static final byte EK_INT = 1;
/*      */   static final byte EK_BCI = 2;
/*      */   static final byte EK_BCO = 3;
/*      */   static final byte EK_FLAG = 4;
/*      */   static final byte EK_REPL = 5;
/*      */   static final byte EK_REF = 6;
/*      */   static final byte EK_UN = 7;
/*      */   static final byte EK_CASE = 8;
/*      */   static final byte EK_CALL = 9;
/*      */   static final byte EK_CBLE = 10;
/*      */   static final byte EF_SIGN = 1;
/*      */   static final byte EF_DELTA = 2;
/*      */   static final byte EF_NULL = 4;
/*      */   static final byte EF_BACK = 8;
/*      */   static final int NO_BAND_INDEX = -1;
/*      */ 
/*      */   public String name()
/*      */   {
/*   55 */     return this.def.name(); } 
/*   56 */   public Layout layout() { return this.def; } 
/*   57 */   public byte[] bytes() { return this.bytes; } 
/*   58 */   public int size() { return this.bytes.length; } 
/*   59 */   public ConstantPool.Entry getNameRef() { return this.def.getNameRef(); }
/*      */ 
/*      */   private Attribute(Attribute paramAttribute) {
/*   62 */     this.def = paramAttribute.def;
/*   63 */     this.bytes = paramAttribute.bytes;
/*   64 */     this.fixups = paramAttribute.fixups;
/*      */   }
/*      */ 
/*      */   public Attribute(Layout paramLayout, byte[] paramArrayOfByte, Object paramObject) {
/*   68 */     this.def = paramLayout;
/*   69 */     this.bytes = paramArrayOfByte;
/*   70 */     this.fixups = paramObject;
/*   71 */     Fixups.setBytes(paramObject, paramArrayOfByte);
/*      */   }
/*      */   public Attribute(Layout paramLayout, byte[] paramArrayOfByte) {
/*   74 */     this(paramLayout, paramArrayOfByte, null);
/*      */   }
/*      */ 
/*      */   public Attribute addContent(byte[] paramArrayOfByte, Object paramObject) {
/*   78 */     assert (isCanonical());
/*   79 */     if ((paramArrayOfByte.length == 0) && (paramObject == null))
/*   80 */       return this;
/*   81 */     Attribute localAttribute = new Attribute(this);
/*   82 */     localAttribute.bytes = paramArrayOfByte;
/*   83 */     localAttribute.fixups = paramObject;
/*   84 */     Fixups.setBytes(paramObject, paramArrayOfByte);
/*   85 */     return localAttribute;
/*      */   }
/*      */   public Attribute addContent(byte[] paramArrayOfByte) {
/*   88 */     return addContent(paramArrayOfByte, null);
/*      */   }
/*      */ 
/*      */   public void finishRefs(ConstantPool.Index paramIndex) {
/*   92 */     if (this.fixups != null) {
/*   93 */       Fixups.finishRefs(this.fixups, this.bytes, paramIndex);
/*   94 */       this.fixups = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isCanonical() {
/*   99 */     return this == this.def.canon;
/*      */   }
/*      */ 
/*      */   public int compareTo(Object paramObject) {
/*  103 */     Attribute localAttribute = (Attribute)paramObject;
/*  104 */     return this.def.compareTo(localAttribute.def);
/*      */   }
/*      */ 
/*      */   public static List<Attribute> getCanonList(List<Attribute> paramList)
/*      */   {
/*  116 */     synchronized (canonLists) {
/*  117 */       Object localObject1 = (List)canonLists.get(paramList);
/*  118 */       if (localObject1 == null) {
/*  119 */         localObject1 = new ArrayList(paramList.size());
/*  120 */         ((List)localObject1).addAll(paramList);
/*  121 */         localObject1 = Collections.unmodifiableList((List)localObject1);
/*  122 */         canonLists.put(paramList, localObject1);
/*      */       }
/*  124 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Attribute find(int paramInt, String paramString1, String paramString2)
/*      */   {
/*  130 */     Layout localLayout = Layout.makeKey(paramInt, paramString1, paramString2);
/*  131 */     synchronized (attributes) {
/*  132 */       Attribute localAttribute = (Attribute)attributes.get(localLayout);
/*  133 */       if (localAttribute == null) {
/*  134 */         localAttribute = new Layout(paramInt, paramString1, paramString2).canonicalInstance();
/*  135 */         attributes.put(localLayout, localAttribute);
/*      */       }
/*  137 */       return localAttribute;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Layout keyForLookup(int paramInt, String paramString) {
/*  142 */     return Layout.makeKey(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public static Attribute lookup(Map<Layout, Attribute> paramMap, int paramInt, String paramString)
/*      */   {
/*  149 */     if (paramMap == null) {
/*  150 */       paramMap = standardDefs;
/*      */     }
/*  152 */     return (Attribute)paramMap.get(Layout.makeKey(paramInt, paramString));
/*      */   }
/*      */ 
/*      */   public static Attribute define(Map<Layout, Attribute> paramMap, int paramInt, String paramString1, String paramString2)
/*      */   {
/*  157 */     Attribute localAttribute = find(paramInt, paramString1, paramString2);
/*  158 */     paramMap.put(Layout.makeKey(paramInt, paramString1), localAttribute);
/*  159 */     return localAttribute;
/*      */   }
/*      */ 
/*      */   public static String contextName(int paramInt)
/*      */   {
/*  281 */     switch (paramInt) { case 0:
/*  282 */       return "class";
/*      */     case 1:
/*  283 */       return "field";
/*      */     case 2:
/*  284 */       return "method";
/*      */     case 3:
/*  285 */       return "code";
/*      */     }
/*  287 */     return null;
/*      */   }
/*      */ 
/*      */   void visitRefs(Holder paramHolder, int paramInt, final Collection<ConstantPool.Entry> paramCollection)
/*      */   {
/*  684 */     if (paramInt == 0) {
/*  685 */       paramCollection.add(getNameRef());
/*      */     }
/*      */ 
/*  688 */     if (this.bytes.length == 0) return;
/*  689 */     if (!this.def.hasRefs) return;
/*  690 */     if (this.fixups != null) {
/*  691 */       Fixups.visitRefs(this.fixups, paramCollection);
/*  692 */       return;
/*      */     }
/*      */ 
/*  695 */     this.def.parse(paramHolder, this.bytes, 0, this.bytes.length, new ValueStream() {
/*      */       public void putInt(int paramAnonymousInt1, int paramAnonymousInt2) {
/*      */       }
/*      */ 
/*      */       public void putRef(int paramAnonymousInt, ConstantPool.Entry paramAnonymousEntry) {
/*  700 */         paramCollection.add(paramAnonymousEntry);
/*      */       }
/*      */       public int encodeBCI(int paramAnonymousInt) {
/*  703 */         return paramAnonymousInt;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void parse(Holder paramHolder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ValueStream paramValueStream) {
/*  709 */     this.def.parse(paramHolder, paramArrayOfByte, paramInt1, paramInt2, paramValueStream);
/*      */   }
/*      */   public Object unparse(ValueStream paramValueStream, ByteArrayOutputStream paramByteArrayOutputStream) {
/*  712 */     return this.def.unparse(paramValueStream, paramByteArrayOutputStream);
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  716 */     return this.def + "{" + (this.bytes == null ? -1 : size()) + "}" + (this.fixups == null ? "" : this.fixups.toString());
/*      */   }
/*      */ 
/*      */   public static String normalizeLayoutString(String paramString)
/*      */   {
/*  729 */     StringBuilder localStringBuilder = new StringBuilder();
/*  730 */     int i = 0; for (int j = paramString.length(); i < j; ) {
/*  731 */       char c = paramString.charAt(i++);
/*  732 */       if (c > ' ')
/*      */       {
/*      */         int k;
/*      */         int m;
/*  735 */         if (c == '#')
/*      */         {
/*  737 */           k = paramString.indexOf('\n', i);
/*  738 */           m = paramString.indexOf('\r', i);
/*  739 */           if (k < 0) k = j;
/*  740 */           if (m < 0) m = j;
/*  741 */           i = Math.min(k, m);
/*  742 */         } else if (c == '\\')
/*      */         {
/*  744 */           localStringBuilder.append(paramString.charAt(i++));
/*  745 */         } else if ((c == '0') && (paramString.startsWith("0x", i - 1)))
/*      */         {
/*  747 */           k = i - 1;
/*  748 */           m = k + 2;
/*  749 */           while (m < j) {
/*  750 */             int n = paramString.charAt(m);
/*  751 */             if (((n < 48) || (n > 57)) && ((n < 97) || (n > 102)))
/*      */               break;
/*  753 */             m++;
/*      */           }
/*      */ 
/*  757 */           if (m > k) {
/*  758 */             String str2 = paramString.substring(k, m);
/*  759 */             localStringBuilder.append(Integer.decode(str2));
/*  760 */             i = m;
/*      */           } else {
/*  762 */             localStringBuilder.append(c);
/*      */           }
/*      */         } else {
/*  765 */           localStringBuilder.append(c);
/*      */         }
/*      */       }
/*      */     }
/*  768 */     String str1 = localStringBuilder.toString();
/*      */ 
/*  774 */     return str1;
/*      */   }
/*      */ 
/*      */   static Attribute.Layout.Element[] tokenizeLayout(Layout paramLayout, int paramInt, String paramString)
/*      */   {
/*  841 */     ArrayList localArrayList = new ArrayList(paramString.length());
/*  842 */     tokenizeLayout(paramLayout, paramInt, paramString, localArrayList);
/*  843 */     Attribute.Layout.Element[] arrayOfElement = new Attribute.Layout.Element[localArrayList.size()];
/*  844 */     localArrayList.toArray(arrayOfElement);
/*  845 */     return arrayOfElement;
/*      */   }
/*      */ 
/*      */   static void tokenizeLayout(Layout paramLayout, int paramInt, String paramString, List<Attribute.Layout.Element> paramList) {
/*  849 */     int i = 0;
/*  850 */     int j = paramString.length(); for (int k = 0; k < j; ) {
/*  851 */       int m = k;
/*      */ 
/*  853 */       paramLayout.getClass(); Attribute.Layout.Element localElement1 = new Attribute.Layout.Element(paramLayout);
/*      */       byte b;
/*      */       int n;
/*      */       int i3;
/*      */       Object localObject2;
/*  857 */       switch (paramString.charAt(k++)) { case 'B':
/*      */       case 'H':
/*      */       case 'I':
/*      */       case 'V':
/*  860 */         b = 1;
/*  861 */         k--;
/*  862 */         k = tokenizeUInt(localElement1, paramString, k);
/*  863 */         break;
/*      */       case 'S':
/*  865 */         b = 1;
/*  866 */         k--;
/*  867 */         k = tokenizeSInt(localElement1, paramString, k);
/*  868 */         break;
/*      */       case 'P':
/*  870 */         b = 2;
/*  871 */         if (paramString.charAt(k++) == 'O')
/*      */         {
/*      */           Attribute.Layout.Element tmp305_303 = localElement1; tmp305_303.flags = ((byte)(tmp305_303.flags | 0x2));
/*      */ 
/*  875 */           if (i == 0)
/*  876 */             k = -k;
/*  877 */           else k++; 
/*      */         }
/*  879 */         else { k--;
/*  880 */           k = tokenizeUInt(localElement1, paramString, k); }
/*  881 */         break;
/*      */       case 'O':
/*  883 */         b = 3;
/*      */         Attribute.Layout.Element tmp352_350 = localElement1; tmp352_350.flags = ((byte)(tmp352_350.flags | 0x2));
/*      */ 
/*  886 */         if (i == 0)
/*  887 */           k = -k;
/*  888 */         else k = tokenizeSInt(localElement1, paramString, k);
/*  889 */         break;
/*      */       case 'F':
/*  891 */         b = 4;
/*  892 */         k = tokenizeUInt(localElement1, paramString, k);
/*  893 */         break;
/*      */       case 'N':
/*  895 */         b = 5;
/*  896 */         k = tokenizeUInt(localElement1, paramString, k);
/*  897 */         if (paramString.charAt(k++) != '[') {
/*  898 */           k = -k; } else {
/*  899 */           k = skipBody(paramString, n = k);
/*  900 */           localElement1.body = tokenizeLayout(paramLayout, paramInt, paramString.substring(n, k++));
/*      */         }
/*  902 */         break;
/*      */       case 'T':
/*  904 */         b = 7;
/*  905 */         k = tokenizeSInt(localElement1, paramString, k);
/*  906 */         ArrayList localArrayList = new ArrayList();
/*      */         Object localObject1;
/*      */         while (true) { if (paramString.charAt(k++) != '(') {
/*  910 */             k = -k; break;
/*  911 */           }i1 = k;
/*  912 */           k = paramString.indexOf(')', k);
/*  913 */           localObject1 = paramString.substring(i1, k++);
/*  914 */           i3 = ((String)localObject1).length();
/*  915 */           if (paramString.charAt(k++) != '[') {
/*  916 */             k = -k; break;
/*      */           }
/*  918 */           if (paramString.charAt(k) == ']')
/*  919 */             n = k;
/*      */           else
/*  921 */             k = skipBody(paramString, n = k);
/*  922 */           localObject2 = tokenizeLayout(paramLayout, paramInt, paramString.substring(n, k++));
/*      */ 
/*  925 */           if (i3 == 0) {
/*  926 */             paramLayout.getClass(); Attribute.Layout.Element localElement2 = new Attribute.Layout.Element(paramLayout);
/*  927 */             localElement2.body = ((Attribute.Layout.Element[])localObject2);
/*  928 */             localElement2.kind = 8;
/*  929 */             localElement2.removeBand();
/*  930 */             localArrayList.add(localElement2);
/*  931 */             break;
/*      */           }
/*      */ 
/*  934 */           int i4 = 1;
/*      */           int i6;
/*  935 */           for (int i5 = 0; ; i5 = i6 + 1)
/*      */           {
/*  937 */             i6 = ((String)localObject1).indexOf(',', i5);
/*  938 */             if (i6 < 0) i6 = i3;
/*  939 */             String str2 = ((String)localObject1).substring(i5, i6);
/*  940 */             if (str2.length() == 0) {
/*  941 */               str2 = "empty";
/*      */             }
/*      */ 
/*  944 */             int i9 = findCaseDash(str2, 0);
/*      */             int i7;
/*      */             int i8;
/*  945 */             if (i9 >= 0) {
/*  946 */               i7 = parseIntBefore(str2, i9);
/*  947 */               i8 = parseIntAfter(str2, i9);
/*  948 */               if (i7 >= i8) {
/*  949 */                 k = -k; break;
/*      */               }
/*      */             } else { i7 = i8 = Integer.parseInt(str2); }
/*      */ 
/*      */ 
/*  954 */             for (; ; i7++) {
/*  955 */               paramLayout.getClass(); Attribute.Layout.Element localElement3 = new Attribute.Layout.Element(paramLayout);
/*  956 */               localElement3.body = ((Attribute.Layout.Element[])localObject2);
/*  957 */               localElement3.kind = 8;
/*  958 */               localElement3.removeBand();
/*  959 */               if (i4 == 0)
/*      */               {
/*      */                 Attribute.Layout.Element tmp820_818 = localElement3; tmp820_818.flags = ((byte)(tmp820_818.flags | 0x8));
/*  962 */               }i4 = 0;
/*  963 */               localElement3.value = i7;
/*  964 */               localArrayList.add(localElement3);
/*  965 */               if (i7 == i8) break;
/*      */             }
/*  967 */             if (i6 == i3)
/*      */             {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*  973 */         localElement1.body = new Attribute.Layout.Element[localArrayList.size()];
/*  974 */         localArrayList.toArray(localElement1.body);
/*  975 */         localElement1.kind = b;
/*  976 */         for (int i1 = 0; i1 < localElement1.body.length - 1; i1++) {
/*  977 */           localObject1 = localElement1.body[i1];
/*  978 */           if (matchCase(localElement1, ((Attribute.Layout.Element)localObject1).value) != localObject1)
/*      */           {
/*  980 */             k = -k; break;
/*      */           }
/*      */         }
/*  983 */         break;
/*      */       case '(':
/*  985 */         b = 9;
/*  986 */         localElement1.removeBand();
/*  987 */         k = paramString.indexOf(')', k);
/*  988 */         String str1 = paramString.substring(m + 1, k++);
/*  989 */         int i2 = Integer.parseInt(str1);
/*  990 */         i3 = paramInt + i2;
/*  991 */         if ((!(i2 + "").equals(str1)) || (paramLayout.elems == null) || (i3 < 0) || (i3 >= paramLayout.elems.length))
/*      */         {
/*  995 */           k = -k; } else {
/*  996 */           localObject2 = paramLayout.elems[i3];
/*  997 */           assert (((Attribute.Layout.Element)localObject2).kind == 10);
/*  998 */           localElement1.value = i3;
/*  999 */           localElement1.body = new Attribute.Layout.Element[] { localObject2 };
/*      */ 
/* 1001 */           if (i2 <= 0)
/*      */           {
/* 1004 */             Attribute.Layout.Element tmp1148_1146 = localElement1; tmp1148_1146.flags = ((byte)(tmp1148_1146.flags | 0x8));
/*      */             Object tmp1161_1159 = localObject2; tmp1161_1159.flags = ((byte)(tmp1161_1159.flags | 0x8)); }  } break;
/*      */       case 'K':
/* 1008 */         b = 6;
/* 1009 */         switch (paramString.charAt(k++)) { case 'I':
/* 1010 */           localElement1.refKind = 3; break;
/*      */         case 'J':
/* 1011 */           localElement1.refKind = 5; break;
/*      */         case 'F':
/* 1012 */           localElement1.refKind = 4; break;
/*      */         case 'D':
/* 1013 */           localElement1.refKind = 6; break;
/*      */         case 'S':
/* 1014 */           localElement1.refKind = 8; break;
/*      */         case 'Q':
/* 1015 */           localElement1.refKind = 20; break;
/*      */         case 'E':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'M':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'R':
/*      */         default:
/* 1016 */           k = -k; } break;
/*      */       case 'R':
/* 1020 */         b = 6;
/* 1021 */         switch (paramString.charAt(k++)) { case 'C':
/* 1022 */           localElement1.refKind = 7; break;
/*      */         case 'S':
/* 1023 */           localElement1.refKind = 13; break;
/*      */         case 'D':
/* 1024 */           localElement1.refKind = 12; break;
/*      */         case 'F':
/* 1025 */           localElement1.refKind = 9; break;
/*      */         case 'M':
/* 1026 */           localElement1.refKind = 10; break;
/*      */         case 'I':
/* 1027 */           localElement1.refKind = 11; break;
/*      */         case 'U':
/* 1029 */           localElement1.refKind = 1; break;
/*      */         case 'Q':
/* 1030 */           localElement1.refKind = 19; break;
/*      */         case 'E':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'J':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'R':
/*      */         case 'T':
/*      */         default:
/* 1032 */           k = -k; } break;
/*      */       case ')':
/*      */       case '*':
/*      */       case '+':
/*      */       case ',':
/*      */       case '-':
/*      */       case '.':
/*      */       case '/':
/*      */       case '0':
/*      */       case '1':
/*      */       case '2':
/*      */       case '3':
/*      */       case '4':
/*      */       case '5':
/*      */       case '6':
/*      */       case '7':
/*      */       case '8':
/*      */       case '9':
/*      */       case ':':
/*      */       case ';':
/*      */       case '<':
/*      */       case '=':
/*      */       case '>':
/*      */       case '?':
/*      */       case '@':
/*      */       case 'A':
/*      */       case 'C':
/*      */       case 'D':
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'J':
/*      */       case 'L':
/*      */       case 'M':
/*      */       case 'Q':
/*      */       case 'U':
/*      */       default:
/* 1035 */         k = -k; continue;
/*      */ 
/* 1039 */         if (b == 6)
/*      */         {
/* 1041 */           if (paramString.charAt(k++) == 'N')
/*      */           {
/*      */             Attribute.Layout.Element tmp1554_1552 = localElement1; tmp1554_1552.flags = ((byte)(tmp1554_1552.flags | 0x4));
/* 1043 */             k++;
/*      */           }
/* 1045 */           k--;
/* 1046 */           k = tokenizeUInt(localElement1, paramString, k);
/* 1047 */           paramLayout.hasRefs = true;
/*      */         }
/*      */ 
/* 1050 */         i = b == 2 ? 1 : 0;
/*      */ 
/* 1053 */         localElement1.kind = b;
/* 1054 */         localElement1.layout = paramString.substring(m, k);
/* 1055 */         paramList.add(localElement1); }
/*      */     }
/*      */   }
/*      */ 
/*      */   static String[] splitBodies(String paramString) {
/* 1060 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1062 */     for (int i = 0; i < paramString.length(); i++) {
/* 1063 */       if (paramString.charAt(i++) != '[')
/* 1064 */         paramString.charAt(-i);
/*      */       int j;
/* 1066 */       i = skipBody(paramString, j = i);
/* 1067 */       localArrayList.add(paramString.substring(j, i));
/*      */     }
/* 1069 */     String[] arrayOfString = new String[localArrayList.size()];
/* 1070 */     localArrayList.toArray(arrayOfString);
/* 1071 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private static int skipBody(String paramString, int paramInt) {
/* 1075 */     assert (paramString.charAt(paramInt - 1) == '[');
/* 1076 */     if (paramString.charAt(paramInt) == ']')
/*      */     {
/* 1078 */       return -paramInt;
/*      */     }
/* 1080 */     for (int i = 1; i > 0; ) {
/* 1081 */       switch (paramString.charAt(paramInt++)) { case '[':
/* 1082 */         i++; break;
/*      */       case ']':
/* 1083 */         i--;
/*      */       }
/*      */     }
/* 1086 */     paramInt--;
/* 1087 */     assert (paramString.charAt(paramInt) == ']');
/* 1088 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private static int tokenizeUInt(Attribute.Layout.Element paramElement, String paramString, int paramInt) {
/* 1092 */     switch (paramString.charAt(paramInt++)) { case 'V':
/* 1093 */       paramElement.len = 0; break;
/*      */     case 'B':
/* 1094 */       paramElement.len = 1; break;
/*      */     case 'H':
/* 1095 */       paramElement.len = 2; break;
/*      */     case 'I':
/* 1096 */       paramElement.len = 4; break;
/*      */     default:
/* 1097 */       return -paramInt;
/*      */     }
/* 1099 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private static int tokenizeSInt(Attribute.Layout.Element paramElement, String paramString, int paramInt) {
/* 1103 */     if (paramString.charAt(paramInt) == 'S') {
/* 1104 */       paramElement.flags = ((byte)(paramElement.flags | 0x1));
/* 1105 */       paramInt++;
/*      */     }
/* 1107 */     return tokenizeUInt(paramElement, paramString, paramInt);
/*      */   }
/*      */ 
/*      */   private static boolean isDigit(char paramChar)
/*      */   {
/* 1112 */     return (paramChar >= '0') && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   static int findCaseDash(String paramString, int paramInt)
/*      */   {
/* 1118 */     if (paramInt <= 0) paramInt = 1;
/* 1119 */     int i = paramString.length() - 2;
/*      */     while (true) {
/* 1121 */       int j = paramString.indexOf('-', paramInt);
/* 1122 */       if ((j < 0) || (j > i)) return -1;
/* 1123 */       if (isDigit(paramString.charAt(j - 1))) {
/* 1124 */         char c = paramString.charAt(j + 1);
/* 1125 */         if ((c == '-') && (j + 2 < paramString.length()))
/* 1126 */           c = paramString.charAt(j + 2);
/* 1127 */         if (isDigit(c))
/*      */         {
/* 1129 */           return j;
/*      */         }
/*      */       }
/* 1132 */       paramInt = j + 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static int parseIntBefore(String paramString, int paramInt) {
/* 1137 */     int i = paramInt;
/* 1138 */     int j = i;
/* 1139 */     while ((j > 0) && (isDigit(paramString.charAt(j - 1)))) {
/* 1140 */       j--;
/*      */     }
/* 1142 */     if (j == i) return Integer.parseInt("empty");
/*      */ 
/* 1144 */     if ((j >= 1) && (paramString.charAt(j - 1) == '-')) j--;
/* 1145 */     assert ((j == 0) || (!isDigit(paramString.charAt(j - 1))));
/* 1146 */     return Integer.parseInt(paramString.substring(j, i));
/*      */   }
/*      */ 
/*      */   static int parseIntAfter(String paramString, int paramInt) {
/* 1150 */     int i = paramInt + 1;
/* 1151 */     int j = i;
/* 1152 */     int k = paramString.length();
/* 1153 */     if ((j < k) && (paramString.charAt(j) == '-')) j++;
/* 1154 */     while ((j < k) && (isDigit(paramString.charAt(j)))) {
/* 1155 */       j++;
/*      */     }
/* 1157 */     if (i == j) return Integer.parseInt("empty");
/* 1158 */     return Integer.parseInt(paramString.substring(i, j));
/*      */   }
/*      */ 
/*      */   static String expandCaseDashNotation(String paramString)
/*      */   {
/* 1163 */     int i = findCaseDash(paramString, 0);
/* 1164 */     if (i < 0) return paramString;
/* 1165 */     StringBuilder localStringBuilder = new StringBuilder(paramString.length() * 3);
/* 1166 */     int j = 0;
/*      */     while (true)
/*      */     {
/* 1169 */       localStringBuilder.append(paramString.substring(j, i));
/* 1170 */       j = i + 1;
/*      */ 
/* 1172 */       int k = parseIntBefore(paramString, i);
/* 1173 */       int m = parseIntAfter(paramString, i);
/* 1174 */       assert (k < m);
/* 1175 */       localStringBuilder.append(",");
/* 1176 */       for (int n = k + 1; n < m; n++) {
/* 1177 */         localStringBuilder.append(n);
/* 1178 */         localStringBuilder.append(",");
/*      */       }
/* 1180 */       i = findCaseDash(paramString, j);
/* 1181 */       if (i < 0) break;
/*      */     }
/* 1183 */     localStringBuilder.append(paramString.substring(j));
/* 1184 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   static int parseUsing(Attribute.Layout.Element[] paramArrayOfElement, Holder paramHolder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ValueStream paramValueStream)
/*      */   {
/* 1199 */     int i = 0;
/* 1200 */     int j = 0;
/* 1201 */     int k = paramInt1 + paramInt2;
/* 1202 */     int[] arrayOfInt = { 0 };
/* 1203 */     for (int m = 0; m < paramArrayOfElement.length; m++) {
/* 1204 */       Attribute.Layout.Element localElement1 = paramArrayOfElement[m];
/* 1205 */       int n = localElement1.bandIndex;
/*      */       int i1;
/*      */       int i2;
/*      */       int i3;
/* 1208 */       switch (localElement1.kind) {
/*      */       case 1:
/* 1210 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1211 */         i1 = arrayOfInt[0];
/* 1212 */         paramValueStream.putInt(n, i1);
/* 1213 */         break;
/*      */       case 2:
/* 1215 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1216 */         i2 = arrayOfInt[0];
/* 1217 */         i3 = paramValueStream.encodeBCI(i2);
/* 1218 */         if (!localElement1.flagTest((byte)2))
/*      */         {
/* 1220 */           i1 = i3;
/*      */         }
/*      */         else {
/* 1223 */           i1 = i3 - j;
/*      */         }
/* 1225 */         i = i2;
/* 1226 */         j = i3;
/* 1227 */         paramValueStream.putInt(n, i1);
/* 1228 */         break;
/*      */       case 3:
/* 1230 */         assert (localElement1.flagTest((byte)2));
/*      */ 
/* 1232 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1233 */         i2 = i + arrayOfInt[0];
/* 1234 */         i3 = paramValueStream.encodeBCI(i2);
/* 1235 */         i1 = i3 - j;
/* 1236 */         i = i2;
/* 1237 */         j = i3;
/* 1238 */         paramValueStream.putInt(n, i1);
/* 1239 */         break;
/*      */       case 4:
/* 1241 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1242 */         i1 = arrayOfInt[0];
/* 1243 */         paramValueStream.putInt(n, i1);
/* 1244 */         break;
/*      */       case 5:
/* 1246 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1247 */         i1 = arrayOfInt[0];
/* 1248 */         paramValueStream.putInt(n, i1);
/* 1249 */         for (int i4 = 0; i4 < i1; i4++) {
/* 1250 */           paramInt1 = parseUsing(localElement1.body, paramHolder, paramArrayOfByte, paramInt1, k - paramInt1, paramValueStream);
/*      */         }
/* 1252 */         break;
/*      */       case 7:
/* 1254 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1255 */         i1 = arrayOfInt[0];
/* 1256 */         paramValueStream.putInt(n, i1);
/* 1257 */         Attribute.Layout.Element localElement2 = matchCase(localElement1, i1);
/* 1258 */         paramInt1 = parseUsing(localElement2.body, paramHolder, paramArrayOfByte, paramInt1, k - paramInt1, paramValueStream);
/*      */ 
/* 1260 */         break;
/*      */       case 9:
/* 1263 */         assert (localElement1.body.length == 1);
/* 1264 */         assert (localElement1.body[0].kind == 10);
/* 1265 */         if (localElement1.flagTest((byte)8))
/* 1266 */           paramValueStream.noteBackCall(localElement1.value);
/* 1267 */         paramInt1 = parseUsing(localElement1.body[0].body, paramHolder, paramArrayOfByte, paramInt1, k - paramInt1, paramValueStream);
/* 1268 */         break;
/*      */       case 6:
/* 1270 */         paramInt1 = parseInt(localElement1, paramArrayOfByte, paramInt1, arrayOfInt);
/* 1271 */         int i5 = arrayOfInt[0];
/*      */         Object localObject;
/* 1273 */         if (i5 == 0) {
/* 1274 */           localObject = null;
/*      */         } else {
/* 1276 */           localObject = paramHolder.getCPMap()[i5];
/* 1277 */           if ((localElement1.refKind == 13) && (((ConstantPool.Entry)localObject).getTag() == 1))
/*      */           {
/* 1280 */             String str = ((ConstantPool.Entry)localObject).stringValue();
/* 1281 */             localObject = ConstantPool.getSignatureEntry(str);
/* 1282 */           } else if (localElement1.refKind == 20) {
/* 1283 */             assert (((ConstantPool.Entry)localObject).getTag() >= 3);
/* 1284 */             if ((!$assertionsDisabled) && (((ConstantPool.Entry)localObject).getTag() > 8)) throw new AssertionError(); 
/*      */           }
/* 1285 */           else if ((localElement1.refKind != 19) && 
/* 1286 */             (!$assertionsDisabled) && (localElement1.refKind != ((ConstantPool.Entry)localObject).getTag())) { throw new AssertionError(); }
/*      */ 
/*      */         }
/* 1289 */         paramValueStream.putRef(n, (ConstantPool.Entry)localObject);
/* 1290 */         break;
/*      */       case 8:
/*      */       default:
/* 1291 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/*      */     }
/* 1294 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   static Attribute.Layout.Element matchCase(Attribute.Layout.Element paramElement, int paramInt)
/*      */   {
/* 1299 */     assert (paramElement.kind == 7);
/* 1300 */     int i = paramElement.body.length - 1;
/* 1301 */     for (int j = 0; j < i; j++) {
/* 1302 */       Attribute.Layout.Element localElement = paramElement.body[j];
/* 1303 */       assert (localElement.kind == 8);
/* 1304 */       if (paramInt == localElement.value)
/* 1305 */         return localElement;
/*      */     }
/* 1307 */     return paramElement.body[i];
/*      */   }
/*      */ 
/*      */   private static int parseInt(Attribute.Layout.Element paramElement, byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt)
/*      */   {
/* 1312 */     int i = 0;
/* 1313 */     int j = paramElement.len * 8;
/*      */ 
/* 1315 */     int k = j;
/*      */     while (true) { k -= 8; if (k < 0) break;
/* 1316 */       i += ((paramArrayOfByte[(paramInt++)] & 0xFF) << k);
/*      */     }
/* 1318 */     if ((j < 32) && (paramElement.flagTest((byte)1)))
/*      */     {
/* 1320 */       k = 32 - j;
/* 1321 */       i = i << k >> k;
/*      */     }
/* 1323 */     paramArrayOfInt[0] = i;
/* 1324 */     return paramInt;
/*      */   }
/*      */ 
/*      */   static void unparseUsing(Attribute.Layout.Element[] paramArrayOfElement, Object[] paramArrayOfObject, ValueStream paramValueStream, ByteArrayOutputStream paramByteArrayOutputStream)
/*      */   {
/* 1333 */     int i = 0;
/* 1334 */     int j = 0;
/* 1335 */     for (int k = 0; k < paramArrayOfElement.length; k++) {
/* 1336 */       Attribute.Layout.Element localElement1 = paramArrayOfElement[k];
/* 1337 */       int m = localElement1.bandIndex;
/*      */       int n;
/*      */       int i2;
/*      */       int i1;
/* 1340 */       switch (localElement1.kind) {
/*      */       case 1:
/* 1342 */         n = paramValueStream.getInt(m);
/* 1343 */         unparseInt(localElement1, n, paramByteArrayOutputStream);
/* 1344 */         break;
/*      */       case 2:
/* 1346 */         n = paramValueStream.getInt(m);
/* 1347 */         if (!localElement1.flagTest((byte)2))
/*      */         {
/* 1349 */           i2 = n;
/*      */         }
/*      */         else {
/* 1352 */           i2 = j + n;
/*      */         }
/* 1354 */         assert (i == paramValueStream.decodeBCI(j));
/* 1355 */         i1 = paramValueStream.decodeBCI(i2);
/* 1356 */         unparseInt(localElement1, i1, paramByteArrayOutputStream);
/* 1357 */         i = i1;
/* 1358 */         j = i2;
/* 1359 */         break;
/*      */       case 3:
/* 1361 */         n = paramValueStream.getInt(m);
/* 1362 */         assert (localElement1.flagTest((byte)2));
/*      */ 
/* 1364 */         assert (i == paramValueStream.decodeBCI(j));
/* 1365 */         i2 = j + n;
/* 1366 */         i1 = paramValueStream.decodeBCI(i2);
/* 1367 */         unparseInt(localElement1, i1 - i, paramByteArrayOutputStream);
/* 1368 */         i = i1;
/* 1369 */         j = i2;
/* 1370 */         break;
/*      */       case 4:
/* 1372 */         n = paramValueStream.getInt(m);
/* 1373 */         unparseInt(localElement1, n, paramByteArrayOutputStream);
/* 1374 */         break;
/*      */       case 5:
/* 1376 */         n = paramValueStream.getInt(m);
/* 1377 */         unparseInt(localElement1, n, paramByteArrayOutputStream);
/* 1378 */         for (int i3 = 0; i3 < n; i3++) {
/* 1379 */           unparseUsing(localElement1.body, paramArrayOfObject, paramValueStream, paramByteArrayOutputStream);
/*      */         }
/* 1381 */         break;
/*      */       case 7:
/* 1383 */         n = paramValueStream.getInt(m);
/* 1384 */         unparseInt(localElement1, n, paramByteArrayOutputStream);
/* 1385 */         Attribute.Layout.Element localElement2 = matchCase(localElement1, n);
/* 1386 */         unparseUsing(localElement2.body, paramArrayOfObject, paramValueStream, paramByteArrayOutputStream);
/* 1387 */         break;
/*      */       case 9:
/* 1389 */         assert (localElement1.body.length == 1);
/* 1390 */         assert (localElement1.body[0].kind == 10);
/* 1391 */         unparseUsing(localElement1.body[0].body, paramArrayOfObject, paramValueStream, paramByteArrayOutputStream);
/* 1392 */         break;
/*      */       case 6:
/* 1394 */         ConstantPool.Entry localEntry = paramValueStream.getRef(m);
/*      */         int i4;
/* 1396 */         if (localEntry != null)
/*      */         {
/* 1398 */           paramArrayOfObject[0] = Fixups.add(paramArrayOfObject[0], null, paramByteArrayOutputStream.size(), 0, localEntry);
/*      */ 
/* 1400 */           i4 = 0;
/*      */         } else {
/* 1402 */           i4 = 0;
/*      */         }
/* 1404 */         unparseInt(localElement1, i4, paramByteArrayOutputStream);
/* 1405 */         break;
/*      */       case 8:
/*      */       default:
/* 1406 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void unparseInt(Attribute.Layout.Element paramElement, int paramInt, ByteArrayOutputStream paramByteArrayOutputStream)
/*      */   {
/* 1413 */     int i = paramElement.len * 8;
/* 1414 */     if (i == 0)
/*      */     {
/* 1416 */       return;
/*      */     }
/* 1418 */     if (i < 32) {
/* 1419 */       j = 32 - i;
/*      */       int k;
/* 1421 */       if (paramElement.flagTest((byte)1))
/* 1422 */         k = paramInt << j >> j;
/*      */       else
/* 1424 */         k = paramInt << j >>> j;
/* 1425 */       if (k != paramInt) {
/* 1426 */         throw new InternalError("cannot code in " + paramElement.len + " bytes: " + paramInt);
/*      */       }
/*      */     }
/* 1429 */     int j = i;
/*      */     while (true) { j -= 8; if (j < 0) break;
/* 1430 */       paramByteArrayOutputStream.write((byte)(paramInt >>> j));
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  107 */     canonLists = new HashMap();
/*  108 */     attributes = new HashMap();
/*  109 */     standardDefs = new HashMap();
/*      */ 
/*  163 */     Object localObject = standardDefs;
/*  164 */     define((Map)localObject, 0, "Signature", "RSH");
/*  165 */     define((Map)localObject, 0, "Synthetic", "");
/*  166 */     define((Map)localObject, 0, "Deprecated", "");
/*  167 */     define((Map)localObject, 0, "SourceFile", "RUH");
/*  168 */     define((Map)localObject, 0, "EnclosingMethod", "RCHRDNH");
/*  169 */     define((Map)localObject, 0, "InnerClasses", "NH[RCHRCNHRUNHFH]");
/*      */ 
/*  171 */     define((Map)localObject, 1, "Signature", "RSH");
/*  172 */     define((Map)localObject, 1, "Synthetic", "");
/*  173 */     define((Map)localObject, 1, "Deprecated", "");
/*  174 */     define((Map)localObject, 1, "ConstantValue", "KQH");
/*      */ 
/*  176 */     define((Map)localObject, 2, "Signature", "RSH");
/*  177 */     define((Map)localObject, 2, "Synthetic", "");
/*  178 */     define((Map)localObject, 2, "Deprecated", "");
/*  179 */     define((Map)localObject, 2, "Exceptions", "NH[RCH]");
/*      */ 
/*  182 */     define((Map)localObject, 3, "StackMapTable", "[NH[(1)]][TB(64-127)[(2)](247)[(1)(2)](248-251)[(1)](252)[(1)(2)](253)[(1)(2)(2)](254)[(1)(2)(2)(2)](255)[(1)NH[(2)]NH[(2)]]()[]][H][TB(7)[RCH](8)[PH]()[]]");
/*      */ 
/*  197 */     define((Map)localObject, 3, "LineNumberTable", "NH[PHH]");
/*  198 */     define((Map)localObject, 3, "LocalVariableTable", "NH[PHOHRUHRSHH]");
/*  199 */     define((Map)localObject, 3, "LocalVariableTypeTable", "NH[PHOHRUHRSHH]");
/*      */ 
/*  227 */     localObject = new String[] { normalizeLayoutString("\n  # parameter_annotations :=\n  [ NB[(1)] ]     # forward call to annotations"), normalizeLayoutString("\n  # annotations :=\n  [ NH[(1)] ]     # forward call to annotation\n  \n  # annotation :=\n  [RSH\n    NH[RUH (1)]   # forward call to value\n    ]"), normalizeLayoutString("\n  # value :=\n  [TB # Callable 2 encodes one tagged value.\n    (\\B,\\C,\\I,\\S,\\Z)[KIH]\n    (\\D)[KDH]\n    (\\F)[KFH]\n    (\\J)[KJH]\n    (\\c)[RSH]\n    (\\e)[RSH RUH]\n    (\\s)[RUH]\n    (\\[)[NH[(0)]] # backward self-call to value\n    (\\@)[RSH NH[RUH (0)]] # backward self-call to value\n    ()[] ]") };
/*      */ 
/*  259 */     Map localMap = standardDefs;
/*  260 */     String str1 = localObject[2];
/*  261 */     String str2 = localObject[1] + localObject[2];
/*  262 */     String str3 = localObject[0] + str2;
/*  263 */     for (int i = 0; i < 4; i++) {
/*  264 */       if (i != 3) {
/*  265 */         define(localMap, i, "RuntimeVisibleAnnotations", str2);
/*      */ 
/*  267 */         define(localMap, i, "RuntimeInvisibleAnnotations", str2);
/*      */ 
/*  269 */         if (i == 2) {
/*  270 */           define(localMap, i, "RuntimeVisibleParameterAnnotations", str3);
/*      */ 
/*  272 */           define(localMap, i, "RuntimeInvisibleParameterAnnotations", str3);
/*      */ 
/*  274 */           define(localMap, i, "AnnotationDefault", str1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1187 */     assert (expandCaseDashNotation("1-5").equals("1,2,3,4,5"));
/* 1188 */     assert (expandCaseDashNotation("-2--1").equals("-2,-1"));
/* 1189 */     assert (expandCaseDashNotation("-2-1").equals("-2,-1,0,1"));
/* 1190 */     assert (expandCaseDashNotation("-1-0").equals("-1,0"));
/*      */   }
/*      */ 
/*      */   public static class FormatException extends IOException
/*      */   {
/*      */     private int ctype;
/*      */     private String name;
/*      */     String layout;
/*      */ 
/*      */     public FormatException(String paramString1, int paramInt, String paramString2, String paramString3)
/*      */     {
/*  671 */       super();
/*      */ 
/*  673 */       this.ctype = paramInt;
/*  674 */       this.name = paramString2;
/*  675 */       this.layout = paramString3;
/*      */     }
/*      */ 
/*      */     public FormatException(String paramString1, int paramInt, String paramString2) {
/*  679 */       this(paramString1, paramInt, paramString2, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Holder
/*      */   {
/*      */     protected int flags;
/*      */     protected List<Attribute> attributes;
/*  366 */     static final List<Attribute> noAttributes = Arrays.asList(new Attribute[0]);
/*      */ 
/*      */     protected abstract ConstantPool.Entry[] getCPMap();
/*      */ 
/*      */     public int attributeSize()
/*      */     {
/*  305 */       return this.attributes == null ? 0 : this.attributes.size();
/*      */     }
/*      */ 
/*      */     public void trimToSize() {
/*  309 */       if (this.attributes == null) {
/*  310 */         return;
/*      */       }
/*  312 */       if (this.attributes.isEmpty()) {
/*  313 */         this.attributes = null;
/*  314 */         return;
/*      */       }
/*  316 */       if ((this.attributes instanceof ArrayList)) {
/*  317 */         ArrayList localArrayList = (ArrayList)this.attributes;
/*  318 */         localArrayList.trimToSize();
/*  319 */         int i = 1;
/*  320 */         for (Attribute localAttribute : localArrayList) {
/*  321 */           if (!localAttribute.isCanonical()) {
/*  322 */             i = 0;
/*      */           }
/*  324 */           if (localAttribute.fixups != null) {
/*  325 */             assert (!localAttribute.isCanonical());
/*  326 */             localAttribute.fixups = Fixups.trimToSize(localAttribute.fixups);
/*      */           }
/*      */         }
/*  329 */         if (i != 0)
/*      */         {
/*  333 */           this.attributes = Attribute.getCanonList(localArrayList);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addAttribute(Attribute paramAttribute) {
/*  339 */       if (this.attributes == null)
/*  340 */         this.attributes = new ArrayList(3);
/*  341 */       else if (!(this.attributes instanceof ArrayList))
/*  342 */         this.attributes = new ArrayList(this.attributes);
/*  343 */       this.attributes.add(paramAttribute);
/*      */     }
/*      */ 
/*      */     public Attribute removeAttribute(Attribute paramAttribute) {
/*  347 */       if (this.attributes == null) return null;
/*  348 */       if (!this.attributes.contains(paramAttribute)) return null;
/*  349 */       if (!(this.attributes instanceof ArrayList))
/*  350 */         this.attributes = new ArrayList(this.attributes);
/*  351 */       this.attributes.remove(paramAttribute);
/*  352 */       return paramAttribute;
/*      */     }
/*      */ 
/*      */     public Attribute getAttribute(int paramInt) {
/*  356 */       return (Attribute)this.attributes.get(paramInt);
/*      */     }
/*      */ 
/*      */     protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  360 */       if (this.attributes == null) return;
/*  361 */       for (Attribute localAttribute : this.attributes)
/*  362 */         localAttribute.visitRefs(this, paramInt, paramCollection);
/*      */     }
/*      */ 
/*      */     public List<Attribute> getAttributes()
/*      */     {
/*  369 */       if (this.attributes == null)
/*  370 */         return noAttributes;
/*  371 */       return this.attributes;
/*      */     }
/*      */ 
/*      */     public void setAttributes(List<Attribute> paramList) {
/*  375 */       if (paramList.isEmpty())
/*  376 */         this.attributes = null;
/*      */       else
/*  378 */         this.attributes = paramList;
/*      */     }
/*      */ 
/*      */     public Attribute getAttribute(String paramString) {
/*  382 */       if (this.attributes == null) return null;
/*  383 */       for (Attribute localAttribute : this.attributes) {
/*  384 */         if (localAttribute.name().equals(paramString))
/*  385 */           return localAttribute;
/*      */       }
/*  387 */       return null;
/*      */     }
/*      */ 
/*      */     public Attribute getAttribute(Attribute.Layout paramLayout) {
/*  391 */       if (this.attributes == null) return null;
/*  392 */       for (Attribute localAttribute : this.attributes) {
/*  393 */         if (localAttribute.layout() == paramLayout)
/*  394 */           return localAttribute;
/*      */       }
/*  396 */       return null;
/*      */     }
/*      */ 
/*      */     public Attribute removeAttribute(String paramString) {
/*  400 */       return removeAttribute(getAttribute(paramString));
/*      */     }
/*      */ 
/*      */     public Attribute removeAttribute(Attribute.Layout paramLayout) {
/*  404 */       return removeAttribute(getAttribute(paramLayout));
/*      */     }
/*      */ 
/*      */     public void strip(String paramString) {
/*  408 */       removeAttribute(getAttribute(paramString));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Layout
/*      */     implements Comparable
/*      */   {
/*      */     int ctype;
/*      */     String name;
/*      */     boolean hasRefs;
/*      */     String layout;
/*      */     int bandCount;
/*      */     Element[] elems;
/*      */     Attribute canon;
/*  617 */     private static final Element[] noElems = new Element[0];
/*      */ 
/*      */     public int ctype()
/*      */     {
/*  459 */       return this.ctype; } 
/*  460 */     public String name() { return this.name; } 
/*  461 */     public String layout() { return this.layout; } 
/*  462 */     public Attribute canonicalInstance() { return this.canon; }
/*      */ 
/*      */     public ConstantPool.Entry getNameRef() {
/*  465 */       return ConstantPool.getUtf8Entry(name());
/*      */     }
/*      */ 
/*      */     public boolean isEmpty() {
/*  469 */       return this.layout.isEmpty();
/*      */     }
/*      */ 
/*      */     public Layout(int paramInt, String paramString1, String paramString2) {
/*  473 */       this.ctype = paramInt;
/*  474 */       this.name = paramString1.intern();
/*  475 */       this.layout = paramString2.intern();
/*  476 */       assert (paramInt < 4);
/*  477 */       boolean bool = paramString2.startsWith("[");
/*      */       try {
/*  479 */         if (!bool) {
/*  480 */           this.elems = Attribute.tokenizeLayout(this, -1, paramString2);
/*      */         } else {
/*  482 */           String[] arrayOfString = Attribute.splitBodies(paramString2);
/*      */ 
/*  484 */           Element[] arrayOfElement = new Element[arrayOfString.length];
/*  485 */           this.elems = arrayOfElement;
/*      */           Element localElement;
/*  486 */           for (int i = 0; i < arrayOfElement.length; i++) {
/*  487 */             localElement = new Element();
/*  488 */             localElement.kind = 10;
/*  489 */             localElement.removeBand();
/*  490 */             localElement.bandIndex = -1;
/*  491 */             localElement.layout = arrayOfString[i];
/*  492 */             arrayOfElement[i] = localElement;
/*      */           }
/*      */ 
/*  495 */           for (i = 0; i < arrayOfElement.length; i++) {
/*  496 */             localElement = arrayOfElement[i];
/*  497 */             localElement.body = Attribute.tokenizeLayout(this, i, arrayOfString[i]);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
/*      */       {
/*  503 */         throw new RuntimeException("Bad attribute layout: " + paramString2, localStringIndexOutOfBoundsException);
/*      */       }
/*      */ 
/*  507 */       this.canon = new Attribute(this, Constants.noBytes);
/*      */     }
/*      */     private Layout() {
/*      */     }
/*  511 */     static Layout makeKey(int paramInt, String paramString1, String paramString2) { Layout localLayout = new Layout();
/*  512 */       localLayout.ctype = paramInt;
/*  513 */       localLayout.name = paramString1.intern();
/*  514 */       localLayout.layout = paramString2.intern();
/*  515 */       assert (paramInt < 4);
/*  516 */       return localLayout; }
/*      */ 
/*      */     static Layout makeKey(int paramInt, String paramString) {
/*  519 */       return makeKey(paramInt, paramString, "");
/*      */     }
/*      */ 
/*      */     public Attribute addContent(byte[] paramArrayOfByte, Object paramObject) {
/*  523 */       return this.canon.addContent(paramArrayOfByte, paramObject);
/*      */     }
/*      */     public Attribute addContent(byte[] paramArrayOfByte) {
/*  526 */       return this.canon.addContent(paramArrayOfByte, null);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  530 */       return (paramObject != null) && (paramObject.getClass() == Layout.class) && (equals((Layout)paramObject));
/*      */     }
/*      */ 
/*      */     public boolean equals(Layout paramLayout) {
/*  534 */       return (this.name.equals(paramLayout.name)) && (this.layout.equals(paramLayout.layout)) && (this.ctype == paramLayout.ctype);
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  539 */       return ((17 + this.name.hashCode()) * 37 + this.layout.hashCode()) * 37 + this.ctype;
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject)
/*      */     {
/*  544 */       Layout localLayout = (Layout)paramObject;
/*      */ 
/*  546 */       int i = this.name.compareTo(localLayout.name);
/*  547 */       if (i != 0) return i;
/*  548 */       i = this.layout.compareTo(localLayout.layout);
/*  549 */       if (i != 0) return i;
/*  550 */       return this.ctype - localLayout.ctype;
/*      */     }
/*      */     public String toString() {
/*  553 */       String str = Attribute.contextName(this.ctype) + "." + this.name + "[" + this.layout + "]";
/*      */ 
/*  555 */       assert ((str = stringForDebug()) != null);
/*  556 */       return str;
/*      */     }
/*      */     private String stringForDebug() {
/*  559 */       return Attribute.contextName(this.ctype) + "." + this.name + Arrays.asList(this.elems);
/*      */     }
/*      */ 
/*      */     public boolean hasCallables()
/*      */     {
/*  615 */       return (this.elems.length > 0) && (this.elems[0].kind == 10);
/*      */     }
/*      */ 
/*      */     public Element[] getCallables() {
/*  619 */       if (hasCallables()) {
/*  620 */         Element[] arrayOfElement = (Element[])Arrays.copyOf(this.elems, this.elems.length);
/*  621 */         return arrayOfElement;
/*      */       }
/*  623 */       return noElems;
/*      */     }
/*      */     public Element[] getEntryPoint() {
/*  626 */       if (hasCallables()) {
/*  627 */         return this.elems[0].body;
/*      */       }
/*  629 */       Element[] arrayOfElement = (Element[])Arrays.copyOf(this.elems, this.elems.length);
/*  630 */       return arrayOfElement;
/*      */     }
/*      */ 
/*      */     public void parse(Attribute.Holder paramHolder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, Attribute.ValueStream paramValueStream)
/*      */     {
/*  639 */       int i = Attribute.parseUsing(getEntryPoint(), paramHolder, paramArrayOfByte, paramInt1, paramInt2, paramValueStream);
/*      */ 
/*  641 */       if (i != paramInt1 + paramInt2)
/*  642 */         throw new InternalError("layout parsed " + (i - paramInt1) + " out of " + paramInt2 + " bytes");
/*      */     }
/*      */ 
/*      */     public Object unparse(Attribute.ValueStream paramValueStream, ByteArrayOutputStream paramByteArrayOutputStream)
/*      */     {
/*  650 */       Object[] arrayOfObject = { null };
/*  651 */       Attribute.unparseUsing(getEntryPoint(), arrayOfObject, paramValueStream, paramByteArrayOutputStream);
/*  652 */       return arrayOfObject[0];
/*      */     }
/*      */ 
/*      */     public String layoutForPackageMajver(int paramInt) {
/*  656 */       if (paramInt <= 150)
/*      */       {
/*  658 */         return Attribute.expandCaseDashNotation(this.layout);
/*      */       }
/*  660 */       return this.layout;
/*      */     }
/*      */ 
/*      */     public class Element
/*      */     {
/*      */       String layout;
/*      */       byte flags;
/*      */       byte kind;
/*      */       byte len;
/*      */       byte refKind;
/*      */       int bandIndex;
/*      */       int value;
/*      */       Element[] body;
/*      */ 
/*      */       boolean flagTest(byte paramByte)
/*      */       {
/*  573 */         return (this.flags & paramByte) != 0;
/*      */       }
/*      */       Element() {
/*  576 */         this.bandIndex = (Attribute.Layout.this.bandCount++);
/*      */       }
/*      */ 
/*      */       void removeBand() {
/*  580 */         Attribute.Layout.this.bandCount -= 1;
/*  581 */         assert (this.bandIndex == Attribute.Layout.this.bandCount);
/*  582 */         this.bandIndex = -1;
/*      */       }
/*      */ 
/*      */       public boolean hasBand() {
/*  586 */         return this.bandIndex >= 0;
/*      */       }
/*      */       public String toString() {
/*  589 */         String str = this.layout;
/*      */ 
/*  591 */         assert ((str = stringForDebug()) != null);
/*  592 */         return str;
/*      */       }
/*      */       private String stringForDebug() {
/*  595 */         Element[] arrayOfElement = this.body;
/*  596 */         switch (this.kind) {
/*      */         case 9:
/*  598 */           arrayOfElement = null;
/*  599 */           break;
/*      */         case 8:
/*  601 */           if (flagTest((byte)8))
/*  602 */             arrayOfElement = null;
/*      */           break;
/*      */         }
/*  605 */         return this.layout + (!hasBand() ? "" : new StringBuilder().append("#").append(this.bandIndex).toString()) + "<" + (this.flags == 0 ? "" : new StringBuilder().append("").append(this.flags).toString()) + this.kind + this.len + (this.refKind == 0 ? "" : new StringBuilder().append("").append(this.refKind).toString()) + ">" + (this.value == 0 ? "" : new StringBuilder().append("(").append(this.value).append(")").toString()) + (arrayOfElement == null ? "" : new StringBuilder().append("").append(Arrays.asList(arrayOfElement)).toString());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class ValueStream
/*      */   {
/*      */     public int getInt(int paramInt)
/*      */     {
/*  416 */       throw undef(); } 
/*  417 */     public void putInt(int paramInt1, int paramInt2) { throw undef(); } 
/*  418 */     public ConstantPool.Entry getRef(int paramInt) { throw undef(); } 
/*  419 */     public void putRef(int paramInt, ConstantPool.Entry paramEntry) { throw undef(); } 
/*      */     public int decodeBCI(int paramInt) {
/*  421 */       throw undef(); } 
/*  422 */     public int encodeBCI(int paramInt) { throw undef(); } 
/*      */     public void noteBackCall(int paramInt) {
/*      */     }
/*  425 */     private RuntimeException undef() { return new UnsupportedOperationException("ValueStream method"); }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Attribute
 * JD-Core Version:    0.6.2
 */