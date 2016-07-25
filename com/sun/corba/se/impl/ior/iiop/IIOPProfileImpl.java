/*     */ package com.sun.corba.se.impl.ior.iiop;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.impl.ior.EncapsulationUtility;
/*     */ import com.sun.corba.se.impl.logging.IORSystemException;
/*     */ import com.sun.corba.se.impl.util.JDKBridge;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableBase;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ import org.omg.IOP.TaggedProfileHelper;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class IIOPProfileImpl extends IdentifiableBase
/*     */   implements IIOPProfile
/*     */ {
/*     */   private ORB orb;
/*     */   private IORSystemException wrapper;
/*     */   private ObjectId oid;
/*     */   private IIOPProfileTemplate proftemp;
/*     */   private ObjectKeyTemplate oktemp;
/*  91 */   protected String codebase = null;
/*  92 */   protected boolean cachedCodebase = false;
/*     */ 
/*  94 */   private boolean checkedIsLocal = false;
/*  95 */   private boolean cachedIsLocal = false;
/*     */ 
/* 111 */   private GIOPVersion giopVersion = null;
/*     */ 
/*     */   public boolean equals(java.lang.Object paramObject)
/*     */   {
/* 115 */     if (!(paramObject instanceof IIOPProfileImpl)) {
/* 116 */       return false;
/*     */     }
/* 118 */     IIOPProfileImpl localIIOPProfileImpl = (IIOPProfileImpl)paramObject;
/*     */ 
/* 120 */     return (this.oid.equals(localIIOPProfileImpl.oid)) && (this.proftemp.equals(localIIOPProfileImpl.proftemp)) && (this.oktemp.equals(localIIOPProfileImpl.oktemp));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 126 */     return this.oid.hashCode() ^ this.proftemp.hashCode() ^ this.oktemp.hashCode();
/*     */   }
/*     */ 
/*     */   public ObjectId getObjectId()
/*     */   {
/* 131 */     return this.oid;
/*     */   }
/*     */ 
/*     */   public TaggedProfileTemplate getTaggedProfileTemplate()
/*     */   {
/* 136 */     return this.proftemp;
/*     */   }
/*     */ 
/*     */   public ObjectKeyTemplate getObjectKeyTemplate()
/*     */   {
/* 141 */     return this.oktemp;
/*     */   }
/*     */ 
/*     */   private IIOPProfileImpl(ORB paramORB)
/*     */   {
/* 146 */     this.orb = paramORB;
/* 147 */     this.wrapper = IORSystemException.get(paramORB, "oa.ior");
/*     */   }
/*     */ 
/*     */   public IIOPProfileImpl(ORB paramORB, ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId, IIOPProfileTemplate paramIIOPProfileTemplate)
/*     */   {
/* 154 */     this(paramORB);
/* 155 */     this.oktemp = paramObjectKeyTemplate;
/* 156 */     this.oid = paramObjectId;
/* 157 */     this.proftemp = paramIIOPProfileTemplate;
/*     */   }
/*     */ 
/*     */   public IIOPProfileImpl(InputStream paramInputStream)
/*     */   {
/* 162 */     this((ORB)paramInputStream.orb());
/* 163 */     init(paramInputStream);
/*     */   }
/*     */ 
/*     */   public IIOPProfileImpl(ORB paramORB, org.omg.IOP.TaggedProfile paramTaggedProfile)
/*     */   {
/* 168 */     this(paramORB);
/*     */ 
/* 170 */     if ((paramTaggedProfile == null) || (paramTaggedProfile.tag != 0) || (paramTaggedProfile.profile_data == null))
/*     */     {
/* 172 */       throw this.wrapper.invalidTaggedProfile();
/*     */     }
/*     */ 
/* 175 */     EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(paramORB, paramTaggedProfile.profile_data, paramTaggedProfile.profile_data.length);
/*     */ 
/* 177 */     localEncapsInputStream.consumeEndian();
/* 178 */     init(localEncapsInputStream);
/*     */   }
/*     */ 
/*     */   private void init(InputStream paramInputStream)
/*     */   {
/* 184 */     GIOPVersion localGIOPVersion = new GIOPVersion();
/* 185 */     localGIOPVersion.read(paramInputStream);
/* 186 */     IIOPAddressImpl localIIOPAddressImpl = new IIOPAddressImpl(paramInputStream);
/* 187 */     byte[] arrayOfByte = EncapsulationUtility.readOctets(paramInputStream);
/*     */ 
/* 189 */     ObjectKey localObjectKey = this.orb.getObjectKeyFactory().create(arrayOfByte);
/* 190 */     this.oktemp = localObjectKey.getTemplate();
/* 191 */     this.oid = localObjectKey.getId();
/*     */ 
/* 193 */     this.proftemp = IIOPFactories.makeIIOPProfileTemplate(this.orb, localGIOPVersion, localIIOPAddressImpl);
/*     */ 
/* 197 */     if (localGIOPVersion.getMinor() > 0) {
/* 198 */       EncapsulationUtility.readIdentifiableSequence(this.proftemp, this.orb.getTaggedComponentFactoryFinder(), paramInputStream);
/*     */     }
/*     */ 
/* 206 */     if (uncachedGetCodeBase() == null) {
/* 207 */       JavaCodebaseComponent localJavaCodebaseComponent = LocalCodeBaseSingletonHolder.comp;
/*     */ 
/* 209 */       if (localJavaCodebaseComponent != null) {
/* 210 */         if (localGIOPVersion.getMinor() > 0) {
/* 211 */           this.proftemp.add(localJavaCodebaseComponent);
/*     */         }
/* 213 */         this.codebase = localJavaCodebaseComponent.getURLs();
/*     */       }
/*     */ 
/* 218 */       this.cachedCodebase = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeContents(OutputStream paramOutputStream)
/*     */   {
/* 224 */     this.proftemp.write(this.oktemp, this.oid, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 229 */     return this.proftemp.getId();
/*     */   }
/*     */ 
/*     */   public boolean isEquivalent(com.sun.corba.se.spi.ior.TaggedProfile paramTaggedProfile)
/*     */   {
/* 234 */     if (!(paramTaggedProfile instanceof IIOPProfile)) {
/* 235 */       return false;
/*     */     }
/* 237 */     IIOPProfile localIIOPProfile = (IIOPProfile)paramTaggedProfile;
/*     */ 
/* 239 */     return (this.oid.equals(localIIOPProfile.getObjectId())) && (this.proftemp.isEquivalent(localIIOPProfile.getTaggedProfileTemplate())) && (this.oktemp.equals(localIIOPProfile.getObjectKeyTemplate()));
/*     */   }
/*     */ 
/*     */   public ObjectKey getObjectKey()
/*     */   {
/* 246 */     ObjectKey localObjectKey = IORFactories.makeObjectKey(this.oktemp, this.oid);
/* 247 */     return localObjectKey;
/*     */   }
/*     */ 
/*     */   public org.omg.IOP.TaggedProfile getIOPProfile()
/*     */   {
/* 252 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.orb);
/*     */ 
/* 254 */     localEncapsOutputStream.write_long(getId());
/* 255 */     write(localEncapsOutputStream);
/* 256 */     InputStream localInputStream = (InputStream)localEncapsOutputStream.create_input_stream();
/* 257 */     return TaggedProfileHelper.read(localInputStream);
/*     */   }
/*     */ 
/*     */   private String uncachedGetCodeBase() {
/* 261 */     Iterator localIterator = this.proftemp.iteratorById(25);
/*     */ 
/* 263 */     if (localIterator.hasNext()) {
/* 264 */       JavaCodebaseComponent localJavaCodebaseComponent = (JavaCodebaseComponent)localIterator.next();
/* 265 */       return localJavaCodebaseComponent.getURLs();
/*     */     }
/*     */ 
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized String getCodebase() {
/* 272 */     if (!this.cachedCodebase) {
/* 273 */       this.cachedCodebase = true;
/* 274 */       this.codebase = uncachedGetCodeBase();
/*     */     }
/*     */ 
/* 277 */     return this.codebase;
/*     */   }
/*     */ 
/*     */   public ORBVersion getORBVersion()
/*     */   {
/* 284 */     return this.oktemp.getORBVersion();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isLocal()
/*     */   {
/* 289 */     if (!this.checkedIsLocal) {
/* 290 */       this.checkedIsLocal = true;
/* 291 */       String str = this.proftemp.getPrimaryAddress().getHost();
/*     */ 
/* 293 */       this.cachedIsLocal = ((this.orb.isLocalHost(str)) && (this.orb.isLocalServerId(this.oktemp.getSubcontractId(), this.oktemp.getServerId())) && (this.orb.getLegacyServerSocketManager().legacyIsLocalServerPort(this.proftemp.getPrimaryAddress().getPort())));
/*     */     }
/*     */ 
/* 301 */     return this.cachedIsLocal;
/*     */   }
/*     */ 
/*     */   public java.lang.Object getServant()
/*     */   {
/* 312 */     if (!isLocal()) {
/* 313 */       return null;
/*     */     }
/* 315 */     RequestDispatcherRegistry localRequestDispatcherRegistry = this.orb.getRequestDispatcherRegistry();
/* 316 */     ObjectAdapterFactory localObjectAdapterFactory = localRequestDispatcherRegistry.getObjectAdapterFactory(this.oktemp.getSubcontractId());
/*     */ 
/* 319 */     ObjectAdapterId localObjectAdapterId = this.oktemp.getObjectAdapterId();
/* 320 */     ObjectAdapter localObjectAdapter = null;
/*     */     try
/*     */     {
/* 323 */       localObjectAdapter = localObjectAdapterFactory.find(localObjectAdapterId);
/*     */     }
/*     */     catch (SystemException localSystemException)
/*     */     {
/* 328 */       this.wrapper.getLocalServantFailure(localSystemException, localObjectAdapterId.toString());
/* 329 */       return null;
/*     */     }
/*     */ 
/* 332 */     byte[] arrayOfByte = this.oid.getId();
/* 333 */     org.omg.CORBA.Object localObject = localObjectAdapter.getLocalServant(arrayOfByte);
/* 334 */     return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized GIOPVersion getGIOPVersion()
/*     */   {
/* 344 */     return this.proftemp.getGIOPVersion();
/*     */   }
/*     */ 
/*     */   public void makeImmutable()
/*     */   {
/* 349 */     this.proftemp.makeImmutable();
/*     */   }
/*     */ 
/*     */   private static class LocalCodeBaseSingletonHolder
/*     */   {
/*     */     public static JavaCodebaseComponent comp;
/*     */ 
/*     */     static
/*     */     {
/* 102 */       String str = JDKBridge.getLocalCodebase();
/* 103 */       if (str == null)
/* 104 */         comp = null;
/*     */       else
/* 106 */         comp = IIOPFactories.makeJavaCodebaseComponent(str);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.IIOPProfileImpl
 * JD-Core Version:    0.6.2
 */