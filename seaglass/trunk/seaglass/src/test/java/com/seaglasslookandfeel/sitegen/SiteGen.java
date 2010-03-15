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
package com.seaglasslookandfeel.sitegen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * Generate image files as described in the input file for use in documentation.
 *
 * @author Kathryn Huxtable
 */
public class SiteGen {

    private File   outputDirectory;
    private JPanel panel;

    /**
     * Creates a new SiteGen object.
     *
     * @param outputDirectory the directory in which to create the image files.
     */
    public SiteGen(File outputDirectory) {
        this.outputDirectory = outputDirectory;

        setSeaGlassLookAndFeel();

        panel = new JPanel();
        panel.setOpaque(true);
    }

    /**
     * Create a new SeaGlassLookAndFeel and set it as the current look and feel.
     */
    private void setSeaGlassLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an image from the info and write it to a file.
     *
     * @param filename   the filename for the image.
     * @param className  the class of control to be created, e.g.
     *                   "javax.swing.JButton".
     * @param width      the desired width of the control.
     * @param height     the desired height of the control.
     * @param args       any arguments to the control constructor.
     * @param properties a map containing the client properties to set on the
     *                   control.
     */
    private void drawImage(String filename, String className, int width, int height, Object[] args, Map<String, Object> properties) {
        JComponent c = createSwingObject(className, args);

        for (String key : properties.keySet()) {
            c.putClientProperty(key, properties.get(key));
        }

        BufferedImage image = paintToBufferedImage(c, width, height);

        writeImageFile(filename, image);
    }

    /**
     * Create a Swing object from its class name and arguments.
     *
     * @param  className the class name.
     * @param  args      the arguments. May be empty.
     *
     * @return the newly created Swing object.
     */
    private JComponent createSwingObject(String className, Object... args) {
        try {
            Class c = Class.forName(className);

            Class[] argClasses = new Class[args.length];

            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }

            Constructor constructor = c.getConstructor(argClasses);

            if (constructor == null) {
                throw new NullPointerException("Failed to find the constructor for the class: " + className);
            }

            return (JComponent) constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Paint the control to a newly created buffered image.
     *
     * @param  c      the control to paint.
     * @param  width  the desired width of the control.
     * @param  height the desired height of the control.
     *
     * @return the buffered image containing the printed control against a panel
     *         background.
     */
    private BufferedImage paintToBufferedImage(JComponent c, int width, int height) {
        panel.removeAll();
        panel.setSize(width, height);

        panel.add(c);
        c.setBounds(0, 0, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics      g     = image.createGraphics();

        panel.paint(g);
        return image;
    }

    /**
     * Write the buffered image to the file.
     *
     * @param filename the filename portion of the file. The directory and the
     *                 extension will be added.
     * @param image    the buffered image.
     */
    private void writeImageFile(String filename, BufferedImage image) {
        try {
            File outputfile = new File(outputDirectory, filename + ".png");

            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Static Methods -------------------------------------------------

    /**
     * Main method.
     *
     * @param args command line arguments: xml-file output-directory
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: SiteGen configfile outputdirectory");
            System.exit(1);
        }

        InputStream configStream    = getConfigFileStream(args[0]);
        File        outputDirectory = getOutputDirectory(args[1]);

        SiteGen siteGen = new SiteGen(outputDirectory);

        Document doc = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder        db  = dbf.newDocumentBuilder();

            doc = db.parse(configStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        doc.getDocumentElement().normalize();
        NodeList  nodeList = doc.getElementsByTagName("image");
        ImageInfo info     = new ImageInfo();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node imageNode = nodeList.item(i);

            if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
                getImageInfo((Element) imageNode, info);
                siteGen.drawImage(info.filename, info.className, info.width, info.height, info.args, info.properties);
            }

        }
    }

    /**
     * Create an input stream from the filename.
     *
     * @param  filename the file to create a stream from.
     *
     * @return the input stream.
     */
    private static InputStream getConfigFileStream(String filename) {
        try {
            return new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open config file \"" + filename + "\": " + e.getLocalizedMessage());
            System.exit(1);
        }

        return null;
    }

    /**
     * Get the File for the output directory, creating it if necessary.
     *
     * @param  outputDirectoryName the directory name.
     *
     * @return the File.
     */
    private static File getOutputDirectory(String outputDirectoryName) {
        File outputDirectory = new File(outputDirectoryName);

        if (outputDirectory.exists()) {
            if (!outputDirectory.isDirectory()) {
                System.err.println("Output directory \"" + outputDirectoryName + "\" exists, but is not a directory.");
                System.exit(1);
            } else if (!outputDirectory.canWrite()) {
                System.err.println("Output directory \"" + outputDirectoryName + "\" exists, but is not writable.");
                System.exit(1);
            }
        } else if (!outputDirectory.mkdirs()) {
            System.err.println("Output directory \"" + outputDirectoryName + "\" could not be created.");
            System.exit(1);
        }

        return outputDirectory;
    }

    /**
     * Parse the XML for an image element to create the ImageInfo class for it.
     *
     * @param  imageElem the W3C Element containing the image information.
     * @param  info      an ImageInfo object into which to parse the
     *                   information.
     *
     * @return the ImageInfo class containing the information.
     */
    private static ImageInfo getImageInfo(Element imageElem, ImageInfo info) {
        info.filename  = imageElem.getAttribute("file");
        info.className = imageElem.getAttribute("class");
        info.width     = Integer.parseInt(imageElem.getAttribute("width"));
        info.height    = Integer.parseInt(imageElem.getAttribute("height"));

        List<Object> argList = new ArrayList<Object>();

        NodeList nodeList = imageElem.getElementsByTagName("argument");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element argElem = (Element) node;
                String  type    = argElem.getAttribute("type");
                String  value   = argElem.getAttribute("value");

                argList.add(getObject(type, value));
            }
        }

        info.args = argList.toArray();

        info.properties = new HashMap<String, Object>();

        nodeList = imageElem.getElementsByTagName("clientProperty");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element argElem = (Element) node;
                String  name    = argElem.getAttribute("name");
                String  type    = argElem.getAttribute("type");
                String  value   = argElem.getAttribute("value");

                info.properties.put(name, getObject(type, value));
            }
        }

        info.args = argList.toArray();

        return info;
    }

    /**
     * Get the value as an Object, given its type and value.
     *
     * @param  type  the type, e.g. String, Integer, Float, or Double.
     * @param  value the value.
     *
     * @return the value as an Object of the specified type.
     */
    private static Object getObject(String type, String value) {
        Object obj = null;

        if ("String".equals(type)) {
            obj = value;
        } else if ("Integer".equals(type)) {
            obj = Integer.parseInt(value);
        } else if ("Float".equals(type)) {
            obj = Float.parseFloat(value);
        } else if ("Double".equals(type)) {
            obj = Double.parseDouble(value);
        } else {
            System.err.println("Unknown argument type: " + type);
        }

        return obj;
    }

    /**
     * Information used to generate each image.
     */
    public static class ImageInfo {
        String              filename;
        String              className;
        int                 width;
        int                 height;
        Object[]            args;
        Map<String, Object> properties;
    }
}
