package sun.net.idn;

/** @deprecated */
class UCharacterEnums
{
  /** @deprecated */
  public static abstract interface ECharacterCategory
  {
    public static final int UNASSIGNED = 0;
    public static final int GENERAL_OTHER_TYPES = 0;
    public static final int UPPERCASE_LETTER = 1;
    public static final int LOWERCASE_LETTER = 2;
    public static final int TITLECASE_LETTER = 3;
    public static final int MODIFIER_LETTER = 4;
    public static final int OTHER_LETTER = 5;
    public static final int NON_SPACING_MARK = 6;
    public static final int ENCLOSING_MARK = 7;
    public static final int COMBINING_SPACING_MARK = 8;
    public static final int DECIMAL_DIGIT_NUMBER = 9;
    public static final int LETTER_NUMBER = 10;
    public static final int OTHER_NUMBER = 11;
    public static final int SPACE_SEPARATOR = 12;
    public static final int LINE_SEPARATOR = 13;
    public static final int PARAGRAPH_SEPARATOR = 14;
    public static final int CONTROL = 15;
    public static final int FORMAT = 16;
    public static final int PRIVATE_USE = 17;
    public static final int SURROGATE = 18;
    public static final int DASH_PUNCTUATION = 19;
    public static final int START_PUNCTUATION = 20;
    public static final int END_PUNCTUATION = 21;
    public static final int CONNECTOR_PUNCTUATION = 22;
    public static final int OTHER_PUNCTUATION = 23;
    public static final int MATH_SYMBOL = 24;
    public static final int CURRENCY_SYMBOL = 25;
    public static final int MODIFIER_SYMBOL = 26;
    public static final int OTHER_SYMBOL = 27;
    public static final int INITIAL_PUNCTUATION = 28;

    /** @deprecated */
    public static final int INITIAL_QUOTE_PUNCTUATION = 28;
    public static final int FINAL_PUNCTUATION = 29;

    /** @deprecated */
    public static final int FINAL_QUOTE_PUNCTUATION = 29;
    public static final int CHAR_CATEGORY_COUNT = 30;
  }

  /** @deprecated */
  public static abstract interface ECharacterDirection
  {
    public static final int LEFT_TO_RIGHT = 0;

    /** @deprecated */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;

    /** @deprecated */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
    public static final int EUROPEAN_NUMBER = 2;

    /** @deprecated */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 2;
    public static final int EUROPEAN_NUMBER_SEPARATOR = 3;

    /** @deprecated */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 3;
    public static final int EUROPEAN_NUMBER_TERMINATOR = 4;

    /** @deprecated */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 4;
    public static final int ARABIC_NUMBER = 5;

    /** @deprecated */
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 5;
    public static final int COMMON_NUMBER_SEPARATOR = 6;

    /** @deprecated */
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 6;
    public static final int BLOCK_SEPARATOR = 7;

    /** @deprecated */
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 7;
    public static final int SEGMENT_SEPARATOR = 8;

    /** @deprecated */
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 8;
    public static final int WHITE_SPACE_NEUTRAL = 9;

    /** @deprecated */
    public static final byte DIRECTIONALITY_WHITESPACE = 9;
    public static final int OTHER_NEUTRAL = 10;

    /** @deprecated */
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 10;
    public static final int LEFT_TO_RIGHT_EMBEDDING = 11;

    /** @deprecated */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 11;
    public static final int LEFT_TO_RIGHT_OVERRIDE = 12;

    /** @deprecated */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 12;
    public static final int RIGHT_TO_LEFT_ARABIC = 13;

    /** @deprecated */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 13;
    public static final int RIGHT_TO_LEFT_EMBEDDING = 14;

    /** @deprecated */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 14;
    public static final int RIGHT_TO_LEFT_OVERRIDE = 15;

    /** @deprecated */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 15;
    public static final int POP_DIRECTIONAL_FORMAT = 16;

    /** @deprecated */
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 16;
    public static final int DIR_NON_SPACING_MARK = 17;

    /** @deprecated */
    public static final byte DIRECTIONALITY_NON_SPACING_MARK = 17;
    public static final int BOUNDARY_NEUTRAL = 18;

    /** @deprecated */
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 18;
    public static final int CHAR_DIRECTION_COUNT = 19;

    /** @deprecated */
    public static final byte DIRECTIONALITY_UNDEFINED = -1;
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.idn.UCharacterEnums
 * JD-Core Version:    0.6.2
 */