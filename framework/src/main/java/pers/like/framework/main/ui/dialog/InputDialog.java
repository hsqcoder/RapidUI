package pers.like.framework.main.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.InputType;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import pers.like.framework.main.Callback;
import pers.like.framework.main.R;
import pers.like.framework.main.util.Toasty;


/**
 * @author Like
 */
@SuppressWarnings("unused")
public class InputDialog extends BottomSheetDialog {

    private Input mInput;

    public InputDialog(@NonNull Context context, Input input, Callback<String> callback) {
        super(context, R.style.BottomSheetEdit);
        setContentView(R.layout.dialog_input);
        if (input == null) {
            mInput = new Input(InputType.TYPE_NULL, "标题", "", "");
        } else {
            mInput = input;
        }
        EditText text = findViewById(R.id.text_input_content);
        TextView description = findViewById(R.id.text_input_description);
        TextView submit = findViewById(R.id.text_input_submit);
        assert text != null;
        assert description != null;
        assert submit != null;
        description.setText(mInput.description);
        if (mInput.maxLines == 1) {
            text.setSingleLine();
        } else {
            text.setMaxLines(mInput.maxLines);
        }
        text.setInputType(mInput.inputType);
        text.setText(mInput.defaultValue);
        text.setHint(mInput.hint);
        text.setSelection(text.getText().toString().length());
        text.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit.performClick();
                return true;
            }
            return false;
        });
        submit.setOnClickListener(v -> {
            if (text.getText().length() == 0) {
                Toasty.warning(context, mInput.description + "不能为空").show();
            } else {
                callback.call(text.getText().toString());
                dismiss();
            }
        });
    }

    public static class Input {
        public int inputType;
        public String description;
        public String hint;
        public String defaultValue;
        public int maxLines = 1;

        public Input(int inputType, String description, String hint, String defaultValue) {
            this.inputType = inputType;
            this.description = description;
            this.hint = hint;
            this.defaultValue = defaultValue;
        }

        public Input(int inputType, String description, String hint, String defaultValue, int maxLines) {
            this.inputType = inputType;
            this.description = description;
            this.hint = hint;
            this.defaultValue = defaultValue;
            this.maxLines = maxLines;
        }

    }

}
