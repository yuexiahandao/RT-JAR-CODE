/*      */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*      */ 
/*      */ public class ClassWriter
/*      */   implements ClassVisitor
/*      */ {
/*      */   public static final int COMPUTE_MAXS = 1;
/*      */   public static final int COMPUTE_FRAMES = 2;
/*      */   static final int NOARG_INSN = 0;
/*      */   static final int SBYTE_INSN = 1;
/*      */   static final int SHORT_INSN = 2;
/*      */   static final int VAR_INSN = 3;
/*      */   static final int IMPLVAR_INSN = 4;
/*      */   static final int TYPE_INSN = 5;
/*      */   static final int FIELDORMETH_INSN = 6;
/*      */   static final int ITFMETH_INSN = 7;
/*      */   static final int LABEL_INSN = 8;
/*      */   static final int LABELW_INSN = 9;
/*      */   static final int LDC_INSN = 10;
/*      */   static final int LDCW_INSN = 11;
/*      */   static final int IINC_INSN = 12;
/*      */   static final int TABL_INSN = 13;
/*      */   static final int LOOK_INSN = 14;
/*      */   static final int MANA_INSN = 15;
/*      */   static final int WIDE_INSN = 16;
/*  484 */   static final byte[] TYPE = b;
/*      */   static final int CLASS = 7;
/*      */   static final int FIELD = 9;
/*      */   static final int METH = 10;
/*      */   static final int IMETH = 11;
/*      */   static final int STR = 8;
/*      */   static final int INT = 3;
/*      */   static final int FLOAT = 4;
/*      */   static final int LONG = 5;
/*      */   static final int DOUBLE = 6;
/*      */   static final int NAME_TYPE = 12;
/*      */   static final int UTF8 = 1;
/*      */   static final int TYPE_NORMAL = 13;
/*      */   static final int TYPE_UNINIT = 14;
/*      */   static final int TYPE_MERGED = 15;
/*      */   ClassReader cr;
/*      */   int version;
/*      */   int index;
/*      */   final ByteVector pool;
/*      */   Item[] items;
/*      */   int threshold;
/*      */   final Item key;
/*      */   final Item key2;
/*      */   final Item key3;
/*      */   Item[] typeTable;
/*      */   private short typeCount;
/*      */   private int access;
/*      */   private int name;
/*      */   String thisName;
/*      */   private int signature;
/*      */   private int superName;
/*      */   private int interfaceCount;
/*      */   private int[] interfaces;
/*      */   private int sourceFile;
/*      */   private ByteVector sourceDebug;
/*      */   private int enclosingMethodOwner;
/*      */   private int enclosingMethod;
/*      */   private AnnotationWriter anns;
/*      */   private AnnotationWriter ianns;
/*      */   private Attribute attrs;
/*      */   private int innerClassesCount;
/*      */   private ByteVector innerClasses;
/*      */   FieldWriter firstField;
/*      */   FieldWriter lastField;
/*      */   MethodWriter firstMethod;
/*      */   MethodWriter lastMethod;
/*      */   private final boolean computeMaxs;
/*      */   private final boolean computeFrames;
/*      */   boolean invalidFrames;
/*      */ 
/*      */   public ClassWriter(int flags)
/*      */   {
/*  565 */     this.index = 1;
/*  566 */     this.pool = new ByteVector();
/*  567 */     this.items = new Item[256];
/*  568 */     this.threshold = ((int)(0.75D * this.items.length));
/*  569 */     this.key = new Item();
/*  570 */     this.key2 = new Item();
/*  571 */     this.key3 = new Item();
/*  572 */     this.computeMaxs = ((flags & 0x1) != 0);
/*  573 */     this.computeFrames = ((flags & 0x2) != 0);
/*      */   }
/*      */ 
/*      */   public ClassWriter(ClassReader classReader, int flags)
/*      */   {
/*  601 */     this(flags);
/*  602 */     classReader.copyPool(this);
/*  603 */     this.cr = classReader;
/*      */   }
/*      */ 
/*      */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*      */   {
/*  618 */     this.version = version;
/*  619 */     this.access = access;
/*  620 */     this.name = newClass(name);
/*  621 */     this.thisName = name;
/*  622 */     if (signature != null) {
/*  623 */       this.signature = newUTF8(signature);
/*      */     }
/*  625 */     this.superName = (superName == null ? 0 : newClass(superName));
/*  626 */     if ((interfaces != null) && (interfaces.length > 0)) {
/*  627 */       this.interfaceCount = interfaces.length;
/*  628 */       this.interfaces = new int[this.interfaceCount];
/*  629 */       for (int i = 0; i < this.interfaceCount; i++)
/*  630 */         this.interfaces[i] = newClass(interfaces[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSource(String file, String debug)
/*      */   {
/*  636 */     if (file != null) {
/*  637 */       this.sourceFile = newUTF8(file);
/*      */     }
/*  639 */     if (debug != null)
/*  640 */       this.sourceDebug = new ByteVector().putUTF8(debug);
/*      */   }
/*      */ 
/*      */   public void visitOuterClass(String owner, String name, String desc)
/*      */   {
/*  649 */     this.enclosingMethodOwner = newClass(owner);
/*  650 */     if ((name != null) && (desc != null))
/*  651 */       this.enclosingMethod = newNameType(name, desc);
/*      */   }
/*      */ 
/*      */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/*      */   {
/*  662 */     ByteVector bv = new ByteVector();
/*      */ 
/*  664 */     bv.putShort(newUTF8(desc)).putShort(0);
/*  665 */     AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, 2);
/*  666 */     if (visible) {
/*  667 */       aw.next = this.anns;
/*  668 */       this.anns = aw;
/*      */     } else {
/*  670 */       aw.next = this.ianns;
/*  671 */       this.ianns = aw;
/*      */     }
/*  673 */     return aw;
/*      */   }
/*      */ 
/*      */   public void visitAttribute(Attribute attr) {
/*  677 */     attr.next = this.attrs;
/*  678 */     this.attrs = attr;
/*      */   }
/*      */ 
/*      */   public void visitInnerClass(String name, String outerName, String innerName, int access)
/*      */   {
/*  687 */     if (this.innerClasses == null) {
/*  688 */       this.innerClasses = new ByteVector();
/*      */     }
/*  690 */     this.innerClassesCount += 1;
/*  691 */     this.innerClasses.putShort(name == null ? 0 : newClass(name));
/*  692 */     this.innerClasses.putShort(outerName == null ? 0 : newClass(outerName));
/*  693 */     this.innerClasses.putShort(innerName == null ? 0 : newUTF8(innerName));
/*  694 */     this.innerClasses.putShort(access);
/*      */   }
/*      */ 
/*      */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
/*      */   {
/*  704 */     return new FieldWriter(this, access, name, desc, signature, value);
/*      */   }
/*      */ 
/*      */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/*      */   {
/*  714 */     return new MethodWriter(this, access, name, desc, signature, exceptions, this.computeMaxs, this.computeFrames);
/*      */   }
/*      */ 
/*      */   public void visitEnd()
/*      */   {
/*      */   }
/*      */ 
/*      */   public byte[] toByteArray()
/*      */   {
/*  738 */     int size = 24 + 2 * this.interfaceCount;
/*  739 */     int nbFields = 0;
/*  740 */     FieldWriter fb = this.firstField;
/*  741 */     while (fb != null) {
/*  742 */       nbFields++;
/*  743 */       size += fb.getSize();
/*  744 */       fb = fb.next;
/*      */     }
/*  746 */     int nbMethods = 0;
/*  747 */     MethodWriter mb = this.firstMethod;
/*  748 */     while (mb != null) {
/*  749 */       nbMethods++;
/*  750 */       size += mb.getSize();
/*  751 */       mb = mb.next;
/*      */     }
/*  753 */     int attributeCount = 0;
/*  754 */     if (this.signature != 0) {
/*  755 */       attributeCount++;
/*  756 */       size += 8;
/*  757 */       newUTF8("Signature");
/*      */     }
/*  759 */     if (this.sourceFile != 0) {
/*  760 */       attributeCount++;
/*  761 */       size += 8;
/*  762 */       newUTF8("SourceFile");
/*      */     }
/*  764 */     if (this.sourceDebug != null) {
/*  765 */       attributeCount++;
/*  766 */       size += this.sourceDebug.length + 4;
/*  767 */       newUTF8("SourceDebugExtension");
/*      */     }
/*  769 */     if (this.enclosingMethodOwner != 0) {
/*  770 */       attributeCount++;
/*  771 */       size += 10;
/*  772 */       newUTF8("EnclosingMethod");
/*      */     }
/*  774 */     if ((this.access & 0x20000) != 0) {
/*  775 */       attributeCount++;
/*  776 */       size += 6;
/*  777 */       newUTF8("Deprecated");
/*      */     }
/*  779 */     if (((this.access & 0x1000) != 0) && ((this.version & 0xFFFF) < 49))
/*      */     {
/*  782 */       attributeCount++;
/*  783 */       size += 6;
/*  784 */       newUTF8("Synthetic");
/*      */     }
/*  786 */     if (this.innerClasses != null) {
/*  787 */       attributeCount++;
/*  788 */       size += 8 + this.innerClasses.length;
/*  789 */       newUTF8("InnerClasses");
/*      */     }
/*  791 */     if (this.anns != null) {
/*  792 */       attributeCount++;
/*  793 */       size += 8 + this.anns.getSize();
/*  794 */       newUTF8("RuntimeVisibleAnnotations");
/*      */     }
/*  796 */     if (this.ianns != null) {
/*  797 */       attributeCount++;
/*  798 */       size += 8 + this.ianns.getSize();
/*  799 */       newUTF8("RuntimeInvisibleAnnotations");
/*      */     }
/*  801 */     if (this.attrs != null) {
/*  802 */       attributeCount += this.attrs.getCount();
/*  803 */       size += this.attrs.getSize(this, null, 0, -1, -1);
/*      */     }
/*  805 */     size += this.pool.length;
/*      */ 
/*  808 */     ByteVector out = new ByteVector(size);
/*  809 */     out.putInt(-889275714).putInt(this.version);
/*  810 */     out.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
/*  811 */     out.putShort(this.access).putShort(this.name).putShort(this.superName);
/*  812 */     out.putShort(this.interfaceCount);
/*  813 */     for (int i = 0; i < this.interfaceCount; i++) {
/*  814 */       out.putShort(this.interfaces[i]);
/*      */     }
/*  816 */     out.putShort(nbFields);
/*  817 */     fb = this.firstField;
/*  818 */     while (fb != null) {
/*  819 */       fb.put(out);
/*  820 */       fb = fb.next;
/*      */     }
/*  822 */     out.putShort(nbMethods);
/*  823 */     mb = this.firstMethod;
/*  824 */     while (mb != null) {
/*  825 */       mb.put(out);
/*  826 */       mb = mb.next;
/*      */     }
/*  828 */     out.putShort(attributeCount);
/*  829 */     if (this.signature != 0) {
/*  830 */       out.putShort(newUTF8("Signature")).putInt(2).putShort(this.signature);
/*      */     }
/*  832 */     if (this.sourceFile != 0) {
/*  833 */       out.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
/*      */     }
/*  835 */     if (this.sourceDebug != null) {
/*  836 */       int len = this.sourceDebug.length - 2;
/*  837 */       out.putShort(newUTF8("SourceDebugExtension")).putInt(len);
/*  838 */       out.putByteArray(this.sourceDebug.data, 2, len);
/*      */     }
/*  840 */     if (this.enclosingMethodOwner != 0) {
/*  841 */       out.putShort(newUTF8("EnclosingMethod")).putInt(4);
/*  842 */       out.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
/*      */     }
/*  844 */     if ((this.access & 0x20000) != 0) {
/*  845 */       out.putShort(newUTF8("Deprecated")).putInt(0);
/*      */     }
/*  847 */     if (((this.access & 0x1000) != 0) && ((this.version & 0xFFFF) < 49))
/*      */     {
/*  850 */       out.putShort(newUTF8("Synthetic")).putInt(0);
/*      */     }
/*  852 */     if (this.innerClasses != null) {
/*  853 */       out.putShort(newUTF8("InnerClasses"));
/*  854 */       out.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
/*  855 */       out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
/*      */     }
/*  857 */     if (this.anns != null) {
/*  858 */       out.putShort(newUTF8("RuntimeVisibleAnnotations"));
/*  859 */       this.anns.put(out);
/*      */     }
/*  861 */     if (this.ianns != null) {
/*  862 */       out.putShort(newUTF8("RuntimeInvisibleAnnotations"));
/*  863 */       this.ianns.put(out);
/*      */     }
/*  865 */     if (this.attrs != null) {
/*  866 */       this.attrs.put(this, null, 0, -1, -1, out);
/*      */     }
/*  868 */     if (this.invalidFrames) {
/*  869 */       ClassWriter cw = new ClassWriter(2);
/*  870 */       new ClassReader(out.data).accept(cw, 4);
/*  871 */       return cw.toByteArray();
/*      */     }
/*  873 */     return out.data;
/*      */   }
/*      */ 
/*      */   Item newConstItem(Object cst)
/*      */   {
/*  891 */     if ((cst instanceof Integer)) {
/*  892 */       int val = ((Integer)cst).intValue();
/*  893 */       return newInteger(val);
/*  894 */     }if ((cst instanceof Byte)) {
/*  895 */       int val = ((Byte)cst).intValue();
/*  896 */       return newInteger(val);
/*  897 */     }if ((cst instanceof Character)) {
/*  898 */       int val = ((Character)cst).charValue();
/*  899 */       return newInteger(val);
/*  900 */     }if ((cst instanceof Short)) {
/*  901 */       int val = ((Short)cst).intValue();
/*  902 */       return newInteger(val);
/*  903 */     }if ((cst instanceof Boolean)) {
/*  904 */       int val = ((Boolean)cst).booleanValue() ? 1 : 0;
/*  905 */       return newInteger(val);
/*  906 */     }if ((cst instanceof Float)) {
/*  907 */       float val = ((Float)cst).floatValue();
/*  908 */       return newFloat(val);
/*  909 */     }if ((cst instanceof Long)) {
/*  910 */       long val = ((Long)cst).longValue();
/*  911 */       return newLong(val);
/*  912 */     }if ((cst instanceof Double)) {
/*  913 */       double val = ((Double)cst).doubleValue();
/*  914 */       return newDouble(val);
/*  915 */     }if ((cst instanceof String))
/*  916 */       return newString((String)cst);
/*  917 */     if ((cst instanceof Type)) {
/*  918 */       Type t = (Type)cst;
/*  919 */       return newClassItem(t.getSort() == 10 ? t.getInternalName() : t.getDescriptor());
/*      */     }
/*      */ 
/*  923 */     throw new IllegalArgumentException("value " + cst);
/*      */   }
/*      */ 
/*      */   public int newConst(Object cst)
/*      */   {
/*  940 */     return newConstItem(cst).index;
/*      */   }
/*      */ 
/*      */   public int newUTF8(String value)
/*      */   {
/*  953 */     this.key.set(1, value, null, null);
/*  954 */     Item result = get(this.key);
/*  955 */     if (result == null) {
/*  956 */       this.pool.putByte(1).putUTF8(value);
/*  957 */       result = new Item(this.index++, this.key);
/*  958 */       put(result);
/*      */     }
/*  960 */     return result.index;
/*      */   }
/*      */ 
/*      */   Item newClassItem(String value)
/*      */   {
/*  973 */     this.key2.set(7, value, null, null);
/*  974 */     Item result = get(this.key2);
/*  975 */     if (result == null) {
/*  976 */       this.pool.put12(7, newUTF8(value));
/*  977 */       result = new Item(this.index++, this.key2);
/*  978 */       put(result);
/*      */     }
/*  980 */     return result;
/*      */   }
/*      */ 
/*      */   public int newClass(String value)
/*      */   {
/*  993 */     return newClassItem(value).index;
/*      */   }
/*      */ 
/*      */   Item newFieldItem(String owner, String name, String desc)
/*      */   {
/* 1007 */     this.key3.set(9, owner, name, desc);
/* 1008 */     Item result = get(this.key3);
/* 1009 */     if (result == null) {
/* 1010 */       put122(9, newClass(owner), newNameType(name, desc));
/* 1011 */       result = new Item(this.index++, this.key3);
/* 1012 */       put(result);
/*      */     }
/* 1014 */     return result;
/*      */   }
/*      */ 
/*      */   public int newField(String owner, String name, String desc)
/*      */   {
/* 1030 */     return newFieldItem(owner, name, desc).index;
/*      */   }
/*      */ 
/*      */   Item newMethodItem(String owner, String name, String desc, boolean itf)
/*      */   {
/* 1049 */     int type = itf ? 11 : 10;
/* 1050 */     this.key3.set(type, owner, name, desc);
/* 1051 */     Item result = get(this.key3);
/* 1052 */     if (result == null) {
/* 1053 */       put122(type, newClass(owner), newNameType(name, desc));
/* 1054 */       result = new Item(this.index++, this.key3);
/* 1055 */       put(result);
/*      */     }
/* 1057 */     return result;
/*      */   }
/*      */ 
/*      */   public int newMethod(String owner, String name, String desc, boolean itf)
/*      */   {
/* 1078 */     return newMethodItem(owner, name, desc, itf).index;
/*      */   }
/*      */ 
/*      */   Item newInteger(int value)
/*      */   {
/* 1089 */     this.key.set(value);
/* 1090 */     Item result = get(this.key);
/* 1091 */     if (result == null) {
/* 1092 */       this.pool.putByte(3).putInt(value);
/* 1093 */       result = new Item(this.index++, this.key);
/* 1094 */       put(result);
/*      */     }
/* 1096 */     return result;
/*      */   }
/*      */ 
/*      */   Item newFloat(float value)
/*      */   {
/* 1107 */     this.key.set(value);
/* 1108 */     Item result = get(this.key);
/* 1109 */     if (result == null) {
/* 1110 */       this.pool.putByte(4).putInt(this.key.intVal);
/* 1111 */       result = new Item(this.index++, this.key);
/* 1112 */       put(result);
/*      */     }
/* 1114 */     return result;
/*      */   }
/*      */ 
/*      */   Item newLong(long value)
/*      */   {
/* 1125 */     this.key.set(value);
/* 1126 */     Item result = get(this.key);
/* 1127 */     if (result == null) {
/* 1128 */       this.pool.putByte(5).putLong(value);
/* 1129 */       result = new Item(this.index, this.key);
/* 1130 */       put(result);
/* 1131 */       this.index += 2;
/*      */     }
/* 1133 */     return result;
/*      */   }
/*      */ 
/*      */   Item newDouble(double value)
/*      */   {
/* 1144 */     this.key.set(value);
/* 1145 */     Item result = get(this.key);
/* 1146 */     if (result == null) {
/* 1147 */       this.pool.putByte(6).putLong(this.key.longVal);
/* 1148 */       result = new Item(this.index, this.key);
/* 1149 */       put(result);
/* 1150 */       this.index += 2;
/*      */     }
/* 1152 */     return result;
/*      */   }
/*      */ 
/*      */   private Item newString(String value)
/*      */   {
/* 1163 */     this.key2.set(8, value, null, null);
/* 1164 */     Item result = get(this.key2);
/* 1165 */     if (result == null) {
/* 1166 */       this.pool.put12(8, newUTF8(value));
/* 1167 */       result = new Item(this.index++, this.key2);
/* 1168 */       put(result);
/*      */     }
/* 1170 */     return result;
/*      */   }
/*      */ 
/*      */   public int newNameType(String name, String desc)
/*      */   {
/* 1184 */     this.key2.set(12, name, desc, null);
/* 1185 */     Item result = get(this.key2);
/* 1186 */     if (result == null) {
/* 1187 */       put122(12, newUTF8(name), newUTF8(desc));
/* 1188 */       result = new Item(this.index++, this.key2);
/* 1189 */       put(result);
/*      */     }
/* 1191 */     return result.index;
/*      */   }
/*      */ 
/*      */   int addType(String type)
/*      */   {
/* 1202 */     this.key.set(13, type, null, null);
/* 1203 */     Item result = get(this.key);
/* 1204 */     if (result == null) {
/* 1205 */       result = addType(this.key);
/*      */     }
/* 1207 */     return result.index;
/*      */   }
/*      */ 
/*      */   int addUninitializedType(String type, int offset)
/*      */   {
/* 1221 */     this.key.type = 14;
/* 1222 */     this.key.intVal = offset;
/* 1223 */     this.key.strVal1 = type;
/* 1224 */     this.key.hashCode = (0x7FFFFFFF & 14 + type.hashCode() + offset);
/* 1225 */     Item result = get(this.key);
/* 1226 */     if (result == null) {
/* 1227 */       result = addType(this.key);
/*      */     }
/* 1229 */     return result.index;
/*      */   }
/*      */ 
/*      */   private Item addType(Item item)
/*      */   {
/* 1240 */     this.typeCount = ((short)(this.typeCount + 1));
/* 1241 */     Item result = new Item(this.typeCount, this.key);
/* 1242 */     put(result);
/* 1243 */     if (this.typeTable == null) {
/* 1244 */       this.typeTable = new Item[16];
/*      */     }
/* 1246 */     if (this.typeCount == this.typeTable.length) {
/* 1247 */       Item[] newTable = new Item[2 * this.typeTable.length];
/* 1248 */       System.arraycopy(this.typeTable, 0, newTable, 0, this.typeTable.length);
/* 1249 */       this.typeTable = newTable;
/*      */     }
/* 1251 */     this.typeTable[this.typeCount] = result;
/* 1252 */     return result;
/*      */   }
/*      */ 
/*      */   int getMergedType(int type1, int type2)
/*      */   {
/* 1266 */     this.key2.type = 15;
/* 1267 */     this.key2.longVal = (type1 | type2 << 32);
/* 1268 */     this.key2.hashCode = (0x7FFFFFFF & 15 + type1 + type2);
/* 1269 */     Item result = get(this.key2);
/* 1270 */     if (result == null) {
/* 1271 */       String t = this.typeTable[type1].strVal1;
/* 1272 */       String u = this.typeTable[type2].strVal1;
/* 1273 */       this.key2.intVal = addType(getCommonSuperClass(t, u));
/* 1274 */       result = new Item(0, this.key2);
/* 1275 */       put(result);
/*      */     }
/* 1277 */     return result.intVal;
/*      */   }
/*      */ 
/*      */   protected String getCommonSuperClass(String type1, String type2)
/*      */   {
/*      */     Class c;
/*      */     Class d;
/*      */     try
/*      */     {
/* 1298 */       c = Class.forName(type1.replace('/', '.'));
/* 1299 */       d = Class.forName(type2.replace('/', '.'));
/*      */     } catch (Exception e) {
/* 1301 */       throw new RuntimeException(e.toString());
/*      */     }
/* 1303 */     if (c.isAssignableFrom(d)) {
/* 1304 */       return type1;
/*      */     }
/* 1306 */     if (d.isAssignableFrom(c)) {
/* 1307 */       return type2;
/*      */     }
/* 1309 */     if ((c.isInterface()) || (d.isInterface())) {
/* 1310 */       return "java/lang/Object";
/*      */     }
/*      */     do
/* 1313 */       c = c.getSuperclass();
/* 1314 */     while (!c.isAssignableFrom(d));
/* 1315 */     return c.getName().replace('.', '/');
/*      */   }
/*      */ 
/*      */   private Item get(Item key)
/*      */   {
/* 1328 */     Item i = this.items[(key.hashCode % this.items.length)];
/* 1329 */     while ((i != null) && (!key.isEqualTo(i))) {
/* 1330 */       i = i.next;
/*      */     }
/* 1332 */     return i;
/*      */   }
/*      */ 
/*      */   private void put(Item i)
/*      */   {
/* 1342 */     if (this.index > this.threshold) {
/* 1343 */       int ll = this.items.length;
/* 1344 */       int nl = ll * 2 + 1;
/* 1345 */       Item[] newItems = new Item[nl];
/* 1346 */       for (int l = ll - 1; l >= 0; l--) {
/* 1347 */         Item j = this.items[l];
/* 1348 */         while (j != null) {
/* 1349 */           int index = j.hashCode % newItems.length;
/* 1350 */           Item k = j.next;
/* 1351 */           j.next = newItems[index];
/* 1352 */           newItems[index] = j;
/* 1353 */           j = k;
/*      */         }
/*      */       }
/* 1356 */       this.items = newItems;
/* 1357 */       this.threshold = ((int)(nl * 0.75D));
/*      */     }
/* 1359 */     int index = i.hashCode % this.items.length;
/* 1360 */     i.next = this.items[index];
/* 1361 */     this.items[index] = i;
/*      */   }
/*      */ 
/*      */   private void put122(int b, int s1, int s2)
/*      */   {
/* 1372 */     this.pool.put12(b, s1).putShort(s2);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  476 */     byte[] b = new byte['Ü'];
/*  477 */     String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
/*      */ 
/*  481 */     for (int i = 0; i < b.length; i++)
/*  482 */       b[i] = ((byte)(s.charAt(i) - 'A'));
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter
 * JD-Core Version:    0.6.2
 */