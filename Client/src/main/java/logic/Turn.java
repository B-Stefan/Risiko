package main.java.logic;

public class Turn {
	private Player player;
	
	public Turn(Player p){
		this.player = p;
	}
    public Player getPlayer (){return this.player; }
    public String toString(){
        return "Turn: " + this.player.toString();
    }
}
