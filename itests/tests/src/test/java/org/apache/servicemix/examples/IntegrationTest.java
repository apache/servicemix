/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.examples;

import org.apache.cxf.Bus;
import org.apache.servicemix.examples.cxf.HelloWorld;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.nmr.api.*;
import org.apache.servicemix.platform.testing.support.AbstractIntegrationTest;
import org.apache.servicemix.util.FileUtil;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import javax.activation.DataHandler;
import javax.xml.ws.Holder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.jar.Manifest;

public class IntegrationTest extends AbstractIntegrationTest {

    private Properties dependencies;

    /**
     * The manifest to use for the "virtual bundle" created
     * out of the test classes and resources in this project
     *
     * This is actually the boilerplate manifest with one additional
     * import-package added. We should provide a simpler customization
     * point for such use cases that doesn't require duplication
     * of the entire manifest...
     */
    protected String getManifestLocation() {
        return "classpath:org/apache/servicemix/MANIFEST.MF";
    }

    /**
     * The location of the packaged OSGi bundles to be installed
     * for this test. Values are Spring resource paths. The bundles
     * we want to use are part of the same multi-project maven
     * build as this project is. Hence we use the localMavenArtifact
     * helper method to find the bundles produced by the package
     * phase of the maven build (these tests will run after the
     * packaging phase, in the integration-test phase).
     *
     * JUnit, commons-logging, spring-core and the spring OSGi
     * test bundle are automatically included so do not need
     * to be specified here.
     */
    protected String[] getTestBundlesNames() {
        return new String[] {
            getBundle("org.apache.felix", "org.apache.felix.prefs"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.activation-api-1.1"),
            getBundle("org.apache.geronimo.specs", "geronimo-annotation_1.0_spec"),
            getBundle("org.apache.geronimo.specs", "geronimo-servlet_2.5_spec"),
            getBundle("org.apache.geronimo.specs", "geronimo-ws-metadata_2.0_spec"),
            getBundle("org.apache.geronimo.specs", "geronimo-j2ee-connector_1.5_spec"),
            getBundle("org.apache.geronimo.specs", "geronimo-jta_1.1_spec"),
                       
            //for activemq
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.commons-lang"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.jasypt"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.josql"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.oro"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.velocity"),
            getBundle("commons-collections", "commons-collections"),
            getBundle("org.springframework", "spring-jms"),
            getBundle("org.springframework", "spring-tx"),
            getBundle("org.apache.geronimo.specs", "geronimo-j2ee-management_1.1_spec"),
            getBundle("org.apache.geronimo.specs", "geronimo-jms_1.1_spec"),
            getBundle("commons-pool", "commons-pool"),
            getBundle("org.apache.xbean", "xbean-spring"),
            getBundle("org.apache.activemq", "activemq-core"),
            getBundle("org.apache.activemq", "activemq-ra"),
            getBundle("org.apache.activemq", "activemq-console"),
            getBundle("org.apache.activemq", "activemq-pool"),
            getBundle("org.apache.activemq", "kahadb"),           
            
            //for ws-security
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.opensaml"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.joda-time"),
            getBundle("org.apache.ws.security", "wss4j"),
 
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.jbi-api-1.0"),            
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.stax-api-1.0"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.saaj-api-1.3"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.jaxb-api-2.2"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.jaxws-api-2.2"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.java-persistence-api-2.0"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.asm"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.cglib"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.jaxb-impl"),
            getBundle("org.apache.neethi", "neethi"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.saaj-impl"),
            getBundle("org.codehaus.woodstox", "stax2-api"),
            getBundle("org.codehaus.woodstox", "woodstox-core-asl"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.dom4j"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.wsdl4j"),
            getBundle("org.apache.ws.xmlschema", "xmlschema-core"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.xmlresolver"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.bcel"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.xerces"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.xalan"),
            getBundle("org.apache.santuario", "xmlsec"),
            //getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.jetty"),
            getBundle("org.eclipse.jetty", "jetty-util"),
            getBundle("org.eclipse.jetty", "jetty-io"),
            getBundle("org.eclipse.jetty", "jetty-http"),
            getBundle("org.eclipse.jetty", "jetty-client"),
            getBundle("org.eclipse.jetty", "jetty-continuation"),
            getBundle("org.eclipse.jetty", "jetty-jmx"),
            getBundle("org.eclipse.jetty", "jetty-server"),
            getBundle("org.eclipse.jetty", "jetty-security"),
            getBundle("org.eclipse.jetty", "jetty-servlet"),
            getBundle("org.eclipse.jetty", "jetty-servlets"),
            getBundle("org.eclipse.jetty", "jetty-xml"),
            getBundle("org.eclipse.jetty", "jetty-webapp"),
            getBundle("javax.mail", "mail"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.commons-codec"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.commons-httpclient"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.abdera"),
            getBundle("org.codehaus.jettison", "jettison"),
            getBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.xmlbeans"),
            getBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.jsr311-api-1.1.1"),
            getBundle("org.ops4j.pax.web", "pax-web-api"),
            getBundle("org.ops4j.pax.web", "pax-web-spi"),
            getBundle("org.ops4j.pax.web", "pax-web-runtime"),
            getBundle("org.ops4j.pax.web", "pax-web-jetty"),
            getBundle("org.ops4j.pax.web", "pax-web-extender-whiteboard"),
            getBundle("org.apache.servicemix", "servicemix-utils"),
            getBundle("org.fusesource.commonman", "commons-management"),
            getBundle("org.apache.karaf.jaas", "org.apache.karaf.jaas.boot"),
            getBundle("org.apache.karaf.jaas", "org.apache.karaf.jaas.config"),
            getBundle("org.apache.karaf.management", "org.apache.karaf.management.server"),
            getBundle("org.apache.cxf", "cxf-bundle"),
            getBundle("org.apache.servicemix.cxf", "org.apache.servicemix.cxf.transport.nmr"),
            getBundle("org.apache.servicemix.nmr", "org.apache.servicemix.nmr.api"),
            getBundle("org.apache.servicemix.nmr", "org.apache.servicemix.nmr.core"),
            getBundle("org.apache.servicemix.nmr", "org.apache.servicemix.nmr.management"),
            getBundle("org.apache.servicemix.nmr", "org.apache.servicemix.nmr.osgi"),
            getBundle("org.apache.servicemix.document", "org.apache.servicemix.document"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.test-commons"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-http-osgi"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-ws-security-osgi"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-jms-osgi"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-soap-handler-osgi"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-mtom-osgi"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-handler-cfg"),
            getBundle("org.apache.servicemix.examples", "cxf-ws-addressing"),
            getBundle("org.apache.servicemix.examples", "cxf-jaxrs"),
            getBundle("org.apache.servicemix.itests", "org.apache.servicemix.itests.cxf-nmr-osgi"),
        };
    }

    public void testJbiComponent() throws Exception {
        Thread.sleep(5000);
        installBundle("org.apache.servicemix.examples", "cxf-osgi", null, "jar");
        Thread.sleep(5000);
    }

    public void testHttpOsgi() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-http-osgi");
        Thread.sleep(5000);

        String filter = "(&(" + "objectclass=" + HelloWorld.class.getName() + ")(TEST-BUNDLE-NAME=cxf-http-osgi))";
        ServiceReference ref = bundleContext.getServiceReferences(null, filter)[0];
        assertNotNull("Service Reference is null", ref);

        org.apache.servicemix.examples.cxf.HelloWorld helloWorld = null;

        helloWorld = (org.apache.servicemix.examples.cxf.HelloWorld) bundleContext.getService(ref);
        assertNotNull("Cannot find the service", helloWorld);

        assertEquals("Hello Bonjour", helloWorld.sayHi("Bonjour"));
    }
    
    public void testJmsOsgi() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-jms-osgi");
        Thread.sleep(5000);
        String filter = "(&(" + "objectclass=" + HelloWorld.class.getName() + ")(TEST-BUNDLE-NAME=cxf-jms-osgi))";
        ServiceReference ref = bundleContext.getServiceReferences(null, filter)[0];
        assertNotNull("Service Reference is null", ref);

        org.apache.servicemix.examples.cxf.HelloWorld helloWorld = null;

        helloWorld = (org.apache.servicemix.examples.cxf.HelloWorld) bundleContext.getService(ref);
        assertNotNull("Cannot find the service", helloWorld);

        assertEquals("Hello Bonjour", helloWorld.sayHi("Bonjour"));
    }
    
