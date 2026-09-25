package vn.edu.vhu.ltdd.a1_231a290093;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "A1_231A290093";

    private TextView tvLog;
    private final StringBuilder history = new StringBuilder();
    private int step = 0;

    //NC1: Map lưu số lần gọi mỗi callback vòng đời
    private final Map<String, Integer> callbackCounts = new LinkedHashMap<>();

    private void logEvent(String event) {
        step++;
        String time = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                .format(new Date());

        // Lấy tên hàm callback chính (ví dụ: onCreate, onStart)
        String callbackName = event.split(" ")[0];
        callbackCounts.put(callbackName, callbackCounts.getOrDefault(callbackName, 0) + 1);

        // Tạo chuỗi hiển thị bảng đếm ở đầu (NC1)
        StringBuilder countSummary = new StringBuilder("--- BẢNG ĐẾM CALLBACK (NC1) ---\n");
        for (Map.Entry<String, Integer> entry : callbackCounts.entrySet()) {
            countSummary.append(entry.getKey()).append(": ").append(entry.getValue()).append("  |  ");
        }
        countSummary.append("\n---------------------------------------\n\n");

        String line = step + ". [" + time + "] " + event;
        Log.d(TAG, line);
        history.append(line).append('\n');

        if (tvLog != null) {
            tvLog.setText(countSummary.toString() + history.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvLog = findViewById(R.id.tvLog);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnCrash = findViewById(R.id.btnCrash);
        Button btnFinish = findViewById(R.id.btnFinish);

        if (btnClear != null) {
            btnClear.setOnClickListener(v -> {
                history.setLength(0);
                step = 0;
                callbackCounts.clear();
                tvLog.setText("");
                Log.i(TAG, "---- Đã xóa lịch sử ----");
            });
        }

        // NC2: Xử lý bẫy lỗi try-catch cho btnCrash
        if (btnCrash != null) {
            btnCrash.setOnClickListener(v -> {
                try {
                    String ten = null;
                    Log.d(TAG, "Độ dài tên: " + ten.length());
                } catch (NullPointerException e) {
                    Log.e(TAG, "Bắt được lỗi NullPointerException", e);
                    Toast.makeText(this, "Đã bắt lỗi: tên đang null", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> finish());
        }

        String state = (savedInstanceState == null) ? "= null" : "!= null";
        logEvent("onCreate (savedInstanceState " + state + ")");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logEvent("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logEvent("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logEvent("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logEvent("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logEvent("onRestart");
    }

    @Override
    protected void onDestroy() {
        logEvent("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logEvent("onSaveInstanceState");
    }
}