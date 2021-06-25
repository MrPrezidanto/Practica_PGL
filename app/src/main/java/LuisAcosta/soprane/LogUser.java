package LuisAcosta.soprane;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;

public class LogUser extends Activity {

    TabHost tabHost;
    LocalActivityManager lam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_user);

        initTab(savedInstanceState);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                colorTab();
            }
        });
    }

    @Override
    protected void onResume() {
        lam.dispatchResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        lam.dispatchPause(isFinishing());
        super.onPause();
    }

    private void initTab(Bundle savedInstanceState){
        Resources res = getResources();
        tabHost = findViewById(android.R.id.tabhost);
        lam = new LocalActivityManager(this, false);
        lam.dispatchCreate(savedInstanceState);
        tabHost.setup(lam);

        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(this, Login.class);
        spec = tabHost.newTabSpec("Login").setIndicator("Login").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RegisterUser.class).putExtra("admin", this.getIntent().getStringExtra("admin"));
        spec = tabHost.newTabSpec("Register").setIndicator("Register").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(getIntent().getExtras().getInt("tab"));
        colorTab();
    }

    private void colorTab(){
        TabWidget widget = tabHost.getTabWidget();

        for(int i = 0; i < widget.getChildCount(); i++) {
            if(widget.getChildAt(i).isSelected()) {
                if(i == 0)  widget.getChildAt(i).setBackground(getDrawable(R.drawable.border_tab_right));
                else widget.getChildAt(i).setBackground(getDrawable(R.drawable.border_tab_left));
            }
            else widget.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.blanco));
        }
    }
}