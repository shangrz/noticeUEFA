package com.shang.noticeuefa.test;

import com.androidquery.AQuery;
import com.shang.noticeuefa.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
 
public class testAQUERYActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticeitem);
        renderContent(this);
        
    }
    
    
    public void renderContent( Activity act) {

        AQuery aq = new AQuery(act);

        aq.id(R.id.teamA_flag_imageView).image(R.drawable.esp).visible().clicked(this, "doit");
        aq.id(R.id.teamA_name_textView).text("西班牙");
        aq.id(R.id.match_datetime_textView).text("1234").visible();
        //aq.id(R.id.desc).text(content.getDesc()).visible();

    }
    public void doit(View v) {
        Toast.makeText(v.getContext(), "123", 1000).show();
    }

}
