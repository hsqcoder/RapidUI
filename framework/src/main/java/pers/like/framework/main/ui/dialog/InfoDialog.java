package pers.like.framework.main.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import pers.like.framework.main.R;
import pers.like.framework.main.util.ViewUtil;

/**
 * @author like
 */
@SuppressWarnings("unused")
public class InfoDialog extends Dialog {

    private TextView tvTitle, tvNegative, tvPositive, tvMessage;

    private InfoDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog_Info);
        setContentView(R.layout.dialog_info);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = new ViewUtil(context).getWidth(0.8f);
        }
        tvTitle = findViewById(R.id.title);
        tvMessage = findViewById(R.id.message);
        tvNegative = findViewById(R.id.negative);
        tvPositive = findViewById(R.id.positive);
    }

    private void title(String title) {
        tvTitle.setText(title);
    }

    public void message(String message) {
        tvMessage.setText(message);
    }

    private void negative(String negative, final OnClickListener negativeListener) {
        this.tvNegative.setText(negative);
        this.tvNegative.setOnClickListener(v -> {
            if (negativeListener != null) {
                negativeListener.onClick(InfoDialog.this, 0);
            }
            dismiss();
        });
        this.tvNegative.setVisibility(View.VISIBLE);
    }

    private void positive(final String positive, final OnClickListener positiveListener) {
        this.tvPositive.setText(positive);
        this.tvPositive.setOnClickListener(v -> {
            if (positiveListener != null) {
                positiveListener.onClick(InfoDialog.this, 1);
            }
            dismiss();
        });
        tvPositive.setVisibility(View.VISIBLE);
    }

    public static class Builder {
        private String title, message, negative, positive;
        private OnClickListener negativeListener, positiveListener;
        private InfoDialog dialog;

        public Builder(Context context) {
            dialog = new InfoDialog(context);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder negative(String negative) {
            this.negative = negative;
            return this;
        }

        public Builder negative(String negative, OnClickListener negativeListener) {
            this.negative = negative;
            this.negativeListener = negativeListener;
            return this;
        }

        public Builder positive(String positive, OnClickListener positiveListener) {
            this.positive = positive;
            this.positiveListener = positiveListener;
            return this;
        }

        public Builder positive(String positive) {
            this.positive = positive;
            return this;
        }

        public InfoDialog create() {
            dialog.title(title);
            dialog.message(message);
            if (!TextUtils.isEmpty(positive)) {
                dialog.positive(positive, positiveListener);
            }
            if (!TextUtils.isEmpty(negative)) {
                dialog.negative(negative, negativeListener);
            }
            return dialog;
        }
    }


}
