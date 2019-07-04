package com.parent.o2o.utils.string;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class StrUtils {

	private StrUtils() {};

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.isEmpty(null)      = true
	 * StrUtils.isEmpty("")        = true
	 * StrUtils.isEmpty(" ")       = false
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static Boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.isNotEmpty(null)      = false
	 * StrUtils.isNotEmpty("")        = false
	 * StrUtils.isNotEmpty(" ")       = true
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static Boolean isNotEmpty(String str) {
		return !StrUtils.isEmpty(str);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String a = null;
	 * String b = "";
	 * String c = "";
	 * String d = "";
	 * StrUtils.isEmptyAll(a,b,c,d)        = true
	 * </pre> 
	   
	 *********************************************************
	 */
	public static Boolean isEmptyAll(String... arges) {
		for (String arge : arges) {
			if (isNotEmpty(arge)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ********************************************************<br>
	 * <pre>
	 * String a = "1";
	 * String b = "1";
	 * String c = "1";
	 * String d = "1";
	 * StrUtils.isNotEmptyAll(a,b,c,d)        = true
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static Boolean isNotEmptyAll(String... arges) {
		for (String arge : arges) {
			if (isEmpty(arge)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.isBlank(null)      = true
	 * StrUtils.isBlank("")        = true
	 * StrUtils.isBlank(" ")       = true
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.isNotBlank(null)      = false
	 * StrUtils.isNotBlank("")        = false
	 * StrUtils.isNotBlank(" ")       = false
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static boolean isNotBlank(String str) {
		return !StrUtils.isBlank(str);
	}
	
	/**
	 * ********************************************************<br>
	 * <pre>
	 * String a = null;
	 * String b = "";
	 * String c = " ";
	 * String d = "";
	 * StrUtils.isBlankAll(a,b,c,d)        = true
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static Boolean isBlankAll(String... arges) {
		for (String arge : arges) {
			if (isNotBlank(arge)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ********************************************************<br>
	 * <pre>
	 * String a = "1";
	 * String b = "1";
	 * String c = "1";
	 * String d = "1";
	 * StrUtils.isNotBlankAll(a,b,c,d)        = true
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static Boolean isNotBlankAll(String... arges) {
		for (String arge : arges) {
			if (isBlank(arge)) {
				return false;
			}
		}
		return true;
	}

	public static String trim(String str) {
		return StrUtils.trim(str, null);
	}

	public static String trim(String str, Boolean isToNull) {
		if (null == isToNull) {
			return StringUtils.trim(str);
		} else if (isToNull) {
			return StringUtils.trimToNull(str);
		} else {
			return StringUtils.trimToEmpty(str);
		}
	}

	public static String strip(String str) {
		return StrUtils.strip(str, null);
	}

	public static String strip(String str, Boolean isToNull) {
		if (null == isToNull) {
			return StringUtils.strip(str);
		} else if (isToNull) {
			return StringUtils.stripToNull(str);
		} else {
			return StringUtils.stripToEmpty(str);
		}
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.abbreviate("abcdefg", 6) = "abc..."
	 * StrUtils.abbreviate("abcdefg", 7) = "abcdefg"
	 * StrUtils.abbreviate("abcdefg", 8) = "abcdefg"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String abbreviate(String str, int maxWidth) {
		return StringUtils.abbreviate(str, maxWidth);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
     * StrUtils.left("abc", 2)   = "ab"
     * StrUtils.left("abc", 4)   = "abc"
     * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String left(String str, int len) {
		return StringUtils.left(str, len);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
     * StrUtils.right("abc", 2)   = "bc"
     * StrUtils.right("abc", 4)   = "abc"
     * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String right(String str, int len) {
		return StringUtils.right(str, len);
	}

	public static String mid(String str, int pos, int len) {
		return StringUtils.mid(str, pos, len);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.substringBefore("abcba", "b") = "a"
	 * StrUtils.substringBefore("abc", "c")   = "ab"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String substringBefore(String str, String separator) {
		return StringUtils.substringBefore(str, separator);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.substringBeforeLast("abcba", "b") = "abc"
	 * StrUtils.substringBeforeLast("abc", "c")   = "ab"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String substringBeforeLast(String str, String separator) {
		return StringUtils.substringBeforeLast(str, separator);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * StrUtils.substringBeforeLastPoint("1.2") = "1"
	 * StrUtils.substringBeforeLastPoint("a.jpg")   = "a"
	 * </pre>
	   
	 *********************************************************
	 */
	public static String substringBeforeLastPoint(String str) {
		return StrUtils.substringBeforeLast(str, ".");
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * StrUtils.substringBeforeLastSlash("f:/1/2") = "f:/1"
	 * </pre> 
	   
	 *********************************************************
	 */
	public static String substringBeforeLastSlash(String str) {
		return StrUtils.substringBeforeLast(str, "/");
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * StrUtils.substringBeforeLastBackSlash("f:\\1\\2") = "f:\\1"
	 * </pre>
	   
	 *********************************************************
	 */
	public static String substringBeforeLastBackSlash(String str) {
		return StrUtils.substringBeforeLast(str, "\\");
	}
	
	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.substringAfter("abc", "a")   = "bc"
	 * StrUtils.substringAfter("abcba", "b") = "cba"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String substringAfter(String str, String separator) {
		return StringUtils.substringAfter(str, separator);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.substringAfterLast("abc", "a")   = "bc"
	 * StrUtils.substringAfterLast("abcba", "b") = "a"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String substringAfterLast(String str, String separator) {
		return StringUtils.substringAfterLast(str, separator);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.substringBetween("wx[b]yz", "[", "]") = "b"
	 * StrUtils.substringBetween("a111ba222b", "a", "b")   = "111"
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String substringBetween(String str, String open, String close) {
		return StringUtils.substringBetween(str, open, close);
	}

	/**
	 * ********************************************************<br>
	 * <pre>
	 * StrUtils.split("a,b,c,d", ",") = ["a", "b", "c", "d"]
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String[] split(String str, String separatorChars) {
		return StringUtils.split(str, separatorChars);
	}

    public static String substringByChar(String str, int length) {
        int count = 0;
        int offset = 0;
        if (isEmpty(str)) {
            return "";
        }

        if (str.getBytes().length <= length) {
            return str;
        }

        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] > 256) {
                offset = 2;
                count += 2;
            } else {
                offset = 1;
                count++;
            }
            if (count == length) {
                return str.substring(0, i + 1);
            }
            if ((count == length + 1 && offset == 2)) {
                return str.substring(0, i);
            }
        }
        return "";
    }
    
	/**
	 * ********************************************************<br>
	 * <pre>
	 * String a = "111%s222%s";
	 * StrUtils.format(a, "a", "b") = 111a222b
	 * </pre>
	 * 
	 ********************************************************* 
	 */
	public static String format(String str, Object... args) {
		return String.format(str, args);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String str = "1,123,1001,444";
	 * StrUtils.IsIn(str, "444") = true
	 * StrUtils.IsIn(str, "4244") = false
	 * </pre> 
	   
	 *********************************************************
	 */
	public static Boolean IsIn(String str, String searchStr) {
		if (StringUtils.indexOf(str, searchStr) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String a = "123";
	 * StrUtils.leftPad(a, "0", 5) = 00123
	 * </pre>
	   
	 *********************************************************
	 */
	public static String leftPad(String str, String padStr, int size) {
		return StringUtils.leftPad(str, size, padStr);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String a = "123";
	 * StrUtils.leftPadZero(a, 5) = 00123
	 * </pre>
	   
	 *********************************************************
	 */
	public static String leftPadZero(String str, int size) {
		return StrUtils.leftPad(str, "0", size);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String a = "123";
	 * StrUtils.rightPad(a, "0", 5) = 12300
	 * </pre> 
	   
	 *********************************************************
	 */
	public static String rightPad(String str, String padStr, int size) {
		return StringUtils.rightPad(str, size, padStr);
	}
	
	/**
	 * ********************************************************<br>      
	 * <pre>
	 * String a = "123";
	 * StrUtils.rightPadZero(a, 5) = 12300
	 * </pre>
	   
	 *********************************************************
	 */
	public static String rightPadZero(String str, int size) {
		return StrUtils.rightPad(str, "0", size);
	}
	
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	public static String append(String... arges) {
		StringBuilder strAppend = new StringBuilder("");
		for (String arge : arges) {
			if (isNotBlank(arge)) {
				strAppend.append(arge);
			}
		}
		return strAppend.toString();
	}

}

