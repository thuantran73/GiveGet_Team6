package com.example.giveandgetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giveandgetapp.database.Database;
import com.example.giveandgetapp.database.Image;

import java.sql.Connection;
import java.sql.ResultSet;

public class ActorInforActivity extends AppCompatActivity {

    private int actorId;
    private boolean _isFromRating;

    private ImageView avataruser;
    private TextView sobaidang;
    private TextView sodanhgia;
    private TextView sobaocao;
    private TextView lbltenuser;
    private TextView lbllopuser;
    private TextView lblmssvuser;
    private TextView lblsdtuser;
    public Button _btnKetthucActorInfor;
    public TextView _labelInfo;
    public Button btnPhone;

    private static final int REQUEST_CALL = 1;


    private Database _database;

    //Round rating count
    public static double roundHalf(double number) {
        double diff = number - (int) number;
        if (diff < 0.25) {
            return (int) number;
        } else if (diff < 0.75) {
            return (int) number + 0.5;
        } else {
            return (int) number + 1;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_infor);
        Button btnCall;
        this.actorId = getIntent().getIntExtra("Actor_Id", 0);
        this._isFromRating = getIntent().getBooleanExtra("IsFromRating", false);


        this.avataruser = findViewById(R.id.avataruser);
        this.sobaidang = findViewById(R.id.sobaidang);
        this.sodanhgia = findViewById(R.id.sodanhgia);
        this.sobaocao = findViewById(R.id.sobaocao);
        _btnKetthucActorInfor = findViewById(R.id.btnKethucActorInfor);
        _labelInfo = findViewById(R.id.thongtinActorInfoActivity);
        if (_isFromRating) {
            _btnKetthucActorInfor.setVisibility(View.VISIBLE);
            _labelInfo.setVisibility(View.VISIBLE);
        }

        this.lbltenuser = findViewById(R.id.lbltenuser);
        this.lbllopuser = findViewById(R.id.lbllopuser);
        this.lblmssvuser = findViewById(R.id.lblmssvuser);
        this.lblsdtuser = findViewById(R.id.lblsdtuser);

        _database = new Database(this);
        Connection con = _database.connectToDatabase();

        String query = "SELECT [Avatar]" +
                "      ,[StudentId]" +
                "      ,[Email]" +
                "      ,[Name]" +
                "      ,[Class]" +
                "      ,[Phone]" +
                "      ,[ReportCount]" +
                "      ,[RatingCount]" +
                "      ,[NumberPostHadRated]" +
                "  FROM [User]" +
                "  WHERE Id = " + actorId;

        ResultSet resultSet = _database.excuteCommand(con, query);

        try {
            if (resultSet.next()) {
                avataruser.setImageBitmap(_database.getImageInDatabaseInSquire(con, resultSet.getInt("Avatar")));
                sobaidang.setText(resultSet.getInt("NumberPostHadRated") + "");
                int rpc = resultSet.getInt("ReportCount");
                float rtc = resultSet.getFloat("RatingCount");
                double parsertcToDouble = rtc;
                double rating = roundHalf(parsertcToDouble);
                sodanhgia.setText(rating + "");
                sobaocao.setText(resultSet.getInt("ReportCount") + "");

                lbltenuser.setText("Tên: " + resultSet.getString("Name"));
                lbllopuser.setText("Lớp: " + resultSet.getString("Class"));
                lblmssvuser.setText("MSSV: " + resultSet.getString("StudentId"));
                lblsdtuser.setText(resultSet.getString("Phone"));
            }

            con.close();

        } catch (Exception e) {

        }

        btnPhone = findViewById(R.id.btnCall);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = lblsdtuser.getText().toString();
//                String s = "tel:" + phone;
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse(s));
                if (ContextCompat.checkSelfPermission(ActorInforActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActorInforActivity.this,
                            new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }else {
                    String s = "tel:" + phone;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(s)));
                }
//                startActivity(intent);
            }
        });

//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:" + "Phone"));
//        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        startActivity(intent);
//
////        public void makeCall() {
////            String number = lblsdtuser.getText().toString();
////
////                if (ContextCompat.checkSelfPermission(ActorInforActivity.this,
////                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                    ActivityCompat.requestPermissions(ActorInforActivity.this,
////                            new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
////                }else {
////                    String dial = "tel:" + "Phone";
////                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
////                }
////
////        };

        _btnKetthucActorInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                finish();
            }
        });
    }
}
