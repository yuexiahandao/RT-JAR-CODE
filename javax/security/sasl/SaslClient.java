package javax.security.sasl;

public abstract interface SaslClient
{
  public abstract String getMechanismName();

  public abstract boolean hasInitialResponse();

  public abstract byte[] evaluateChallenge(byte[] paramArrayOfByte)
    throws SaslException;

  public abstract boolean isComplete();

  public abstract byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SaslException;

  public abstract byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SaslException;

  public abstract Object getNegotiatedProperty(String paramString);

  public abstract void dispose()
    throws SaslException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.sasl.SaslClient
 * JD-Core Version:    0.6.2
 */