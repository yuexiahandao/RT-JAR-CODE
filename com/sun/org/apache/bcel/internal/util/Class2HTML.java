/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ClassParser;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class Class2HTML
/*     */   implements Constants
/*     */ {
/*     */   private JavaClass java_class;
/*     */   private String dir;
/*     */   private static String class_package;
/*     */   private static String class_name;
/*     */   private static ConstantPool constant_pool;
/*     */ 
/*     */   public Class2HTML(JavaClass java_class, String dir)
/*     */     throws IOException
/*     */   {
/* 104 */     Method[] methods = java_class.getMethods();
/*     */ 
/* 106 */     this.java_class = java_class;
/* 107 */     this.dir = dir;
/* 108 */     class_name = java_class.getClassName();
/* 109 */     constant_pool = java_class.getConstantPool();
/*     */ 
/* 112 */     int index = class_name.lastIndexOf('.');
/* 113 */     if (index > -1)
/* 114 */       class_package = class_name.substring(0, index);
/*     */     else {
/* 116 */       class_package = "";
/*     */     }
/* 118 */     ConstantHTML constant_html = new ConstantHTML(dir, class_name, class_package, methods, constant_pool);
/*     */ 
/* 124 */     AttributeHTML attribute_html = new AttributeHTML(dir, class_name, constant_pool, constant_html);
/*     */ 
/* 126 */     MethodHTML method_html = new MethodHTML(dir, class_name, methods, java_class.getFields(), constant_html, attribute_html);
/*     */ 
/* 129 */     writeMainHTML(attribute_html);
/* 130 */     new CodeHTML(dir, class_name, methods, constant_pool, constant_html);
/* 131 */     attribute_html.close();
/*     */   }
/*     */ 
/*     */   public static void _main(String[] argv)
/*     */   {
/* 136 */     String[] file_name = new String[argv.length];
/* 137 */     int files = 0;
/* 138 */     ClassParser parser = null;
/* 139 */     JavaClass java_class = null;
/* 140 */     String zip_file = null;
/* 141 */     char sep = SecuritySupport.getSystemProperty("file.separator").toCharArray()[0];
/* 142 */     String dir = "." + sep;
/*     */     try
/*     */     {
/* 147 */       for (int i = 0; i < argv.length; i++) {
/* 148 */         if (argv[i].charAt(0) == '-') {
/* 149 */           if (argv[i].equals("-d")) {
/* 150 */             dir = argv[(++i)];
/*     */ 
/* 152 */             if (!dir.endsWith("" + sep)) {
/* 153 */               dir = dir + sep;
/*     */             }
/* 155 */             new File(dir).mkdirs();
/*     */           }
/* 157 */           else if (argv[i].equals("-zip")) {
/* 158 */             zip_file = argv[(++i)];
/*     */           } else {
/* 160 */             System.out.println("Unknown option " + argv[i]);
/*     */           }
/*     */         }
/* 163 */         else file_name[(files++)] = argv[i];
/*     */       }
/*     */ 
/* 166 */       if (files == 0)
/* 167 */         System.err.println("Class2HTML: No input files specified.");
/*     */       else
/* 169 */         for (int i = 0; i < files; i++) {
/* 170 */           System.out.print("Processing " + file_name[i] + "...");
/* 171 */           if (zip_file == null)
/* 172 */             parser = new ClassParser(file_name[i]);
/*     */           else {
/* 174 */             parser = new ClassParser(zip_file, file_name[i]);
/*     */           }
/* 176 */           java_class = parser.parse();
/* 177 */           new Class2HTML(java_class, dir);
/* 178 */           System.out.println("Done.");
/*     */         }
/*     */     }
/*     */     catch (Exception e) {
/* 182 */       System.out.println(e);
/* 183 */       e.printStackTrace(System.out);
/*     */     }
/*     */   }
/*     */ 
/*     */   static String referenceClass(int index)
/*     */   {
/* 192 */     String str = constant_pool.getConstantString(index, (byte)7);
/* 193 */     str = Utility.compactClassName(str);
/* 194 */     str = Utility.compactClassName(str, class_package + ".", true);
/*     */ 
/* 196 */     return "<A HREF=\"" + class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + str + "</A>";
/*     */   }
/*     */ 
/*     */   static final String referenceType(String type)
/*     */   {
/* 201 */     String short_type = Utility.compactClassName(type);
/* 202 */     short_type = Utility.compactClassName(short_type, class_package + ".", true);
/*     */ 
/* 204 */     int index = type.indexOf('[');
/* 205 */     if (index > -1) {
/* 206 */       type = type.substring(0, index);
/*     */     }
/*     */ 
/* 209 */     if ((type.equals("int")) || (type.equals("short")) || (type.equals("boolean")) || (type.equals("void")) || (type.equals("char")) || (type.equals("byte")) || (type.equals("long")) || (type.equals("double")) || (type.equals("float")))
/*     */     {
/* 212 */       return "<FONT COLOR=\"#00FF00\">" + type + "</FONT>";
/*     */     }
/* 214 */     return "<A HREF=\"" + type + ".html\" TARGET=_top>" + short_type + "</A>";
/*     */   }
/*     */ 
/*     */   static String toHTML(String str) {
/* 218 */     StringBuffer buf = new StringBuffer();
/*     */     try
/*     */     {
/* 221 */       for (int i = 0; i < str.length(); i++)
/*     */       {
/*     */         char ch;
/* 224 */         switch (ch = str.charAt(i)) { case '<':
/* 225 */           buf.append("&lt;"); break;
/*     */         case '>':
/* 226 */           buf.append("&gt;"); break;
/*     */         case '\n':
/* 227 */           buf.append("\\n"); break;
/*     */         case '\r':
/* 228 */           buf.append("\\r"); break;
/*     */         default:
/* 229 */           buf.append(ch); }
/*     */       }
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException e) {
/*     */     }
/* 234 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private void writeMainHTML(AttributeHTML attribute_html) throws IOException {
/* 238 */     PrintWriter file = new PrintWriter(new FileOutputStream(this.dir + class_name + ".html"));
/* 239 */     Attribute[] attributes = this.java_class.getAttributes();
/*     */ 
/* 241 */     file.println("<HTML>\n<HEAD><TITLE>Documentation for " + class_name + "</TITLE>" + "</HEAD>\n" + "<FRAMESET BORDER=1 cols=\"30%,*\">\n" + "<FRAMESET BORDER=1 rows=\"80%,*\">\n" + "<FRAME NAME=\"ConstantPool\" SRC=\"" + class_name + "_cp.html" + "\"\n MARGINWIDTH=\"0\" " + "MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n" + "<FRAME NAME=\"Attributes\" SRC=\"" + class_name + "_attributes.html" + "\"\n MARGINWIDTH=\"0\" " + "MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n" + "</FRAMESET>\n" + "<FRAMESET BORDER=1 rows=\"80%,*\">\n" + "<FRAME NAME=\"Code\" SRC=\"" + class_name + "_code.html\"\n MARGINWIDTH=0 " + "MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n" + "<FRAME NAME=\"Methods\" SRC=\"" + class_name + "_methods.html\"\n MARGINWIDTH=0 " + "MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n" + "</FRAMESET></FRAMESET></HTML>");
/*     */ 
/* 261 */     file.close();
/*     */ 
/* 263 */     for (int i = 0; i < attributes.length; i++)
/* 264 */       attribute_html.writeAttribute(attributes[i], "class" + i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.Class2HTML
 * JD-Core Version:    0.6.2
 */