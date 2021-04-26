package com.honeydew.honeydewlist.data;

public class Friend extends Owner{

        private String email;

        // Required for FireStore
        public Friend() {
                super();
        }

        public Friend(String username, String userID, String email) {
                super(username, userID);
                this.email = email;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }
}
