package nhatniem.rf.gd.icrypt.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import nhatniem.rf.gd.icrypt.AESEncryption;
import nhatniem.rf.gd.icrypt.BitmapEncoder;
import nhatniem.rf.gd.icrypt.GetFile;
import nhatniem.rf.gd.icrypt.MainActivity;
import nhatniem.rf.gd.icrypt.databinding.FragmentHomeBinding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class HomeFragment extends Fragment {
    int SELECT_PICTURE = 200;
    String pathImage;
    String pathImageAes;

    Bitmap bitmapOriginal;
    String imageStringOriginal;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    binding.editTextTextPersonName.setText(s.toString().replace(" ",""));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.button2.setOnClickListener(view -> {
            if (pathImage == null || pathImage.equals("")) {
                Toast.makeText(requireContext(), "Hãy chọn ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Tên file");

// Set up the input
            final EditText input = new EditText(requireContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("Lưu", (dialog, which) -> {
                String keyStr = binding.editTextTextPersonName.getText().toString();

                if (keyStr.length() < 16) {
                    Toast.makeText(requireContext(), "Vui lòng nhập key có độ dài 16 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // compress format
                bitmapOriginal.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                bitmapOriginal.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                // initialize byte array
                byte[] bytes = byteArrayOutputStream.toByteArray();
                //get based 64 encoded string and set encoded text
                imageStringOriginal = Base64.encodeToString(bytes, Base64.DEFAULT);

                byte[] bytesEncrypted = new AESEncryption().encrypt(imageStringOriginal, keyStr);


                try {
                    File path = new File("//sdcard//Download//mahoa//");
                    File folder = new File("//sdcard//Download//mahoa");

                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    File fileEncrypted = new File(path, input.getText() + ".jpg");

                    BitmapEncoder.encodeToBitmap(bytesEncrypted, fileEncrypted);
                    String filePath = fileEncrypted.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    binding.imageView2.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Mã hoá thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                System.out.println(pathImageAes);
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

            builder.show();
        });
        // final TextView textView = binding.textHome;
        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                pathImage = GetFile.getFile(requireContext(), selectedImageUri).getPath();


                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                        bitmapOriginal = BitmapFactory.decodeStream(inputStream);
                        binding.imageView.setImageBitmap(bitmapOriginal);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}