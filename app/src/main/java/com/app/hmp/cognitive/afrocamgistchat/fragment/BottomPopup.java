package com.app.hmp.cognitive.afrocamgistchat.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.hmp.cognitive.afrocamgistchat.R;
import com.app.hmp.cognitive.afrocamgistchat.activity.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class BottomPopup extends BottomSheetDialogFragment {

    public Message message;

    private View itemView;
    private TextView txtDelete,txtReply,txtEdit;

    private DialogDismissListener mListener;

    public BottomPopup() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogDismissListener) {
            mListener = (DialogDismissListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static BottomPopup newInstance(Message message) {
        BottomPopup bottomSheetFragment = new BottomPopup();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", message);
        bottomSheetFragment.setArguments(bundle);
        return bottomSheetFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_popup, container, false);

    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /*BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
        return dialog;*/

        return new BottomSheetDialog(requireContext(), getTheme());  //set your created theme here
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.itemView = view;

        message = (Message) getArguments().getSerializable("message");

        Log.d("message99", new GsonBuilder().setPrettyPrinting().create().toJson(message));

        findViews();
        initClickListener();
    }

    private void findViews() {
        txtReply = itemView.findViewById(R.id.txtReply);
        txtEdit = itemView.findViewById(R.id.txtEdit);
        txtDelete = itemView.findViewById(R.id.txtDelete);
    }

    private void initClickListener() {

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message!=null && message.getMessageId()!=null && message.getMessageText()!=null && message.getToId()!=null){
                    mListener.onDialogDismiss("edit",message.getMessageId(),message.getMessageText(),message.getToId());
                }
                dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message!=null && message.getMessageId()!=null && message.getMessageText()!=null && message.getToId()!=null){
                    mListener.onDialogDismiss("delete",message.getMessageId(),message.getMessageText(),message.getToId());
                }
                dismiss();
            }
        });

        txtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message!=null && message.getMessageId()!=null && message.getMessageText()!=null && message.getToId()!=null && message.getFromId()!=null){
                    if(message.getFrom().equalsIgnoreCase("me")){
                        mListener.onDialogDismiss("reply",message.getMessageId(),message.getMessageText(),message.getToId());
                    }else {
                        mListener.onDialogDismiss("reply",message.getMessageId(),message.getMessageText(),message.getFromId());
                    }
                }
                dismiss();
            }
        });

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

    }

    public interface DialogDismissListener {
        void onDialogDismiss(String action,int messageID, String messageText, int toId);
    }


}
