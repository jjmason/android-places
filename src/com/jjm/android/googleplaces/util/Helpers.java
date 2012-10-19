/*
	This file is part of GooglePlaces.

    GooglePlaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GooglePlaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GooglePlaces.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.jjm.android.googleplaces.util;

import java.io.IOException;
 
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
 

/**
 * @author Jon Mason <jonathan.j.mason@gmail.com>
 * TODO Document Helpers
 */
public class Helpers {
	public static HttpRequestFactory createJsonRequestFactory(){
		return new NetHttpTransport().createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
				request.addParser(new JsonHttpParser(new JacksonFactory()));
			}
		});
	}
}