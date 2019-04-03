package org.riggvar.js08;

import java.net.*;
import java.text.*;
import java.util.*;

import java.awt.*;

/**
 * all static class of generic utilities
 **/
class Util {
    public static final boolean DO_TRACE = true;
    public static final boolean NO_TRACE = false;

    public static String sDumpTitle = null;

    public static void setDumpTitle(String title) {
        sDumpTitle = title;
    }

    public static BaseObject sDumpObject = null;

    public static void setDumpObject(BaseObject object) {
        sDumpObject = object;
    }

    /**
     * Pops up a confirmation dialog that requires user to type the word Yes into a
     * text field in order to confirm the question asked.
     * 
     * @param message The message to be asked of the user. Should be worded for a
     *                yes/no answer.
     * @returns true if user replied 'yes' or false if they gave any other answer
     */
    public static boolean confirm(String message) {
        return confirm(message, true);
    }

    /**
     * Pops up a confirmation dialog that requires user to type the word Yes into a
     * text field in order to confirm the question asked.
     * 
     * @param message   The message to be asked of the user. Should be worded for a
     *                  yes/no answer.
     * @param typeYesNo When true, will force the user to actually type "Yes"
     *                  instead of simply hitting a Yes button
     * @returns true if user replied 'yes' or false if they gave any other answer
     */
    public static boolean confirm(String message, boolean typeYesNo) {
        return false;
    }

    /**
     * Shows an error message from a source object including a stack trace
     * 
     * @param source  the object generating the error
     * @param message the string of the error message
     */
    public static void showError(Object source, String message) {
        showError(source, message, DO_TRACE, null);
    }

    /**
     * Shows an error message from a source exception, includes an option to display
     * (or not) a stack trace
     * 
     * @param source    the exception to be displayed
     * @param wantTrace a boolean true if a stack trace should be displayed, false
     *                  if not
     */
    public static void showError(Exception source, boolean wantTrace) {
        showError(source, "Exception", wantTrace, null);
    }

    /**
     * Shows an error message from a source object, with a title message and an
     * option to display (or not) a stack trace
     * 
     * @param source    the exception or object that is the source of the error
     * @param message   the string of the error message
     * @param wantTrace a boolean true if a stack trace should be displayed, false
     *                  if not
     */
    public static void showError(Object source, String message, boolean wantTrace) {
        showError(source, message, wantTrace, true, sDumpObject, sDumpTitle, null);
    }

    /**
     * Shows an error message from a source object, with a title message and an
     * option to display (or not) a stack trace
     * 
     * @param source      the exception or object that is the source of the error
     * @param message     the string of the error message
     * @param wantTrace   a boolean true if a stack trace should be displayed, false
     *                    if not
     * @param secondStack a second stack trace to be added to the errors
     */
    public static void showError(Object source, String message, boolean wantTrace, Throwable secondStack) {
        showError(source, message, wantTrace, true, sDumpObject, sDumpTitle, secondStack);
    }

//    private static boolean sTesting = false;
//    public static void setTesting(boolean b)
//    {
//        sTesting = b;
//    }

    /**
     * Shows an error message from a source object, with a title message and an
     * option to display (or not) a stack trace
     * 
     * @param source      the exception or object that is the source of the error
     * @param message     the string of the error message
     * @param wantTrace   a boolean true if a stack trace should be displayed, false
     *                    if not
     * @param dumpOption  boolean, when true creates a "core dump"
     * @param dumpObject  when dumpOption is true, this is an optional Object whose
     *                    contents will
     * @param dumpTitle   a title string, expected to be a program name and version
     * @param secondStack a second stack to be shown in error message be included in
     *                    the "core dump"
     */
    private static void showError(Object source, String message, boolean wantTrace, boolean dumpOption,
            Object dumpObject, String dumpTitle, Throwable secondStack) {
    }

    /**
     * quick and dirty error message to standard output.
     * 
     * @param source  the source of the problem
     * @param e       the exception encountered
     * @param doTrace true if you want a stack trace
     */
    public static void printlnException(Object source, Exception e, boolean doTrace) {
        System.out.println(e.toString() + " encountered in " + source.toString());
        if (doTrace) {
            showStack(e);
        }
    }

    /**
     * prints a stack trace of a Throwable to standard output
     * 
     * @param t the Throwable instance to trace
     */
    public static void showStack(Throwable t) {
        t.printStackTrace(System.out);
    }

