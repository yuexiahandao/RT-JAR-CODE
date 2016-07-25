/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import com.sun.imageio.plugins.bmp.BMPImageReaderSpi;
/*     */ import com.sun.imageio.plugins.bmp.BMPImageWriterSpi;
/*     */ import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
/*     */ import com.sun.imageio.plugins.gif.GIFImageWriterSpi;
/*     */ import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
/*     */ import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
/*     */ import com.sun.imageio.plugins.png.PNGImageReaderSpi;
/*     */ import com.sun.imageio.plugins.png.PNGImageWriterSpi;
/*     */ import com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi;
/*     */ import com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi;
/*     */ import com.sun.imageio.spi.FileImageInputStreamSpi;
/*     */ import com.sun.imageio.spi.FileImageOutputStreamSpi;
/*     */ import com.sun.imageio.spi.InputStreamImageInputStreamSpi;
/*     */ import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;
/*     */ import com.sun.imageio.spi.RAFImageInputStreamSpi;
/*     */ import com.sun.imageio.spi.RAFImageOutputStreamSpi;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public final class IIORegistry extends ServiceRegistry
/*     */ {
/* 118 */   private static final Vector initialCategories = new Vector(5);
/*     */ 
/*     */   private IIORegistry()
/*     */   {
/* 136 */     super(initialCategories.iterator());
/* 137 */     registerStandardSpis();
/* 138 */     registerApplicationClasspathSpis();
/*     */   }
/*     */ 
/*     */   public static IIORegistry getDefaultInstance()
/*     */   {
/* 154 */     AppContext localAppContext = AppContext.getAppContext();
/* 155 */     IIORegistry localIIORegistry = (IIORegistry)localAppContext.get(IIORegistry.class);
/*     */ 
/* 157 */     if (localIIORegistry == null)
/*     */     {
/* 159 */       localIIORegistry = new IIORegistry();
/* 160 */       localAppContext.put(IIORegistry.class, localIIORegistry);
/*     */     }
/* 162 */     return localIIORegistry;
/*     */   }
/*     */ 
/*     */   private void registerStandardSpis()
/*     */   {
/* 167 */     registerServiceProvider(new GIFImageReaderSpi());
/* 168 */     registerServiceProvider(new GIFImageWriterSpi());
/* 169 */     registerServiceProvider(new BMPImageReaderSpi());
/* 170 */     registerServiceProvider(new BMPImageWriterSpi());
/* 171 */     registerServiceProvider(new WBMPImageReaderSpi());
/* 172 */     registerServiceProvider(new WBMPImageWriterSpi());
/* 173 */     registerServiceProvider(new PNGImageReaderSpi());
/* 174 */     registerServiceProvider(new PNGImageWriterSpi());
/* 175 */     registerServiceProvider(new JPEGImageReaderSpi());
/* 176 */     registerServiceProvider(new JPEGImageWriterSpi());
/* 177 */     registerServiceProvider(new FileImageInputStreamSpi());
/* 178 */     registerServiceProvider(new FileImageOutputStreamSpi());
/* 179 */     registerServiceProvider(new InputStreamImageInputStreamSpi());
/* 180 */     registerServiceProvider(new OutputStreamImageOutputStreamSpi());
/* 181 */     registerServiceProvider(new RAFImageInputStreamSpi());
/* 182 */     registerServiceProvider(new RAFImageOutputStreamSpi());
/*     */ 
/* 184 */     registerInstalledProviders();
/*     */   }
/*     */ 
/*     */   public void registerApplicationClasspathSpis()
/*     */   {
/* 199 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 201 */     Iterator localIterator1 = getCategories();
/* 202 */     while (localIterator1.hasNext()) {
/* 203 */       Class localClass = (Class)localIterator1.next();
/* 204 */       Iterator localIterator2 = ServiceLoader.load(localClass, localClassLoader).iterator();
/*     */ 
/* 206 */       while (localIterator2.hasNext())
/*     */       {
/*     */         try
/*     */         {
/* 210 */           IIOServiceProvider localIIOServiceProvider = (IIOServiceProvider)localIterator2.next();
/* 211 */           registerServiceProvider(localIIOServiceProvider);
/*     */         } catch (ServiceConfigurationError localServiceConfigurationError) {
/* 213 */           if (System.getSecurityManager() != null)
/*     */           {
/* 216 */             localServiceConfigurationError.printStackTrace();
/*     */           }
/*     */           else
/*     */           {
/* 220 */             throw localServiceConfigurationError;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void registerInstalledProviders()
/*     */   {
/* 235 */     PrivilegedAction local1 = new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 238 */         Iterator localIterator1 = IIORegistry.this.getCategories();
/* 239 */         while (localIterator1.hasNext()) {
/* 240 */           Class localClass = (Class)localIterator1.next();
/* 241 */           for (IIOServiceProvider localIIOServiceProvider : ServiceLoader.loadInstalled(localClass)) {
/* 242 */             IIORegistry.this.registerServiceProvider(localIIOServiceProvider);
/*     */           }
/*     */         }
/* 245 */         return this;
/*     */       }
/*     */     };
/* 249 */     AccessController.doPrivileged(local1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 121 */     initialCategories.add(ImageReaderSpi.class);
/* 122 */     initialCategories.add(ImageWriterSpi.class);
/* 123 */     initialCategories.add(ImageTranscoderSpi.class);
/* 124 */     initialCategories.add(ImageInputStreamSpi.class);
/* 125 */     initialCategories.add(ImageOutputStreamSpi.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.IIORegistry
 * JD-Core Version:    0.6.2
 */