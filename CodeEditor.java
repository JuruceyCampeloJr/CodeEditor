package com.jurucey.codeblue.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import com.jurucey.codeblue.R;

public class CodeEditor extends AppCompatMultiAutoCompleteTextView {
	
	private int maxNumberOfSuggestions = Integer.MAX_VALUE;
    private int autoCompleteItemHeightInDp = (int) (50 * Resources.getSystem().getDisplayMetrics().density);
	
	private Paint currentLineBackground;
	private Rect currentLineBounds;
	private boolean enableCurrentLineBackground;
	
	private Paint lineNumberBackground;
	private Paint lineDividerLineNumber;
	private final int LINE_WIDTH_DIVIDER_LINE_NUMBER = 3;
	
	private Paint lineNumberText;
	private float marginLeftLineNumberText;
	private float marginRightLineNumberText;
	
	private Paint currentLineNumberTextBackground;
	
	private Paint currentLineNumberText;
	
	private boolean enableLineNumber;
	
	public CodeEditor(Context context) {
		super(context);
		init();
	}
	
	public CodeEditor(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init();
	}
	
	public CodeEditor(Context context, AttributeSet attributeSet, int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
		init();
	}
	
	private void init() {
		initDefaultSettings();
		initNewFeatures();
	}
	
	private void initDefaultSettings() {
		setBackgroundColor(0xFF232323);
		setTextColor(0xFFFFFFFF);
		setGravity(Gravity.TOP);
		setHorizontallyScrolling(true);
		setScrollContainer(true);
		setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN);
		setPadding(0, 0, 0, 0);
	}
	
	private void initNewFeatures() {
		currentLineBackground = new Paint();
		currentLineBounds = new Rect();
		enableCurrentLineBackground = true;
		
		currentLineBackground.setColor(0xFF353535);
		
		lineNumberBackground = new Paint();
		lineDividerLineNumber = new Paint();
		
		lineNumberBackground.setColor(0xFF2C2C2C);
		lineDividerLineNumber.setColor(0xFF555555);
		
		lineNumberText = new Paint();
		
		lineNumberText.setColor(0xFF555555);
		lineNumberText.setTypeface(getTypeface());
		lineNumberText.setTextSize(getTextSize());
		lineNumberText.setTextAlign(Paint.Align.RIGHT);
		
		marginLeftLineNumberText = 15.0f;
		marginRightLineNumberText = 10.0f;
		
		currentLineNumberTextBackground = new Paint();
		
		currentLineNumberTextBackground.setColor(0xFF505040);
		
		currentLineNumberText = new Paint();
		
		currentLineNumberText.setColor(0xFFFFFFFF);
		currentLineNumberText.setTypeface(getTypeface());
		currentLineNumberText.setTextSize(getTextSize());
		currentLineNumberText.setTextAlign(Paint.Align.RIGHT);
		
		enableLineNumber = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(enableCurrentLineBackground) {
			drawCurrentLineBackground(canvas);
		}
		super.onDraw(canvas);
		
		if(enableLineNumber) {
			drawLineNumber(canvas);
		}
	}
	
	private void drawCurrentLineBackground(Canvas canvas) {
		getLineBounds(getCurrentLine(), currentLineBounds);
		canvas.drawRect(currentLineBounds, currentLineBackground);
	}
	
	public int getCurrentLineBackgroundColor() {
		return currentLineBackground.getColor();
	}
	
	public void setCurrentLineBackgroundColor(int color) {
		currentLineBackground.setColor(color);
	}
	
	public boolean isCurrentLineBackgroundEnabled() {
		return enableCurrentLineBackground;
	}
	
	public void setEnableCurrentLineBackground(boolean enable) {
		enableCurrentLineBackground = enable;
	}
	
	private void drawLineNumber(Canvas canvas) {
		float width = lineNumberText.measureText(String.valueOf(getLineCount()));
		float lineNumberBackgroundWidth = width + marginLeftLineNumberText + marginRightLineNumberText;
		float positionXLineNumberText = width + marginLeftLineNumberText;
		
		drawLineNumberBackground(canvas, lineNumberBackgroundWidth);
		drawLineNumberText(canvas, positionXLineNumberText);
		
		drawCurrentLineTextBackground(canvas, lineNumberBackgroundWidth);
		drawCurrentLineText(canvas, positionXLineNumberText);
		
		setPadding((int)lineNumberBackgroundWidth, 0, 0, 0);
	}
	
	public boolean isLineNumberEnabled() {
		return enableLineNumber;
	}
	
	public void setEnableLineNumber(boolean enable) {
		enableLineNumber = enable;
	}
	
	private void drawLineNumberBackground(Canvas canvas, float width) {
		int scrollX = getScrollX();
		int scrollY = getScrollY();
		int height = getHeight() + scrollY;
		
		width += scrollX;
		
		canvas.drawRect(scrollX, scrollY, width, height, lineNumberBackground);
		canvas.drawRect(width - LINE_WIDTH_DIVIDER_LINE_NUMBER, scrollY, width, height, lineDividerLineNumber);
	}
	
	public int getLineNumberBackgroundColor() {
		return lineNumberBackground.getColor();
	}
	
	public void setLineNumberBackgroundColor(int color) {
		lineNumberBackground.setColor(color);
	}
	
	public int getLineDividerLineNumberColor() {
		return lineDividerLineNumber.getColor();
	}
	
	public void setLineDividerLineNumberColor(int color) {
		lineDividerLineNumber.setColor(color);
	}
	
	private void drawLineNumberText(Canvas canvas, float width) {
		int scrollY = getScrollY();
		Layout layout = getLayout();
		int firstLine = layout.getLineForVertical(scrollY);
		int lastLine = layout.getLineForVertical(scrollY + (getHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom()));
		
		for(int line = firstLine; line <= lastLine; line++) {
			canvas.drawText(String.valueOf(line + 1), getScrollX() + width, layout.getLineBaseline(line), lineNumberText);
		}
	}
	
	public int getLineNumberTextColor() {
		return lineNumberText.getColor();
	}
	
	public void setLineNumberTextColor(int color) {
		lineNumberText.setColor(color);
	}
	
	public float getMarginLeftLineNumberText() {
		return marginLeftLineNumberText;
	}
	
	public void setMarginLeftLineNumberText(float marginLeft) {
		marginLeftLineNumberText = marginLeft;
	}
	
	public float getMarginRightLineNumberText() {
		return marginRightLineNumberText;
	}
	
	public void setMarginRightLineNumberText(float marginRight) {
		marginRightLineNumberText = marginRight;
	}
	
	private void drawCurrentLineTextBackground(Canvas canvas, float width) {
		Layout layout = getLayout();
		int scrollX = getScrollX();
		int line = getCurrentLine();
		
		width += scrollX;
		
		canvas.drawRect(scrollX, layout.getLineTop(line), width - LINE_WIDTH_DIVIDER_LINE_NUMBER, layout.getLineBottom(line), currentLineNumberTextBackground);
	}
	
	public int getCurrentLineTextBackgroundColor() {
		return currentLineNumberTextBackground.getColor();
	}
	
	public void setCurrentLineTextBackgroundColor(int color) {
		currentLineNumberTextBackground.setColor(color);
	}
	
	private void drawCurrentLineText(Canvas canvas, float width) {
		Layout layout = getLayout();
		int line = getCurrentLine();
		
		canvas.drawText(String.valueOf(line + 1), getScrollX() + width, layout.getLineBaseline(line), currentLineNumberText);
	}
	
	public int getCurrentLineTextColor() {
		return currentLineNumberText.getColor();
	}
	
	public void setCurrentLineTextColor(int color) {
		currentLineNumberText.setColor(color);
	}
	
	private int getCurrentLine() {
		Layout layout = getLayout();
		
		if(layout != null) {
			return layout.getLineForOffset(Selection.getSelectionStart(getText()));
		}
		return 0;
	}
	
	@Override
    public void showDropDown() {
        final Layout layout = getLayout();
        final int position = getSelectionStart();
        final int line = layout.getLineForOffset(position);
        final int lineButton = layout.getLineBottom(line);

        int numberOfMatchedItems = getAdapter().getCount();
        if (numberOfMatchedItems > maxNumberOfSuggestions) {
            numberOfMatchedItems = maxNumberOfSuggestions;
        }

        int dropDownHeight = getDropDownHeight();
        int modifiedDropDownHeight = numberOfMatchedItems * autoCompleteItemHeightInDp;
        if (dropDownHeight != modifiedDropDownHeight) {
            dropDownHeight = modifiedDropDownHeight;
        }

        final Rect displayFrame = new Rect();
        getGlobalVisibleRect(displayFrame);

        int displayFrameHeight = displayFrame.height();

        int verticalOffset = lineButton + dropDownHeight;
        if (verticalOffset > displayFrameHeight) {
            verticalOffset = displayFrameHeight - autoCompleteItemHeightInDp;
        }

        setDropDownHeight(dropDownHeight);
        setDropDownVerticalOffset(verticalOffset - displayFrameHeight - dropDownHeight);
        setDropDownHorizontalOffset((int) layout.getPrimaryHorizontal(position));

        super.showDropDown();
    }
	}
