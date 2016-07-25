/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import com.sun.org.apache.bcel.internal.util.ClassQueue;
/*     */ import com.sun.org.apache.bcel.internal.util.ClassVector;
/*     */ import com.sun.org.apache.bcel.internal.util.Repository;
/*     */ import com.sun.org.apache.bcel.internal.util.SyntheticRepository;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class JavaClass extends AccessFlags
/*     */   implements Cloneable, Node
/*     */ {
/*     */   private String file_name;
/*     */   private String package_name;
/*  88 */   private String source_file_name = "<Unknown>";
/*     */   private int class_name_index;
/*     */   private int superclass_name_index;
/*     */   private String class_name;
/*     */   private String superclass_name;
/*     */   private int major;
/*     */   private int minor;
/*     */   private ConstantPool constant_pool;
/*     */   private int[] interfaces;
/*     */   private String[] interface_names;
/*     */   private Field[] fields;
/*     */   private Method[] methods;
/*     */   private Attribute[] attributes;
/* 100 */   private byte source = 1;
/*     */   public static final byte HEAP = 1;
/*     */   public static final byte FILE = 2;
/*     */   public static final byte ZIP = 3;
/* 106 */   static boolean debug = false;
/* 107 */   static char sep = '/';
/*     */ 
/* 114 */   private transient Repository repository = SyntheticRepository.getInstance();
/*     */ 
/*     */   public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes, byte source)
/*     */   {
/* 148 */     if (interfaces == null)
/* 149 */       interfaces = new int[0];
/* 150 */     if (attributes == null)
/* 151 */       this.attributes = new Attribute[0];
/* 152 */     if (fields == null)
/* 153 */       fields = new Field[0];
/* 154 */     if (methods == null) {
/* 155 */       methods = new Method[0];
/*     */     }
/* 157 */     this.class_name_index = class_name_index;
/* 158 */     this.superclass_name_index = superclass_name_index;
/* 159 */     this.file_name = file_name;
/* 160 */     this.major = major;
/* 161 */     this.minor = minor;
/* 162 */     this.access_flags = access_flags;
/* 163 */     this.constant_pool = constant_pool;
/* 164 */     this.interfaces = interfaces;
/* 165 */     this.fields = fields;
/* 166 */     this.methods = methods;
/* 167 */     this.attributes = attributes;
/* 168 */     this.source = source;
/*     */ 
/* 171 */     for (int i = 0; i < attributes.length; i++) {
/* 172 */       if ((attributes[i] instanceof SourceFile)) {
/* 173 */         this.source_file_name = ((SourceFile)attributes[i]).getSourceFileName();
/* 174 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     this.class_name = constant_pool.getConstantString(class_name_index, (byte)7);
/*     */ 
/* 184 */     this.class_name = Utility.compactClassName(this.class_name, false);
/*     */ 
/* 186 */     int index = this.class_name.lastIndexOf('.');
/* 187 */     if (index < 0)
/* 188 */       this.package_name = "";
/*     */     else {
/* 190 */       this.package_name = this.class_name.substring(0, index);
/*     */     }
/* 192 */     if (superclass_name_index > 0) {
/* 193 */       this.superclass_name = constant_pool.getConstantString(superclass_name_index, (byte)7);
/*     */ 
/* 195 */       this.superclass_name = Utility.compactClassName(this.superclass_name, false);
/*     */     }
/*     */     else {
/* 198 */       this.superclass_name = "java.lang.Object";
/*     */     }
/* 200 */     this.interface_names = new String[interfaces.length];
/* 201 */     for (int i = 0; i < interfaces.length; i++) {
/* 202 */       String str = constant_pool.getConstantString(interfaces[i], (byte)7);
/* 203 */       this.interface_names[i] = Utility.compactClassName(str, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes)
/*     */   {
/* 233 */     this(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, fields, methods, attributes, (byte)1);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 246 */     v.visitJavaClass(this);
/*     */   }
/*     */ 
/*     */   static final void Debug(String str)
/*     */   {
/* 252 */     if (debug)
/* 253 */       System.out.println(str);
/*     */   }
/*     */ 
/*     */   public void dump(File file)
/*     */     throws IOException
/*     */   {
/* 264 */     String parent = file.getParent();
/*     */ 
/* 266 */     if (parent != null) {
/* 267 */       File dir = new File(parent);
/*     */ 
/* 269 */       if (dir != null) {
/* 270 */         dir.mkdirs();
/*     */       }
/*     */     }
/* 273 */     dump(new DataOutputStream(new FileOutputStream(file)));
/*     */   }
/*     */ 
/*     */   public void dump(String file_name)
/*     */     throws IOException
/*     */   {
/* 284 */     dump(new File(file_name));
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 291 */     ByteArrayOutputStream s = new ByteArrayOutputStream();
/* 292 */     DataOutputStream ds = new DataOutputStream(s);
/*     */     try
/*     */     {
/* 295 */       dump(ds);
/*     */     } catch (IOException e) {
/* 297 */       e.printStackTrace(); } finally {
/*     */       try {
/* 299 */         ds.close(); } catch (IOException e2) { e2.printStackTrace(); }
/*     */ 
/*     */     }
/* 302 */     return s.toByteArray();
/*     */   }
/*     */ 
/*     */   public void dump(OutputStream file)
/*     */     throws IOException
/*     */   {
/* 312 */     dump(new DataOutputStream(file));
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 323 */     file.writeInt(-889275714);
/* 324 */     file.writeShort(this.minor);
/* 325 */     file.writeShort(this.major);
/*     */ 
/* 327 */     this.constant_pool.dump(file);
/*     */ 
/* 329 */     file.writeShort(this.access_flags);
/* 330 */     file.writeShort(this.class_name_index);
/* 331 */     file.writeShort(this.superclass_name_index);
/*     */ 
/* 333 */     file.writeShort(this.interfaces.length);
/* 334 */     for (int i = 0; i < this.interfaces.length; i++) {
/* 335 */       file.writeShort(this.interfaces[i]);
/*     */     }
/* 337 */     file.writeShort(this.fields.length);
/* 338 */     for (int i = 0; i < this.fields.length; i++) {
/* 339 */       this.fields[i].dump(file);
/*     */     }
/* 341 */     file.writeShort(this.methods.length);
/* 342 */     for (int i = 0; i < this.methods.length; i++) {
/* 343 */       this.methods[i].dump(file);
/*     */     }
/* 345 */     if (this.attributes != null) {
/* 346 */       file.writeShort(this.attributes.length);
/* 347 */       for (int i = 0; i < this.attributes.length; i++)
/* 348 */         this.attributes[i].dump(file);
/*     */     }
/*     */     else {
/* 351 */       file.writeShort(0);
/*     */     }
/* 353 */     file.close();
/*     */   }
/*     */ 
/*     */   public Attribute[] getAttributes()
/*     */   {
/* 359 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 364 */     return this.class_name;
/*     */   }
/*     */ 
/*     */   public String getPackageName()
/*     */   {
/* 369 */     return this.package_name;
/*     */   }
/*     */ 
/*     */   public int getClassNameIndex()
/*     */   {
/* 374 */     return this.class_name_index;
/*     */   }
/*     */ 
/*     */   public ConstantPool getConstantPool()
/*     */   {
/* 379 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public Field[] getFields()
/*     */   {
/* 386 */     return this.fields;
/*     */   }
/*     */ 
/*     */   public String getFileName()
/*     */   {
/* 391 */     return this.file_name;
/*     */   }
/*     */ 
/*     */   public String[] getInterfaceNames()
/*     */   {
/* 396 */     return this.interface_names;
/*     */   }
/*     */ 
/*     */   public int[] getInterfaceIndices()
/*     */   {
/* 401 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 406 */     return this.major;
/*     */   }
/*     */ 
/*     */   public Method[] getMethods()
/*     */   {
/* 411 */     return this.methods;
/*     */   }
/*     */ 
/*     */   public Method getMethod(java.lang.reflect.Method m)
/*     */   {
/* 418 */     for (int i = 0; i < this.methods.length; i++) {
/* 419 */       Method method = this.methods[i];
/*     */ 
/* 421 */       if ((m.getName().equals(method.getName())) && (m.getModifiers() == method.getModifiers()) && (Type.getSignature(m).equals(method.getSignature())))
/*     */       {
/* 424 */         return method;
/*     */       }
/*     */     }
/*     */ 
/* 428 */     return null;
/*     */   }
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 434 */     return this.minor;
/*     */   }
/*     */ 
/*     */   public String getSourceFileName()
/*     */   {
/* 439 */     return this.source_file_name;
/*     */   }
/*     */ 
/*     */   public String getSuperclassName()
/*     */   {
/* 444 */     return this.superclass_name;
/*     */   }
/*     */ 
/*     */   public int getSuperclassNameIndex()
/*     */   {
/* 449 */     return this.superclass_name_index;
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attribute[] attributes)
/*     */   {
/* 477 */     this.attributes = attributes;
/*     */   }
/*     */ 
/*     */   public void setClassName(String class_name)
/*     */   {
/* 484 */     this.class_name = class_name;
/*     */   }
/*     */ 
/*     */   public void setClassNameIndex(int class_name_index)
/*     */   {
/* 491 */     this.class_name_index = class_name_index;
/*     */   }
/*     */ 
/*     */   public void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 498 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public void setFields(Field[] fields)
/*     */   {
/* 505 */     this.fields = fields;
/*     */   }
/*     */ 
/*     */   public void setFileName(String file_name)
/*     */   {
/* 512 */     this.file_name = file_name;
/*     */   }
/*     */ 
/*     */   public void setInterfaceNames(String[] interface_names)
/*     */   {
/* 519 */     this.interface_names = interface_names;
/*     */   }
/*     */ 
/*     */   public void setInterfaces(int[] interfaces)
/*     */   {
/* 526 */     this.interfaces = interfaces;
/*     */   }
/*     */ 
/*     */   public void setMajor(int major)
/*     */   {
/* 533 */     this.major = major;
/*     */   }
/*     */ 
/*     */   public void setMethods(Method[] methods)
/*     */   {
/* 540 */     this.methods = methods;
/*     */   }
/*     */ 
/*     */   public void setMinor(int minor)
/*     */   {
/* 547 */     this.minor = minor;
/*     */   }
/*     */ 
/*     */   public void setSourceFileName(String source_file_name)
/*     */   {
/* 554 */     this.source_file_name = source_file_name;
/*     */   }
/*     */ 
/*     */   public void setSuperclassName(String superclass_name)
/*     */   {
/* 561 */     this.superclass_name = superclass_name;
/*     */   }
/*     */ 
/*     */   public void setSuperclassNameIndex(int superclass_name_index)
/*     */   {
/* 568 */     this.superclass_name_index = superclass_name_index;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 575 */     String access = Utility.accessToString(this.access_flags, true);
/* 576 */     access = access + " ";
/*     */ 
/* 578 */     StringBuffer buf = new StringBuffer(access + Utility.classOrInterface(this.access_flags) + " " + this.class_name + " extends " + Utility.compactClassName(this.superclass_name, false) + '\n');
/*     */ 
/* 584 */     int size = this.interfaces.length;
/*     */ 
/* 586 */     if (size > 0) {
/* 587 */       buf.append("implements\t\t");
/*     */ 
/* 589 */       for (int i = 0; i < size; i++) {
/* 590 */         buf.append(this.interface_names[i]);
/* 591 */         if (i < size - 1) {
/* 592 */           buf.append(", ");
/*     */         }
/*     */       }
/* 595 */       buf.append('\n');
/*     */     }
/*     */ 
/* 598 */     buf.append("filename\t\t" + this.file_name + '\n');
/* 599 */     buf.append("compiled from\t\t" + this.source_file_name + '\n');
/* 600 */     buf.append("compiler version\t" + this.major + "." + this.minor + '\n');
/* 601 */     buf.append("access flags\t\t" + this.access_flags + '\n');
/* 602 */     buf.append("constant pool\t\t" + this.constant_pool.getLength() + " entries\n");
/* 603 */     buf.append("ACC_SUPER flag\t\t" + isSuper() + "\n");
/*     */ 
/* 605 */     if (this.attributes.length > 0) {
/* 606 */       buf.append("\nAttribute(s):\n");
/* 607 */       for (int i = 0; i < this.attributes.length; i++) {
/* 608 */         buf.append(indent(this.attributes[i]));
/*     */       }
/*     */     }
/* 611 */     if (this.fields.length > 0) {
/* 612 */       buf.append("\n" + this.fields.length + " fields:\n");
/* 613 */       for (int i = 0; i < this.fields.length; i++) {
/* 614 */         buf.append("\t" + this.fields[i] + '\n');
/*     */       }
/*     */     }
/* 617 */     if (this.methods.length > 0) {
/* 618 */       buf.append("\n" + this.methods.length + " methods:\n");
/* 619 */       for (int i = 0; i < this.methods.length; i++) {
/* 620 */         buf.append("\t" + this.methods[i] + '\n');
/*     */       }
/*     */     }
/* 623 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static final String indent(Object obj) {
/* 627 */     StringTokenizer tok = new StringTokenizer(obj.toString(), "\n");
/* 628 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 630 */     while (tok.hasMoreTokens()) {
/* 631 */       buf.append("\t" + tok.nextToken() + "\n");
/*     */     }
/* 633 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public JavaClass copy()
/*     */   {
/* 640 */     JavaClass c = null;
/*     */     try
/*     */     {
/* 643 */       c = (JavaClass)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 646 */     c.constant_pool = this.constant_pool.copy();
/* 647 */     c.interfaces = ((int[])this.interfaces.clone());
/* 648 */     c.interface_names = ((String[])this.interface_names.clone());
/*     */ 
/* 650 */     c.fields = new Field[this.fields.length];
/* 651 */     for (int i = 0; i < this.fields.length; i++) {
/* 652 */       c.fields[i] = this.fields[i].copy(c.constant_pool);
/*     */     }
/* 654 */     c.methods = new Method[this.methods.length];
/* 655 */     for (int i = 0; i < this.methods.length; i++) {
/* 656 */       c.methods[i] = this.methods[i].copy(c.constant_pool);
/*     */     }
/* 658 */     c.attributes = new Attribute[this.attributes.length];
/* 659 */     for (int i = 0; i < this.attributes.length; i++) {
/* 660 */       c.attributes[i] = this.attributes[i].copy(c.constant_pool);
/*     */     }
/* 662 */     return c;
/*     */   }
/*     */ 
/*     */   public final boolean isSuper() {
/* 666 */     return (this.access_flags & 0x20) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean isClass() {
/* 670 */     return (this.access_flags & 0x200) == 0;
/*     */   }
/*     */ 
/*     */   public final byte getSource()
/*     */   {
/* 676 */     return this.source;
/*     */   }
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 686 */     return this.repository;
/*     */   }
/*     */ 
/*     */   public void setRepository(Repository repository)
/*     */   {
/* 694 */     this.repository = repository;
/*     */   }
/*     */ 
/*     */   public final boolean instanceOf(JavaClass super_class)
/*     */   {
/* 702 */     if (equals(super_class)) {
/* 703 */       return true;
/*     */     }
/* 705 */     JavaClass[] super_classes = getSuperClasses();
/*     */ 
/* 707 */     for (int i = 0; i < super_classes.length; i++) {
/* 708 */       if (super_classes[i].equals(super_class)) {
/* 709 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 713 */     if (super_class.isInterface()) {
/* 714 */       return implementationOf(super_class);
/*     */     }
/*     */ 
/* 717 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean implementationOf(JavaClass inter)
/*     */   {
/* 724 */     if (!inter.isInterface()) {
/* 725 */       throw new IllegalArgumentException(inter.getClassName() + " is no interface");
/*     */     }
/*     */ 
/* 728 */     if (equals(inter)) {
/* 729 */       return true;
/*     */     }
/*     */ 
/* 732 */     JavaClass[] super_interfaces = getAllInterfaces();
/*     */ 
/* 734 */     for (int i = 0; i < super_interfaces.length; i++) {
/* 735 */       if (super_interfaces[i].equals(inter)) {
/* 736 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 740 */     return false;
/*     */   }
/*     */ 
/*     */   public JavaClass getSuperClass()
/*     */   {
/* 748 */     if ("java.lang.Object".equals(getClassName())) {
/* 749 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 753 */       return this.repository.loadClass(getSuperclassName());
/*     */     } catch (ClassNotFoundException e) {
/* 755 */       System.err.println(e);
/* 756 */     }return null;
/*     */   }
/*     */ 
/*     */   public JavaClass[] getSuperClasses()
/*     */   {
/* 765 */     JavaClass clazz = this;
/* 766 */     ClassVector vec = new ClassVector();
/*     */ 
/* 768 */     for (clazz = clazz.getSuperClass(); clazz != null; 
/* 769 */       clazz = clazz.getSuperClass())
/*     */     {
/* 771 */       vec.addElement(clazz);
/*     */     }
/*     */ 
/* 774 */     return vec.toArray();
/*     */   }
/*     */ 
/*     */   public JavaClass[] getInterfaces()
/*     */   {
/* 781 */     String[] interfaces = getInterfaceNames();
/* 782 */     JavaClass[] classes = new JavaClass[interfaces.length];
/*     */     try
/*     */     {
/* 785 */       for (int i = 0; i < interfaces.length; i++)
/* 786 */         classes[i] = this.repository.loadClass(interfaces[i]);
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/* 789 */       System.err.println(e);
/* 790 */       return null;
/*     */     }
/*     */ 
/* 793 */     return classes;
/*     */   }
/*     */ 
/*     */   public JavaClass[] getAllInterfaces()
/*     */   {
/* 800 */     ClassQueue queue = new ClassQueue();
/* 801 */     ClassVector vec = new ClassVector();
/*     */ 
/* 803 */     queue.enqueue(this);
/*     */ 
/* 805 */     while (!queue.empty()) {
/* 806 */       JavaClass clazz = queue.dequeue();
/*     */ 
/* 808 */       JavaClass souper = clazz.getSuperClass();
/* 809 */       JavaClass[] interfaces = clazz.getInterfaces();
/*     */ 
/* 811 */       if (clazz.isInterface()) {
/* 812 */         vec.addElement(clazz);
/*     */       }
/* 814 */       else if (souper != null) {
/* 815 */         queue.enqueue(souper);
/*     */       }
/*     */ 
/* 819 */       for (int i = 0; i < interfaces.length; i++) {
/* 820 */         queue.enqueue(interfaces[i]);
/*     */       }
/*     */     }
/*     */ 
/* 824 */     return vec.toArray();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 453 */     String debug = null; String sep = null;
/*     */     try
/*     */     {
/* 456 */       debug = SecuritySupport.getSystemProperty("JavaClass.debug");
/*     */ 
/* 458 */       sep = SecuritySupport.getSystemProperty("file.separator");
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/*     */     }
/*     */ 
/* 464 */     if (debug != null) {
/* 465 */       debug = new Boolean(debug).booleanValue();
/*     */     }
/* 467 */     if (sep != null)
/*     */       try {
/* 469 */         sep = sep.charAt(0);
/*     */       }
/*     */       catch (StringIndexOutOfBoundsException e)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.JavaClass
 * JD-Core Version:    0.6.2
 */