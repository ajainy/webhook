package io.snapcx.webhook;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utils {
	
	public static String getJSOnStr(Object obj) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Writer strWriter = new StringWriter();
		mapper.writeValue(strWriter, obj);
		String userDataJSON = strWriter.toString();
		//System.out.println(URLEncoder.encode(userDataJSON));
		//System.out.println(userDataJSON);
		return userDataJSON;
		
	}

}
