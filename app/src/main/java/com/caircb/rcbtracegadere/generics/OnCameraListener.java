package com.caircb.rcbtracegadere.generics;

import android.content.Intent;

public interface OnCameraListener {
    void onCameraResult(int requestCode, int resultCode, Intent data);
}
