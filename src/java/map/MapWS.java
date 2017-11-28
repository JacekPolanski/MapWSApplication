package map;

import java.awt.image.BufferedImage;
import java.util.Base64;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.jws.WebParam;

/**
 *
 * @author jacek
 */
@WebService(serviceName = "MapWS", targetNamespace = "map")
@Stateless()
public class MapWS {
    private static final double START_WIDTH_COORD = 52.7025000; // ToDo: zmienić na poprawne
    private static final double START_LENGTH_COORD = 21.0827600; // ToDo: zmienić na poprawne
    private static final double END_WIDTH_COORD = 52.8025000; // ToDo: zmienić na poprawne
    private static final double END_LENGTH_COORD = 21.1827600; // ToDo: zmienić na poprawne
    
    private final File map;
    private final Base64.Encoder encoder = Base64.getEncoder();
    
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
    public byte[] getMap() throws Exception {
        return encoder.encode(loadFileAsBytesArray(map));
    }
    
    /**
     * @param fileName
     * @return
     * @throws Exception 
     */
    private static byte[] loadFileAsBytesArray(File map) throws Exception {
        int length = (int) map.length();
        BufferedInputStream reader;
        reader = new BufferedInputStream(new FileInputStream(map));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        
        return bytes;
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
    public byte[] getMapSectionByPixels(
            @WebParam(name = "x1") int x1, 
            @WebParam(name = "y1") int y1,
            @WebParam(name = "x2") int x2, 
            @WebParam(name = "y2") int y2
    ) throws Exception {
        BufferedImage originalImage = ImageIO.read(map);
        BufferedImage croppedImage = originalImage.getSubimage(x1, y1, x2 - x1, y2 - y1);
        
        File outputFile = new File("cropped.png");
        ImageIO.write(croppedImage, "png", outputFile);
        
        return encoder.encode(loadFileAsBytesArray(outputFile));
    }
    
    /**
     * @param xWidthCoord1
     * @param yWidthCoord1
     * @param xLenghtCoord2
     * @param yLengthCoord2
     * @return 
     * @throws java.lang.Exception 
     */
    @WebMethod(operationName = "getMapSectionByCoords")
    public byte[] getMapSectionByCoords(
            @WebParam(name = "xWidthCoord1") double xWidthCoord1,
            @WebParam(name = "yWidthCoord1") double yWidthCoord1,
            @WebParam(name = "xLenghtCoord2") double xLenghtCoord2,
            @WebParam(name = "yLengthCoord2") double yLengthCoord2
    ) throws Exception {
        
       // ToDo: dodać logikę
       
       return null;
    }
}
