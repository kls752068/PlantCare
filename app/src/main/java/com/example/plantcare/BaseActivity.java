package com.example.plantcare;

import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

// Keeps controls clear of system bars and the keyboard on modern Android.
public abstract class BaseActivity extends AppCompatActivity {
    @Override public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View root = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        int left = root.getPaddingLeft();
        int top = root.getPaddingTop();
        int right = root.getPaddingRight();
        int bottom = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
            view.setPadding(left + insets.left, top + insets.top,
                    right + insets.right, bottom + insets.bottom);
            return windowInsets;
        });
        WindowCompat.getInsetsController(getWindow(), root).setAppearanceLightStatusBars(true);
        ViewCompat.requestApplyInsets(root);
    }
}
