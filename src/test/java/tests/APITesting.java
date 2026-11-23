package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITesting {

    Playwright playwright;
    @Test
    public void e2eApiTest(){

        HashMap<Object, Object> requestBody = new HashMap<>();
        requestBody.put("email", "deepakdntest2@gmail.com");
        requestBody.put("password", "DeepakTest@123");
       playwright =  Playwright.create();
       APIRequestContext apiRequest = playwright.request().newContext();
       APIResponse apiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
               RequestOptions.create().setData(requestBody));
       Assert.assertTrue(apiResponse.ok());
       System.out.println(apiResponse.text());

        String token = JsonPath.read(apiResponse.text(), "$.token");
        System.out.println("Login Success --"+token);

        HashMap<Object, Object> eventData = new HashMap<>();
        eventData.put("title", "API Test Automation 1");
        eventData.put("description", "Testing API testing with Playwright Automation");
        eventData.put("category", "Concert");
        eventData.put("venue", "Embassy tech village");
        eventData.put("city", "Bengaluru");
        eventData.put("eventDate", "2026-11-12T17:00:00.000Z");
        eventData.put("price", "278");
        eventData.put("totalSeats", "389");
        APIResponse eventDataResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                    RequestOptions.create().setHeader("Authorization", "Bearer "+token)
                            .setData(eventData));
        Assert.assertTrue(eventDataResponse.ok(), "Event added successfully through Automation");

        int eventID = JsonPath.read(eventDataResponse.text(), "$.data.id");
        System.out.println("Event added succeed through automation Event ID is ---"+eventID);

        //Get Event
        APIResponse retrieveEvents =  apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("limit", "10")
                        .setHeader("Authorization", "Bearer "+token));

        Assert.assertTrue(retrieveEvents.ok());

        List<Integer> listOfEvents = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(listOfEvents.contains(eventID));

        System.out.println("Event is present----"+listOfEvents.contains(eventID));

        //Delete Event
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventID,
                RequestOptions.create().setHeader("Authorization", "Bearer "+token));

        Assert.assertTrue(deleteResponse.ok(), "EVENT DELETION SUCCEED");
        System.out.println("Event deleted successfully ---"+eventID);

         retrieveEvents =  apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("limit", "10")
                        .setHeader("Authorization", "Bearer "+token));
         listOfEvents = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
         Assert.assertFalse(listOfEvents.contains(eventID));






    }

}
