package cn.ucai.ttmusic.model.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import cn.ucai.ttmusic.R;

public class DialogBuilder {

    private Context context;
    private String title, message, comfirm_text, cancel_text;
    private DialogInterface.OnClickListener comfirm_click, cancel_click;

    public DialogBuilder(Context context) {
        this.context = context;
    }

    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public DialogBuilder setPositiveButton(String comfirm, DialogInterface.OnClickListener clickListener) {
        this.comfirm_text = comfirm;
        this.comfirm_click = clickListener;
        return this;
    }

    public DialogBuilder setNegativeButton(String cancel, DialogInterface.OnClickListener clickListener) {
        this.cancel_text = cancel;
        this.cancel_click = clickListener;
        return this;
    }

    public Dialog create() {
        final Dialog dialog = new Dialog(context, R.style.dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_custom_dialog, null);
        //设置布局参数
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置宽度
        WindowManager.LayoutParams windowParams = dialog.getWindow().getAttributes();
        params.width = (int) (PhoneUtil.getScreenWidth(context) * 0.8);
        dialog.getWindow().setAttributes(windowParams);
        //设置布局
        DialogHolder holder = new DialogHolder(layout);
        //设置标题
        if (title != null) {
            holder.dialogTitle.setText(title);
            holder.dialogTitle.getPaint().setFakeBoldText(true); //加粗
        } else {
            holder.dialogTitle.setVisibility(View.GONE);
        }
        //设置消息、内容
        if (message != null) {
            holder.dialogMessage.setText(message);
        } else {
            holder.dialogMessage.setVisibility(View.GONE);
        }
        //确定事件
        if (comfirm_text != null) {
            holder.confirmBtn.setText(comfirm_text);
        }
        if (comfirm_click == null) {
            comfirm_click = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            };
        }
        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comfirm_click.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                dialog.dismiss();
            }
        });
        //取消事件
        if (cancel_text != null) {
            holder.cancelBtn.setText(cancel_text);
        }
        if (cancel_click == null) {
            cancel_click = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            };
        }
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_click.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                dialog.dismiss();
            }
        });

        dialog.setContentView(layout, params);
        return dialog;
    }

    public class DialogHolder {

        TextView dialogTitle, dialogMessage;
        Button cancelBtn, confirmBtn;

        public DialogHolder(View view) {
            dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
            dialogMessage = (TextView) view.findViewById(R.id.dialog_message);
            cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
            confirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        }
    }
}
