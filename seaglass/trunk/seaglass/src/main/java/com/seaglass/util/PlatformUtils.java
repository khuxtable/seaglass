/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Aqvavit Pluggable Look and Feel.
 *
 * Aqvavit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * Aqvavit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Aqvavit.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.util;

/**
 * Platform utilities to determine anything we need to do differently on a
 * platform or JRE basis.
 * 
 * Based on MacWidgets for Java by Ken Orr.
 * 
 * @author Ken Orr
 */
public class PlatformUtils {

    private static final String SEA_GLASS_OVERRIDE_OS_NAME = "SeaGlass.Override.os.name";

    private PlatformUtils() {
        // utility class - no constructor needed.
    }

    /**
     * Get's the version of Java currently running.
     * 
     * @return the version of Java that is running.
     */
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Gets the operating system version that the JVM is running on.
     * 
     * @return the operating system version that the JVM is running on.
     */
    public static String getOsVersion() {
        return System.getProperty("os.version");
    }

    /**
     * True if this JVM is running on a Mac.
     * 
     * @return true if this JVM is running on a Mac.
     */
    public static boolean isMac() {
        if (System.getProperty(SEA_GLASS_OVERRIDE_OS_NAME) != null) {
            return System.getProperty(SEA_GLASS_OVERRIDE_OS_NAME).startsWith("Mac OS");
        }
        return System.getProperty("os.name").startsWith("Mac OS");
    }

    /**
     * True if this JVM is running Java 6 on a Mac.
     * 
     * @return true if this JVM is running Java 6 on a Mac.
     */
    public static boolean isJava6OnMac() {
        return isMac() && getJavaVersion().startsWith("1.6");
    }

    /**
     * True if this JVM is running 64 bit Java on a Mac.
     * 
     * @return true if this JVM is running 64 bit Java on a Mac.
     */
    public static boolean is64BitJavaOnMac() {
        return isMac() && System.getProperty("os.arch").equals("x86_64");
    }

    /**
     * True if this JVM is running on Mac OS X 10.5, Leopard.
     * 
     * @return true if this JVM is running on Mac OS X 10.5, Leopard.
     */
    public static boolean isLeopard() {
        return isMac() && getOsVersion().startsWith("10.5");
    }

    /**
     * {@code true} if the Unified Tool Bar, Preference Tool Bar or Bottom Bar
     * backgrounds should be manually painted in code, rather than letting Mac
     * OS X do the painting. This will always return true on platforms other
     * than Mac, and will sometimes return true on Mac's due to painting bugs in
     * the Java distribution.
     */
    public static boolean shouldManuallyPaintTexturedWindowBackground() {
        boolean shouldManuallyPaintOnMac = isMac() && isLeopard() && isJava6OnMac();
        return !isMac() || shouldManuallyPaintOnMac;
    }
}
