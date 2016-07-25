/*      */ package javax.print;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.util.Map;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class DocFlavor
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   private static final long serialVersionUID = -4512080796965449721L;
/*  465 */   public static final String hostEncoding = (String)AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
/*      */   private transient MimeType myMimeType;
/*      */   private String myClassName;
/*  484 */   private transient String myStringValue = null;
/*      */ 
/*      */   public DocFlavor(String paramString1, String paramString2)
/*      */   {
/*  503 */     if (paramString2 == null) {
/*  504 */       throw new NullPointerException();
/*      */     }
/*  506 */     this.myMimeType = new MimeType(paramString1);
/*  507 */     this.myClassName = paramString2;
/*      */   }
/*      */ 
/*      */   public String getMimeType()
/*      */   {
/*  516 */     return this.myMimeType.getMimeType();
/*      */   }
/*      */ 
/*      */   public String getMediaType()
/*      */   {
/*  524 */     return this.myMimeType.getMediaType();
/*      */   }
/*      */ 
/*      */   public String getMediaSubtype()
/*      */   {
/*  532 */     return this.myMimeType.getMediaSubtype();
/*      */   }
/*      */ 
/*      */   public String getParameter(String paramString)
/*      */   {
/*  551 */     return (String)this.myMimeType.getParameterMap().get(paramString.toLowerCase());
/*      */   }
/*      */ 
/*      */   public String getRepresentationClassName()
/*      */   {
/*  560 */     return this.myClassName;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  572 */     return getStringValue();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  579 */     return getStringValue().hashCode();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  601 */     return (paramObject != null) && ((paramObject instanceof DocFlavor)) && (getStringValue().equals(((DocFlavor)paramObject).getStringValue()));
/*      */   }
/*      */ 
/*      */   private String getStringValue()
/*      */   {
/*  611 */     if (this.myStringValue == null) {
/*  612 */       this.myStringValue = (this.myMimeType + "; class=\"" + this.myClassName + "\"");
/*      */     }
/*  614 */     return this.myStringValue;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  622 */     paramObjectOutputStream.defaultWriteObject();
/*  623 */     paramObjectOutputStream.writeObject(this.myMimeType.getMimeType());
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/*  637 */     paramObjectInputStream.defaultReadObject();
/*  638 */     this.myMimeType = new MimeType((String)paramObjectInputStream.readObject());
/*      */   }
/*      */ 
/*      */   public static class BYTE_ARRAY extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = -9065578006593857475L;
/*  676 */     public static final BYTE_ARRAY TEXT_PLAIN_HOST = new BYTE_ARRAY("text/plain; charset=" + hostEncoding);
/*      */ 
/*  685 */     public static final BYTE_ARRAY TEXT_PLAIN_UTF_8 = new BYTE_ARRAY("text/plain; charset=utf-8");
/*      */ 
/*  694 */     public static final BYTE_ARRAY TEXT_PLAIN_UTF_16 = new BYTE_ARRAY("text/plain; charset=utf-16");
/*      */ 
/*  705 */     public static final BYTE_ARRAY TEXT_PLAIN_UTF_16BE = new BYTE_ARRAY("text/plain; charset=utf-16be");
/*      */ 
/*  715 */     public static final BYTE_ARRAY TEXT_PLAIN_UTF_16LE = new BYTE_ARRAY("text/plain; charset=utf-16le");
/*      */ 
/*  724 */     public static final BYTE_ARRAY TEXT_PLAIN_US_ASCII = new BYTE_ARRAY("text/plain; charset=us-ascii");
/*      */ 
/*  735 */     public static final BYTE_ARRAY TEXT_HTML_HOST = new BYTE_ARRAY("text/html; charset=" + hostEncoding);
/*      */ 
/*  744 */     public static final BYTE_ARRAY TEXT_HTML_UTF_8 = new BYTE_ARRAY("text/html; charset=utf-8");
/*      */ 
/*  753 */     public static final BYTE_ARRAY TEXT_HTML_UTF_16 = new BYTE_ARRAY("text/html; charset=utf-16");
/*      */ 
/*  763 */     public static final BYTE_ARRAY TEXT_HTML_UTF_16BE = new BYTE_ARRAY("text/html; charset=utf-16be");
/*      */ 
/*  773 */     public static final BYTE_ARRAY TEXT_HTML_UTF_16LE = new BYTE_ARRAY("text/html; charset=utf-16le");
/*      */ 
/*  782 */     public static final BYTE_ARRAY TEXT_HTML_US_ASCII = new BYTE_ARRAY("text/html; charset=us-ascii");
/*      */ 
/*  790 */     public static final BYTE_ARRAY PDF = new BYTE_ARRAY("application/pdf");
/*      */ 
/*  797 */     public static final BYTE_ARRAY POSTSCRIPT = new BYTE_ARRAY("application/postscript");
/*      */ 
/*  805 */     public static final BYTE_ARRAY PCL = new BYTE_ARRAY("application/vnd.hp-PCL");
/*      */ 
/*  812 */     public static final BYTE_ARRAY GIF = new BYTE_ARRAY("image/gif");
/*      */ 
/*  818 */     public static final BYTE_ARRAY JPEG = new BYTE_ARRAY("image/jpeg");
/*      */ 
/*  824 */     public static final BYTE_ARRAY PNG = new BYTE_ARRAY("image/png");
/*      */ 
/*  833 */     public static final BYTE_ARRAY AUTOSENSE = new BYTE_ARRAY("application/octet-stream");
/*      */ 
/*      */     public BYTE_ARRAY(String paramString)
/*      */     {
/*  666 */       super("[B");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CHAR_ARRAY extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = -8720590903724405128L;
/* 1267 */     public static final CHAR_ARRAY TEXT_PLAIN = new CHAR_ARRAY("text/plain; charset=utf-16");
/*      */ 
/* 1275 */     public static final CHAR_ARRAY TEXT_HTML = new CHAR_ARRAY("text/html; charset=utf-16");
/*      */ 
/*      */     public CHAR_ARRAY(String paramString)
/*      */     {
/* 1259 */       super("[C");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class INPUT_STREAM extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = -7045842700749194127L;
/*  875 */     public static final INPUT_STREAM TEXT_PLAIN_HOST = new INPUT_STREAM("text/plain; charset=" + hostEncoding);
/*      */ 
/*  884 */     public static final INPUT_STREAM TEXT_PLAIN_UTF_8 = new INPUT_STREAM("text/plain; charset=utf-8");
/*      */ 
/*  893 */     public static final INPUT_STREAM TEXT_PLAIN_UTF_16 = new INPUT_STREAM("text/plain; charset=utf-16");
/*      */ 
/*  903 */     public static final INPUT_STREAM TEXT_PLAIN_UTF_16BE = new INPUT_STREAM("text/plain; charset=utf-16be");
/*      */ 
/*  913 */     public static final INPUT_STREAM TEXT_PLAIN_UTF_16LE = new INPUT_STREAM("text/plain; charset=utf-16le");
/*      */ 
/*  922 */     public static final INPUT_STREAM TEXT_PLAIN_US_ASCII = new INPUT_STREAM("text/plain; charset=us-ascii");
/*      */ 
/*  932 */     public static final INPUT_STREAM TEXT_HTML_HOST = new INPUT_STREAM("text/html; charset=" + hostEncoding);
/*      */ 
/*  941 */     public static final INPUT_STREAM TEXT_HTML_UTF_8 = new INPUT_STREAM("text/html; charset=utf-8");
/*      */ 
/*  950 */     public static final INPUT_STREAM TEXT_HTML_UTF_16 = new INPUT_STREAM("text/html; charset=utf-16");
/*      */ 
/*  960 */     public static final INPUT_STREAM TEXT_HTML_UTF_16BE = new INPUT_STREAM("text/html; charset=utf-16be");
/*      */ 
/*  970 */     public static final INPUT_STREAM TEXT_HTML_UTF_16LE = new INPUT_STREAM("text/html; charset=utf-16le");
/*      */ 
/*  979 */     public static final INPUT_STREAM TEXT_HTML_US_ASCII = new INPUT_STREAM("text/html; charset=us-ascii");
/*      */ 
/*  988 */     public static final INPUT_STREAM PDF = new INPUT_STREAM("application/pdf");
/*      */ 
/*  995 */     public static final INPUT_STREAM POSTSCRIPT = new INPUT_STREAM("application/postscript");
/*      */ 
/* 1003 */     public static final INPUT_STREAM PCL = new INPUT_STREAM("application/vnd.hp-PCL");
/*      */ 
/* 1011 */     public static final INPUT_STREAM GIF = new INPUT_STREAM("image/gif");
/*      */ 
/* 1018 */     public static final INPUT_STREAM JPEG = new INPUT_STREAM("image/jpeg");
/*      */ 
/* 1025 */     public static final INPUT_STREAM PNG = new INPUT_STREAM("image/png");
/*      */ 
/* 1035 */     public static final INPUT_STREAM AUTOSENSE = new INPUT_STREAM("application/octet-stream");
/*      */ 
/*      */     public INPUT_STREAM(String paramString)
/*      */     {
/*  865 */       super("java.io.InputStream");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class READER extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = 7100295812579351567L;
/* 1365 */     public static final READER TEXT_PLAIN = new READER("text/plain; charset=utf-16");
/*      */ 
/* 1373 */     public static final READER TEXT_HTML = new READER("text/html; charset=utf-16");
/*      */ 
/*      */     public READER(String paramString)
/*      */     {
/* 1357 */       super("java.io.Reader");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class SERVICE_FORMATTED extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = 6181337766266637256L;
/* 1412 */     public static final SERVICE_FORMATTED RENDERABLE_IMAGE = new SERVICE_FORMATTED("java.awt.image.renderable.RenderableImage");
/*      */ 
/* 1420 */     public static final SERVICE_FORMATTED PRINTABLE = new SERVICE_FORMATTED("java.awt.print.Printable");
/*      */ 
/* 1428 */     public static final SERVICE_FORMATTED PAGEABLE = new SERVICE_FORMATTED("java.awt.print.Pageable");
/*      */ 
/*      */     public SERVICE_FORMATTED(String paramString)
/*      */     {
/* 1403 */       super(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class STRING extends DocFlavor
/*      */   {
/*      */     private static final long serialVersionUID = 4414407504887034035L;
/* 1316 */     public static final STRING TEXT_PLAIN = new STRING("text/plain; charset=utf-16");
/*      */ 
/* 1324 */     public static final STRING TEXT_HTML = new STRING("text/html; charset=utf-16");
/*      */ 
/*      */     public STRING(String paramString)
/*      */     {
/* 1308 */       super("java.lang.String");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class URL extends DocFlavor
/*      */   {
/* 1075 */     public static final URL TEXT_PLAIN_HOST = new URL("text/plain; charset=" + hostEncoding);
/*      */ 
/* 1084 */     public static final URL TEXT_PLAIN_UTF_8 = new URL("text/plain; charset=utf-8");
/*      */ 
/* 1093 */     public static final URL TEXT_PLAIN_UTF_16 = new URL("text/plain; charset=utf-16");
/*      */ 
/* 1103 */     public static final URL TEXT_PLAIN_UTF_16BE = new URL("text/plain; charset=utf-16be");
/*      */ 
/* 1113 */     public static final URL TEXT_PLAIN_UTF_16LE = new URL("text/plain; charset=utf-16le");
/*      */ 
/* 1122 */     public static final URL TEXT_PLAIN_US_ASCII = new URL("text/plain; charset=us-ascii");
/*      */ 
/* 1132 */     public static final URL TEXT_HTML_HOST = new URL("text/html; charset=" + hostEncoding);
/*      */ 
/* 1141 */     public static final URL TEXT_HTML_UTF_8 = new URL("text/html; charset=utf-8");
/*      */ 
/* 1150 */     public static final URL TEXT_HTML_UTF_16 = new URL("text/html; charset=utf-16");
/*      */ 
/* 1160 */     public static final URL TEXT_HTML_UTF_16BE = new URL("text/html; charset=utf-16be");
/*      */ 
/* 1170 */     public static final URL TEXT_HTML_UTF_16LE = new URL("text/html; charset=utf-16le");
/*      */ 
/* 1179 */     public static final URL TEXT_HTML_US_ASCII = new URL("text/html; charset=us-ascii");
/*      */ 
/* 1187 */     public static final URL PDF = new URL("application/pdf");
/*      */ 
/* 1193 */     public static final URL POSTSCRIPT = new URL("application/postscript");
/*      */ 
/* 1199 */     public static final URL PCL = new URL("application/vnd.hp-PCL");
/*      */ 
/* 1205 */     public static final URL GIF = new URL("image/gif");
/*      */ 
/* 1211 */     public static final URL JPEG = new URL("image/jpeg");
/*      */ 
/* 1217 */     public static final URL PNG = new URL("image/png");
/*      */ 
/* 1226 */     public static final URL AUTOSENSE = new URL("application/octet-stream");
/*      */ 
/*      */     public URL(String paramString)
/*      */     {
/* 1065 */       super("java.net.URL");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.DocFlavor
 * JD-Core Version:    0.6.2
 */