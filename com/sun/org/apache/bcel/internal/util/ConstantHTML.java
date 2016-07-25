/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantString;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ final class ConstantHTML
/*     */   implements Constants
/*     */ {
/*     */   private String class_name;
/*     */   private String class_package;
/*     */   private ConstantPool constant_pool;
/*     */   private PrintWriter file;
/*     */   private String[] constant_ref;
/*     */   private Constant[] constants;
/*     */   private Method[] methods;
/*     */ 
/*     */   ConstantHTML(String dir, String class_name, String class_package, Method[] methods, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  83 */     this.class_name = class_name;
/*  84 */     this.class_package = class_package;
/*  85 */     this.constant_pool = constant_pool;
/*  86 */     this.methods = methods;
/*  87 */     this.constants = constant_pool.getConstantPool();
/*  88 */     this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_cp.html"));
/*  89 */     this.constant_ref = new String[this.constants.length];
/*  90 */     this.constant_ref[0] = "&lt;unknown&gt;";
/*     */ 
/*  92 */     this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
/*     */ 
/*  95 */     for (int i = 1; i < this.constants.length; i++) {
/*  96 */       if (i % 2 == 0)
/*  97 */         this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
/*     */       else {
/*  99 */         this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
/*     */       }
/* 101 */       if (this.constants[i] != null) {
/* 102 */         writeConstant(i);
/*     */       }
/* 104 */       this.file.print("</TD></TR>\n");
/*     */     }
/*     */ 
/* 107 */     this.file.println("</TABLE></BODY></HTML>");
/* 108 */     this.file.close();
/*     */   }
/*     */ 
/*     */   String referenceConstant(int index) {
/* 112 */     return this.constant_ref[index];
/*     */   }
/*     */ 
/*     */   private void writeConstant(int index) {
/* 116 */     byte tag = this.constants[index].getTag();
/*     */ 
/* 121 */     this.file.println("<H4> <A NAME=cp" + index + ">" + index + "</A> " + CONSTANT_NAMES[tag] + "</H4>");
/*     */     int class_index;
/*     */     int name_index;
/*     */     String ref;
/*     */     String ref;
/* 125 */     switch (tag)
/*     */     {
/*     */     case 10:
/*     */     case 11:
/*     */       int name_index;
/* 129 */       if (tag == 10) {
/* 130 */         ConstantMethodref c = (ConstantMethodref)this.constant_pool.getConstant(index, (byte)10);
/* 131 */         int class_index = c.getClassIndex();
/* 132 */         name_index = c.getNameAndTypeIndex();
/*     */       }
/*     */       else {
/* 135 */         ConstantInterfaceMethodref c1 = (ConstantInterfaceMethodref)this.constant_pool.getConstant(index, (byte)11);
/* 136 */         class_index = c1.getClassIndex();
/* 137 */         name_index = c1.getNameAndTypeIndex();
/*     */       }
/*     */ 
/* 141 */       String method_name = this.constant_pool.constantToString(name_index, (byte)12);
/* 142 */       String html_method_name = Class2HTML.toHTML(method_name);
/*     */ 
/* 145 */       String method_class = this.constant_pool.constantToString(class_index, (byte)7);
/* 146 */       String short_method_class = Utility.compactClassName(method_class);
/* 147 */       short_method_class = Utility.compactClassName(method_class);
/* 148 */       short_method_class = Utility.compactClassName(short_method_class, this.class_package + ".", true);
/*     */ 
/* 151 */       ConstantNameAndType c2 = (ConstantNameAndType)this.constant_pool.getConstant(name_index, (byte)12);
/* 152 */       String signature = this.constant_pool.constantToString(c2.getSignatureIndex(), (byte)1);
/*     */ 
/* 154 */       String[] args = Utility.methodSignatureArgumentTypes(signature, false);
/*     */ 
/* 157 */       String type = Utility.methodSignatureReturnType(signature, false);
/* 158 */       String ret_type = Class2HTML.referenceType(type);
/*     */ 
/* 160 */       StringBuffer buf = new StringBuffer("(");
/* 161 */       for (int i = 0; i < args.length; i++) {
/* 162 */         buf.append(Class2HTML.referenceType(args[i]));
/* 163 */         if (i < args.length - 1)
/* 164 */           buf.append(",&nbsp;");
/*     */       }
/* 166 */       buf.append(")");
/*     */ 
/* 168 */       String arg_types = buf.toString();
/*     */       String ref;
/* 170 */       if (method_class.equals(this.class_name)) {
/* 171 */         ref = "<A HREF=\"" + this.class_name + "_code.html#method" + getMethodNumber(new StringBuilder().append(method_name).append(signature).toString()) + "\" TARGET=Code>" + html_method_name + "</A>";
/*     */       }
/*     */       else {
/* 174 */         ref = "<A HREF=\"" + method_class + ".html" + "\" TARGET=_top>" + short_method_class + "</A>." + html_method_name;
/*     */       }
/*     */ 
/* 177 */       this.constant_ref[index] = (ret_type + "&nbsp;<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index + "\" TARGET=Constants>" + short_method_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + html_method_name + "</A>&nbsp;" + arg_types);
/*     */ 
/* 182 */       this.file.println("<P><TT>" + ret_type + "&nbsp;" + ref + arg_types + "&nbsp;</TT>\n<UL>" + "<LI><A HREF=\"#cp" + class_index + "\">Class index(" + class_index + ")</A>\n" + "<LI><A HREF=\"#cp" + name_index + "\">NameAndType index(" + name_index + ")</A></UL>");
/*     */ 
/* 185 */       break;
/*     */     case 9:
/* 189 */       ConstantFieldref c3 = (ConstantFieldref)this.constant_pool.getConstant(index, (byte)9);
/* 190 */       class_index = c3.getClassIndex();
/* 191 */       name_index = c3.getNameAndTypeIndex();
/*     */ 
/* 194 */       String field_class = this.constant_pool.constantToString(class_index, (byte)7);
/* 195 */       String short_field_class = Utility.compactClassName(field_class);
/* 196 */       short_field_class = Utility.compactClassName(short_field_class, this.class_package + ".", true);
/*     */ 
/* 198 */       String field_name = this.constant_pool.constantToString(name_index, (byte)12);
/*     */ 
/* 200 */       if (field_class.equals(this.class_name)) {
/* 201 */         ref = "<A HREF=\"" + field_class + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>";
/*     */       }
/*     */       else {
/* 204 */         ref = "<A HREF=\"" + field_class + ".html\" TARGET=_top>" + short_field_class + "</A>." + field_name + "\n";
/*     */       }
/*     */ 
/* 207 */       this.constant_ref[index] = ("<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index + "\" TARGET=Constants>" + short_field_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + field_name + "</A>");
/*     */ 
/* 211 */       this.file.println("<P><TT>" + ref + "</TT><BR>\n" + "<UL>" + "<LI><A HREF=\"#cp" + class_index + "\">Class(" + class_index + ")</A><BR>\n" + "<LI><A HREF=\"#cp" + name_index + "\">NameAndType(" + name_index + ")</A></UL>");
/*     */ 
/* 214 */       break;
/*     */     case 7:
/* 217 */       ConstantClass c4 = (ConstantClass)this.constant_pool.getConstant(index, (byte)7);
/* 218 */       name_index = c4.getNameIndex();
/* 219 */       String class_name2 = this.constant_pool.constantToString(index, tag);
/* 220 */       String short_class_name = Utility.compactClassName(class_name2);
/* 221 */       short_class_name = Utility.compactClassName(short_class_name, this.class_package + ".", true);
/*     */ 
/* 223 */       ref = "<A HREF=\"" + class_name2 + ".html\" TARGET=_top>" + short_class_name + "</A>";
/* 224 */       this.constant_ref[index] = ("<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + short_class_name + "</A>");
/*     */ 
/* 227 */       this.file.println("<P><TT>" + ref + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A></UL>\n");
/*     */ 
/* 229 */       break;
/*     */     case 8:
/* 232 */       ConstantString c5 = (ConstantString)this.constant_pool.getConstant(index, (byte)8);
/* 233 */       name_index = c5.getStringIndex();
/*     */ 
/* 235 */       String str = Class2HTML.toHTML(this.constant_pool.constantToString(index, tag));
/*     */ 
/* 237 */       this.file.println("<P><TT>" + str + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A></UL>\n");
/*     */ 
/* 239 */       break;
/*     */     case 12:
/* 242 */       ConstantNameAndType c6 = (ConstantNameAndType)this.constant_pool.getConstant(index, (byte)12);
/* 243 */       name_index = c6.getNameIndex();
/* 244 */       int signature_index = c6.getSignatureIndex();
/*     */ 
/* 246 */       this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A>\n" + "<LI><A HREF=\"#cp" + signature_index + "\">Signature index(" + signature_index + ")</A></UL>\n");
/*     */ 
/* 250 */       break;
/*     */     default:
/* 253 */       this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT>\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final int getMethodNumber(String str) {
/* 258 */     for (int i = 0; i < this.methods.length; i++) {
/* 259 */       String cmp = this.methods[i].getName() + this.methods[i].getSignature();
/* 260 */       if (cmp.equals(str))
/* 261 */         return i;
/*     */     }
/* 263 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ConstantHTML
 * JD-Core Version:    0.6.2
 */