package javax.smartcardio;

public abstract class CardTerminal
{
  public abstract String getName();

  public abstract Card connect(String paramString)
    throws CardException;

  public abstract boolean isCardPresent()
    throws CardException;

  public abstract boolean waitForCardPresent(long paramLong)
    throws CardException;

  public abstract boolean waitForCardAbsent(long paramLong)
    throws CardException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.CardTerminal
 * JD-Core Version:    0.6.2
 */