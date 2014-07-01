package ui.GUI.country;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.rmi.RemoteException;

import javax.swing.JLabel;

import interfaces.data.ICountry;
import ui.CUI.utils.IO;
import ui.GUI.utils.JExceptionDialog;

public class JCountryInfo extends JLabel{
	private final ICountry country;
	private static final int BORDER = 4;
	
	public JCountryInfo(ICountry country){
		this.country = country;
		
		setMinimumSize(new Dimension(20,20));
		setPreferredSize(new Dimension(20,20));
		setOpaque(false);
	}
	
	public void paint(final Graphics g){
		// Vom LayoutManager zugewiesene Größe des Buttons ermitteln
		final int width = 30;
		final int height = 30;
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
		g.fillArc(this.BORDER, this.BORDER, width - 2 * this.BORDER, height - 2 * this.BORDER, 0, 360);
        try {
            writeLabel(g, width, height);
        }catch (RemoteException e){
            e.printStackTrace();
            IO.println(e.getMessage());
            return;
        }

	}
	
	private void writeLabel(final Graphics g, final int width, final int height) throws RemoteException{
		final String amountArmies = "" + this.country.getArmySize();
        // Font einstellen
        final Font myFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
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
