package io.snapcx.webhook.integrations;

import io.snapcx.webhook.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("mailChimp")
public class MailChimp {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String providerName = "MAILCHIMP";

	@Value("${mailChimp.base.url}")
	private String endPointURL;
	
	@Value("${mailChimp.default.listId}")
	private String listId;
	
	@Value("${mailChimp.userId}")
	private String userId;
	
	@Value("${mailChimp.apiKey}")
	private String apiKey;
	
	@Autowired
	private RestTemplate restTemplate;
	private HttpHeaders httpHeaders = null;
	
	public String addNewMemberToList(String emailAddress, String orgName, String fName, String lName) {
		try {
			MCAddNewSubscriberRequest request = new MCAddNewSubscriberRequest();
			request.email_address = emailAddress;
			request.merge_fields = new MCMergeFields();
			request.merge_fields.FNAME = fName;
			request.merge_fields.LNAME = lName;
			request.merge_fields.MMERGE3 = orgName;
			JsonNode node = this.getJSONTreeNodeUsingRestTemplate(request, this.endPointURL+"/lists/"+this.listId+"/members");
			String newId = node.get("id").asText();
			this.logger.info("Returned subscriber id for user "+emailAddress+" is "+newId);
			
			if (newId == null || newId.isEmpty()) {
				this.logger.error("Errorneous response string is \n"+node.toString());
				throw new Exception("Something went wrong with request. Returned new id is empty");
			}
			return newId;
		} catch (Exception ex) {
			this.logger.error("Exception in adding new member "+emailAddress, ex);
		}
		
		return null;
	}

	
 protected JsonNode getJSONTreeNodeUsingRestTemplate(Object requestObj, String endPointURL) throws Exception{
 	String jsonContent = this.invokeServiceUsingRestTemplate(requestObj, endPointURL);
   ObjectMapper mapper = new ObjectMapper();
   JsonNode rootNode = mapper.readValue(jsonContent, JsonNode.class);
   return rootNode;
}

 protected String invokeServiceUsingRestTemplate(Object requestObj, String endPointURL) throws Exception {
	    HttpEntity<?> httpEntity = this.getHttpEntity(requestObj);
	    ResponseEntity<String> responseEntity = this.restTemplate.exchange(endPointURL, HttpMethod.POST, httpEntity, String.class);
	    String jsonBodyResponse = responseEntity.getBody();
	    HttpStatus httpStatus = responseEntity.getStatusCode();
	    HttpHeaders headersResponse = responseEntity.getHeaders();//Future use
	    
	    if (httpStatus.is2xxSuccessful()) {
		    return jsonBodyResponse;

	    } else {
	    	String errMessage = "RestService returned status is "+httpStatus+" ,and URL is "+endPointURL;
	    	throw new IOException(errMessage);
	    }
	  }

  protected HttpEntity<?> getHttpEntity(Object request) throws Exception {
  	HttpHeaders headers = this.createHeaders(this.userId, this.apiKey);
  	String requestBody = this.createRequestBody(request);
   	return new HttpEntity(requestBody, headers);
  }
  
  
  private String createRequestBody(Object request) throws Exception {
   String jsonStr = Utils.getJSOnStr(request);
   return jsonStr;
  }

 private HttpHeaders createHeaders(final String username, final String password){
	if (null == this.httpHeaders) {
	  this.httpHeaders = new HttpHeaders(){
	   {
	  	 String auth = username + ":" + password;
       byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")) );
       String encodedAuth2 = "Basic " + new String( encodedAuth );
	  	 set("Authorization", encodedAuth2);
	  	 set("Content-Type", "application/json");
	   }
      };
	}
	return this.httpHeaders;
   }
	
}
