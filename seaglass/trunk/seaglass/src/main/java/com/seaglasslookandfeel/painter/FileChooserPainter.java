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
 * $Id: MenuPainter.java 1097 2010-02-06 13:31:05Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.painter;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.icons.FileIcon;
import com.seaglasslookandfeel.icons.FloppyDiskIcon;
import com.seaglasslookandfeel.icons.FolderHomeIcon;
import com.seaglasslookandfeel.icons.FolderIcon;
import com.seaglasslookandfeel.icons.FolderNewIcon;
import com.seaglasslookandfeel.icons.FolderUpIcon;
import com.seaglasslookandfeel.icons.HardDiskIcon;
import com.seaglasslookandfeel.icons.ViewDetailsIcon;
import com.seaglasslookandfeel.icons.ViewListIcon;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class FileChooserPainter extends AbstractRegionPainter {
    public static enum Which {
        FILEICON_ENABLED, DIRECTORYICON_ENABLED, UPFOLDERICON_ENABLED, 
        NEWFOLDERICON_ENABLED, HOMEFOLDERICON_ENABLED, 
        DETAILSVIEWICON_ENABLED, LISTVIEWICON_ENABLED, 
        HARDDRIVEICON_ENABLED, FLOPPYDRIVEICON_ENABLED        
    };

    private Which        state;
    private PaintContext ctx;
   
    /**
     * @param state
     */
    public FileChooserPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FILEICON_ENABLED:
            paintFileIcon(g, c, width, height);
            break;
        case DIRECTORYICON_ENABLED:
            paintDirectoryIcon(g, c, width, height);
            break;
        case UPFOLDERICON_ENABLED:
            paintUpFolderIcon(g, c, width, height);
            break;
        case NEWFOLDERICON_ENABLED:
            paintNewFolderIcon(g, c, width, height);
            break;
        case HOMEFOLDERICON_ENABLED:
            paintHomeFolderIcon(g, c, width, height);
            break;
        case DETAILSVIEWICON_ENABLED:
            paintViewDetailsIcon(g, c, width, height);
            break;
        case LISTVIEWICON_ENABLED:
            paintViewListIcon(g, c, width, height);
            break;
        case HARDDRIVEICON_ENABLED:
            paintHardDriveIcon(g, c, width, height);
            break;
        case FLOPPYDRIVEICON_ENABLED:
            paintFloppyDiskIcon(g, c, width, height);
            break;
        }
    }

    private void paintFloppyDiskIcon(Graphics2D g, JComponent c, int width, int height) {
        FloppyDiskIcon icon = new FloppyDiskIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintHardDriveIcon(Graphics2D g, JComponent c, int width, int height) {
        HardDiskIcon icon = new HardDiskIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintViewListIcon(Graphics2D g, JComponent c, int width, int height) {
        ViewListIcon icon = new ViewListIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintViewDetailsIcon(Graphics2D g, JComponent c, int width, int height) {
        ViewDetailsIcon icon = new ViewDetailsIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintHomeFolderIcon(Graphics2D g, JComponent c, int width, int height) {
        FolderHomeIcon icon = new FolderHomeIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintNewFolderIcon(Graphics2D g, JComponent c, int width, int height) {
        FolderNewIcon icon = new FolderNewIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintUpFolderIcon(Graphics2D g, JComponent c, int width, int height) {
        FolderUpIcon icon = new FolderUpIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintDirectoryIcon(Graphics2D g, JComponent c, int width, int height) {
        FolderIcon icon = new FolderIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    private void paintFileIcon(Graphics2D g, JComponent c, int width, int height) {
        FileIcon icon = new FileIcon();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
    
    

  
}
