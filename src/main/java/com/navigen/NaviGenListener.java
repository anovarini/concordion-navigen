/**
 * Copyright 2012 Alessandro Novarini
 *
 * This file is part of the Concordion-NaviGen project.
 *
 * Concordion-NaviGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.navigen;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import org.concordion.api.Element;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

public class NaviGenListener implements SpecificationProcessingListener {

    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

        // Taken from concordion source code, isn't it possible to have it in one place only?
        String concordionOutputDir = System.getProperty("concordion.output.dir");
        if (concordionOutputDir == null) {
            concordionOutputDir = System.getProperty("java.io.tmpdir") + "/concordion";
        }

        Element naviGen = event.getRootElement().getElementById("navigen");
        if (null != naviGen) {
            File filePath = getFilePath(event);
            scanForLink(filePath, naviGen);
        }
    }

    private File getFilePath(SpecificationProcessingEvent event) {
        URL url = this.getClass().getResource(event.getResource().getPath());
        return new File(url.getPath());
    }

    private void scanForLink(File path, Element naviGen) {
        File[] scannedFiles = path.getParentFile().listFiles(htmlOrDirectoryButNot(path.getName()));
        for (final File eachFile : scannedFiles) {
            add(eachFile, naviGen);
        }
    }

    private void add(final File file, Element naviGen) {
        if (file.isFile()) {
            addFile(file, naviGen);
        }
        if (file.isDirectory()) {
            addDirectory(file, naviGen);
        }
    }

    private void addDirectory(File directory, Element naviGen) {
        FilenameFilter indexFileOnly = indexFile(directory);
        File[] listFiles = directory.listFiles(indexFileOnly);
        // This should contain at most one element
        for (File file : listFiles) {
            addIndex(naviGen, file);
        }
    }

    private void addIndex(Element naviGen, File file) {
        Element li = createListItem(naviGen);
        String dirName = file.getParentFile().getName();
        String fileName = file.getName();
        String href = dirName + "/" + fileName;

        Element link = createAnchor(href, fileName);

        li.appendChild(link);
    }

    private Element createAnchor(String href, String fileName) {
        Element link = new Element("a");
        link.addAttribute("href", href);
        link.appendText(fileName);
        return link;
    }

    private Element createListItem(Element naviGen) {
        Element ul = naviGen.getFirstChildElement("ul");
        if (null == ul) {
            ul = naviGen.appendChild("ul");
        }
        return ul.appendChild("li");
    }

    private void addFile(File file, Element naviGen) {
        Element li = createListItem(naviGen);
        Element link = createAnchor(file.getName(), file.getName());
        li.appendChild(link);
    }

    private FilenameFilter indexFile(final File file) {
        return new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase(file.getName() + ".html");
            }
        };
    }

    private FilenameFilter htmlOrDirectoryButNot(final String fileToIgnore) {
        return new FilenameFilter() {

            public boolean accept(File dir, String name) {
                boolean isNotSelf = !fileToIgnore.equals(name);
                File file = new File(dir, name);
                return isNotSelf && (name.endsWith(".html") || file.isDirectory());
            }
        };
    }

    public void beforeProcessingSpecification(SpecificationProcessingEvent arg0) {
        // Do nothing before
    }
}
