/*     */ package com.sun.xml.internal.bind.v2.bytecode;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public final class ClassTailor
/*     */ {
/*  47 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*     */   public static String toVMClassName(Class c)
/*     */   {
/*  53 */     assert (!c.isPrimitive());
/*  54 */     if (c.isArray())
/*     */     {
/*  56 */       return toVMTypeName(c);
/*  57 */     }return c.getName().replace('.', '/');
/*     */   }
/*     */ 
/*     */   public static String toVMTypeName(Class c) {
/*  61 */     if (c.isArray())
/*     */     {
/*  63 */       return '[' + toVMTypeName(c.getComponentType());
/*     */     }
/*  65 */     if (c.isPrimitive()) {
/*  66 */       if (c == Boolean.TYPE) return "Z";
/*  67 */       if (c == Character.TYPE) return "C";
/*  68 */       if (c == Byte.TYPE) return "B";
/*  69 */       if (c == Double.TYPE) return "D";
/*  70 */       if (c == Float.TYPE) return "F";
/*  71 */       if (c == Integer.TYPE) return "I";
/*  72 */       if (c == Long.TYPE) return "J";
/*  73 */       if (c == Short.TYPE) return "S";
/*     */ 
/*  75 */       throw new IllegalArgumentException(c.getName());
/*     */     }
/*  77 */     return 'L' + c.getName().replace('.', '/') + ';';
/*     */   }
/*     */ 
/*     */   public static byte[] tailor(Class templateClass, String newClassName, String[] replacements)
/*     */   {
/*  83 */     String vmname = toVMClassName(templateClass);
/*  84 */     return tailor(templateClass.getClassLoader().getResourceAsStream(vmname + ".class"), vmname, newClassName, replacements);
/*     */   }
/*     */ 
/*     */   public static byte[] tailor(InputStream image, String templateClassName, String newClassName, String[] replacements)
/*     */   {
/* 103 */     DataInputStream in = new DataInputStream(image);
/*     */     try
/*     */     {
/* 106 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 107 */       DataOutputStream out = new DataOutputStream(baos);
/*     */ 
/* 110 */       long l = in.readLong();
/* 111 */       out.writeLong(l);
/*     */ 
/* 114 */       short count = in.readShort();
/* 115 */       out.writeShort(count);
/*     */ 
/* 118 */       for (int i = 0; i < count; i++) {
/* 119 */         byte tag = in.readByte();
/* 120 */         out.writeByte(tag);
/* 121 */         switch (tag)
/*     */         {
/*     */         case 0:
/* 127 */           break;
/*     */         case 1:
/* 131 */           String value = in.readUTF();
/* 132 */           if (value.equals(templateClassName))
/* 133 */             value = newClassName;
/*     */           else {
/* 135 */             for (int j = 0; j < replacements.length; j += 2)
/* 136 */               if (value.equals(replacements[j])) {
/* 137 */                 value = replacements[(j + 1)];
/* 138 */                 break;
/*     */               }
/*     */           }
/* 141 */           out.writeUTF(value);
/*     */ 
/* 143 */           break;
/*     */         case 3:
/*     */         case 4:
/* 147 */           out.writeInt(in.readInt());
/* 148 */           break;
/*     */         case 5:
/*     */         case 6:
/* 152 */           i++;
/* 153 */           out.writeLong(in.readLong());
/* 154 */           break;
/*     */         case 7:
/*     */         case 8:
/* 158 */           out.writeShort(in.readShort());
/* 159 */           break;
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/* 165 */           out.writeInt(in.readInt());
/* 166 */           break;
/*     */         case 2:
/*     */         default:
/* 169 */           throw new IllegalArgumentException("Unknown constant type " + tag);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 174 */       byte[] buf = new byte[512];
/*     */       int len;
/* 176 */       while ((len = in.read(buf)) > 0) {
/* 177 */         out.write(buf, 0, len);
/*     */       }
/* 179 */       in.close();
/* 180 */       out.close();
/*     */ 
/* 183 */       return baos.toByteArray();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 187 */       logger.log(Level.WARNING, "failed to tailor", e);
/* 188 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.bytecode.ClassTailor
 * JD-Core Version:    0.6.2
 */