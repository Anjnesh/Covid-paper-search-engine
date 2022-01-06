package PreProcessData;

import Classes.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class JsonCollection {
	public File[] listOfFiles;
	public int corpusLen = 0;
	int paperNum =0;
	
	
	public JsonCollection() throws IOException{
		
		File folder = new File(Path.folderPath);
		listOfFiles = folder.listFiles();
		corpusLen = listOfFiles.length;
		
		
	}
	
	public Map<String, Object> nextDocument() throws IOException, ParseException{
		//store the result
		Map<String, Object> res = new HashMap<>();
		
		//store the document content
		String cont ="";
		  
		String filePath = Path.folderPath+"//"+listOfFiles[paperNum].getName();
		
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(new FileReader(filePath));
		
		JSONObject jsonObject =  (JSONObject) obj;

        String paper_id = (String) jsonObject.get("paper_id");
        
        
        
        JSONObject metadata =(JSONObject) jsonObject.get("metadata");
        if(!metadata.isEmpty()) {
        	String title = (String) metadata.get("title");
        	cont = title;
        	JSONArray author = (JSONArray ) metadata.get("authors");
        	if(!author.isEmpty()) {
        		Iterator<JSONObject> iteratorAu = author.iterator();
        		while (iteratorAu.hasNext()) {
        			JSONObject anAuthor = iteratorAu.next();
        			String firstname = (String) anAuthor.get("first");
        			cont = cont + " " + firstname;
        			JSONArray midName = (JSONArray) anAuthor.get("middle");
        			for(Object midN: midName) {
        				cont = cont + " " + midN;
        			}
        			String lastName = (String) anAuthor.get("last");
        			cont = cont + " " + lastName;
        			
        			JSONObject affiliation = (JSONObject) anAuthor.get("affiliation");
        			if(!affiliation.isEmpty()) {
        				String laboratory = (String)affiliation.get("laboratory");
        				cont = cont + " " + laboratory;
        				String institution = (String)affiliation.get("institution");
        				cont = cont + " " + institution;
        				
        				JSONObject location = (JSONObject) affiliation.get("location");
        				if(!location.isEmpty()) {
        					String settlement = (String)location.get("settlement");
        					cont = cont + " " + settlement;
        					
        					String region = (String)location.get("region");
        					cont = cont + " " + region;
        					
        					String country = (String)location.get("country");
        					cont = cont + " " + country;
        					
        				}
        			}
        		}
        		
        	}
        }
        
        JSONArray  paperAbstract = (JSONArray ) jsonObject.get("abstract");
        String paperAbstractText="";
        Iterator<JSONObject> iterator = paperAbstract.iterator();
		while (iterator.hasNext()) {
			paperAbstractText += (String) iterator.next().get("text")+" ";
		}
		cont = cont + " " + paperAbstractText;
		JSONArray  body = (JSONArray ) jsonObject.get("body_text");
        Iterator<JSONObject> iteratorBody = body.iterator();
        String bodyText ="";
		while (iteratorBody.hasNext()) {
			bodyText += (String) iteratorBody.next().get("text")+" ";
		}
		cont = cont + " " + bodyText;
		
		res.put(paper_id, cont.toCharArray());
		  
		paperNum++;
		
		return res;
	}

}
