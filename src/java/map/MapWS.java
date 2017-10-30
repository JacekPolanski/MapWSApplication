package map;


import java.util.Base64;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author jacek
 */
@WebService(serviceName = "MapWS")
@Stateless()
public class MapWS {

    /**
     * Web service operation
     * @return 
     * @throws java.lang.Exception 
     */
    @WebMethod(operationName = "getMap")
    public byte[] getMap() throws Exception {
        String sourceFile = "/home/jacek/Obrazy/pultusk.png";
        
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] mapData = loadFileAsBytesArray(sourceFile);
        byte[] encodedMap = encoder.encode(mapData);
        
        return encodedMap;
    }
    
    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader;
        reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        
        return bytes;

    }
}
