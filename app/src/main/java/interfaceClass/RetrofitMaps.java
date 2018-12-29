package interfaceClass;

import recorridosDistancia.ListRoute;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Javier on 1/8/17.
 */
public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     * AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM
     * AIzaSyAcdDIpWjo3D68HFvOp70UBLqh6uxlhdiU
     */
    @GET("api/directions/json?key=AIzaSyAcdDIpWjo3D68HFvOp70UBLqh6uxlhdiU")
    Call<ListRoute> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
