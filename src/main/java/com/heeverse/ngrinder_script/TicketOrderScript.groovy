package com.heeverse.ngrinder_script
import HTTPClient.NVPair
import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.ngrinder.http.cookie.CookieManager

import static net.grinder.script.Grinder.grinder
import static org.hamcrest.Matchers.isOneOf
import static org.junit.Assert.assertThat

@RunWith(GrinderRunner)
/**
 * @author jeongheekim
 * @date 10/13/23
 */
class TicketOrderScript {
    public static String url = "http://118.67.142.206"
    public static GTest test
    public static HTTPRequest request
    public static Map<String, String> headers = [:]

    public static HTTPRequest loginRequest
    public static Map<String, String> loginHeaders = [:]
    public static String accessToken = ""
    public static List<Cookie> cookies = []

    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)
        // 각 테스트 통계를 수집할 때 사용되는 GTest인스턴스 정의
        test = new GTest(1, "WAS1")
        request = new HTTPRequest()
        loginRequest = new HTTPRequest()

        // Set header data
        headers.put("Content-Type", "application/json")
        grinder.logger.info("before process.")
    }

    @BeforeThread
    public void beforeThread() {
        grinder.logger.info("beforeThread Start.")

        //login
        loginHeaders.put("Content-Type", "application/json")
        loginRequest.setHeaders(loginHeaders)

        HTTPResponse loginResponse = loginRequest.POST(url + "/login", createRandomMemberSeqList());
        accessToken = loginResponse.getHeader("Authorization").toString()
        if (accessToken != null) {
            accessToken = accessToken.substring("Authorization: Bearer ".length())
        } else {
            grinder.logger.info("Authorization header is missing")
        }

        test.record(this, "test")
        grinder.statistics.delayReports = true
        grinder.logger.info("before thread end.")
    }

    @Before
    public void before() {
        headers.put("Authorization", accessToken)
        request.setHeaders(headers)
        CookieManager.addCookies(cookies)
        grinder.logger.info("before. init headers and cookies")
    }

    @Test
    public void test() {
        List<NVPair> ticketSetList = createRandomTicketSeqList()
        grinder.logger.info(ticketSetList as String)
        Map<String, Object> params = [:]
        params.put("ticketSetList", ticketSetList)
        HTTPResponse response = request.POST(url + "/ticket-order", params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, isOneOf(200,201))
        }
    }

    def createRandomMemberSeqList() {
        int startSeq = 1
        int endSeq = 1000000
        def random = new Random()
        int randomNum = random.nextInt(endSeq) + startSeq
        Map<String, Object> members = [:]
        members.put("id", "heeverse" + randomNum)
        members.put("password", "Heeverse1026!!")
        return members
    }

    def createRandomTicketSeqList() {
        def random = new Random()
        def numberOfRandomNumbers = random.nextInt(3) + 1
        return selectUniqueRandomNumbers(numberOfRandomNumbers, 1, 7500)
    }

    def selectUniqueRandomNumbers(int count, int min, int max) {
        def random = new Random()
        if (count <= 0 || count > (max - min + 1)) {
            throw new IllegalArgumentException("Invalid count or range")
        }

        def selectedNumbers = []
        def availableNumbers = (min..max).toSet()
        while (selectedNumbers.size() < count) {
            def randomIndex = random.nextInt(availableNumbers.size())
            def randomElement = availableNumbers[randomIndex]
            selectedNumbers.add(randomElement)
            availableNumbers.remove(randomElement)
        }
        return selectedNumbers
    }
}
