package com.app.ahaspora.utilities;

import android.os.Environment;

import java.io.File;

/**
 * Created by aangjnr on 11/06/2017.
 */

public class Constants {


    public static final String PARENT_URL = "http://api.ahaspora.com/api/v1/";


    public static boolean IS_BOTTOM_NAVIGATION_VISIBLE = true;

    public static boolean IS_NAVIGATION_DRAWER_VISIBLE = false;
    public static String TYPE_POST = "Post";
    public static String TYPE_JOB = "Job";
    public static String TYPE_EVENT = "Event";
    public static String TYPE_RECOMMENDATION = "Recommendation";



    public static final int PLACE_PICKER_REQUEST = 101;
    public static final int PERMISSION_FINE_LOCATION = 102;
    public static final int PERMISSION_CALL = 222;
    public static final int CAMERA_REQUEST = 1888;
    public static final int PERMISSION_CAMERA = 4040;
    public static final int PERMISSION_STORAGE = 6060;
    public static final int TAKE_PICTURE_REQUEST = 103;
    public static final int SELECT_PHOTO_REQUEST = 104;
    public static final float NOT_AVAILABLE = -1;
    public static final float AVAILABLE = 1;
    public static final float NEUTRAL = 0;
    public static final int READ_SMS = 390;
    public static final String ROOKIE = "Rookie";
    public static final String PADWAN = "Padwan";
    public static final String OFFICER = "Officer";
    public static final String VIGILANTE = "Vigilante";
    public static final String SUPER_HERO = "Super Hero";
    public static String HOODCOD_DATABASE = "Hoodcop Database";
    public static String USERS = "users";
    public static String FEEDS = "feeds";
    public static String PEOPLE_WHO_STARRED = "stars";
    public static String COMMENTS = "comments";
     public static String USERS_PHONE = "phoneNo";
    public static String USER_NAME = "userName";
    public static String USER_AGE = "userAge";
    public static String USER_EMAIL = "userEmail";
    public static String USER_UID = "userUid";
    public static String USER_TOKEN = "userToken";
    public static String USER_PHOTO_LOCAL_URL = "userProfilePhotoLocal";
    public static String USER_RANKING = "ranking";
    public static String TAGLINE = "tagline";
    public static String IS_SIGNED_IN = "sign_in";
    public static String WAS_SIGNING_UP = "wasSigningUp";
    public static int NUMBER_OF_DEPOTS = 3;
    public static String DEFAULT_ADDRESS_UID = "default_address";
    public static String SHOULD_RESTORE = "restore";
    public static String ROOT_PICTURES = Environment
            .getExternalStorageDirectory() + File.separator + "Ahaspora" + File.separator + "Pictures";



    final public static String J_FULL_TIME = "Full Time";
    final public static String J_PART_TIME = "Part Time";
    final public static String J_TEMPORARY = "Temporary";
    final public static String J_FREELANCE = "Freelance";
    final public static String J_INTERNSHIP = "Internship";




    final public static String R_CATEGORY_ARTS_AND_CULTURE = "Arts and Culture";
    final public static String R_CLOTHING = "Clothing";
    final public static String R_CRAFTSMEN = "Craftsmen";
    final public static String R_EDUCATION = "Education";
    final public static String R_FINANCE = "Finance";
    final public static String R_FITNESS = "Fitness";
    final public static String R_FOOD = "Food";
    final public static String R_GROOMING = "Grooming";
    final public static String R_HEATHCARE = "Health Care";
    final public static String R_HOUSE_KEEPING = "House Keeping & Childcare";
    final public static String R_HR = "HR";
    final public static String R_IT = "Information Technology";
    final public static String R_LEGAL = "Legal";
    final public static String R_MERCHANDISE = "Merchandise";
    final public static String R_PHOTO_VIDEOGRAPHY = "Photography/Videography";
    final public static String R_REAL_ESTATE = "Real Estate";
    final public static String R_SERVICES = "Services";
    final public static String R_TRANSPORTATION = "Transportation";
    final public static String R_TRAVEL = "Travel";
    final public static String R_MEN_CLOTHING = "Men Clothing";
    final public static String R_DANCE_CHOREOGRAPHY = "Dance/Choreography";
    final public static String R_SECURITY =  "Security";

    final public static String R_ADVERTS =  "Advertising";

    final public static String R_WORKOUT =  "Workout";






    public static final String MTN_MOBILE_MONEY = "MTN Mobile Money";
    public static final String VODAFONE_CASH = "Vodafone Cash";
    public static final String VISA = "VISA";
    public static final String AIRTEL_MONEY = "Airtel Money";
    public static final String SLYDEPAY = "Slydepay";









}
