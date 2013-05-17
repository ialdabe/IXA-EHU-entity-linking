package ixa.entity.linking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.jdom2.JDOMException;

import ixa.kaflib.KAFDocument;

public class EntityLinking {

    public static void main(String[] args) { // throws IOException {

	int port;
	if (args.length == 1) {
	    try {
		port = Integer.parseInt(args[0]);
		Annotate annotator = new Annotate();
		// Input
		BufferedReader stdInReader = null;
		// Output
		BufferedWriter w = null;

		stdInReader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
		w = new BufferedWriter(new OutputStreamWriter(System.out,"UTF-8"));
		KAFDocument kaf = KAFDocument.createFromStream(stdInReader);
		annotator.disambiguateNEsToKAF(kaf,port);
		w.write(kaf.toString());
		w.close();
	    } 
	    catch (NumberFormatException e) {
		System.err.println("Argument" + " must be an integer");
		System.exit(1);
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
	else{
	    System.err.println("The function requires the port parameter");
	    System.exit(1);
	}
    } 

}
