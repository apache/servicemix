package ${packageName};

import javax.jws.WebService;
import javax.xml.ws.Holder;

import ${packageName}.types.GetPerson;
import ${packageName}.types.GetPersonResponse;

@WebService(serviceName = "PersonService", endpointInterface = "${packageName}.Person")
public class PersonImpl implements Person {

    public void getPerson(Holder<String> personId, Holder<String> ssn, Holder<String> name)
        throws UnknownPersonFault
    {
        if (personId.value == null || personId.value.length() == 0) {
            ${packageName}.types.UnknownPersonFault fault = new ${packageName}.types.UnknownPersonFault();
            fault.setPersonId(personId.value);
            throw new UnknownPersonFault(null,fault);
        }
        name.value = "Guillaume";
        ssn.value = "000-000-0000";
    }

}
