package ixa.entity.linking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.jdom2.JDOMException;

import ixa.kaflib.KAFDocument;

public class EntityLinking {

    public static void main(String[] args) { // throws IOException {
	
	Annotate annotator = new Annotate();
	// Input
	BufferedReader stdInReader = null;
	// Output
	BufferedWriter w = null;
	try {
	    stdInReader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
	    w = new BufferedWriter(new OutputStreamWriter(System.out,"UTF-8"));
	    KAFDocument kaf = KAFDocument.createFromStream(stdInReader);
	    annotator.disambiguateNEsToKAF(kaf);
	    w.write(kaf.toString());
	    w.close();
	}
	catch (IOException e){ 
	    e.printStackTrace();
	}
	catch (JDOMException e){ 
	    e.printStackTrace();
	}
	catch (Exception e){
	    e.printStackTrace();
	}
    } 

}
