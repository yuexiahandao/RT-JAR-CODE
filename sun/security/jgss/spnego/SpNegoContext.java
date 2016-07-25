/*      */ package sun.security.jgss.spnego;
/*      */ 
/*      */ import com.sun.security.jgss.ExtendedGSSContext;
/*      */ import com.sun.security.jgss.InquireType;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.Provider;
/*      */ import org.ietf.jgss.ChannelBinding;
/*      */ import org.ietf.jgss.GSSContext;
/*      */ import org.ietf.jgss.GSSCredential;
/*      */ import org.ietf.jgss.GSSException;
/*      */ import org.ietf.jgss.GSSName;
/*      */ import org.ietf.jgss.MessageProp;
/*      */ import org.ietf.jgss.Oid;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.jgss.GSSCredentialImpl;
/*      */ import sun.security.jgss.GSSManagerImpl;
/*      */ import sun.security.jgss.GSSNameImpl;
/*      */ import sun.security.jgss.GSSUtil;
/*      */ import sun.security.jgss.spi.GSSContextSpi;
/*      */ import sun.security.jgss.spi.GSSCredentialSpi;
/*      */ import sun.security.jgss.spi.GSSNameSpi;
/*      */ import sun.security.util.BitArray;
/*      */ import sun.security.util.DerOutputStream;
/*      */ 
/*      */ public class SpNegoContext
/*      */   implements GSSContextSpi
/*      */ {
/*      */   private static final int STATE_NEW = 1;
/*      */   private static final int STATE_IN_PROCESS = 2;
/*      */   private static final int STATE_DONE = 3;
/*      */   private static final int STATE_DELETED = 4;
/*   54 */   private int state = 1;
/*      */ 
/*   60 */   private boolean credDelegState = false;
/*   61 */   private boolean mutualAuthState = true;
/*   62 */   private boolean replayDetState = true;
/*   63 */   private boolean sequenceDetState = true;
/*   64 */   private boolean confState = true;
/*   65 */   private boolean integState = true;
/*   66 */   private boolean delegPolicyState = false;
/*      */ 
/*   68 */   private GSSNameSpi peerName = null;
/*   69 */   private GSSNameSpi myName = null;
/*   70 */   private SpNegoCredElement myCred = null;
/*      */ 
/*   72 */   private GSSContext mechContext = null;
/*   73 */   private byte[] DER_mechTypes = null;
/*      */   private int lifetime;
/*      */   private ChannelBinding channelBinding;
/*      */   private boolean initiator;
/*   80 */   private Oid internal_mech = null;
/*      */   private final SpNegoMechFactory factory;
/*   86 */   static final boolean DEBUG = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.spnego.debug"))).booleanValue();
/*      */ 
/*      */   public SpNegoContext(SpNegoMechFactory paramSpNegoMechFactory, GSSNameSpi paramGSSNameSpi, GSSCredentialSpi paramGSSCredentialSpi, int paramInt)
/*      */     throws GSSException
/*      */   {
/*   99 */     if (paramGSSNameSpi == null)
/*  100 */       throw new IllegalArgumentException("Cannot have null peer name");
/*  101 */     if ((paramGSSCredentialSpi != null) && (!(paramGSSCredentialSpi instanceof SpNegoCredElement))) {
/*  102 */       throw new IllegalArgumentException("Wrong cred element type");
/*      */     }
/*  104 */     this.peerName = paramGSSNameSpi;
/*  105 */     this.myCred = ((SpNegoCredElement)paramGSSCredentialSpi);
/*  106 */     this.lifetime = paramInt;
/*  107 */     this.initiator = true;
/*  108 */     this.factory = paramSpNegoMechFactory;
/*      */   }
/*      */ 
/*      */   public SpNegoContext(SpNegoMechFactory paramSpNegoMechFactory, GSSCredentialSpi paramGSSCredentialSpi)
/*      */     throws GSSException
/*      */   {
/*  117 */     if ((paramGSSCredentialSpi != null) && (!(paramGSSCredentialSpi instanceof SpNegoCredElement))) {
/*  118 */       throw new IllegalArgumentException("Wrong cred element type");
/*      */     }
/*  120 */     this.myCred = ((SpNegoCredElement)paramGSSCredentialSpi);
/*  121 */     this.initiator = false;
/*  122 */     this.factory = paramSpNegoMechFactory;
/*      */   }
/*      */ 
/*      */   public SpNegoContext(SpNegoMechFactory paramSpNegoMechFactory, byte[] paramArrayOfByte)
/*      */     throws GSSException
/*      */   {
/*  130 */     throw new GSSException(16, -1, "GSS Import Context not available");
/*      */   }
/*      */ 
/*      */   public final void requestConf(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  138 */     if ((this.state == 1) && (isInitiator()))
/*  139 */       this.confState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getConfState()
/*      */   {
/*  146 */     return this.confState;
/*      */   }
/*      */ 
/*      */   public final void requestInteg(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  153 */     if ((this.state == 1) && (isInitiator()))
/*  154 */       this.integState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final void requestDelegPolicy(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  161 */     if ((this.state == 1) && (isInitiator()))
/*  162 */       this.delegPolicyState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getIntegState()
/*      */   {
/*  169 */     return this.integState;
/*      */   }
/*      */ 
/*      */   public final boolean getDelegPolicyState()
/*      */   {
/*  176 */     if ((isInitiator()) && (this.mechContext != null) && ((this.mechContext instanceof ExtendedGSSContext)) && ((this.state == 2) || (this.state == 3)))
/*      */     {
/*  179 */       return ((ExtendedGSSContext)this.mechContext).getDelegPolicyState();
/*      */     }
/*  181 */     return this.delegPolicyState;
/*      */   }
/*      */ 
/*      */   public final void requestCredDeleg(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  190 */     if ((this.state == 1) && (isInitiator()))
/*  191 */       this.credDelegState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getCredDelegState()
/*      */   {
/*  198 */     if ((isInitiator()) && (this.mechContext != null) && ((this.state == 2) || (this.state == 3)))
/*      */     {
/*  200 */       return this.mechContext.getCredDelegState();
/*      */     }
/*  202 */     return this.credDelegState;
/*      */   }
/*      */ 
/*      */   public final void requestMutualAuth(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  212 */     if ((this.state == 1) && (isInitiator()))
/*  213 */       this.mutualAuthState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getMutualAuthState()
/*      */   {
/*  223 */     return this.mutualAuthState;
/*      */   }
/*      */ 
/*      */   public final Oid getMech()
/*      */   {
/*  232 */     if (isEstablished()) {
/*  233 */       return getNegotiatedMech();
/*      */     }
/*  235 */     return SpNegoMechFactory.GSS_SPNEGO_MECH_OID;
/*      */   }
/*      */ 
/*      */   public final Oid getNegotiatedMech() {
/*  239 */     return this.internal_mech;
/*      */   }
/*      */ 
/*      */   public final Provider getProvider() {
/*  243 */     return SpNegoMechFactory.PROVIDER;
/*      */   }
/*      */ 
/*      */   public final void dispose() throws GSSException {
/*  247 */     this.mechContext = null;
/*  248 */     this.state = 4;
/*      */   }
/*      */ 
/*      */   public final boolean isInitiator()
/*      */   {
/*  258 */     return this.initiator;
/*      */   }
/*      */ 
/*      */   public final boolean isProtReady()
/*      */   {
/*  270 */     return this.state == 3;
/*      */   }
/*      */ 
/*      */   public final byte[] initSecContext(InputStream paramInputStream, int paramInt)
/*      */     throws GSSException
/*      */   {
/*  289 */     Object localObject1 = null;
/*  290 */     NegTokenInit localNegTokenInit = null;
/*  291 */     byte[] arrayOfByte1 = null;
/*  292 */     int i = 11;
/*      */ 
/*  294 */     if (DEBUG) {
/*  295 */       System.out.println("Entered SpNego.initSecContext with state=" + printState(this.state));
/*      */     }
/*      */ 
/*  298 */     if (!isInitiator())
/*  299 */       throw new GSSException(11, -1, "initSecContext on an acceptor GSSContext");
/*      */     try
/*      */     {
/*      */       Object localObject2;
/*  304 */       if (this.state == 1) {
/*  305 */         this.state = 2;
/*      */ 
/*  307 */         i = 13;
/*      */ 
/*  310 */         localObject2 = getAvailableMechs();
/*  311 */         this.DER_mechTypes = getEncodedMechs((Oid[])localObject2);
/*      */ 
/*  314 */         this.internal_mech = localObject2[0];
/*      */ 
/*  317 */         arrayOfByte1 = GSS_initSecContext(null);
/*      */ 
/*  319 */         i = 10;
/*      */ 
/*  321 */         localNegTokenInit = new NegTokenInit(this.DER_mechTypes, getContextFlags(), arrayOfByte1, null);
/*      */ 
/*  323 */         if (DEBUG) {
/*  324 */           System.out.println("SpNegoContext.initSecContext: sending token of type = " + SpNegoToken.getTokenName(localNegTokenInit.getType()));
/*      */         }
/*      */ 
/*  329 */         localObject1 = localNegTokenInit.getEncoded();
/*      */       }
/*  331 */       else if (this.state == 2)
/*      */       {
/*  333 */         i = 11;
/*  334 */         if (paramInputStream == null) {
/*  335 */           throw new GSSException(i, -1, "No token received from peer!");
/*      */         }
/*      */ 
/*  339 */         i = 10;
/*  340 */         localObject2 = new byte[paramInputStream.available()];
/*  341 */         SpNegoToken.readFully(paramInputStream, (byte[])localObject2);
/*  342 */         if (DEBUG) {
/*  343 */           System.out.println("SpNegoContext.initSecContext: process received token = " + SpNegoToken.getHexBytes((byte[])localObject2));
/*      */         }
/*      */ 
/*  350 */         localObject3 = new NegTokenTarg((byte[])localObject2);
/*      */ 
/*  352 */         if (DEBUG) {
/*  353 */           System.out.println("SpNegoContext.initSecContext: received token of type = " + SpNegoToken.getTokenName(((NegTokenTarg)localObject3).getType()));
/*      */         }
/*      */ 
/*  359 */         this.internal_mech = ((NegTokenTarg)localObject3).getSupportedMech();
/*  360 */         if (this.internal_mech == null)
/*      */         {
/*  362 */           throw new GSSException(i, -1, "supported mechansim from server is null");
/*      */         }
/*      */ 
/*  367 */         SpNegoToken.NegoResult localNegoResult = null;
/*  368 */         int j = ((NegTokenTarg)localObject3).getNegotiatedResult();
/*  369 */         switch (j) {
/*      */         case 0:
/*  371 */           localNegoResult = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
/*  372 */           this.state = 3;
/*  373 */           break;
/*      */         case 1:
/*  375 */           localNegoResult = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
/*  376 */           this.state = 2;
/*  377 */           break;
/*      */         case 2:
/*  379 */           localNegoResult = SpNegoToken.NegoResult.REJECT;
/*  380 */           this.state = 4;
/*  381 */           break;
/*      */         default:
/*  383 */           this.state = 3;
/*      */         }
/*      */ 
/*  387 */         i = 2;
/*      */ 
/*  389 */         if (localNegoResult == SpNegoToken.NegoResult.REJECT) {
/*  390 */           throw new GSSException(i, -1, this.internal_mech.toString());
/*      */         }
/*      */ 
/*  394 */         i = 10;
/*      */ 
/*  396 */         if ((localNegoResult == SpNegoToken.NegoResult.ACCEPT_COMPLETE) || (localNegoResult == SpNegoToken.NegoResult.ACCEPT_INCOMPLETE))
/*      */         {
/*  400 */           byte[] arrayOfByte2 = ((NegTokenTarg)localObject3).getResponseToken();
/*  401 */           if (arrayOfByte2 == null) {
/*  402 */             if (!isMechContextEstablished())
/*      */             {
/*  404 */               throw new GSSException(i, -1, "mechanism token from server is null");
/*      */             }
/*      */           }
/*      */           else {
/*  408 */             arrayOfByte1 = GSS_initSecContext(arrayOfByte2);
/*      */           }
/*      */ 
/*  411 */           if (!GSSUtil.useMSInterop()) {
/*  412 */             byte[] arrayOfByte3 = ((NegTokenTarg)localObject3).getMechListMIC();
/*  413 */             if (!verifyMechListMIC(this.DER_mechTypes, arrayOfByte3)) {
/*  414 */               throw new GSSException(i, -1, "verification of MIC on MechList Failed!");
/*      */             }
/*      */           }
/*      */ 
/*  418 */           if (isMechContextEstablished()) {
/*  419 */             this.state = 3;
/*  420 */             localObject1 = arrayOfByte1;
/*  421 */             if (DEBUG) {
/*  422 */               System.out.println("SPNEGO Negotiated Mechanism = " + this.internal_mech + " " + GSSUtil.getMechStr(this.internal_mech));
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  428 */             localNegTokenInit = new NegTokenInit(null, null, arrayOfByte1, null);
/*      */ 
/*  430 */             if (DEBUG) {
/*  431 */               System.out.println("SpNegoContext.initSecContext: continue sending token of type = " + SpNegoToken.getTokenName(localNegTokenInit.getType()));
/*      */             }
/*      */ 
/*  436 */             localObject1 = localNegTokenInit.getEncoded();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  442 */       else if (DEBUG) {
/*  443 */         System.out.println(this.state);
/*      */       }
/*      */ 
/*  446 */       if ((DEBUG) && 
/*  447 */         (localObject1 != null)) {
/*  448 */         System.out.println("SNegoContext.initSecContext: sending token = " + SpNegoToken.getHexBytes((byte[])localObject1));
/*      */       }
/*      */     }
/*      */     catch (GSSException localGSSException)
/*      */     {
/*  453 */       localObject3 = new GSSException(i, -1, localGSSException.getMessage());
/*      */ 
/*  455 */       ((GSSException)localObject3).initCause(localGSSException);
/*  456 */       throw ((Throwable)localObject3);
/*      */     } catch (IOException localIOException) {
/*  458 */       Object localObject3 = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  460 */       ((GSSException)localObject3).initCause(localIOException);
/*  461 */       throw ((Throwable)localObject3);
/*      */     }
/*      */ 
/*  464 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public final byte[] acceptSecContext(InputStream paramInputStream, int paramInt)
/*      */     throws GSSException
/*      */   {
/*  483 */     byte[] arrayOfByte1 = null;
/*      */ 
/*  485 */     boolean bool = true;
/*      */ 
/*  487 */     if (DEBUG) {
/*  488 */       System.out.println("Entered SpNegoContext.acceptSecContext with state=" + printState(this.state));
/*      */     }
/*      */ 
/*  492 */     if (isInitiator())
/*  493 */       throw new GSSException(11, -1, "acceptSecContext on an initiator GSSContext");
/*      */     try
/*      */     {
/*      */       byte[] arrayOfByte2;
/*      */       Object localObject2;
/*      */       SpNegoToken.NegoResult localNegoResult;
/*  498 */       if (this.state == 1) {
/*  499 */         this.state = 2;
/*      */ 
/*  502 */         arrayOfByte2 = new byte[paramInputStream.available()];
/*  503 */         SpNegoToken.readFully(paramInputStream, arrayOfByte2);
/*  504 */         if (DEBUG) {
/*  505 */           System.out.println("SpNegoContext.acceptSecContext: receiving token = " + SpNegoToken.getHexBytes(arrayOfByte2));
/*      */         }
/*      */ 
/*  512 */         localObject1 = new NegTokenInit(arrayOfByte2);
/*      */ 
/*  514 */         if (DEBUG) {
/*  515 */           System.out.println("SpNegoContext.acceptSecContext: received token of type = " + SpNegoToken.getTokenName(((NegTokenInit)localObject1).getType()));
/*      */         }
/*      */ 
/*  520 */         localObject2 = ((NegTokenInit)localObject1).getMechTypeList();
/*  521 */         this.DER_mechTypes = ((NegTokenInit)localObject1).getMechTypes();
/*  522 */         if (this.DER_mechTypes == null) {
/*  523 */           bool = false;
/*      */         }
/*      */ 
/*  527 */         byte[] arrayOfByte3 = ((NegTokenInit)localObject1).getMechToken();
/*  528 */         if (arrayOfByte3 == null) {
/*  529 */           throw new GSSException(11, -1, "mechToken is missing");
/*      */         }
/*      */ 
/*  538 */         Oid[] arrayOfOid = getAvailableMechs();
/*  539 */         Oid localOid = negotiate_mech_type(arrayOfOid, (Oid[])localObject2);
/*      */ 
/*  541 */         if (localOid == null) {
/*  542 */           bool = false;
/*      */         }
/*      */ 
/*  545 */         this.internal_mech = localOid;
/*      */ 
/*  548 */         byte[] arrayOfByte4 = GSS_acceptSecContext(arrayOfByte3);
/*      */ 
/*  551 */         if ((!GSSUtil.useMSInterop()) && (bool)) {
/*  552 */           bool = verifyMechListMIC(this.DER_mechTypes, ((NegTokenInit)localObject1).getMechListMIC());
/*      */         }
/*      */ 
/*  557 */         if (bool) {
/*  558 */           if (isMechContextEstablished()) {
/*  559 */             localNegoResult = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
/*  560 */             this.state = 3;
/*      */ 
/*  562 */             setContextFlags();
/*      */ 
/*  564 */             if (DEBUG) {
/*  565 */               System.out.println("SPNEGO Negotiated Mechanism = " + this.internal_mech + " " + GSSUtil.getMechStr(this.internal_mech));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  570 */             localNegoResult = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
/*  571 */             this.state = 2;
/*      */           }
/*      */         } else {
/*  574 */           localNegoResult = SpNegoToken.NegoResult.REJECT;
/*  575 */           this.state = 3;
/*      */         }
/*      */ 
/*  578 */         if (DEBUG) {
/*  579 */           System.out.println("SpNegoContext.acceptSecContext: mechanism wanted = " + localOid);
/*      */ 
/*  581 */           System.out.println("SpNegoContext.acceptSecContext: negotiated result = " + localNegoResult);
/*      */         }
/*      */ 
/*  586 */         NegTokenTarg localNegTokenTarg = new NegTokenTarg(localNegoResult.ordinal(), localOid, arrayOfByte4, null);
/*      */ 
/*  588 */         if (DEBUG) {
/*  589 */           System.out.println("SpNegoContext.acceptSecContext: sending token of type = " + SpNegoToken.getTokenName(localNegTokenTarg.getType()));
/*      */         }
/*      */ 
/*  594 */         arrayOfByte1 = localNegTokenTarg.getEncoded();
/*      */       }
/*  596 */       else if (this.state == 2)
/*      */       {
/*  598 */         arrayOfByte2 = new byte[paramInputStream.available()];
/*  599 */         SpNegoToken.readFully(paramInputStream, arrayOfByte2);
/*  600 */         localObject1 = GSS_acceptSecContext(arrayOfByte2);
/*  601 */         if (localObject1 == null) {
/*  602 */           bool = false;
/*      */         }
/*      */ 
/*  606 */         if (bool) {
/*  607 */           if (isMechContextEstablished()) {
/*  608 */             localNegoResult = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
/*  609 */             this.state = 3;
/*      */           } else {
/*  611 */             localNegoResult = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
/*  612 */             this.state = 2;
/*      */           }
/*      */         } else {
/*  615 */           localNegoResult = SpNegoToken.NegoResult.REJECT;
/*  616 */           this.state = 3;
/*      */         }
/*      */ 
/*  620 */         localObject2 = new NegTokenTarg(localNegoResult.ordinal(), null, (byte[])localObject1, null);
/*      */ 
/*  622 */         if (DEBUG) {
/*  623 */           System.out.println("SpNegoContext.acceptSecContext: sending token of type = " + SpNegoToken.getTokenName(((NegTokenTarg)localObject2).getType()));
/*      */         }
/*      */ 
/*  628 */         arrayOfByte1 = ((NegTokenTarg)localObject2).getEncoded();
/*      */       }
/*  632 */       else if (DEBUG) {
/*  633 */         System.out.println("AcceptSecContext: state = " + this.state);
/*      */       }
/*      */ 
/*  636 */       if (DEBUG)
/*  637 */         System.out.println("SpNegoContext.acceptSecContext: sending token = " + SpNegoToken.getHexBytes(arrayOfByte1));
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  641 */       Object localObject1 = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  643 */       ((GSSException)localObject1).initCause(localIOException);
/*  644 */       throw ((Throwable)localObject1);
/*      */     }
/*      */ 
/*  647 */     if (this.state == 3)
/*      */     {
/*  649 */       setContextFlags();
/*      */     }
/*  651 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   private Oid[] getAvailableMechs()
/*      */   {
/*  658 */     if (this.myCred != null) {
/*  659 */       Oid[] arrayOfOid = new Oid[1];
/*  660 */       arrayOfOid[0] = this.myCred.getInternalMech();
/*  661 */       return arrayOfOid;
/*      */     }
/*  663 */     return this.factory.availableMechs;
/*      */   }
/*      */ 
/*      */   private byte[] getEncodedMechs(Oid[] paramArrayOfOid)
/*      */     throws IOException, GSSException
/*      */   {
/*  673 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  674 */     for (int i = 0; i < paramArrayOfOid.length; i++) {
/*  675 */       arrayOfByte = paramArrayOfOid[i].getDER();
/*  676 */       localDerOutputStream1.write(arrayOfByte);
/*      */     }
/*      */ 
/*  679 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  680 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*  681 */     byte[] arrayOfByte = localDerOutputStream2.toByteArray();
/*  682 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private BitArray getContextFlags()
/*      */   {
/*  689 */     BitArray localBitArray = new BitArray(7);
/*      */ 
/*  691 */     if (getCredDelegState()) localBitArray.set(0, true);
/*  692 */     if (getMutualAuthState()) localBitArray.set(1, true);
/*  693 */     if (getReplayDetState()) localBitArray.set(2, true);
/*  694 */     if (getSequenceDetState()) localBitArray.set(3, true);
/*  695 */     if (getConfState()) localBitArray.set(5, true);
/*  696 */     if (getIntegState()) localBitArray.set(6, true);
/*      */ 
/*  698 */     return localBitArray;
/*      */   }
/*      */ 
/*      */   private void setContextFlags()
/*      */   {
/*  706 */     if (this.mechContext != null)
/*      */     {
/*  708 */       if (this.mechContext.getCredDelegState()) {
/*  709 */         this.credDelegState = true;
/*      */       }
/*      */ 
/*  712 */       if (!this.mechContext.getMutualAuthState()) {
/*  713 */         this.mutualAuthState = false;
/*      */       }
/*  715 */       if (!this.mechContext.getReplayDetState()) {
/*  716 */         this.replayDetState = false;
/*      */       }
/*  718 */       if (!this.mechContext.getSequenceDetState()) {
/*  719 */         this.sequenceDetState = false;
/*      */       }
/*  721 */       if (!this.mechContext.getIntegState()) {
/*  722 */         this.integState = false;
/*      */       }
/*  724 */       if (!this.mechContext.getConfState())
/*  725 */         this.confState = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean verifyMechListMIC(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */     throws GSSException
/*      */   {
/*  779 */     if (paramArrayOfByte2 == null) {
/*  780 */       if (DEBUG) {
/*  781 */         System.out.println("SpNegoContext: no MIC token validation");
/*      */       }
/*  783 */       return true;
/*      */     }
/*      */ 
/*  787 */     if (!this.mechContext.getIntegState()) {
/*  788 */       if (DEBUG) {
/*  789 */         System.out.println("SpNegoContext: no MIC token validation - mechanism does not support integrity");
/*      */       }
/*      */ 
/*  792 */       return true;
/*      */     }
/*      */ 
/*  796 */     boolean bool = false;
/*      */     try {
/*  798 */       MessageProp localMessageProp = new MessageProp(0, true);
/*  799 */       verifyMIC(paramArrayOfByte2, 0, paramArrayOfByte2.length, paramArrayOfByte1, 0, paramArrayOfByte1.length, localMessageProp);
/*      */ 
/*  801 */       bool = true;
/*      */     } catch (GSSException localGSSException) {
/*  803 */       bool = false;
/*  804 */       if (DEBUG) {
/*  805 */         System.out.println("SpNegoContext: MIC validation failed! " + localGSSException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/*  809 */     return bool;
/*      */   }
/*      */ 
/*      */   private byte[] GSS_initSecContext(byte[] paramArrayOfByte)
/*      */     throws GSSException
/*      */   {
/*  816 */     byte[] arrayOfByte = null;
/*      */ 
/*  818 */     if (this.mechContext == null)
/*      */     {
/*  820 */       localObject = this.factory.manager.createName(this.peerName.toString(), this.peerName.getStringNameType(), this.internal_mech);
/*      */ 
/*  823 */       GSSCredentialImpl localGSSCredentialImpl = null;
/*  824 */       if (this.myCred != null)
/*      */       {
/*  826 */         localGSSCredentialImpl = new GSSCredentialImpl(this.factory.manager, this.myCred.getInternalCred());
/*      */       }
/*      */ 
/*  829 */       this.mechContext = this.factory.manager.createContext((GSSName)localObject, this.internal_mech, localGSSCredentialImpl, 0);
/*      */ 
/*  832 */       this.mechContext.requestConf(this.confState);
/*  833 */       this.mechContext.requestInteg(this.integState);
/*  834 */       this.mechContext.requestCredDeleg(this.credDelegState);
/*  835 */       this.mechContext.requestMutualAuth(this.mutualAuthState);
/*  836 */       this.mechContext.requestReplayDet(this.replayDetState);
/*  837 */       this.mechContext.requestSequenceDet(this.sequenceDetState);
/*  838 */       if ((this.mechContext instanceof ExtendedGSSContext)) {
/*  839 */         ((ExtendedGSSContext)this.mechContext).requestDelegPolicy(this.delegPolicyState);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  845 */     if (paramArrayOfByte != null)
/*  846 */       arrayOfByte = paramArrayOfByte;
/*      */     else {
/*  848 */       arrayOfByte = new byte[0];
/*      */     }
/*      */ 
/*  852 */     Object localObject = this.mechContext.initSecContext(arrayOfByte, 0, arrayOfByte.length);
/*      */ 
/*  854 */     return localObject;
/*      */   }
/*      */ 
/*      */   private byte[] GSS_acceptSecContext(byte[] paramArrayOfByte)
/*      */     throws GSSException
/*      */   {
/*  862 */     if (this.mechContext == null)
/*      */     {
/*  864 */       localObject = null;
/*  865 */       if (this.myCred != null)
/*      */       {
/*  867 */         localObject = new GSSCredentialImpl(this.factory.manager, this.myCred.getInternalCred());
/*      */       }
/*      */ 
/*  870 */       this.mechContext = this.factory.manager.createContext((GSSCredential)localObject);
/*      */     }
/*      */ 
/*  875 */     Object localObject = this.mechContext.acceptSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */ 
/*  878 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static Oid negotiate_mech_type(Oid[] paramArrayOfOid1, Oid[] paramArrayOfOid2)
/*      */   {
/*  891 */     for (int i = 0; i < paramArrayOfOid1.length; i++) {
/*  892 */       for (int j = 0; j < paramArrayOfOid2.length; j++) {
/*  893 */         if (paramArrayOfOid2[j].equals(paramArrayOfOid1[i])) {
/*  894 */           if (DEBUG) {
/*  895 */             System.out.println("SpNegoContext: negotiated mechanism = " + paramArrayOfOid2[j]);
/*      */           }
/*      */ 
/*  898 */           return paramArrayOfOid2[j];
/*      */         }
/*      */       }
/*      */     }
/*  902 */     return null;
/*      */   }
/*      */ 
/*      */   public final boolean isEstablished() {
/*  906 */     return this.state == 3;
/*      */   }
/*      */ 
/*      */   public final boolean isMechContextEstablished() {
/*  910 */     if (this.mechContext != null) {
/*  911 */       return this.mechContext.isEstablished();
/*      */     }
/*  913 */     if (DEBUG) {
/*  914 */       System.out.println("The underlying mechansim context has not been initialized");
/*      */     }
/*      */ 
/*  917 */     return false;
/*      */   }
/*      */ 
/*      */   public final byte[] export() throws GSSException
/*      */   {
/*  922 */     throw new GSSException(16, -1, "GSS Export Context not available");
/*      */   }
/*      */ 
/*      */   public final void setChannelBinding(ChannelBinding paramChannelBinding)
/*      */     throws GSSException
/*      */   {
/*  932 */     this.channelBinding = paramChannelBinding;
/*      */   }
/*      */ 
/*      */   final ChannelBinding getChannelBinding() {
/*  936 */     return this.channelBinding;
/*      */   }
/*      */ 
/*      */   public final void requestAnonymity(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*      */   }
/*      */ 
/*      */   public final boolean getAnonymityState()
/*      */   {
/*  957 */     return false;
/*      */   }
/*      */ 
/*      */   public void requestLifetime(int paramInt)
/*      */     throws GSSException
/*      */   {
/*  965 */     if ((this.state == 1) && (isInitiator()))
/*  966 */       this.lifetime = paramInt;
/*      */   }
/*      */ 
/*      */   public final int getLifetime()
/*      */   {
/*  973 */     if (this.mechContext != null) {
/*  974 */       return this.mechContext.getLifetime();
/*      */     }
/*  976 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   public final boolean isTransferable() throws GSSException
/*      */   {
/*  981 */     return false;
/*      */   }
/*      */ 
/*      */   public final void requestSequenceDet(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  989 */     if ((this.state == 1) && (isInitiator()))
/*  990 */       this.sequenceDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getSequenceDetState()
/*      */   {
/*  998 */     return (this.sequenceDetState) || (this.replayDetState);
/*      */   }
/*      */ 
/*      */   public final void requestReplayDet(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/* 1006 */     if ((this.state == 1) && (isInitiator()))
/* 1007 */       this.replayDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getReplayDetState()
/*      */   {
/* 1015 */     return (this.replayDetState) || (this.sequenceDetState);
/*      */   }
/*      */ 
/*      */   public final GSSNameSpi getTargName()
/*      */     throws GSSException
/*      */   {
/* 1021 */     if (this.mechContext != null) {
/* 1022 */       GSSNameImpl localGSSNameImpl = (GSSNameImpl)this.mechContext.getTargName();
/* 1023 */       this.peerName = localGSSNameImpl.getElement(this.internal_mech);
/* 1024 */       return this.peerName;
/*      */     }
/* 1026 */     if (DEBUG) {
/* 1027 */       System.out.println("The underlying mechansim context has not been initialized");
/*      */     }
/*      */ 
/* 1030 */     return null;
/*      */   }
/*      */ 
/*      */   public final GSSNameSpi getSrcName()
/*      */     throws GSSException
/*      */   {
/* 1037 */     if (this.mechContext != null) {
/* 1038 */       GSSNameImpl localGSSNameImpl = (GSSNameImpl)this.mechContext.getSrcName();
/* 1039 */       this.myName = localGSSNameImpl.getElement(this.internal_mech);
/* 1040 */       return this.myName;
/*      */     }
/* 1042 */     if (DEBUG) {
/* 1043 */       System.out.println("The underlying mechansim context has not been initialized");
/*      */     }
/*      */ 
/* 1046 */     return null;
/*      */   }
/*      */ 
/*      */   public final GSSCredentialSpi getDelegCred()
/*      */     throws GSSException
/*      */   {
/* 1061 */     if ((this.state != 2) && (this.state != 3))
/* 1062 */       throw new GSSException(12);
/* 1063 */     if (this.mechContext != null) {
/* 1064 */       GSSCredentialImpl localGSSCredentialImpl = (GSSCredentialImpl)this.mechContext.getDelegCred();
/*      */ 
/* 1067 */       boolean bool = false;
/* 1068 */       if (localGSSCredentialImpl.getUsage() == 1) {
/* 1069 */         bool = true;
/*      */       }
/* 1071 */       GSSCredentialSpi localGSSCredentialSpi = localGSSCredentialImpl.getElement(this.internal_mech, bool);
/*      */ 
/* 1073 */       SpNegoCredElement localSpNegoCredElement = new SpNegoCredElement(localGSSCredentialSpi);
/* 1074 */       return localSpNegoCredElement.getInternalCred();
/*      */     }
/* 1076 */     throw new GSSException(12, -1, "getDelegCred called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final int getWrapSizeLimit(int paramInt1, boolean paramBoolean, int paramInt2)
/*      */     throws GSSException
/*      */   {
/* 1083 */     if (this.mechContext != null) {
/* 1084 */       return this.mechContext.getWrapSizeLimit(paramInt1, paramBoolean, paramInt2);
/*      */     }
/* 1086 */     throw new GSSException(12, -1, "getWrapSizeLimit called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1093 */     if (this.mechContext != null) {
/* 1094 */       return this.mechContext.wrap(paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*      */     }
/* 1096 */     throw new GSSException(12, -1, "Wrap called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final void wrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1103 */     if (this.mechContext != null)
/* 1104 */       this.mechContext.wrap(paramInputStream, paramOutputStream, paramMessageProp);
/*      */     else
/* 1106 */       throw new GSSException(12, -1, "Wrap called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1114 */     if (this.mechContext != null) {
/* 1115 */       return this.mechContext.unwrap(paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*      */     }
/* 1117 */     throw new GSSException(12, -1, "UnWrap called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final void unwrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1124 */     if (this.mechContext != null)
/* 1125 */       this.mechContext.unwrap(paramInputStream, paramOutputStream, paramMessageProp);
/*      */     else
/* 1127 */       throw new GSSException(12, -1, "UnWrap called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final byte[] getMIC(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1135 */     if (this.mechContext != null) {
/* 1136 */       return this.mechContext.getMIC(paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*      */     }
/* 1138 */     throw new GSSException(12, -1, "getMIC called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final void getMIC(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1145 */     if (this.mechContext != null)
/* 1146 */       this.mechContext.getMIC(paramInputStream, paramOutputStream, paramMessageProp);
/*      */     else
/* 1148 */       throw new GSSException(12, -1, "getMIC called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final void verifyMIC(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1157 */     if (this.mechContext != null) {
/* 1158 */       this.mechContext.verifyMIC(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3, paramInt4, paramMessageProp);
/*      */     }
/*      */     else
/* 1161 */       throw new GSSException(12, -1, "verifyMIC called in invalid state!");
/*      */   }
/*      */ 
/*      */   public final void verifyMIC(InputStream paramInputStream1, InputStream paramInputStream2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1168 */     if (this.mechContext != null)
/* 1169 */       this.mechContext.verifyMIC(paramInputStream1, paramInputStream2, paramMessageProp);
/*      */     else
/* 1171 */       throw new GSSException(12, -1, "verifyMIC called in invalid state!");
/*      */   }
/*      */ 
/*      */   private static String printState(int paramInt)
/*      */   {
/* 1177 */     switch (paramInt) {
/*      */     case 1:
/* 1179 */       return "STATE_NEW";
/*      */     case 2:
/* 1181 */       return "STATE_IN_PROCESS";
/*      */     case 3:
/* 1183 */       return "STATE_DONE";
/*      */     case 4:
/* 1185 */       return "STATE_DELETED";
/*      */     }
/* 1187 */     return "Unknown state " + paramInt;
/*      */   }
/*      */ 
/*      */   public Object inquireSecContext(InquireType paramInquireType)
/*      */     throws GSSException
/*      */   {
/* 1196 */     if (this.mechContext == null) {
/* 1197 */       throw new GSSException(12, -1, "Underlying mech not established.");
/*      */     }
/*      */ 
/* 1200 */     if ((this.mechContext instanceof ExtendedGSSContext)) {
/* 1201 */       return ((ExtendedGSSContext)this.mechContext).inquireSecContext(paramInquireType);
/*      */     }
/* 1203 */     throw new GSSException(2, -1, "inquireSecContext not supported by underlying mech.");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spnego.SpNegoContext
 * JD-Core Version:    0.6.2
 */