package rohre;
import java.util.ArrayList;

public class Bauraum {

	//ATTRIBUTE
	
	/*
	 * Liste der Rohre  im Bauraum
	 * "start" also Anfangselement zur weiteren Verknüpfung
	 * anz_total gibt die Anzahl der tatsächlich erstellten Rohre an
	 * anz_verbaut gibt die Anzahl der aktuell eingebauten Rohre an
	 */
	Rohr start;
	ArrayList<Rohr> tubes = new ArrayList<Rohr>();
	int anz_total = 0;
	int anz_verbaut = 0;
	
	/*
	 * berechnete Druckwerte in erweiterbaren Listen
	 * "berechnet" gibt an, ob für die aktuelle Einbausitiation ein Ergebnis vorliegt
	 */
	ArrayList<Double> pressure_total = new ArrayList<Double>();
	ArrayList<Double> pressure_static = new ArrayList<Double>();
	boolean berechnet = false;
	
	//KONSTRUKTOREN
	
	Bauraum(){
		this.start = new Rohr();
		start.position = 0;
	}
	
	//METHODEN
	/**
	 * gibt Rohr an bestimmter Einbauposition im Bauraum zurück
	 * @param position: Stelle, an der das Rohr eingebaut ist
	 * @return: Rohr an der Stelle
	 */
	Rohr getRohr(int position){
		Rohr actualtube = this.start;
		for(int i = 0; i<position; i++){
			actualtube = actualtube.next;
			if(actualtube==null){
				return actualtube;
			}
		}
		return actualtube;
	}
	
	Bauraum kopieren(){
		//erstelle neues Bauraum-Objekt
		Bauraum bauraum_new = new Bauraum();
		
		//erstelle Rohr-Objekte im Bauraum und klone Eigenschaften
		bauraum_new.start = this.start.klonen();
		bauraum_new.tubes = new ArrayList<Rohr>();
		for(int i=0; i<this.anz_total; i++){
			bauraum_new.tubes.add(this.tubes.get(i).klonen());
		}
		bauraum_new.anz_total = this.anz_total;
		
		//baue Rohre entsprechend ein
		Rohr lasttube = bauraum_new.start;
		Rohr actualtube;
		
		for(int i=0; i<anz_verbaut; i++){
			for(int j=0; j<anz_total; j++){
				actualtube = bauraum_new.tubes.get(j);
				if(actualtube.getPos()==i){
					lasttube.next = actualtube;
					actualtube.prev = lasttube;
					bauraum_new.anz_verbaut = bauraum_new.anz_verbaut+1;
					lasttube = actualtube;
					System.out.println("check");
					break;
				}
				
			}
			
		}
		
		
		//erstelle Druck-Arrays im Bauraum und kopiere ggf. Ergebnisse
		bauraum_new.pressure_static = new ArrayList<Double>();
		bauraum_new.pressure_total = new ArrayList<Double>();
		
		if(this.berechnet){
			for(int i=0; i<this.pressure_static.size(); i++){
				bauraum_new.pressure_static.set(i, this.pressure_static.get(i));
				bauraum_new.pressure_static.set(i, this.pressure_static.get(i));
			}
			
			bauraum_new.berechnet = true;
		}
		
		return bauraum_new;
	}
	
	/**
	 * gibt die Gesamtlänge der verbauten Rohre dieses Bauraums zurück
	 * @return Summe der Längen aller verbauten Rohre
	 */
	double totalLength(){
		Rohr actualtube = this.start;
		double length = 0;
		while(actualtube.next != null){
			actualtube = actualtube.next;
			length = length + actualtube.length;
		}
		return length;
	}
	
	
	void checkEinbau(){
		Rohr actualtube = this.start;
		System.out.println("Stelle (0)");
		for(int i = 1; actualtube.next != null; i++){
			actualtube = actualtube.next;
			System.out.println("Rohr " + actualtube.index);
			System.out.println("Stelle (" + i + ")");
		}
	}
	
	double getVolumeFlux(Fluid fluid, int pos_1, double p_1, int pos_2, double p_2){
		Rohr tube_1 = this.getRohr(pos_1+1);
		Rohr tube_2 = this.getRohr(pos_2+1);
		double volume_flux;
		double friction_tot = 0;
		double friction_neu;
		
		while(tube_1!=tube_2){
			friction_neu = tube_1.friction()*(fluid.getDensity()*8)/(Math.pow(Math.PI, 2)*Math.pow(tube_1.getDiameter(),4));
			friction_tot = friction_tot + friction_neu;
			
			tube_1 = tube_1.next;
		}
		
		volume_flux = Math.sqrt((p_1 - p_2)/friction_tot);
		volume_flux = Math.round(volume_flux*10000)/10000.0;
		
		return volume_flux;
		
	}
	
	void pTot_calc(Fluid fluid, double pressure_0, double volume_flux, int pos_start){
		this.pressure_total.clear();
		
		Rohr tube_rev = this.getRohr(pos_start);
		Rohr tube_for = tube_rev.next;
	
		this.pressure_total.add(pressure_0);

		
		for(int i = 0; i<pos_start; i++){
			this.pressure_total.add(0,fluid.bernoulli_reverse(tube_rev, this.pressure_total.get(0), tube_rev.getVelocity(volume_flux)));
			tube_rev = tube_rev.prev;
		}
		
		
		for(int i = pos_start; i < this.getEinbau() ;i++){
			this.pressure_total.add(fluid.bernoulli(tube_for, this.pressure_total.get(i), tube_for.getVelocity(volume_flux)));
			tube_for = tube_for.next;
		}
		
	}
	
	void pStat_calc(Fluid fluid, ArrayList<Double> pTot, double volume_flux){
		this.pressure_static.clear();
		
		Rohr actualtube = this.start;
		
		for(int i = 0; i<=this.getEinbau(); i++){
			if(actualtube==this.start){
				this.pressure_static.add(fluid.pStat(this.pressure_total.get(i),0));
			}
			else {
				this.pressure_static.add(fluid.pStat(this.pressure_total.get(i), actualtube.getVelocity(volume_flux)));
			}

			if (actualtube.next == null){
				this.pressure_static.add(fluid.pStat(this.pressure_total.get(i),0));
			}	
			else{
				this.pressure_static.add(fluid.pStat(this.pressure_total.get(i), actualtube.next.getVelocity(volume_flux)));
			}
			actualtube = actualtube.next;
		}
		
	}

	
	/*
	 * Getter- und Setter-Methode für die Anzahl der im Bauraum vorhandenen Rohre und "berechnet"-boolean
	 */
	
	int getAnz(){
		return  this.anz_total;
	}
	
	void setAnz(int anz_neu){
		this.anz_total = anz_neu;
	}
	
	int getEinbau(){
		return this.anz_verbaut;
	}
	
	void setEinbau(int anz_verbaut){
		this.anz_verbaut = anz_verbaut;
	}
	
	boolean getBerechnet(){
		return this.berechnet;
	}
	
	void setBerechnet(boolean berechnet){
		this.berechnet = berechnet;
	}
	
}
