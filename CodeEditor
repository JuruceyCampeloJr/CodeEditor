import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.MultiAutoCompleteTextView;

public class CodeEditor extends  MultiAutoCompleteTextView {
	
	private int maxNumberOfSuggestions = Integer.MAX_VALUE;
    private int autoCompleteItemHeightInDp = (int) (50 * Resources.getSystem().getDisplayMetrics().density);
	
	private Paint currentLineBackground;
	private Rect currentLineBounds;
	private boolean currentLineBackgroundEnabled;
	
	private Paint lineNumberBackground;
	private Paint lineDivider;
	
	private Paint lineNumberText;
	private float marginLeftLineNumberText;
	private float marginRightLineNumberText;
	
	private boolean lineNumberEnabled;
	
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
	public CodeEditor(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
		super(context, attributeSet, defStyleAttr, defStyleRes);
		init();
	}
	
	private void init() {
		setBackgroundColor(0xFF232323);
		setTextColor(0xFFFFFFFF);
		setGravity(Gravity.TOP);
		setHorizontallyScrolling(true);
		setScrollContainer(true);
		setTypeface(getResources().getFont(R.font.jet_brains_mono_regular));
		setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN);
		setPadding(0, 0, 0, 0);
		
		currentLineBackground = new Paint();
		currentLineBounds = new Rect();
		currentLineBackgroundEnabled = true;
		
		currentLineBackground.setColor(0xFF353535);
		
		lineNumberBackground = new Paint();
		lineDivider = new Paint();
		
		lineNumberBackground.setColor(0xFF2C2C2C);
		lineDivider.setColor(0xFF555555);
		
		lineNumberText = new Paint();
		
		lineNumberText.setColor(0xFF555555);
		lineNumberText.setTypeface(getTypeface());
		lineNumberText.setTextSize(getTextSize());
		lineNumberText.setTextAlign(Paint.Align.RIGHT);
		
		marginLeftLineNumberText = 15.0f;
		marginRightLineNumberText = 5.0f;
		
		lineNumberEnabled = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(currentLineBackgroundEnabled) {
			drawCurrentLineBackground(canvas);
		}
		super.onDraw(canvas);
		
		if(lineNumberEnabled) {
			drawLineNumber(canvas);
		}
	}
	
	private void drawCurrentLineBackground(Canvas canvas) {
		getLineBounds(getLayout().getLineForOffset(Selection.getSelectionStart(getText())), currentLineBounds);
		canvas.drawRect(currentLineBounds, currentLineBackground);
	}
	
	public int getCurrentLineBackgroundColor() {
		return currentLineBackground.getColor();
	}
	
	public void setCurrentLineBackgroundColor(int color) {
		currentLineBackground.setColor(color);
	}
	
	public boolean isCurrentLineBackgroundEnabled() {
		return currentLineBackgroundEnabled;
	}
	
	public void setisCurrentLineBackgroundEnabled(boolean enabled) {
		currentLineBackgroundEnabled = enabled;
	}
	
	private void drawLineNumber(Canvas canvas) {
		float width = lineNumberText.measureText(String.valueOf(getLineCount())) + marginLeftLineNumberText + marginRightLineNumberText;
		
		drawLineNumberBackground(canvas, width);
		drawLineNumberText(canvas, width);
	}
	
	public boolean isLineNumberEnabled() {
		return lineNumberEnabled;
	}
	
	public void setLineNumberEnabled(boolean enabled) {
		lineNumberEnabled = true;
	}
	
	private void drawLineNumberBackground(Canvas canvas, float width) {
		int scrollX = getScrollX();
		int scrollY = getScrollY();
		int height = getHeight();
		
		canvas.drawRect(scrollX, scrollY, scrollX + width, scrollY + height, lineNumberBackground);
		canvas.drawRect(scrollX + width - 3, scrollY, scrollX + width, scrollY + height, lineDivider);
	}
	
	public int getLineNumberBackgroundColor() {
		return lineNumberBackground.getColor();
	}
	
	public void setLineNumberBackgroundColor(int color) {
		lineNumberBackground.setColor(color);
	}
	
	public int getLineDividerColor() {
		return lineDivider.getColor();
	}
	
	public void setLineDividerColor(int color) {
		lineDivider.setColor(color);
	}
	
	private void drawLineNumberText(Canvas canvas, float width) {
		int scrollY = getScrollY();
		Layout layout = getLayout();
		int positionY = getBaseline();
		int firstLine = layout.getLineForVertical(scrollY);
		int lastLine = layout.getLineForVertical(scrollY + (getHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom()));
		
		for(int i = firstLine; i <= lastLine; i++) {
			positionY += layout.getLineBaseline(i) - positionY;
			canvas.drawText(String.valueOf(i + 1), getScrollX() + width - marginRightLineNumberText - 3, positionY, lineNumberText);
		}
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
