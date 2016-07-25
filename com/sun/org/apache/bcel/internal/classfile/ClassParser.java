/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public final class ClassParser
/*     */ {
/*     */   private DataInputStream file;
/*     */   private ZipFile zip;
/*     */   private String file_name;
/*     */   private int class_name_index;
/*     */   private int superclass_name_index;
/*     */   private int major;
/*     */   private int minor;
/*     */   private int access_flags;
/*     */   private int[] interfaces;
/*     */   private ConstantPool constant_pool;
/*     */   private Field[] fields;
/*     */   private Method[] methods;
/*     */   private Attribute[] attributes;
/*     */   private boolean is_zip;
/*     */   private static final int BUFSIZE = 8192;
/*     */ 
/*     */   public ClassParser(InputStream file, String file_name)
/*     */   {
/* 102 */     this.file_name = file_name;
/*     */ 
/* 104 */     String clazz = file.getClass().getName();
/* 105 */     this.is_zip = ((clazz.startsWith("java.util.zip.")) || (clazz.startsWith("java.util.jar.")));
/*     */ 
/* 107 */     if ((file instanceof DataInputStream))
/* 108 */       this.file = ((DataInputStream)file);
/*     */     else
/* 110 */       this.file = new DataInputStream(new BufferedInputStream(file, 8192));
/*     */   }
/*     */ 
/*     */   public ClassParser(String file_name)
/*     */     throws IOException
/*     */   {
/* 120 */     this.is_zip = false;
/* 121 */     this.file_name = file_name;
/* 122 */     this.file = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name), 8192));
/*     */   }
/*     */ 
/*     */   public ClassParser(String zip_file, String file_name)
/*     */     throws IOException
/*     */   {
/* 133 */     this.is_zip = true;
/* 134 */     this.zip = new ZipFile(zip_file);
/* 135 */     ZipEntry entry = this.zip.getEntry(file_name);
/*     */ 
/* 137 */     this.file_name = file_name;
/*     */ 
/* 139 */     this.file = new DataInputStream(new BufferedInputStream(this.zip.getInputStream(entry), 8192));
/*     */   }
/*     */ 
/*     */   public JavaClass parse()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 158 */     readID();
/*     */ 
/* 161 */     readVersion();
/*     */ 
/* 165 */     readConstantPool();
/*     */ 
/* 168 */     readClassInfo();
/*     */ 
/* 171 */     readInterfaces();
/*     */ 
/* 175 */     readFields();
/*     */ 
/* 178 */     readMethods();
/*     */ 
/* 181 */     readAttributes();
/*     */ 
/* 201 */     this.file.close();
/* 202 */     if (this.zip != null) {
/* 203 */       this.zip.close();
/*     */     }
/*     */ 
/* 206 */     return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, this.access_flags, this.constant_pool, this.interfaces, this.fields, this.methods, this.attributes, (byte)(this.is_zip ? 3 : 2));
/*     */   }
/*     */ 
/*     */   private final void readAttributes()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 221 */     int attributes_count = this.file.readUnsignedShort();
/* 222 */     this.attributes = new Attribute[attributes_count];
/*     */ 
/* 224 */     for (int i = 0; i < attributes_count; i++)
/* 225 */       this.attributes[i] = Attribute.readAttribute(this.file, this.constant_pool);
/*     */   }
/*     */ 
/*     */   private final void readClassInfo()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 235 */     this.access_flags = this.file.readUnsignedShort();
/*     */ 
/* 240 */     if ((this.access_flags & 0x200) != 0) {
/* 241 */       this.access_flags |= 1024;
/*     */     }
/* 243 */     if (((this.access_flags & 0x400) != 0) && ((this.access_flags & 0x10) != 0))
/*     */     {
/* 245 */       throw new ClassFormatException("Class can't be both final and abstract");
/*     */     }
/* 247 */     this.class_name_index = this.file.readUnsignedShort();
/* 248 */     this.superclass_name_index = this.file.readUnsignedShort();
/*     */   }
/*     */ 
/*     */   private final void readConstantPool()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 257 */     this.constant_pool = new ConstantPool(this.file);
/*     */   }
/*     */ 
/*     */   private final void readFields()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 269 */     int fields_count = this.file.readUnsignedShort();
/* 270 */     this.fields = new Field[fields_count];
/*     */ 
/* 272 */     for (int i = 0; i < fields_count; i++)
/* 273 */       this.fields[i] = new Field(this.file, this.constant_pool);
/*     */   }
/*     */ 
/*     */   private final void readID()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 286 */     int magic = -889275714;
/*     */ 
/* 288 */     if (this.file.readInt() != magic)
/* 289 */       throw new ClassFormatException(this.file_name + " is not a Java .class file");
/*     */   }
/*     */ 
/*     */   private final void readInterfaces()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 300 */     int interfaces_count = this.file.readUnsignedShort();
/* 301 */     this.interfaces = new int[interfaces_count];
/*     */ 
/* 303 */     for (int i = 0; i < interfaces_count; i++)
/* 304 */       this.interfaces[i] = this.file.readUnsignedShort();
/*     */   }
/*     */ 
/*     */   private final void readMethods()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 315 */     int methods_count = this.file.readUnsignedShort();
/* 316 */     this.methods = new Method[methods_count];
/*     */ 
/* 318 */     for (int i = 0; i < methods_count; i++)
/* 319 */       this.methods[i] = new Method(this.file, this.constant_pool);
/*     */   }
/*     */ 
/*     */   private final void readVersion()
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 328 */     this.minor = this.file.readUnsignedShort();
/* 329 */     this.major = this.file.readUnsignedShort();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ClassParser
 * JD-Core Version:    0.6.2
 */