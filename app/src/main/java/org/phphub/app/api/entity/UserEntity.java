package org.phphub.app.api.entity;

import org.phphub.app.api.entity.element.User;

import java.util.List;

public class UserEntity {

    public class UserList {
        public List<User> data;

        public List<User> getData() {
            return data;
        }

        public void setData(List<User> data) {
            this.data = data;
        }
    }

    public class UserObj {
        public User data;

        public User getData() {
            return data;
        }

        public void setData(User data) {
            this.data = data;
        }
    }

}
