/* This file is part of Sign-up Page Sample.

The Sign-up Page Sample is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Sign-up Page Sample is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with The Sign-up Page Sample.  If not, see <http://www.gnu.org/licenses/>.

 * Program Work Flow
 * 1. Check if the subscription exists.
 * 2a. If it does exist, retrieve subscription_id.
 * 	2b. Edit the subscription and add to the new list.
 * 3a. If it does NOT exist.
 * 	3b. Add new subscription to list.
 */
package com.trumpia.signup.command;

import com.trumpia.signup.util.RequestRest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SubscriptionCommand {

    // Account information and validation.
    // Replace myName with the proper username and myKey with the API key
    // Get your free Trumpia API Key at http://api.trumpia.com
    // Replace with the list_name the subscription will be added to
    private String apiKey   = "594c862a4bfe227e3a537be359d3381b";
    private String userName = "paulkim";
    private String listName = "MyContacts";

    // Class for REST request
    private RequestRest requestRest;

    private String subscriptionId;
    private String baseUrl = "http://api.trumpia.com/rest/v1/";

    public void execute(String firstName, String lastName, String mobileNum) throws Exception {

        JSONObject response;
        requestRest = new RequestRest();

        // Mobile number search.
        // If the subscription exists, it will grab the existing subscription_id and edit the subscription with (POST).
        // If the subscription does not exist, it will add it as a new record.
        if (searchExist(firstName, lastName, mobileNum))
            response = postSubscription(firstName, lastName, mobileNum);
        else
            response = putSubscription(firstName, lastName, mobileNum);

        // Get report from response.
        getReport(response);
    }

    // This function will search if the mobile number exists in the database.
    private boolean searchExist(String firstName, String lastName, String mobileNum) throws IOException, JSONException {

        String searchType = "2";
        String requestUrl = baseUrl + userName + "/subscription/search?" +
                "search_type=" + searchType +
                "&search_data=" + mobileNum;
        requestRest.setApiKey(apiKey);
        requestRest.setRequestUrl(requestUrl);
        JSONObject response = new JSONObject(requestRest.get());

        if (response.has("subscription_id_list")) {
            subscriptionId = response.getJSONArray("subscription_id_list").get(0).toString();
            return true;
        }
        return false;
    }

    // This function will edit a subscription.
    private JSONObject postSubscription(String firstName, String lastName, String mobileNum) throws Exception {

        String requestUrl = baseUrl + userName + "/subscription/" + subscriptionId;

        JSONObject jsonBody = body(firstName, lastName, mobileNum);

        // Remove unnecessary parameters from JSONObject.
        jsonBody.getJSONArray("subscriptions").getJSONObject(0).remove("voice_device");
        jsonBody.getJSONArray("subscriptions").getJSONObject(0).getJSONObject("mobile").remove("number");

        String requestBody = jsonBody.toString();
        requestRest.setRequestUrl(requestUrl);
        requestRest.setRequestBody(requestBody);
        return new JSONObject(requestRest.post());
    }

    // This function will add a new subscription.
    private JSONObject putSubscription(String firstName, String lastName, String mobileNum) throws Exception {

        String requestUrl = baseUrl + userName + "/subscription";

        JSONObject jsonBody = body(firstName, lastName, mobileNum);
        String requestBody = jsonBody.toString();

        requestRest.setRequestUrl(requestUrl);
        requestRest.setRequestBody(requestBody);

        // Get report from response.
        return new JSONObject(requestRest.put());
    }

    // This function makes JSONObject request body.
    private JSONObject body(String firstName, String lastName, String mobileNum) throws JSONException {

        JSONObject jsonBody = new JSONObject();
        JSONArray subArray = new JSONArray();
        JSONObject subscriptions = new JSONObject();
        JSONObject mobile = new JSONObject();
        subscriptions.put("voice_device", "mobile");
        mobile.put("country_code", "1");
        mobile.put("number", mobileNum);
        subscriptions.put("mobile", mobile);
        subscriptions.put("last_name", lastName);
        subscriptions.put("first_name", firstName);
        subArray.put(subscriptions);
        jsonBody.put("subscriptions", subArray);
        jsonBody.put("list_name", listName);
        return jsonBody;
    }

    // This function retrieves report data of GET/POST response.
    private void getReport(JSONObject response) throws Exception {
        // Proper Trumpia request has status code.
        if (response.has("status_code")) {
            String requestUrl = baseUrl + userName + "/report/" + response.get("request_id");
            Thread.sleep(1000);
            requestRest.setRequestUrl(requestUrl);
            String reportString = requestRest.get();
            JSONObject report;
            try {
                report = new JSONObject(reportString);
            } catch (JSONException e) {
                report = new JSONObject(reportString.substring(1, reportString.length() - 1));
            }
            if (report.has("subscription_id")) {
                if (report.has("message"))
                    alert(report.getString("message"));
                else
                    alert("Success input new Subscription");
            } else {
                switch (report.get("status_code").toString()) {
                    case "MPSE2302":
                        alert("Request failed - not a valid list_name value.");
                    case "MPSE0501":
                        alert("Request failed - Mobile number is blocked.");
                    case "MPSE2201":
                        alert("Request failed - Invalid mobile number.");
                }
            }
        }
        // Response of Unusual request.
        else
            alert(response.toString());
    }

    // This function is to make alert message and return to index page.
    private void alert(String message) throws IOException {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "');</script>");
        out.println("<meta http-equiv=\"refresh\" content=\"0; url=/\">");
        out.flush();
        out.close();
    }

}
