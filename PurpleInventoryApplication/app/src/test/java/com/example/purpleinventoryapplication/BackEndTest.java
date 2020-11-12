package com.example.purpleinventoryapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BackEndTest {
String db ;
String status;
Integer number;

        @Test
        public void checkDatabase() {
            assertNotNull("Database not found", db);

        }

    @Test
    public void checkEmailSent() {
        assertEquals("Not Sent", "Success", status);
    }

    @Test
    public void validateNumber() {
        assertTrue("Variable is not an Integer", number instanceof Integer);
    }


    }

