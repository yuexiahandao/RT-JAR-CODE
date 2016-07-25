/*     */ package sun.invoke.anon;
/*     */ 
/*     */ public class ConstantPoolVisitor
/*     */ {
/*     */   public static final byte CONSTANT_None = 0;
/*     */   public static final byte CONSTANT_Utf8 = 1;
/*     */   public static final byte CONSTANT_Integer = 3;
/*     */   public static final byte CONSTANT_Float = 4;
/*     */   public static final byte CONSTANT_Long = 5;
/*     */   public static final byte CONSTANT_Double = 6;
/*     */   public static final byte CONSTANT_Class = 7;
/*     */   public static final byte CONSTANT_String = 8;
/*     */   public static final byte CONSTANT_Fieldref = 9;
/*     */   public static final byte CONSTANT_Methodref = 10;
/*     */   public static final byte CONSTANT_InterfaceMethodref = 11;
/*     */   public static final byte CONSTANT_NameAndType = 12;
/* 168 */   private static String[] TAG_NAMES = { "Empty", "Utf8", null, "Integer", "Float", "Long", "Double", "Class", "String", "Fieldref", "Methodref", "InterfaceMethodref", "NameAndType" };
/*     */ 
/*     */   public void visitUTF8(int paramInt, byte paramByte, String paramString) {
/*     */   }
/*     */   public void visitConstantValue(int paramInt, byte paramByte, Object paramObject) {
/*     */   }
/*     */ 
/*     */   public void visitConstantString(int paramInt1, byte paramByte, String paramString, int paramInt2) {
/*     */   }
/*     */ 
/*     */   public void visitDescriptor(int paramInt1, byte paramByte, String paramString1, String paramString2, int paramInt2, int paramInt3) {
/*     */   }
/*     */ 
/*     */   public void visitMemberRef(int paramInt1, byte paramByte, String paramString1, String paramString2, String paramString3, int paramInt2, int paramInt3) {
/*     */   }
/*     */ 
/*     */   public static String tagName(byte paramByte) {
/* 185 */     String str = null;
/* 186 */     if ((paramByte & 0xFF) < TAG_NAMES.length)
/* 187 */       str = TAG_NAMES[paramByte];
/* 188 */     if (str == null)
/* 189 */       str = "Unknown#" + (paramByte & 0xFF);
/* 190 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.anon.ConstantPoolVisitor
 * JD-Core Version:    0.6.2
 */