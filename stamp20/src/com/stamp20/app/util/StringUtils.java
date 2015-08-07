package com.stamp20.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    public static String spaceToHyphen(String before) {
        String alphanumeric = before.replaceAll("[^A-Za-z0-9]", "_");
        return "D_" + alphanumeric;
    }

    public static boolean validateEmail(final String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email != null && email.matches(EMAIL_PATTERN);
    }

    public static boolean isEmptyString(final String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String color2String(int intColor) {
        return String.format("#%06X", 0xFFFFFF & intColor);
    }

    public static int indexInArray(String[] array, String target) {
        int result = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static String centIntegerToDollorString(int cents, boolean freeAsText) {
        if (cents == 0 && freeAsText) {
            return "free";
        } else {
            double priceDouble = cents / 100.00;
            return "$ " + String.format("%1$.2f", priceDouble);
        }

    }

    public static String dateToString(Date d) {
        SimpleDateFormat f = new SimpleDateFormat("d MMM yyyy");
        return f.format(d);
    }

    public static boolean hasAnyPrefix(String number, String... prefixes) {
        if (number == null) {
            return false;
        }
        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidZip(String zip) {
        final String regex = "^\\d{5}(-\\d{4})?$";
        return zip.matches(regex);
    }

    public static int getIndexFromList(List olist, String ostring) {
        int result = 0;
        for (int i = 0; i < olist.size(); i++) {
            if (olist.get(i).toString().equals(ostring)) {
                result = i;
                break;
            }
        }
        return result;
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Removes a substring only if it is at the beginning of a source string,
     * otherwise returns the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} search string
     * will return the source string.
     * </p>
     *
     * <pre>
     * StringUtils.removeStart(null, *)      = null
     * StringUtils.removeStart("", *)        = ""
     * StringUtils.removeStart(*, null)      = *
     * StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
     * StringUtils.removeStart("domain.com", "www.")       = "domain.com"
     * StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeStart("abc", "")    = "abc"
     * </pre>
     *
     * @param str
     *            the source String to search, may be null
     * @param remove
     *            the String to search for and remove, may be null
     * @return the substring with the string removed if found, {@code null} if
     *         null String input
     * @since 2.1
     */
    public static String removeStart(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    /**
     * <p>
     * Checks if a CharSequence is empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * CharSequence. That functionality is available in isBlank().
     * </p>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to
     *        isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to
     *        isBlank(CharSequence)
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
