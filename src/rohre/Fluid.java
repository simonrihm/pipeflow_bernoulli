package rohre;

public class Fluid {

	//ATTRIBUTE
	double density; //Dichte in kg/m³
	double viscosity; //dyn. Viskosität in kg/(m*s)
	double temperature; //absolute Temperatur in °C
	double pressure_vap; //Dampfdruck in Pa
	double volume_flux; //Volumenstrom in m³/s
	double antoine_A, antoine_B, antoine_C; //Parameter der Antoine-Gleichung
	
	//KONSTRUKTOREN
	Fluid(double density, double viscosity, double A, double B, double C){
		this.density = density;
		this.viscosity = viscosity;
		this.temperature = 20;
		this.antoine_A = A;
		this.antoine_B = B;
		this.antoine_C = C;
		this.antoine();
	}
	
	
	//METHODEN
	double bernoulli(Rohr tube, double pressure_1, double velocity){
		double pressure_2 = pressure_1 - density/2 * velocity * velocity * tube.friction() ;
		return pressure_2;
	}
	
	double bernoulli_reverse(Rohr tube, double pressure_2, double velocity){
		double pressure_1 = pressure_2 + density/2 * velocity * velocity * tube.friction();
		return pressure_1;
	}
	
	double pStat(double pTot, double velocity){
		return pTot - this.density/2 * velocity * velocity;
	}
	
	boolean checkKavitation(double pressure){
		if (pressure < this.pressure_vap){
			return true;
		}
		return false;
	}
	
	void antoine(){
		this.pressure_vap = Math.pow(10, this.antoine_A - this.antoine_B/(this.antoine_C+this.temperature));
	}
	
	double getDensity(){
		return this.density;
	}
	
	void setDensity(double density){
		this.density = density;
	}
	
	double getViscosity(){
		return this.viscosity;
	}
	
	void setViscosity(double viscosity){
		this.viscosity = viscosity;
	}
	
	double getTemperature(){
		return this.temperature;
	}
	
	void setTemperature(double temperature){
		this.temperature = temperature;
	}
}