    /**
     * prints a stack trace of current location (creates a Throwable() on the fly)
     */
    public static void showStack() {
        showStack(new Throwable());
    }

    /**
     * given any component, returns a location for the top left corner of the
     * component that will center it on the screen
     * 
     * @param comp the Component to find a center for
     * @return Point the point at which component would be centered
     */
    public static Point getLocationToCenterOnScreen(Component comp) {
        Dimension screenDim = comp.getToolkit().getScreenSize();
        Dimension compDim = comp.getSize();
        return new Point(Math.max(0, (screenDim.width - compDim.width) / 2),
                Math.max(0, (screenDim.height - compDim.height) / 2));
    }

    /**
     * Useful for debugging output, returns a String containing the class and
     * hashcode of the specified object. The class name has the SEA package prefix
     * stripped off it if it begins with that.
     * 
     * @param obj
     * @return a string describing object's address and class name
     **/
    public static String getObjectIdString(Object obj) {
        String retval;
        if (obj == null) {
            retval = "<null>";
        } else {
            retval = obj.getClass().getName() + "/" + obj.hashCode();
        }
        return retval;
    }

    /**
     * Finds and loads an image based on a string input name. Looks first in JAR
     * file, if any, then on local disk, then local file
     * 
     * @param inComp component in which to load the image
     * @param inName the target filename of the image
     * @return the Image after loading it
     **/
    public static Image findImage(Component inComp, String inName) {
        boolean haveLocalAccess = true; // (isApplet() ? false : SeaUtilities.checkLocalReadAccess());
        Image retImg = null;

        // first try the Jar file:
        try {
            URL u = null;
            try {
                u = inComp.getClass().getResource(inName);
            } catch (NullPointerException e) {
            }
            if (u != null) {
                retImg = loadImage(inComp, u);
            }
        } catch (Exception e) {
        }

        if ((retImg == null) && (haveLocalAccess)) // not found in jar, try local disk
        {
            try {
                String fName = null;
                if (inName.startsWith("/")) {
                    fName = "." + inName;
                } else {
                    fName = inName;
                }
                retImg = loadImage(inComp, ClassLoader.getSystemResource(fName));
            } catch (Exception e) {
            }
        }

        return retImg;
    } // of findImage

    /**
     * tries to load image from Url. Returns null if input Url is null, or image
     * cannot be loaded
     *
     * @param inComp component in which to load the image
     * @param inUrl  the target URL of the image
     * @return the Image after loading it
     * @exception IllegalArgumentException if inComp is null
     **/
    public static Image loadImage(Component inComp, URL inUrl) {
        if (inComp == null) {
            // Must have valid Component for loading
            throw new IllegalArgumentException("loadImage called with null Component");
        }

        if (inUrl == null) {
            // No URL, so no Image
            return null;
        }

        Image loadedImage = null;

        try {
            Image tempImg = null;

            MediaTracker mt = new MediaTracker(inComp);
            tempImg = java.awt.Toolkit.getDefaultToolkit().getImage(inUrl);
            mt.addImage(tempImg, 0);
            mt.waitForID(0);

            loadedImage = tempImg;
        } catch (Exception e) {
            loadedImage = null;
        }

        return loadedImage;
    }

    /**
     * finds a parent frame for the specified container. If none is found it invents
     * one. Useful for creating new dialogs that insist on having a parent
     * 
     * @param theFrame the container whose parent is sought
     * @return the parent Frame of the container
     */
    public static Frame getParentFrame(Container theFrame) {
        do {
            theFrame = theFrame.getParent();
        } while ((theFrame != null) && !(theFrame instanceof Frame));
        if (theFrame == null) {
            theFrame = new Frame();
        }
        return (Frame) theFrame;
    }

    public static boolean equalsWithNull(Object left, Object right) {
        try {
            return left.equals(right);
        } catch (NullPointerException e) {
            return (right == null);
        }
    }

    public static boolean equalsIgnoreCaseWithNull(String left, String right) {
        try {
            return left.equalsIgnoreCase(right);
        } catch (NullPointerException e) {
            return (right == null);
        }
    }

