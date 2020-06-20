package wsclient;
import java.util.List;
import wsproxy.*;
 
public class CodeInfoClient {
 
    public static void main(String[] args) {

        IpAddressSearchWebService service = new IpAddressSearchWebService();
        IpAddressSearchWebServiceSoap pService = service.getIpAddressSearchWebServiceSoap();
         
        ArrayOfString countryCityByIp = pService.getCountryCityByIp(args[0]);

        List<String> list = countryCityByIp.getString();

        Object[] object = list.toArray();

        for (Object o: object){
            System.out.println(o);
        }
 
    }
}