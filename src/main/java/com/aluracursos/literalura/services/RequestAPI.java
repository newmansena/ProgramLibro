package com.aluracursos.literalura.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestAPI
{

    // Hace un llamado a un API
    public String getData(String url)
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        String json = response.body();
        return json;
    }
}
