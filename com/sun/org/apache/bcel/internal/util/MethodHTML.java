/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Code;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ final class MethodHTML
/*     */   implements Constants
/*     */ {
/*     */   private String class_name;
/*     */   private PrintWriter file;
/*     */   private ConstantHTML constant_html;
/*     */   private AttributeHTML attribute_html;
/*     */ 
/*     */   MethodHTML(String dir, String class_name, Method[] methods, Field[] fields, ConstantHTML constant_html, AttributeHTML attribute_html)
/*     */     throws IOException
/*     */   {
/*  80 */     this.class_name = class_name;
/*  81 */     this.attribute_html = attribute_html;
/*  82 */     this.constant_html = constant_html;
/*     */ 
/*  84 */     this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_methods.html"));
/*     */ 
/*  86 */     this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
/*  87 */     this.file.println("<TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Type</TH><TH ALIGN=LEFT>Field&nbsp;name</TH></TR>");
/*     */ 
/*  89 */     for (int i = 0; i < fields.length; i++)
/*  90 */       writeField(fields[i]);
/*  91 */     this.file.println("</TABLE>");
/*     */ 
/*  93 */     this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Return&nbsp;type</TH><TH ALIGN=LEFT>Method&nbsp;name</TH><TH ALIGN=LEFT>Arguments</TH></TR>");
/*     */ 
/*  96 */     for (int i = 0; i < methods.length; i++) {
/*  97 */       writeMethod(methods[i], i);
/*     */     }
/*  99 */     this.file.println("</TABLE></BODY></HTML>");
/* 100 */     this.file.close();
/*     */   }
/*     */ 
/*     */   private void writeField(Field field)
/*     */     throws IOException
/*     */   {
/* 110 */     String type = Utility.signatureToString(field.getSignature());
/* 111 */     String name = field.getName();
/* 112 */     String access = Utility.accessToString(field.getAccessFlags());
/*     */ 
/* 115 */     access = Utility.replace(access, " ", "&nbsp;");
/*     */ 
/* 117 */     this.file.print("<TR><TD><FONT COLOR=\"#FF0000\">" + access + "</FONT></TD>\n<TD>" + Class2HTML.referenceType(type) + "</TD><TD><A NAME=\"field" + name + "\">" + name + "</A></TD>");
/*     */ 
/* 121 */     Attribute[] attributes = field.getAttributes();
/*     */ 
/* 124 */     for (int i = 0; i < attributes.length; i++) {
/* 125 */       this.attribute_html.writeAttribute(attributes[i], name + "@" + i);
/*     */     }
/* 127 */     for (int i = 0; i < attributes.length; i++) {
/* 128 */       if (attributes[i].getTag() == 1) {
/* 129 */         String str = ((ConstantValue)attributes[i]).toString();
/*     */ 
/* 132 */         this.file.print("<TD>= <A HREF=\"" + this.class_name + "_attributes.html#" + name + "@" + i + "\" TARGET=\"Attributes\">" + str + "</TD>\n");
/*     */ 
/* 134 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 138 */     this.file.println("</TR>");
/*     */   }
/*     */ 
/*     */   private final void writeMethod(Method method, int method_number) throws IOException
/*     */   {
/* 143 */     String signature = method.getSignature();
/*     */ 
/* 145 */     String[] args = Utility.methodSignatureArgumentTypes(signature, false);
/*     */ 
/* 147 */     String type = Utility.methodSignatureReturnType(signature, false);
/*     */ 
/* 149 */     String name = method.getName();
/*     */ 
/* 151 */     String access = Utility.accessToString(method.getAccessFlags());
/*     */ 
/* 153 */     Attribute[] attributes = method.getAttributes();
/*     */ 
/* 158 */     access = Utility.replace(access, " ", "&nbsp;");
/* 159 */     String html_name = Class2HTML.toHTML(name);
/*     */ 
/* 161 */     this.file.print("<TR VALIGN=TOP><TD><FONT COLOR=\"#FF0000\"><A NAME=method" + method_number + ">" + access + "</A></FONT></TD>");
/*     */ 
/* 164 */     this.file.print("<TD>" + Class2HTML.referenceType(type) + "</TD><TD>" + "<A HREF=" + this.class_name + "_code.html#method" + method_number + " TARGET=Code>" + html_name + "</A></TD>\n<TD>(");
/*     */ 
/* 168 */     for (int i = 0; i < args.length; i++) {
/* 169 */       this.file.print(Class2HTML.referenceType(args[i]));
/* 170 */       if (i < args.length - 1) {
/* 171 */         this.file.print(", ");
/*     */       }
/*     */     }
/* 174 */     this.file.print(")</TD></TR>");
/*     */ 
/* 177 */     for (int i = 0; i < attributes.length; i++) {
/* 178 */       this.attribute_html.writeAttribute(attributes[i], "method" + method_number + "@" + i, method_number);
/*     */ 
/* 181 */       byte tag = attributes[i].getTag();
/* 182 */       if (tag == 3) {
/* 183 */         this.file.print("<TR VALIGN=TOP><TD COLSPAN=2></TD><TH ALIGN=LEFT>throws</TH><TD>");
/* 184 */         int[] exceptions = ((ExceptionTable)attributes[i]).getExceptionIndexTable();
/*     */ 
/* 186 */         for (int j = 0; j < exceptions.length; j++) {
/* 187 */           this.file.print(this.constant_html.referenceConstant(exceptions[j]));
/*     */ 
/* 189 */           if (j < exceptions.length - 1)
/* 190 */             this.file.print(", ");
/*     */         }
/* 192 */         this.file.println("</TD></TR>");
/* 193 */       } else if (tag == 2) {
/* 194 */         Attribute[] c_a = ((Code)attributes[i]).getAttributes();
/*     */ 
/* 196 */         for (int j = 0; j < c_a.length; j++)
/* 197 */           this.attribute_html.writeAttribute(c_a[j], "method" + method_number + "@" + i + "@" + j, method_number);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.MethodHTML
 * JD-Core Version:    0.6.2
 */