/*      */ package sun.security.jgss.krb5;
/*      */ 
/*      */ import com.sun.security.jgss.AuthorizationDataEntry;
/*      */ import com.sun.security.jgss.InquireType;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.Key;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.Provider;
/*      */ import java.util.Set;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.security.auth.kerberos.KerberosTicket;
/*      */ import javax.security.auth.kerberos.ServicePermission;
/*      */ import org.ietf.jgss.ChannelBinding;
/*      */ import org.ietf.jgss.GSSException;
/*      */ import org.ietf.jgss.MessageProp;
/*      */ import org.ietf.jgss.Oid;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.jgss.GSSCaller;
/*      */ import sun.security.jgss.GSSUtil;
/*      */ import sun.security.jgss.TokenTracker;
/*      */ import sun.security.jgss.spi.GSSContextSpi;
/*      */ import sun.security.jgss.spi.GSSCredentialSpi;
/*      */ import sun.security.jgss.spi.GSSNameSpi;
/*      */ import sun.security.krb5.Credentials;
/*      */ import sun.security.krb5.EncryptionKey;
/*      */ import sun.security.krb5.KrbApReq;
/*      */ import sun.security.krb5.KrbException;
/*      */ import sun.security.krb5.PrincipalName;
/*      */ 
/*      */ class Krb5Context
/*      */   implements GSSContextSpi
/*      */ {
/*      */   private static final int STATE_NEW = 1;
/*      */   private static final int STATE_IN_PROCESS = 2;
/*      */   private static final int STATE_DONE = 3;
/*      */   private static final int STATE_DELETED = 4;
/*   68 */   private int state = 1;
/*      */   public static final int SESSION_KEY = 0;
/*      */   public static final int INITIATOR_SUBKEY = 1;
/*      */   public static final int ACCEPTOR_SUBKEY = 2;
/*   79 */   private boolean credDelegState = false;
/*   80 */   private boolean mutualAuthState = true;
/*   81 */   private boolean replayDetState = true;
/*   82 */   private boolean sequenceDetState = true;
/*   83 */   private boolean confState = true;
/*   84 */   private boolean integState = true;
/*   85 */   private boolean delegPolicyState = false;
/*      */   private int mySeqNumber;
/*      */   private int peerSeqNumber;
/*      */   private int keySrc;
/*      */   private TokenTracker peerTokenTracker;
/*   92 */   private CipherHelper cipherHelper = null;
/*      */ 
/*  103 */   private Object mySeqNumberLock = new Object();
/*  104 */   private Object peerSeqNumberLock = new Object();
/*      */   private EncryptionKey key;
/*      */   private Krb5NameElement myName;
/*      */   private Krb5NameElement peerName;
/*      */   private int lifetime;
/*      */   private boolean initiator;
/*      */   private ChannelBinding channelBinding;
/*      */   private Krb5CredElement myCred;
/*      */   private Krb5CredElement delegatedCred;
/*  117 */   private Cipher desCipher = null;
/*      */   private Credentials serviceCreds;
/*      */   private KrbApReq apReq;
/*      */   private final GSSCaller caller;
/*  124 */   private static final boolean DEBUG = Krb5Util.DEBUG;
/*      */   private boolean[] tktFlags;
/*      */   private String authTime;
/*      */   private AuthorizationDataEntry[] authzData;
/*      */ 
/*      */   Krb5Context(GSSCaller paramGSSCaller, Krb5NameElement paramKrb5NameElement, Krb5CredElement paramKrb5CredElement, int paramInt)
/*      */     throws GSSException
/*      */   {
/*  134 */     if (paramKrb5NameElement == null) {
/*  135 */       throw new IllegalArgumentException("Cannot have null peer name");
/*      */     }
/*  137 */     this.caller = paramGSSCaller;
/*  138 */     this.peerName = paramKrb5NameElement;
/*  139 */     this.myCred = paramKrb5CredElement;
/*  140 */     this.lifetime = paramInt;
/*  141 */     this.initiator = true;
/*      */   }
/*      */ 
/*      */   Krb5Context(GSSCaller paramGSSCaller, Krb5CredElement paramKrb5CredElement)
/*      */     throws GSSException
/*      */   {
/*  150 */     this.caller = paramGSSCaller;
/*  151 */     this.myCred = paramKrb5CredElement;
/*  152 */     this.initiator = false;
/*      */   }
/*      */ 
/*      */   public Krb5Context(GSSCaller paramGSSCaller, byte[] paramArrayOfByte)
/*      */     throws GSSException
/*      */   {
/*  160 */     throw new GSSException(16, -1, "GSS Import Context not available");
/*      */   }
/*      */ 
/*      */   public final boolean isTransferable()
/*      */     throws GSSException
/*      */   {
/*  169 */     return false;
/*      */   }
/*      */ 
/*      */   public final int getLifetime()
/*      */   {
/*  177 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   public void requestLifetime(int paramInt)
/*      */     throws GSSException
/*      */   {
/*  204 */     if ((this.state == 1) && (isInitiator()))
/*  205 */       this.lifetime = paramInt;
/*      */   }
/*      */ 
/*      */   public final void requestConf(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  212 */     if ((this.state == 1) && (isInitiator()))
/*  213 */       this.confState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getConfState()
/*      */   {
/*  220 */     return this.confState;
/*      */   }
/*      */ 
/*      */   public final void requestInteg(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  227 */     if ((this.state == 1) && (isInitiator()))
/*  228 */       this.integState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getIntegState()
/*      */   {
/*  235 */     return this.integState;
/*      */   }
/*      */ 
/*      */   public final void requestCredDeleg(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  243 */     if ((this.state == 1) && (isInitiator()))
/*  244 */       this.credDelegState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getCredDelegState()
/*      */   {
/*  251 */     return this.credDelegState;
/*      */   }
/*      */ 
/*      */   public final void requestMutualAuth(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  260 */     if ((this.state == 1) && (isInitiator()))
/*  261 */       this.mutualAuthState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getMutualAuthState()
/*      */   {
/*  271 */     return this.mutualAuthState;
/*      */   }
/*      */ 
/*      */   public final void requestReplayDet(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  279 */     if ((this.state == 1) && (isInitiator()))
/*  280 */       this.replayDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getReplayDetState()
/*      */   {
/*  288 */     return (this.replayDetState) || (this.sequenceDetState);
/*      */   }
/*      */ 
/*      */   public final void requestSequenceDet(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*  296 */     if ((this.state == 1) && (isInitiator()))
/*  297 */       this.sequenceDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getSequenceDetState()
/*      */   {
/*  305 */     return (this.sequenceDetState) || (this.replayDetState);
/*      */   }
/*      */ 
/*      */   public final void requestDelegPolicy(boolean paramBoolean)
/*      */   {
/*  312 */     if ((this.state == 1) && (isInitiator()))
/*  313 */       this.delegPolicyState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final boolean getDelegPolicyState()
/*      */   {
/*  320 */     return this.delegPolicyState;
/*      */   }
/*      */ 
/*      */   public final void requestAnonymity(boolean paramBoolean)
/*      */     throws GSSException
/*      */   {
/*      */   }
/*      */ 
/*      */   public final boolean getAnonymityState()
/*      */   {
/*  342 */     return false;
/*      */   }
/*      */ 
/*      */   final CipherHelper getCipherHelper(EncryptionKey paramEncryptionKey)
/*      */     throws GSSException
/*      */   {
/*  354 */     EncryptionKey localEncryptionKey = null;
/*  355 */     if (this.cipherHelper == null) {
/*  356 */       localEncryptionKey = getKey() == null ? paramEncryptionKey : getKey();
/*  357 */       this.cipherHelper = new CipherHelper(localEncryptionKey);
/*      */     }
/*  359 */     return this.cipherHelper;
/*      */   }
/*      */ 
/*      */   final int incrementMySequenceNumber()
/*      */   {
/*      */     int i;
/*  364 */     synchronized (this.mySeqNumberLock) {
/*  365 */       i = this.mySeqNumber;
/*  366 */       this.mySeqNumber = (i + 1);
/*      */     }
/*  368 */     return i;
/*      */   }
/*      */ 
/*      */   final void resetMySequenceNumber(int paramInt) {
/*  372 */     if (DEBUG) {
/*  373 */       System.out.println("Krb5Context setting mySeqNumber to: " + paramInt);
/*      */     }
/*      */ 
/*  376 */     synchronized (this.mySeqNumberLock) {
/*  377 */       this.mySeqNumber = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void resetPeerSequenceNumber(int paramInt) {
/*  382 */     if (DEBUG) {
/*  383 */       System.out.println("Krb5Context setting peerSeqNumber to: " + paramInt);
/*      */     }
/*      */ 
/*  386 */     synchronized (this.peerSeqNumberLock) {
/*  387 */       this.peerSeqNumber = paramInt;
/*  388 */       this.peerTokenTracker = new TokenTracker(this.peerSeqNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */   final void setKey(int paramInt, EncryptionKey paramEncryptionKey) throws GSSException {
/*  393 */     this.key = paramEncryptionKey;
/*  394 */     this.keySrc = paramInt;
/*      */ 
/*  396 */     this.cipherHelper = new CipherHelper(paramEncryptionKey);
/*      */   }
/*      */ 
/*      */   public final int getKeySrc() {
/*  400 */     return this.keySrc;
/*      */   }
/*      */ 
/*      */   private final EncryptionKey getKey() {
/*  404 */     return this.key;
/*      */   }
/*      */ 
/*      */   final void setDelegCred(Krb5CredElement paramKrb5CredElement)
/*      */   {
/*  412 */     this.delegatedCred = paramKrb5CredElement;
/*      */   }
/*      */ 
/*      */   final void setCredDelegState(boolean paramBoolean)
/*      */   {
/*  428 */     this.credDelegState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setMutualAuthState(boolean paramBoolean) {
/*  432 */     this.mutualAuthState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setReplayDetState(boolean paramBoolean) {
/*  436 */     this.replayDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setSequenceDetState(boolean paramBoolean) {
/*  440 */     this.sequenceDetState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setConfState(boolean paramBoolean) {
/*  444 */     this.confState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setIntegState(boolean paramBoolean) {
/*  448 */     this.integState = paramBoolean;
/*      */   }
/*      */ 
/*      */   final void setDelegPolicyState(boolean paramBoolean) {
/*  452 */     this.delegPolicyState = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final void setChannelBinding(ChannelBinding paramChannelBinding)
/*      */     throws GSSException
/*      */   {
/*  461 */     this.channelBinding = paramChannelBinding;
/*      */   }
/*      */ 
/*      */   final ChannelBinding getChannelBinding() {
/*  465 */     return this.channelBinding;
/*      */   }
/*      */ 
/*      */   public final Oid getMech()
/*      */   {
/*  474 */     return Krb5MechFactory.GSS_KRB5_MECH_OID;
/*      */   }
/*      */ 
/*      */   public final GSSNameSpi getSrcName()
/*      */     throws GSSException
/*      */   {
/*  484 */     return isInitiator() ? this.myName : this.peerName;
/*      */   }
/*      */ 
/*      */   public final GSSNameSpi getTargName()
/*      */     throws GSSException
/*      */   {
/*  494 */     return !isInitiator() ? this.myName : this.peerName;
/*      */   }
/*      */ 
/*      */   public final GSSCredentialSpi getDelegCred()
/*      */     throws GSSException
/*      */   {
/*  508 */     if ((this.state != 2) && (this.state != 3))
/*  509 */       throw new GSSException(12);
/*  510 */     if (this.delegatedCred == null)
/*  511 */       throw new GSSException(13);
/*  512 */     return this.delegatedCred;
/*      */   }
/*      */ 
/*      */   public final boolean isInitiator()
/*      */   {
/*  522 */     return this.initiator;
/*      */   }
/*      */ 
/*      */   public final boolean isProtReady()
/*      */   {
/*  534 */     return this.state == 3;
/*      */   }
/*      */ 
/*      */   public final byte[] initSecContext(InputStream paramInputStream, int paramInt)
/*      */     throws GSSException
/*      */   {
/*  553 */     byte[] arrayOfByte = null;
/*  554 */     InitSecContextToken localInitSecContextToken = null;
/*  555 */     int i = 11;
/*  556 */     if (DEBUG) {
/*  557 */       System.out.println("Entered Krb5Context.initSecContext with state=" + printState(this.state));
/*      */     }
/*      */ 
/*  560 */     if (!isInitiator()) {
/*  561 */       throw new GSSException(11, -1, "initSecContext on an acceptor GSSContext");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  567 */       if (this.state == 1) {
/*  568 */         this.state = 2;
/*      */ 
/*  570 */         i = 13;
/*      */ 
/*  572 */         if (this.myCred == null) {
/*  573 */           this.myCred = Krb5InitCredential.getInstance(this.caller, this.myName, 0);
/*      */         }
/*  575 */         else if (!this.myCred.isInitiatorCredential()) {
/*  576 */           throw new GSSException(i, -1, "No TGT available");
/*      */         }
/*      */ 
/*  579 */         this.myName = ((Krb5NameElement)this.myCred.getName());
/*  580 */         Credentials localCredentials = ((Krb5InitCredential)this.myCred).getKrb5Credentials();
/*      */ 
/*  583 */         checkPermission(this.peerName.getKrb5PrincipalName().getName(), "initiate");
/*      */ 
/*  591 */         localObject1 = AccessController.getContext();
/*      */         Object localObject2;
/*  594 */         if (GSSUtil.useSubjectCredsOnly(this.caller)) {
/*  595 */           localObject2 = null;
/*      */           try
/*      */           {
/*  598 */             localObject2 = (KerberosTicket)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */             {
/*      */               public KerberosTicket run()
/*      */                 throws Exception
/*      */               {
/*  606 */                 return Krb5Util.getTicket(GSSCaller.CALLER_UNKNOWN, Krb5Context.this.myName.getKrb5PrincipalName().getName(), Krb5Context.this.peerName.getKrb5PrincipalName().getName(), this.val$acc);
/*      */               }
/*      */ 
/*      */             });
/*      */           }
/*      */           catch (PrivilegedActionException localPrivilegedActionException)
/*      */           {
/*  615 */             if (DEBUG) {
/*  616 */               System.out.println("Attempt to obtain service ticket from the subject failed!");
/*      */             }
/*      */           }
/*      */ 
/*  620 */           if (localObject2 != null) {
/*  621 */             if (DEBUG) {
/*  622 */               System.out.println("Found service ticket in the subject" + localObject2);
/*      */             }
/*      */ 
/*  630 */             this.serviceCreds = Krb5Util.ticketToCreds((KerberosTicket)localObject2);
/*      */           }
/*      */         }
/*  633 */         if (this.serviceCreds == null)
/*      */         {
/*  636 */           if (DEBUG) {
/*  637 */             System.out.println("Service ticket not found in the subject");
/*      */           }
/*      */ 
/*  641 */           this.serviceCreds = Credentials.acquireServiceCreds(this.peerName.getKrb5PrincipalName().getName(), localCredentials);
/*      */ 
/*  644 */           if (GSSUtil.useSubjectCredsOnly(this.caller)) {
/*  645 */             localObject2 = (Subject)AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Subject run()
/*      */               {
/*  649 */                 return Subject.getSubject(this.val$acc);
/*      */               }
/*      */             });
/*  652 */             if ((localObject2 != null) && (!((Subject)localObject2).isReadOnly()))
/*      */             {
/*  661 */               final KerberosTicket localKerberosTicket = Krb5Util.credsToTicket(this.serviceCreds);
/*      */ 
/*  663 */               AccessController.doPrivileged(new PrivilegedAction()
/*      */               {
/*      */                 public Void run() {
/*  666 */                   this.val$subject.getPrivateCredentials().add(localKerberosTicket);
/*  667 */                   return null;
/*      */                 }
/*      */ 
/*      */               });
/*      */             }
/*  672 */             else if (DEBUG) {
/*  673 */               System.out.println("Subject is readOnly;Kerberos Service ticket not stored");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  681 */         i = 11;
/*  682 */         localInitSecContextToken = new InitSecContextToken(this, localCredentials, this.serviceCreds);
/*  683 */         this.apReq = ((InitSecContextToken)localInitSecContextToken).getKrbApReq();
/*  684 */         arrayOfByte = localInitSecContextToken.encode();
/*  685 */         this.myCred = null;
/*  686 */         if (!getMutualAuthState()) {
/*  687 */           this.state = 3;
/*      */         }
/*  689 */         if (DEBUG) {
/*  690 */           System.out.println("Created InitSecContextToken:\n" + new HexDumpEncoder().encodeBuffer(arrayOfByte));
/*      */         }
/*      */       }
/*  693 */       else if (this.state == 2)
/*      */       {
/*  696 */         new AcceptSecContextToken(this, this.serviceCreds, this.apReq, paramInputStream);
/*  697 */         this.serviceCreds = null;
/*  698 */         this.apReq = null;
/*  699 */         this.state = 3;
/*      */       }
/*  702 */       else if (DEBUG) {
/*  703 */         System.out.println(this.state);
/*      */       }
/*      */     }
/*      */     catch (KrbException localKrbException) {
/*  707 */       if (DEBUG) {
/*  708 */         localKrbException.printStackTrace();
/*      */       }
/*  710 */       localObject1 = new GSSException(i, -1, localKrbException.getMessage());
/*      */ 
/*  712 */       ((GSSException)localObject1).initCause(localKrbException);
/*  713 */       throw ((Throwable)localObject1);
/*      */     } catch (IOException localIOException) {
/*  715 */       Object localObject1 = new GSSException(i, -1, localIOException.getMessage());
/*      */ 
/*  717 */       ((GSSException)localObject1).initCause(localIOException);
/*  718 */       throw ((Throwable)localObject1);
/*      */     }
/*  720 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public final boolean isEstablished() {
/*  724 */     return this.state == 3;
/*      */   }
/*      */ 
/*      */   public final byte[] acceptSecContext(InputStream paramInputStream, int paramInt)
/*      */     throws GSSException
/*      */   {
/*  742 */     byte[] arrayOfByte = null;
/*      */ 
/*  744 */     if (DEBUG) {
/*  745 */       System.out.println("Entered Krb5Context.acceptSecContext with state=" + printState(this.state));
/*      */     }
/*      */ 
/*  749 */     if (isInitiator()) {
/*  750 */       throw new GSSException(11, -1, "acceptSecContext on an initiator GSSContext");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  755 */       if (this.state == 1) {
/*  756 */         this.state = 2;
/*  757 */         if (this.myCred == null)
/*  758 */           this.myCred = Krb5AcceptCredential.getInstance(this.caller, this.myName);
/*  759 */         else if (!this.myCred.isAcceptorCredential()) {
/*  760 */           throw new GSSException(13, -1, "No Secret Key available");
/*      */         }
/*      */ 
/*  763 */         this.myName = ((Krb5NameElement)this.myCred.getName());
/*      */ 
/*  765 */         checkPermission(this.myName.getKrb5PrincipalName().getName(), "accept");
/*      */ 
/*  768 */         EncryptionKey[] arrayOfEncryptionKey = ((Krb5AcceptCredential)this.myCred).getKrb5EncryptionKeys();
/*      */ 
/*  771 */         localObject = new InitSecContextToken(this, arrayOfEncryptionKey, paramInputStream);
/*      */ 
/*  773 */         PrincipalName localPrincipalName = ((InitSecContextToken)localObject).getKrbApReq().getClient();
/*  774 */         this.peerName = Krb5NameElement.getInstance(localPrincipalName);
/*  775 */         if (getMutualAuthState()) {
/*  776 */           arrayOfByte = new AcceptSecContextToken(this, ((InitSecContextToken)localObject).getKrbApReq()).encode();
/*      */         }
/*      */ 
/*  779 */         this.myCred = null;
/*  780 */         this.state = 3;
/*      */       }
/*  783 */       else if (DEBUG) {
/*  784 */         System.out.println(this.state);
/*      */       }
/*      */     }
/*      */     catch (KrbException localKrbException) {
/*  788 */       localObject = new GSSException(11, -1, localKrbException.getMessage());
/*      */ 
/*  790 */       ((GSSException)localObject).initCause(localKrbException);
/*  791 */       throw ((Throwable)localObject);
/*      */     } catch (IOException localIOException) {
/*  793 */       if (DEBUG) {
/*  794 */         localIOException.printStackTrace();
/*      */       }
/*  796 */       Object localObject = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  798 */       ((GSSException)localObject).initCause(localIOException);
/*  799 */       throw ((Throwable)localObject);
/*      */     }
/*      */ 
/*  802 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public final int getWrapSizeLimit(int paramInt1, boolean paramBoolean, int paramInt2)
/*      */     throws GSSException
/*      */   {
/*  824 */     int i = 0;
/*  825 */     if (this.cipherHelper.getProto() == 0) {
/*  826 */       i = WrapToken.getSizeLimit(paramInt1, paramBoolean, paramInt2, getCipherHelper(null));
/*      */     }
/*  828 */     else if (this.cipherHelper.getProto() == 1) {
/*  829 */       i = WrapToken_v2.getSizeLimit(paramInt1, paramBoolean, paramInt2, getCipherHelper(null));
/*      */     }
/*      */ 
/*  832 */     return i;
/*      */   }
/*      */ 
/*      */   public final byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*  844 */     if (DEBUG) {
/*  845 */       System.out.println("Krb5Context.wrap: data=[" + getHexBytes(paramArrayOfByte, paramInt1, paramInt2) + "]");
/*      */     }
/*      */ 
/*  850 */     if (this.state != 3) {
/*  851 */       throw new GSSException(12, -1, "Wrap called in invalid state!");
/*      */     }
/*      */ 
/*  854 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/*      */       Object localObject;
/*  856 */       if (this.cipherHelper.getProto() == 0) {
/*  857 */         localObject = new WrapToken(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  859 */         arrayOfByte = ((WrapToken)localObject).encode();
/*  860 */       } else if (this.cipherHelper.getProto() == 1) {
/*  861 */         localObject = new WrapToken_v2(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  863 */         arrayOfByte = ((WrapToken_v2)localObject).encode();
/*      */       }
/*  865 */       if (DEBUG) {
/*  866 */         System.out.println("Krb5Context.wrap: token=[" + getHexBytes(arrayOfByte, 0, arrayOfByte.length) + "]");
/*      */       }
/*      */ 
/*  870 */       return arrayOfByte;
/*      */     } catch (IOException localIOException) {
/*  872 */       arrayOfByte = null;
/*  873 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  875 */       localGSSException.initCause(localIOException);
/*  876 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int wrap(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*  884 */     if (this.state != 3) {
/*  885 */       throw new GSSException(12, -1, "Wrap called in invalid state!");
/*      */     }
/*      */ 
/*  888 */     int i = 0;
/*      */     try
/*      */     {
/*      */       Object localObject;
/*  890 */       if (this.cipherHelper.getProto() == 0) {
/*  891 */         localObject = new WrapToken(this, paramMessageProp, paramArrayOfByte1, paramInt1, paramInt2);
/*      */ 
/*  893 */         i = ((WrapToken)localObject).encode(paramArrayOfByte2, paramInt3);
/*  894 */       } else if (this.cipherHelper.getProto() == 1) {
/*  895 */         localObject = new WrapToken_v2(this, paramMessageProp, paramArrayOfByte1, paramInt1, paramInt2);
/*      */ 
/*  897 */         i = ((WrapToken_v2)localObject).encode(paramArrayOfByte2, paramInt3);
/*      */       }
/*  899 */       if (DEBUG) {
/*  900 */         System.out.println("Krb5Context.wrap: token=[" + getHexBytes(paramArrayOfByte2, paramInt3, i) + "]");
/*      */       }
/*      */ 
/*  904 */       return i;
/*      */     } catch (IOException localIOException) {
/*  906 */       i = 0;
/*  907 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  909 */       localGSSException.initCause(localIOException);
/*  910 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*  918 */     if (this.state != 3) {
/*  919 */       throw new GSSException(12, -1, "Wrap called in invalid state!");
/*      */     }
/*      */ 
/*  922 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/*      */       Object localObject;
/*  924 */       if (this.cipherHelper.getProto() == 0) {
/*  925 */         localObject = new WrapToken(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  927 */         ((WrapToken)localObject).encode(paramOutputStream);
/*  928 */         if (DEBUG)
/*  929 */           arrayOfByte = ((WrapToken)localObject).encode();
/*      */       }
/*  931 */       else if (this.cipherHelper.getProto() == 1) {
/*  932 */         localObject = new WrapToken_v2(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/*  934 */         ((WrapToken_v2)localObject).encode(paramOutputStream);
/*  935 */         if (DEBUG)
/*  936 */           arrayOfByte = ((WrapToken_v2)localObject).encode();
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  940 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  942 */       localGSSException.initCause(localIOException);
/*  943 */       throw localGSSException;
/*      */     }
/*      */ 
/*  946 */     if (DEBUG)
/*  947 */       System.out.println("Krb5Context.wrap: token=[" + getHexBytes(arrayOfByte, 0, arrayOfByte.length) + "]");
/*      */   }
/*      */ 
/*      */   public final void wrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/*  958 */       arrayOfByte = new byte[paramInputStream.available()];
/*  959 */       paramInputStream.read(arrayOfByte);
/*      */     } catch (IOException localIOException) {
/*  961 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/*  963 */       localGSSException.initCause(localIOException);
/*  964 */       throw localGSSException;
/*      */     }
/*  966 */     wrap(arrayOfByte, 0, arrayOfByte.length, paramOutputStream, paramMessageProp);
/*      */   }
/*      */ 
/*      */   public final byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*  973 */     if (DEBUG) {
/*  974 */       System.out.println("Krb5Context.unwrap: token=[" + getHexBytes(paramArrayOfByte, paramInt1, paramInt2) + "]");
/*      */     }
/*      */ 
/*  979 */     if (this.state != 3) {
/*  980 */       throw new GSSException(12, -1, " Unwrap called in invalid state!");
/*      */     }
/*      */ 
/*  984 */     byte[] arrayOfByte = null;
/*      */     Object localObject;
/*  985 */     if (this.cipherHelper.getProto() == 0) {
/*  986 */       localObject = new WrapToken(this, paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/*  988 */       arrayOfByte = ((WrapToken)localObject).getData();
/*  989 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/*  990 */     } else if (this.cipherHelper.getProto() == 1) {
/*  991 */       localObject = new WrapToken_v2(this, paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/*  993 */       arrayOfByte = ((WrapToken_v2)localObject).getData();
/*  994 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/*      */ 
/*  997 */     if (DEBUG) {
/*  998 */       System.out.println("Krb5Context.unwrap: data=[" + getHexBytes(arrayOfByte, 0, arrayOfByte.length) + "]");
/*      */     }
/*      */ 
/* 1003 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public final int unwrap(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1010 */     if (this.state != 3)
/* 1011 */       throw new GSSException(12, -1, "Unwrap called in invalid state!");
/*      */     Object localObject;
/* 1014 */     if (this.cipherHelper.getProto() == 0) {
/* 1015 */       localObject = new WrapToken(this, paramArrayOfByte1, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/* 1017 */       paramInt2 = ((WrapToken)localObject).getData(paramArrayOfByte2, paramInt3);
/* 1018 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/* 1019 */     } else if (this.cipherHelper.getProto() == 1) {
/* 1020 */       localObject = new WrapToken_v2(this, paramArrayOfByte1, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/* 1022 */       paramInt2 = ((WrapToken_v2)localObject).getData(paramArrayOfByte2, paramInt3);
/* 1023 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/* 1025 */     return paramInt2;
/*      */   }
/*      */ 
/*      */   public final int unwrap(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1032 */     if (this.state != 3) {
/* 1033 */       throw new GSSException(12, -1, "Unwrap called in invalid state!");
/*      */     }
/*      */ 
/* 1036 */     int i = 0;
/*      */     Object localObject;
/* 1037 */     if (this.cipherHelper.getProto() == 0) {
/* 1038 */       localObject = new WrapToken(this, paramInputStream, paramMessageProp);
/* 1039 */       i = ((WrapToken)localObject).getData(paramArrayOfByte, paramInt);
/* 1040 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/* 1041 */     } else if (this.cipherHelper.getProto() == 1) {
/* 1042 */       localObject = new WrapToken_v2(this, paramInputStream, paramMessageProp);
/* 1043 */       i = ((WrapToken_v2)localObject).getData(paramArrayOfByte, paramInt);
/* 1044 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/* 1046 */     return i;
/*      */   }
/*      */ 
/*      */   public final void unwrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1053 */     if (this.state != 3) {
/* 1054 */       throw new GSSException(12, -1, "Unwrap called in invalid state!");
/*      */     }
/*      */ 
/* 1057 */     byte[] arrayOfByte = null;
/*      */     Object localObject;
/* 1058 */     if (this.cipherHelper.getProto() == 0) {
/* 1059 */       localObject = new WrapToken(this, paramInputStream, paramMessageProp);
/* 1060 */       arrayOfByte = ((WrapToken)localObject).getData();
/* 1061 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/* 1062 */     } else if (this.cipherHelper.getProto() == 1) {
/* 1063 */       localObject = new WrapToken_v2(this, paramInputStream, paramMessageProp);
/* 1064 */       arrayOfByte = ((WrapToken_v2)localObject).getData();
/* 1065 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/*      */     try
/*      */     {
/* 1069 */       paramOutputStream.write(arrayOfByte);
/*      */     } catch (IOException localIOException) {
/* 1071 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1073 */       localGSSException.initCause(localIOException);
/* 1074 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final byte[] getMIC(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1082 */     byte[] arrayOfByte = null;
/*      */     try
/*      */     {
/*      */       Object localObject;
/* 1084 */       if (this.cipherHelper.getProto() == 0) {
/* 1085 */         localObject = new MicToken(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/* 1087 */         arrayOfByte = ((MicToken)localObject).encode();
/* 1088 */       } else if (this.cipherHelper.getProto() == 1) {
/* 1089 */         localObject = new MicToken_v2(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */       }
/* 1091 */       return ((MicToken_v2)localObject).encode();
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1095 */       arrayOfByte = null;
/* 1096 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1098 */       localGSSException.initCause(localIOException);
/* 1099 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getMIC(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/* 1108 */     int i = 0;
/*      */     try
/*      */     {
/*      */       Object localObject;
/* 1110 */       if (this.cipherHelper.getProto() == 0) {
/* 1111 */         localObject = new MicToken(this, paramMessageProp, paramArrayOfByte1, paramInt1, paramInt2);
/*      */ 
/* 1113 */         i = ((MicToken)localObject).encode(paramArrayOfByte2, paramInt3);
/* 1114 */       } else if (this.cipherHelper.getProto() == 1) {
/* 1115 */         localObject = new MicToken_v2(this, paramMessageProp, paramArrayOfByte1, paramInt1, paramInt2);
/*      */       }
/* 1117 */       return ((MicToken_v2)localObject).encode(paramArrayOfByte2, paramInt3);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1121 */       i = 0;
/* 1122 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1124 */       localGSSException.initCause(localIOException);
/* 1125 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getMIC(byte[] paramArrayOfByte, int paramInt1, int paramInt2, OutputStream paramOutputStream, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*      */     try
/*      */     {
/*      */       Object localObject;
/* 1141 */       if (this.cipherHelper.getProto() == 0) {
/* 1142 */         localObject = new MicToken(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/* 1144 */         ((MicToken)localObject).encode(paramOutputStream);
/* 1145 */       } else if (this.cipherHelper.getProto() == 1) {
/* 1146 */         localObject = new MicToken_v2(this, paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*      */ 
/* 1148 */         ((MicToken_v2)localObject).encode(paramOutputStream);
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 1151 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1153 */       localGSSException.initCause(localIOException);
/* 1154 */       throw localGSSException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void getMIC(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp) throws GSSException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try {
/* 1162 */       arrayOfByte = new byte[paramInputStream.available()];
/* 1163 */       paramInputStream.read(arrayOfByte);
/*      */     } catch (IOException localIOException) {
/* 1165 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1167 */       localGSSException.initCause(localIOException);
/* 1168 */       throw localGSSException;
/*      */     }
/* 1170 */     getMIC(arrayOfByte, 0, arrayOfByte.length, paramOutputStream, paramMessageProp);
/*      */   }
/*      */ 
/*      */   public final void verifyMIC(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*      */     Object localObject;
/* 1178 */     if (this.cipherHelper.getProto() == 0) {
/* 1179 */       localObject = new MicToken(this, paramArrayOfByte1, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/* 1181 */       ((MicToken)localObject).verify(paramArrayOfByte2, paramInt3, paramInt4);
/* 1182 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/* 1183 */     } else if (this.cipherHelper.getProto() == 1) {
/* 1184 */       localObject = new MicToken_v2(this, paramArrayOfByte1, paramInt1, paramInt2, paramMessageProp);
/*      */ 
/* 1186 */       ((MicToken_v2)localObject).verify(paramArrayOfByte2, paramInt3, paramInt4);
/* 1187 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void verifyMIC(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*      */     throws GSSException
/*      */   {
/*      */     Object localObject;
/* 1196 */     if (this.cipherHelper.getProto() == 0) {
/* 1197 */       localObject = new MicToken(this, paramInputStream, paramMessageProp);
/* 1198 */       ((MicToken)localObject).verify(paramArrayOfByte, paramInt1, paramInt2);
/* 1199 */       setSequencingAndReplayProps((MessageToken)localObject, paramMessageProp);
/* 1200 */     } else if (this.cipherHelper.getProto() == 1) {
/* 1201 */       localObject = new MicToken_v2(this, paramInputStream, paramMessageProp);
/* 1202 */       ((MicToken_v2)localObject).verify(paramArrayOfByte, paramInt1, paramInt2);
/* 1203 */       setSequencingAndReplayProps((MessageToken_v2)localObject, paramMessageProp);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void verifyMIC(InputStream paramInputStream1, InputStream paramInputStream2, MessageProp paramMessageProp) throws GSSException
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try {
/* 1211 */       arrayOfByte = new byte[paramInputStream2.available()];
/* 1212 */       paramInputStream2.read(arrayOfByte);
/*      */     } catch (IOException localIOException) {
/* 1214 */       GSSException localGSSException = new GSSException(11, -1, localIOException.getMessage());
/*      */ 
/* 1216 */       localGSSException.initCause(localIOException);
/* 1217 */       throw localGSSException;
/*      */     }
/* 1219 */     verifyMIC(paramInputStream1, arrayOfByte, 0, arrayOfByte.length, paramMessageProp);
/*      */   }
/*      */ 
/*      */   public final byte[] export()
/*      */     throws GSSException
/*      */   {
/* 1231 */     throw new GSSException(16, -1, "GSS Export Context not available");
/*      */   }
/*      */ 
/*      */   public final void dispose()
/*      */     throws GSSException
/*      */   {
/* 1243 */     this.state = 4;
/* 1244 */     this.delegatedCred = null;
/*      */   }
/*      */ 
/*      */   public final Provider getProvider() {
/* 1248 */     return Krb5MechFactory.PROVIDER;
/*      */   }
/*      */ 
/*      */   private void setSequencingAndReplayProps(MessageToken paramMessageToken, MessageProp paramMessageProp)
/*      */   {
/* 1257 */     if ((this.replayDetState) || (this.sequenceDetState)) {
/* 1258 */       int i = paramMessageToken.getSequenceNumber();
/* 1259 */       this.peerTokenTracker.getProps(i, paramMessageProp);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setSequencingAndReplayProps(MessageToken_v2 paramMessageToken_v2, MessageProp paramMessageProp)
/*      */   {
/* 1269 */     if ((this.replayDetState) || (this.sequenceDetState)) {
/* 1270 */       int i = paramMessageToken_v2.getSequenceNumber();
/* 1271 */       this.peerTokenTracker.getProps(i, paramMessageProp);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkPermission(String paramString1, String paramString2) {
/* 1276 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1277 */     if (localSecurityManager != null) {
/* 1278 */       ServicePermission localServicePermission = new ServicePermission(paramString1, paramString2);
/*      */ 
/* 1280 */       localSecurityManager.checkPermission(localServicePermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String getHexBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/* 1286 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1287 */     for (int i = 0; i < paramInt2; i++)
/*      */     {
/* 1289 */       int j = paramArrayOfByte[i] >> 4 & 0xF;
/* 1290 */       int k = paramArrayOfByte[i] & 0xF;
/*      */ 
/* 1292 */       localStringBuffer.append(Integer.toHexString(j));
/* 1293 */       localStringBuffer.append(Integer.toHexString(k));
/* 1294 */       localStringBuffer.append(' ');
/*      */     }
/* 1296 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static String printState(int paramInt) {
/* 1300 */     switch (paramInt) {
/*      */     case 1:
/* 1302 */       return "STATE_NEW";
/*      */     case 2:
/* 1304 */       return "STATE_IN_PROCESS";
/*      */     case 3:
/* 1306 */       return "STATE_DONE";
/*      */     case 4:
/* 1308 */       return "STATE_DELETED";
/*      */     }
/* 1310 */     return "Unknown state " + paramInt;
/*      */   }
/*      */ 
/*      */   GSSCaller getCaller()
/*      */   {
/* 1316 */     return this.caller;
/*      */   }
/*      */ 
/*      */   public Object inquireSecContext(InquireType paramInquireType)
/*      */     throws GSSException
/*      */   {
/* 1356 */     if (!isEstablished()) {
/* 1357 */       throw new GSSException(12, -1, "Security context not established.");
/*      */     }
/*      */ 
/* 1360 */     switch (4.$SwitchMap$com$sun$security$jgss$InquireType[paramInquireType.ordinal()]) {
/*      */     case 1:
/* 1362 */       return new KerberosSessionKey(this.key);
/*      */     case 2:
/* 1364 */       return this.tktFlags.clone();
/*      */     case 3:
/* 1366 */       if (isInitiator()) {
/* 1367 */         throw new GSSException(16, -1, "AuthzData not available on initiator side.");
/*      */       }
/*      */ 
/* 1370 */       return this.authzData == null ? null : (AuthorizationDataEntry[])this.authzData.clone();
/*      */     case 4:
/* 1373 */       return this.authTime;
/*      */     }
/* 1375 */     throw new GSSException(16, -1, "Inquire type not supported.");
/*      */   }
/*      */ 
/*      */   public void setTktFlags(boolean[] paramArrayOfBoolean)
/*      */   {
/* 1385 */     this.tktFlags = paramArrayOfBoolean;
/*      */   }
/*      */ 
/*      */   public void setAuthTime(String paramString) {
/* 1389 */     this.authTime = paramString;
/*      */   }
/*      */ 
/*      */   public void setAuthzData(AuthorizationDataEntry[] paramArrayOfAuthorizationDataEntry) {
/* 1393 */     this.authzData = paramArrayOfAuthorizationDataEntry;
/*      */   }
/*      */ 
/*      */   static class KerberosSessionKey
/*      */     implements Key
/*      */   {
/*      */     private final EncryptionKey key;
/*      */ 
/*      */     KerberosSessionKey(EncryptionKey paramEncryptionKey)
/*      */     {
/* 1326 */       this.key = paramEncryptionKey;
/*      */     }
/*      */ 
/*      */     public String getAlgorithm()
/*      */     {
/* 1331 */       return Integer.toString(this.key.getEType());
/*      */     }
/*      */ 
/*      */     public String getFormat()
/*      */     {
/* 1336 */       return "RAW";
/*      */     }
/*      */ 
/*      */     public byte[] getEncoded()
/*      */     {
/* 1341 */       return (byte[])this.key.getBytes().clone();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1346 */       return "Kerberos session key: etype: " + this.key.getEType() + "\n" + new HexDumpEncoder().encodeBuffer(this.key.getBytes());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.Krb5Context
 * JD-Core Version:    0.6.2
 */