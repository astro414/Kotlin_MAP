package com.example.kotlinmap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.kotlinmap.Api.Api
import com.example.kotlinmap.Model.MyWeather
import com.example.kotlinmap.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    //update location
    lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    lateinit var binding:ActivityMainBinding

    lateinit var googleMap: GoogleMap
    var lat: Double? = 0.00
    var lon: Double? =0.00
    var lat1: Double? = 0.00
    var lon1: Double? =0.00

    lateinit var lastlocation:LatLng
    lateinit var lastlocation2:LatLng
    var a=0



    lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.googlemap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //sobirali

        buildLocationRequest()
        lastlocation()
        startLocationUpdates()

        binding.floatingactionbutton1.setOnClickListener {
            val location = CameraUpdateFactory.newLatLngZoom(
                lastlocation, 45f
            )
            googleMap.animateCamera(location);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        binding.cardview1.setOnClickListener {
            getdataretrofit()
        }







    }
    fun buildLocationRequest() {
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
    }

    fun lastlocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {

                //Toast.makeText(this@MainActivity,"${it.latitude}  ${it.longitude}", Toast.LENGTH_SHORT).show()

            }
        }
    }



    fun startLocationUpdates() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return

                for (location in p0.locations) {

                        lat=location.latitude
                        lon=location.longitude

                    onMapReady(googleMap)
                       // Toast.makeText(this@MainActivity, " ${location.latitude}/ ${location.longitude}", Toast.LENGTH_SHORT).show()



                }

            }
        }
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onMapReady(p0: GoogleMap) {

        googleMap=p0

        if (lat != null && lon != null) {
            //googleMap.clear()

            lastlocation = LatLng(lat!!, lon!!)
            var markerOptions = MarkerOptions()
                .position(lastlocation)
                .title("Sanjar HOME")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocationnn))

           lastlocation2 = LatLng(lat1!!, lon1!!)
            var markerOptions2 = MarkerOptions()
                .position(lastlocation2)
                .title("Sanjar HOME")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.busmap))


            googleMap.addMarker(markerOptions)
            googleMap.addMarker(markerOptions2)

            Handler().postDelayed({
                if(a==0){
                    a++
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastlocation, 15f))
                    binding.imageview1.visibility=View.GONE
                    binding.floatingactionbutton1.visibility=View.VISIBLE

                }

            }, 1000)


        }
    }


    fun getdataretrofit(){
        val retrofit= Retrofit.Builder()
            .baseUrl("https://community-open-weather-map.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api=retrofit.create(Api::class.java)
        val call:Call<MyWeather>?=api.getWeather(binding.edittext1.text.toString())
        call?.enqueue(object :Callback<MyWeather> {
            override fun onResponse(call: Call<MyWeather>, response: Response<MyWeather>) {
                if (response.isSuccessful){
                    var myWeather=response.body()
                    lat1=myWeather?.coord?.lat
                    lon1=myWeather?.coord?.lon
                    onMapReady(googleMap)
                    //Toast.makeText(this@MainActivity, " $lat1/ $lon1", Toast.LENGTH_SHORT).show()
                    val location = CameraUpdateFactory.newLatLngZoom(
                        lastlocation2, 12f
                    )
                    googleMap.animateCamera(location);

                }
            }

            override fun onFailure(call: Call<MyWeather>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}