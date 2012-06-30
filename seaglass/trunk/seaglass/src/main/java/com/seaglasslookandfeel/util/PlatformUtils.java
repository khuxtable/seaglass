/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */
package com.seaglasslookandfeel.util;

/**
 * Platform utilities to determine anything we need to do differently on a
 * platform or JRE basis.
 *
 * <p>Based on MacWidgets for Java by Ken Orr.</p>
 *
 * @author Ken Orr
 */
public class PlatformUtils {

    private static final String SEA_GLASS_OVERRIDE_OS_NAME = "SeaGlass.Override.os.name";

    /**
     * Creates a new PlatformUtils object.
     */
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
     * True if this JVM is running on Windows.
     *
     * @return true if this JVM is running on Windows.
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
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
        return isMac() && isJava6();
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
     * True if this JVM is running on Mac OS X 10.6, Snow Leopard.
     *
     * @return true if this JVM is running on Mac OS X 10.6, Snow Leopard.
     */
    public static boolean isSnowLeopard() {
        return isMac() && getOsVersion().startsWith("10.6");
    }
    
    /**
     * True if this JVM is running on Mac OS X 10.7, Lion.
     *
     * @return true if this JVM is running on Mac OS X 10.7, Lion.
     */
    public static boolean isLion() {
        return isMac() && getOsVersion().startsWith("10.7");
    }

    /**
     * @return true if we run on java 6 else we should be on Java 7 or newer
     */
    public static boolean isJava6() {
        return getJavaVersion().startsWith("1.6");
    }
}
