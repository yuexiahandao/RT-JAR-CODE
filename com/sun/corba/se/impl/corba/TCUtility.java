/*     */ package com.sun.corba.se.impl.corba;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.encoding.CDROutputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.Principal;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class TCUtility
/*     */ {
/*     */   static void marshalIn(org.omg.CORBA.portable.OutputStream paramOutputStream, TypeCode paramTypeCode, long paramLong, java.lang.Object paramObject)
/*     */   {
/*  65 */     switch (paramTypeCode.kind().value())
/*     */     {
/*     */     case 0:
/*     */     case 1:
/*     */     case 31:
/*  70 */       break;
/*     */     case 2:
/*  73 */       paramOutputStream.write_short((short)(int)(paramLong & 0xFFFF));
/*  74 */       break;
/*     */     case 4:
/*  77 */       paramOutputStream.write_ushort((short)(int)(paramLong & 0xFFFF));
/*  78 */       break;
/*     */     case 3:
/*     */     case 17:
/*  82 */       paramOutputStream.write_long((int)(paramLong & 0xFFFFFFFF));
/*  83 */       break;
/*     */     case 5:
/*  86 */       paramOutputStream.write_ulong((int)(paramLong & 0xFFFFFFFF));
/*  87 */       break;
/*     */     case 6:
/*  90 */       paramOutputStream.write_float(Float.intBitsToFloat((int)(paramLong & 0xFFFFFFFF)));
/*  91 */       break;
/*     */     case 7:
/*  94 */       paramOutputStream.write_double(Double.longBitsToDouble(paramLong));
/*  95 */       break;
/*     */     case 8:
/*  98 */       if (paramLong == 0L)
/*  99 */         paramOutputStream.write_boolean(false);
/*     */       else
/* 101 */         paramOutputStream.write_boolean(true);
/* 102 */       break;
/*     */     case 9:
/* 105 */       paramOutputStream.write_char((char)(int)(paramLong & 0xFFFF));
/* 106 */       break;
/*     */     case 10:
/* 109 */       paramOutputStream.write_octet((byte)(int)(paramLong & 0xFF));
/* 110 */       break;
/*     */     case 11:
/* 113 */       paramOutputStream.write_any((Any)paramObject);
/* 114 */       break;
/*     */     case 12:
/* 117 */       paramOutputStream.write_TypeCode((TypeCode)paramObject);
/* 118 */       break;
/*     */     case 13:
/* 121 */       paramOutputStream.write_Principal((Principal)paramObject);
/* 122 */       break;
/*     */     case 14:
/* 125 */       paramOutputStream.write_Object((org.omg.CORBA.Object)paramObject);
/* 126 */       break;
/*     */     case 23:
/* 129 */       paramOutputStream.write_longlong(paramLong);
/* 130 */       break;
/*     */     case 24:
/* 133 */       paramOutputStream.write_ulonglong(paramLong);
/* 134 */       break;
/*     */     case 26:
/* 137 */       paramOutputStream.write_wchar((char)(int)(paramLong & 0xFFFF));
/* 138 */       break;
/*     */     case 18:
/* 141 */       paramOutputStream.write_string((String)paramObject);
/* 142 */       break;
/*     */     case 27:
/* 145 */       paramOutputStream.write_wstring((String)paramObject);
/* 146 */       break;
/*     */     case 29:
/*     */     case 30:
/* 150 */       ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value((Serializable)paramObject);
/* 151 */       break;
/*     */     case 28:
/* 156 */       if ((paramOutputStream instanceof CDROutputStream))
/*     */         try {
/* 158 */           ((CDROutputStream)paramOutputStream).write_fixed((BigDecimal)paramObject, paramTypeCode.fixed_digits(), paramTypeCode.fixed_scale());
/*     */         }
/*     */         catch (BadKind localBadKind)
/*     */         {
/*     */         }
/*     */       else {
/* 164 */         paramOutputStream.write_fixed((BigDecimal)paramObject);
/*     */       }
/* 166 */       break;
/*     */     case 15:
/*     */     case 16:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/* 174 */       ((Streamable)paramObject)._write(paramOutputStream);
/* 175 */       break;
/*     */     case 32:
/* 178 */       ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_abstract_interface(paramObject);
/* 179 */       break;
/*     */     case 25:
/*     */     default:
/* 184 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get((ORB)paramOutputStream.orb(), "rpc.presentation");
/*     */ 
/* 187 */       throw localORBUtilSystemException.typecodeNotSupported();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void unmarshalIn(org.omg.CORBA.portable.InputStream paramInputStream, TypeCode paramTypeCode, long[] paramArrayOfLong, java.lang.Object[] paramArrayOfObject)
/*     */   {
/* 193 */     int i = paramTypeCode.kind().value();
/* 194 */     long l = 0L;
/* 195 */     java.lang.Object localObject = paramArrayOfObject[0];
/*     */ 
/* 197 */     switch (i)
/*     */     {
/*     */     case 0:
/*     */     case 1:
/*     */     case 31:
/* 202 */       break;
/*     */     case 2:
/* 205 */       l = paramInputStream.read_short() & 0xFFFF;
/* 206 */       break;
/*     */     case 4:
/* 209 */       l = paramInputStream.read_ushort() & 0xFFFF;
/* 210 */       break;
/*     */     case 3:
/*     */     case 17:
/* 214 */       l = paramInputStream.read_long() & 0xFFFFFFFF;
/* 215 */       break;
/*     */     case 5:
/* 218 */       l = paramInputStream.read_ulong() & 0xFFFFFFFF;
/* 219 */       break;
/*     */     case 6:
/* 222 */       l = Float.floatToIntBits(paramInputStream.read_float()) & 0xFFFFFFFF;
/* 223 */       break;
/*     */     case 7:
/* 226 */       l = Double.doubleToLongBits(paramInputStream.read_double());
/* 227 */       break;
/*     */     case 9:
/* 230 */       l = paramInputStream.read_char() & 0xFFFF;
/* 231 */       break;
/*     */     case 10:
/* 234 */       l = paramInputStream.read_octet() & 0xFF;
/* 235 */       break;
/*     */     case 8:
/* 238 */       if (paramInputStream.read_boolean())
/* 239 */         l = 1L;
/*     */       else
/* 241 */         l = 0L;
/* 242 */       break;
/*     */     case 11:
/* 245 */       localObject = paramInputStream.read_any();
/* 246 */       break;
/*     */     case 12:
/* 249 */       localObject = paramInputStream.read_TypeCode();
/* 250 */       break;
/*     */     case 13:
/* 253 */       localObject = paramInputStream.read_Principal();
/* 254 */       break;
/*     */     case 14:
/* 257 */       if ((localObject instanceof Streamable))
/* 258 */         ((Streamable)localObject)._read(paramInputStream);
/*     */       else
/* 260 */         localObject = paramInputStream.read_Object();
/* 261 */       break;
/*     */     case 23:
/* 264 */       l = paramInputStream.read_longlong();
/* 265 */       break;
/*     */     case 24:
/* 268 */       l = paramInputStream.read_ulonglong();
/* 269 */       break;
/*     */     case 26:
/* 272 */       l = paramInputStream.read_wchar() & 0xFFFF;
/* 273 */       break;
/*     */     case 18:
/* 276 */       localObject = paramInputStream.read_string();
/* 277 */       break;
/*     */     case 27:
/* 280 */       localObject = paramInputStream.read_wstring();
/* 281 */       break;
/*     */     case 29:
/*     */     case 30:
/* 285 */       localObject = ((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value();
/* 286 */       break;
/*     */     case 28:
/*     */       try
/*     */       {
/* 292 */         if ((paramInputStream instanceof CDRInputStream)) {
/* 293 */           localObject = ((CDRInputStream)paramInputStream).read_fixed(paramTypeCode.fixed_digits(), paramTypeCode.fixed_scale());
/*     */         }
/*     */         else {
/* 296 */           BigDecimal localBigDecimal = paramInputStream.read_fixed();
/* 297 */           localObject = localBigDecimal.movePointLeft(paramTypeCode.fixed_scale());
/*     */         }
/*     */       }
/*     */       catch (BadKind localBadKind)
/*     */       {
/*     */       }
/*     */     case 15:
/*     */     case 16:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/* 309 */       ((Streamable)localObject)._read(paramInputStream);
/* 310 */       break;
/*     */     case 32:
/* 313 */       localObject = ((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_abstract_interface();
/* 314 */       break;
/*     */     case 25:
/*     */     default:
/* 319 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get((ORB)paramInputStream.orb(), "rpc.presentation");
/*     */ 
/* 322 */       throw localORBUtilSystemException.typecodeNotSupported();
/*     */     }
/*     */ 
/* 325 */     paramArrayOfObject[0] = localObject;
/* 326 */     paramArrayOfLong[0] = l;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.TCUtility
 * JD-Core Version:    0.6.2
 */