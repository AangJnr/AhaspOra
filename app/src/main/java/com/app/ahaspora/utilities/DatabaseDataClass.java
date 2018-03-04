package com.app.ahaspora.utilities;

import android.provider.BaseColumns;

/**
 * Created by aangjnr on 07/02/2018.
 */

public class DatabaseDataClass {

    public static final String CATEGORY = "category";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " integer";
    private static final String COMMA = ",";


    public static final String ID = "id";


    public static final String CATEGORIES_TABLE = "categories_table";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_TYPE = "type";
    public static final String CATEGORY_PARENT_ID = "parent_id";
    public static final String CATEGORY_ORDER_NO = "order_id";
    public static final String DATE_CREATED = "created_date";
    public static final String DATE_UPDATED = "updated_date";




    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String IMAGE = "image";




    public static final String COMMENTS_TABLE = "comments_table";
    public static final String POST_ID = "post_id";
    public static final String COMMENT_BODY = "body";
    public static final String COMMENT_IMAGE_URL = "image";
    public static final String COMMENT_TYPE = "type";




    public static final String POSTS_TABLE = "posts_table";





    public static final String LOCATIONS_TABLE = "locations_table";
    public static final String LOCATION_NAME = "location_name";
    public static final String LOCATION_LAT = "latitude";
    public static final String LOCATION_LON = "longitude";









    public static final String CONTACTS_TABLE = "contacts_table";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE = "phone";
    public static final String CONTACT_EMAIL = "email";
    public static final String CONTACTS_WEBSITE = "website";







    public static final String RECOMMENDATIONS_TABLE = "recommendations_table";
    public static final String AUTHOR_NAME = "author_name";
    public static final String AUTHOR_IMAGE_URL = "author_image";
    public static final String RECOMMENDATIONS_CATEGORY_ID = "category_id";
    public static final String RECOMMENDATIONS_CATEGORY_NAME = "category_name";
    public static final String RECOMMENDATIONS_TITLE = "title";
    public static final String TAGLINE = "tagline";
    public static final String RECOMMENDATIONS_BODY = "body";
    public static final String RECOMMENDATIONS_IMAGE = "image";



    public static final String EVENTS_TABLE = "events_table";
    public static final String SUBTITLE = "subtitle";
    public static final String START_TIME = "s_time";
    public static final String END_TIME = "e_time";
    public static final String STATUS = "status";




    public static final String OWNERS_TABLE = "owners_table";
    public static final String ROLE_ID = "role_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String WEBSITE = "website";
    public static final String TWITTER = "twitter";
    public static final String LINKEDIN = "linkedin";
    public static final String AVATAR = "avatar";



    public static final String COMPANYS_TABLE = "companys_table";
    public static final String OWNER_ID = "owner_id";
    public static final String VIDEO = "video";
    public static final String DESCRIPTION = "desc";
    public static final String PHONE = "phone";




    public static final String JOBS_TABLE = "jobs_table";
    public static final String CATEGORY_ID = "category_id";
    public static final String COMPANY_ID = "company_id";
    public static final String FEATURED = "featured";

    public static final String EXPIRES = "expires";
    public static final String DEADLINE = "deadline";




    public static final String CREATE_JOBS_TABLE = "CREATE TABLE IF NOT EXISTS " + JOBS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + AUTHOR_NAME + TEXT_TYPE + COMMA
            + AUTHOR_IMAGE_URL + TEXT_TYPE + COMMA
            + CATEGORY_ID + INT_TYPE + COMMA
            + COMPANY_ID + INT_TYPE + COMMA
            + TITLE + TEXT_TYPE + COMMA
            + BODY + TEXT_TYPE + COMMA
            + IMAGE + TEXT_TYPE + COMMA
            + FEATURED + TEXT_TYPE + COMMA
            + EXPIRES + TEXT_TYPE + COMMA
            + DEADLINE + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";

    public static final String DELETE_JOBS_TABLE = "DROP TABLE IF EXISTS " + JOBS_TABLE;















    public static final String CREATE_COMPANYS_TABLE = "CREATE TABLE IF NOT EXISTS " + COMPANYS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + OWNER_ID + INT_TYPE + COMMA
            + NAME + TEXT_TYPE + COMMA
            + TAGLINE + TEXT_TYPE + COMMA
            + DESCRIPTION + TEXT_TYPE + COMMA
            + EMAIL + TEXT_TYPE + COMMA
            + LOCATION_NAME + TEXT_TYPE + COMMA
            + PHONE + TEXT_TYPE + COMMA
            + WEBSITE + TEXT_TYPE + COMMA
            + TWITTER + TEXT_TYPE + COMMA
            + VIDEO + TEXT_TYPE + COMMA
            + IMAGE + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";



    public static final String DELETE_COMPANYS_TABLE = "DROP TABLE IF EXISTS " + COMPANYS_TABLE;