    public static int compareWithNull(Date left, Date right) {
        if (left == null) {
            if (right == null) {
                return 0;
            } else {
                return 1;
            }
        } else if (right == null) {
            return -1;
        } else {
            if (left.before(right)) {
                return 1;
            } else if (left.after(right)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * checks the executing version of Java, displays a warning message if the
     * version is less than the specified argument a Warning and suggestion to
     * upgrade will be displayed
     * 
     * @param minVersion the minimum version to be allowed without a warning
     */
    public static void checkJreVersion(String minVersion) {
    }

    public static double parseDouble(String inStr) {
        return Double.parseDouble(inStr);
    }

    /**
     * Formats a double value to specified number of decimal places runs through the
     * internationalized NumberFormat, so may NOT do scientific notation
     *
     * @param inVal  the input value
     * @param inDecs the number of decimals places to be displayed
     * @return the string of the double
     */
    public static String formatDouble(double inVal, int inDecs) {
        return formatDouble(inVal, inDecs, 14);
    }

    /**
     * Formats a double value to specified number of decimal places runs through the
     * internationalized NumberFormat, so may NOT do scientific notation
     *
     * @param inVal       double number to be formatted
     * @param inDecs      integer of number of decimal places
     * @param inLeftOfDec integer of max number of places to left of decimal
     * @return the string of the double
     */
    public static String formatDouble(double inVal, int inDecs, int inLeftOfDec) {
        String returnVal = "";
        if (Double.isInfinite(inVal)) {
            returnVal = INFINITY;
        } else if (Double.isNaN(inVal)) {
            returnVal = NAN;
        } else {
            NumberFormat nf = NumberFormat.getNumberInstance();
            // NumberFormat nf = NumberFormat.getInstance( sLocale);
            nf.setMaximumIntegerDigits(inLeftOfDec);
            nf.setMinimumFractionDigits(inDecs);
            returnVal = nf.format(inVal);
        }

        return returnVal;
    }

    /**
     * Formats a double value to specified number of decimal places handles need (or
     * not) for scientific notation assumes default max length of 10, for no
     * particular reason Note: this does NOT do internationalized numbers.
     *
     * @param inVal  the input value
     * @param inDecs the number of decimals places to be displayed
     * @return the string of the double
     */
    public static String formatDoubleScientific(double inVal, int inDecs) {
        return formatDoubleScientific(inVal, inDecs, 14, false);
    }

    /**
     * Formats a double value to specified number of decimal places handles need (or
     * not) for scientific notation Note: this does NOT do internationalized
     * numbers.
     *
     * @param inVal       double number to be formatted
     * @param inDecs      integer of number of decimal places
     * @param inLeftOfDec integer of max number of places to left of decimal
     * @return the string of the double
     */
    public static String formatDoubleScientific(double inVal, int inDecs, int inLeftOfDec) {
        return formatDoubleScientific(inVal, inDecs, inLeftOfDec, false);
    }

    private static String INFINITY = Double.toString(Double.POSITIVE_INFINITY);
    private static String NAN = Double.toString(Double.NaN);

    /**
     * Formats a double value to specified number of decimal places handles need (or
     * not) for scientific notation
     * 
     * @param inVal       double number to be formatted
     * @param inDecs      integer of number of decimal places
     * @param inLeftOfDec integer of max number of places to left of decimal
     * @param recursing   true when the method is calling itself
     * @return the string of the double
     */
    protected static String formatDoubleScientific(double inVal, int inDecs, int inLeftOfDec, boolean recursing) {
        String returnVal = "";

        if (Double.isInfinite(inVal)) {
            returnVal = INFINITY;
        } else if (Double.isNaN(inVal)) {
            returnVal = NAN;
        } else if (inVal == 0) {
            StringBuffer sb = new StringBuffer("0");
            if (inDecs > 0) {
                sb.append(".");
            }
            for (int i = 0; i < inDecs; i++) {
                sb.append("0");
            }
            returnVal = sb.toString();
        } else {
            // don't let digits to left of decimal be less than 1
            inLeftOfDec = Math.max(inLeftOfDec, 1);

            int maxExp = inLeftOfDec - inDecs; // max 10 power before going to sci notation
            if (inDecs == 0) {
                maxExp++;
            }
            if (inVal < 0) {
                maxExp--;

            }
            int minExp = -inDecs; // min 10 power before going to sci notation

            boolean doSN = (Math.abs(inVal) > pow10(maxExp) ? true : (Math.abs(inVal) < pow10(minExp)));

            if (!doSN) {
                String ret = null;
                // not doing scientific notation
                if (inDecs == 0) {
                    ret = Long.toString(Math.round(inVal));
                } else {
                    double p10 = pow10(inDecs);
                    ret = Double.toString(Math.round(inVal * p10) / p10);
                    int dotLoc = ret.indexOf(".");
                    if (inDecs > 0 && dotLoc > 0) {
                        ret = ret.substring(0, Math.min(ret.length(), dotLoc + inDecs + 1));
                    }
                }
                return ret;
            } else {
                // dont let digits to left of decimal be less than 1
                inLeftOfDec = Math.max(inLeftOfDec, 1);

                // is scientific notation, still use recommended decimals
                int sign = 1;
                if (inVal < 0) {
                    sign = -1;
                    inVal = -inVal;
                }

                int p10 = 0;
                if (inVal != 0) {
                    p10 = (int) Math.floor(log10(inVal));

                }
                double adjVal = sign * inVal / pow10(p10);

                if (Double.isNaN(adjVal)) {
                    returnVal = NAN;
                } else {
                    // to avoid an endless loop
                    StringBuffer sb = new StringBuffer(((recursing) ? Double.toString(inVal)
                            : formatDoubleScientific(adjVal, inDecs, inLeftOfDec, true)));
                    sb.append("e");
                    sb.append(p10);
                    returnVal = sb.toString();
                }
            }
        }
        return returnVal;
    }

    // returns log based 10
    public static double log10(double inVal) {
        return Math.log(inVal) / Math.log(10);
    }

    public static double pow10(int n) {
        if (n == 0) {
            return 1;
        } else if (n > 0) {
            double d = 1;
            for (int i = 0; i < n; i++) {
                d = d * 10.;
            }
            return d;
        } else {
            double d = 1;
            for (int i = 0; i > n; i--) {
                d = d / 10.;
            }
            return d;
        }
    }

    /**
     * reads line from reader file and parses it on a delimiter and returns array of
     * cells. Generally used to split a comma separated set of strings into
     * individual components
     * 
     * @param line      the line to be split
     * @param delimiter the delimiter on which to split the string
     * @return array of split string elements
     */
    public static String[] stringSplit(String line, String delimiter) {
        return stringSplit(line, delimiter, null);
    }

    /**
     * reads line from reader file and parses it on a delimiter and returns array of
     * cells. Generally used to split a comma separated set of quotes into
     * individual components with the quotes removed
     * 
     * @param line      the line to be split
     * @param delimiter the delimiter on which to split the string
     * @param trimmer   to trim from each end of a string segment
     * @return array of split string elements
     */
    public static String[] stringSplit(String line, String delimiter, String trimmer) {
        ArrayList<String> v = new ArrayList<String>(10);
        int left = 0;
        int tloc = 0;
        while ((tloc = line.indexOf(delimiter, left)) >= 0) {
            String cell = trim(line.substring(left, tloc), trimmer);
            v.add(cell);
            left = tloc + 1;
        }

        if (left < line.length() - 1) {
            // have string after last delimiter
            String cell = trim(line.substring(left, line.length()), trimmer);
            v.add(cell);
        }

        String[] ret = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            ret[i] = v.get(i);
        }
        return ret;
    }

    /**
     * replaces all occurrences of 'key' in 'origString' with 'newName'.
     * 
     * @param origString the original string
     * @param key        the substring to be replaced
     * @param newName    the string to replace all occurrences of 'key' with
     * @return the revised String
     */
    public static String stringReplace(String origString, String key, String newName) {
        int rloc = origString.indexOf(key);
        if (rloc < 0) {
            return origString;
        }

        StringBuffer sb = new StringBuffer(origString.length() + newName.length());

        int lloc = 0;
        while (rloc > 0) {
            sb.append(origString.substring(lloc, rloc));
            sb.append(newName);
            lloc = rloc + key.length();
            rloc = origString.indexOf(key, lloc);
        }
        sb.append(origString.substring(lloc));
        String newString = sb.toString();
        return newString;
    }

    public static String trim(String orig, String trimmer) {
        String cell = orig;
        if (trimmer == null) {
            return cell;
        }

        if (cell.startsWith(trimmer)) {
            cell = cell.substring(trimmer.length(), cell.length());
        }
        if (cell.endsWith(trimmer)) {
            cell = cell.substring(0, cell.length() - trimmer.length());
        }
        return cell;
    }

}
