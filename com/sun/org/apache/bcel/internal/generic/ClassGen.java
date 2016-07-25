/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.AccessFlags;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.SourceFile;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ClassGen extends AccessFlags
/*     */   implements Cloneable
/*     */ {
/*     */   private String class_name;
/*     */   private String super_class_name;
/*     */   private String file_name;
/*  77 */   private int class_name_index = -1; private int superclass_name_index = -1;
/*  78 */   private int major = 45; private int minor = 3;
/*     */   private ConstantPoolGen cp;
/*  83 */   private ArrayList field_vec = new ArrayList();
/*  84 */   private ArrayList method_vec = new ArrayList();
/*  85 */   private ArrayList attribute_vec = new ArrayList();
/*  86 */   private ArrayList interface_vec = new ArrayList();
/*     */   private ArrayList observers;
/*     */ 
/*     */   public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces, ConstantPoolGen cp)
/*     */   {
/*  99 */     this.class_name = class_name;
/* 100 */     this.super_class_name = super_class_name;
/* 101 */     this.file_name = file_name;
/* 102 */     this.access_flags = access_flags;
/* 103 */     this.cp = cp;
/*     */ 
/* 106 */     if (file_name != null) {
/* 107 */       addAttribute(new SourceFile(cp.addUtf8("SourceFile"), 2, cp.addUtf8(file_name), cp.getConstantPool()));
/*     */     }
/*     */ 
/* 110 */     this.class_name_index = cp.addClass(class_name);
/* 111 */     this.superclass_name_index = cp.addClass(super_class_name);
/*     */ 
/* 113 */     if (interfaces != null)
/* 114 */       for (int i = 0; i < interfaces.length; i++)
/* 115 */         addInterface(interfaces[i]);
/*     */   }
/*     */ 
/*     */   public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces)
/*     */   {
/* 128 */     this(class_name, super_class_name, file_name, access_flags, interfaces, new ConstantPoolGen());
/*     */   }
/*     */ 
/*     */   public ClassGen(JavaClass clazz)
/*     */   {
/* 137 */     this.class_name_index = clazz.getClassNameIndex();
/* 138 */     this.superclass_name_index = clazz.getSuperclassNameIndex();
/* 139 */     this.class_name = clazz.getClassName();
/* 140 */     this.super_class_name = clazz.getSuperclassName();
/* 141 */     this.file_name = clazz.getSourceFileName();
/* 142 */     this.access_flags = clazz.getAccessFlags();
/* 143 */     this.cp = new ConstantPoolGen(clazz.getConstantPool());
/* 144 */     this.major = clazz.getMajor();
/* 145 */     this.minor = clazz.getMinor();
/*     */ 
/* 147 */     Attribute[] attributes = clazz.getAttributes();
/* 148 */     Method[] methods = clazz.getMethods();
/* 149 */     Field[] fields = clazz.getFields();
/* 150 */     String[] interfaces = clazz.getInterfaceNames();
/*     */ 
/* 152 */     for (int i = 0; i < interfaces.length; i++) {
/* 153 */       addInterface(interfaces[i]);
/*     */     }
/* 155 */     for (int i = 0; i < attributes.length; i++) {
/* 156 */       addAttribute(attributes[i]);
/*     */     }
/* 158 */     for (int i = 0; i < methods.length; i++) {
/* 159 */       addMethod(methods[i]);
/*     */     }
/* 161 */     for (int i = 0; i < fields.length; i++)
/* 162 */       addField(fields[i]);
/*     */   }
/*     */ 
/*     */   public JavaClass getJavaClass()
/*     */   {
/* 169 */     int[] interfaces = getInterfaces();
/* 170 */     Field[] fields = getFields();
/* 171 */     Method[] methods = getMethods();
/* 172 */     Attribute[] attributes = getAttributes();
/*     */ 
/* 175 */     ConstantPool cp = this.cp.getFinalConstantPool();
/*     */ 
/* 177 */     return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, this.access_flags, cp, interfaces, fields, methods, attributes);
/*     */   }
/*     */ 
/*     */   public void addInterface(String name)
/*     */   {
/* 187 */     this.interface_vec.add(name);
/*     */   }
/*     */ 
/*     */   public void removeInterface(String name)
/*     */   {
/* 195 */     this.interface_vec.remove(name);
/*     */   }
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 201 */     return this.major;
/*     */   }
/*     */ 
/*     */   public void setMajor(int major)
/*     */   {
/* 207 */     this.major = major;
/*     */   }
/*     */ 
/*     */   public void setMinor(int minor)
/*     */   {
/* 214 */     this.minor = minor;
/*     */   }
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 220 */     return this.minor;
/*     */   }
/*     */ 
/*     */   public void addAttribute(Attribute a)
/*     */   {
/* 226 */     this.attribute_vec.add(a);
/*     */   }
/*     */ 
/*     */   public void addMethod(Method m)
/*     */   {
/* 232 */     this.method_vec.add(m);
/*     */   }
/*     */ 
/*     */   public void addEmptyConstructor(int access_flags)
/*     */   {
/* 241 */     InstructionList il = new InstructionList();
/* 242 */     il.append(InstructionConstants.THIS);
/* 243 */     il.append(new INVOKESPECIAL(this.cp.addMethodref(this.super_class_name, "<init>", "()V")));
/*     */ 
/* 245 */     il.append(InstructionConstants.RETURN);
/*     */ 
/* 247 */     MethodGen mg = new MethodGen(access_flags, Type.VOID, Type.NO_ARGS, null, "<init>", this.class_name, il, this.cp);
/*     */ 
/* 249 */     mg.setMaxStack(1);
/* 250 */     addMethod(mg.getMethod());
/*     */   }
/*     */ 
/*     */   public void addField(Field f)
/*     */   {
/* 257 */     this.field_vec.add(f);
/*     */   }
/* 259 */   public boolean containsField(Field f) { return this.field_vec.contains(f); }
/*     */ 
/*     */ 
/*     */   public Field containsField(String name)
/*     */   {
/* 264 */     for (Iterator e = this.field_vec.iterator(); e.hasNext(); ) {
/* 265 */       Field f = (Field)e.next();
/* 266 */       if (f.getName().equals(name)) {
/* 267 */         return f;
/*     */       }
/*     */     }
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */   public Method containsMethod(String name, String signature)
/*     */   {
/* 276 */     for (Iterator e = this.method_vec.iterator(); e.hasNext(); ) {
/* 277 */       Method m = (Method)e.next();
/* 278 */       if ((m.getName().equals(name)) && (m.getSignature().equals(signature))) {
/* 279 */         return m;
/*     */       }
/*     */     }
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   public void removeAttribute(Attribute a)
/*     */   {
/* 289 */     this.attribute_vec.remove(a);
/*     */   }
/*     */ 
/*     */   public void removeMethod(Method m)
/*     */   {
/* 295 */     this.method_vec.remove(m);
/*     */   }
/*     */ 
/*     */   public void replaceMethod(Method old, Method new_)
/*     */   {
/* 301 */     if (new_ == null) {
/* 302 */       throw new ClassGenException("Replacement method must not be null");
/*     */     }
/* 304 */     int i = this.method_vec.indexOf(old);
/*     */ 
/* 306 */     if (i < 0)
/* 307 */       this.method_vec.add(new_);
/*     */     else
/* 309 */       this.method_vec.set(i, new_);
/*     */   }
/*     */ 
/*     */   public void replaceField(Field old, Field new_)
/*     */   {
/* 316 */     if (new_ == null) {
/* 317 */       throw new ClassGenException("Replacement method must not be null");
/*     */     }
/* 319 */     int i = this.field_vec.indexOf(old);
/*     */ 
/* 321 */     if (i < 0)
/* 322 */       this.field_vec.add(new_);
/*     */     else
/* 324 */       this.field_vec.set(i, new_);
/*     */   }
/*     */ 
/*     */   public void removeField(Field f)
/*     */   {
/* 331 */     this.field_vec.remove(f);
/*     */   }
/* 333 */   public String getClassName() { return this.class_name; } 
/* 334 */   public String getSuperclassName() { return this.super_class_name; } 
/* 335 */   public String getFileName() { return this.file_name; }
/*     */ 
/*     */   public void setClassName(String name) {
/* 338 */     this.class_name = name.replace('/', '.');
/* 339 */     this.class_name_index = this.cp.addClass(name);
/*     */   }
/*     */ 
/*     */   public void setSuperclassName(String name) {
/* 343 */     this.super_class_name = name.replace('/', '.');
/* 344 */     this.superclass_name_index = this.cp.addClass(name);
/*     */   }
/*     */ 
/*     */   public Method[] getMethods() {
/* 348 */     Method[] methods = new Method[this.method_vec.size()];
/* 349 */     this.method_vec.toArray(methods);
/* 350 */     return methods;
/*     */   }
/*     */ 
/*     */   public void setMethods(Method[] methods) {
/* 354 */     this.method_vec.clear();
/* 355 */     for (int m = 0; m < methods.length; m++)
/* 356 */       addMethod(methods[m]);
/*     */   }
/*     */ 
/*     */   public void setMethodAt(Method method, int pos) {
/* 360 */     this.method_vec.set(pos, method);
/*     */   }
/*     */ 
/*     */   public Method getMethodAt(int pos) {
/* 364 */     return (Method)this.method_vec.get(pos);
/*     */   }
/*     */ 
/*     */   public String[] getInterfaceNames() {
/* 368 */     int size = this.interface_vec.size();
/* 369 */     String[] interfaces = new String[size];
/*     */ 
/* 371 */     this.interface_vec.toArray(interfaces);
/* 372 */     return interfaces;
/*     */   }
/*     */ 
/*     */   public int[] getInterfaces() {
/* 376 */     int size = this.interface_vec.size();
/* 377 */     int[] interfaces = new int[size];
/*     */ 
/* 379 */     for (int i = 0; i < size; i++) {
/* 380 */       interfaces[i] = this.cp.addClass((String)this.interface_vec.get(i));
/*     */     }
/* 382 */     return interfaces;
/*     */   }
/*     */ 
/*     */   public Field[] getFields() {
/* 386 */     Field[] fields = new Field[this.field_vec.size()];
/* 387 */     this.field_vec.toArray(fields);
/* 388 */     return fields;
/*     */   }
/*     */ 
/*     */   public Attribute[] getAttributes() {
/* 392 */     Attribute[] attributes = new Attribute[this.attribute_vec.size()];
/* 393 */     this.attribute_vec.toArray(attributes);
/* 394 */     return attributes;
/*     */   }
/*     */   public ConstantPoolGen getConstantPool() {
/* 397 */     return this.cp;
/*     */   }
/* 399 */   public void setConstantPool(ConstantPoolGen constant_pool) { this.cp = constant_pool; }
/*     */ 
/*     */   public void setClassNameIndex(int class_name_index)
/*     */   {
/* 403 */     this.class_name_index = class_name_index;
/* 404 */     this.class_name = this.cp.getConstantPool().getConstantString(class_name_index, (byte)7).replace('/', '.');
/*     */   }
/*     */ 
/*     */   public void setSuperclassNameIndex(int superclass_name_index)
/*     */   {
/* 409 */     this.superclass_name_index = superclass_name_index;
/* 410 */     this.super_class_name = this.cp.getConstantPool().getConstantString(superclass_name_index, (byte)7).replace('/', '.');
/*     */   }
/*     */ 
/*     */   public int getSuperclassNameIndex() {
/* 414 */     return this.superclass_name_index;
/*     */   }
/* 416 */   public int getClassNameIndex() { return this.class_name_index; }
/*     */ 
/*     */ 
/*     */   public void addObserver(ClassObserver o)
/*     */   {
/* 423 */     if (this.observers == null) {
/* 424 */       this.observers = new ArrayList();
/*     */     }
/* 426 */     this.observers.add(o);
/*     */   }
/*     */ 
/*     */   public void removeObserver(ClassObserver o)
/*     */   {
/* 432 */     if (this.observers != null)
/* 433 */       this.observers.remove(o);
/*     */   }
/*     */ 
/*     */   public void update()
/*     */   {
/*     */     Iterator e;
/* 441 */     if (this.observers != null)
/* 442 */       for (e = this.observers.iterator(); e.hasNext(); )
/* 443 */         ((ClassObserver)e.next()).notify(this);
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     try {
/* 448 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 450 */       System.err.println(e);
/* 451 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ClassGen
 * JD-Core Version:    0.6.2
 */