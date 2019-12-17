package com.tanyayuferova.mnist;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Author: Tanya Yuferova
 * Date: 12/17/2019
 */
public class PromptsView extends LinearLayout {

    private List<TextView> promptsViews = new ArrayList<>();

    public PromptsView(Context context) {
        this(context, null);
    }

    public PromptsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptsView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PromptsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.stroked_background);
        int number = 0;

        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.PromptsView,
            defStyleAttr,
            defStyleRes
        );
        try {
            number = a.getInteger(R.styleable.PromptsView_prompts_number, 0);
        } finally {
            a.recycle();
        }

        if (number > 0) {
            addPromptView();
        }
        for (int i = 1; i < number; i++) {
            addDivider();
            addPromptView();
        }
    }

    private void addPromptView() {
        ContextThemeWrapper themedContext = new ContextThemeWrapper(getContext(), R.style.PromptTextView);
        LayoutParams params = new LayoutParams(0, WRAP_CONTENT, 1);
        TextView promptsView = new TextView(themedContext);
        this.addView(promptsView, params);
        promptsViews.add(promptsView);
    }

    private void addDivider() {
        LayoutParams params = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.border_width), MATCH_PARENT);
        View divider = new View(getContext());
        divider.setBackgroundResource(R.color.border);
        this.addView(divider, params);
    }

    void updatePrompts(String[] prompts) {
        for (int i = 0; i < promptsViews.size(); i++) {
            TextView view = promptsViews.get(i);
            if (prompts.length > i + 1) {
                view.setText(prompts[i + 1]);
            } else {
                view.setText("");
            }
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        for (TextView view : promptsViews) {
            view.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof TextView && promptsViews.contains(child)) {
            promptsViews.remove(child);
        }
    }
}
