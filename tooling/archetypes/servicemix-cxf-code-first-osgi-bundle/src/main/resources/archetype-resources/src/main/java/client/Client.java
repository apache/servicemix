package ${packageName}.client;

import ${packageName}.*;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public final class Client {

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(Person.class);
        if (args != null && args.length > 0 && !"".equals(args[0])) {
            factory.setAddress(args[0]);
        } else {
            factory.setAddress("http://localhost:8181/cxf/PersonServiceCF");
        }
       
        Person client = (Person)factory.create();
        System.out.println("Invoking getPerson...");
        java.lang.String _getPerson_personIdVal = "Guillaume";
        javax.xml.ws.Holder<java.lang.String> _getPerson_personId = new javax.xml.ws.Holder<java.lang.String>(_getPerson_personIdVal);
        javax.xml.ws.Holder<java.lang.String> _getPerson_ssn = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> _getPerson_name = new javax.xml.ws.Holder<java.lang.String>();
        try {
            client.getPerson(_getPerson_personId, _getPerson_ssn, _getPerson_name);

            System.out.println("getPerson._getPerson_personId=" + _getPerson_personId.value);
            System.out.println("getPerson._getPerson_ssn=" + _getPerson_ssn.value);
            System.out.println("getPerson._getPerson_name=" + _getPerson_name.value);
        } catch (${packageName}.UnknownPersonFault upf) {
            System.out.println("Expected exception: UnknownPersonFault has occurred.");
            System.out.println(upf.toString());
        }
    }
}


