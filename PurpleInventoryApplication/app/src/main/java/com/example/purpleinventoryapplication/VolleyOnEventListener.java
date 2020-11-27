package com.example.purpleinventoryapplication;

import java.util.List;
import java.util.Map;

/**
 * @author Randeep Ranu - rfranu7@gmail.com
 * VolleyOnEventListener allows data that is taken
 * from an external API to be sent to another activity
 *
 * @param <T>
 */

public interface VolleyOnEventListener<T> {
    /**
     * This method is used for handling successful responses
     * and is utilized by ViewInventory.getAllData();
     * @param object
     */
    public void onSuccess(List<Map<String, Object>> object);
}