package io.snapcx.webhook.web;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.snapcx.webhook.integrations.MailChimp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.xml.DomUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


@RestController
public class WebhookController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier("mailChimp")
	private MailChimp mailChimp;
	
  @RequestMapping(value="/ping", method=RequestMethod.GET)
  public String ping() {
  	return "webhook_pong";
  }

  @RequestMapping(value="/3scaleToMailChimp", method=RequestMethod.GET)
  public String ping2() {
  	return "webhook_pong";
  }
  
  
  @RequestMapping(
  		value="/3scaleToMailChimp", 
  		method=RequestMethod.POST,
  		produces={MediaType.TEXT_PLAIN_VALUE},
  		consumes={MediaType.APPLICATION_XML_VALUE})
	public String process3ScaleToMailChimp(@RequestBody(required=true) String threeScaleRequest) {
  	long startTime = System.currentTimeMillis();
  	try {
  		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(threeScaleRequest));
      Document xmlDom = docBuilder.parse(is);
      String orgName = null;
      String email = null;
      if (null != xmlDom) {
        Element eventNode = xmlDom.getDocumentElement();
        Element objectNode = DomUtils.getChildElementByTagName(eventNode, "object");
        if (null != objectNode) {
        	Element accountNode = DomUtils.getChildElementByTagName(objectNode, "account");
        	if (null != accountNode) {
        		Element orgNameNode = DomUtils.getChildElementByTagName(accountNode, "org_name");
        		orgName = DomUtils.getTextValue(orgNameNode);
        	}
        	Element usersNode = DomUtils.getChildElementByTagName(accountNode, "users");
        	if (null != usersNode) {
        		Element userNode = DomUtils.getChildElementByTagName(usersNode, "user");
        		email = DomUtils.getChildElementValueByTagName(userNode, "email");
        	}
        }
      }
      
      if (null != orgName && null != email) {
        	String response = this.mailChimp.addNewMemberToList(email, orgName, "", "");
        	//return response;
      } else {
        	throw new Exception("Something wrong in parsing XML request and getting values");
      }
  	} catch (Exception ex) {
  		this.logger.error("3ScaleToMailChimp : Caught exception at rest controller.", ex);
  		return "FAIL";
  	}
  	
    long endTime = System.currentTimeMillis();
    String responseTime = (endTime - startTime)+ " msecs";
    this.logger.info("Time took for server to process this request is "+responseTime);
    return "SUCCESS";
  }

}
