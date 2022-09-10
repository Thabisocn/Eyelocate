package com.example.user.eyelocate.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({

            "Content-Type:application/json",
            "Authorization:key=AAAA4heq7ts:APA91bGFUfqFYM7fhkAXxWFaDZJz_gw0o92MRMLxOORVnGixszJqRssTP6bE4wX6JWr3cnRylbLZ85Z6APByUktpyS2M3979yeHBX7DTpmqxHPfunYYRLR6gTKiiWURTMLMoHDte2Fxe"
    })

    @POST("fcm/send")
    Call<Response> sendNotification (@Body Sender body);
}
