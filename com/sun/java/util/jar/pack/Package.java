/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ class Package
/*      */ {
/*      */   int verbose;
/*      */   int magic;
/*      */   int package_minver;
/*      */   int package_majver;
/*      */   int default_modtime;
/*      */   int default_options;
/*      */   short default_class_majver;
/*      */   short default_class_minver;
/*      */   short min_class_majver;
/*      */   short min_class_minver;
/*      */   short max_class_majver;
/*      */   short max_class_minver;
/*      */   short observed_max_class_majver;
/*      */   short observed_max_class_minver;
/*      */   ConstantPool.IndexGroup cp;
/*      */   public static final Attribute.Layout attrCodeEmpty;
/*      */   public static final Attribute.Layout attrInnerClassesEmpty;
/*      */   public static final Attribute.Layout attrSourceFileSpecial;
/*      */   public static final Map<Attribute.Layout, Attribute> attrDefs;
/*      */   ArrayList<Class> classes;
/*      */   ArrayList<File> files;
/*      */   List<InnerClass> allInnerClasses;
/*      */   Map<ConstantPool.ClassEntry, InnerClass> allInnerClassesByThis;
/*      */   private static final int SLASH_MIN = 46;
/*      */   private static final int SLASH_MAX = 47;
/*      */   private static final int DOLLAR_MIN = 0;
/*      */   private static final int DOLLAR_MAX = 45;
/* 1318 */   static final List<Object> noObjects = Arrays.asList(new Object[0]);
/* 1319 */   static final List<Package.Class.Field> noFields = Arrays.asList(new Package.Class.Field[0]);
/* 1320 */   static final List<Package.Class.Method> noMethods = Arrays.asList(new Package.Class.Method[0]);
/* 1321 */   static final List<InnerClass> noInnerClasses = Arrays.asList(new InnerClass[0]);
/*      */ 
/*      */   Package()
/*      */   {
/*   64 */     PropMap localPropMap = Utils.currentPropMap();
/*   65 */     if (localPropMap != null) {
/*   66 */       this.verbose = localPropMap.getInteger("com.sun.java.util.jar.pack.verbose");
/*      */     }
/*      */ 
/*   73 */     this.default_modtime = 0;
/*   74 */     this.default_options = 0;
/*      */ 
/*   76 */     this.default_class_majver = -1;
/*   77 */     this.default_class_minver = 0;
/*      */ 
/*   80 */     this.min_class_majver = 45;
/*   81 */     this.min_class_minver = 3;
/*   82 */     this.max_class_majver = 51;
/*   83 */     this.max_class_minver = 0;
/*      */ 
/*   85 */     this.observed_max_class_majver = this.min_class_majver;
/*   86 */     this.observed_max_class_minver = this.min_class_minver;
/*      */ 
/*   89 */     this.cp = new ConstantPool.IndexGroup();
/*      */ 
/*  184 */     this.classes = new ArrayList();
/*      */ 
/*  735 */     this.files = new ArrayList();
/*      */ 
/*  899 */     this.allInnerClasses = new ArrayList();
/*      */ 
/*   92 */     this.magic = -889270259;
/*   93 */     this.package_minver = -1;
/*   94 */     this.package_majver = 0;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*   99 */     this.cp = new ConstantPool.IndexGroup();
/*  100 */     this.classes.clear();
/*  101 */     this.files.clear();
/*  102 */     BandStructure.nextSeqForDebug = 0;
/*      */   }
/*      */ 
/*      */   int getPackageVersion() {
/*  106 */     return (this.package_majver << 16) + this.package_minver;
/*      */   }
/*      */ 
/*      */   int getDefaultClassVersion()
/*      */   {
/*  126 */     return (this.default_class_majver << 16) + (char)this.default_class_minver;
/*      */   }
/*      */ 
/*      */   int getHighestClassVersion()
/*      */   {
/*  133 */     int i = 0;
/*  134 */     for (Class localClass : this.classes) {
/*  135 */       int j = localClass.getVersion();
/*  136 */       if (i < j) i = j;
/*      */     }
/*  138 */     return i;
/*      */   }
/*      */ 
/*      */   void choosePackageVersion()
/*      */   {
/*  145 */     assert (this.package_majver <= 0);
/*  146 */     int i = getHighestClassVersion();
/*  147 */     if ((i == 0) || (i >>> 16 < 50))
/*      */     {
/*  149 */       this.package_majver = 150;
/*  150 */       this.package_minver = 7;
/*  151 */     } else if (i >>> 16 == 50) {
/*  152 */       this.package_majver = 160;
/*  153 */       this.package_minver = 1;
/*      */     }
/*      */     else
/*      */     {
/*  157 */       this.package_majver = 160;
/*  158 */       this.package_minver = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkVersion()
/*      */     throws IOException
/*      */   {
/*      */     String str1;
/*      */     String str2;
/*  166 */     if (this.magic != -889270259) {
/*  167 */       str1 = Integer.toHexString(this.magic);
/*  168 */       str2 = Integer.toHexString(-889270259);
/*  169 */       throw new IOException("Unexpected package magic number: got " + str1 + "; expected " + str2);
/*      */     }
/*  171 */     if (((this.package_majver != 160) && (this.package_majver != 150)) || ((this.package_minver != 1) && (this.package_minver != 7)))
/*      */     {
/*  176 */       str1 = this.package_majver + "." + this.package_minver;
/*  177 */       str2 = "160.1 OR 150.7";
/*      */ 
/*  180 */       throw new IOException("Unexpected package minor version: got " + str1 + "; expected " + str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<Class> getClasses()
/*      */   {
/*  187 */     return this.classes;
/*      */   }
/*      */ 
/*      */   void addClass(Class paramClass)
/*      */   {
/*  726 */     assert (paramClass.getPackage() == this);
/*  727 */     boolean bool = this.classes.add(paramClass);
/*  728 */     assert (bool);
/*      */ 
/*  730 */     if (paramClass.file == null) paramClass.initFile(null);
/*  731 */     addFile(paramClass.file);
/*      */   }
/*      */ 
/*      */   public List<File> getFiles()
/*      */   {
/*  738 */     return this.files;
/*      */   }
/*      */ 
/*      */   public List<File> getClassStubs() {
/*  742 */     ArrayList localArrayList = new ArrayList(this.classes.size());
/*  743 */     for (Class localClass : this.classes) {
/*  744 */       assert (localClass.file.isClassStub());
/*  745 */       localArrayList.add(localClass.file);
/*      */     }
/*  747 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   File newStub(String paramString)
/*      */   {
/*  878 */     File localFile = new File(paramString);
/*  879 */     localFile.options |= 2;
/*  880 */     localFile.prepend = null;
/*  881 */     localFile.append = null;
/*  882 */     return localFile;
/*      */   }
/*      */ 
/*      */   private static String fixupFileName(String paramString) {
/*  886 */     String str = paramString.replace(File.separatorChar, '/');
/*  887 */     if (str.startsWith("/")) {
/*  888 */       throw new IllegalArgumentException("absolute file name " + str);
/*      */     }
/*  890 */     return str;
/*      */   }
/*      */ 
/*      */   void addFile(File paramFile) {
/*  894 */     boolean bool = this.files.add(paramFile);
/*  895 */     assert (bool);
/*      */   }
/*      */ 
/*      */   public List<InnerClass> getAllInnerClasses()
/*      */   {
/*  904 */     return this.allInnerClasses;
/*      */   }
/*      */ 
/*      */   public void setAllInnerClasses(Collection<InnerClass> paramCollection)
/*      */   {
/*  909 */     assert (paramCollection != this.allInnerClasses);
/*  910 */     this.allInnerClasses.clear();
/*  911 */     this.allInnerClasses.addAll(paramCollection);
/*      */ 
/*  914 */     this.allInnerClassesByThis = new HashMap(this.allInnerClasses.size());
/*  915 */     for (InnerClass localInnerClass : this.allInnerClasses) {
/*  916 */       Object localObject = this.allInnerClassesByThis.put(localInnerClass.thisClass, localInnerClass);
/*  917 */       assert (localObject == null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public InnerClass getGlobalInnerClass(ConstantPool.Entry paramEntry)
/*      */   {
/*  924 */     assert ((paramEntry instanceof ConstantPool.ClassEntry));
/*  925 */     return (InnerClass)this.allInnerClassesByThis.get(paramEntry);
/*      */   }
/*      */ 
/*      */   private static void visitInnerClassRefs(Collection<InnerClass> paramCollection, int paramInt, Collection<ConstantPool.Entry> paramCollection1)
/*      */   {
/* 1003 */     if (paramCollection == null) {
/* 1004 */       return;
/*      */     }
/* 1006 */     if (paramInt == 0) {
/* 1007 */       paramCollection1.add(getRefString("InnerClasses"));
/*      */     }
/* 1009 */     if (paramCollection.size() > 0)
/*      */     {
/* 1011 */       for (InnerClass localInnerClass : paramCollection)
/* 1012 */         localInnerClass.visitRefs(paramInt, paramCollection1);
/*      */     }
/*      */   }
/*      */ 
/*      */   static String[] parseInnerClassName(String paramString)
/*      */   {
/* 1022 */     int k = paramString.length();
/* 1023 */     int m = lastIndexOf(46, 47, paramString, paramString.length()) + 1;
/* 1024 */     int j = lastIndexOf(0, 45, paramString, paramString.length());
/* 1025 */     if (j < m) return null;
/*      */     String str2;
/*      */     String str3;
/*      */     int i;
/* 1026 */     if (isDigitString(paramString, j + 1, k))
/*      */     {
/* 1028 */       str2 = paramString.substring(j + 1, k);
/* 1029 */       str3 = null;
/* 1030 */       i = j;
/* 1031 */     } else if (((i = lastIndexOf(0, 45, paramString, j - 1)) > m) && (isDigitString(paramString, i + 1, j)))
/*      */     {
/* 1036 */       str2 = paramString.substring(i + 1, j);
/* 1037 */       str3 = paramString.substring(j + 1, k).intern();
/*      */     }
/*      */     else {
/* 1040 */       i = j;
/* 1041 */       str2 = null;
/* 1042 */       str3 = paramString.substring(j + 1, k).intern();
/*      */     }
/*      */     String str1;
/* 1044 */     if (str2 == null)
/* 1045 */       str1 = paramString.substring(0, i).intern();
/*      */     else {
/* 1047 */       str1 = null;
/*      */     }
/* 1049 */     return new String[] { str1, str2, str3 };
/*      */   }
/*      */ 
/*      */   private static int lastIndexOf(int paramInt1, int paramInt2, String paramString, int paramInt3)
/*      */   {
/* 1062 */     int i = paramInt3;
/*      */     while (true) { i--; if (i < 0) break;
/* 1063 */       int j = paramString.charAt(i);
/* 1064 */       if ((j >= paramInt1) && (j <= paramInt2)) {
/* 1065 */         return i;
/*      */       }
/*      */     }
/* 1068 */     return -1;
/*      */   }
/*      */ 
/*      */   private static boolean isDigitString(String paramString, int paramInt1, int paramInt2) {
/* 1072 */     if (paramInt1 == paramInt2) return false;
/* 1073 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 1074 */       int j = paramString.charAt(i);
/* 1075 */       if ((j < 48) || (j > 57)) return false;
/*      */     }
/* 1077 */     return true;
/*      */   }
/*      */ 
/*      */   static String getObviousSourceFile(String paramString) {
/* 1081 */     String str1 = paramString;
/* 1082 */     int i = lastIndexOf(46, 47, str1, str1.length()) + 1;
/* 1083 */     str1 = str1.substring(i);
/* 1084 */     int j = str1.length();
/*      */     while (true)
/*      */     {
/* 1087 */       int k = lastIndexOf(0, 45, str1, j - 1);
/* 1088 */       if (k < 0)
/*      */         break;
/* 1090 */       j = k;
/* 1091 */       if (j == 0)
/*      */         break;
/*      */     }
/* 1094 */     String str2 = str1.substring(0, j) + ".java";
/* 1095 */     return str2;
/*      */   }
/*      */ 
/*      */   static ConstantPool.Utf8Entry getRefString(String paramString)
/*      */   {
/* 1108 */     return ConstantPool.getUtf8Entry(paramString);
/*      */   }
/*      */ 
/*      */   static ConstantPool.LiteralEntry getRefLiteral(Comparable paramComparable) {
/* 1112 */     return ConstantPool.getLiteralEntry(paramComparable);
/*      */   }
/*      */ 
/*      */   void stripAttributeKind(String paramString)
/*      */   {
/* 1117 */     if (this.verbose > 0)
/* 1118 */       Utils.log.info("Stripping " + paramString.toLowerCase() + " data and attributes...");
/* 1119 */     switch (paramString) {
/*      */     case "Debug":
/* 1121 */       strip("SourceFile");
/* 1122 */       strip("LineNumberTable");
/* 1123 */       strip("LocalVariableTable");
/* 1124 */       strip("LocalVariableTypeTable");
/* 1125 */       break;
/*      */     case "Compile":
/* 1131 */       strip("Deprecated");
/* 1132 */       strip("Synthetic");
/* 1133 */       break;
/*      */     case "Exceptions":
/* 1138 */       strip("Exceptions");
/* 1139 */       break;
/*      */     case "Constant":
/* 1141 */       stripConstantFields();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void trimToSize()
/*      */   {
/* 1147 */     this.classes.trimToSize();
/* 1148 */     for (Class localClass : this.classes) {
/* 1149 */       localClass.trimToSize();
/*      */     }
/* 1151 */     this.files.trimToSize();
/*      */   }
/*      */ 
/*      */   public void strip(String paramString) {
/* 1155 */     for (Class localClass : this.classes)
/* 1156 */       localClass.strip(paramString);
/*      */   }
/*      */ 
/*      */   public static String versionStringOf(int paramInt1, int paramInt2)
/*      */   {
/* 1161 */     return paramInt1 + "." + paramInt2;
/*      */   }
/*      */   public static String versionStringOf(int paramInt) {
/* 1164 */     return versionStringOf(paramInt >>> 16, (char)paramInt);
/*      */   }
/*      */ 
/*      */   public void stripConstantFields() {
/* 1168 */     for (Class localClass : this.classes)
/* 1169 */       for (localIterator2 = localClass.fields.iterator(); localIterator2.hasNext(); ) {
/* 1170 */         Package.Class.Field localField = (Package.Class.Field)localIterator2.next();
/* 1171 */         if ((Modifier.isFinal(localField.flags)) && (Modifier.isStatic(localField.flags)) && (localField.getAttribute("ConstantValue") != null) && (!localField.getName().startsWith("serial")))
/*      */         {
/* 1176 */           if (this.verbose > 2) {
/* 1177 */             Utils.log.fine(">> Strip " + this + " ConstantValue");
/* 1178 */             localIterator2.remove();
/*      */           }
/*      */         }
/*      */       }
/*      */     Iterator localIterator2;
/*      */   }
/*      */ 
/*      */   protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/* 1186 */     for (Iterator localIterator = this.classes.iterator(); localIterator.hasNext(); ) { localObject = (Class)localIterator.next();
/* 1187 */       ((Class)localObject).visitRefs(paramInt, paramCollection);
/*      */     }
/*      */     Object localObject;
/* 1189 */     if (paramInt != 0) {
/* 1190 */       for (localIterator = this.files.iterator(); localIterator.hasNext(); ) { localObject = (File)localIterator.next();
/* 1191 */         ((File)localObject).visitRefs(paramInt, paramCollection);
/*      */       }
/* 1193 */       visitInnerClassRefs(this.allInnerClasses, paramInt, paramCollection);
/*      */     }
/*      */   }
/*      */ 
/*      */   void reorderFiles(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1205 */     if (!paramBoolean1)
/*      */     {
/* 1207 */       Collections.sort(this.classes);
/*      */     }
/*      */ 
/* 1215 */     List localList = getClassStubs();
/* 1216 */     for (Iterator localIterator = this.files.iterator(); localIterator.hasNext(); ) {
/* 1217 */       File localFile = (File)localIterator.next();
/* 1218 */       if ((localFile.isClassStub()) || ((paramBoolean2) && (localFile.isDirectory())))
/*      */       {
/* 1220 */         localIterator.remove();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1229 */     Collections.sort(this.files, new Comparator() {
/*      */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/* 1231 */         Package.File localFile1 = (Package.File)paramAnonymousObject1;
/* 1232 */         Package.File localFile2 = (Package.File)paramAnonymousObject2;
/*      */ 
/* 1234 */         String str1 = localFile1.nameString;
/* 1235 */         String str2 = localFile2.nameString;
/* 1236 */         if (str1.equals(str2)) return 0;
/* 1237 */         if ("META-INF/MANIFEST.MF".equals(str1)) return -1;
/* 1238 */         if ("META-INF/MANIFEST.MF".equals(str2)) return 1;
/*      */ 
/* 1240 */         String str3 = str1.substring(1 + str1.lastIndexOf('/'));
/* 1241 */         String str4 = str2.substring(1 + str2.lastIndexOf('/'));
/*      */ 
/* 1243 */         String str5 = str3.substring(1 + str3.lastIndexOf('.'));
/* 1244 */         String str6 = str4.substring(1 + str4.lastIndexOf('.'));
/*      */ 
/* 1247 */         int i = str5.compareTo(str6);
/* 1248 */         if (i != 0) return i;
/* 1249 */         i = str1.compareTo(str2);
/* 1250 */         return i;
/*      */       }
/*      */     });
/* 1255 */     this.files.addAll(localList);
/*      */   }
/*      */ 
/*      */   void trimStubs()
/*      */   {
/* 1260 */     for (ListIterator localListIterator = this.files.listIterator(this.files.size()); localListIterator.hasPrevious(); ) {
/* 1261 */       File localFile = (File)localListIterator.previous();
/* 1262 */       if (!localFile.isTrivialClassStub()) {
/* 1263 */         if (this.verbose <= 1) break;
/* 1264 */         Utils.log.fine("Keeping last non-trivial " + localFile); break;
/*      */       }
/*      */ 
/* 1267 */       if (this.verbose > 2)
/* 1268 */         Utils.log.fine("Removing trivial " + localFile);
/* 1269 */       localListIterator.remove();
/*      */     }
/*      */ 
/* 1272 */     if (this.verbose > 0)
/* 1273 */       Utils.log.info("Transmitting " + this.files.size() + " files, including per-file data for " + getClassStubs().size() + " classes out of " + this.classes.size());
/*      */   }
/*      */ 
/*      */   void buildGlobalConstantPool(Set<ConstantPool.Entry> paramSet)
/*      */   {
/* 1279 */     if (this.verbose > 1)
/* 1280 */       Utils.log.fine("Checking for unused CP entries");
/* 1281 */     paramSet.add(getRefString(""));
/* 1282 */     visitRefs(1, paramSet);
/* 1283 */     ConstantPool.completeReferencesIn(paramSet, false);
/* 1284 */     if (this.verbose > 1)
/* 1285 */       Utils.log.fine("Sorting CP entries");
/* 1286 */     ConstantPool.Index localIndex1 = ConstantPool.makeIndex("unsorted", paramSet);
/* 1287 */     ConstantPool.Index[] arrayOfIndex = ConstantPool.partitionByTag(localIndex1);
/*      */     byte b;
/*      */     ConstantPool.Index localIndex2;
/* 1288 */     for (int i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++) {
/* 1289 */       b = ConstantPool.TAGS_IN_ORDER[i];
/*      */ 
/* 1291 */       localIndex2 = arrayOfIndex[b];
/* 1292 */       if (localIndex2 != null) {
/* 1293 */         ConstantPool.sort(localIndex2);
/* 1294 */         this.cp.initIndexByTag(b, localIndex2);
/* 1295 */         arrayOfIndex[b] = null;
/*      */       }
/*      */     }
/* 1297 */     for (i = 0; i < arrayOfIndex.length; i++) {
/* 1298 */       assert (arrayOfIndex[i] == null);
/*      */     }
/* 1300 */     for (i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++) {
/* 1301 */       b = ConstantPool.TAGS_IN_ORDER[i];
/* 1302 */       localIndex2 = this.cp.getIndexByTag(b);
/* 1303 */       assert (localIndex2.assertIsSorted());
/* 1304 */       if (this.verbose > 2) Utils.log.fine(localIndex2.dumpString());
/*      */     }
/*      */   }
/*      */ 
/*      */   void ensureAllClassFiles()
/*      */   {
/* 1310 */     HashSet localHashSet = new HashSet(this.files);
/* 1311 */     for (Class localClass : this.classes)
/*      */     {
/* 1313 */       if (!localHashSet.contains(localClass.file))
/* 1314 */         this.files.add(localClass.file);
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  115 */     HashMap localHashMap = new HashMap(3);
/*  116 */     attrCodeEmpty = Attribute.define(localHashMap, 2, "Code", "").layout();
/*      */ 
/*  118 */     attrInnerClassesEmpty = Attribute.define(localHashMap, 0, "InnerClasses", "").layout();
/*      */ 
/*  120 */     attrSourceFileSpecial = Attribute.define(localHashMap, 0, "SourceFile", "RUNH").layout();
/*      */ 
/*  122 */     attrDefs = Collections.unmodifiableMap(localHashMap);
/*      */ 
/* 1057 */     assert (lastIndexOf(0, 45, "x$$y$", 4) == 2);
/* 1058 */     assert (lastIndexOf(46, 47, "x//y/", 4) == 2);
/*      */   }
/*      */ 
/*      */   public final class Class extends Attribute.Holder
/*      */     implements Comparable
/*      */   {
/*      */     Package.File file;
/*      */     int magic;
/*      */     short minver;
/*      */     short majver;
/*      */     ConstantPool.Entry[] cpMap;
/*      */     ConstantPool.ClassEntry thisClass;
/*      */     ConstantPool.ClassEntry superClass;
/*      */     ConstantPool.ClassEntry[] interfaces;
/*      */     ArrayList<Field> fields;
/*      */     ArrayList<Method> methods;
/*      */     ArrayList<Package.InnerClass> innerClasses;
/*      */ 
/*      */     public Package getPackage()
/*      */     {
/*  192 */       return Package.this;
/*      */     }
/*      */ 
/*      */     Class(int paramClassEntry1, ConstantPool.ClassEntry paramClassEntry2, ConstantPool.ClassEntry paramArrayOfClassEntry, ConstantPool.ClassEntry[] arg5)
/*      */     {
/*  218 */       this.magic = -889275714;
/*  219 */       this.minver = Package.this.default_class_minver;
/*  220 */       this.majver = Package.this.default_class_majver;
/*  221 */       this.flags = paramClassEntry1;
/*  222 */       this.thisClass = paramClassEntry2;
/*  223 */       this.superClass = paramArrayOfClassEntry;
/*      */       Object localObject;
/*  224 */       this.interfaces = localObject;
/*      */ 
/*  226 */       boolean bool = Package.this.classes.add(this);
/*  227 */       assert (bool);
/*      */     }
/*      */ 
/*      */     Class(String arg2)
/*      */     {
/*      */       String str;
/*  232 */       initFile(Package.this.newStub(str));
/*      */     }
/*      */     List<Field> getFields() {
/*  235 */       return this.fields == null ? Package.noFields : this.fields; } 
/*  236 */     List<Method> getMethods() { return this.methods == null ? Package.noMethods : this.methods; }
/*      */ 
/*      */     public String getName() {
/*  239 */       return this.thisClass.stringValue();
/*      */     }
/*      */ 
/*      */     int getVersion() {
/*  243 */       return (this.majver << 16) + (char)this.minver;
/*      */     }
/*      */     String getVersionString() {
/*  246 */       return Package.versionStringOf(this.majver, this.minver);
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject)
/*      */     {
/*  251 */       Class localClass = (Class)paramObject;
/*  252 */       String str1 = getName();
/*  253 */       String str2 = localClass.getName();
/*  254 */       return str1.compareTo(str2);
/*      */     }
/*      */ 
/*      */     String getObviousSourceFile() {
/*  258 */       return Package.getObviousSourceFile(getName());
/*      */     }
/*      */ 
/*      */     private void transformSourceFile(boolean paramBoolean)
/*      */     {
/*  263 */       Attribute localAttribute1 = getAttribute(Package.attrSourceFileSpecial);
/*  264 */       if (localAttribute1 == null)
/*  265 */         return;
/*  266 */       String str = getObviousSourceFile();
/*  267 */       ArrayList localArrayList = new ArrayList(1);
/*  268 */       localAttribute1.visitRefs(this, 1, localArrayList);
/*  269 */       ConstantPool.Utf8Entry localUtf8Entry = (ConstantPool.Utf8Entry)localArrayList.get(0);
/*  270 */       Attribute localAttribute2 = localAttribute1;
/*      */       Object localObject1;
/*  271 */       if (localUtf8Entry == null) {
/*  272 */         if (paramBoolean)
/*      */         {
/*  274 */           localAttribute2 = Attribute.find(0, "SourceFile", "H");
/*  275 */           localAttribute2 = localAttribute2.addContent(new byte[2]);
/*      */         }
/*      */         else {
/*  278 */           localObject1 = new byte[2];
/*  279 */           localUtf8Entry = Package.getRefString(str);
/*  280 */           Object localObject2 = null;
/*  281 */           localObject2 = Fixups.add(localObject2, (byte[])localObject1, 0, 0, localUtf8Entry);
/*  282 */           localAttribute2 = Package.attrSourceFileSpecial.addContent((byte[])localObject1, localObject2);
/*      */         }
/*  284 */       } else if (str.equals(localUtf8Entry.stringValue())) {
/*  285 */         if (paramBoolean)
/*      */         {
/*  287 */           localAttribute2 = Package.attrSourceFileSpecial.addContent(new byte[2]);
/*      */         }
/*  289 */         else if (!$assertionsDisabled) throw new AssertionError();
/*      */       }
/*      */ 
/*  292 */       if (localAttribute2 != localAttribute1) {
/*  293 */         if (Package.this.verbose > 2)
/*  294 */           Utils.log.fine("recoding obvious SourceFile=" + str);
/*  295 */         localObject1 = new ArrayList(getAttributes());
/*  296 */         int i = ((List)localObject1).indexOf(localAttribute1);
/*  297 */         ((List)localObject1).set(i, localAttribute2);
/*  298 */         setAttributes((List)localObject1);
/*      */       }
/*      */     }
/*      */ 
/*      */     void minimizeSourceFile() {
/*  303 */       transformSourceFile(true);
/*      */     }
/*      */     void expandSourceFile() {
/*  306 */       transformSourceFile(false);
/*      */     }
/*      */ 
/*      */     protected ConstantPool.Entry[] getCPMap() {
/*  310 */       return this.cpMap;
/*      */     }
/*      */ 
/*      */     protected void setCPMap(ConstantPool.Entry[] paramArrayOfEntry) {
/*  314 */       this.cpMap = paramArrayOfEntry;
/*      */     }
/*      */ 
/*      */     boolean hasInnerClasses() {
/*  318 */       return this.innerClasses != null;
/*      */     }
/*      */     List<Package.InnerClass> getInnerClasses() {
/*  321 */       return this.innerClasses;
/*      */     }
/*      */ 
/*      */     public void setInnerClasses(Collection<Package.InnerClass> paramCollection) {
/*  325 */       this.innerClasses = (paramCollection == null ? null : new ArrayList(paramCollection));
/*      */ 
/*  327 */       Attribute localAttribute = getAttribute(Package.attrInnerClassesEmpty);
/*  328 */       if ((this.innerClasses != null) && (localAttribute == null))
/*  329 */         addAttribute(Package.attrInnerClassesEmpty.canonicalInstance());
/*  330 */       else if ((this.innerClasses == null) && (localAttribute != null))
/*  331 */         removeAttribute(localAttribute);
/*      */     }
/*      */ 
/*      */     public List<Package.InnerClass> computeGloballyImpliedICs()
/*      */     {
/*  344 */       HashSet localHashSet = new HashSet();
/*      */ 
/*  346 */       Object localObject1 = this.innerClasses;
/*  347 */       this.innerClasses = null;
/*  348 */       visitRefs(0, localHashSet);
/*  349 */       this.innerClasses = ((ArrayList)localObject1);
/*      */ 
/*  351 */       ConstantPool.completeReferencesIn(localHashSet, true);
/*      */ 
/*  353 */       localObject1 = new HashSet();
/*  354 */       for (Object localObject2 = localHashSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ConstantPool.Entry)((Iterator)localObject2).next();
/*      */ 
/*  356 */         if ((localObject3 instanceof ConstantPool.ClassEntry))
/*      */         {
/*  358 */           while (localObject3 != null) {
/*  359 */             localInnerClass = Package.this.getGlobalInnerClass((ConstantPool.Entry)localObject3);
/*  360 */             if ((localInnerClass == null) || 
/*  361 */               (!((Set)localObject1).add(localObject3))) break;
/*  362 */             localObject3 = localInnerClass.outerClass;
/*      */           }
/*      */         }
/*      */       }
/*  370 */       Package.InnerClass localInnerClass;
/*  369 */       localObject2 = new ArrayList();
/*  370 */       for (Object localObject3 = Package.this.allInnerClasses.iterator(); ((Iterator)localObject3).hasNext(); ) { localInnerClass = (Package.InnerClass)((Iterator)localObject3).next();
/*      */ 
/*  376 */         if ((((Set)localObject1).contains(localInnerClass.thisClass)) || (localInnerClass.outerClass == this.thisClass))
/*      */         {
/*  379 */           if (Package.this.verbose > 1)
/*  380 */             Utils.log.fine("Relevant IC: " + localInnerClass);
/*  381 */           ((ArrayList)localObject2).add(localInnerClass);
/*      */         }
/*      */       }
/*  384 */       return localObject2;
/*      */     }
/*      */ 
/*      */     private List<Package.InnerClass> computeICdiff()
/*      */     {
/*  390 */       List localList1 = computeGloballyImpliedICs();
/*  391 */       List localList2 = getInnerClasses();
/*  392 */       if (localList2 == null) {
/*  393 */         localList2 = Collections.emptyList();
/*      */       }
/*      */ 
/*  403 */       if (localList2.isEmpty()) {
/*  404 */         return localList1;
/*      */       }
/*      */ 
/*  407 */       if (localList1.isEmpty()) {
/*  408 */         return localList2;
/*      */       }
/*      */ 
/*  412 */       HashSet localHashSet = new HashSet(localList2);
/*  413 */       localHashSet.retainAll(new HashSet(localList1));
/*  414 */       localList1.addAll(localList2);
/*  415 */       localList1.removeAll(localHashSet);
/*      */ 
/*  417 */       return localList1;
/*      */     }
/*      */ 
/*      */     void minimizeLocalICs()
/*      */     {
/*  431 */       List localList1 = computeICdiff();
/*  432 */       ArrayList localArrayList = this.innerClasses;
/*      */       List localList2;
/*  434 */       if (localList1.isEmpty())
/*      */       {
/*  436 */         localList2 = null;
/*  437 */         if ((localArrayList != null) && (localArrayList.isEmpty()))
/*      */         {
/*  440 */           if (Package.this.verbose > 0)
/*  441 */             Utils.log.info("Warning: Dropping empty InnerClasses attribute from " + this);
/*      */         }
/*  443 */       } else if (localArrayList == null)
/*      */       {
/*  446 */         localList2 = Collections.emptyList();
/*      */       }
/*      */       else
/*      */       {
/*  450 */         localList2 = localList1;
/*      */       }
/*      */ 
/*  453 */       setInnerClasses(localList2);
/*  454 */       if ((Package.this.verbose > 1) && (localList2 != null))
/*  455 */         Utils.log.fine("keeping local ICs in " + this + ": " + localList2);
/*      */     }
/*      */ 
/*      */     int expandLocalICs()
/*      */     {
/*  463 */       ArrayList localArrayList = this.innerClasses;
/*      */       List localList1;
/*      */       int i;
/*  466 */       if (localArrayList == null)
/*      */       {
/*  468 */         List localList2 = computeGloballyImpliedICs();
/*  469 */         if (localList2.isEmpty()) {
/*  470 */           localList1 = null;
/*  471 */           i = 0;
/*      */         } else {
/*  473 */           localList1 = localList2;
/*  474 */           i = 1;
/*      */         }
/*  476 */       } else if (localArrayList.isEmpty())
/*      */       {
/*  478 */         localList1 = null;
/*  479 */         i = 0;
/*      */       }
/*      */       else {
/*  482 */         localList1 = computeICdiff();
/*      */ 
/*  484 */         i = localList1.containsAll(localArrayList) ? 1 : -1;
/*      */       }
/*  486 */       setInnerClasses(localList1);
/*  487 */       return i;
/*      */     }
/*      */ 
/*      */     public void trimToSize()
/*      */     {
/*  609 */       super.trimToSize();
/*      */       Iterator localIterator;
/*  610 */       for (int i = 0; i <= 1; i++) {
/*  611 */         ArrayList localArrayList = i == 0 ? this.fields : this.methods;
/*  612 */         if (localArrayList != null) {
/*  613 */           localArrayList.trimToSize();
/*  614 */           for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) {
/*  615 */             Member localMember = (Member)localIterator.next();
/*  616 */             localMember.trimToSize();
/*      */           }
/*      */         }
/*      */       }
/*  619 */       if (this.innerClasses != null)
/*  620 */         this.innerClasses.trimToSize();
/*      */     }
/*      */ 
/*      */     public void strip(String paramString)
/*      */     {
/*  625 */       if ("InnerClass".equals(paramString))
/*  626 */         this.innerClasses = null;
/*      */       Iterator localIterator;
/*  627 */       for (int i = 0; i <= 1; i++) {
/*  628 */         ArrayList localArrayList = i == 0 ? this.fields : this.methods;
/*  629 */         if (localArrayList != null)
/*  630 */           for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) {
/*  631 */             Member localMember = (Member)localIterator.next();
/*  632 */             localMember.strip(paramString);
/*      */           }
/*      */       }
/*  635 */       super.strip(paramString);
/*      */     }
/*      */ 
/*      */     protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  639 */       if (Package.this.verbose > 2) Utils.log.fine("visitRefs " + this);
/*  640 */       paramCollection.add(this.thisClass);
/*  641 */       paramCollection.add(this.superClass);
/*  642 */       paramCollection.addAll(Arrays.asList(this.interfaces));
/*      */       Iterator localIterator;
/*  643 */       for (int i = 0; i <= 1; i++) {
/*  644 */         ArrayList localArrayList = i == 0 ? this.fields : this.methods;
/*  645 */         if (localArrayList != null)
/*  646 */           for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) {
/*  647 */             Member localMember = (Member)localIterator.next();
/*  648 */             int j = 0;
/*      */             try {
/*  650 */               localMember.visitRefs(paramInt, paramCollection);
/*  651 */               j = 1;
/*      */             } finally {
/*  653 */               if (j == 0)
/*  654 */                 Utils.log.warning("Error scanning " + localMember);
/*      */             }
/*      */           }
/*      */       }
/*  658 */       visitInnerClassRefs(paramInt, paramCollection);
/*      */ 
/*  660 */       super.visitRefs(paramInt, paramCollection);
/*      */     }
/*      */ 
/*      */     protected void visitInnerClassRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  664 */       Package.visitInnerClassRefs(this.innerClasses, paramInt, paramCollection);
/*      */     }
/*      */ 
/*      */     void finishReading()
/*      */     {
/*  669 */       trimToSize();
/*  670 */       maybeChooseFileName();
/*      */     }
/*      */ 
/*      */     public void initFile(Package.File paramFile) {
/*  674 */       assert (this.file == null);
/*  675 */       if (paramFile == null)
/*      */       {
/*  677 */         paramFile = Package.this.newStub(canonicalFileName());
/*      */       }
/*  679 */       this.file = paramFile;
/*  680 */       assert (paramFile.isClassStub());
/*  681 */       paramFile.stubClass = this;
/*  682 */       maybeChooseFileName();
/*      */     }
/*      */ 
/*      */     public void maybeChooseFileName() {
/*  686 */       if (this.thisClass == null) {
/*  687 */         return;
/*      */       }
/*  689 */       String str = canonicalFileName();
/*  690 */       if (this.file.nameString.equals("")) {
/*  691 */         this.file.nameString = str;
/*      */       }
/*  693 */       if (this.file.nameString.equals(str))
/*      */       {
/*  695 */         this.file.name = Package.getRefString("");
/*  696 */         return;
/*      */       }
/*      */ 
/*  699 */       if (this.file.name == null)
/*  700 */         this.file.name = Package.getRefString(this.file.nameString);
/*      */     }
/*      */ 
/*      */     public String canonicalFileName()
/*      */     {
/*  705 */       if (this.thisClass == null) return null;
/*  706 */       return this.thisClass.stringValue() + ".class";
/*      */     }
/*      */ 
/*      */     public File getFileName(File paramFile) {
/*  710 */       String str1 = this.file.name.stringValue();
/*  711 */       if (str1.equals(""))
/*  712 */         str1 = canonicalFileName();
/*  713 */       String str2 = str1.replace('/', File.separatorChar);
/*  714 */       return new File(paramFile, str2);
/*      */     }
/*      */     public File getFileName() {
/*  717 */       return getFileName(null);
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  721 */       return this.thisClass.stringValue();
/*      */     }
/*      */ 
/*      */     public class Field extends Package.Class.Member
/*      */     {
/*      */       int order;
/*      */ 
/*      */       public Field(int paramDescriptorEntry, ConstantPool.DescriptorEntry arg3)
/*      */       {
/*  539 */         super(paramDescriptorEntry, localDescriptorEntry);
/*  540 */         assert (!localDescriptorEntry.isMethod());
/*  541 */         if (Package.Class.this.fields == null)
/*  542 */           Package.Class.this.fields = new ArrayList();
/*  543 */         boolean bool = Package.Class.this.fields.add(this);
/*  544 */         assert (bool);
/*  545 */         this.order = Package.Class.this.fields.size();
/*      */       }
/*      */ 
/*      */       public byte getLiteralTag() {
/*  549 */         return this.descriptor.getLiteralTag();
/*      */       }
/*      */ 
/*      */       public int compareTo(Object paramObject) {
/*  553 */         Field localField = (Field)paramObject;
/*  554 */         return this.order - localField.order;
/*      */       }
/*      */     }
/*      */ 
/*      */     public abstract class Member extends Attribute.Holder
/*      */       implements Comparable
/*      */     {
/*      */       ConstantPool.DescriptorEntry descriptor;
/*      */ 
/*      */       protected Member(int paramDescriptorEntry, ConstantPool.DescriptorEntry arg3)
/*      */       {
/*  495 */         this.flags = paramDescriptorEntry;
/*      */         Object localObject;
/*  496 */         this.descriptor = localObject;
/*      */       }
/*      */       public Package.Class thisClass() {
/*  499 */         return Package.Class.this;
/*      */       }
/*      */       public ConstantPool.DescriptorEntry getDescriptor() {
/*  502 */         return this.descriptor;
/*      */       }
/*      */       public String getName() {
/*  505 */         return this.descriptor.nameRef.stringValue();
/*      */       }
/*      */       public String getType() {
/*  508 */         return this.descriptor.typeRef.stringValue();
/*      */       }
/*      */ 
/*      */       protected ConstantPool.Entry[] getCPMap() {
/*  512 */         return Package.Class.this.cpMap;
/*      */       }
/*      */       protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  515 */         if (Package.this.verbose > 2) Utils.log.fine("visitRefs " + this);
/*      */ 
/*  518 */         if (paramInt == 0) {
/*  519 */           paramCollection.add(this.descriptor.nameRef);
/*  520 */           paramCollection.add(this.descriptor.typeRef);
/*      */         } else {
/*  522 */           paramCollection.add(this.descriptor);
/*      */         }
/*      */ 
/*  525 */         super.visitRefs(paramInt, paramCollection);
/*      */       }
/*      */ 
/*      */       public String toString() {
/*  529 */         return Package.Class.this + "." + this.descriptor.prettyString();
/*      */       }
/*      */     }
/*      */ 
/*      */     public class Method extends Package.Class.Member
/*      */     {
/*      */       Code code;
/*      */ 
/*      */       public Method(int paramDescriptorEntry, ConstantPool.DescriptorEntry arg3)
/*      */       {
/*  564 */         super(paramDescriptorEntry, localDescriptorEntry);
/*  565 */         assert (localDescriptorEntry.isMethod());
/*  566 */         if (Package.Class.this.methods == null)
/*  567 */           Package.Class.this.methods = new ArrayList();
/*  568 */         boolean bool = Package.Class.this.methods.add(this);
/*  569 */         assert (bool);
/*      */       }
/*      */ 
/*      */       public void trimToSize() {
/*  573 */         super.trimToSize();
/*  574 */         if (this.code != null)
/*  575 */           this.code.trimToSize();
/*      */       }
/*      */ 
/*      */       public int getArgumentSize() {
/*  579 */         int i = this.descriptor.typeRef.computeSize(true);
/*  580 */         int j = Modifier.isStatic(this.flags) ? 0 : 1;
/*  581 */         return j + i;
/*      */       }
/*      */ 
/*      */       public int compareTo(Object paramObject)
/*      */       {
/*  586 */         Method localMethod = (Method)paramObject;
/*  587 */         return getDescriptor().compareTo(localMethod.getDescriptor());
/*      */       }
/*      */ 
/*      */       public void strip(String paramString) {
/*  591 */         if ("Code".equals(paramString))
/*  592 */           this.code = null;
/*  593 */         if (this.code != null)
/*  594 */           this.code.strip(paramString);
/*  595 */         super.strip(paramString);
/*      */       }
/*      */       protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  598 */         super.visitRefs(paramInt, paramCollection);
/*  599 */         if (this.code != null) {
/*  600 */           if (paramInt == 0) {
/*  601 */             paramCollection.add(Package.getRefString("Code"));
/*      */           }
/*  603 */           this.code.visitRefs(paramInt, paramCollection);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class File
/*      */     implements Comparable
/*      */   {
/*      */     String nameString;
/*      */     ConstantPool.Utf8Entry name;
/*  753 */     int modtime = 0;
/*  754 */     int options = 0;
/*      */     Package.Class stubClass;
/*  756 */     ArrayList prepend = new ArrayList();
/*  757 */     ByteArrayOutputStream append = new ByteArrayOutputStream();
/*      */ 
/*      */     File(ConstantPool.Utf8Entry arg2)
/*      */     {
/*      */       Object localObject;
/*  760 */       this.name = localObject;
/*  761 */       this.nameString = localObject.stringValue();
/*      */     }
/*      */ 
/*      */     File(String arg2) {
/*  765 */       String str = Package.fixupFileName(str);
/*  766 */       this.name = Package.getRefString(str);
/*  767 */       this.nameString = this.name.stringValue();
/*      */     }
/*      */ 
/*      */     public boolean isDirectory()
/*      */     {
/*  772 */       return this.nameString.endsWith("/");
/*      */     }
/*      */     public boolean isClassStub() {
/*  775 */       return (this.options & 0x2) != 0;
/*      */     }
/*      */     public Package.Class getStubClass() {
/*  778 */       assert (isClassStub());
/*  779 */       assert (this.stubClass != null);
/*  780 */       return this.stubClass;
/*      */     }
/*      */     public boolean isTrivialClassStub() {
/*  783 */       return (isClassStub()) && (this.name.stringValue().equals("")) && ((this.modtime == 0) || (this.modtime == Package.this.default_modtime)) && ((this.options & 0xFFFFFFFD) == 0);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  792 */       if ((paramObject == null) || (paramObject.getClass() != File.class))
/*  793 */         return false;
/*  794 */       File localFile = (File)paramObject;
/*  795 */       return localFile.nameString.equals(this.nameString);
/*      */     }
/*      */     public int hashCode() {
/*  798 */       return this.nameString.hashCode();
/*      */     }
/*      */ 
/*      */     public int compareTo(Object paramObject) {
/*  802 */       File localFile = (File)paramObject;
/*  803 */       return this.nameString.compareTo(localFile.nameString);
/*      */     }
/*      */     public String toString() {
/*  806 */       return this.nameString + "{" + (isClassStub() ? "*" : "") + (BandStructure.testBit(this.options, 1) ? "@" : "") + (this.modtime == 0 ? "" : new StringBuilder().append("M").append(this.modtime).toString()) + (getFileLength() == 0L ? "" : new StringBuilder().append("[").append(getFileLength()).append("]").toString()) + "}";
/*      */     }
/*      */ 
/*      */     public File getFileName()
/*      */     {
/*  815 */       return getFileName(null);
/*      */     }
/*      */     public File getFileName(File paramFile) {
/*  818 */       String str1 = this.nameString;
/*      */ 
/*  820 */       String str2 = str1.replace('/', File.separatorChar);
/*  821 */       return new File(paramFile, str2);
/*      */     }
/*      */ 
/*      */     public void addBytes(byte[] paramArrayOfByte) {
/*  825 */       addBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */     }
/*      */     public void addBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  828 */       if ((this.append.size() | paramInt2) << 2 < 0) {
/*  829 */         this.prepend.add(this.append.toByteArray());
/*  830 */         this.append.reset();
/*      */       }
/*  832 */       this.append.write(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */     public long getFileLength() {
/*  835 */       long l = 0L;
/*  836 */       if ((this.prepend == null) || (this.append == null)) return 0L;
/*  837 */       for (Iterator localIterator = this.prepend.iterator(); localIterator.hasNext(); ) {
/*  838 */         byte[] arrayOfByte = (byte[])localIterator.next();
/*  839 */         l += arrayOfByte.length;
/*      */       }
/*  841 */       l += this.append.size();
/*  842 */       return l;
/*      */     }
/*      */     public void writeTo(OutputStream paramOutputStream) throws IOException {
/*  845 */       if ((this.prepend == null) || (this.append == null)) return;
/*  846 */       for (Iterator localIterator = this.prepend.iterator(); localIterator.hasNext(); ) {
/*  847 */         byte[] arrayOfByte = (byte[])localIterator.next();
/*  848 */         paramOutputStream.write(arrayOfByte);
/*      */       }
/*  850 */       this.append.writeTo(paramOutputStream);
/*      */     }
/*      */     public void readFrom(InputStream paramInputStream) throws IOException {
/*  853 */       byte[] arrayOfByte = new byte[65536];
/*      */       int i;
/*  855 */       while ((i = paramInputStream.read(arrayOfByte)) > 0)
/*  856 */         addBytes(arrayOfByte, 0, i);
/*      */     }
/*      */ 
/*      */     public InputStream getInputStream() {
/*  860 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.append.toByteArray());
/*  861 */       if (this.prepend.isEmpty()) return localByteArrayInputStream;
/*  862 */       ArrayList localArrayList = new ArrayList(this.prepend.size() + 1);
/*  863 */       for (Iterator localIterator = this.prepend.iterator(); localIterator.hasNext(); ) {
/*  864 */         byte[] arrayOfByte = (byte[])localIterator.next();
/*  865 */         localArrayList.add(new ByteArrayInputStream(arrayOfByte));
/*      */       }
/*  867 */       localArrayList.add(localByteArrayInputStream);
/*  868 */       return new SequenceInputStream(Collections.enumeration(localArrayList));
/*      */     }
/*      */ 
/*      */     protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  872 */       assert (this.name != null);
/*  873 */       paramCollection.add(this.name);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class InnerClass
/*      */     implements Comparable
/*      */   {
/*      */     final ConstantPool.ClassEntry thisClass;
/*      */     final ConstantPool.ClassEntry outerClass;
/*      */     final ConstantPool.Utf8Entry name;
/*      */     final int flags;
/*      */     final boolean predictable;
/*      */ 
/*      */     InnerClass(ConstantPool.ClassEntry paramClassEntry1, ConstantPool.ClassEntry paramClassEntry2, ConstantPool.Utf8Entry paramUtf8Entry, int paramInt)
/*      */     {
/*  944 */       this.thisClass = paramClassEntry1;
/*  945 */       this.outerClass = paramClassEntry2;
/*  946 */       this.name = paramUtf8Entry;
/*  947 */       this.flags = paramInt;
/*  948 */       this.predictable = computePredictable();
/*      */     }
/*      */ 
/*      */     private boolean computePredictable()
/*      */     {
/*  953 */       String[] arrayOfString = Package.parseInnerClassName(this.thisClass.stringValue());
/*  954 */       if (arrayOfString == null) return false;
/*  955 */       String str1 = arrayOfString[0];
/*      */ 
/*  957 */       String str2 = arrayOfString[2];
/*  958 */       String str3 = this.name == null ? null : this.name.stringValue();
/*  959 */       String str4 = this.outerClass == null ? null : this.outerClass.stringValue();
/*  960 */       boolean bool = (str2 == str3) && (str1 == str4);
/*      */ 
/*  962 */       return bool;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  966 */       if ((paramObject == null) || (paramObject.getClass() != InnerClass.class))
/*  967 */         return false;
/*  968 */       InnerClass localInnerClass = (InnerClass)paramObject;
/*  969 */       return (eq(this.thisClass, localInnerClass.thisClass)) && (eq(this.outerClass, localInnerClass.outerClass)) && (eq(this.name, localInnerClass.name)) && (this.flags == localInnerClass.flags);
/*      */     }
/*      */ 
/*      */     private static boolean eq(Object paramObject1, Object paramObject2)
/*      */     {
/*  975 */       return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*      */     }
/*      */     public int hashCode() {
/*  978 */       return this.thisClass.hashCode();
/*      */     }
/*      */     public int compareTo(Object paramObject) {
/*  981 */       InnerClass localInnerClass = (InnerClass)paramObject;
/*  982 */       return this.thisClass.compareTo(localInnerClass.thisClass);
/*      */     }
/*      */ 
/*      */     protected void visitRefs(int paramInt, Collection<ConstantPool.Entry> paramCollection) {
/*  986 */       paramCollection.add(this.thisClass);
/*  987 */       if ((paramInt == 0) || (!this.predictable))
/*      */       {
/*  990 */         paramCollection.add(this.outerClass);
/*  991 */         paramCollection.add(this.name);
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  996 */       return this.thisClass.stringValue();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Package
 * JD-Core Version:    0.6.2
 */