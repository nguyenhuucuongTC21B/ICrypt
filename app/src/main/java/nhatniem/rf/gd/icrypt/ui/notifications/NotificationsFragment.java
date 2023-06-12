package nhatniem.rf.gd.icrypt.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import nhatniem.rf.gd.icrypt.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.text2.setOnClickListener(v -> {
            startActivity(getTelegramInt(requireContext()));
        });
        binding.text3.setOnClickListener(v -> {
            String url = "http://nhatniem.rf.gd";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

      //  final TextView textView = binding.textNotifications;
       // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    Intent getTelegramInt(Context context) {
        Intent intent;
        try {
            try { // check for telegram app
                context.getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            } catch (PackageManager.NameNotFoundException e) {
                // check for telegram X app
                context.getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
            }
            // set app Uri
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=${Doremon559}"));
        } catch (PackageManager.NameNotFoundException e) {
            // set browser URI
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/Doremon559"));
        }
        return intent;
    }
}