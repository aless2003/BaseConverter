package online.hatsunemiku.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean run = true;
    while (run) {
      System.out.println(
          "Enter two numbers in format: {source base} {target base} (To quit type /exit)");
      String[] bases = scanner.nextLine().split(" ");
      if (bases[0].equals("/exit")) {
        run = false;
      } else {
        int sourceBase = Integer.parseInt(bases[0]);
        int targetBase = Integer.parseInt(bases[1]);
        conversionDialog(sourceBase, targetBase);
      }
    }
  }

  public static void conversionDialog(int sourceBase, int targetBase) {
    String template = "Enter number in base %d to convert to base %d (To go back type /back)";
    boolean run = true;
    Scanner scanner = new Scanner(System.in);
    while (run) {
      String output = String.format(template, sourceBase, targetBase);
      System.out.println(output);
      String number = scanner.nextLine();
      if ("/back".equals(number)) {
        run = false;
      } else {
        String result;
        if (number.contains(".")) {
          result = convertFractional(number, sourceBase, targetBase);
        } else {
          result = convert(number, sourceBase, targetBase);
        }
        System.out.println("Conversion result: " + result);
        System.out.println();
      }
      scanner = new Scanner(System.in);
    }
  }

  private static String convertFractional(String number, int sourceBase, int targetBase) {
    String[] parts = number.split("\\.");
    String integerPart = parts[0];
    String fractionalPart = parts[1];
    String integerResult = convert(integerPart, sourceBase, targetBase);
    String fractionalResult = convertFractionalPart(fractionalPart, sourceBase, targetBase);
    return integerResult + "." + fractionalResult;
  }

  private static String convertFractionalPart(String sourceNumber, int sourceBase, int targetBase) {
    BigDecimal fraction = new BigDecimal("0");
    for (int i = 0; i < sourceNumber.length(); i++) {
      int digit = Character.getNumericValue(sourceNumber.charAt(i));
      BigDecimal decimalDigit = new BigDecimal(digit);
      decimalDigit = decimalDigit.multiply(new BigDecimal(sourceBase).pow(-i - 1, MathContext.DECIMAL128));
      fraction = fraction.add(decimalDigit);
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      fraction = fraction.multiply(new BigDecimal(targetBase));
      char digit = Character.forDigit(fraction.intValue(), targetBase);
      result.append(digit);
      fraction = fraction.subtract(new BigDecimal(fraction.intValue()));
    }
    return result.toString();
  }

  private static String convert(String number, int sourceBase, int targetBase) {
    BigInteger source = new BigInteger(number, sourceBase);
    return source.toString(targetBase);
  }
}
