package io.snapcx.webhook.integrations;

public class MCAddNewSubscriberRequest {
	/*
  "email_address":"ajainy+MC03@gmail.com", 
    "merge_fields" : {
    "FNAME": "FNAMETestMC02",
    "LNAME": "LName002",
    "MMERGE3": "www.test.org"
    },
    "status":"subscribed"
	 */
	public String email_address = null;
	public String status = "subscribed";
	public MCMergeFields merge_fields;
}
