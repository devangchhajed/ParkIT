package com.codeencounter.nagarro.Config;

import com.codeencounter.nagarro.Adapters.ParkingSuggestionList;

import java.util.List;

public class AppConfig {

    public static final String baseurl = "http://10.177.12.58/ParkIT/api/";
    public static final String registerurl = baseurl + "users/register.php";
    public static final String loginurl = baseurl + "users/login.php";
    public static final String placeslist = baseurl + "places/getplaceslist.php";
    public static final String allplaceslist = baseurl + "places/getallplaceslist.php";
    public static final String booking = baseurl + "booking/bookparking.php";
    public static final String bookinghiatory = baseurl + "booking/gethistory.php";
    public static final String oneplaceslist = baseurl + "places/getoneplacesfromlist.php";

    public static Double lat=28.4916963, lng=77.0742998;


    public static List<ParkingSuggestionList> savedParkingList;




}
