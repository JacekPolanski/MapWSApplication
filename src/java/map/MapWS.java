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
    @WebMethod(operationName = "getMapSection")
    public byte[] getMapSection(
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
}
