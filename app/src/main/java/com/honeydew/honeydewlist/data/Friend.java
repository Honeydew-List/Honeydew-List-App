package com.honeydew.honeydewlist.data;

public class Friend extends Owner{

        // Required for FireStore
        public Friend() {
                super();
        }

        public Friend(String username, String userID) {
                super(username, userID);
        }

}
