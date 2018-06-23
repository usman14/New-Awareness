package com.awareness.usman.newawareness.Objects;

import io.realm.RealmObject;

/**
 * Created by usman on 4/12/2017.
 */

public class Realm_Intro extends RealmObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
