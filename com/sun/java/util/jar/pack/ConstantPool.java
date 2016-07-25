/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.AbstractList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ abstract class ConstantPool
/*      */ {
/*      */   protected static final Entry[] noRefs;
/*      */   protected static final ClassEntry[] noClassRefs;
/*      */   static final byte[] TAGS_IN_ORDER;
/*      */   static final byte[] TAG_ORDER;
/*      */ 
/*      */   static int verbose()
/*      */   {
/*   47 */     return Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
/*      */   }
/*      */ 
/*      */   public static synchronized Utf8Entry getUtf8Entry(String paramString)
/*      */   {
/*   55 */     Map localMap = Utils.getUtf8Entries();
/*   56 */     Utf8Entry localUtf8Entry = (Utf8Entry)localMap.get(paramString);
/*   57 */     if (localUtf8Entry == null) {
/*   58 */       localUtf8Entry = new Utf8Entry(paramString);
/*   59 */       localMap.put(localUtf8Entry.stringValue(), localUtf8Entry);
/*      */     }
/*   61 */     return localUtf8Entry;
/*      */   }
/*      */ 
/*      */   public static synchronized ClassEntry getClassEntry(String paramString) {
/*   65 */     Map localMap = Utils.getClassEntries();
/*   66 */     ClassEntry localClassEntry = (ClassEntry)localMap.get(paramString);
/*   67 */     if (localClassEntry == null) {
/*   68 */       localClassEntry = new ClassEntry(getUtf8Entry(paramString));
/*   69 */       assert (paramString.equals(localClassEntry.stringValue()));
/*   70 */       localMap.put(localClassEntry.stringValue(), localClassEntry);
/*      */     }
/*   72 */     return localClassEntry;
/*      */   }
/*      */ 
/*      */   public static synchronized LiteralEntry getLiteralEntry(Comparable paramComparable) {
/*   76 */     Map localMap = Utils.getLiteralEntries();
/*   77 */     Object localObject = (LiteralEntry)localMap.get(paramComparable);
/*   78 */     if (localObject == null) {
/*   79 */       if ((paramComparable instanceof String))
/*   80 */         localObject = new StringEntry(getUtf8Entry((String)paramComparable));
/*      */       else
/*   82 */         localObject = new NumberEntry((Number)paramComparable);
/*   83 */       localMap.put(paramComparable, localObject);
/*      */     }
/*   85 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static synchronized StringEntry getStringEntry(String paramString) {
/*   89 */     return (StringEntry)getLiteralEntry(paramString);
/*      */   }
/*      */ 
/*      */   public static synchronized SignatureEntry getSignatureEntry(String paramString)
/*      */   {
/*   94 */     Map localMap = Utils.getSignatureEntries();
/*   95 */     SignatureEntry localSignatureEntry = (SignatureEntry)localMap.get(paramString);
/*   96 */     if (localSignatureEntry == null) {
/*   97 */       localSignatureEntry = new SignatureEntry(paramString);
/*   98 */       assert (localSignatureEntry.stringValue().equals(paramString));
/*   99 */       localMap.put(paramString, localSignatureEntry);
/*      */     }
/*  101 */     return localSignatureEntry;
/*      */   }
/*      */ 
/*      */   public static SignatureEntry getSignatureEntry(Utf8Entry paramUtf8Entry, ClassEntry[] paramArrayOfClassEntry) {
/*  105 */     return getSignatureEntry(SignatureEntry.stringValueOf(paramUtf8Entry, paramArrayOfClassEntry));
/*      */   }
/*      */ 
/*      */   public static synchronized DescriptorEntry getDescriptorEntry(Utf8Entry paramUtf8Entry, SignatureEntry paramSignatureEntry)
/*      */   {
/*  110 */     Map localMap = Utils.getDescriptorEntries();
/*  111 */     String str = DescriptorEntry.stringValueOf(paramUtf8Entry, paramSignatureEntry);
/*  112 */     DescriptorEntry localDescriptorEntry = (DescriptorEntry)localMap.get(str);
/*  113 */     if (localDescriptorEntry == null) {
/*  114 */       localDescriptorEntry = new DescriptorEntry(paramUtf8Entry, paramSignatureEntry);
/*      */ 
/*  116 */       assert (localDescriptorEntry.stringValue().equals(str)) : (localDescriptorEntry.stringValue() + " != " + str);
/*  117 */       localMap.put(str, localDescriptorEntry);
/*      */     }
/*  119 */     return localDescriptorEntry;
/*      */   }
/*      */ 
/*      */   public static DescriptorEntry getDescriptorEntry(Utf8Entry paramUtf8Entry1, Utf8Entry paramUtf8Entry2) {
/*  123 */     return getDescriptorEntry(paramUtf8Entry1, getSignatureEntry(paramUtf8Entry2.stringValue()));
/*      */   }
/*      */ 
/*      */   public static synchronized MemberEntry getMemberEntry(byte paramByte, ClassEntry paramClassEntry, DescriptorEntry paramDescriptorEntry)
/*      */   {
/*  128 */     Map localMap = Utils.getMemberEntries();
/*  129 */     String str = MemberEntry.stringValueOf(paramByte, paramClassEntry, paramDescriptorEntry);
/*  130 */     MemberEntry localMemberEntry = (MemberEntry)localMap.get(str);
/*  131 */     if (localMemberEntry == null) {
/*  132 */       localMemberEntry = new MemberEntry(paramByte, paramClassEntry, paramDescriptorEntry);
/*      */ 
/*  134 */       assert (localMemberEntry.stringValue().equals(str)) : (localMemberEntry.stringValue() + " != " + str);
/*  135 */       localMap.put(str, localMemberEntry);
/*      */     }
/*  137 */     return localMemberEntry;
/*      */   }
/*      */ 
/*      */   static boolean isMemberTag(byte paramByte)
/*      */   {
/*  237 */     switch (paramByte) {
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*  241 */       return true;
/*      */     }
/*  243 */     return false;
/*      */   }
/*      */ 
/*      */   static byte numberTagOf(Number paramNumber) {
/*  247 */     if ((paramNumber instanceof Integer)) return 3;
/*  248 */     if ((paramNumber instanceof Float)) return 4;
/*  249 */     if ((paramNumber instanceof Long)) return 5;
/*  250 */     if ((paramNumber instanceof Double)) return 6;
/*  251 */     throw new RuntimeException("bad literal value " + paramNumber);
/*      */   }
/*      */ 
/*      */   static int compareSignatures(String paramString1, String paramString2)
/*      */   {
/*  610 */     return compareSignatures(paramString1, paramString2, null, null);
/*      */   }
/*      */ 
/*      */   static int compareSignatures(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*      */   {
/*  615 */     int i = paramString1.charAt(0);
/*  616 */     int j = paramString2.charAt(0);
/*      */ 
/*  618 */     if ((i != 40) && (j == 40)) return -1;
/*  619 */     if ((j != 40) && (i == 40)) return 1;
/*  620 */     if (paramArrayOfString1 == null) paramArrayOfString1 = structureSignature(paramString1);
/*  621 */     if (paramArrayOfString2 == null) paramArrayOfString2 = structureSignature(paramString2);
/*      */ 
/*  629 */     if (paramArrayOfString1.length != paramArrayOfString2.length) return paramArrayOfString1.length - paramArrayOfString2.length;
/*  630 */     int k = paramArrayOfString1.length;
/*  631 */     int m = k;
/*      */     while (true) { m--; if (m < 0) break;
/*  632 */       int n = paramArrayOfString1[m].compareTo(paramArrayOfString2[m]);
/*  633 */       if (n != 0) return n;
/*      */     }
/*  635 */     assert (paramString1.equals(paramString2));
/*  636 */     return 0;
/*      */   }
/*      */ 
/*      */   static int countClassParts(Utf8Entry paramUtf8Entry) {
/*  640 */     int i = 0;
/*  641 */     String str = paramUtf8Entry.stringValue();
/*  642 */     for (int j = 0; j < str.length(); j++) {
/*  643 */       if (str.charAt(j) == 'L') i++;
/*      */     }
/*  645 */     return i;
/*      */   }
/*      */ 
/*      */   static String flattenSignature(String[] paramArrayOfString) {
/*  649 */     String str1 = paramArrayOfString[0];
/*  650 */     if (paramArrayOfString.length == 1) return str1;
/*  651 */     int i = str1.length();
/*  652 */     for (int j = 1; j < paramArrayOfString.length; j++) {
/*  653 */       i += paramArrayOfString[j].length();
/*      */     }
/*  655 */     char[] arrayOfChar = new char[i];
/*  656 */     int k = 0;
/*  657 */     int m = 1;
/*  658 */     for (int n = 0; n < str1.length(); n++) {
/*  659 */       int i1 = str1.charAt(n);
/*  660 */       arrayOfChar[(k++)] = i1;
/*  661 */       if (i1 == 76) {
/*  662 */         String str2 = paramArrayOfString[(m++)];
/*  663 */         str2.getChars(0, str2.length(), arrayOfChar, k);
/*  664 */         k += str2.length();
/*      */       }
/*      */     }
/*      */ 
/*  668 */     assert (k == i);
/*  669 */     assert (m == paramArrayOfString.length);
/*  670 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   private static int skipTo(char paramChar, String paramString, int paramInt) {
/*  674 */     paramInt = paramString.indexOf(paramChar, paramInt);
/*  675 */     return paramInt >= 0 ? paramInt : paramString.length();
/*      */   }
/*      */ 
/*      */   static String[] structureSignature(String paramString) {
/*  679 */     int i = paramString.indexOf('L');
/*  680 */     if (i < 0) {
/*  681 */       localObject = new String[] { paramString };
/*  682 */       return localObject;
/*      */     }
/*      */ 
/*  692 */     Object localObject = null;
/*  693 */     String[] arrayOfString = null;
/*  694 */     for (int j = 0; j <= 1; j++)
/*      */     {
/*  696 */       int k = 0;
/*  697 */       int m = 1;
/*  698 */       int n = 0; int i1 = 0;
/*  699 */       int i2 = 0;
/*      */       int i4;
/*  700 */       for (int i3 = i + 1; i3 > 0; i3 = paramString.indexOf('L', i4) + 1)
/*      */       {
/*  703 */         if (n < i3) n = skipTo(';', paramString, i3);
/*  704 */         if (i1 < i3) i1 = skipTo('<', paramString, i3);
/*  705 */         i4 = n < i1 ? n : i1;
/*  706 */         if (j != 0) {
/*  707 */           paramString.getChars(i2, i3, (char[])localObject, k);
/*  708 */           arrayOfString[m] = paramString.substring(i3, i4);
/*      */         }
/*  710 */         k += i3 - i2;
/*  711 */         m++;
/*  712 */         i2 = i4;
/*      */       }
/*  714 */       if (j != 0) {
/*  715 */         paramString.getChars(i2, paramString.length(), (char[])localObject, k);
/*  716 */         break;
/*      */       }
/*  718 */       k += paramString.length() - i2;
/*  719 */       localObject = new char[k];
/*  720 */       arrayOfString = new String[m];
/*      */     }
/*  722 */     arrayOfString[0] = new String((char[])localObject);
/*      */ 
/*  724 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public static Index makeIndex(String paramString, Entry[] paramArrayOfEntry)
/*      */   {
/*  898 */     return new Index(paramString, paramArrayOfEntry);
/*      */   }
/*      */ 
/*      */   public static Index makeIndex(String paramString, Collection<Entry> paramCollection)
/*      */   {
/*  903 */     return new Index(paramString, paramCollection);
/*      */   }
/*      */ 
/*      */   public static void sort(Index paramIndex)
/*      */   {
/*  910 */     paramIndex.clearIndex();
/*  911 */     Arrays.sort(paramIndex.cpMap);
/*  912 */     if (verbose() > 2)
/*  913 */       System.out.println("sorted " + paramIndex.dumpString());
/*      */   }
/*      */ 
/*      */   public static Index[] partition(Index paramIndex, int[] paramArrayOfInt)
/*      */   {
/*  924 */     ArrayList localArrayList = new ArrayList();
/*  925 */     Entry[] arrayOfEntry = paramIndex.cpMap;
/*  926 */     assert (paramArrayOfInt.length == arrayOfEntry.length);
/*      */     Object localObject;
/*  927 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/*  928 */       j = paramArrayOfInt[i];
/*  929 */       if (j >= 0) {
/*  930 */         while (j >= localArrayList.size()) {
/*  931 */           localArrayList.add(null);
/*      */         }
/*  933 */         localObject = (List)localArrayList.get(j);
/*  934 */         if (localObject == null) {
/*  935 */           localArrayList.set(j, localObject = new ArrayList());
/*      */         }
/*  937 */         ((List)localObject).add(arrayOfEntry[i]);
/*      */       }
/*      */     }
/*  939 */     Index[] arrayOfIndex = new Index[localArrayList.size()];
/*  940 */     for (int j = 0; j < arrayOfIndex.length; j++) {
/*  941 */       localObject = (List)localArrayList.get(j);
/*  942 */       if (localObject != null) {
/*  943 */         arrayOfIndex[j] = new Index(paramIndex.debugName + "/part#" + j, (Collection)localObject);
/*  944 */         assert (arrayOfIndex[j].indexOf((Entry)((List)localObject).get(0)) == 0);
/*      */       }
/*      */     }
/*  946 */     return arrayOfIndex;
/*      */   }
/*      */ 
/*      */   public static Index[] partitionByTag(Index paramIndex)
/*      */   {
/*  951 */     Entry[] arrayOfEntry = paramIndex.cpMap;
/*  952 */     int[] arrayOfInt = new int[arrayOfEntry.length];
/*  953 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  954 */       Entry localEntry = arrayOfEntry[i];
/*  955 */       arrayOfInt[i] = (localEntry == null ? -1 : localEntry.tag);
/*      */     }
/*  957 */     Object localObject = partition(paramIndex, arrayOfInt);
/*  958 */     for (int j = 0; j < localObject.length; j++) {
/*  959 */       if (localObject[j] != null)
/*  960 */         localObject[j].debugName = tagName(j);
/*      */     }
/*  962 */     if (localObject.length < 14) {
/*  963 */       Index[] arrayOfIndex = new Index[14];
/*  964 */       System.arraycopy(localObject, 0, arrayOfIndex, 0, localObject.length);
/*  965 */       localObject = arrayOfIndex;
/*      */     }
/*  967 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static void completeReferencesIn(Set<Entry> paramSet, boolean paramBoolean)
/*      */   {
/* 1142 */     paramSet.remove(null);
/* 1143 */     ListIterator localListIterator = new ArrayList(paramSet).listIterator(paramSet.size());
/*      */ 
/* 1145 */     while (localListIterator.hasPrevious()) {
/* 1146 */       Object localObject1 = (Entry)localListIterator.previous();
/* 1147 */       localListIterator.remove();
/* 1148 */       assert (localObject1 != null);
/*      */       Object localObject2;
/* 1149 */       if ((paramBoolean) && (((Entry)localObject1).tag == 13)) {
/* 1150 */         SignatureEntry localSignatureEntry = (SignatureEntry)localObject1;
/* 1151 */         localObject2 = localSignatureEntry.asUtf8Entry();
/*      */ 
/* 1153 */         paramSet.remove(localSignatureEntry);
/* 1154 */         paramSet.add(localObject2);
/* 1155 */         localObject1 = localObject2;
/*      */       }
/*      */ 
/* 1158 */       for (int i = 0; ; i++) {
/* 1159 */         localObject2 = ((Entry)localObject1).getRef(i);
/* 1160 */         if (localObject2 == null)
/*      */           break;
/* 1162 */         if (paramSet.add(localObject2))
/* 1163 */           localListIterator.add(localObject2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static double percent(int paramInt1, int paramInt2) {
/* 1169 */     return (int)(10000.0D * paramInt1 / paramInt2 + 0.5D) / 100.0D;
/*      */   }
/*      */ 
/*      */   public static String tagName(int paramInt) {
/* 1173 */     switch (paramInt) { case 1:
/* 1174 */       return "Utf8";
/*      */     case 3:
/* 1175 */       return "Integer";
/*      */     case 4:
/* 1176 */       return "Float";
/*      */     case 5:
/* 1177 */       return "Long";
/*      */     case 6:
/* 1178 */       return "Double";
/*      */     case 7:
/* 1179 */       return "Class";
/*      */     case 8:
/* 1180 */       return "String";
/*      */     case 9:
/* 1181 */       return "Fieldref";
/*      */     case 10:
/* 1182 */       return "Methodref";
/*      */     case 11:
/* 1183 */       return "InterfaceMethodref";
/*      */     case 12:
/* 1184 */       return "NameandType";
/*      */     case 19:
/* 1187 */       return "*All";
/*      */     case 0:
/* 1188 */       return "*None";
/*      */     case 13:
/* 1189 */       return "*Signature";
/*      */     case 2:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/* 1191 */     case 18: } return "tag#" + paramInt;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  728 */     noRefs = new Entry[0];
/*  729 */     noClassRefs = new ClassEntry[0];
/*      */ 
/* 1195 */     TAGS_IN_ORDER = new byte[] { 1, 3, 4, 5, 6, 8, 7, 13, 12, 9, 10, 11 };
/*      */ 
/* 1211 */     TAG_ORDER = new byte[14];
/* 1212 */     for (int i = 0; i < TAGS_IN_ORDER.length; i++)
/* 1213 */       TAG_ORDER[TAGS_IN_ORDER[i]] = ((byte)(i + 1));
/*      */   }
/*      */ 
/*      */   public static class ClassEntry extends ConstantPool.Entry
/*      */   {
/*      */     final ConstantPool.Utf8Entry ref;
/*      */ 
/*      */     public ConstantPool.Entry getRef(int paramInt)
/*      */     {
/*  333 */       return paramInt == 0 ? this.ref : null;
/*      */     }
/*      */     protected int computeValueHash() {
/*  336 */       return this.ref.hashCode() + this.tag;
/*      */     }
/*      */     ClassEntry(ConstantPool.Entry paramEntry) {
/*  339 */       super();
/*  340 */       this.ref = ((ConstantPool.Utf8Entry)paramEntry);
/*  341 */       hashCode();
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/*  344 */       return (paramObject != null) && (paramObject.getClass() == ClassEntry.class) && (((ClassEntry)paramObject).ref.eq(this.ref));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  348 */       int i = superCompareTo(paramObject);
/*  349 */       if (i == 0) {
/*  350 */         i = this.ref.compareTo(((ClassEntry)paramObject).ref);
/*      */       }
/*  352 */       return i;
/*      */     }
/*      */     public String stringValue() {
/*  355 */       return this.ref.stringValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DescriptorEntry extends ConstantPool.Entry {
/*      */     final ConstantPool.Utf8Entry nameRef;
/*      */     final ConstantPool.SignatureEntry typeRef;
/*      */ 
/*  364 */     public ConstantPool.Entry getRef(int paramInt) { if (paramInt == 0) return this.nameRef;
/*  365 */       if (paramInt == 1) return this.typeRef;
/*  366 */       return null; }
/*      */ 
/*      */     DescriptorEntry(ConstantPool.Entry paramEntry1, ConstantPool.Entry paramEntry2) {
/*  369 */       super();
/*  370 */       if ((paramEntry2 instanceof ConstantPool.Utf8Entry)) {
/*  371 */         paramEntry2 = ConstantPool.getSignatureEntry(paramEntry2.stringValue());
/*      */       }
/*  373 */       this.nameRef = ((ConstantPool.Utf8Entry)paramEntry1);
/*  374 */       this.typeRef = ((ConstantPool.SignatureEntry)paramEntry2);
/*  375 */       hashCode();
/*      */     }
/*      */     protected int computeValueHash() {
/*  378 */       int i = this.typeRef.hashCode();
/*  379 */       return this.nameRef.hashCode() + (i << 8) ^ i;
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/*  382 */       if ((paramObject == null) || (paramObject.getClass() != DescriptorEntry.class)) {
/*  383 */         return false;
/*      */       }
/*  385 */       DescriptorEntry localDescriptorEntry = (DescriptorEntry)paramObject;
/*  386 */       return (this.nameRef.eq(localDescriptorEntry.nameRef)) && (this.typeRef.eq(localDescriptorEntry.typeRef));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  390 */       int i = superCompareTo(paramObject);
/*  391 */       if (i == 0) {
/*  392 */         DescriptorEntry localDescriptorEntry = (DescriptorEntry)paramObject;
/*      */ 
/*  394 */         i = this.typeRef.compareTo(localDescriptorEntry.typeRef);
/*  395 */         if (i == 0)
/*  396 */           i = this.nameRef.compareTo(localDescriptorEntry.nameRef);
/*      */       }
/*  398 */       return i;
/*      */     }
/*      */     public String stringValue() {
/*  401 */       return stringValueOf(this.nameRef, this.typeRef);
/*      */     }
/*      */ 
/*      */     static String stringValueOf(ConstantPool.Entry paramEntry1, ConstantPool.Entry paramEntry2) {
/*  405 */       return paramEntry2.stringValue() + "," + paramEntry1.stringValue();
/*      */     }
/*      */ 
/*      */     public String prettyString() {
/*  409 */       return this.nameRef.stringValue() + this.typeRef.prettyString();
/*      */     }
/*      */ 
/*      */     public boolean isMethod() {
/*  413 */       return this.typeRef.isMethod();
/*      */     }
/*      */ 
/*      */     public byte getLiteralTag() {
/*  417 */       return this.typeRef.getLiteralTag();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Entry
/*      */     implements Comparable
/*      */   {
/*      */     protected final byte tag;
/*      */     protected int valueHash;
/*      */ 
/*      */     protected Entry(byte paramByte)
/*      */     {
/*  148 */       this.tag = paramByte;
/*      */     }
/*      */ 
/*      */     public final byte getTag() {
/*  152 */       return this.tag;
/*      */     }
/*      */ 
/*      */     public Entry getRef(int paramInt) {
/*  156 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean eq(Entry paramEntry) {
/*  160 */       assert (paramEntry != null);
/*  161 */       return (this == paramEntry) || (equals(paramEntry));
/*      */     }
/*      */ 
/*      */     public abstract boolean equals(Object paramObject);
/*      */ 
/*      */     public final int hashCode() {
/*  167 */       if (this.valueHash == 0) {
/*  168 */         this.valueHash = computeValueHash();
/*  169 */         if (this.valueHash == 0) this.valueHash = 1;
/*      */       }
/*  171 */       return this.valueHash;
/*      */     }
/*      */     protected abstract int computeValueHash();
/*      */ 
/*      */     public abstract int compareTo(Object paramObject);
/*      */ 
/*      */     protected int superCompareTo(Object paramObject) {
/*  178 */       Entry localEntry = (Entry)paramObject;
/*      */ 
/*  180 */       if (this.tag != localEntry.tag) {
/*  181 */         return ConstantPool.TAG_ORDER[this.tag] - ConstantPool.TAG_ORDER[localEntry.tag];
/*      */       }
/*      */ 
/*  184 */       return 0;
/*      */     }
/*      */ 
/*      */     public final boolean isDoubleWord() {
/*  188 */       return (this.tag == 6) || (this.tag == 5);
/*      */     }
/*      */ 
/*      */     public final boolean tagMatches(int paramInt) {
/*  192 */       return this.tag == paramInt;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  196 */       String str = stringValue();
/*  197 */       if (ConstantPool.verbose() > 4) {
/*  198 */         if (this.valueHash != 0)
/*  199 */           str = str + " hash=" + this.valueHash;
/*  200 */         str = str + " id=" + System.identityHashCode(this);
/*      */       }
/*  202 */       return ConstantPool.tagName(this.tag) + "=" + str;
/*      */     }
/*      */ 
/*      */     public abstract String stringValue();
/*      */   }
/*      */ 
/*      */   public static final class Index extends AbstractList
/*      */   {
/*      */     protected String debugName;
/*      */     protected ConstantPool.Entry[] cpMap;
/*      */     protected boolean flattenSigs;
/*      */     protected ConstantPool.Entry[] indexKey;
/*      */     protected int[] indexValue;
/*      */ 
/*      */     protected ConstantPool.Entry[] getMap()
/*      */     {
/*  738 */       return this.cpMap;
/*      */     }
/*      */     protected Index(String paramString) {
/*  741 */       this.debugName = paramString;
/*      */     }
/*      */     protected Index(String paramString, ConstantPool.Entry[] paramArrayOfEntry) {
/*  744 */       this(paramString);
/*  745 */       setMap(paramArrayOfEntry);
/*      */     }
/*      */     protected void setMap(ConstantPool.Entry[] paramArrayOfEntry) {
/*  748 */       clearIndex();
/*  749 */       this.cpMap = paramArrayOfEntry;
/*      */     }
/*      */     protected Index(String paramString, Collection<ConstantPool.Entry> paramCollection) {
/*  752 */       this(paramString);
/*  753 */       setMap(paramCollection);
/*      */     }
/*      */     protected void setMap(Collection<ConstantPool.Entry> paramCollection) {
/*  756 */       this.cpMap = new ConstantPool.Entry[paramCollection.size()];
/*  757 */       paramCollection.toArray(this.cpMap);
/*  758 */       setMap(this.cpMap);
/*      */     }
/*      */     public int size() {
/*  761 */       return this.cpMap.length;
/*      */     }
/*      */     public Object get(int paramInt) {
/*  764 */       return this.cpMap[paramInt];
/*      */     }
/*      */ 
/*      */     public ConstantPool.Entry getEntry(int paramInt) {
/*  768 */       return this.cpMap[paramInt];
/*      */     }
/*      */ 
/*      */     private int findIndexOf(ConstantPool.Entry paramEntry)
/*      */     {
/*  779 */       if (this.indexKey == null) {
/*  780 */         initializeIndex();
/*      */       }
/*  782 */       int i = findIndexLocation(paramEntry);
/*  783 */       if (this.indexKey[i] != paramEntry) {
/*  784 */         if ((this.flattenSigs) && (paramEntry.tag == 13)) {
/*  785 */           ConstantPool.SignatureEntry localSignatureEntry = (ConstantPool.SignatureEntry)paramEntry;
/*  786 */           return findIndexOf(localSignatureEntry.asUtf8Entry());
/*      */         }
/*  788 */         return -1;
/*      */       }
/*  790 */       int j = this.indexValue[i];
/*  791 */       assert (paramEntry.equals(this.cpMap[j]));
/*  792 */       return j;
/*      */     }
/*      */     public boolean contains(ConstantPool.Entry paramEntry) {
/*  795 */       return findIndexOf(paramEntry) >= 0;
/*      */     }
/*      */ 
/*      */     public int indexOf(ConstantPool.Entry paramEntry) {
/*  799 */       int i = findIndexOf(paramEntry);
/*  800 */       if ((i < 0) && (ConstantPool.verbose() > 0)) {
/*  801 */         System.out.println("not found: " + paramEntry);
/*  802 */         System.out.println("       in: " + dumpString());
/*  803 */         Thread.dumpStack();
/*      */       }
/*  805 */       assert (i >= 0);
/*  806 */       return i;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/*  809 */       return findIndexOf((ConstantPool.Entry)paramObject) >= 0;
/*      */     }
/*      */     public int indexOf(Object paramObject) {
/*  812 */       return findIndexOf((ConstantPool.Entry)paramObject);
/*      */     }
/*      */     public int lastIndexOf(Object paramObject) {
/*  815 */       return indexOf(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean assertIsSorted() {
/*  819 */       for (int i = 1; i < this.cpMap.length; i++) {
/*  820 */         if (this.cpMap[(i - 1)].compareTo(this.cpMap[i]) > 0) {
/*  821 */           System.out.println("Not sorted at " + (i - 1) + "/" + i + ": " + dumpString());
/*  822 */           return false;
/*      */         }
/*      */       }
/*  825 */       return true;
/*      */     }
/*      */ 
/*      */     protected void clearIndex()
/*      */     {
/*  832 */       this.indexKey = null;
/*  833 */       this.indexValue = null;
/*      */     }
/*      */     private int findIndexLocation(ConstantPool.Entry paramEntry) {
/*  836 */       int i = this.indexKey.length;
/*  837 */       int j = paramEntry.hashCode();
/*  838 */       int k = j & i - 1;
/*  839 */       int m = (j >>> 8 | 0x1) & i - 1;
/*      */       while (true) {
/*  841 */         ConstantPool.Entry localEntry = this.indexKey[k];
/*  842 */         if ((localEntry == paramEntry) || (localEntry == null))
/*  843 */           return k;
/*  844 */         k += m;
/*  845 */         if (k >= i) k -= i; 
/*      */       }
/*      */     }
/*      */ 
/*  849 */     private void initializeIndex() { if (ConstantPool.verbose() > 2)
/*  850 */         System.out.println("initialize Index " + this.debugName + " [" + size() + "]");
/*  851 */       int i = (int)((this.cpMap.length + 10) * 1.5D);
/*  852 */       int j = 1;
/*  853 */       while (j < i) {
/*  854 */         j <<= 1;
/*      */       }
/*  856 */       this.indexKey = new ConstantPool.Entry[j];
/*  857 */       this.indexValue = new int[j];
/*  858 */       for (int k = 0; k < this.cpMap.length; k++) {
/*  859 */         ConstantPool.Entry localEntry = this.cpMap[k];
/*  860 */         if (localEntry != null) {
/*  861 */           int m = findIndexLocation(localEntry);
/*  862 */           assert (this.indexKey[m] == null);
/*  863 */           this.indexKey[m] = localEntry;
/*  864 */           this.indexValue[m] = k;
/*      */         }
/*      */       } } 
/*      */     public Object[] toArray(Object[] paramArrayOfObject) {
/*  868 */       int i = size();
/*  869 */       if (paramArrayOfObject.length < i) return super.toArray(paramArrayOfObject);
/*  870 */       System.arraycopy(this.cpMap, 0, paramArrayOfObject, 0, i);
/*  871 */       if (paramArrayOfObject.length > i) paramArrayOfObject[i] = null;
/*  872 */       return paramArrayOfObject;
/*      */     }
/*      */     public Object[] toArray() {
/*  875 */       return toArray(new ConstantPool.Entry[size()]);
/*      */     }
/*      */     public Object clone() {
/*  878 */       return new Index(this.debugName, (ConstantPool.Entry[])this.cpMap.clone());
/*      */     }
/*      */     public String toString() {
/*  881 */       return "Index " + this.debugName + " [" + size() + "]";
/*      */     }
/*      */     public String dumpString() {
/*  884 */       String str = toString();
/*  885 */       str = str + " {\n";
/*  886 */       for (int i = 0; i < this.cpMap.length; i++) {
/*  887 */         str = str + "    " + i + ": " + this.cpMap[i] + "\n";
/*      */       }
/*  889 */       str = str + "}";
/*  890 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class IndexGroup
/*      */   {
/*      */     private ConstantPool.Index indexUntyped;
/*  974 */     private ConstantPool.Index[] indexByTag = new ConstantPool.Index[14];
/*      */     private int[] untypedFirstIndexByTag;
/*      */     private int totalSize;
/*      */     private ConstantPool.Index[][] indexByTagAndClass;
/*      */ 
/*      */     public ConstantPool.Index getUntypedIndex()
/*      */     {
/*  981 */       if (this.indexUntyped == null) {
/*  982 */         untypedIndexOf(null);
/*  983 */         ConstantPool.Entry[] arrayOfEntry = new ConstantPool.Entry[this.totalSize];
/*  984 */         for (int i = 0; i < this.indexByTag.length; i++) {
/*  985 */           ConstantPool.Index localIndex = this.indexByTag[i];
/*  986 */           if (localIndex != null) {
/*  987 */             int j = localIndex.cpMap.length;
/*  988 */             if (j != 0) {
/*  989 */               int k = this.untypedFirstIndexByTag[i];
/*  990 */               assert (arrayOfEntry[k] == null);
/*  991 */               assert (arrayOfEntry[(k + j - 1)] == null);
/*  992 */               System.arraycopy(localIndex.cpMap, 0, arrayOfEntry, k, j);
/*      */             }
/*      */           }
/*      */         }
/*  994 */         this.indexUntyped = new ConstantPool.Index("untyped", arrayOfEntry);
/*      */       }
/*  996 */       return this.indexUntyped;
/*      */     }
/*      */ 
/*      */     public int untypedIndexOf(ConstantPool.Entry paramEntry) {
/* 1000 */       if (this.untypedFirstIndexByTag == null) {
/* 1001 */         this.untypedFirstIndexByTag = new int[14];
/* 1002 */         i = 0;
/* 1003 */         for (int j = 0; j < ConstantPool.TAGS_IN_ORDER.length; j++) {
/* 1004 */           k = ConstantPool.TAGS_IN_ORDER[j];
/* 1005 */           ConstantPool.Index localIndex2 = this.indexByTag[k];
/* 1006 */           if (localIndex2 != null) {
/* 1007 */             int m = localIndex2.cpMap.length;
/* 1008 */             this.untypedFirstIndexByTag[k] = i;
/* 1009 */             i += m;
/*      */           }
/*      */         }
/* 1011 */         this.totalSize = i;
/*      */       }
/* 1013 */       if (paramEntry == null) return -1;
/* 1014 */       int i = paramEntry.tag;
/* 1015 */       ConstantPool.Index localIndex1 = this.indexByTag[i];
/* 1016 */       if (localIndex1 == null) return -1;
/* 1017 */       int k = localIndex1.findIndexOf(paramEntry);
/* 1018 */       if (k >= 0)
/* 1019 */         k += this.untypedFirstIndexByTag[i];
/* 1020 */       return k;
/*      */     }
/*      */ 
/*      */     public void initIndexByTag(byte paramByte, ConstantPool.Index paramIndex) {
/* 1024 */       assert (this.indexByTag[paramByte] == null);
/* 1025 */       ConstantPool.Entry[] arrayOfEntry = paramIndex.cpMap;
/* 1026 */       for (int i = 0; i < arrayOfEntry.length; i++)
/*      */       {
/* 1028 */         assert (arrayOfEntry[i].tag == paramByte);
/*      */       }
/* 1030 */       if (paramByte == 1)
/*      */       {
/* 1032 */         assert ((arrayOfEntry.length == 0) || (arrayOfEntry[0].stringValue().equals("")));
/*      */       }
/* 1034 */       this.indexByTag[paramByte] = paramIndex;
/*      */ 
/* 1036 */       this.untypedFirstIndexByTag = null;
/* 1037 */       this.indexUntyped = null;
/* 1038 */       if (this.indexByTagAndClass != null)
/* 1039 */         this.indexByTagAndClass[paramByte] = null;
/*      */     }
/*      */ 
/*      */     public ConstantPool.Index getIndexByTag(byte paramByte)
/*      */     {
/* 1044 */       if (paramByte == 19) {
/* 1045 */         return getUntypedIndex();
/*      */       }
/* 1047 */       ConstantPool.Index localIndex = this.indexByTag[paramByte];
/* 1048 */       if (localIndex == null)
/*      */       {
/* 1050 */         localIndex = new ConstantPool.Index(ConstantPool.tagName(paramByte), new ConstantPool.Entry[0]);
/* 1051 */         this.indexByTag[paramByte] = localIndex;
/*      */       }
/* 1053 */       return localIndex;
/*      */     }
/*      */ 
/*      */     public ConstantPool.Index getMemberIndex(byte paramByte, ConstantPool.ClassEntry paramClassEntry)
/*      */     {
/* 1058 */       if (paramClassEntry == null)
/* 1059 */         throw new RuntimeException("missing class reference for " + ConstantPool.tagName(paramByte));
/* 1060 */       if (this.indexByTagAndClass == null)
/* 1061 */         this.indexByTagAndClass = new ConstantPool.Index[14][];
/* 1062 */       ConstantPool.Index localIndex1 = getIndexByTag((byte)7);
/* 1063 */       ConstantPool.Index[] arrayOfIndex = this.indexByTagAndClass[paramByte];
/* 1064 */       if (arrayOfIndex == null)
/*      */       {
/* 1067 */         ConstantPool.Index localIndex2 = getIndexByTag(paramByte);
/* 1068 */         int[] arrayOfInt = new int[localIndex2.size()];
/* 1069 */         for (int j = 0; j < arrayOfInt.length; j++) {
/* 1070 */           ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)localIndex2.get(j);
/* 1071 */           int k = localIndex1.indexOf(localMemberEntry.classRef);
/* 1072 */           arrayOfInt[j] = k;
/*      */         }
/* 1074 */         arrayOfIndex = ConstantPool.partition(localIndex2, arrayOfInt);
/* 1075 */         for (j = 0; j < arrayOfIndex.length; j++) {
/* 1076 */           assert ((arrayOfIndex[j] == null) || (arrayOfIndex[j].assertIsSorted()));
/*      */         }
/*      */ 
/* 1079 */         this.indexByTagAndClass[paramByte] = arrayOfIndex;
/*      */       }
/* 1081 */       int i = localIndex1.indexOf(paramClassEntry);
/* 1082 */       return arrayOfIndex[i];
/*      */     }
/*      */ 
/*      */     public int getOverloadingIndex(ConstantPool.MemberEntry paramMemberEntry)
/*      */     {
/* 1088 */       ConstantPool.Index localIndex = getMemberIndex(paramMemberEntry.tag, paramMemberEntry.classRef);
/* 1089 */       ConstantPool.Utf8Entry localUtf8Entry = paramMemberEntry.descRef.nameRef;
/* 1090 */       int i = 0;
/* 1091 */       for (int j = 0; j < localIndex.cpMap.length; j++) {
/* 1092 */         ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)localIndex.cpMap[j];
/* 1093 */         if (localMemberEntry.equals(paramMemberEntry))
/* 1094 */           return i;
/* 1095 */         if (localMemberEntry.descRef.nameRef.equals(localUtf8Entry))
/*      */         {
/* 1097 */           i++;
/*      */         }
/*      */       }
/* 1099 */       throw new RuntimeException("should not reach here");
/*      */     }
/*      */ 
/*      */     public ConstantPool.MemberEntry getOverloadingForIndex(byte paramByte, ConstantPool.ClassEntry paramClassEntry, String paramString, int paramInt)
/*      */     {
/* 1104 */       assert (paramString.equals(paramString.intern()));
/* 1105 */       ConstantPool.Index localIndex = getMemberIndex(paramByte, paramClassEntry);
/* 1106 */       int i = 0;
/* 1107 */       for (int j = 0; j < localIndex.cpMap.length; j++) {
/* 1108 */         ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)localIndex.cpMap[j];
/* 1109 */         if (localMemberEntry.descRef.nameRef.stringValue().equals(paramString)) {
/* 1110 */           if (i == paramInt) return localMemberEntry;
/* 1111 */           i++;
/*      */         }
/*      */       }
/* 1114 */       throw new RuntimeException("should not reach here");
/*      */     }
/*      */ 
/*      */     public boolean haveNumbers() {
/* 1118 */       for (byte b = 3; b <= 6; b = (byte)(b + 1)) {
/* 1119 */         switch (b) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/* 1124 */           break;
/*      */         default:
/* 1126 */           if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */         }
/* 1128 */         if (getIndexByTag(b).size() > 0) return true;
/*      */       }
/* 1130 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class LiteralEntry extends ConstantPool.Entry
/*      */   {
/*      */     protected LiteralEntry(byte paramByte)
/*      */     {
/*  257 */       super();
/*      */     }
/*      */ 
/*      */     public abstract Comparable literalValue();
/*      */   }
/*      */ 
/*      */   public static class MemberEntry extends ConstantPool.Entry
/*      */   {
/*      */     final ConstantPool.ClassEntry classRef;
/*      */     final ConstantPool.DescriptorEntry descRef;
/*      */ 
/*      */     public ConstantPool.Entry getRef(int paramInt)
/*      */     {
/*  426 */       if (paramInt == 0) return this.classRef;
/*  427 */       if (paramInt == 1) return this.descRef;
/*  428 */       return null;
/*      */     }
/*      */     protected int computeValueHash() {
/*  431 */       int i = this.descRef.hashCode();
/*  432 */       return this.classRef.hashCode() + (i << 8) ^ i;
/*      */     }
/*      */ 
/*      */     MemberEntry(byte paramByte, ConstantPool.ClassEntry paramClassEntry, ConstantPool.DescriptorEntry paramDescriptorEntry) {
/*  436 */       super();
/*  437 */       assert (ConstantPool.isMemberTag(paramByte));
/*  438 */       this.classRef = paramClassEntry;
/*  439 */       this.descRef = paramDescriptorEntry;
/*  440 */       hashCode();
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/*  443 */       if ((paramObject == null) || (paramObject.getClass() != MemberEntry.class)) {
/*  444 */         return false;
/*      */       }
/*  446 */       MemberEntry localMemberEntry = (MemberEntry)paramObject;
/*  447 */       return (this.classRef.eq(localMemberEntry.classRef)) && (this.descRef.eq(localMemberEntry.descRef));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  451 */       int i = superCompareTo(paramObject);
/*  452 */       if (i == 0) {
/*  453 */         MemberEntry localMemberEntry = (MemberEntry)paramObject;
/*      */ 
/*  455 */         i = this.classRef.compareTo(localMemberEntry.classRef);
/*  456 */         if (i == 0)
/*  457 */           i = this.descRef.compareTo(localMemberEntry.descRef);
/*      */       }
/*  459 */       return i;
/*      */     }
/*      */     public String stringValue() {
/*  462 */       return stringValueOf(this.tag, this.classRef, this.descRef);
/*      */     }
/*      */ 
/*      */     static String stringValueOf(byte paramByte, ConstantPool.ClassEntry paramClassEntry, ConstantPool.DescriptorEntry paramDescriptorEntry) {
/*  466 */       assert (ConstantPool.isMemberTag(paramByte));
/*      */       String str;
/*  468 */       switch (paramByte) { case 9:
/*  469 */         str = "Field:"; break;
/*      */       case 10:
/*  470 */         str = "Method:"; break;
/*      */       case 11:
/*  471 */         str = "IMethod:"; break;
/*      */       default:
/*  472 */         str = paramByte + "???";
/*      */       }
/*  474 */       return str + paramClassEntry.stringValue() + "," + paramDescriptorEntry.stringValue();
/*      */     }
/*      */ 
/*      */     public boolean isMethod() {
/*  478 */       return this.descRef.isMethod();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class NumberEntry extends ConstantPool.LiteralEntry
/*      */   {
/*      */     final Number value;
/*      */ 
/*      */     NumberEntry(Number paramNumber)
/*      */     {
/*  267 */       super();
/*  268 */       this.value = paramNumber;
/*  269 */       hashCode();
/*      */     }
/*      */     protected int computeValueHash() {
/*  272 */       return this.value.hashCode();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  276 */       return (paramObject != null) && (paramObject.getClass() == NumberEntry.class) && (((NumberEntry)paramObject).value.equals(this.value));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject)
/*      */     {
/*  281 */       int i = superCompareTo(paramObject);
/*  282 */       if (i == 0) {
/*  283 */         i = ((Comparable)this.value).compareTo(((NumberEntry)paramObject).value);
/*      */       }
/*  285 */       return i;
/*      */     }
/*      */     public Number numberValue() {
/*  288 */       return this.value;
/*      */     }
/*      */     public Comparable literalValue() {
/*  291 */       return (Comparable)this.value;
/*      */     }
/*      */     public String stringValue() {
/*  294 */       return this.value.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class SignatureEntry extends ConstantPool.Entry
/*      */   {
/*      */     final ConstantPool.Utf8Entry formRef;
/*      */     final ConstantPool.ClassEntry[] classRefs;
/*      */     String value;
/*      */     ConstantPool.Utf8Entry asUtf8Entry;
/*      */ 
/*      */     public ConstantPool.Entry getRef(int paramInt)
/*      */     {
/*  489 */       if (paramInt == 0) return this.formRef;
/*  490 */       return paramInt - 1 < this.classRefs.length ? this.classRefs[(paramInt - 1)] : null;
/*      */     }
/*      */     SignatureEntry(String paramString) {
/*  493 */       super();
/*  494 */       paramString = paramString.intern();
/*  495 */       this.value = paramString;
/*  496 */       String[] arrayOfString = ConstantPool.structureSignature(paramString);
/*  497 */       this.formRef = ConstantPool.getUtf8Entry(arrayOfString[0]);
/*  498 */       this.classRefs = new ConstantPool.ClassEntry[arrayOfString.length - 1];
/*  499 */       for (int i = 1; i < arrayOfString.length; i++) {
/*  500 */         this.classRefs[(i - 1)] = ConstantPool.getClassEntry(arrayOfString[i]);
/*      */       }
/*  502 */       hashCode();
/*      */     }
/*      */     protected int computeValueHash() {
/*  505 */       stringValue();
/*  506 */       return this.value.hashCode() + this.tag;
/*      */     }
/*      */ 
/*      */     public ConstantPool.Utf8Entry asUtf8Entry() {
/*  510 */       if (this.asUtf8Entry == null) {
/*  511 */         this.asUtf8Entry = ConstantPool.getUtf8Entry(stringValue());
/*      */       }
/*  513 */       return this.asUtf8Entry;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  517 */       return (paramObject != null) && (paramObject.getClass() == SignatureEntry.class) && (((SignatureEntry)paramObject).value.equals(this.value));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  521 */       int i = superCompareTo(paramObject);
/*  522 */       if (i == 0) {
/*  523 */         SignatureEntry localSignatureEntry = (SignatureEntry)paramObject;
/*  524 */         i = ConstantPool.compareSignatures(this.value, localSignatureEntry.value);
/*      */       }
/*  526 */       return i;
/*      */     }
/*      */     public String stringValue() {
/*  529 */       if (this.value == null) {
/*  530 */         this.value = stringValueOf(this.formRef, this.classRefs);
/*      */       }
/*  532 */       return this.value;
/*      */     }
/*      */ 
/*      */     static String stringValueOf(ConstantPool.Utf8Entry paramUtf8Entry, ConstantPool.ClassEntry[] paramArrayOfClassEntry) {
/*  536 */       String[] arrayOfString = new String[1 + paramArrayOfClassEntry.length];
/*  537 */       arrayOfString[0] = paramUtf8Entry.stringValue();
/*  538 */       for (int i = 1; i < arrayOfString.length; i++) {
/*  539 */         arrayOfString[i] = paramArrayOfClassEntry[(i - 1)].stringValue();
/*      */       }
/*  541 */       return ConstantPool.flattenSignature(arrayOfString).intern();
/*      */     }
/*      */ 
/*      */     public int computeSize(boolean paramBoolean) {
/*  545 */       String str = this.formRef.stringValue();
/*  546 */       int i = 0;
/*  547 */       int j = 1;
/*  548 */       if (isMethod()) {
/*  549 */         i = 1;
/*  550 */         j = str.indexOf(')');
/*      */       }
/*  552 */       int k = 0;
/*  553 */       label154: for (int m = i; m < j; m++) {
/*  554 */         switch (str.charAt(m)) {
/*      */         case 'D':
/*      */         case 'J':
/*  557 */           if (paramBoolean)
/*  558 */             k++; break;
/*      */         case '[':
/*      */         case ';':
/*      */         default:
/*  563 */           while (str.charAt(m) == '[') {
/*  564 */             m++; continue;
/*      */ 
/*  568 */             break label154;
/*      */ 
/*  570 */             assert (0 <= "BSCIJFDZLV([".indexOf(str.charAt(m)));
/*      */           }
/*      */         }
/*  573 */         k++;
/*      */       }
/*  575 */       return k;
/*      */     }
/*      */     public boolean isMethod() {
/*  578 */       return this.formRef.stringValue().charAt(0) == '(';
/*      */     }
/*      */     public byte getLiteralTag() {
/*  581 */       switch (this.formRef.stringValue().charAt(0)) { case 'L':
/*  582 */         return 8;
/*      */       case 'I':
/*  583 */         return 3;
/*      */       case 'J':
/*  584 */         return 5;
/*      */       case 'F':
/*  585 */         return 4;
/*      */       case 'D':
/*  586 */         return 6;
/*      */       case 'B':
/*      */       case 'C':
/*      */       case 'S':
/*      */       case 'Z':
/*  588 */         return 3;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'H':
/*      */       case 'K':
/*      */       case 'M':
/*      */       case 'N':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'Q':
/*      */       case 'R':
/*      */       case 'T':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*  590 */       case 'Y': } if (!$assertionsDisabled) throw new AssertionError();
/*  591 */       return 0;
/*      */     }
/*      */ 
/*      */     public String prettyString()
/*      */     {
/*      */       String str;
/*  595 */       if (isMethod()) {
/*  596 */         str = this.formRef.stringValue();
/*  597 */         str = str.substring(0, 1 + str.indexOf(')'));
/*      */       } else {
/*  599 */         str = "/" + this.formRef.stringValue();
/*      */       }
/*      */       int i;
/*  602 */       while ((i = str.indexOf(';')) >= 0) {
/*  603 */         str = str.substring(0, i) + str.substring(i + 1);
/*      */       }
/*  605 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class StringEntry extends ConstantPool.LiteralEntry
/*      */   {
/*      */     final ConstantPool.Utf8Entry ref;
/*      */ 
/*      */     public ConstantPool.Entry getRef(int paramInt)
/*      */     {
/*  301 */       return paramInt == 0 ? this.ref : null;
/*      */     }
/*      */     StringEntry(ConstantPool.Entry paramEntry) {
/*  304 */       super();
/*  305 */       this.ref = ((ConstantPool.Utf8Entry)paramEntry);
/*  306 */       hashCode();
/*      */     }
/*      */     protected int computeValueHash() {
/*  309 */       return this.ref.hashCode() + this.tag;
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/*  312 */       return (paramObject != null) && (paramObject.getClass() == StringEntry.class) && (((StringEntry)paramObject).ref.eq(this.ref));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  316 */       int i = superCompareTo(paramObject);
/*  317 */       if (i == 0) {
/*  318 */         i = this.ref.compareTo(((StringEntry)paramObject).ref);
/*      */       }
/*  320 */       return i;
/*      */     }
/*      */     public Comparable literalValue() {
/*  323 */       return this.ref.stringValue();
/*      */     }
/*      */     public String stringValue() {
/*  326 */       return this.ref.stringValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Utf8Entry extends ConstantPool.Entry
/*      */   {
/*      */     final String value;
/*      */ 
/*      */     Utf8Entry(String paramString)
/*      */     {
/*  212 */       super();
/*  213 */       this.value = paramString.intern();
/*  214 */       hashCode();
/*      */     }
/*      */     protected int computeValueHash() {
/*  217 */       return this.value.hashCode();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  221 */       return (paramObject != null) && (paramObject.getClass() == Utf8Entry.class) && (((Utf8Entry)paramObject).value.equals(this.value));
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  225 */       int i = superCompareTo(paramObject);
/*  226 */       if (i == 0) {
/*  227 */         i = this.value.compareTo(((Utf8Entry)paramObject).value);
/*      */       }
/*  229 */       return i;
/*      */     }
/*      */     public String stringValue() {
/*  232 */       return this.value;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.ConstantPool
 * JD-Core Version:    0.6.2
 */