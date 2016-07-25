package org.ietf.jgss;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface GSSContext
{
  public static final int DEFAULT_LIFETIME = 0;
  public static final int INDEFINITE_LIFETIME = 2147483647;

  public abstract byte[] initSecContext(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws GSSException;

  public abstract int initSecContext(InputStream paramInputStream, OutputStream paramOutputStream)
    throws GSSException;

  public abstract byte[] acceptSecContext(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws GSSException;

  public abstract void acceptSecContext(InputStream paramInputStream, OutputStream paramOutputStream)
    throws GSSException;

  public abstract boolean isEstablished();

  public abstract void dispose()
    throws GSSException;

  public abstract int getWrapSizeLimit(int paramInt1, boolean paramBoolean, int paramInt2)
    throws GSSException;

  public abstract byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
    throws GSSException;

  public abstract void wrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
    throws GSSException;

  public abstract byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
    throws GSSException;

  public abstract void unwrap(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
    throws GSSException;

  public abstract byte[] getMIC(byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
    throws GSSException;

  public abstract void getMIC(InputStream paramInputStream, OutputStream paramOutputStream, MessageProp paramMessageProp)
    throws GSSException;

  public abstract void verifyMIC(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, MessageProp paramMessageProp)
    throws GSSException;

  public abstract void verifyMIC(InputStream paramInputStream1, InputStream paramInputStream2, MessageProp paramMessageProp)
    throws GSSException;

  public abstract byte[] export()
    throws GSSException;

  public abstract void requestMutualAuth(boolean paramBoolean)
    throws GSSException;

  public abstract void requestReplayDet(boolean paramBoolean)
    throws GSSException;

  public abstract void requestSequenceDet(boolean paramBoolean)
    throws GSSException;

  public abstract void requestCredDeleg(boolean paramBoolean)
    throws GSSException;

  public abstract void requestAnonymity(boolean paramBoolean)
    throws GSSException;

  public abstract void requestConf(boolean paramBoolean)
    throws GSSException;

  public abstract void requestInteg(boolean paramBoolean)
    throws GSSException;

  public abstract void requestLifetime(int paramInt)
    throws GSSException;

  public abstract void setChannelBinding(ChannelBinding paramChannelBinding)
    throws GSSException;

  public abstract boolean getCredDelegState();

  public abstract boolean getMutualAuthState();

  public abstract boolean getReplayDetState();

  public abstract boolean getSequenceDetState();

  public abstract boolean getAnonymityState();

  public abstract boolean isTransferable()
    throws GSSException;

  public abstract boolean isProtReady();

  public abstract boolean getConfState();

  public abstract boolean getIntegState();

  public abstract int getLifetime();

  public abstract GSSName getSrcName()
    throws GSSException;

  public abstract GSSName getTargName()
    throws GSSException;

  public abstract Oid getMech()
    throws GSSException;

  public abstract GSSCredential getDelegCred()
    throws GSSException;

  public abstract boolean isInitiator()
    throws GSSException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.GSSContext
 * JD-Core Version:    0.6.2
 */