    public void testNMROsgi() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-nmr-osgi");
        Thread.sleep(5000);
        NMR nmr = getOsgiService(NMR.class);
        assertNotNull(nmr);
        
        Channel client = nmr.createChannel();
        Exchange e = client.createExchange(Pattern.InOut);
        for (Endpoint ep : nmr.getEndpointRegistry().getServices()) {
            e.setTarget(nmr.getEndpointRegistry().lookup(nmr.getEndpointRegistry().getProperties(ep)));
            e.getIn().setBody(new StringSource("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:sayHi xmlns:ns2=\"http://cxf.examples.servicemix.apache.org/\"><arg0>Bonjour</arg0></ns2:sayHi></soap:Body></soap:Envelope>"));
            boolean res = client.sendSync(e);
            assertTrue(res);
        }
    
    }

    protected Manifest getManifest() {
        Manifest mf = super.getManifest();
        String importP = mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE);
        mf.getMainAttributes().putValue(Constants.IMPORT_PACKAGE,
            importP + ",META-INF.cxf, org.apache.servicemix.jbi.jaxp");
        String exportP = mf.getMainAttributes().getValue(Constants.EXPORT_PACKAGE);
        mf.getMainAttributes().putValue(Constants.EXPORT_PACKAGE,
                                      exportP + ",org.apache.handlers, "
                                      + "org.apache.springcfg.handlers, "
                                      + "org.apache.cxf.mime, "
                                      + "javax.xml.ws, "
                                      + "javax.activation, "
                                      + "org.apache.handlers.types, "
                                      + "org.apache.servicemix.examples.cxf,"
                                      + "org.apache.servicemix.examples.cxf.soaphandler,"
                                      + "org.apache.servicemix.examples.cxf.springcfghandler,"
                                      + "org.apache.servicemix.examples.cxf.wsaddressing,"
                                      + "org.apache.servicemix.util,"
                                      + "org.apache.hello_world_soap_http,"
                                      + "org.apache.cxf,"
                                      + "org.apache.cxf.bus,"
                                      + "org.apache.cxf.interceptor"
                                      );
        return mf;
    }

    public void testSoapHandlerOsgi() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-soap-handler-osgi");
        Thread.sleep(5000);

        ServiceReference ref = bundleContext.getServiceReference(org.apache.handlers.AddNumbers.class.getName());
        assertNotNull("Service Reference is null", ref);

        org.apache.handlers.AddNumbers addNumbers = null;

        addNumbers = (org.apache.handlers.AddNumbers) bundleContext.getService(ref);
        assertNotNull("Cannot find the service", addNumbers);

        assertEquals(2, addNumbers.addNumbers(1,1));

    }

     public void testSpringConfigHandlerOsgi() throws Exception {
         Thread.sleep(5000);
         waitOnContextCreation("org.apache.servicemix.itests.cxf-handler-cfg");
         Thread.sleep(5000);

         ServiceReference ref = bundleContext.getServiceReference(org.apache.springcfg.handlers.AddNumbers.class.getName());
         assertNotNull("Service Reference is null", ref);

         org.apache.springcfg.handlers.AddNumbers addNumbers = null;

         addNumbers = (org.apache.springcfg.handlers.AddNumbers) bundleContext.getService(ref);
         assertNotNull("Cannot find the service", addNumbers);

         assertEquals(1016, addNumbers.addNumbers(10, 16));
    }

    public void testWsAddressingOsgi() throws Exception {
         Thread.sleep(5000);
         waitOnContextCreation("cxf-ws-addressing");

         ServiceReference busref = bundleContext.getServiceReference(org.apache.cxf.bus.CXFBusImpl.class.getName());
         assertNotNull("Bus Reference is null", busref);

         Bus bus = (Bus)bundleContext.getService(busref);

         ByteArrayOutputStream input = new ByteArrayOutputStream();
         PrintWriter writer = new PrintWriter(input, true);
         org.apache.cxf.interceptor.LoggingInInterceptor in = new org.apache.cxf.interceptor.LoggingInInterceptor(writer);
         bus.getInInterceptors().add(in);

         ByteArrayOutputStream output = new ByteArrayOutputStream();
         PrintWriter outwriter = new PrintWriter(output, true);
         org.apache.cxf.interceptor.LoggingOutInterceptor out = new org.apache.cxf.interceptor.LoggingOutInterceptor(outwriter);
         bus.getOutInterceptors().add(out);

         ServiceReference ref = bundleContext.getServiceReference(org.apache.hello_world_soap_http.Greeter.class.getName());
         assertNotNull("Service Reference is null", ref);


         org.apache.hello_world_soap_http.Greeter greeter = null;

         greeter = (org.apache.hello_world_soap_http.Greeter) bundleContext.getService(ref);
         assertNotNull("Cannot find the service", greeter);

         assertEquals("Bonjour", greeter.sayHi());

         String expectedOut = "<Address>http://www.w3.org/2005/08/addressing/anonymous</Address>";
         String expectedIn = "<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">";

         assertTrue(output.toString().indexOf(expectedOut) != -1);
         assertTrue(input.toString().indexOf(expectedIn) != -1);
    }

    public void testJaxRS() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("cxf-jaxrs");

        URL url = new URL("http://localhost:8080/cxf/crm/customerservice/customers/123");
        InputStream in = url.openStream();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int c = 0;
        while ((c = in.read()) != -1) {
            bos.write(c);
        }
        in.close();
        bos.close();
        System.out.println(bos.toString());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>123</id><name>John</name></Customer>",
                     bos.toString());
    }

    public void testWSSecurity() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-ws-security-osgi");
        Thread.sleep(5000);
        URLConnection connection = new URL("http://localhost:8080/cxf/HelloWorldSecurity")
                .openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        // Post the request file.
        InputStream fis = getClass().getClassLoader().getResourceAsStream("org/apache/servicemix/request.xml");
        FileUtil.copyInputStream(fis, os);
        // Read the response.
        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileUtil.copyInputStream(is, baos);
        assertTrue(baos.toString().indexOf("Hello John Doe") >= 0);

    }

    public void testMtomOsgi() throws Exception {
        Thread.sleep(5000);
        waitOnContextCreation("org.apache.servicemix.itests.cxf-mtom-osgi");
        Thread.sleep(5000);

        ServiceReference ref = bundleContext.getServiceReference(org.apache.cxf.mime.TestMtom.class.getName());
        assertNotNull("Service Reference is null", ref);

        org.apache.cxf.mime.TestMtom testMtom = null;

        testMtom = (org.apache.cxf.mime.TestMtom) bundleContext.getService(ref);
        assertNotNull("Cannot find the service", testMtom);
         
        URL fileURL = getClass().getClassLoader().getResource("me.bmp");
        System.out.println("\nStarting MTOM test with DataHandler:");
        Holder<String> name = new Holder<String>("Bob");
        Holder<DataHandler> handler = new Holder<DataHandler>();

        handler.value = new DataHandler(fileURL);

        System.out.println("--Sending the me.bmp image to server");
        System.out.println("--Sending a name value of " + name.value);

        testMtom.testXop(name, handler);

        InputStream mtomIn = handler.value.getInputStream();
        long fileSize = 0;
        for (int i = mtomIn.read(); i != -1; i = mtomIn.read()) {
            fileSize++;
        }

        System.out.println("--Received DataHandler back from server, "
            + "returned size is " + fileSize);
        System.out.println("--Returned string value is " + name.value);
        assertEquals(fileSize, 163166);
        System.out.println("Successfully ran MTOM/DataHandler demo");

    }

}
