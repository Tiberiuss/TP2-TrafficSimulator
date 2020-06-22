package simulator.misc;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum ImageEnum {

	CAR("car.png"),
	CAR_FRONT("car_front.png"),
	CLOUDY("cloudy.png"),
	CO2CLASS("co2class.png"),
	CONT_0("cont_0.png"),
	CONT_1("cont_1.png"),
	CONT_2("cont_2.png"),
	CONT_3("cont_3.png"),
	CONT_4("cont_4.png"),
	CONT_5("cont_5.png"),
	EXIT("exit.png"),
	OPEN("open.png"),
	RAINY("rainy.png"),
	RUN("run.png"),
	STOP("stop.png"),
	STORM("storm.png"),
	SUNNY("sunny.png"),
	WEATHER("weather.png"),
	WINDY("windy.png");

	private Image image = null;

	private ImageEnum(String img) {		
//		java.net.URL url = null;
//		url = ImageEnum.class.getResource("/icons/" + filename);	//Importante barra delante de icons
//		if (url != null) {
//			image = new ImageIcon(url);
//		}		
		
		try {
			image=ImageIO.read(this.getClass().getResourceAsStream("/icons/" + img));
		} catch (Exception e) {
			System.err.println("IMAGE NOT FOUND");	
		}			
		
	}

	public ImageIcon getImage() {
		if(image!=null)
			return new ImageIcon(image);
		else
			return null;
	}

	
	public Image getImageIO() {
		return image;
	}
	
}

/*
 * public class LoadResources {
 * 
 * private static String src = "/icons/";
 * 
 * public static ImageIcon loadImage(String name) { URL a =
 * LoadResources.class.getResource(src + name); return (a == null) ? null : new
 * ImageIcon(a); }
 * 
 * }
 */