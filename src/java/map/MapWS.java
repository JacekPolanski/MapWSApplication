package map;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

/**
 *
 * @author jacek
 */
@WebService(serviceName = "MapWS", targetNamespace = "map")
@Stateless()
public class MapWS {
    private static final double START_WIDTH_COORD = 52.7025000; 
    private static final double START_LENGTH_COORD = 21.0827600;
    private static final double END_WIDTH_COORD = 52.8025000;
    private static final double END_LENGTH_COORD = 21.1827600;
    
    private final File map;
    
    public MapWS() throws Exception {
        URL sourceFile = this.getClass().getClassLoader().getResource("pultusk.png");
        map = new File(sourceFile.getPath());
    }
    
    /**
     * Web service operation
     * @return 
     * @throws java.lang.Exception 
     */
    @WebMethod(operationName = "getMap")
    public String getMap() throws Exception {        
        System.out.println("Wysłano mapę");
        
        return encoder(map);
    }
    
    public static String encoder(File file) {
        String base64Image = "";
	try (FileInputStream imageInFile = new FileInputStream(file)) {
		// Reading a Image file from file system
		byte imageData[] = new byte[(int) file.length()];
		imageInFile.read(imageData);
		base64Image = Base64.getEncoder().encodeToString(imageData);
	} catch (FileNotFoundException e) {
		System.out.println("Image not found" + e);
	} catch (IOException ioe) {
		System.out.println("Exception while reading the Image " + ioe);
	}
	return base64Image;
}
   
    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return 
     * @throws java.lang.Exception 
     */
    @WebMethod(operationName = "getMapSectionByPixels")
    public String getMapSectionByPixels(
            @WebParam(name = "x1") int x1, 
            @WebParam(name = "y1") int y1,
            @WebParam(name = "x2") int x2, 
            @WebParam(name = "y2") int y2
    ) throws Exception {
        BufferedImage originalImage = ImageIO.read(map);
        BufferedImage croppedImage = 
                originalImage.getSubimage(x1, y1, x2 - x1, y2 - y1);
        
        File outputFile = new File("cropped.png");
        ImageIO.write(croppedImage, "png", outputFile);
        
        System.out.println("Wysłano mapę");
        
        return encoder(outputFile);
    }
    
    /**
     * @param widthCoord1
     * @param lengthCoord1
     * @param widthCoord2
     * @param lengthCoord2
     * @return 
     * @throws java.lang.Exception 
     */
    @WebMethod(operationName = "getMapSectionByCoords")
    public String MapSectionByCoords(
            @WebParam(name = "widthCoord1") double widthCoord1,
            @WebParam(name = "lengthCoord1") double lengthCoord1,
            @WebParam(name = "widthCoord2") double widthCoord2,
            @WebParam(name = "lengthCoord2") double lengthCoord2
    ) throws Exception {
       double a = (END_WIDTH_COORD - START_WIDTH_COORD) / 1000;
       double b = (END_LENGTH_COORD - START_LENGTH_COORD) / 1000;
       
       int x1 = (int) ((widthCoord1 - START_WIDTH_COORD) / a);
       int y1 = (int) ((lengthCoord1 - START_LENGTH_COORD) / b);
       int x2 = (int) ((widthCoord2 - START_WIDTH_COORD) / a);
       int y2 = (int) ((lengthCoord2 - START_LENGTH_COORD) / b);
       
        BufferedImage originalImage = ImageIO.read(map);
        BufferedImage croppedImage = 
                originalImage.getSubimage(x1, y1, x2 - x1, y2 - y1);

        File outputFile = new File("cropped.png");
        ImageIO.write(croppedImage, "png", outputFile);

        System.out.println("Wysłano mapę");
        
        return encoder(outputFile);
    }
}
