package modele.entite;

import jason.environment.grid.Location;



public abstract class EntiteLocalisable {

	protected Location l;
	
	public EntiteLocalisable(Location l){
		this.l = l;
	}
	
	public Location getLocation(){
		return l;
	}
	
	public void setLocation(Location l){
		this.l = l;
	}
	
	public void setX(int x){
		this.l.x = x;
	}
	
	public void setY(int y){
		this.l.y = y;
	}
}
