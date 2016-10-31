/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.googlespreadsheet;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;
import java.util.HashMap;
import java.util.Map;

public class GooglespreadsheetConnectorIntegrationTest extends ConnectorIntegrationTestBase {
    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();
    private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();
    private String apiEndpointUrl;

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        init("googlespreadsheet-connector-1.0.0");
        esbRequestHeadersMap.put("Content-Type", "application/json");
        apiRequestHeadersMap.put("Content-Type", "application/json");
        apiEndpointUrl = "https://www.googleapis.com/oauth2/v4/token?grant_type=refresh_token&client_id=" + connectorProperties.getProperty("clientId") +
                "&client_secret=" + connectorProperties.getProperty("clientSecret") + "&refresh_token=" + connectorProperties.getProperty("refreshToken");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndpointUrl, "POST", apiRequestHeadersMap);
        final String accessToken = apiRestResponse.getBody().getString("access_token");
        connectorProperties.put("accessToken", accessToken);
        apiRequestHeadersMap.put("Authorization", "Bearer " + accessToken);
        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
    }
    /**
     * Positive test case for getMultipleCellData method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlespreadsheet {getMultipleCellData} integration test with mandatory parameters.")
    public void testGetMultipleCellDataWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getMultipleCellData");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getMultipleCellDataMandatory.json");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("spreadsheetId") + "/values:batchGet?fields="
                + connectorProperties.getProperty("fieldSpreadsheetId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().get("spreadsheetId"), apiRestResponse.getBody().get("spreadsheetId"));
    }
    /**
     * Negative test case for getMultipleCellData method.
     */
    @Test(groups = {"wso2.esb"}, description = "googlespreadsheet {getMultipleCellData} integration test with negative cases.")
    public void testGetMultipleCellDataWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getMultipleCellData");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getMultipleCellDataNegative.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
    }
    /**
     * Positive test case for getMultipleCellData method with Optional parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlespreadsheet {getMultipleCellData} integration test with Optional parameters.")
    public void testGetMultipleCellDataWithOptionalParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getMultipleCellData");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "getMultipleCellDataOptional.json");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("spreadsheetId")
                + "/values:batchGet?fields=" + connectorProperties.getProperty("fieldSpreadsheetId")
                + "&ranges=" + connectorProperties.getProperty("ranges1")
                + "&dateTimeRenderOption=" + connectorProperties.getProperty("dateTimeRenderOption")
                + "&majorDimension=" + connectorProperties.getProperty("majorDimension")
                + "&valueRenderOption=" + connectorProperties.getProperty("valueRenderOption");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().get("spreadsheetId"), apiRestResponse.getBody().get("spreadsheetId"));
    }

}
