package com.websarva.wings.android.dailyprotein;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // カスタムレイアウトの用意
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View customAlertView = inflater.inflate(R.layout.alert_delete_dialog, null);

        // タイトルの変更
        TextView title = customAlertView.findViewById(R.id.delete_alert_title);
        title.setText("警告");

        // メッセージの変更
        TextView message = customAlertView.findViewById(R.id.delete_alert_message);
        message.setText("本当にこのタンパク質摂取記録を削除してもよろしいですか？");

        // ダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customAlertView);

        builder.setPositiveButton(R.string.dialog_btn_delete, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    EditFoodActivity editFoodActivity = (EditFoodActivity) getActivity();
                    editFoodActivity.deleteFood();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