    public static final String CREATE_OWNERS_TABLE = "CREATE TABLE IF NOT EXISTS " + OWNERS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + ROLE_ID + INT_TYPE + COMMA
            + NAME + TEXT_TYPE + COMMA
            + EMAIL + TEXT_TYPE + COMMA
            + WEBSITE + TEXT_TYPE + COMMA
            + TWITTER + TEXT_TYPE + COMMA
            + LINKEDIN + TEXT_TYPE + COMMA
            + AVATAR + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";



    public static final String DELETE_OWNERS_TABLE = "DROP TABLE IF EXISTS " + OWNERS_TABLE;











    public static final String CREATE_RECOMMENDATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + RECOMMENDATIONS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + AUTHOR_NAME + TEXT_TYPE + COMMA
            + AUTHOR_IMAGE_URL + TEXT_TYPE + COMMA
            + RECOMMENDATIONS_CATEGORY_ID + TEXT_TYPE + COMMA
            + RECOMMENDATIONS_CATEGORY_NAME + TEXT_TYPE + COMMA
            + RECOMMENDATIONS_TITLE + TEXT_TYPE + COMMA
            + TAGLINE + TEXT_TYPE + COMMA
            + RECOMMENDATIONS_BODY + TEXT_TYPE + COMMA
            + RECOMMENDATIONS_IMAGE + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";

    public static final String DELETE_RECOMMENDATIONS_TABLE = "DROP TABLE IF EXISTS " + RECOMMENDATIONS_TABLE;





    public static final String CREATE_COMMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENTS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + POST_ID + INT_TYPE + COMMA
            + AUTHOR_NAME + TEXT_TYPE + COMMA
            + AUTHOR_IMAGE_URL + TEXT_TYPE + COMMA
            + COMMENT_BODY + TEXT_TYPE + COMMA
            + COMMENT_IMAGE_URL + TEXT_TYPE + COMMA
            + COMMENT_TYPE + TEXT_TYPE + COMMA
            + CATEGORY + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";

    public static final String DELETE_COMMENTS_TABLE = "DROP TABLE IF EXISTS " + COMMENTS_TABLE;







    public static final String CREATE_POSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + POSTS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + AUTHOR_NAME + TEXT_TYPE + COMMA
            + AUTHOR_IMAGE_URL + TEXT_TYPE + COMMA
            + CATEGORY_PARENT_ID + TEXT_TYPE + COMMA
            + TITLE + TEXT_TYPE + COMMA
            + BODY + TEXT_TYPE + COMMA
            + IMAGE + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";

    public static final String DELETE_POSTS_TABLE = "DROP TABLE IF EXISTS " + POSTS_TABLE;







    public static final String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + AUTHOR_NAME + TEXT_TYPE + COMMA
            + AUTHOR_IMAGE_URL + TEXT_TYPE + COMMA
            + TITLE + TEXT_TYPE + COMMA
            + SUBTITLE + TEXT_TYPE + COMMA
            + BODY + TEXT_TYPE + COMMA
            + IMAGE + TEXT_TYPE + COMMA
            + START_TIME + TEXT_TYPE + COMMA
            + END_TIME + TEXT_TYPE + COMMA
            + STATUS + TEXT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";

    public static final String DELETE_EVENTS_TABLE = "DROP TABLE IF EXISTS " + EVENTS_TABLE;






    public static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + CATEGORIES_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + CATEGORY_NAME + TEXT_TYPE + COMMA
            + CATEGORY_TYPE + TEXT_TYPE + COMMA
            + CATEGORY_PARENT_ID + INT_TYPE + COMMA
            + CATEGORY_ORDER_NO + INT_TYPE + COMMA
            + DATE_CREATED + TEXT_TYPE + COMMA
            + DATE_UPDATED + " TEXT" + ")";



    public static final String DELETE_CATEGORIES_TABLE = "DROP TABLE IF EXISTS " + CATEGORIES_TABLE;




    public static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + LOCATIONS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + LOCATION_NAME + TEXT_TYPE + COMMA
            + LOCATION_LAT + TEXT_TYPE + COMMA
            + LOCATION_LON + TEXT_TYPE + COMMA
            + CATEGORY + " TEXT" + ")";



    public static final String DELETE_LOCATIONS_TABLE = "DROP TABLE IF EXISTS " + LOCATIONS_TABLE;






    public static final String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE + "("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + ID + INT_TYPE + COMMA
            + CONTACT_NAME + TEXT_TYPE + COMMA
            + CONTACT_PHONE + TEXT_TYPE + COMMA
            + CONTACT_EMAIL + TEXT_TYPE + COMMA
            + CATEGORY + TEXT_TYPE + COMMA
            + CONTACTS_WEBSITE + " TEXT" + ")";



    public static final String DELETE_CONTACTS_TABLE = "DROP TABLE IF EXISTS " + CONTACTS_TABLE;



















}
