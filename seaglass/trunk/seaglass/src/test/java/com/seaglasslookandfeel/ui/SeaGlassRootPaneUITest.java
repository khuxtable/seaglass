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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.ui;

import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.util.PlatformUtils;

/**
 * DOCUMENT ME!
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassRootPaneUITest {

    /**
     * DOCUMENT ME!
     *
     * @throws Exception java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        try {
            UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    @Ignore // Needs an update
    public void testSimpleFrameSize() {
        JFrame frame = new JFrame();
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.CENTER));
        content.setBackground(new Color(250, 250, 250));
        content.setPreferredSize(new Dimension(400,400));
        frame.add(BorderLayout.CENTER, content);
        frame.pack();
        Dimension frameSize = frame.getSize();
        Dimension contentSize = content.getSize();
        int titleHeight = PlatformUtils.isMac() ? 22 : 25;
        assertEquals("Window height must correspond to content width + title height", 400 + titleHeight, frameSize.height);
        assertEquals("Window width must correspond to content width", 400, frameSize.width);
        assertEquals("Content height must be 400", 400, contentSize.height);
        assertEquals("Content width must be 400", 400, contentSize.width);
    }
}
