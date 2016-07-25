/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.ClassParser;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ClassLoader extends java.lang.ClassLoader
/*     */ {
/*  89 */   private Hashtable classes = new Hashtable();
/*  90 */   private String[] ignored_packages = { "java.", "javax.", "sun." };
/*     */ 
/*  93 */   private Repository repository = SyntheticRepository.getInstance();
/*  94 */   private java.lang.ClassLoader deferTo = getSystemClassLoader();
/*     */ 
/*     */   public ClassLoader() {
/*     */   }
/*     */ 
/*     */   public ClassLoader(java.lang.ClassLoader deferTo) {
/* 100 */     this.deferTo = deferTo;
/* 101 */     this.repository = new ClassLoaderRepository(deferTo);
/*     */   }
/*     */ 
/*     */   public ClassLoader(String[] ignored_packages)
/*     */   {
/* 108 */     addIgnoredPkgs(ignored_packages);
/*     */   }
/*     */ 
/*     */   public ClassLoader(java.lang.ClassLoader deferTo, String[] ignored_packages) {
/* 112 */     this.deferTo = deferTo;
/* 113 */     this.repository = new ClassLoaderRepository(deferTo);
/*     */ 
/* 115 */     addIgnoredPkgs(ignored_packages);
/*     */   }
/*     */ 
/*     */   private void addIgnoredPkgs(String[] ignored_packages) {
/* 119 */     String[] new_p = new String[ignored_packages.length + this.ignored_packages.length];
/*     */ 
/* 121 */     System.arraycopy(this.ignored_packages, 0, new_p, 0, this.ignored_packages.length);
/* 122 */     System.arraycopy(ignored_packages, 0, new_p, this.ignored_packages.length, ignored_packages.length);
/*     */ 
/* 125 */     this.ignored_packages = new_p;
/*     */   }
/*     */ 
/*     */   protected Class loadClass(String class_name, boolean resolve)
/*     */     throws ClassNotFoundException
/*     */   {
/* 131 */     Class cl = null;
/*     */ 
/* 135 */     if ((cl = (Class)this.classes.get(class_name)) == null)
/*     */     {
/* 139 */       for (int i = 0; i < this.ignored_packages.length; i++) {
/* 140 */         if (class_name.startsWith(this.ignored_packages[i])) {
/* 141 */           cl = this.deferTo.loadClass(class_name);
/* 142 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 146 */       if (cl == null) {
/* 147 */         JavaClass clazz = null;
/*     */ 
/* 151 */         if (class_name.indexOf("$$BCEL$$") >= 0) {
/* 152 */           clazz = createClass(class_name);
/*     */         }
/* 154 */         else if ((clazz = this.repository.loadClass(class_name)) != null) {
/* 155 */           clazz = modifyClass(clazz);
/*     */         }
/*     */         else {
/* 158 */           throw new ClassNotFoundException(class_name);
/*     */         }
/*     */ 
/* 161 */         if (clazz != null) {
/* 162 */           byte[] bytes = clazz.getBytes();
/* 163 */           cl = defineClass(class_name, bytes, 0, bytes.length);
/*     */         } else {
/* 165 */           cl = Class.forName(class_name);
/*     */         }
/*     */       }
/* 168 */       if (resolve) {
/* 169 */         resolveClass(cl);
/*     */       }
/*     */     }
/* 172 */     this.classes.put(class_name, cl);
/*     */ 
/* 174 */     return cl;
/*     */   }
/*     */ 
/*     */   protected JavaClass modifyClass(JavaClass clazz)
/*     */   {
/* 181 */     return clazz;
/*     */   }
/*     */ 
/*     */   protected JavaClass createClass(String class_name)
/*     */   {
/* 199 */     int index = class_name.indexOf("$$BCEL$$");
/* 200 */     String real_name = class_name.substring(index + 8);
/*     */ 
/* 202 */     JavaClass clazz = null;
/*     */     try {
/* 204 */       byte[] bytes = Utility.decode(real_name, true);
/* 205 */       ClassParser parser = new ClassParser(new ByteArrayInputStream(bytes), "foo");
/*     */ 
/* 207 */       clazz = parser.parse();
/*     */     } catch (Throwable e) {
/* 209 */       e.printStackTrace();
/* 210 */       return null;
/*     */     }
/*     */ 
/* 214 */     ConstantPool cp = clazz.getConstantPool();
/*     */ 
/* 216 */     ConstantClass cl = (ConstantClass)cp.getConstant(clazz.getClassNameIndex(), (byte)7);
/*     */ 
/* 218 */     ConstantUtf8 name = (ConstantUtf8)cp.getConstant(cl.getNameIndex(), (byte)1);
/*     */ 
/* 220 */     name.setBytes(class_name.replace('.', '/'));
/*     */ 
/* 222 */     return clazz;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ClassLoader
 * JD-Core Version:    0.6.2
 */