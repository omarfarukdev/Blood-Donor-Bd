package com.example.blooddonorbd.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.blooddonorbd.R;

public class FAQSActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView,textViewh,textViews,textViewm,input2,input3,inputh3,input4,input5,input6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("FAQS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView=findViewById(R.id.input);
        textViewh=findViewById(R.id.inputh);
        textViews=findViewById(R.id.inputs);
        textViewm=findViewById(R.id.input1);
        input2=findViewById(R.id.input2);
        inputh3=findViewById(R.id.inputh3);
        input3=findViewById(R.id.input3);
        input4=findViewById(R.id.input4);
        input5=findViewById(R.id.input5);
        input6=findViewById(R.id.input6);

        textViewh.setText(Html.fromHtml("<b>• carbon dioxide</b><br>"+"<b>• glucose</b><br>"+"<b>• hormones</b><br>"+"<b>• proteins</b><br>"+"<b>• mineral salts</b><br>"+"<b>• fats</b><br>"+"<b>• vitamins</b><br>"));
        textViews.setText(Html.fromHtml("<br><b>Blood consists of plasma, red and white blood cells, and platelets.</b>"));
        textView.setText(Html.fromHtml("<b><i>Plasma:</i></b>"+" This constitutes approximately 55 percent of blood fluid in humans. Plasma is 92 percent water,\n"+
                "and the contents of the remaining 8 percent include:"));
        textViewm.setText(Html.fromHtml("The remaining 45 percent of the blood mainly consists of red and white blood cells and platelets. Each of these has a vital role to play in keeping the blood functioning effectively.</p>"+
                "<p><b><i>Red blood cells (RBCs), or erythrocytes:</i></b> They are shaped like slightly indented, flattened disks and\n" +
                "transport oxygen to and from the lungs. Hemoglobin is a protein that contains iron and retains the\n" +
                "oxygen until its destination. The life span of an RBC is 4 months, and the body replaces them regularly.\n" +
                "Amazingly, our body produces around 2 million blood cells every second.</p>"+"<p>The expected number of RBCs in a single drop, or microliter, of blood is 4.5 to 6.2 million in men and 4.0\n" +
                "to 5.2 million in women.</p>"+"<p><b><i>White blood cells, or leukocytes:</i></b>White blood cells make up less than 1 percent of blood content, and\n" +
                "they form vital defenses against disease and infection. The normal range of the number of white blood\n" +
                "cells in a microliter of blood is between 3,700 and 10,500. Higher and lower levels of white blood cells\n" +
                "can indicate disease.</p>"+"<p><b><i>Platelets, or thrombocytes:</i></b>These interact with clotting proteins to prevent or stop bleeding. There\n" +
                "should be between 150,000 and 400,000 platelets per microliter of blood.</p>"+"<p>RBCs, white blood cells, and platelets are produced in the bone marrow before entering the\n" +
                "bloodstream. Plasma is mostly water that is absorbed from ingested food and drink by the intestines.\n" +
                "Combined, these are propelled around the entire body by the heart and carried by the blood vessels."));
        input2.setText(Html.fromHtml("<br><b>Blood has a number of functions that are central to survival, including:</b><br>"));
        inputh3.setText(Html.fromHtml("<p>• supplying oxygen to cells and tissues</p>"+"<p>• providing essential nutrients to cells, such as amino acids, fatty acids, and glucose</p>"+"<p>• removing waste materials, such as carbon dioxide, urea, and lactic acid</p>"+
                "<p>• protecting the body from infection and foreign bodies through the white blood cells</p>"+"<p>• transporting hormones from one part of the body to another, transmitting messages, and completing important processes</p>"+
                "<p>• regulating acidity (pH) levels and body temperature</p>"+"<p>• engorging parts of the body when needed, for example, a penile erection as a response to sexual arousal</p>"));
        input3.setText(Html.fromHtml("<p>Another important function of the blood is its protective action against disease. White blood cells\n" +
                "defend the body against infections, foreign materials, and abnormal cells.</p><p>The platelets in blood enable the clotting, or coagulation, of blood. When bleeding occurs, the platelets\n" +
                "group together to create a clot. The clot becomes a scab and stops the bleeding, as well as helping to protect the wound from infection.</p><br>"));
        input4.setText(Html.fromHtml("<p>Blood groups categorize blood <b>based on the presence and absence of certain antibodies.</b>The groupings also take into account antigens on the surface of the blood cells.</p>"+
                "<p><b>Antibodies are proteins in plasma </b>that alert the immune system to the presence of potentially harmful\n" +
                "foreign substances. The immune system will attack the threat of disease or infection. Antigens are\n" +
                "protein molecules on the surface of red blood cells.</p>"+"<p>When giving or receiving organ donations or blood transfusions, the blood group of an individual\n" +
                "becomes extremely important. Antibodies will attack new blood cells if they have an unrecognizable\n" +
                "antigen, and this can lead to life-threatening complications. For example, anti-A antibodies will attack\n" +
                "cells that have A antigens.</p>"+"<p>RBCs sometimes contain another antigen called RhD. This is also noted as part of the blood group. A\n" +
                "positive blood group means that RhD is present.</p>"+"<p>Humans can have one of four main blood groups. Each of these groups can be Rhd positive or negative,\n" +
                "forming eight main categories.</p>"));
        input5.setText(Html.fromHtml("<p><b>• Group A positive or A negative:</b>A antigens are found on the surfaces of blood cells. Anti-B antibodies are found in the plasma.</p>"+"<p><b>• Group B positive or B negative: </b>B antigens are found on the surfaces of blood cells. Anti-A antibodies are found in the plasma.</p>"+
                "<p><b>• Group AB positive or AB negative:</b>A and B antigens are found on the surfaces of blood cells.There are no antibodies are found in the plasma.</p>"+
                "<p><b>• Group O positive and O negative:</b>There are no antigens are found on the surfaces of blood cells. Both anti-B and anti-A antibodies are found in the plasma.</p><br>"));
        input6.setText(Html.fromHtml("<p>Group O blood can be given to people of virtually any blood type, and people with Group AB+ blood can generally receive blood from any group.<b>Talk to your doctor to find out your blood type. If you donate blood, a doctor can also tell you your blood type.</b></p>"+
                "<p>Blood groups are important during pregnancy. If a woman has RhD negative blood, for example, but her\n" +
                "fetus inherits RhD positive blood from the father, treatment will be needed to prevent a condition\n" +
                "known as hemolytic disease of the newborn (HDN).</p>"));
    }
}
