package com.aalto.precious.sensing;

import android.content.Context;
import android.net.Uri;

/**
 * Created by fida on 7.11.2015.
 */

public class ReadTaskParameters {
    Uri uri;
    Context context;

    ReadTaskParameters(Uri uri, Context context) {
        this.uri = uri;
        this.context = context;

    }
}
