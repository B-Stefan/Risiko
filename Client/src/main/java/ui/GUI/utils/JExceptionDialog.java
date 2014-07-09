/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package ui.GUI.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Klasse zum anzeigen einer Exception
 */
public class JExceptionDialog {

    /**
     * Root Fenster in dem die Exception angezeigt wird
     */
    private final Window window;

    /**
     * Exception die angezeigt werden soll
     */
    private final Exception e;

    /**
     * Klasse zur Anzeige einer Exception
     * @param com Komponente, parent für das Dialogfenster
     * @param e Exception die angezeigt werden soll
     */
    public JExceptionDialog(Component com,Exception e ){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = e;
        this.openModal();
    }
    /**
     * Klasse zur Anzeige einer Exception
     * @param com Komponente, parent für das Dialogfenster
     * @param message Nachricht, die direkt angezeigt werden soll
     */
    public JExceptionDialog(Component com,String message){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = new Exception(message);
        this.openModal();
    }

    /**
     * Klasse zum anzeigen einer Exception
     * @param e
     */
    public JExceptionDialog(Exception e){
        this(new JWindow(),e);
    }

    /**
     * Klasse zum anzeigen einer Exception
     * @param e
     */
    public JExceptionDialog(String e){
        this(new JWindow(),e);
    }

    /**
     * Öffnet den modalen Dialog
     */
    private void openModal (){
        JOptionPane.showMessageDialog(window,
                e.getMessage(),
                "An Error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
