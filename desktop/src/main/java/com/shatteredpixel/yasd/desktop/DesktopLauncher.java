/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.yasd.UpdateImpl;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.services.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DesktopLauncher {

    public static void main (String[] args) {

        if (!DesktopLaunchValidator.verifyValidJVMState(args)){
            return;
        }

        final String title;
        if (DesktopLauncher.class.getPackage().getSpecificationTitle() == null){
            title = System.getProperty("Specification-Title");
        } else {
            title = DesktopLauncher.class.getPackage().getSpecificationTitle();
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Game.reportException(throwable);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                pw.flush();
                String exceptionMsg = sw.toString();

                //shorten/simplify exception message to make it easier to fit into a message box
                exceptionMsg = exceptionMsg.replaceAll("\\(.*:([0-9]*)\\)", "($1)");
                exceptionMsg = exceptionMsg.replace("com.shatteredpixel.yasd.general.", "");
                exceptionMsg = exceptionMsg.replace("com.watabou.", "");
                exceptionMsg = exceptionMsg.replace("com.badlogic.gdx.", "");
                exceptionMsg = exceptionMsg.replace("\t", "    ");
                String message = title + " has run into an error it can't recover from and has crashed, sorry about that!\n\n" +
                        "The error has been copied to your clipboard. If you can, please post it on my Discord.\n\n" +
                        "version: " + Game.version + "\n" +
                        exceptionMsg;

                TinyFileDialogs.tinyfd_messageBox(title + " Has Crashed!",
                        message,
                        "ok", "error", false );
                StringSelection stringSelection = new StringSelection(message);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                if (Gdx.app != null) Gdx.app.exit();
            }
        });

        Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
        if (Game.version == null) {
            Game.version = System.getProperty("Specification-Version");
        }

        try {
            Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
        } catch (NumberFormatException e) {
            Game.versionCode = Integer.parseInt(System.getProperty("Implementation-Version"));
        }

        if (UpdateImpl.supportsUpdates()){
            Updates.service = UpdateImpl.getUpdateService();
        }

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle( title );

        String basePath = "";
        if (SharedLibraryLoader.isWindows) {
            if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
                basePath = "Application Data/.smujb/Cursed Pixel Dungeon/";
            } else {
                basePath = "AppData/Roaming/.smujb/Cursed Pixel Dungeon/";
            }
        } else if (SharedLibraryLoader.isMac) {
            basePath = "Library/Application Support/Cursed Pixel Dungeon/";
        } else if (SharedLibraryLoader.isLinux) {
            basePath = ".smujb/cursed-pixel-dungeon/";
        }

        //copy over prefs from old file location from legacy desktop codebase
        FileHandle oldPrefs = new Lwjgl3FileHandle(basePath + "pd-prefs", Files.FileType.External);
        FileHandle newPrefs = new Lwjgl3FileHandle(basePath + CPDSettings.DEFAULT_PREFS_FILE, Files.FileType.External);
        if (oldPrefs.exists() && !newPrefs.exists()){
            oldPrefs.copyTo(newPrefs);
        }

        config.setPreferencesConfig( basePath, Files.FileType.External );
        CPDSettings.set( new Lwjgl3Preferences( CPDSettings.DEFAULT_PREFS_FILE, basePath) );
        FileUtils.setDefaultFileProperties( Files.FileType.External, basePath );

        config.setWindowSizeLimits( 480, 320, -1, -1 );
        Point p = CPDSettings.windowResolution();
        config.setWindowedMode( p.x, p.y );
        config.setAutoIconify( true );

        //we set fullscreen/maximized in the listener as doing it through the config seems to be buggy
        DesktopWindowListener listener = new DesktopWindowListener();
        config.setWindowListener( listener );

        config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_64.png",
                "icons/icon_128.png", "icons/icon_256.png");

        new Lwjgl3Application(new CPDGame(new DesktopPlatformSupport()), config);
    }
}