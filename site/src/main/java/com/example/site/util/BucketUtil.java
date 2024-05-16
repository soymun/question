package com.example.site.util;

public class BucketUtil {

    public enum Buckets {

        REPORTS("reports"), FILES("files");

        public final String value;

        Buckets(String value) {
            this.value = value;
        }
    }

}
