package com.hoeflm.immosearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URL;

import oauth.signpost.OAuthConsumer;

import oauth.signpost.OAuthProvider;

import oauth.signpost.basic.DefaultOAuthConsumer;

import oauth.signpost.basic.DefaultOAuthProvider;

import oauth.signpost.exception.OAuthCommunicationException;

import oauth.signpost.exception.OAuthExpectationFailedException;

import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class ImmosearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImmosearchApplication.class, args);

		try {
			//run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping
	public ResponseEntity<String> getEcho(final String echo) {
		return new ResponseEntity<>(echo, HttpStatus.OK);
	}



	public static void run(String[] args) throws Exception{

		OAuthConsumer consumer = new DefaultOAuthConsumer("{your oAuthKey}", "{your oAuthSecret}");

		OAuthProvider provider = new DefaultOAuthProvider("http://rest.immobilienscout24.de/restapi/security/oauth/request_token", "http://rest.immobilienscout24.de/restapi/security/oauth/access_token", "http://rest.immobilienscout24.de/restapi/security/oauth/confirm_access");

		System.out.println("Fetching request token...");

		String authUrl = provider.retrieveRequestToken(consumer, "http://www.google.de");

		String requestToken = consumer.getToken();

		String requestTokenSecret = consumer.getTokenSecret();

		System.out.println("Request token: " + requestToken);

		System.out.println("Token secret: " + requestTokenSecret);

		System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");

		System.out.println("Enter the verification code and hit ENTER when you're done:");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String verificationCode = br.readLine();

		System.out.println("Fetching access token...");

		provider.retrieveAccessToken(consumer, verificationCode.trim());

		String accessToken = consumer.getToken();

		String accessTokenSecret = consumer.getTokenSecret();

		System.out.println("Access token: " + accessToken);

		System.out.println("Token secret: " + accessTokenSecret);

		System.out.println("first call"); requestObjectApi(consumer);

		System.out.println("second call"); requestObjectApi(consumer);

		System.out.println("third call"); OAuthConsumer consumer2 = new DefaultOAuthConsumer("{your oAuthKey}", "{your oAuthSecret}");

		consumer2.setTokenWithSecret(accessToken, accessTokenSecret);

		requestObjectApi(consumer2);}



	private static void requestObjectApi(OAuthConsumer consumer) throws MalformedURLException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, UnsupportedEncodingException {

			System.out.println("#################################################################################################"); URL url = new URL("http://rest.immobilienscout24.de/restapi/api/offer/v1.0/user/{username}/realestate");

			HttpURLConnection apiRequest = (HttpURLConnection) url.openConnection();

			consumer.sign(apiRequest);

			System.out.println("Sending request...");

			apiRequest.connect();

			System.out.println("Expiration " + apiRequest.getExpiration());

			System.out.println("Timeout " + apiRequest.getConnectTimeout());

			System.out.println("URL " + apiRequest.getURL());

			System.out.println("Method " + apiRequest.getRequestMethod());

			System.out.println("Response: " + apiRequest.getResponseCode() + " " + apiRequest.getResponseMessage());

			System.out.println("#################################################################################################");
	}
}
