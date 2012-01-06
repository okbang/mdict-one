/*
 *
 */
package openones.ilunarcal.andr;

import openones.ilunarcal.andr.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The Class Detail.
 */
public class Detail extends Activity implements OnClickListener {

    /** The cancel. */
    private Button cancel = null;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_form);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(onCancel);

        // Intent intent = new Intent(Detail.this, ILunarCalActivity.class);
        // Detail.this.startActivity(intent);
    }

    /** The on cancel. */
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();
        }
    };

    /**
     * [Explain the description for this method here].
     *
     * @param arg0 the arg0
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
    }
}
