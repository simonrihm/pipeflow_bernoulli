package rohre;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;

public class Main {

	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	static void erstellen(Bauraum bauraum){
		
		//Eingabe der Anzahl zu erstellender Rohr-Objekte
		System.out.println("Wie viele Rohre sollen erstellt werden?");
		int x = 0;
		try {
			x = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			System.out.println("Falsche Eingabe.");
		}
		
		
		for(int i=0; i<x; i++){
			double d = 0;
			double l = 0;
			double lambda  = 0;
			int index;
			boolean d_eingabe = false;
			boolean l_eingabe = false;
			boolean lambda_eingabe = false;
			
			while(l_eingabe == false){
				System.out.println("Länge des " + (bauraum.getAnz()+1) + ". Rohrs in Meter?");
				try {
					l = Double.parseDouble(in.readLine());
					l_eingabe = true;
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}
			
			while(d_eingabe == false){
				System.out.println("Durchmesser des " + (bauraum.getAnz()+1) + ". Rohrs in Meter?");
				try {
					d = Double.parseDouble(in.readLine());
					d_eingabe = true;
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}
			
			while(lambda_eingabe==false){
				System.out.println("Reibungsbeiwert des " + (bauraum.getAnz()+1) + ". Rohrs?");
				try {
					lambda = Double.parseDouble(in.readLine());
					lambda_eingabe = true;
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}	
			
			index = bauraum.getAnz()+1;
			bauraum.tubes.add(bauraum.getAnz(), new Rohr(l,d,lambda,index));
				
			bauraum.setAnz(bauraum.getAnz()+1);
			
		}
		
		//System.out.println(tubes[3].position);
		System.out.println(">> " + x + " Rohr(e) erstellt");
	}

	static void kopieren(Bauraum bauraum){
		int index_copy = 0;
		int anzahl = 0;
		int index_new;
		
		System.out.println("Welches Rohr soll kopiert werden?");
		try {
			index_copy = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			System.out.println("Falsche Eingabe.");
		}
		
		System.out.println("Wie oft soll das Rohr kopiert werden?");
		try {
			anzahl = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			System.out.println("Falsche Eingabe.");
		}
		
		for(int i=0; i<anzahl; i++){
			index_new = bauraum.getAnz()+1;
			bauraum.tubes.add(bauraum.getAnz(), bauraum.tubes.get(index_copy-1).kopieren(index_new));
			bauraum.setAnz(bauraum.getAnz()+1);
		}
		
		System.out.println(">> Rohr " + index_copy + " wurde " + anzahl + " Mal kopiert");
	}
	
	static void einbauen(Bauraum bauraum){
		int rohr = 0;
		int stelle = 0;
		boolean rohr_eingabe = false;
		boolean stelle_eingabe = false;
		
		while(rohr_eingabe == false){
			System.out.println("Welches Rohr soll eingebaut werden?");
			try {
				rohr = Integer.parseInt(in.readLine());
				if(bauraum.tubes.get(rohr-1).position==-1){
					rohr_eingabe = true;
				}
				else{
					System.out.println("Dieses Rohr ist bereits eingebaut!");	
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		while(stelle_eingabe == false){
			System.out.println("An welche Stelle soll das Rohr eingebaut werden?");
			try {
				stelle = Integer.parseInt(in.readLine());
				if(bauraum.getRohr(stelle)==null){
					System.out.println("Einbau kann nur an der Stelle 0, hinter ein bereits eingebautes Rohr oder zwischen zwei bereits eingebaute Rohre erfolgen.");
				}
				else{
					stelle_eingabe = true;	
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		bauraum.tubes.get(rohr-1).einbauen(bauraum.getRohr(stelle));
		bauraum.setBerechnet(false);
		bauraum.setEinbau(bauraum.getEinbau()+1);
		System.out.println(">> eingebaut");
	}
	
	static void ausbauen(Bauraum bauraum){
		int rohr = 0;
		boolean rohr_eingabe = false;
		
		while(rohr_eingabe == false){
			System.out.println("Welches Rohr soll ausgebaut werden?");
			try {
				rohr = Integer.parseInt(in.readLine());
				if(bauraum.tubes.get(rohr-1).position==-1){
					System.out.println("Dieses Rohr ist momentan nicht eingebaut!");	
				}
				else{
					rohr_eingabe = true;
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		bauraum.tubes.get(rohr-1).ausbauen();
		bauraum.setBerechnet(false);
		bauraum.setEinbau(bauraum.getEinbau()-1);
		System.out.println(">> ausgebaut");
	}
	
	static void pruefen(Bauraum bauraum){
		String input = " ";
		System.out.println("Prüfe (Rohre | Einbau | Ergebnis):");
		
		try {
			input = in.readLine();
		} catch (IOException e) {
		
		}
		
		switch (input) {
		case "Rohre":
			if(bauraum.getAnz() == 0){
				System.out.println("Bisher wurden keine Rohre erstellt!");
			}
			else{
				for(int i = 0; i< bauraum.getAnz();i++){
					System.out.print("Rohr Nr. " + (i+1) +"\t");
					bauraum.tubes.get(i).checkStatus();
				}
			}
			System.out.println(">> Rohre geprueft");
			break;
							
		case "Einbau":
			if(bauraum.totalLength()==0){
				System.out.println("Bisher wurden keine Rohre eingebaut!");
			}
			else{
				bauraum.checkEinbau();
				System.out.println("Gesamtlänge: " + bauraum.totalLength() + "m");
			}
			System.out.println(">> Einbau geprueft");
			break;

		case "Ergebnis":
			if(bauraum.getBerechnet()){
				System.out.println("Stelle \t totaler Druck \t statischer Druck");
				for(int i=0; i<= bauraum.getEinbau(); i++){
					System.out.println("(" + i + ")" + "\t" + Math.round(bauraum.pressure_total.get(i)*100)/100.0 + " Pa \t" + Math.round(bauraum.pressure_static.get(i*2)*100)/100.0 + " Pa \t" + Math.round(bauraum.pressure_static.get(i*2+1)*100)/100.0 + " Pa");
				}
				
			}
			else{
				System.out.println("Für die aktuelle Einbausituation liegt kein Ergebnis vor!");
			}
			System.out.println(">> Ergebnis geprueft");
			break;
			
		default:
			System.out.println("Ungültige Eingabe!");
			break;
			
		}
		

	}
	
	static void berechnen(Bauraum bauraum, Fluid fluid){
		int pos_start = 0;
		int pos_end = 0;
		double volume_flux = 0;
		double pressure_0 = 0;
		double pressure_1 = 0;
		int calc_mode = 0; //1: p+V_dot    -1: p+p    Rest: "not set"
		boolean pos_start_eingabe = false;
		boolean pos_end_eingabe = false;
		boolean volume_flux_eingabe = false;
		boolean pressure_0_eingabe = false;
		boolean pressure_1_eingabe = false;
		boolean calc_mode_eingabe = false;
		
		while(calc_mode_eingabe == false){
			System.out.println("Sollen Druck und Volumenstrom (-1) oder zwei Drücke (1) vorgegeben werden?");
			try {
				calc_mode = Integer.parseInt(in.readLine());
				if(calc_mode == 1 || calc_mode == -1){
					calc_mode_eingabe = true;
				}
				else{
					System.out.println("Der Modus muss mit 1 oder -1 vorgegeben werden!");	
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		
		while(pos_start_eingabe == false){
			System.out.println("An welcher Position soll der Druck vorgegeben werden?");
			try {
				pos_start = Integer.parseInt(in.readLine());
				if(pos_start < bauraum.getEinbau() && pos_start >= 0){
					pos_start_eingabe = true;
				}
				else if(pos_start == bauraum.getEinbau() && pos_start >= 0){
					if(calc_mode==-1){
						pos_start_eingabe = true;
					}
					else{
						System.out.println("Es muss zuerst die vordere Position genannt werden!");
					}
				}
				else{
					System.out.println("Es muss eine Position am Anfang oder zwischen eingebauten Rohren gewählt werden!");	
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		while(pressure_0_eingabe == false){
			System.out.println("Wie groß ist der Druck an dieser Stelle?");
			try {
				pressure_0 = Double.parseDouble(in.readLine());
				if(pressure_0 > 0){
					pressure_0_eingabe = true;
				}
				else{
					System.out.println("Der Druck muss positiv sein!");	
				}
			} catch (Throwable e) {
				System.out.println("Falsche Eingabe.");
			}
		}
		
		if(calc_mode == -1){
			while(volume_flux_eingabe == false){
				System.out.println("Wie groß ist der Volumenstrom durch die Rohre?");
				try {
					volume_flux = Double.parseDouble(in.readLine());
					if(volume_flux > 0){
						volume_flux_eingabe = true;
					}
					else{
						System.out.println("Der Volumenstrom muss positiv sein!");	
					}
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}
			
//			pressure_tot = bauraum.pTot_calc(fluid, pressure_0, volume_flux, pos_start);
//			pressure_stat = bauraum.pStat_calc(fluid, pressure_tot, volume_flux);
		}
		
		
		if(calc_mode==1){
			while(pos_end_eingabe == false){
				System.out.println("An welcher Position soll der Druck vorgegeben werden?");
				try {
					pos_end = Integer.parseInt(in.readLine());
					if(pos_end <= bauraum.getAnz() && pos_end > pos_start){
						pos_end_eingabe = true;
					}
					else{
						System.out.println("Es muss eine Position hinter der  ersten Druckvorgabe innerhalb des Bauraums vorgegeben werden!");	
					}
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}
			
			while(pressure_1_eingabe == false){
				System.out.println("Wie groß ist der Druck an dieser Stelle?");
				try {
					pressure_1 = Double.parseDouble(in.readLine());
					if(pressure_1 > 0){
						pressure_1_eingabe = true;
					}
					else{
						System.out.println("Der Druck muss positiv sein!");	
					}
				} catch (Throwable e) {
					System.out.println("Falsche Eingabe.");
				}
			}
			
			volume_flux = bauraum.getVolumeFlux(fluid,pos_start, pressure_0, pos_end, pressure_1);
		}


		bauraum.pTot_calc(fluid, pressure_0, volume_flux, pos_start);
		bauraum.pStat_calc(fluid, bauraum.pressure_total, volume_flux);
		
		bauraum.setBerechnet(true);

		System.out.println("Volumenstrom: " + volume_flux + " m³/s");
		System.out.println("Stelle \t totaler Druck \t statischer Druck \t Kavitation");
		
		for(int i=0; i<= bauraum.getEinbau(); i++){
			System.out.println("(" + i + ")" + "\t" + Math.round(bauraum.pressure_total.get(i)*100)/100.0 + " Pa \t" + Math.round(bauraum.pressure_static.get(i*2)*100)/100.0 + " Pa \t" + Math.round(bauraum.pressure_static.get(i*2+1)*100)/100.0 + " Pa \t" + fluid.checkKavitation(bauraum.pressure_static.get(i)));
		}
		
		System.out.println(">> berechnet");
	}

	static void testen(Bauraum bauraum, Fluid fluid){
		bauraum.tubes.add(bauraum.getAnz(), new Rohr(5,0.1,1e-4,bauraum.getAnz()+1));
		bauraum.tubes.add(bauraum.getAnz()+1, new Rohr(10,0.2,2e-4,bauraum.getAnz()+2));
		bauraum.tubes.add(bauraum.getAnz()+2, new Rohr(15,0.3,3e-4,bauraum.getAnz()+3));
		bauraum.tubes.add(bauraum.getAnz()+3, new Rohr(20,0.4,4e-4,bauraum.getAnz()+4));
		
		
		bauraum.tubes.get(bauraum.getAnz()).einbauen(bauraum.start);
		bauraum.setAnz(bauraum.getAnz()+1);
		bauraum.setEinbau(bauraum.getEinbau()+1);
		bauraum.tubes.get(bauraum.getAnz()).einbauen(bauraum.tubes.get(0));
		bauraum.setAnz(bauraum.getAnz()+1);
		bauraum.setEinbau(bauraum.getEinbau()+1);
		bauraum.tubes.get(bauraum.getAnz()).einbauen(bauraum.tubes.get(1));
		bauraum.setAnz(bauraum.getAnz()+1);
		bauraum.setEinbau(bauraum.getEinbau()+1);
		bauraum.tubes.get(bauraum.getAnz()).einbauen(bauraum.tubes.get(1));
		bauraum.setAnz(bauraum.getAnz()+1);
		bauraum.setEinbau(bauraum.getEinbau()+1);
		
		bauraum.setBerechnet(false);
		
//		if(bauraum.tubes[4]!=null){
//		System.out.println(bauraum.tubes[4].getVelocity(0.1));
//		System.out.println(fluid.pStat(2e5, bauraum.tubes[4].getVelocity(0.1)));
//		}
		
		System.out.println(">>getestet");
	}
	
	public static void main(String[] args) {
//		Bauraum bauraum = new Bauraum();
		ArrayList<Bauraum> bauraum = new ArrayList<Bauraum>();
		int bauraum_anz = 0;
		int bauraum_index = bauraum_anz;
		
		Fluid water = new Fluid(3170.0,1e-7,7.196,1730.0,233.0);
		boolean laufend_main = true;
		
		while(laufend_main){
			String input_main = " ";
			System.out.print("Hauptmenü (Bauraum | Rohre | Analyse | exit)");			
			try {
				input_main = in.readLine();
			} catch (IOException e) {
							
			}
			
			switch (input_main) {
			case "Bauraum":
				boolean laufend_bauraum = true;

				while(laufend_bauraum){
					if(bauraum_anz == 0){
						System.out.println("Momentan ist noch kein Bauraum vorhanden!");
					}
					else{
						System.out.println("== Bauraum " + (bauraum_index+1) + " ==");
					}
					System.out.println("Bauraum (erstellen | wechseln | kopieren | back)");
					String input_bauraum = " ";
					try {
						input_bauraum = in.readLine();
					} catch (IOException e) {
						
					}
					
					switch (input_bauraum){
					case "erstellen":
						System.out.println(">> erstelle...");
						bauraum.add(new Bauraum());
						bauraum_anz = bauraum_anz+1;
						bauraum_index = bauraum_anz-1;
						System.out.println(">> Bauraum " + bauraum_anz + " erstellt");
						break;
					case "wechseln":
						System.out.println(">> wechsle...");
						boolean bauraum_wechsel_eingabe = false;						
						int bauraum_wechsel;
						
						while(bauraum_wechsel_eingabe == false){
							System.out.println("In welchen Bauraum wechseln?");
							try {
								bauraum_wechsel = Integer.parseInt(in.readLine());
								if(bauraum_wechsel <= bauraum_anz && bauraum_wechsel > 0){
									bauraum_index = bauraum_wechsel-1;
									bauraum_wechsel_eingabe = true;
									System.out.println(">> gewechselt in Bauraum " + (bauraum_index+1));
								}
								else{
									System.out.println("Dieser Bauraum ist nicht vorhanden!");	
								}
							} catch (Throwable e) {
								System.out.println("Falsche Eingabe.");
							}
						}
						break;
						
					case "kopieren":
						System.out.println(">> kopiere...");
						boolean bauraum_kopie_eingabe = false;						
						int bauraum_kopie;
						
						while(bauraum_kopie_eingabe == false){
							System.out.println("Welcher Bauraum soll kopiert werden?");
							try {
								bauraum_kopie = Integer.parseInt(in.readLine());
								if(bauraum_kopie <= bauraum_anz && bauraum_kopie > 0){
									bauraum.add(bauraum.get(bauraum_kopie-1).kopieren());
									//System.out.println("check 1");
									bauraum_anz = bauraum_anz+1;
									//System.out.println("check 2");
									bauraum_kopie_eingabe = true;
									//System.out.println("check 3");
									bauraum_index = bauraum_anz-1;
									System.out.println("Bauraum " + bauraum_anz + " als Kopie von Bauraum " + bauraum_kopie + " erstellt.");
								}
								else{
									System.out.println("Dieser Bauraum ist nicht vorhanden!");	
								}
							} catch (Throwable e) {
								System.out.println("Falsche Eingabe.");
							}
						}
						break;
					case "back":
						laufend_bauraum = false;
						break;
					default:
						System.out.println("Ungültige Eingabe!");
						break;
						
					}
				}
				break;
								
			case "Rohre":
				boolean laufend_rohre = true;
				while(laufend_rohre){
					System.out.println("Rohre (erstellen | kopieren | einbauen | ausbauen | back)");
					String input_rohre = " ";
					try {
						input_rohre = in.readLine();
					} catch (IOException e) {
						
					}
					
					switch (input_rohre){
					case "erstellen":
						System.out.println(">> erstelle...");
						erstellen(bauraum.get(bauraum_index));
						break;
					case "kopieren":
						System.out.println(">> kopiere...");
						kopieren(bauraum.get(bauraum_index));
						break;
					case "einbauen":
						System.out.println(">> baue ein...");
						einbauen(bauraum.get(bauraum_index));
						break;
					case "ausbauen":
						System.out.println(">> baue aus...");
						ausbauen(bauraum.get(bauraum_index));
						break;
					case "back":
						laufend_rohre = false;
						break;
					default:
						System.out.println("Ungültige Eingabe!");
						break;	
					}
				}
				
				break;
					
			case "Analyse":
				boolean laufend_analyse = true;

				while(laufend_analyse){
					System.out.println("Analyse (prüfen | berechnen | back)");
					String input_analyse = " ";
					try {
						input_analyse = in.readLine();
					} catch (IOException e) {
						
					}
					
					switch (input_analyse){
					case "prüfen":
						System.out.println(">> prüfe...");
						pruefen(bauraum.get(bauraum_index));
						break;
					case "berechnen":
						System.out.println(">> berechne...");
						berechnen(bauraum.get(bauraum_index), water);
						break;

					case "back":
						laufend_analyse = false;
						break;
					default:
						System.out.println("Ungültige Eingabe!");
						break;	
					}
				}
				break;
				
			case "testen":
				System.out.println(">> teste...");
				testen(bauraum.get(bauraum_index),water);
				break;			
				
			case "exit":
				laufend_main = false;
				System.out.println(">> beendet");
				break;
								
			default:
				System.out.println("Ungültige Eingabe!");
				break;
				
			}
		}
		
//		while(laufend){
//			String input = " ";
//			System.out.print("Rohre (erstellen | einbauen | pruefen | berechnen): ");
//			
//			try {
//				input = in.readLine();
//			} catch (IOException e) {
//			
//			}
//			
//			switch (input) {
//			case "erstellen": 	System.out.println(">> erstelle...");
//								erstellen(bauraum);
//								break;
//								
//			case "einbauen": 	System.out.println(">> baue ein...");
//								einbauen(bauraum);
//								break;
//			
//			case "pruefen":		System.out.println(">> pruefe...");
//								pruefen(bauraum);
//								break;
//			
//			case "berechnen": 	System.out.println(">> berechne...");
//								berechnen(bauraum, water);
//								break;
//								
//			case "testen":		System.out.println(">> teste...");
//								testen(bauraum,water);
//								break;			
//								
//			case "exit":		laufend = false;
//								System.out.println(">> beendet");
//								break;
//								
//			default:			System.out.println("Ungültige Eingabe!");
//								break;
//				
//			}
//		}
	}

}
