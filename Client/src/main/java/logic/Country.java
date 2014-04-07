package main.java.logic;


public class Country {
	
	/**
	 * Name des Lands
	 */
	private String name;
	
	/**
	 * Bestimmt den Spieler, der aktuell das Land besetzt h�llt
	 */
	private Player owner;

	
	
	public Country(String n){
		this.name = n;
	}
	
	/**
	 * setzt den aktuellen Besitzer des Lands
	 * @param p Spieler, der als Owener gesetzt werden soll
	 */
	public void setOwner(Player p){
		this.owner = p;
	}
	public Player getOwner(){
		return this.owner;
	}
	public String getName(){
		return this.name;
	}

    @Override
    public String toString(){
        String ownerString = " @ ";
        if (this.owner != null){
            ownerString += this.owner.ToString();
        }
        return this.getName() + ownerString;
    }
}
