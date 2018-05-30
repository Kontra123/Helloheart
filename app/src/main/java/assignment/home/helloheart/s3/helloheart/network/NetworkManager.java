package assignment.home.helloheart.s3.helloheart.network;

import android.util.Log;

import java.util.List;

import assignment.home.helloheart.s3.helloheart.data.BloodTestConfigData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AmirG on 5/28/2018.
 */

public class NetworkManager {

    private APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);;
    private NetworkManagerListener networkManagerListener;

    public static final int RESPONSE_OK = 200;

    public static synchronized NetworkManager getInstance()
    {
        return SingeltonHolder.NETWORK_MANAGER;
    }

    private static class SingeltonHolder
    {
        private static final NetworkManager NETWORK_MANAGER = new NetworkManager();
    }


    public void setNetworkManagerListener(NetworkManagerListener networkManagerListener) {
        this.networkManagerListener = networkManagerListener;
    }

    public interface NetworkManagerListener {
        public void onResponseFromServerSucceeded(BloodTestConfigData serverData);
        public void onResponseFromServerFailed();
    }



    public void sendNetworkStatusToServer() {
        Call<BloodTestConfigData> call = apiInterface.getTestConfigData();
        call.enqueue(new Callback<BloodTestConfigData>() {
            @Override
            public void onResponse(Call<BloodTestConfigData> call, Response<BloodTestConfigData> response) {

                BloodTestConfigData serverData = response.body();
                int responseCode = response.code();
                if(responseCode == RESPONSE_OK) {
                    networkManagerListener.onResponseFromServerSucceeded(serverData);
                }
                else {
                    networkManagerListener.onResponseFromServerFailed();
                }

            }

            @Override
            public void onFailure(Call<BloodTestConfigData> call, Throwable t) {

                call.cancel();

                networkManagerListener.onResponseFromServerFailed();
            }
        });
    }

}
