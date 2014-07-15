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

package Client.ui.GUI.country;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.rmi.RemoteException;

import javax.swing.JLabel;

import commons.interfaces.data.ICountry;
import Client.ui.CUI.utils.IO;

public class JCountryInfo extends JLabel{
	private final ICountry country;
	private static final int BORDER = 4;
	
	public JCountryInfo(ICountry country){
        super();
		this.country = country;
		
		setMinimumSize(new Dimension(20,20));
		setPreferredSize(new Dimension(20,20));
		setOpaque(false);
	}
	
	public void paint(final Graphics g){
		// Vom LayoutManager zugewiesene Größe des Buttons ermitteln
		final int width = 40;
		final int height = 40;
		// Farbe entsprechend des besetzenden Spielers setzten
		final Color col;
        try {
            if (!(this.country.getOwner() == null)){
                col = this.country.getOwner().getColor();
            }else{
                col = Color.BLACK;
            }
        }catch (RemoteException e){
            e.printStackTrace();
            IO.println(e.getMessage());
            return;
        }
		g.setColor(col);
		g.fillArc(0, 0, width, height, 0, 360);
		g.setColor(Color.white);
		g.fillArc(BORDER, BORDER, width - 2 * BORDER, height - 2 * BORDER, 0, 360);
        try {
            writeLabel(g, width, height);
        }catch (RemoteException e){
            e.printStackTrace();
            IO.println(e.getMessage());
            return;
        }

	}
	
	private void writeLabel(final Graphics g, final int width, final int height) throws RemoteException{
		final String amountArmies = String.format(this.country.getArmySize() + " " + this.country.getShortName());
        // Font einstellen
        final Font myFont = new Font(Font.SANS_SERIF, Font.BOLD, 11);
        g.setFont(myFont);
        // Beschriftung anbringen
        final FontMetrics fm = g.getFontMetrics();
        final int textWidth = fm.stringWidth(amountArmies);
        final int textHeight = fm.getHeight();
        final int descent = fm.getDescent();
        g.setColor(Color.BLACK);
        g.drawString(amountArmies, width / 2 - textWidth / 2, height / 2
                + (textHeight / 2 - descent));
	}
	
}
