package rohre;
import java.lang.Math;

public class Rohr {

	//ATTRIBUTE
	
	double length; //Länge in Meter
	double diameter; //innerer Durchmesser [m]
	int position = -1; //Stelle  von 0 bis x (-1 bedeutet "noch nicht eingebaut")
	Rohr prev; //Vorgänger-Element
	Rohr next; //Nachfolger-Element
	double lambda; //Reibungsbeiwert [-]
	int index; //"Name des Rohrs"
	
	//KONSTRUKTOREN
	
	/**
	 * Konstruktor für normale Rohre
	 * @param length Rohrlänge
	 * @param diameter Rohrdurchmesser
	 * @param lambda Reibungsbeiwert
	 * @param index soundsovieltes Rohr im Bauraum
	 */
	Rohr(double length, double diameter, double lambda, int index){
		this.diameter = diameter;
		this.length = length;
		this.lambda = lambda;
		this.index = index;
	}
	
	/**
	 * Konstruktor für Platzhalter-Rohre (z.B. Start-Element an Position 0)
	 */
	Rohr(){
		
	}

	//METHODEN
	
	/**
	 * Einbau des Rohrs in 2 möglichen Fällen:
	 * 		1. Einbau hinter das hinterste Rohr (oder "start")
	 * 		2. Einbau zwischen zwei hintereinanderliegende Rohre (wobei das vordere "start" sein kann)
	 * @param prev Vorgänger-Rohr, hinter das eingebaut werden soll
	 */
	void einbauen(Rohr prev){
		if(prev.next == null){ 
			//setze Vorgänger- und Nachfolge-Element
			this.prev = prev;
			//setze Position hinter Vorgänger-Element
			this.position = prev.position + 1;
			//setze Verknüpfung des Vorgänger-Elements
			prev.next = this;
		}
		else {
			//setze Vorgänger- und Nachfolge-Element
			this.prev = prev;
			this.next = prev.next;
			//setze Position hinter Vorgänger-Element
			this.position = prev.position + 1;
			/* 
			 * setze Verknüpfungen der Vorgänger- und Nachfolge-Elemente
			 * erhöhe Position des Nachfolge-Elements
			 */
			prev.next = this;
			this.next.prev = this;
			this.next.position = next.position + 1;
		}
	}
	
	/**
	 * Ausbau des Rohres, Position wird auf -1 gesetzt
	 * Falls ein nachfolgendes Rohr existiert, rutscht dieses eins zurück
	 */
	void ausbauen(){
		this.prev.next = this.next;
		if(this.next != null){
			this.next.prev =  this.prev;
			this.next.position = this.next.position - 1;
		}
		this.position = -1;
	}

	
	/**
	 * kopiert ein vorhandenes Rohr mit äußerlichen Eigenschaften 
	 * @param index Index der erstellten Kopie im Bauraum
	 * @return Rückgabe der Kopie
	 */
	Rohr kopieren(int index){
		Rohr rohr_neu = new Rohr();
		rohr_neu.setDiameter(this.diameter);
		rohr_neu.setLambda(this.lambda);
		rohr_neu.setLength(this.length);
		rohr_neu.setIndex(index);
		return rohr_neu;
	}
	
	/**
	 * klont ein vorhandenens Rohr mit allen Eigenschaften
	 * @return Rückgabe des Klons
	 */
	Rohr klonen(){
		Rohr rohr_neu = new Rohr();
		rohr_neu.setDiameter(this.diameter);
		rohr_neu.setLambda(this.lambda);
		rohr_neu.setLength(this.length);
		rohr_neu.setIndex(this.index);
		rohr_neu.position = this.position;
		return rohr_neu;
	}
	
	/**
	 * prüft, ob das Rohr bereits eingebaut ist (also über eine Position im Bauraum verfügt)
	 */
	void checkStatus(){
		String eingebaut;
		if(this.position == -1) eingebaut = "nein";
		else eingebaut = "ja";
		System.out.println("Länge: " + this.length + "m \t Durchmesser: " + this.diameter + "m \t Reibungsbeiwert: " + this.lambda + "\t eingebaut: " + eingebaut);
	}
	
	/**
	 * berechnet innere Querschnittsfläche
	 * @return Pi*r^2
	 */
	double getQuerschnitt(){
		return Math.PI*this.diameter*this.diameter/4;
	}
	
	/**
	 * berechne Geschwindigkeit aus Volumenstrom
	 * @param volume_flux vorgegebener Volumenstrom [m³/s]
	 * @return Geschwindigkeit [m/s]
	 */
	double getVelocity(double volume_flux){
		return volume_flux/this.getQuerschnitt();
	}
	
	/**
	 * Umkehrung obiger Methode
	 * @param velocity vorgegebene Geschwindigkeit [m/s]
	 * @return Volumenstrom [m³/s]
	 */
	double getVolumeFlux(double velocity){
		return velocity*this.getQuerschnitt();
	}
	
	double friction(){
		return this.length/this.diameter * this.lambda;
	}
	
	/*
	 * getter- und setter-Methoden für die Attribute
	 */
	
	double getLength(){
		return this.length;
	}
	
	void setLength(double l_neu){
		this.length = l_neu;
	}
	
	double getDiameter(){
		return this.diameter;
	}
	
	void setDiameter(double d_neu){
		this.diameter = d_neu;
	}
	
	double getLambda(){
		return this.lambda;
	}
	
	void setLambda(double lambda_neu){
		this.lambda = lambda_neu;
	}
	
	int getPos(){
		return this.position;
	}
	
	void setPos(int position){
		this.position = position;
	}
	
	int getIndex(){
		return this.index;
	}
	
	void setIndex(int index){
		this.index = index;
	}
}
