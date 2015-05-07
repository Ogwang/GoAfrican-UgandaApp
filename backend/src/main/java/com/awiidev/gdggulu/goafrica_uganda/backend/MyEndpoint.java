/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.awiidev.gdggulu.goafrica_uganda.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.goafrica_uganda.gdggulu.awiidev.com", ownerName = "backend.goafrica_uganda.gdggulu.awiidev.com", packagePath = ""))
public class MyEndpoint {

    @ApiMethod(name = "storeDish")
    public void storeDish(DishBean dishBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key dishBeanParentKey = KeyFactory.createKey("DishBeanParent", "DishAfrica");
            Entity dishEntity = new Entity("DishBean", dishBean.getId(), dishBeanParentKey);
            dishEntity.setProperty("title", dishBean.getTitle());
            dishEntity.setProperty("description", dishBean.getDescription());
            dishEntity.setProperty("ingredients", dishBean.getIngredients());
            dishEntity.setProperty("steps", dishBean.getSteps());
            datastoreService.put(dishEntity);
            txn.commit();
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getDishes")
    public List<DishBean> getDishes() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key dishBeanParentKey = KeyFactory.createKey("DishBeanParent", "DishAfrica");

        Query query = new Query(dishBeanParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<DishBean> dishBeans = new ArrayList<DishBean>();
        for (Entity result : results) {
            DishBean dishBean = new DishBean();
            dishBean.setId(result.getKey().getId());
            dishBean.setTitle((String) result.getProperty("title"));
            dishBean.setDescription((String) result.getProperty("description"));
            dishBean.setIngredients((String) result.getProperty("ingredients"));
            dishBean.setSteps((String) result.getProperty("steps"));

            dishBeans.add(dishBean);
        }
        return dishBeans;
    }
//
//    @ApiMethod(name = "clearDishes")
//    public void clearDishes() {
//
//    }

    @ApiMethod(name = "saveReview")
    public void saveReview(ReviewBean reviewBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "DishAfrica");
            Entity reviewEntity = new Entity("ReviewBean", reviewBean.getId(), reviewBeanParentKey);
            reviewEntity.setProperty("dishId", reviewBean.getDishId());
            reviewEntity.setProperty("username", reviewBean.getUsername());
            reviewEntity.setProperty("comment", reviewBean.getComment());
            reviewEntity.setProperty("rating", reviewBean.getRating());
            datastoreService.put(reviewEntity);
            txn.commit();
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getReviews")
    public List<ReviewBean> getReviews() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "DishAfrica");

        Query query = new Query(reviewBeanParentKey);

        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<ReviewBean> reviewBeans = new ArrayList<ReviewBean>();
        for (Entity result : results) {
            ReviewBean reviewBean = new ReviewBean();
            reviewBean.setId(result.getKey().getId());
            reviewBean.setDishId((Long) result.getProperty("dishId"));
            reviewBean.setUsername((String) result.getProperty("username"));
            reviewBean.setComment((String) result.getProperty("comment"));
            reviewBean.setRating((Long) result.getProperty("rating"));
            reviewBeans.add(reviewBean);
        }
        return reviewBeans;
    }

    @ApiMethod(name = "getReviewsForDish")
    public List<ReviewBean> getReviewsForDish(@com.google.api.server.spi.config.Named("dishId") Long dishId) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "DishAfrica");


        Query.Filter propertyFilter =
                new Query.FilterPredicate("dishId",
                        Query.FilterOperator.EQUAL,
                        dishId);
        Query query = new Query("ReviewBean").setFilter(propertyFilter);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<ReviewBean> reviewBeans = new ArrayList<ReviewBean>();
        for (Entity result : results) {
            ReviewBean reviewBean = new ReviewBean();
            reviewBean.setId(result.getKey().getId());
            reviewBean.setDishId((Long) result.getProperty("dishId"));
            reviewBean.setUsername((String) result.getProperty("username"));
            reviewBean.setComment((String) result.getProperty("comment"));
            reviewBean.setRating((Long) result.getProperty("rating"));
            reviewBeans.add(reviewBean);
        }
        return reviewBeans;
    }

    @ApiMethod(name = "searchDishes")
    public List<DishBean> searchDishes(@com.google.api.server.spi.config.Named("searchTerm") String searchTerm) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key dishBeanParentKey = KeyFactory.createKey("DishBeanParent", "DishAfrica");

        Query.Filter propertyFilter =
                new Query.FilterPredicate("title",
                        Query.FilterOperator.EQUAL,
                        searchTerm);

        Query query = new Query("DishBean").setFilter(propertyFilter);

        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<DishBean> dishBeans = new ArrayList<DishBean>();
        for (Entity result : results) {
            DishBean dishBean = new DishBean();
            dishBean.setId(result.getKey().getId());
            dishBean.setTitle((String) result.getProperty("title"));
            dishBean.setDescription((String) result.getProperty("description"));
            dishBean.setIngredients((String) result.getProperty("ingredients"));
            dishBean.setSteps((String) result.getProperty("steps"));
            dishBeans.add(dishBean);
        }
        return dishBeans;
    }

}
