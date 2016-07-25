/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ public class ImageCache
/*     */ {
/*     */   private int maxCount;
/*     */   private final LinkedList<SoftReference<Entry>> entries;
/*     */ 
/*     */   public ImageCache(int paramInt)
/*     */   {
/*  43 */     this.maxCount = paramInt;
/*  44 */     this.entries = new LinkedList();
/*     */   }
/*     */ 
/*     */   void setMaxCount(int paramInt) {
/*  48 */     this.maxCount = paramInt;
/*     */   }
/*     */ 
/*     */   public void flush() {
/*  52 */     this.entries.clear();
/*     */   }
/*     */ 
/*     */   private Entry getEntry(Object paramObject, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  58 */     ListIterator localListIterator = this.entries.listIterator();
/*  59 */     while (localListIterator.hasNext()) {
/*  60 */       SoftReference localSoftReference = (SoftReference)localListIterator.next();
/*  61 */       localEntry = (Entry)localSoftReference.get();
/*  62 */       if (localEntry == null)
/*     */       {
/*  64 */         localListIterator.remove();
/*     */       }
/*  66 */       else if (localEntry.equals(paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject))
/*     */       {
/*  68 */         localListIterator.remove();
/*  69 */         this.entries.addFirst(localSoftReference);
/*  70 */         return localEntry;
/*     */       }
/*     */     }
/*     */ 
/*  74 */     Entry localEntry = new Entry(paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject);
/*  75 */     if (this.entries.size() >= this.maxCount) {
/*  76 */       this.entries.removeLast();
/*     */     }
/*  78 */     this.entries.addFirst(new SoftReference(localEntry));
/*  79 */     return localEntry;
/*     */   }
/*     */ 
/*     */   public Image getImage(Object paramObject, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  87 */     Entry localEntry = getEntry(paramObject, paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject);
/*  88 */     return localEntry.getImage();
/*     */   }
/*     */ 
/*     */   public void setImage(Object paramObject, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject, Image paramImage)
/*     */   {
/*  96 */     Entry localEntry = getEntry(paramObject, paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject);
/*  97 */     localEntry.setImage(paramImage);
/*     */   }
/*     */ 
/*     */   private static class Entry {
/*     */     private final GraphicsConfiguration config;
/*     */     private final int w;
/*     */     private final int h;
/*     */     private final Object[] args;
/*     */     private Image image;
/*     */ 
/*     */     Entry(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject) {
/* 112 */       this.config = paramGraphicsConfiguration;
/* 113 */       this.args = paramArrayOfObject;
/* 114 */       this.w = paramInt1;
/* 115 */       this.h = paramInt2;
/*     */     }
/*     */ 
/*     */     public void setImage(Image paramImage) {
/* 119 */       this.image = paramImage;
/*     */     }
/*     */ 
/*     */     public Image getImage() {
/* 123 */       return this.image;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 127 */       String str = super.toString() + "[ graphicsConfig=" + this.config + ", image=" + this.image + ", w=" + this.w + ", h=" + this.h;
/*     */ 
/* 131 */       if (this.args != null) {
/* 132 */         for (int i = 0; i < this.args.length; i++) {
/* 133 */           str = str + ", " + this.args[i];
/*     */         }
/*     */       }
/* 136 */       str = str + "]";
/* 137 */       return str;
/*     */     }
/*     */ 
/*     */     public boolean equals(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */     {
/* 142 */       if ((this.w == paramInt1) && (this.h == paramInt2) && (((this.config != null) && (this.config.equals(paramGraphicsConfiguration))) || ((this.config == null) && (paramGraphicsConfiguration == null))))
/*     */       {
/* 145 */         if ((this.args == null) && (paramArrayOfObject == null)) {
/* 146 */           return true;
/*     */         }
/* 148 */         if ((this.args != null) && (paramArrayOfObject != null) && (this.args.length == paramArrayOfObject.length))
/*     */         {
/* 150 */           for (int i = paramArrayOfObject.length - 1; i >= 0; 
/* 151 */             i--) {
/* 152 */             Object localObject1 = this.args[i];
/* 153 */             Object localObject2 = paramArrayOfObject[i];
/* 154 */             if (((localObject1 == null) && (localObject2 != null)) || ((localObject1 != null) && (!localObject1.equals(localObject2))))
/*     */             {
/* 156 */               return false;
/*     */             }
/*     */           }
/* 159 */           return true;
/*     */         }
/*     */       }
/* 162 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.ImageCache
 * JD-Core Version:    0.6.2
 */