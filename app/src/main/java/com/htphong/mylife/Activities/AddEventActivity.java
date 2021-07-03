package com.htphong.mylife.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.EventService;
import com.htphong.mylife.POJO.StatusPOJO;
import com.htphong.mylife.R;
import com.htphong.mylife.Utils.Helper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTitle, edtAddress, edtContent;
    private TextView txtDateStart, txtDateEnd;
    private ImageButton addImageBtn;
    private ImageView eventImage;
    private Button submitBtn;

    private static final int GALLERY_ADD_PHOTO = 10;
    private String dateString;
    private int statusDate = 1;
    private Bitmap bitmap = null;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        edtTitle = findViewById(R.id.edt_event_title);
        edtAddress = findViewById(R.id.edt_event_place);
        edtContent = findViewById(R.id.edt_event_content);
        txtDateStart = findViewById(R.id.txt_time_start);
        txtDateEnd = findViewById(R.id.txt_time_end);
        addImageBtn = findViewById(R.id.event_image_btn);
        eventImage = findViewById(R.id.event_image_view);
        submitBtn = findViewById(R.id.submit_btn);

        txtDateStart.setOnClickListener(this);
        txtDateEnd.setOnClickListener(this);
        addImageBtn.setOnClickListener(this);
        eventImage.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        setInitialValue();
    }

    private void setInitialValue() {
        edtTitle.setText("");
        edtAddress.setText("");
        edtContent.setText("");
        txtDateStart.setText(getTimeNow());
        txtDateEnd.setText(getTimeNow());
        eventImage.setImageResource(R.drawable.ic_task_details_link);
    }

    private String getTimeNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    public void showDateTimePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
        final View view = LayoutInflater.from(AddEventActivity.this).inflate(R.layout.layout_datetime_picker, null);
        DatePicker datePicker = view.findViewById(R.id.layout_dt_date_picker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.layout_dt_time_picker);
        builder.setView(view);
        builder.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dateString = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                String time = dateString + " "
                                + (timePicker.getCurrentHour() < 10 ? "0" + timePicker.getCurrentHour() : timePicker.getCurrentHour())
                                + ":"
                                + (timePicker.getCurrentMinute() < 10 ? ("0" + timePicker.getCurrentMinute()) : timePicker.getCurrentMinute());
                if(statusDate == 1) {
                    txtDateStart.setText(time);
                } else {
                    txtDateEnd.setText(time);
                }
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_ADD_PHOTO && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                eventImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_time_start: {
                statusDate = 1;
                showDateTimePicker();
                break;
            }

            case R.id.txt_time_end: {
                statusDate = 2;
                showDateTimePicker();
                break;
            }

            case R.id.event_image_view:
            case R.id.event_image_btn: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_ADD_PHOTO);
                break;
            }

            case R.id.submit_btn: {
                String title = edtTitle.getText().toString();
                String address = edtAddress.getText().toString();
                String content = edtContent.getText().toString();
                String dateStart = Helper.convertToDatetime(txtDateStart.getText().toString());
                String dateEnd = Helper.convertToDatetime(txtDateEnd.getText().toString());

                if(title.isEmpty() || address.isEmpty() || content.isEmpty()) {
                    showDiaglog();
                } else {
                    createEvent(title, address, content, dateStart, dateEnd);
                }
                break;
            }
        }
    }

    private void createEvent(String title, String address, String content, String dateStart, String dateEnd) {
        dialog.setMessage("Đang thêm sự kiện");
        dialog.show();
        Retrofit retrofit = new Client().getRetrofit(this);
        retrofit.create(EventService.class).createEvent(title, dateStart, dateEnd, content, address, Helper.bitmapToString(bitmap), "1")
                .enqueue(new Callback<StatusPOJO>() {
                    @Override
                    public void onResponse(Call<StatusPOJO> call, Response<StatusPOJO> response) {
                        setInitialValue();
                        dialog.dismiss();
                        Toast.makeText(AddEventActivity.this, "Thêm sự kiện thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<StatusPOJO> call, Throwable t) {
                        setInitialValue();
                        dialog.dismiss();
                    }
                });
    }

    private void showDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
        builder.setTitle("Tiêu đề/ Nơi diễn ra/ Nội dung không được để trống");
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}