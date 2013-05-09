/**
 * Copyright 2011 Pablo Mendes, Max Jakob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ixa.entity.linking;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.Text;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URLEncoder;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.DocumentBuilder; 
import java.io.StringReader;
import org.xml.sax.InputSource;

import org.apache.log4j.Logger;

/**
 * Simple web service-based annotation client for DBpedia Spotlight.
 *
 * @author pablomendes, Joachim Daiber
 */

public class DBpediaSpotlightClient {
    public Logger LOG = Logger.getLogger(this.getClass()); 

    //private final static String API_URL = "http://jodaiber.dyndns.org:2222/";
    private final static String API_URL_EN = "http://localhost:2020/";
    private final static String API_URL_ES = "http://localhost:2222/";
	private static final double CONFIDENCE = 0.0;
	private static final int SUPPORT = 0;

    // Create an instance of HttpClient.
    private static HttpClient client = new HttpClient();


    public String request(HttpMethod method) throws AnnotationException {

        String response = null;

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            // // Deal with the response.
            // // Use caution: ensure correct character encoding and is not binary data
	    InputStream responseBody =  method.getResponseBodyAsStream();
	    response = IOUtils.toString(responseBody, "UTF-8");

        } catch (HttpException e) {
            LOG.error("Fatal protocol violation: " + e.getMessage());
            throw new AnnotationException("Protocol error executing HTTP request.",e);
        } catch (IOException e) {
            LOG.error("Fatal transport error: " + e.getMessage());
            LOG.error(method.getQueryString());
            throw new AnnotationException("Transport error executing HTTP request.",e);
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return response;

    }


    /*
http://spotlight.dbpedia.org/rest/candidates/?spotter=SpotXmlParser&text=
<annotation text="Brazilian oil giant Petrobras and U.S. oilfield service
company Halliburton have signed a technological cooperation agreement,
Petrobras announced Monday. The two companies agreed on three projects:
studies on contamination of fluids in oil wells, laboratory simulation of
well production, and research on solidification of salt and carbon dioxide
formations, said Petrobras. Twelve other projects are still under
negotiation."><surfaceForm name="Brazilian" offset="0"/><surfaceForm name="
oil" offset="10"/><surfaceForm name="giant" offset="14"/><surfaceForm name="
Petrobras" offset="20"/><surfaceForm name="U.S." offset="34"/><surfaceForm
name="oilfield" offset="39"/><surfaceForm name="service" offset="48"/>
<surfaceForm name="company" offset="56"/><surfaceForm name="Halliburton"
offset="64"/><surfaceForm name="signed" offset="81"/><surfaceForm name="
technological" offset="90"/><surfaceForm name="cooperation" offset="104"/>
<surfaceForm name="agreement" offset="116"/><surfaceForm name="Petrobras"
offset="127"/><surfaceForm name="Monday" offset="147"/><surfaceForm name="
companies" offset="163"/><surfaceForm name="projects" offset="189"/>
<surfaceForm name="contamination" offset="210"/><surfaceForm name="fluids"
offset="227"/><surfaceForm name="oil wells" offset="237"/><surfaceForm name
="laboratory" offset="248"/><surfaceForm name="simulation" offset="259"/>
<surfaceForm name="production" offset="278"/><surfaceForm name="
solidification" offset="306"/><surfaceForm name="salt" offset="324"/>
<surfaceForm name="carbon dioxide" offset="333"/><surfaceForm name="
Petrobras" offset="365"/><surfaceForm name="Twelve" offset="376"/>
<surfaceForm name="projects" offset="389"/><surfaceForm name="negotiation"
offset="414"/></annotation>
     */

    public Document extract(Text text,String lang) throws AnnotationException{

        LOG.info("Querying API.");
		String spotlightResponse;
		Document doc = null;
		try {
		    if (lang.equals("en"){
			    GetMethod getMethod = new GetMethod(API_URL_EN + "rest/disambiguate?spotter=SpotXmlParser" +
								"&confidence=" + CONFIDENCE
								+ "&support=" + SUPPORT
								+ "&text=" + URLEncoder.encode(text.text(), "utf-8"));
			    getMethod.addRequestHeader(new Header("Accept", "text/xml"));
			    spotlightResponse = request(getMethod);
			}
			else if (lang.equals("es")){
			    GetMethod getMethod = new GetMethod(API_URL_ES + "rest/disambiguate?spotter=SpotXmlParser" +
								"&confidence=" + CONFIDENCE
								+ "&support=" + SUPPORT
								+ "&text=" + URLEncoder.encode(text.text(), "utf-8"));
			    getMethod.addRequestHeader(new Header("Accept", "text/xml"));
			    spotlightResponse = request(getMethod);
			}
			 
			else {
			    System.out.println("Language " + lang + " is not provided yet");
			    exit(0);
			}   
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationException("Could not encode text.", e);
		}

		try {
		    doc = loadXMLFromString(spotlightResponse);
		}
		catch (javax.xml.parsers.ParserConfigurationException ex) {
		} 
		catch (org.xml.sax.SAXException ex) {
		} 
		catch (java.io.IOException ex) {
		} 		
		
		return doc;
	}

    public static Document loadXMLFromString(String xml)  throws org.xml.sax.SAXException, java.io.IOException, javax.xml.parsers.ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

}
