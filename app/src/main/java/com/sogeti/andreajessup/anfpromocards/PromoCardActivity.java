package com.sogeti.andreajessup.anfpromocards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.bumptech.glide.Glide;

/**
 * Created by andreajessup on 10/6/15.
 */
public class PromoCardActivity extends AppCompatActivity {
    private Promotion promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocard);
        Bundle bundleObject = getIntent().getExtras();
        this.promotion = (Promotion)bundleObject.getSerializable("PROMOTION");
        populateCard();
    }

    private void populateCard() {
        // TableLayout tableLayout = (TableLayout)findViewById(R.id.promotiontable);
        ImageView imageView = (ImageView)findViewById(R.id.promo_image);
        TextView titleTextView = (TextView)findViewById(R.id.promo_title);
        TextView descriptionTextView = (TextView)findViewById(R.id.promo_description);
        TextView footerTextView = (TextView)findViewById(R.id.promo_footer);
        Button button = (Button)findViewById(R.id.promo_button);

        Glide.with(this).load(promotion.getImage()).into(imageView);
        titleTextView.setText(promotion.getTitle());
        descriptionTextView.setText(promotion.getDescription());
        if (promotion.getFooter() != null && promotion.getFooter().length() > 0) {
            footerTextView.setText(Html.fromHtml(promotion.getFooter()));
        }
        button.setText(promotion.getButton().getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("url", promotion.getButton().getTarget());
                startActivity(intent);
            }
        });
    }
}
