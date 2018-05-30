package assignment.home.helloheart.s3.helloheart.network;


import assignment.home.helloheart.s3.helloheart.data.BloodTestConfigData;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterface {

     @GET("bloodTestConfig.json")
     Call<BloodTestConfigData> getTestConfigData();

}