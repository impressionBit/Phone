package impressionbit.peoplebook;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import impressionbit.peoplebook.domain.Results;
import impressionbit.peoplebook.domain.Source;

public class PeopleBook extends AppCompatActivity {

    private String url = "https://randomuser.me/api/?results=101&format=JSON&inc=name,email,phone,picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_people_book);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        generateView(this);
    }


    private void generateView(Context cont) {


        Source source;

        Connector connector = new Connector();
        LinearLayout linealLayoutMain = findViewById(R.id.layaut);

        String json = connector.getJson(url);


        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();
        try {
            source = jacksonObjectMapper.mappedJson(json);

            for (Results result : source.getResults()) {
                final Button button = new Button(cont);
                button.setText(result.getName().getFirst() + " " + result.getName().getLast());
                button.setAlpha(0.7f);

                final String  firstText =result.getName().getFirst();
                final String  lastText = result.getName().getLast();
                final String  phoneText = result.getPhone();
                final String  emailText = result.getEmail();
                final String  pictureLink = result.getPicture().getLarge();

                final Context context = this;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        try {

                                            LayoutInflater layoutInflater = getLayoutInflater();
                                            View layoutPeople = layoutInflater.inflate(R.layout.people,
                                                    (ViewGroup) findViewById(R.id.viewPeople));

                                            ImageView imageView = (ImageView) layoutPeople.findViewById(R.id.imageView);

                                            TextView first = (TextView) layoutPeople.findViewById(R.id.first);
                                            TextView last = (TextView) layoutPeople.findViewById(R.id.last);
                                            TextView phone = (TextView) layoutPeople.findViewById(R.id.phone);
                                            TextView email = (TextView) layoutPeople.findViewById(R.id.email);

                                            URL newurl = new URL(pictureLink);
                                            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                                            imageView.setImageBitmap(mIcon_val);

                                            first.setText(firstText);
                                            last.setText(lastText);
                                            phone.setText(phoneText);
                                            email.setText(emailText);

                                            AlertDialog.Builder builderPeople = new AlertDialog.Builder(context);
                                            builderPeople.setView(layoutPeople);

                                            builderPeople.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });


                                            builderPeople.show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                            }
                        }).start();
                    }
                });

                linealLayoutMain.addView(button);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
