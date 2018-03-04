package com.app.ahaspora.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.app.ahaspora.models.Author;
import com.app.ahaspora.models.Category;
import com.app.ahaspora.models.Comment;
import com.app.ahaspora.models.Company;
import com.app.ahaspora.models.Contact;
import com.app.ahaspora.models.Event;
import com.app.ahaspora.models.Job;
import com.app.ahaspora.models.Location;
import com.app.ahaspora.models.Owner;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.models.Recommendation;
import com.app.ahaspora.models.Time;
import com.stepstone.apprating.C;

import java.util.ArrayList;
import java.util.List;

import static com.app.ahaspora.utilities.Constants.*;
import static com.app.ahaspora.utilities.DatabaseDataClass.*;
import static com.app.ahaspora.utilities.DatabaseDataClass.TAGLINE;


/**
 * Created by aangjnr on 24/06/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    static final String TAG = DatabaseHelper.class.getSimpleName();
    static final String DB_NAME = "ahaspora.db";
    static final int DB_VERSION = 5;

    private static DatabaseHelper instance;
    Context _context;
    SharedPreferences prefs;
    private SQLiteDatabase db;


    private DatabaseHelper(Context ctx) { //
        super(ctx, DB_NAME, null, DB_VERSION);
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        db = this.getWritableDatabase();
        _context = ctx;

    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new DatabaseHelper(ctx.getApplicationContext());

        }
        return instance;
    }


    public void beginTransaction() {
        db.beginTransaction();
    }

    public void endTransaction(boolean success) {
        if (success) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_LOCATIONS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_RECOMMENDATIONS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_COMMENTS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_OWNERS_TABLE);

        db.execSQL(CREATE_COMPANYS_TABLE);
        db.execSQL(CREATE_JOBS_TABLE);





    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_CATEGORIES_TABLE);
        db.execSQL(DELETE_LOCATIONS_TABLE);
        db.execSQL(DELETE_CONTACTS_TABLE);
        db.execSQL(DELETE_RECOMMENDATIONS_TABLE);
        db.execSQL(DELETE_POSTS_TABLE);
        db.execSQL(DELETE_COMMENTS_TABLE);
        db.execSQL(DELETE_EVENTS_TABLE);
        db.execSQL(DELETE_OWNERS_TABLE);
        db.execSQL(DELETE_COMPANYS_TABLE);
        db.execSQL(DELETE_JOBS_TABLE);



        onCreate(db);
    }


    public void deleteAllTables() {

        db.execSQL(DELETE_CATEGORIES_TABLE);

        onCreate(db);

    }




    //CATEGORY

    public boolean addACategory(Category category) {
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, category.getId());
            contentValues.put(CATEGORY_NAME, category.getName());

            contentValues.put(CATEGORY_PARENT_ID, category.getParentId());
            contentValues.put(CATEGORY_ORDER_NO, category.getOrderNo());
            contentValues.put(CATEGORY_TYPE, category.getType());
            contentValues.put(DATE_CREATED, category.getDateCreated());
            contentValues.put(DATE_UPDATED, category.getLastUpdated());


            db.insert(CATEGORIES_TABLE, null, contentValues);

            Log.d(TAG, "CATEGORY WITH ID " + category.getId() + ", NAME " + category.getName() + " AND TYPE " + category.getType() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteCategory(int id) {

        return db.delete(CATEGORIES_TABLE, ID + " = ? ", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean deleteCategoriesTable() {

        try {


            db.execSQL("DELETE FROM " + CATEGORIES_TABLE);

            Log.i("DATABASE", "CATEGORIES TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Category getCategory(Integer id) {

        Category category = null;
        Cursor cursor = null;
        try {
            category = new Category();


            String selectQuery = "SELECT  * FROM " + CATEGORIES_TABLE + " WHERE " +
                    ID + " ='" + id +"'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        category.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        category.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));
                        category.setParentId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));
                        category.setOrderNo(cursor.getInt(cursor.getColumnIndex(CATEGORY_ORDER_NO)));
                        category.setType(cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE)));
                        category.setDateCreated(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        category.setLastUpdated(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        Log.d(TAG, "CATEGORY WITH ID " + category.getId() + ", NAME " + category.getName() + " AND TYPE " + category.getType() + " FOUND");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return category;

    }

    public Category getCategoryById(Integer id) {

        Category category = null;
        Cursor cursor = null;
        try {
            category = new Category();


            String selectQuery = "SELECT  * FROM " + CATEGORIES_TABLE + " WHERE " +
                    ID + " ='" + id;
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        category.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        category.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));
                        category.setParentId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));
                        category.setOrderNo(cursor.getInt(cursor.getColumnIndex(CATEGORY_ORDER_NO)));
                        category.setType(cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE)));
                        category.setDateCreated(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        category.setLastUpdated(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        Log.d(TAG, "CATEGORY WITH ID " + category.getId() + ", NAME " + category.getName() + " AND TYPE " + category.getType() + " FOUND");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return category;

    }




    public String getCategoryNameById(Integer id) {



        String value = "";
        Cursor cursor = null;
        try {


            String selectQuery = "SELECT  * FROM " + CATEGORIES_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                         value = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return value;

    }


    public List<Category> getAllCategoriesByType(String type) {

        List<Category> categories = null;
        Cursor cursor = null;

        try {

            categories = new ArrayList<>();

            String selectQuery = "SELECT  * FROM " + CATEGORIES_TABLE + " WHERE " + CATEGORY_TYPE + " ='" + type + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())


                    do {
                    Category category = new Category();
                        category.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        category.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));
                        category.setParentId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));
                        category.setOrderNo(cursor.getInt(cursor.getColumnIndex(CATEGORY_ORDER_NO)));
                        category.setType(cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE)));
                        category.setDateCreated(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        category.setLastUpdated(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        categories.add(category);
                        Log.d(TAG, "CATEGORY WITH ID " + category.getId() + ", NAME " + category.getName() + " AND TYPE " + category.getType() + " FOUND");


                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (cursor != null)
                cursor.close();
        }

        return categories;


    }



    public Boolean hasCategoryBeenUpdated(int id, String dateUpdated){

        Boolean value = false;
        Recommendation recommendation = null;
        Cursor cursor = null;
        try{

            String selectQuery = "SELECT  * FROM " + CATEGORIES_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        value = !dateUpdated.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        //Todo get comments list


                    } while (cursor.moveToNext());


            }else value = null;

        }catch(Exception e){
            value = false;
            e.printStackTrace();

        }finally {

            if(cursor != null)
                cursor.close();
        }



        return value;
    }








    //LOCATION

    public boolean addALocation(int id, Location Location, String category) {
        try {

            Location.setId(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, Location.getId());
            contentValues.put(LOCATION_NAME, Location.getName());
            contentValues.put(LOCATION_LAT, Location.getLat());
            contentValues.put(LOCATION_LON, Location.getLon());
            contentValues.put(CATEGORY, category);

            db.insert(LOCATIONS_TABLE, null, contentValues);

            Log.d(TAG, "Location WITH ID " + Location.getId() + "AND NAME " + Location.getName() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteLocation(String id, String category) {

        return db.delete(LOCATIONS_TABLE, ID + " = ? AND " + CATEGORY + " = ?", new String[]{id, category}) > 0;

    }

    public boolean deleteLocationsTable() {

        try {


            db.execSQL("DELETE FROM " + LOCATIONS_TABLE);

            Log.i("DATABASE", "LOCATIONS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Location getLocation(Integer id, String category) {

        Location Location = null;
        Cursor cursor = null;
        try {
            Location = new Location();


            String selectQuery = "SELECT  * FROM " + LOCATIONS_TABLE + " WHERE " +
                    ID + " ='" + id + "' AND " + CATEGORY + " ='" + category + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        Location.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        Location.setName(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
                        Location.setLat(cursor.getString(cursor.getColumnIndex(LOCATION_LAT)));
                        Location.setLon(cursor.getString(cursor.getColumnIndex(LOCATION_LON)));

                        Log.d(TAG, "Location WITH ID " + Location.getId() + "AND NAME " + Location.getName() + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return Location;

    }






    //CONTACTS

    public boolean addAContact(int id, Contact Contact, String category) {
        try {

            Contact.setId(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, Contact.getId());
            contentValues.put(CONTACT_NAME, Contact.getName());
            contentValues.put(CONTACT_PHONE, Contact.getPhone());
            contentValues.put(CONTACT_EMAIL, Contact.getEmail());
            contentValues.put(CONTACTS_WEBSITE, Contact.getWebsite());
            contentValues.put(CATEGORY, category);


            db.insert(CONTACTS_TABLE, null, contentValues);

            Log.d(TAG, "Contact WITH ID " + Contact.getId() + "AND NAME " + Contact.getName() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteContact(int id, String category) {

        return db.delete(CONTACTS_TABLE, ID + " = ? AND " + CATEGORY + " = ?", new String[]{String.valueOf(id), category}) > 0;

    }

    public boolean deleteContactsTable() {

        try {


            db.execSQL("DELETE FROM " + CONTACTS_TABLE);

            Log.i("DATABASE", "CONTACTS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Contact getContact(Integer id, String category) {

        Contact Contact = null;
        Cursor cursor = null;
        try {
            Contact = new Contact();


            String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE + " WHERE " +
                    ID + " ='" + id + "' AND " + CATEGORY + " ='" + category + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        Contact.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        Contact.setName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
                        Contact.setPhone(cursor.getString(cursor.getColumnIndex(CONTACT_PHONE)));
                        Contact.setEmail(cursor.getString(cursor.getColumnIndex(CONTACT_EMAIL)));
                        Contact.setWebsite(cursor.getString(cursor.getColumnIndex(CONTACTS_WEBSITE)));


                        Log.d(TAG, "Contact WITH ID " + Contact.getId() + "AND NAME " + Contact.getName() + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return Contact;

    }










    //RECOMMENDATIONS

    public boolean addARecommendation(Recommendation recommendation) {
        try {

            addAContact(recommendation.getId(), recommendation.getContact(), TYPE_RECOMMENDATION);
            addALocation(recommendation.getId(), recommendation.getLocation(), TYPE_RECOMMENDATION);

            //Todo Add comments List

            //Todo Im not saving category id, please fix

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, recommendation.getId());
            contentValues.put(AUTHOR_NAME, recommendation.getAuthor().getName());
            contentValues.put(AUTHOR_IMAGE_URL, recommendation.getAuthor().getAvatar());
            contentValues.put(RECOMMENDATIONS_CATEGORY_ID, recommendation.getCategory().getId());
            contentValues.put(RECOMMENDATIONS_CATEGORY_NAME, recommendation.getCategory().getName());
            contentValues.put(RECOMMENDATIONS_TITLE, recommendation.getTitle());
            contentValues.put(TAGLINE, recommendation.getTagline());
            contentValues.put(RECOMMENDATIONS_BODY, recommendation.getBody());
            contentValues.put(RECOMMENDATIONS_IMAGE, recommendation.getImage());
            contentValues.put(DATE_CREATED, recommendation.getCreatedAt());
            contentValues.put(DATE_UPDATED, recommendation.getUpdatedAt());

            db.insert(RECOMMENDATIONS_TABLE, null, contentValues);



            Log.d(TAG, "recommendation WITH ID " + recommendation.getId() + "AND TITLE " + recommendation.getTitle() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteRecommendation(int id) {

        return db.delete(RECOMMENDATIONS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean deleteRecommendationsTable() {

        try {


            db.execSQL("DELETE FROM " + RECOMMENDATIONS_TABLE);

            Log.i("DATABASE", "RECOMMENDATIONS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Recommendation getRecommendation(String id) {

        Recommendation recommendation = null;
        Cursor cursor = null;
        try {
            recommendation = new Recommendation();


            String selectQuery = "SELECT  * FROM " + RECOMMENDATIONS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        recommendation.setId(cursor.getInt(cursor.getColumnIndex(ID)));

                        recommendation.setTitle(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_TITLE)));
                        recommendation.setTagline(cursor.getString(cursor.getColumnIndex(TAGLINE)));
                        recommendation.setBody(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_BODY)));
                        recommendation.setImage(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_IMAGE)));
                        recommendation.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        recommendation.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));


                        recommendation.setAuthorName(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)));
                        recommendation.setAuthorAvatar(cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL)));
                        recommendation.setCategoryId(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID)));

                        //recommendation.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        // recommendation.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID))));
                        // recommendation.setContact(getContact(recommendation.getId()));
                        // recommendation.setLocation(getLocation(recommendation.getId()));



                        //Todo get comments list

                        Log.d(TAG, "recommendation WITH ID " + recommendation.getId() + "AND NAME " + recommendation.getTitle() + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return recommendation;

    }

    public List<Recommendation> getRecommendationsListByCategory(String categoryId) {

        List<Recommendation> recommendations = new ArrayList<>();

        Recommendation recommendation;
        Cursor cursor = null;
        try {
            recommendation = new Recommendation();


            String selectQuery = "SELECT  * FROM " + RECOMMENDATIONS_TABLE + " WHERE " +
                    RECOMMENDATIONS_CATEGORY_NAME + " ='" + categoryId + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        recommendation.setId(cursor.getInt(cursor.getColumnIndex(ID)));

                        recommendation.setTitle(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_TITLE)));
                        recommendation.setTagline(cursor.getString(cursor.getColumnIndex(TAGLINE)));
                        recommendation.setBody(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_BODY)));
                        recommendation.setImage(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_IMAGE)));
                        recommendation.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        recommendation.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        recommendation.setAuthorName(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)));
                        recommendation.setAuthorAvatar(cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL)));
                        recommendation.setCategoryId(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID)));

                        //recommendation.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                       // recommendation.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID))));
                       // recommendation.setContact(getContact(recommendation.getId()));
                       // recommendation.setLocation(getLocation(recommendation.getId()));


                        recommendations.add(recommendation);

                        //Todo get comments list

                        Log.d(TAG, "recommendation WITH ID " + recommendation.getId() + "AND NAME " + recommendation.getTitle() + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return recommendations;

    }

    public List<Recommendation> getAllRecommendations() {

        List<Recommendation> recommendations = new ArrayList<>();

          ;
        Cursor cursor = null;
        try {


            String selectQuery = "SELECT  * FROM " + RECOMMENDATIONS_TABLE;
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {
                        Recommendation recommendation = new Recommendation();
                        recommendation.setId(cursor.getInt(cursor.getColumnIndex(ID)));

                        recommendation.setTitle(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_TITLE)));
                        recommendation.setTagline(cursor.getString(cursor.getColumnIndex(TAGLINE)));
                        recommendation.setBody(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_BODY)));
                        recommendation.setImage(cursor.getString(cursor.getColumnIndex(RECOMMENDATIONS_IMAGE)));
                        recommendation.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        recommendation.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));


                        recommendation.setAuthorName(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)));
                        recommendation.setAuthorAvatar(cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL)));
                        recommendation.setCategoryId(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID)));

                        recommendation.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        recommendation.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(RECOMMENDATIONS_CATEGORY_ID))));
                        recommendation.setContact(getContact(recommendation.getId(), TYPE_RECOMMENDATION));
                        recommendation.setLocation(getLocation(recommendation.getId(), TYPE_RECOMMENDATION));


                        recommendations.add(recommendation);

                        //Todo get comments list

                        Log.d(TAG, "recommendation WITH ID " + recommendation.getId() + " AND NAME " + recommendation.getTitle() + " FETCHED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return recommendations;

    }






    public Boolean hasRecommendationBeenUpdated(int id, String dateUpdated){

        Boolean value = false;
        Recommendation recommendation = null;
        Cursor cursor = null;
        try{

            String selectQuery = "SELECT  * FROM " + RECOMMENDATIONS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        value = !dateUpdated.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        //Todo get comments list

                        Log.d(TAG, "recommendation WITH ID " + id + " FOUND");

                    } while (cursor.moveToNext());


            }else value = null;

        }catch(Exception e){
            value = false;
            e.printStackTrace();

        }finally {

            if(cursor != null)
            cursor.close();
        }



        return value;
    }





    //COMMENTS

    public boolean addAComment(int postId, Comment comment, String category) {
        try {

            comment.setPostId(postId);

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, comment.getId());
            contentValues.put(POST_ID, comment.getPostId());
            contentValues.put(AUTHOR_NAME, comment.getAuthor().getName());
            contentValues.put(AUTHOR_IMAGE_URL, comment.getAuthor().getAvatar());
            contentValues.put(COMMENT_BODY, comment.getBody());
            contentValues.put(CATEGORY_TYPE, comment.getType());
            contentValues.put(COMMENT_IMAGE_URL, comment.getImage());
            contentValues.put(CATEGORY, category);
            contentValues.put(DATE_CREATED, comment.getCreatedAt());
            contentValues.put(DATE_UPDATED, comment.getUpdatedAt());




            db.insert(COMMENTS_TABLE, null, contentValues);

            Log.d(TAG, "COMMENT WITH ID " + comment.getId() + " AND POST ID " + comment.getPostId() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteComment(int postId, int id, String category) {

        return db.delete(COMMENTS_TABLE, POST_ID + " = ? AND " + ID + " = ? AND " + CATEGORY + " =?", new String[]{String.valueOf(postId), String.valueOf(id), category}) > 0;

    }

    public boolean deleteCommentsTable() {

        try {


            db.execSQL("DELETE FROM " + COMMENTS_TABLE);

            Log.i("DATABASE", "COMMENTS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Comment getComment(int postId, int id, String category) {

        Comment comment;
        Cursor cursor = null;
        try {
            comment = new Comment();


            String selectQuery = "SELECT  * FROM " + COMMENTS_TABLE + " WHERE "
                    + POST_ID + " = '" + postId + "' AND " + ID + " = '" + id + "' AND " + CATEGORY + " ='" + category + "'";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        comment.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        comment.setPostId(cursor.getInt(cursor.getColumnIndex(POST_ID)));
                        comment.setBody(cursor.getString(cursor.getColumnIndex(COMMENT_BODY)));
                        comment.setImage(cursor.getString(cursor.getColumnIndex(COMMENT_IMAGE_URL)));
                        comment.setType(cursor.getString(cursor.getColumnIndex(COMMENT_TYPE)));
                        comment.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        comment.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        comment.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));


                        Log.d(TAG, "Comment WITH ID " + comment.getId() + " WITH POST ID " + comment.getPostId() + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return comment;

    }

    public List<Comment> getAllComments(int postId, String category) {
        List<Comment> comments = new ArrayList<>();

        Cursor cursor = null;
        try {
            Comment comment = new Comment();

            String selectQuery = "SELECT  * FROM " + COMMENTS_TABLE + " WHERE "
                    + POST_ID + " = '" + postId + "' AND " + CATEGORY + " ='" + category + "'";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        comment.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        comment.setPostId(cursor.getInt(cursor.getColumnIndex(POST_ID)));
                        comment.setBody(cursor.getString(cursor.getColumnIndex(COMMENT_BODY)));
                        comment.setImage(cursor.getString(cursor.getColumnIndex(COMMENT_IMAGE_URL)));
                        comment.setType(cursor.getString(cursor.getColumnIndex(COMMENT_TYPE)));
                        comment.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        comment.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        comment.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));


                        comments.add(comment);
                        Log.d(TAG, "Comment WITH ID " + comment.getId() + " WITH POST ID " + comment.getPostId() + " OBTAINED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return comments;

    }










    //Feed
    public boolean addAPost(Post post) {
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, post.getId());
            contentValues.put(AUTHOR_NAME, post.getAuthor().getName());
            contentValues.put(AUTHOR_IMAGE_URL, post.getAuthor().getAvatar());
            contentValues.put(CATEGORY_PARENT_ID, post.getCategory().getId());
            contentValues.put(TITLE, post.getTitle());
            contentValues.put(BODY, post.getBody());
            contentValues.put(IMAGE, post.getImage());
            contentValues.put(DATE_CREATED, post.getCreatedAt());
            contentValues.put(DATE_UPDATED, post.getUpdatedAt());


            if(post.getComments() != null){

                for (Comment c : post.getComments())
                    addAComment(post.getId(), c, TYPE_POST);

            }



            db.insert(POSTS_TABLE, null, contentValues);

            Log.d(TAG, "POST WITH ID " + post.getId() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deletePost(int id) {

        return db.delete(POSTS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }


    public void deleteAllCommentsOfObject(List<Comment> comments, String category) {

        for(Comment c : comments)
        deleteComment(c.getPostId(), c.getId(), category);


    }


    public boolean deletePostsTable() {

        try {


            db.execSQL("DELETE FROM " + POSTS_TABLE);

            Log.i("DATABASE", "POSTS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Post getPost(int id) {

        Post post;
        Cursor cursor = null;
        try {
            post = new Post();


            String selectQuery = "SELECT  * FROM " + POSTS_TABLE + " WHERE "
                    + POST_ID + " = '" + id + "'";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        post.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        post.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        post.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                        post.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        post.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));
                        post.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));

                        post.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        post.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID))));
                        post.setComments(getAllComments(id, TYPE_POST));
                        Log.d(TAG, "POST WITH ID " +id + " INSERTED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return post;

    }



    public List<Post> getAllPostById( int id) {

        List<Post> posts = new ArrayList<>();
        Cursor cursor = null;
        try {


            String selectQuery = "SELECT  * FROM " + POSTS_TABLE + " WHERE "
                    + POST_ID + " = '" + id + "'";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        Post post = new Post();

                        post.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        post.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        post.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                        post.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        post.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));
                        post.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));

                        post.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        post.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID))));
                        post.setComments(getAllComments(id, TYPE_POST));
                        Log.d(TAG, "POST WITH ID " +id + " INSERTED");

                        posts.add(post);

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return posts;

    }



    public List<Post> getAllPost() {

        List<Post> posts = new ArrayList<>();
        Cursor cursor = null;
        try {


            String selectQuery = "SELECT  * FROM " + POSTS_TABLE + "";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        Post post = new Post();

                        post.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        post.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        post.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        post.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                        post.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        post.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));
                        post.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID)));

                        post.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        post.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT_ID))));
                        post.setComments(getAllComments(post.getId(), TYPE_POST));
                        Log.d(TAG, "POST WITH ID " + post.getComments() + " INSERTED");

                        posts.add(post);

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return posts;

    }


    public Boolean hasPostBeenUpdated(int id, String dateUpdated){

        Boolean value = false;
         Cursor cursor = null;

        try{
            String selectQuery = "SELECT  * FROM " + POSTS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        value = !dateUpdated.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        //Todo get comments list

                        Log.d(TAG, "Post WITH ID " + id + " FOUND");

                    } while (cursor.moveToNext());


            }else value = null;

        }catch(Exception e){
            value = false;
            e.printStackTrace();

        }finally {

            if(cursor != null)
                cursor.close();
        }



        return value;
    }









    public boolean addAnEvent(Event event){

        try{

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, event.getId());
        contentValues.put(AUTHOR_NAME, event.getAuthor().getName());
        contentValues.put(AUTHOR_IMAGE_URL, event.getAuthor().getAvatar());
        contentValues.put(TITLE, event.getTitle());
        contentValues.put(SUBTITLE, event.getSubtitle());
        contentValues.put(BODY, event.getBody());
        contentValues.put(IMAGE, event.getImage());
        contentValues.put(START_TIME, event.getTime().getStart());
        contentValues.put(END_TIME, event.getTime().getEnd());
        contentValues.put(DATE_CREATED, event.getCreatedAt());
        contentValues.put(DATE_UPDATED, event.getUpdatedAt());



        if(event.getComments() != null)
        for(Comment c : event.getComments())
            addAComment(event.getId(), c, TYPE_EVENT);

        addALocation(event.getId(), event.getLocation(), TYPE_EVENT);


        db.insert(EVENTS_TABLE, null, contentValues);

        Log.d(TAG, "EVENT WITH ID " + event.getId() + " INSERTED");

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }

        return true;


    }



    public boolean deleteEvent(int id) {

        return db.delete(EVENTS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }



    public List<Event> getAllEvents() {

        List<Event> events = new ArrayList<>();
        Cursor cursor = null;
        try {


            String selectQuery = "SELECT  * FROM " + EVENTS_TABLE + "";

            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        Event event = new Event();

                        event.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        event.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        event.setSubtitle(cursor.getString(cursor.getColumnIndex(SUBTITLE)));

                        event.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        event.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));


                        event.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        event.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));

                        event.setTime(new Time(cursor.getString(cursor.getColumnIndex(START_TIME)), cursor.getString(cursor.getColumnIndex(END_TIME))));
                        event.setLocation(getLocation(event.getId(), TYPE_EVENT));
                        event.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        event.setComments(getAllComments(event.getId(), TYPE_EVENT));
                        Log.d(TAG, "EVENT WITH ID " + event.getId() + " INSERTED");

                        events.add(event);

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return events;

    }





    public Boolean hasEventBeenUpdated(int id, String dateUpdated){

        Boolean value = false;
        Cursor cursor = null;

        try{
            String selectQuery = "SELECT  * FROM " + EVENTS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        value = !dateUpdated.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));


                        Log.d(TAG, "EVENT WITH ID " + id + " FOUND");

                    } while (cursor.moveToNext());


            }else value = null;

        }catch(Exception e){
            value = false;
            e.printStackTrace();

        }finally {

            if(cursor != null)
                cursor.close();
        }



        return value;
    }










    //OWNER's TABLE
    ///////////////////////////


    public boolean addOwner(Owner owner) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, owner.getId());
            contentValues.put(ROLE_ID, owner.getRole_id());
            contentValues.put(NAME, owner.getName());
            contentValues.put(EMAIL, owner.getEmail());
            contentValues.put(WEBSITE, owner.getWebsite());
            contentValues.put(TWITTER, owner.getTwitter());
            contentValues.put(LINKEDIN, owner.getLinkedin());
            contentValues.put(AVATAR, owner.getAvatar());
            contentValues.put(DATE_CREATED, owner.getCreatedAt());
            contentValues.put(DATE_UPDATED, owner.getUpdated_at());

            db.insert(OWNERS_TABLE, null, contentValues);



            Log.d(TAG, "Owner WITH ID " + owner.getId() + "AND TITLE " + owner.getName() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteOwner(int id) {

        return db.delete(OWNERS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean deleteOwnersTable() {

        try {


            db.execSQL("DELETE FROM " + OWNERS_TABLE);

            Log.i("DATABASE", "OWNERS TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Owner getOwner(int id) {

        Owner o = null;
        Cursor cursor = null;
        try {
            o = new Owner();


            String selectQuery = "SELECT  * FROM " + OWNERS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        o.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        o.setRole_id(cursor.getInt(cursor.getColumnIndex(ROLE_ID)));

                        o.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                        o.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                        o.setWebsite(cursor.getString(cursor.getColumnIndex(WEBSITE)));
                        o.setTwitter(cursor.getString(cursor.getColumnIndex(TWITTER)));
                        o.setLinkedin(cursor.getString(cursor.getColumnIndex(LINKEDIN)));
                        o.setAvatar(cursor.getString(cursor.getColumnIndex(AVATAR)));


                        o.setUpdated_at(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));
                        o.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));


                        Log.d(TAG, "recommendation WITH ID " + o.getId() + "AND NAME " + o.getName() + " RETRIEVED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return o;

    }







    public boolean addCompany(Company company) {
        try {


            addOwner(company.getOwner());

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, company.getId());
            contentValues.put(OWNER_ID, company.getOwner_id());
            contentValues.put(NAME, company.getName());
            contentValues.put(TAGLINE, company.getTagline());
            contentValues.put(DESCRIPTION, company.getDescription());
            contentValues.put(EMAIL, company.getEmail());
            contentValues.put(LOCATION_NAME, company.getLocation());
            contentValues.put(PHONE, company.getPhone());
            contentValues.put(WEBSITE, company.getWebsite());
            contentValues.put(TWITTER, company.getTwitter());
            contentValues.put(VIDEO, company.getVideo());
            contentValues.put(IMAGE, company.getImage());
            contentValues.put(DATE_CREATED, company.getCreatedAt());
            contentValues.put(DATE_UPDATED, company.getUpdated_at());

            db.insert(COMPANYS_TABLE, null, contentValues);


            Log.d(TAG, "COMPANY WITH ID " + company.getId() + "AND TITLE " + company.getName() + " INSERTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteCompany(int id) {

        return db.delete(COMPANYS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean deleteCompanysTable() {

        try {


            db.execSQL("DELETE FROM " + COMPANYS_TABLE);

            Log.i("DATABASE", "COMPANY'S TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Company getCompany(int id) {

        Company c;
        Cursor cursor = null;
        try {
            c = new Company();


            String selectQuery = "SELECT  * FROM " + COMPANYS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                    c.setOwner(getOwner(cursor.getInt(cursor.getColumnIndex(OWNER_ID))));

                        c.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        c.setOwner_id(cursor.getInt(cursor.getColumnIndex(OWNER_ID)));
                        c.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                        c.setTagline(cursor.getString(cursor.getColumnIndex(TAGLINE)));
                        c.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                        c.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                        c.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
                        c.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
                        c.setWebsite(cursor.getString(cursor.getColumnIndex(WEBSITE)));
                        c.setTwitter(cursor.getString(cursor.getColumnIndex(TWITTER)));
                        c.setVideo(cursor.getString(cursor.getColumnIndex(VIDEO)));
                        c.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));

                        c.setUpdated_at(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));
                        c.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));




                        Log.d(TAG, "OWNER WITH ID " + c.getId() + "AND NAME " + c.getName() + " RETRIEVED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return c;

    }





    public boolean addJob(Job job) {
        try {





            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, job.getId());
            contentValues.put(AUTHOR_NAME, job.getAuthor().getName());
            contentValues.put(AUTHOR_IMAGE_URL, job.getAuthor().getAvatar());
            contentValues.put(CATEGORY_ID, job.getCategory().getId());
            contentValues.put(COMPANY_ID, job.getCompany().getId());
            contentValues.put(TITLE, job.getTitle());
            contentValues.put(BODY, job.getBody());
            contentValues.put(IMAGE, job.getImage());
            contentValues.put(FEATURED, job.getFeatured());
            contentValues.put(EXPIRES, job.getTime().getExpires());
            contentValues.put(DEADLINE, job.getTime().getDeadline());
            contentValues.put(DATE_CREATED, job.getCreatedAt());
            contentValues.put(DATE_UPDATED, job.getUpdated_at());

            db.insert(JOBS_TABLE, null, contentValues);


            if(addCompany(job.getCompany()))
                if(addALocation(job.getId(), job.getLocation(), TYPE_JOB))

                    for(Comment c :job.getComments())
                        addAComment(job.getId(), c, TYPE_JOB);

            Log.d(TAG, "JOB WITH DESC " + job.getTitle() + "AND NO OF COMMENTS " + job.getComments().size() + " INSERTED");
            Log.i(TAG, "DEADLINE TIME IS " + job.getTime().getDeadline() );


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean deleteJob(int id) {

        return db.delete(JOBS_TABLE, ID + " = ?", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean deleteJobssTable() {

        try {


            db.execSQL("DELETE FROM " + JOBS_TABLE);

            Log.i("DATABASE", "JOB'S TABLE DELETED");


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Job getJob(int id) {

        Job job;
        Cursor cursor = null;
        try {
            job = new Job();


            String selectQuery = "SELECT  * FROM " + JOBS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        job.setId( cursor.getInt(cursor.getColumnIndex(ID)));
                        job.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getString(cursor.getColumnIndex(AVATAR))));
                        job.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID))));
                        job.setCompany(getCompany(cursor.getInt(cursor.getColumnIndex(COMPANY_ID))));
                        job.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        job.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        job.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                        job.setFeatured(cursor.getString(cursor.getColumnIndex(FEATURED)));
                        job.setLocation(getLocation(job.getId(), TYPE_JOB));
                        job.setTime(new Time(cursor.getString(cursor.getColumnIndex(EXPIRES)),cursor.getString(cursor.getColumnIndex(DEADLINE))));
                        job.setComments(getAllComments(job.getId(), TYPE_JOB));
                        job.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        job.setUpdated_at(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));



                        Log.d(TAG, "OWNER WITH ID " + job.getId() + "AND NAME " + job.getTitle() + " RETRIEVED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return job;

    }

    public List<Job> getAllJobs() {

        List<Job> jobs = new ArrayList<>();


        Cursor cursor = null;
        try {

            String selectQuery = "SELECT  * FROM " + JOBS_TABLE;
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {
                        Job job = new Job();


                        job.setId( cursor.getInt(cursor.getColumnIndex(ID)));
                        job.setAuthor(new Author(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)), cursor.getString(cursor.getColumnIndex(AUTHOR_IMAGE_URL))));
                        job.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID))));
                        job.setCompany(getCompany(cursor.getInt(cursor.getColumnIndex(COMPANY_ID))));
                        job.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                        job.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                        job.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                        job.setFeatured(cursor.getString(cursor.getColumnIndex(FEATURED)));
                        job.setLocation(getLocation(job.getId(), TYPE_JOB));
                        job.setTime(new Time(cursor.getString(cursor.getColumnIndex(EXPIRES)),cursor.getString(cursor.getColumnIndex(DEADLINE)), null));
                        job.setComments(getAllComments(job.getId(), TYPE_JOB));
                        job.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));
                        job.setUpdated_at(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));


                        jobs.add(job);

                        Log.d(TAG, "OWNER WITH ID " + job.getId() + "AND NAME " + job.getTitle() + " RETRIEVED");

                    } while (cursor.moveToNext());


            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if (cursor != null)
                cursor.close();
        }

        return jobs;

    }


    public Boolean hasJobBeenUpdated(int id, String dateUpdated){

        Boolean value = false;
        Cursor cursor = null;

        try{
            String selectQuery = "SELECT  * FROM " + JOBS_TABLE + " WHERE " +
                    ID + " ='" + id + "'";
            Log.i("QUERY", selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst())

                    do {

                        value = !dateUpdated.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DATE_UPDATED)));


                        Log.d(TAG, "JOB WITH ID " + id + " FOUND");

                    } while (cursor.moveToNext());


            }else value = null;

        }catch(Exception e){
            value = false;
            e.printStackTrace();

        }finally {

            if(cursor != null)
                cursor.close();
        }



        return value;
    }










    public long queryNumberOfEntries(String tableName) {
        return DatabaseUtils.queryNumEntries(db, tableName);
    }


}
