/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Code;
/*     */ import com.sun.org.apache.bcel.internal.classfile.CodeException;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.InnerClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.InnerClasses;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LineNumber;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.SourceFile;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ final class AttributeHTML
/*     */   implements Constants
/*     */ {
/*     */   private String class_name;
/*     */   private PrintWriter file;
/*  73 */   private int attr_count = 0;
/*     */   private ConstantHTML constant_html;
/*     */   private ConstantPool constant_pool;
/*     */ 
/*     */   AttributeHTML(String dir, String class_name, ConstantPool constant_pool, ConstantHTML constant_html)
/*     */     throws IOException
/*     */   {
/*  80 */     this.class_name = class_name;
/*  81 */     this.constant_pool = constant_pool;
/*  82 */     this.constant_html = constant_html;
/*     */ 
/*  84 */     this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_attributes.html"));
/*  85 */     this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
/*     */   }
/*     */ 
/*     */   private final String codeLink(int link, int method_number) {
/*  89 */     return "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + link + "\" TARGET=Code>" + link + "</A>";
/*     */   }
/*     */ 
/*     */   final void close()
/*     */   {
/*  95 */     this.file.println("</TABLE></BODY></HTML>");
/*  96 */     this.file.close();
/*     */   }
/*     */ 
/*     */   final void writeAttribute(Attribute attribute, String anchor) throws IOException {
/* 100 */     writeAttribute(attribute, anchor, 0);
/*     */   }
/*     */ 
/*     */   final void writeAttribute(Attribute attribute, String anchor, int method_number) throws IOException {
/* 104 */     byte tag = attribute.getTag();
/*     */ 
/* 107 */     if (tag == -1) {
/* 108 */       return;
/*     */     }
/* 110 */     this.attr_count += 1;
/*     */ 
/* 112 */     if (this.attr_count % 2 == 0)
/* 113 */       this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
/*     */     else {
/* 115 */       this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
/*     */     }
/* 117 */     this.file.println("<H4><A NAME=\"" + anchor + "\">" + this.attr_count + " " + ATTRIBUTE_NAMES[tag] + "</A></H4>");
/*     */     int index;
/* 121 */     switch (tag) {
/*     */     case 2:
/* 123 */       Code c = (Code)attribute;
/*     */ 
/* 126 */       this.file.print("<UL><LI>Maximum stack size = " + c.getMaxStack() + "</LI>\n<LI>Number of local variables = " + c.getMaxLocals() + "</LI>\n<LI><A HREF=\"" + this.class_name + "_code.html#method" + method_number + "\" TARGET=Code>Byte code</A></LI></UL>\n");
/*     */ 
/* 132 */       CodeException[] ce = c.getExceptionTable();
/* 133 */       int len = ce.length;
/*     */ 
/* 135 */       if (len > 0) {
/* 136 */         this.file.print("<P><B>Exceptions handled</B><UL>");
/*     */ 
/* 138 */         for (int i = 0; i < len; i++) {
/* 139 */           int catch_type = ce[i].getCatchType();
/*     */ 
/* 141 */           this.file.print("<LI>");
/*     */ 
/* 143 */           if (catch_type != 0)
/* 144 */             this.file.print(this.constant_html.referenceConstant(catch_type));
/*     */           else {
/* 146 */             this.file.print("Any Exception");
/*     */           }
/* 148 */           this.file.print("<BR>(Ranging from lines " + codeLink(ce[i].getStartPC(), method_number) + " to " + codeLink(ce[i].getEndPC(), method_number) + ", handled at line " + codeLink(ce[i].getHandlerPC(), method_number) + ")</LI>");
/*     */         }
/*     */ 
/* 152 */         this.file.print("</UL>"); } break;
/*     */     case 1:
/* 157 */       index = ((ConstantValue)attribute).getConstantValueIndex();
/*     */ 
/* 160 */       this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">Constant value index(" + index + ")</A></UL>\n");
/*     */ 
/* 162 */       break;
/*     */     case 0:
/* 165 */       index = ((SourceFile)attribute).getSourceFileIndex();
/*     */ 
/* 168 */       this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">Source file index(" + index + ")</A></UL>\n");
/*     */ 
/* 170 */       break;
/*     */     case 3:
/* 174 */       int[] indices = ((ExceptionTable)attribute).getExceptionIndexTable();
/*     */ 
/* 176 */       this.file.print("<UL>");
/*     */ 
/* 178 */       for (int i = 0; i < indices.length; i++) {
/* 179 */         this.file.print("<LI><A HREF=\"" + this.class_name + "_cp.html#cp" + indices[i] + "\" TARGET=\"ConstantPool\">Exception class index(" + indices[i] + ")</A>\n");
/*     */       }
/*     */ 
/* 182 */       this.file.print("</UL>\n");
/* 183 */       break;
/*     */     case 4:
/* 186 */       LineNumber[] line_numbers = ((LineNumberTable)attribute).getLineNumberTable();
/*     */ 
/* 189 */       this.file.print("<P>");
/*     */ 
/* 191 */       for (int i = 0; i < line_numbers.length; i++) {
/* 192 */         this.file.print("(" + line_numbers[i].getStartPC() + ",&nbsp;" + line_numbers[i].getLineNumber() + ")");
/*     */ 
/* 194 */         if (i < line_numbers.length - 1)
/* 195 */           this.file.print(", ");
/*     */       }
/* 197 */       break;
/*     */     case 5:
/* 200 */       LocalVariable[] vars = ((LocalVariableTable)attribute).getLocalVariableTable();
/*     */ 
/* 203 */       this.file.print("<UL>");
/*     */ 
/* 205 */       for (int i = 0; i < vars.length; i++) {
/* 206 */         index = vars[i].getSignatureIndex();
/* 207 */         String signature = ((ConstantUtf8)this.constant_pool.getConstant(index, (byte)1)).getBytes();
/* 208 */         signature = Utility.signatureToString(signature, false);
/* 209 */         int start = vars[i].getStartPC();
/* 210 */         int end = start + vars[i].getLength();
/*     */ 
/* 212 */         this.file.println("<LI>" + Class2HTML.referenceType(signature) + "&nbsp;<B>" + vars[i].getName() + "</B> in slot %" + vars[i].getIndex() + "<BR>Valid from lines " + "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + start + "\" TARGET=Code>" + start + "</A> to " + "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + end + "\" TARGET=Code>" + end + "</A></LI>");
/*     */       }
/*     */ 
/* 220 */       this.file.print("</UL>\n");
/*     */ 
/* 222 */       break;
/*     */     case 6:
/* 225 */       InnerClass[] classes = ((InnerClasses)attribute).getInnerClasses();
/*     */ 
/* 228 */       this.file.print("<UL>");
/*     */ 
/* 230 */       for (int i = 0; i < classes.length; i++)
/*     */       {
/* 233 */         int index = classes[i].getInnerNameIndex();
/*     */         String name;
/*     */         String name;
/* 234 */         if (index > 0)
/* 235 */           name = ((ConstantUtf8)this.constant_pool.getConstant(index, (byte)1)).getBytes();
/*     */         else {
/* 237 */           name = "&lt;anonymous&gt;";
/*     */         }
/* 239 */         String access = Utility.accessToString(classes[i].getInnerAccessFlags());
/*     */ 
/* 241 */         this.file.print("<LI><FONT COLOR=\"#FF0000\">" + access + "</FONT> " + this.constant_html.referenceConstant(classes[i].getInnerClassIndex()) + " in&nbsp;class " + this.constant_html.referenceConstant(classes[i].getOuterClassIndex()) + " named " + name + "</LI>\n");
/*     */       }
/*     */ 
/* 248 */       this.file.print("</UL>\n");
/* 249 */       break;
/*     */     default:
/* 252 */       this.file.print("<P>" + attribute.toString());
/*     */     }
/*     */ 
/* 255 */     this.file.println("</TD></TR>");
/* 256 */     this.file.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.AttributeHTML
 * JD-Core Version:    0.6.2
 */