package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

public class MultiSlider extends Widget implements Disableable {
	
	//PROGRESS BAR PORT
	private MultiSliderStyle style;
	private float min, max, stepSize;
	private float[] animateFromValues;
	float[] knobsPositions;
	final boolean vertical;
	private float animateDuration, animateTime;
	private Interpolation animateInterpolation = Interpolation.linear;
	boolean disabled;
	private Interpolation visualInterpolation = Interpolation.linear;
	private boolean round = true;
	
	//SLIDER PORT
	int selectedIndex = -1;
	int draggingPointer = -1;
	boolean mouseOver;
	private Interpolation visualInterpolationInverse = Interpolation.linear;

	public MultiSlider (int knobs, float min, float max, float stepSize, boolean vertical, Skin skin) {
		this(knobs, min, max, stepSize, vertical, skin.get("default-" + (vertical ? "vertical" : "horizontal"), MultiSliderStyle.class));
	}

	public MultiSlider (int knobs, float min, float max, float stepSize, boolean vertical, Skin skin, String styleName) {
		this(knobs, min, max, stepSize, vertical, skin.get(styleName, MultiSliderStyle.class));
	}

	/** Creates a new progress bar. If horizontal, its width is determined by the prefWidth parameter, and its height is determined
	 * by the maximum of the height of either the progress bar {@link NinePatch} or progress bar handle {@link TextureRegion}. The
	 * min and max values determine the range the values of this progress bar can take on, the stepSize parameter specifies the
	 * distance between individual values.
	 * <p>
	 * E.g. min could be 4, max could be 10 and stepSize could be 0.2, giving you a total of 30 values, 4.0 4.2, 4.4 and so on.
	 * @param min the minimum value
	 * @param max the maximum value
	 * @param stepSize the step size between values
	 * @param style the {@link ProgressBarStyle} */
	public MultiSlider (int knobs, float min, float max, float stepSize, boolean vertical, MultiSliderStyle style) {
		if (min > max) throw new IllegalArgumentException("max must be > min. min,max: " + min + ", " + max);
		if (stepSize <= 0) throw new IllegalArgumentException("stepSize must be > 0: " + stepSize);
		if (knobs < 1) throw new IllegalArgumentException("there must be at least one knob! knobs: "+knobs);
		setStyle(style);
		//Create the knobs and evenly distributes them
		this.knobsPositions = new float[knobs];
		this.animateFromValues = new float[knobs];
		//TODO : rivedere la posizione iniziale dei knobs + sistemare per vertical slider
		
		for (int i = 0; i < knobs; i ++) {
			knobsPositions[i] = (max-min)/(knobs + 1) * (i + 1);
			animateFromValues[i] = knobsPositions[i];
		}
		
		this.min = min;
		this.max = max;
		this.stepSize = stepSize;
		this.vertical = vertical;
		setSize(getPrefWidth(), getPrefHeight());
		
		addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (disabled) return false;
				if (draggingPointer != -1) return false;
				draggingPointer = pointer;
				selectedIndex = getNearestKnob(x,y);
				calculatePositionAndValue(selectedIndex, x, y);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (pointer != draggingPointer) return;
				draggingPointer = -1;
				// The position is invalid when focus is cancelled
				if (event.isTouchFocusCancel() || !calculatePositionAndValue(selectedIndex, x, y)) {
					// Fire an event on touchUp even if the value didn't change, so listeners can see when a drag ends via isDragging.
					ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
					fire(changeEvent);
					Pools.free(changeEvent);
				}
			}

			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				calculatePositionAndValue(selectedIndex, x, y);
			}

			@Override
			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) mouseOver = true;
			}

			@Override
			public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (pointer == -1) mouseOver = false;
			}
		});
	}
	
	int getNearestKnob(float x, float y) {
		float min_dst = Float.MAX_VALUE;
		int nearest = - 1;
		for (int index = 0; index < getNumberOfKnobs(); index++) {
			if (vertical) {
				float dst = Math.abs(getPercent(index) * getHeight() - y);
				if (dst < min_dst) {
					min_dst = dst;
					nearest = index;
				}
			}
			else {
				float dst = Math.abs(getPercent(index) * getWidth() - x);
				if (dst < min_dst) {
					min_dst = dst;
					nearest = index;
				}
			}
		}
		
		return nearest;
	}
	
	boolean calculatePositionAndValue (int index, float x, float y) {
		final float knobMin = index > 0 ? getValue(index - 1) + this.stepSize: this.min;
		final float knobMax = index < (getNumberOfKnobs() - 1) ? getValue(index + 1) - this.stepSize: this.max;
		if (vertical) {
			if (knobMin / this.max * getHeight() >= y || knobMax / this.max * getHeight() <= y) {
				return false;
			}
		}
		else {
			if (knobMin / this.max * getWidth() >= x || knobMax / this.max * getWidth() <= x) {
				return false;
			}
		}
		
		final MultiSliderStyle style = getStyle();
		final Drawable knob = (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
		final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;

		float value;
		float oldPosition = getValue(index) * (vertical? getHeight():getWidth());
		float position = 0;

		final float min = getMinValue();
		final float max = getMaxValue();
		
		if (vertical) {
			float height = getHeight() - bg.getTopHeight() - bg.getBottomHeight();
			float knobHeight = knob == null ? 0 : knob.getMinHeight();
			position = y - bg.getBottomHeight() - knobHeight * 0.5f;
			value = min + (max - min) * visualInterpolationInverse.apply(position / (height - knobHeight));
			position = Math.max(max / this.max * height, position);
			position = Math.min(min / this.min * height - knobHeight, position);
		} else {
			float width = getWidth() - bg.getLeftWidth() - bg.getRightWidth();
			float knobWidth = knob == null ? 0 : knob.getMinWidth();
			position = x - bg.getLeftWidth() - knobWidth * 0.5f;
			value = min + (max - min) * visualInterpolationInverse.apply(position / (width - knobWidth));
			position = Math.max(max / this.max * width, position);
			position = Math.min(min / this.min * width - knobWidth, position);
		}

		float oldValue = value;
		//if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) value = snap(value);
		boolean valueSet = setValue(index, value);
		if (value == oldValue) position = oldPosition;
		return valueSet;
	}

	public void setStyle (MultiSliderStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		invalidateHierarchy();
	}

	/** Returns the progress bar's style. Modifying the returned style may not have an effect until
	 * {@link #setStyle(ProgressBarStyle)} is called. */
	public MultiSliderStyle getStyle () {
		return style;
	}

	@Override
	public void act (float delta) {
		super.act(delta);
		if (animateTime > 0) {
			animateTime -= delta;
			Stage stage = getStage();
			if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		MultiSliderStyle style = this.style;
		boolean disabled = this.disabled;
		final Drawable knob = (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
		final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;

		Color color = getColor();
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		float knobHeight = knob.getMinHeight();
		float knobWidth = knob == null ? 0 : knob.getMinWidth();
		float percent = 0;
		float position = 0;

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		if (vertical) {
			float positionHeight = height;

			float bgTopHeight = 0;
			if (bg != null) {
				if (round)
					bg.draw(batch, Math.round(x + (width - bg.getMinWidth()) * 0.5f), y, Math.round(bg.getMinWidth()), height);
				else
					bg.draw(batch, x + width - bg.getMinWidth() * 0.5f, y, bg.getMinWidth(), height);
				bgTopHeight = bg.getTopHeight();
				positionHeight -= bgTopHeight + bg.getBottomHeight();
			}
			
			for (int i = 0; i < knobsPositions.length; i++) {
				percent = getVisualPercent(i);
				if (knob == null) {
					position = positionHeight * percent;
					position = Math.min(positionHeight, knobsPositions[i]);
				} else {
					position = (positionHeight - knobHeight) * percent;
					position = Math.min(positionHeight - knobHeight, knobsPositions[i]) + bg.getBottomHeight();
				}
				position = Math.max(0, knobsPositions[i]);
				if (knob != null) {
					if (round) {
						knob.draw(batch, Math.round(x + (width - knobWidth) * 0.5f), Math.round(y + knobsPositions[i]), Math.round(knobWidth),
							Math.round(knobHeight));
					} else
						knob.draw(batch, x + (width - knobWidth) * 0.5f, y + knobsPositions[i], knobWidth, knobHeight);
				}
			}
		} else {
			float positionWidth = width;

			float bgLeftWidth = 0;
			if (bg != null) {
				if (round)
					bg.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
				else
					bg.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, width, bg.getMinHeight());
				bgLeftWidth = bg.getLeftWidth();
				positionWidth -= bgLeftWidth + bg.getRightWidth();
			}
			
			for (int i = 0; i < knobsPositions.length; i++) {
				percent = getVisualPercent(i);
				if (knob == null) {
					position = (positionWidth) * percent;
					position = Math.min(positionWidth, position);
				} else {
					position = (positionWidth - knobWidth) * percent;
					position = Math.min(positionWidth - knobWidth, position) + bgLeftWidth;
				}
				position = Math.max(0, position);
	
				if (knob != null) {
					if (round) {
						knob.draw(batch, Math.round(x + position), Math.round(y + (height - knobHeight) * 0.5f), Math.round(knobWidth),
							Math.round(knobHeight));
					} else
						knob.draw(batch, x + position, y + (height - knobHeight) * 0.5f, knobWidth, knobHeight);
				}
			}
		}
	}

	public float getValue (int index) {
		return knobsPositions[index];
	}
	
	public float[] getValues() {
		return knobsPositions;
	}
	
	public float getAnimateFromValue(int index) {
		return animateFromValues[index];
	}

	/** If {@link #setAnimateDuration(float) animating} the progress bar value, this returns the value current displayed. */
	public float getVisualValue (int index) {
		if (animateTime > 0) return animateInterpolation.apply(getAnimateFromValue(index), getValue(index), 1 - animateTime / animateDuration);
		return getValue(index);
	}

	public float getPercent (int index) {
		if (min == max) return 0;
		return (getValue(index) - min) / (max - min);
	}

	public float getVisualPercent (int index) {
		if (min == max) return 0;
		return visualInterpolation.apply((getVisualValue(index) - min) / (max - min));
	}

	/** Returns progress bar visual position within the range. */
	protected float[] getKnobPositions () {
		return this.knobsPositions;
	}

	/** Sets the progress bar position, rounded to the nearest step size and clamped to the minimum and maximum values.
	 * {@link #clamp(float)} can be overridden to allow values outside of the progress bar's min/max range.
	 * @return false if the value was not changed because the progress bar already had the value or it was canceled by a
	 *         listener. */
	public boolean setValue (int index, float value) {
		value = clamp(Math.round(value / stepSize) * stepSize);
		float oldValue = getValue(index);
		if (value == oldValue) return false;
		float oldVisualValue = getVisualValue(index);
		this.knobsPositions[index] = value;
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		boolean cancelled = fire(changeEvent);
		if (cancelled)
			this.knobsPositions[index] = oldValue;
		else if (animateDuration > 0) {
			animateFromValues[index] = oldVisualValue;
			animateTime = animateDuration;
		}
		Pools.free(changeEvent);
		return !cancelled;
	}

	/** Clamps the value to the progress bar's min/max range. This can be overridden to allow a range different from the progress
	 * bar knob's range. */
	protected float clamp (float value) {
		return MathUtils.clamp(value, min, max);
	}

	/** Sets the range of this progress bar. The progress bar's current value is clamped to the range. */
	public void setRange (float min, float max) {
		if (min > max) throw new IllegalArgumentException("min must be <= max: " + min + " <= " + max);
		this.min = min;
		this.max = max;
		for (int index = 0; index < getNumberOfKnobs(); index++) {
		if (getValue(index) < min)
			setValue(index, min);
		else if (getValue(index) > max) setValue(index, max);
		}
	}

	public void setStepSize (float stepSize) {
		if (stepSize <= 0) throw new IllegalArgumentException("steps must be > 0: " + stepSize);
		this.stepSize = stepSize;
	}

	public float getPrefWidth () {
		if (vertical) {
			final Drawable knob = (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
			final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
			return Math.max(knob == null ? 0 : knob.getMinWidth(), bg.getMinWidth());
		} else
			return 140;
	}

	public float getPrefHeight () {
		if (vertical)
			return 140;
		else {
			final Drawable knob = (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
			final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
			return Math.max(knob == null ? 0 : knob.getMinHeight(), bg == null ? 0 : bg.getMinHeight());
		}
	}
	
	public int getNumberOfKnobs() {
		return knobsPositions.length;
	}

	public float getMinValue () {
		return this.min;
	}
	
	public float getMaxValue () {
		return this.max;
	}

	public float getStepSize () {
		return this.stepSize;
	}

	/** If > 0, changes to the progress bar value via {@link #setValue(float)} will happen over this duration in seconds. */
	public void setAnimateDuration (float duration) {
		this.animateDuration = duration;
	}

	/** Sets the interpolation to use for {@link #setAnimateDuration(float)}. */
	public void setAnimateInterpolation (Interpolation animateInterpolation) {
		if (animateInterpolation == null) throw new IllegalArgumentException("animateInterpolation cannot be null.");
		this.animateInterpolation = animateInterpolation;
	}

	/** Sets the interpolation to use for display. */
	public void setVisualInterpolation (Interpolation interpolation) {
		this.visualInterpolation = interpolation;
	}

	/** If true (the default), inner Drawable positions and sizes are rounded to integers. */
	public void setRound (boolean round) {
		this.round = round;
	}

	public void setDisabled (boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled () {
		return disabled;
	}

	/** True if the progress bar is vertical, false if it is horizontal. **/
	public boolean isVertical () {
		return vertical;
	}

	/** The style for a multi knob slider, see {@link MultiSlider}.
	 * @author Leonardo La Rocca */
	static public class MultiSliderStyle {
		/** The progress bar background, stretched only in one direction. Optional. */
		public Drawable background;
		/** Optional. **/
		public Drawable disabledBackground;
		/** The knobs image, evenly distributed on the slider. */
		public Drawable knob, disabledKnob;
		
		public MultiSliderStyle () {
		}

		public MultiSliderStyle (Drawable background, Drawable knob) {
			this.background = background;
			this.knob = knob;
		}

		public MultiSliderStyle (MultiSliderStyle style) {
			this.background = style.background;
			this.disabledBackground = style.disabledBackground;
			this.knob = style.knob;
			this.disabledKnob = style.disabledKnob;
		}
		
		public MultiSliderStyle (SliderStyle style) {
			this.background = style.background;
			this.disabledBackground = style.disabledBackground;
			this.knob = style.knob;
			this.disabledKnob = style.disabledKnob;
		}
	}
}
