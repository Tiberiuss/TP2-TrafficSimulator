package simulator.model;

public enum Weather {
	SUNNY(2), CLOUDY(3), RAINY(10), WINDY(15), STORM(20);
	
	  private final int x;//formula ReducirContaminacion en funcion del tiempo

	    Weather(int x) {
	        this.x = x;
	    }

		public int getX() {
			return x;
		}	   
}

